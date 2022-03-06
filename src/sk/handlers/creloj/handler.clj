(ns sk.handlers.creloj.handler
  (:require [sk.handlers.creloj.view
             :refer [registrados-view creloj-view creloj-js]]
            [sk.models.crud :refer [Update db]]
            [sk.models.util :refer [current_time_internal]]
            [cheshire.core :refer [generate-string]]
            [sk.layout
             :refer [application]]
            [sk.models.util :refer [get-session-id]]))

(defn registrados [_]
  (let [title "Corredores Registrados"
        ok (get-session-id)
        content (registrados-view)]
    (application title ok nil content)))

(defn contra-reloj [carrera_id]
  (creloj-view carrera_id)
  (let [title "CORREDORES REGISTRADOS"
        ok (get-session-id)
        js (creloj-js carrera_id)
        content (creloj-view carrera_id)]
    (application title ok js content)))

(defn contra-reloj-salida [id]
  (let [postvars {:salida (current_time_internal)}
        result (Update db :carreras postvars ["id = ?" id])]
    (if (seq result)
      (generate-string {:message "Procesado correctamente!"})
      (generate-string {:message "No se pudo asignar el tiempo de salida!"}))))

(defn contra-reloj-llegada [id]
  (let [postvars {:llegada (current_time_internal)}
        result (Update db :carreras postvars ["id = ?" id])]
    (if (seq result)
      (generate-string {:message "Procesado correctamente!"})
      (generate-string {:message "No se pudo asignar el tiempo de llegada!"}))))
