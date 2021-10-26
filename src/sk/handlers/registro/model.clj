(ns sk.handlers.registro.model
  (:require [sk.models.crud :refer [Query db]]))

(defn get-active-carreras []
  (let [sql "select * from carrera where activa = 'S'"
        rows (Query db sql)]
    rows))

(comment
  (get-active-carreras))
