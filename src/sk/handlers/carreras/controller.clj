(ns sk.handlers.carreras.controller
  (:require [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id]]
            [sk.handlers.carreras.model :refer [get-carreras]]
            [sk.handlers.carreras.view :refer [carreras-view]]))

(defn carreras [_]
  (let [title "Carreras"
        ok (get-session-id)
        js nil
        rows (get-carreras)
        content (carreras-view title rows)]
    (application title ok js content)))
