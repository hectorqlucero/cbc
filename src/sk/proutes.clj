(ns sk.proutes
  (:require [compojure.core :refer [GET POST defroutes]]
            [cheshire.core :refer (generate-string)]
            [sk.handlers.admin.users.handler :as users]
            [sk.handlers.admin.eventos.handler :as eventos]
            [sk.handlers.admin.categorias.handler :as categorias]
            [sk.handlers.admin.carrera.handler :as carrera]))

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

  ;; Start categorias
  (GET "/admin/categorias"  req [] (categorias/categorias req))
  (POST "/admin/categorias" req [] (categorias/categorias-grid req))
  (GET "/admin/categorias/edit/:id" [id] (categorias/categorias-form id))
  (POST "/admin/categorias/save" req [] (categorias/categorias-save req))
  (POST "/admin/categorias/delete" req [] (categorias/categorias-delete req))
  ;; End categorias

  ;; Start carrera
  (GET "/admin/carrera/carreras" [] (generate-string (carrera/carreras)))
  (POST "/admin/carrera/categorias/:id" [id] (carrera/categorias id))
  (POST "/admin/carrera/categoria/save" req [] (carrera/categorias-save req))
  (GET "/admin/carrera"  req [] (carrera/carrera req))
  (POST "/admin/carrera" req [] (carrera/carrera-grid req))
  (GET "/admin/carrera/edit/:id" [id] (carrera/carrera-form id))
  (POST "/admin/carrera/save" req [] (carrera/carrera-save req))
  (POST "/admin/carrera/delete" req [] (carrera/carrera-delete req))
  ;; End carrera
  )
