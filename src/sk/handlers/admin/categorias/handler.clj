(ns sk.handlers.admin.categorias.handler
  (:require [sk.handlers.admin.categorias.view :refer [categorias-scripts categorias-view]]
            [sk.layout :refer [application]]
            [sk.models.crud :refer [build-form-delete build-form-row build-form-save]]
            [sk.models.grid :refer [build-grid]]
            [sk.models.util :refer [get-session-id user-level]]))

(defn categorias
  [_]
  (let [title "Categorias"
        ok (get-session-id)
        js (categorias-scripts)
        content (categorias-view title)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Solo <strong>administradores></strong> pueden accesar esta opción!!!"))))

(defn categorias-grid
  [{params :params}]
  (try
    (let [table "categorias"
          args {:sort-extra "id"}]
      (build-grid params table args))
    (catch Exception e (.getMessage e))))

(defn categorias-form
  [id]
  (try
    (let [table "categorias"]
      (build-form-row table id))
    (catch Exception e (.getMessage e))))

(defn categorias-save
  [{params :params}]
  (try
    (let [table "categorias"]
      (build-form-save params table))
    (catch Exception e (.getMessage e))))

(defn categorias-delete
  [{params :params}]
  (try
    (let [table "categorias"]
      (build-form-delete params table))
    (catch Exception e (.getMessage e))))

(comment
  (categorias {}))
