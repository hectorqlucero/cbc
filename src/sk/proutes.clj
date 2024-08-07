(ns sk.proutes
  (:require [compojure.core :refer [GET POST defroutes]]
            [cheshire.core :refer (generate-string)]
            [sk.handlers.admin.users.handler :as users]
            [sk.handlers.admin.eventos.handler :as eventos]
            [sk.handlers.admin.categorias.handler :as categorias]
            [sk.handlers.admin.carrera.handler :as carrera]
            [sk.handlers.registered.handler :as registered]
            [sk.handlers.admin.mensajes.handler :as mensajes]
            [sk.handlers.creloj.handler :as creloj]))

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

  ;; Start mensajes
  (GET "/admin/mensajes"  req [] (mensajes/mensajes req))
  (POST "/admin/mensajes" req [] (mensajes/mensajes-grid req))
  (GET "/admin/mensajes/edit/:id" [id] (mensajes/mensajes-form id))
  (POST "/admin/mensajes/save" req [] (mensajes/mensajes-save req))
  (POST "/admin/mensajes/delete" req [] (mensajes/mensajes-delete req))
  ;; End mensajes

  ;; Start display registered
  (GET "/display/registered" req [] (registered/registrados req))
  (GET "/display/registered/:carrera_id" [carrera_id] (registered/registered carrera_id))
  (GET "/imprimir/registered/:id" [id] (registered/imprimir id))
  (GET "/update/registered/:id/:no" [id no] (registered/update-db id no))
  ;; End display registered

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

  ;; Start creloj
  (GET "/display/creloj" req [] (creloj/registrados req))
  (GET "/display/creloj/:carrera_id" [carrera_id] (creloj/contra-reloj carrera_id))
  (GET "/display/salidas/:carrera_id" [carrera_id] (creloj/salidas carrera_id))
  (GET "/display/llegadas/:carrera_id" [carrera_id] (creloj/llegadas carrera_id))
  (GET "/update/salida/:id" [id] (creloj/contra-reloj-salida id))
  (GET "/update/llegada/:id" [id] (creloj/contra-reloj-llegada id))
  (GET "/change/salida/:id/:v" [id v] (creloj/contra-reloj-salida-cambiar id v))
  (GET "/change/llegada/:id/:v" [id v] (creloj/contra-reloj-llegada-cambiar id v))
  (GET "/creloj/csv/:carrera_id" [carrera_id] (creloj/generate-csv carrera_id))
  (GET "/procesar/salidas/:carrera_id/:numero" [carrera_id numero] (creloj/procesar-salidas carrera_id numero))
  (GET "/procesar/llegadas/:carrera_id/:numero" [carrera_id numero] (creloj/procesar-llegadas carrera_id numero))
  ;; End creloj

  ;; Start limpiar
  (GET "/admin/limpiar" req [] (creloj/limpiar-form req))
  (POST "/admin/limpiar" req [] (creloj/limpiar-tiempos req))
  ;; End limpiar

  ;; Start lector
  (GET "/procesar/lector" req [] (creloj/lector-carreras req))
  ;; End lector
  )
