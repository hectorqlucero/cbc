(ns sk.routes
  (:require [cheshire.core :refer [generate-string]]
            [compojure.core :refer [GET POST defroutes]]
            [sk.handlers.home.handler :as home]
            [sk.handlers.registrar.handler :as registrar]
            [sk.handlers.registro.handler :as registro]
            [sk.handlers.tref.handler :as table_ref]
            [sk.handlers.eventos.handler :as eventos]))

(defroutes open-routes
  ;; Start table_ref
  (GET "/table_ref/get_users" [] (generate-string (table_ref/get-users)))
  (GET "/table_ref/validate_email/:email" [email] (generate-string (table_ref/get-users-email email)))
  (GET "/table_ref/months" [] (generate-string (table_ref/months)))
  (GET "/table_ref/years/:pyears/:nyears" [pyears nyears] (generate-string (table_ref/years pyears nyears)))
  (GET "/table_ref/get-item/:table/:field/:fname/:fval" [table field fname fval] (table_ref/get-item table field fname fval))
  (GET "/table_ref/get-time" [] (generate-string (table_ref/build-time)))
  (GET "/table_ref/levels" [] (generate-string (table_ref/level-options)))
  (GET "/table_ref/get-carrera-name/:carrera_id" [carrera_id] (table_ref/get-carrera-name carrera_id))
  (GET "/table_ref/get-categorias/:carrera_id" [carrera_id] (generate-string (table_ref/get-categorias carrera_id)))
  ;; End table_ref

  ;; Start home
  (GET "/" request [] (home/main request))
  (GET "/home/login" request [] (home/login request))
  (POST "/home/login" [username password] (home/login! username password))
  (GET "/home/logoff" [] (home/logoff))
  ;; End home

  ;; Start registrar
  (GET "/register" request [] (registrar/registrar request))
  (POST "/register" request [] (registrar/registrar! request))
  (GET "/rpaswd" request [] (registrar/reset-password request))
  (POST "/rpaswd" request [] (registrar/reset-password! request))
  (GET "/reset_password/:token" [token] (registrar/reset-jwt token))
  (POST "/reset_password" request [] (registrar/reset-jwt! request))
  ;; End registrar

  ;; Start eventos
  (GET "/eventos/list" request [] (eventos/eventos request))
  (GET "/eventos/list/:year/:month" [year month] (eventos/display-eventos year month))
  ;; End eventos

  ;; Start registro
  (GET "/registro" request [] (registro/registro request))
  (GET "/registrar/:carrera_id" [carrera_id] (registro/registrar carrera_id))
  (POST "/registrar/:carrera_id/save" request [] (registro/registrar-save request))
  ;; End registro
  )
