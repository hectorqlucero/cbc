(ns sk.handlers.carreras.view
  (:require [sk.models.grid :refer [build-dashboard]]))

(defn carreras-view
  [title rows]
  (let [table-id "carreras_table"
        labels ["NOMBRE" "CIUDAD" "EMAIL" "#" "CATEGORIA" "SALIDA" "LLEGADA" "TIEMPO"]
        db-fields [:nombre :ciudad :email :numero_asignado :categoria :salida :llegada :tiempo]
        fields (zipmap db-fields labels)]
    (build-dashboard title rows table-id fields)))
