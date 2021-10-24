(ns sk.models.cdb
  (:require [noir.util.crypt :as crypt]
            [sk.models.crud :refer [Insert-multi Query! db]]))

;; Start users table
(def users-sql
  "CREATE TABLE users (
  id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  lastname varchar(45) DEFAULT NULL,
  firstname varchar(45) DEFAULT NULL,
  username varchar(45) DEFAULT NULL,
  password TEXT DEFAULT NULL,
  dob date DEFAULT NULL,
  cell varchar(45) DEFAULT NULL,
  phone varchar(45) DEFAULT NULL,fax varchar(45) DEFAULT NULL,
  email varchar(100) DEFAULT NULL,
  level char(1) DEFAULT NULL COMMENT 'A=Administrator,U=User,S=System',
  active char(1) DEFAULT NULL COMMENT 'T=Active,F=Not active',
  imagen varchar(200) DEFAULT NULL,
  UNIQUE KEY username (username)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8")

(def users-rows
  [{:lastname  "User"
    :firstname "Regular"
    :username  "user@gmail.com"
    :password  (crypt/encrypt "user")
    :dob       "1957-02-07"
    :email     "user@gmail.com"
    :level     "U"
    :active    "T"}
   {:lastname "User"
    :firstname "Admin"
    :username "admin@gmail.com"
    :password (crypt/encrypt "admin")
    :dob "1957-02-07"
    :email "admin@gmail.com"
    :level "S"
    :active "T"}])
;; End users table

;; Start eventos
(def eventos-sql
  "
  CREATE TABLE `eventos` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `titulo` varchar(200) DEFAULT NULL,
  `detalles` text,
  `lugar` text,
  `fecha` date DEFAULT NULL,
  `hora` time DEFAULT NULL,
  `organiza` varchar(100) DEFAULT NULL,
  `imagen` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 ")
;; End eventos

(defn drop-tables
  "Drops tables if they exist"
  []
  (Query! db "DROP table IF EXISTS users")
  (Query! db "DROP table IF EXISTS eventos"))

(defn create-tables
  "Creates tables"
  []
  (Query! db users-sql)
  (Query! db eventos-sql))

(defn populate-tables
  "Populates table with default data"
  []
  (Query! db "LOCK TABLES users WRITE;")
  (Insert-multi db :users users-rows)
  (Query! db "UNLOCK TABLES;"))

(defn reset-database
  "Removes existing tables and re-creates them"
  []
  (drop-tables)
  (create-tables)
  (populate-tables))

(comment
  (reset-database))
