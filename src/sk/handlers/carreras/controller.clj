(ns sk.handlers.carreras.controller
  (:require [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id]]
            [sk.handlers.carreras.model :refer [get-carrera-name
                                                get-active-carrera
                                                get-carreras
                                                get-carreras-by-id]]
            [sk.handlers.carreras.view :refer [carreras-view]]))

(defn carreras [_]
  (let [title (get-carrera-name (get-active-carrera))
        ok (get-session-id)
        js nil
        rows (get-carreras)
        content (carreras-view title rows)]
    (application title ok js content)))

(defn reporte [carreras-id]
  (let [title (get-carrera-name carreras-id)
        ok (get-session-id)
        js nil
        rows (get-carreras-by-id carreras-id)
        content (carreras-view title rows)]
    (application title ok js content)))
