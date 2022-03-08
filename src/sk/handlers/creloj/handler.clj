(ns sk.handlers.creloj.handler
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as st]
            [sk.handlers.creloj.view
             :refer [registrados-view creloj-view creloj-js seconds->duration]]
            [sk.handlers.creloj.model :refer [get-oregistered]]
            [sk.models.crud :refer [Update db]]
            [sk.models.util :refer [current_time_internal parse-int]]
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

(defn contra-reloj-salida-cambiar [id v]
  (let [postvars {:salida v}
        result (Update db :carreras postvars ["id = ?" id])]
    (if (seq result)
      (generate-string {:message "Procesado correctamente!"})
      (generate-string {:message "No se pudo asignar el tiempo de salida!"}))))

(defn contra-reloj-llegada-cambiar [id v]
  (let [postvars {:llegada v}
        result (Update db :carreras postvars ["id = ?" id])]
    (if (seq result)
      (generate-string {:message "Procesado correctamente!"})
      (generate-string {:message "No se pudo asignar el tiempo de llegada!"}))))

(defn remove-non-printable-characters [x]
  (if-not (nil? x)
    (st/replace x #"[\p{C}&&^(\S)]" "")
    x))

(defn process-csv [row]
  (let [segundos (parse-int (:tiempo row))
        tiempo (if-not (nil? segundos) (seconds->duration segundos) nil)]
    [(remove-non-printable-characters (:nombre row))
     (remove-non-printable-characters (:apell_paterno row))
     (remove-non-printable-characters (:apell_materno row))
     (or (:categoria row) "  ")
     (or (:numero_asignado row) "  ")
     (or (:hora_salida row) "  ")
     (or (:hora_llegada row) "  ")
     (or tiempo " ")]))

(defn generate-csv [carrera_id]
  (let [rows (get-oregistered carrera_id)
        heads ["NOMBRE" "PATERNO" "MATERNO" "CATEGORIA" "NUMERO ASIGNADO" "SALIDA" "LLEGADA" "TIEMPO"]
        data (vec (cons heads (map process-csv rows)))
        filename (str "contra_reloj_" carrera_id ".csv")]
    (with-open [writer (io/writer filename)]
      (csv/write-csv writer data))
    {:status 200
     :headers {"Content-Type" "text/csv"
               "Content-Disposition" (str "attachment; filename=" filename)}
     :body (slurp filename)}))

(comment
  (remove-non-printable-characters nil)
  (get-oregistered 5)
  (generate-csv 5))
