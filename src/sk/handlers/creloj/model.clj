(ns sk.handlers.creloj.model
  (:require [sk.models.crud :refer [Query Query! Update db]]
            [sk.models.util :refer [seconds->duration current_time_internal]]))

(defn actualizar-salida-general
  [hora carrera-id]
  (Update db :carreras {:salida hora} ["carrera_id = ?" carrera-id]))

(defn get-active-carrera []
  (:id (first (Query db "select id from carrera where activa='S'"))))

(def registered-sql
  "
   select
   id,
   nombre,
   apell_paterno,
   apell_materno,
   numero_asignado,
   categoria_id,
   TIME_FORMAT(salida,'%H:%i:%s') as hora_salida,
   TIME_FORMAT(llegada,'%H:%i:%s') as hora_llegada,
   salida,
   llegada,
   ABS(TIMESTAMPDIFF(SECOND,llegada,salida)) as tiempo
   from carreras 
   where NULLIF(numero_asignado,' ') IS NOT NULL 
   and NULLIF(numero_asignado,'0') IS NOT NULL
   AND carrera_id = ?
   order by
   categoria_id,
   tiempo,
   nombre,
   apell_paterno,
   apell_materno
   ")

(defn get-registered [carrera_id]
  (Query db [registered-sql carrera_id]))

(defn get-categoria [categoria-id]
  (let [categoria (:descripcion (first (Query db ["select descripcion from categorias where id = ?" categoria-id])))]
    categoria))

(defn get-oregistered [carrera-id]
  (let [rows (Query db [registered-sql carrera-id])]
    (map (fn [row]
           (let [categoria (get-categoria (:categoria_id row))]
             (assoc row :categoria categoria))) rows)))

(defn get-carrera-name [carrera_id]
  (:descripcion (first (Query db ["select descripcion from carrera where id = ?" carrera_id]))))

(defn get-active-carrera-name [carrera_id]
  (let [carrera-name (get-carrera-name carrera_id)]
    carrera-name))

(def register-row-sql
  "
   select
   carreras.id,
   carreras.nombre,
   carreras.apell_paterno,
   carreras.apell_materno,
   carreras.pais,
   carreras.ciudad,
   carreras.telefono,
   carreras.email,
   carreras.sexo,
   DATE_FORMAT(carreras.fecha_nacimiento, '%d/%m/%Y') as fecha_nacimiento,
   carreras.direccion,
   carreras.club,
   carreras.carrera_id,
   carreras.categoria_id,
   DATE_FORMAT(carreras.last_updated, '%d/%m/%Y') as date,
   carrera.p1,
   carrera.p2,
   carrera.p3,
   carrera.p4,
   carrera.d1,
   carrera.d2,
   carrera.descripcion as carrera,
   categorias.descripcion as categoria
   from carreras
   left join carrera on carreras.carrera_id = carrera.id
   left join categorias on carreras.categoria_id = categorias.id
   where carreras.id = ?
   ")

(defn get-register-row [carrera_id]
  (first (Query db [register-row-sql carrera_id])))

;; Start limpiar
(def limpiar-sql
  "
  UPDATE carreras
  SET salida = NULL, llegada = NULL
  WHERE
  carrera_id = ?
  ")

(defn limpiar [carrera-id]
  (Query! db [limpiar-sql carrera-id]))

(defn limpiar-numero-asignado [carrera-id]
  (Query! db ["UPDATE carreras SET numero_asignado = NULL WHERE carrera_id = ?" carrera-id]))
;; End limpiar

;; Start limpiar rfid
(defn limpiar-rfid
  []
  (Query! db "TRUNCATE TABLE rfid"))
;; End limpiar rfid

;; Start get-carreras-row
(def get-carreras-row-sql
  "
  SELECT *
  FROM carreras
  WHERE carrera_id = ? and numero_asignado = ?
  ")

(defn get-carreras-row [carrera_id numero_asignado]
  (let [row (first (Query db [get-carreras-row-sql carrera_id numero_asignado]))]
    row))
;; End get-carreras-row

;; Start corredores-salidas
(defn corredores-salidas
  [carrera_id rfid]
  (let [numero_asignado (->> (Query db ["select numero from rfid where rfid = ?" rfid])
                             (first)
                             (:numero))
        carrera-id (->> (Query db ["select id from carreras where COALESCE(NULLIF(salida,''),'') = '' and numero_asignado = ? and carrera_id = ?" numero_asignado carrera_id])
                        (first)
                        (:id))
        row {:salida (current_time_internal)}]
    (when (integer? carrera-id)
      (Update db :carreras row ["id = ?" carrera-id]))))
;; End corredores-salidas

;; Start corredores-llegadas
(defn corredores-llegadas
  [carrera_id rfid]
  (let [numero_asignado (->> (Query db ["select numero from rfid where rfid = ?" rfid])
                             (first)
                             (:numero))
        carrera-id (->> (Query db ["select id from carreras where COALESCE(NULLIF(llegada,''),'') = '' and numero_asignado = ? and carrera_id = ?" numero_asignado carrera_id])
                        (first)
                        (:id))
        row {:llegada (current_time_internal)}]
    (when (integer? carrera-id)
      (Update db :carreras row ["id = ?" carrera-id]))))
;; End corredores-llegadas

(defn carreras-options
  []
  (let [rows (Query db ["select id as value,descripcion as label from carrera order by descripcion"])]
    (list* {:value ""
            :label "Seleccionar carrera!"}
           rows)))

(def get-carreras-by-id-sql
  (str
   "select
    numero_asignado,
    categorias.descripcion as categoria,
    concat(ifnull(carreras.nombre,''),' ',ifnull(carreras.apell_paterno,''),' ',ifnull(carreras.apell_materno,'')) as corredor,
    TIME_FORMAT(carreras.salida,'%h:%i:%s %p') as salida,
    TIME_FORMAT(carreras.llegada,'%h:%i:%s %p') as llegada,
    ABS(TIMESTAMPDIFF(SECOND,carreras.llegada,carreras.salida)) as tiempo
    from carreras
    join categorias on categorias.id = carreras.categoria_id
    where carreras.carrera_id = ?
    and carreras.salida is not null
    and carreras.salida != '00:00:00'
    and carreras.numero_asignado is not null
    and carreras.numero_asignado != ''
    and carreras.numero_asignado != ' '
    and carreras.numero_asignado != '0'
    order by tiempo,CAST(carreras.numero_asignado AS UNSIGNED), carreras.numero_asignado
    "))

(defn get-carreras-by-id [carrera-id]
  (let [rows (Query db [get-carreras-by-id-sql carrera-id])]
    (map (fn [row]
           (let [tiempo (:tiempo row)]
             (assoc row :tiempo (seconds->duration tiempo)))) rows)))

(comment
  (get-carreras-auto-row 16 "E2806894000040170C2224A4")
  (get-carreras-by-id 16)
  (carreras-options)
  (process-lector (get-lector))
  (get-lector)
  (get-carreras-row 5 6)
  (limpiar 5)
  (int (Math/floor (/ 3 2)))
  (get-carrera-name 1)
  (get-active-carrera-name 5)
  (get-registered 5)
  (get-oregistered 5)
  (get-categoria 20)
  (get-register-row 1))
