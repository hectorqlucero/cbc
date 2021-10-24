(ns sk.handlers.admin.eventos.handler
  (:require [sk.handlers.admin.eventos.view
             :refer
             [eventos-scripts eventos-view]]
            [sk.layout :refer [application]]
            [sk.models.crud
             :refer
             [build-form-delete build-form-row build-form-save]]
            [sk.models.grid :refer [build-grid]]
            [sk.models.util :refer [current_year get-session-id]]))

(defn eventos
  [_]
  (let [title   "Calendario - Eventos"
        ok      (get-session-id)
        js      (eventos-scripts)
        content (eventos-view title)]
    (application title ok js content)))

(defn eventos-grid
  [{params :params}]
  (let [table "eventos"
        args  {:sort-extra   "fecha desc,hora"
               :search-extra (str "YEAR(fecha) = '" (current_year) "'")}]
    (build-grid params table args)))

(defn eventos-form
  [id]
  (let [table "eventos"]
    (build-form-row table id)))

(defn eventos-save
  [{params :params}]
  (let [table         "eventos"]
    (build-form-save params table)))

(defn eventos-delete
  [{params :params}]
  (let [table "eventos"]
    (build-form-delete params table)))

(comment
  (eventos-grid {}))
