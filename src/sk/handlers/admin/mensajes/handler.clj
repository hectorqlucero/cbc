(ns sk.handlers.admin.mensajes.handler
  (:require [sk.handlers.admin.mensajes.view :refer [mensajes-scripts mensajes-view]]
            [sk.layout :refer [application]]
            [sk.models.crud :refer [build-form-delete build-form-row build-form-save]]
            [sk.models.grid :refer [build-grid]]
            [sk.models.util :refer [get-session-id]]))

(defn mensajes
  [_]
  (try
    (let [title "Mensajes"
          ok (get-session-id)
          js (mensajes-scripts)
          content (mensajes-view title)]
      (application title ok js content))
    (catch Exception e (.getMessage e))))

(defn mensajes-grid
  [{params :params}]
  (try
    (let [table "mensajes"]
      (build-grid params table))
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
