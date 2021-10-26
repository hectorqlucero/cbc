(ns sk.handlers.registro.handler
  (:require [sk.models.util :refer [get-session-id]]
            [sk.layout :refer [application]]
            [sk.handlers.registro.view :refer [registro-view]]))

(defn registro [_]
  (let [title "Registro de Paseos/Carreras"
        ok (get-session-id)
        content (registro-view)]
    (application title ok nil content)))
