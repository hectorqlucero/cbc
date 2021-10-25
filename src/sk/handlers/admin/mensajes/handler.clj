(ns sk.handlers.admin.mensajes.handler
  (:require [sk.handlers.admin.mensajes.view :refer [mensajes-scripts mensajes-view]]
            [sk.layout :refer [application]]
            [sk.models.crud :refer [build-form-delete build-form-row build-form-save]]
            [sk.models.grid :refer [build-grid]]
            [sk.models.util :refer [get-session-id user-level]]))

(defn mensajes
  [_]
  (let [title "Categorias"
        ok (get-session-id)
        js (mensajes-scripts)
        content (mensajes-view title)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Solo <strong>administradores></strong> pueden accesar esta opción!!!"))))

(defn mensajes-grid
  [{params :params}]
  (println "params: " params)
  (try
    (let [table "mensajes"
          carrera_id (:carrera_id params)
          args (if (nil? carrera_id)
                 {:sort-extra "id"}
                 {:sort-extra "id"
                  :search-extra (str "carrera_id = " carrera_id)})]
      (build-grid params table args))
    (catch Exception e (.getMessage e))))

(defn mensajes-form
  [id]
  (try
    (let [table "mensajes"]
      (build-form-row table id))
    (catch Exception e (.getMessage e))))

(defn mensajes-save
  [{params :params}]
  (try
    (let [table "mensajes"]
      (build-form-save params table))
    (catch Exception e (.getMessage e))))

(defn mensajes-delete
  [{params :params}]
  (try
    (let [table "mensajes"]
      (build-form-delete params table))
    (catch Exception e (.getMessage e))))
