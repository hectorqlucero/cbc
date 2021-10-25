(ns sk.handlers.admin.carrera.handler
  (:require [sk.handlers.admin.carrera.view :refer [carrera-scripts carrera-view]]
            [sk.layout :refer [application]]
            [sk.models.crud :refer [Query db build-form-delete build-form-row build-form-save]]
            [sk.models.grid :refer [build-grid]]
            [sk.models.util :refer [get-session-id user-level]]))

(defn carrera
  [_]
  (try
    (let [title "Carreras Definición"
          ok (get-session-id)
          js (carrera-scripts)
          content (carrera-view title)]
      (if
       (or
        (= (user-level) "A")
        (= (user-level) "S"))
        (application title ok js content)
        (application title ok nil "Solo <strong>Administradores</strong> pueden accesar esta opción!!!")))
    (catch Exception e (.getMessage e))))

(defn carrera-grid
  [{params :params}]
  (try
    (let [table "carrera"
          args {:sort-extra "id"}]
      (build-grid params table args))
    (catch Exception e (.getMessage e))))

(defn carrera-form
  [id]
  (try
    (let [table "carrera"]
      (build-form-row table id))
    (catch Exception e (.getMessage e))))

(defn carrera-save
  [{params :params}]
  (try
    (let [table "carrera"]
      (build-form-save params table))
    (catch Exception e (.getMessage e))))

(defn carrera-delete
  [{params :params}]
  (try
    (let [table "carrera"]
      (build-form-delete params table))
    (catch Exception e (.getMessage e))))

(defn categorias [id]
  (let [table "categorias"
        args {:search-extra (str "carrera_id = " id)}]
    (build-grid {} table args)))

(defn carreras []
  (let [table "carrera"
        sql "select id AS value, descripcion AS text FROM carrera ORDER BY id"
        rows (Query db sql)]
    rows))

(defn categorias-save
  [{params :params}]
  (println "params: " params)
  (try
    (let [table "categorias"]
      (build-form-save params table))
    (catch Exception e (.getMessage e))))

(comment
  (build-grid {} "categorias" {:search-extra "carrera_id = 1"})
  (carrera-grid {})
  (carreras)
  (categorias 1))
