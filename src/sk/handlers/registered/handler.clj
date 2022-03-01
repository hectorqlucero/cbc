(ns sk.handlers.registered.handler
  (:require [sk.handlers.registered.view
             :refer [registered-view
                     registrados-view
                     oregistered-view
                     oregistrados-view
                     registered-pdf
                     registered-js]]
            [sk.layout :refer [application]]
            [cheshire.core :refer [generate-string]]
            [sk.models.crud :refer [Update db]]
            [sk.models.util :refer [get-session-id]]))

(defn registrados [_]
  (let [title "Corredores Registrados"
        ok (get-session-id)
        content (registrados-view)]
    (application title ok nil content)))

(defn registered [carrera_id]
  (let [title "CORREDORES REGISTRADOS"
        ok (get-session-id)
        js (registered-js)
        content (registered-view carrera_id)]
    (application title ok js content)))

(defn oregistrados [_]
  (let [title "Corredores Registrados"
        ok (get-session-id)
        content (oregistrados-view)]
    (application title ok nil content)))

(defn oregistered [carrera_id]
  (let [title "CORREDORES REGISTRADOS"
        ok (get-session-id)
        js nil
        content (oregistered-view carrera_id)]
    (application title ok js content)))

(defn imprimir [id]
  (registered-pdf id))

(defn update-db [id numero-asignado]
  (let [postvars {:numero_asignado numero-asignado}
        result (Update db :carreras postvars ["id = ?" id])]
    (if (seq result)
      (generate-string {:message "Procesado correctamente!"})
      (generate-string {:message "No se pudo asignar el numero!"}))))

(comment
  (registered-view 1)
  (registrados))
