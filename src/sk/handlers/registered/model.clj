(ns sk.handlers.registered.model
  (:require [sk.models.crud :refer [Query db]]))

(defn get-active-carrera []
  (:id (first (Query db "select id from carrera where activa='S'"))))

(def registered-sql
  "
   select * 
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
   ABS(TIMESTAMPDIFF(SECOND,llegada,salida)) as tiempo
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

(comment
  (get-carrera-name 1)
  (get-active-carrera-name 5)
  (get-registered 5)
  (get-oregistered 5)
  (get-categoria 20)
  (get-register-row 1))
