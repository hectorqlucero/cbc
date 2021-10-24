(ns sk.proutes
  (:require [compojure.core :refer [GET POST defroutes]]
            [sk.handlers.admin.users.handler :as users]
            [sk.handlers.admin.eventos.handler :as eventos]))

(defroutes proutes
  ;; Start users
  (GET "/admin/users"  req [] (users/users req))
  (POST "/admin/users" req [] (users/users-grid req))
  (GET "/admin/users/edit/:id" [id] (users/users-form id))
  (POST "/admin/users/save" req [] (users/users-save req))
  (POST "/admin/users/delete" req [] (users/users-delete req))
  ;; End users

  ;; Start eventos
  (GET "/admin/eventos" req [] (eventos/eventos req))
  (POST "/admin/eventos" req [] (eventos/eventos-grid req))
  (GET "/admin/eventos/edit/:id" [id] (eventos/eventos-form id))
  (POST "/admin/eventos/save" req [] (eventos/eventos-save req))
  (POST "/admin/eventos/delete" req [] (eventos/eventos-delete req))
  ;; End eventos
  )
