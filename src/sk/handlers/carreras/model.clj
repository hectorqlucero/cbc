(ns sk.handlers.carreras.model
  (:require [sk.models.crud :refer [Query db]]
            [sk.models.util :refer [seconds->duration]]))

(defn get-active-carrera
  []
  (-> (Query db "select id from carrera where activa = 'S'")
      first
      :id))

(def get-carreras-sql
  (str
   "
   SELECT
   carreras.id,
   carreras.nombre,
   carreras.ciudad,
   carreras.email,
   carreras.numero_asignado,
   categorias.descripcion as categoria,
   TIME_FORMAT(carreras.salida,'%H:%i:%s') as salida,
   TIME_FORMAT(carreras.llegada,'%H:%i:%s') as llegada,
   ABS(TIMESTAMPDIFF(SECOND,carreras.llegada,carreras.salida)) as tiempo
   FROM carreras
   join categorias on categorias.id = carreras.categoria_id
   WHERE
   carreras.carrera_id = ?
   ORDER BY
   categorias.descripcion,
   tiempo,
   carreras.nombre,
   carreras.numero_asignado
"))

(defn get-carreras
  []
  (let [data (Query db [get-carreras-sql (get-active-carrera)])
        rows (map #(assoc % :tiempo (seconds->duration (:tiempo %))) data)]
    rows))

(defn get-carreras-by-id
  [id]
  (let [data (Query db [get-carreras-sql id])
        rows (map #(assoc % :tiempo (seconds->duration (:tiempo %))) data)]
    rows))

(def get-carreras-id-sql
  (str
   "
SELECT *
FROM carreras
WHERE id = ?
"))

(defn get-carreras-id
  [id]
  (first (Query db [get-carreras-id-sql id])))

(comment
  (get-carreras-by-id 14)
  (get-carreras))
