(ns sk.handlers.admin.eventos.model
  (:require [sk.models.crud :refer [Query db]]
            [sk.models.util :refer [current_year]]
            [sk.migrations :refer [config]]
            [clojure.string :as st])
  (:import [java.util Calendar UUID]))

(def get-eventos-sql
  (str
   "
SELECT *
FROM eventos
WHERE YEAR(fecha) >= " (current_year) "
"))

(defn get-eventos
  []
  (let [uuid (str (UUID/randomUUID))
        trows (Query db get-eventos-sql)
        rows (map (fn [row]
                    (let [img-url (str (:path config) (:imagen row) "?" uuid)
                          img-url (str "<img src=" \" img-url "\" width='95' height='71' alt='evento'>")]
                      (assoc row :imagen img-url))) trows)]
    rows))

(def get-eventos-id-sql
  (str
   "
SELECT *
FROM eventos
WHERE id = ?
"))

(defn get-eventos-id
  [id]
  (first (Query db [get-eventos-id-sql id])))

(comment
  (get-eventos)
  (current_year))
