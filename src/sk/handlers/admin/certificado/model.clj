(ns sk.handlers.admin.certificado.model
  (:require [sk.models.crud :refer [Query db]]
            [sk.models.util :refer [image-link]]
            [clojure.string :as st]))

(def get-certificado-sql
  (str
   "
SELECT certificado.*,carrera.descripcion as carrera_formatted
FROM certificado
   join carrera on carrera.id = certificado.carrera_id
"))

(defn get-certificado
  []
  (->> (Query db get-certificado-sql)
       (map #(assoc % :image (image-link (:imagen %))))))

(def get-certificado-id-sql
  (str
   "
SELECT *
FROM certificado
WHERE id = ?
"))

(defn get-certificado-id
  [id]
  (first (Query db [get-certificado-id-sql id])))

(defn carrera-options
  []
  (Query db ["select id as value,descripcion as label from carrera order by id"]))

(comment
  (carrera-options))
