(ns sk.handlers.registered.model
  (:require [sk.models.crud :refer [Query db]]
            [sk.migrations :refer [config]]
            [clojure.java.io :as io]
            [clj.qrgen :refer [as-file from]]
            [sk.models.util :refer [seconds->duration]])
  (:import java.text.SimpleDateFormat
           [java.util Calendar UUID]))

(defn get-active-carrera []
  (:id (first (Query db "select id from carrera where activa='S'"))))

(def registered-sql
  "
  select *,
  ABS(TIMESTAMPDIFF(SECOND,llegada,salida)) as tiempo
  from carreras 
  where carrera_id = ?
  order by
  categoria_id,
  nombre,
  apell_paterno,
  apell_materno
  ")

(def oregistered-sql
  "
   select
   id,
   nombre,
   apell_paterno,
   apell_materno,
   pais,
   ciudad,
   telefono,
   email,
   sexo,
   fecha_nacimiento,
   direccion,
   club,
   numero_asignado,
   categoria_id,
   TIME_FORMAT(salida,'%H:%i:%s') as hora_salida,
   TIME_FORMAT(llegada,'%H:%i:%s') as hora_llegada,
   salida,
   llegada,
   TIMESTAMPDIFF(SECOND,salida,llegada) as tiempo
   from carreras 
   where carrera_id = ?
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
  (let [rows (Query db [oregistered-sql carrera-id])]
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
   TIMESTAMPDIFF(SECOND,carreras.salida,carreras.llegada) as tiempo,
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

;; Start QR
(def temp-dir
  (let [dir (str (System/getProperty "java.io.tmpdir") "/barcodes/")]
    (.mkdir (io/file dir))
    dir))

(defn generate-barcode [id]
  (let [barcode-body (str (config :base-url) "update/number/" (str id))
        barcode (as-file (from barcode-body) (str (str  id) ".png"))]
    barcode))

(defn copy-file [source-path dest-path]
  (io/copy (io/file source-path) (io/file dest-path)))

(defn create-barcode [id]
  (let [uuid (str (UUID/randomUUID))]
    (copy-file (generate-barcode id) (str temp-dir id ".png"))
    (str (config :barcode-url) id ".png")))

(defn get-carreras [carrera-id]
  (first (Query db ["select * from carreras where id = ?" carrera-id])))
;; End QR

(defn get-active-carrera-id []
  (:id (first (Query db ["select id from carrera where activa = 'S'"]))))

; Start get-corredor-by-numero
(def get-corredor-by-numero-sql
  "
  select *
  from carreras
  where
  carrera_id = ?
  and numero_asignado = ?
  ")

(defn get-corredor-by-numero [carrera-id numero]
  (first (Query db [get-corredor-by-numero-sql carrera-id numero])))
;; End get-corredor-by-numero

(defn tiempo-para-evento
  "Get time remaining to event"
  [eventos-id]
  (let [row (-> (Query db ["select TIMESTAMPDIFF(SECOND,CURRENT_TIMESTAMP(),TIMESTAMP(fecha,hora)) as tiempo from eventos where id = ?" eventos-id])
                first)
        result (seconds->duration (abs (:tiempo row)))]
    result))

(def get-corredores-categorias-sql
  (str
   "
    select distinct
    carreras.categoria_id as value,
    categorias.descripcion as label
    from carreras
    join categorias on categorias.id = carreras.categoria_id
    where carreras.carrera_id = ?
    "))
(Query db [get-corredores-categorias-sql 14])
(defn get-corredores-categorias [carrera-id]
  (let [rows (Query db [get-corredores-categorias-sql carrera-id])
        options (map (fn [row] {:value (:value row) :label (:label row)}) rows)]
    (list* {:value "" :label "Seleccionar categoría..."} options)))

(def get-corredores-by-categoria-sql
  "
   select
   id,
   nombre,
   apell_paterno,
   apell_materno,
   pais,
   ciudad,
   telefono,
   email,
   sexo,
   fecha_nacimiento,
   direccion,
   club,
   numero_asignado,
   categoria_id,
   TIME_FORMAT(salida,'%H:%i:%s') as hora_salida,
   TIME_FORMAT(llegada,'%H:%i:%s') as hora_llegada,
   salida,
   llegada,
   TIMESTAMPDIFF(SECOND,salida,llegada) as tiempo
   from carreras 
   where carrera_id = ?
   and categoria_id = ?
   order by
   nombre,
   apell_paterno,
   apell_materno
   ")

(defn get-corredores-by-categoria [carrera-id categoria-id]
  (let [rows (Query db [get-corredores-by-categoria-sql carrera-id categoria-id])]
    (map (fn [row]
           (let [categoria (get-categoria (:categoria_id row))]
             (assoc row :categoria categoria))) rows)))

(comment
  (get-corredores-by-categoria 14 69)
  (get-corredores-categorias (get-active-carrera-id))
  (tiempo-para-evento 83)
  (get-corredor-by-numero (get-active-carrera-id) 704)
  (get-active-carrera-id)
  (get-carreras 415)
  (generate-barcode 175)
  (create-barcode 175)
  (get-carrera-name 1)
  (get-active-carrera-name 5)
  (get-registered 5)
  (get-oregistered 5)
  (get-categoria 20)
  (get-register-row 415))
