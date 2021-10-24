(ns sk.handlers.eventos.handler
  (:require [sk.handlers.eventos.sql :refer [eventos-sql]]
            [sk.handlers.eventos.view
             :refer
             [display-eventos-scripts display-eventos-view eventos-scripts eventos-view]]
            [sk.layout :refer [application]]
            [sk.models.crud :refer [Query config db]]
            [sk.models.util
             :refer
             [current_year get-month-name get-session-id parse-int zpl]]))

(defn eventos [_]
  (let [title   "Eventos"
        ok      (get-session-id)
        js      (eventos-scripts)
        content (eventos-view title (current_year))]
    (application title ok js content)))

(defn display-eventos [year month]
  (let [title   (get-month-name (parse-int month))
        ok      (get-session-id)
        js      (display-eventos-scripts year month)
        rows    (Query db [eventos-sql year month])
        rows    (map #(assoc % :day (zpl (% :day) 2)) rows)
        content (display-eventos-view title year month rows (str (config :path) "eventos/"))]
    (application title ok js content)))
