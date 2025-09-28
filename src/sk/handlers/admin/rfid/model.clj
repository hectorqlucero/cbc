(ns sk.handlers.admin.rfid.model
  (:require [sk.models.crud :refer [Query db]]
            [clojure.string :as st]))

(def get-rfid-sql
  (str
   "
SELECT *
FROM rfid
"))

(defn get-rfid
  []
  (Query db get-rfid-sql))

(def get-rfid-id-sql
  (str
   "
SELECT *
FROM rfid
WHERE id = ?
"))

(defn get-rfid-id
  [id]
  (first (Query db [get-rfid-id-sql id])))
