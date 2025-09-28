(ns sk.routes.proutes
  (:require
   [sk.handlers.admin.rfid.controller :as rfid-controller]
   [sk.handlers.admin.certificado.controller :as certificado-controller]
   [compojure.core :refer [defroutes GET POST]]
   [sk.handlers.admin.users.controller :as users-controller]
   [sk.handlers.admin.eventos.controller :as eventos-controller]
   [sk.handlers.admin.carrera.controller :as carrera-controller]
   [sk.handlers.admin.categorias.controller :as categorias-controller]
   [sk.handlers.registered.controller :as registered-controller]
   [sk.handlers.admin.mensajes.controller :as mensajes-controller]
   [sk.handlers.users.controller :as users-dashboard]
   [sk.handlers.creloj.controller :as creloj-controller]
   [sk.handlers.carreras.controller :as carreras-dashboard]))

(defroutes proutes
  (GET "/admin/rfid" params [] (rfid-controller/rfid params))
  (GET "/admin/rfid/edit/:id" [id] (rfid-controller/rfid-edit id))
  (POST "/admin/rfid/save" params [] (rfid-controller/rfid-save params))
  (GET "/admin/rfid/add" params [] (rfid-controller/rfid-add params))
  (GET "/admin/rfid/delete/:id" [id] (rfid-controller/rfid-delete id))
  (GET "/admin/certificado" params [] (certificado-controller/certificado params))
  (GET "/admin/certificado/edit/:id" [id] (certificado-controller/certificado-edit id))
  (POST "/admin/certificado/save" params [] (certificado-controller/certificado-save params))
  (GET "/admin/certificado/add" params [] (certificado-controller/certificado-add params))
  (GET "/admin/certificado/delete/:id" [id] (certificado-controller/certificado-delete id))
  (GET "/admin/users" params [] (users-controller/users params))
  (GET "/admin/users/edit/:id" [id] (users-controller/users-edit id))
  (POST "/admin/users/save" params [] (users-controller/users-save params))
  (GET "/admin/users/add" params [] (users-controller/users-add params))
  (GET "/admin/users/delete/:id" [id] (users-controller/users-delete id))

  (GET "/admin/eventos" params eventos-controller/eventos)
  (GET "/admin/eventos/edit/:id" [id] (eventos-controller/eventos-edit id))
  (POST "/admin/eventos/save" params [] (eventos-controller/eventos-save params))
  (GET "/admin/eventos/add" params [] (eventos-controller/eventos-add params))
  (GET "/admin/eventos/delete/:id" [id] (eventos-controller/eventos-delete id))

  (GET "/admin/carrera" params carrera-controller/carrera)
  (GET "/admin/carrera/edit/:id" [id] (carrera-controller/carrera-edit id))
  (POST "/admin/carrera/save" params [] (carrera-controller/carrera-save params))
  (GET "/admin/carrera/add" params [] (carrera-controller/carrera-add params))
  (GET "/admin/carrera/delete/:id" [id] (carrera-controller/carrera-delete id))

  (GET "/admin/categorias" params categorias-controller/categorias)
  (GET "/admin/categorias/edit/:id" [id] (categorias-controller/categorias-edit id))
  (POST "/admin/categorias/save" params [] (categorias-controller/categorias-save params))
  (GET "/admin/categorias/add" params [] (categorias-controller/categorias-add params))
  (GET "/admin/categorias/delete/:id" [id] (categorias-controller/categorias-delete id))

  (GET "/admin/mensajes" params mensajes-controller/mensajes)
  (GET "/admin/mensajes/edit/:id" [id] (mensajes-controller/mensajes-edit id))
  (POST "/admin/mensajes/save" params [] (mensajes-controller/mensajes-save params))
  (GET "/admin/mensajes/add" params [] (mensajes-controller/mensajes-add params))
  (GET "/admin/mensajes/delete/:id" [id] (mensajes-controller/mensajes-delete id))

  (GET "/users" params [] (users-dashboard/users params))
  (GET "/carreras" params [] (carreras-dashboard/carreras params))
  (GET "/display/carreras/:id" [id] (carreras-dashboard/reporte id))

  ;; Start display registered
  (GET "/display/registered" req [] (registered-controller/registrados req))
  (GET "/display/registered/:carrera_id" [carrera_id] (registered-controller/registered carrera_id))
  (GET "/filtrar/registered/:carrera_id/:categoria_id" [carrera_id categoria_id] (registered-controller/registered-filter carrera_id categoria_id))
  (GET "/imprimir/registered/:id" [id] (registered-controller/imprimir id))
  (GET "/cert/registered/:id" [id] (registered-controller/cert id))
  (GET "/update/registered/:id/:no" [id no] (registered-controller/update-db id no))
  ;; End display registered

  ;; Start creloj
  (GET "/time" req [] (creloj-controller/get-time))
  (GET "/display/creloj" req [] (creloj-controller/registrados req))
  (GET "/display/creloj/:carrera_id" [carrera_id] (creloj-controller/contra-reloj carrera_id))
  (GET "/display/salidas/:carrera_id" [carrera_id] (creloj-controller/salidas carrera_id))
  (GET "/display/llegadas/:carrera_id" [carrera_id] (creloj-controller/llegadas carrera_id))
  (GET "/auto/salidas/:rfid" [rfid] (creloj-controller/salidas-auto rfid))
  (GET "/auto/llegadas/:rfid" [rfid] (creloj-controller/llegadas-auto rfid))
  (GET "/update/salida/:id" [id] (creloj-controller/contra-reloj-salida id))
  (GET "/update/llegada/:id" [id] (creloj-controller/contra-reloj-llegada id))
  (GET "/change/salida/:id/:v" [id v] (creloj-controller/contra-reloj-salida-cambiar id v))
  (GET "/change/llegada/:id/:v" [id v] (creloj-controller/contra-reloj-llegada-cambiar id v))
  (GET "/creloj/csv/:carrera_id" [carrera_id] (creloj-controller/generate-csv carrera_id))
  (GET "/procesar/salidas/:carrera_id/:numero" [carrera_id numero] (creloj-controller/procesar-salidas carrera_id numero))
  (GET "/procesar/llegadas/:carrera_id/:numero" [carrera_id numero] (creloj-controller/procesar-llegadas carrera_id numero))
  (GET "/procesar/auto/salidas/:carrera_id/:rfid" [carrera_id rfid] (creloj-controller/procesar-auto-salidas carrera_id rfid))
  (GET "/procesar/auto/llegadas/:carrera_id/:rfid" [carrera_id rfid] (creloj-controller/procesar-auto-llegadas carrera_id rfid))
  ;; End creloj

  ;; Start limpiar
  (GET "/admin/salida/general" req [] (creloj-controller/salidas-general req))
  (POST "/admin/salida/general" req [] (creloj-controller/salidas-general-save req))
  (GET "/admin/limpiar" req [] (creloj-controller/limpiar-form req))
  (POST "/admin/limpiar" req [] (creloj-controller/limpiar-tiempos req))
  (GET "/admin/limpiar/numeros" req [] (creloj-controller/limpiar-numeros-form req))
  (POST "/admin/limpiar/numeros" req [] (creloj-controller/limpiar-numeros req))
  ;; End limpiar

  ;; Start rfid
  (GET "/truncar/rfid" req [] (creloj-controller/truncar-rfid req))
  ;; End rfid
  )
