(ns sk.handlers.registro.handler
  (:require [sk.models.util :refer [get-session-id]]
            [sk.models.crud :refer 
             [Query db config Save crud-fix-id build-postvars]]
            [sk.handlers.registro.model :refer
             [get-active-carrera-name registrar-mensaje correo-mensaje]]
            [cheshire.core :refer [generate-string]]
            [sk.layout :refer [application]]
            [pdfkit-clj.core :refer [gen-pdf]]
            [sk.models.email :refer [host send-email]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.handlers.registro.view :refer
             [registro-view registrar-view registrar-view-scripts build-html]]))

(defn registro [_]
  (let [title "Registro de Paseos/Carreras"
        ok (get-session-id)
        content (registro-view)]
    (application title ok nil content)))

;; Start registrar
(defn registrar
  [carrera_id]
  (try
    (let [title "carreras"
          ok -1
          crow (first (Query db ["select * from carrera where id = ?" carrera_id]))
          content (registrar-view (anti-forgery-field) crow)
          scripts (registrar-view-scripts)]
      (application title ok scripts content))
    (catch Exception e (.getMessage e))))

(defn email-body
  "Crear el cuerpo del correo electronico"
  [params]
  (try
    (let [nombre (str (:nombre params) " " (:apell_paterno params) " " (:apell_materno params))
          carrera_id (:carrera_id params)
          domicilio (:direccion params)
          telefono (:telefono params)
          email (:email params)
          categoria_id (:categoria_id params)
          categoria (:descripcion (first (Query db ["select descripcion from categorias where id = ?" categoria_id])))
          carrera (:descripcion (first (Query db ["select * from carrera where id = ?" carrera_id])))
          subject (str "Nuevo Registro - " carrera)
          content (str "<strong>Hola Marco</strong>,</br></br>"
                       "Mis datos son los siguientes:</br></br>"
                       "<strong>Nombre:</strong> " nombre "</br></br>"
                       "<strong>Domicilio:</strong> " domicilio "</br></br>"
                       "<strong>Telefono:</strong> " telefono "</br></br>"
                       "<strong>Email:</strong> " email "</br></br>"
                       "<strong>Categoria:</strong> " categoria)
          body {:from (:email-user config)
                :to "marcopescador@hotmail.com"
                :cc "hectorqlucero@gmail.com"
                :subject subject
                :body [{:type "text/html;charset=utf-8"
                        :content content}]}]
      body)
    (catch Exception e (.getMessage e))))

(defn corredor-email-body
  "Crear el cuerpo del correo electronico de confirmacion at corredor"
  [params id]
  (try
    (let [nombre (str (:nombre params) " " (:apell_paterno params) " " (:apell_materno params))
          email (:email params)
          carrera_id (:carrera_id params)
          subject (str "Nuevo Registro - " (get-active-carrera-name carrera_id))
          content (str "<strong>Hola</strong> " nombre ",<br/>" (correo-mensaje carrera_id))
          body {:from (:email-user config)
                :to email
                :cd "hectorqlucero@gmail.com"
                :subject subject
                :body [{:type "text/html;charset=utf-8"
                        :content content}
                       {:type :inline
                        :content (:path (bean (gen-pdf (build-html id))))
                        :content-type "application/pdf"}]}]
      body)
    (catch Exception e (.getMessage e))))

(defn registrar-save
  [{params :params}]
  (try
    (let [table "carreras"
          id (crud-fix-id (:id params))
          carrera_id (:carrera_id params)
          email-body (email-body params)
          postvars (build-postvars table params)
          success (registrar-mensaje carrera_id)
          result (Save db (keyword table) postvars ["id = ?" id])
          corredor-email-body (corredor-email-body params (:generated_key (first result)))]
      (if (seq result)
        (do
          (send-email host email-body)
          (send-email host corredor-email-body)
          (generate-string {:success success}))
        (generate-string {:error "No se puede procesar!"})))
    (catch Exception e (.getMessage e))))
;; End registrar

(comment
  (email-body {:params {:id nil
                            :nombre "Hector"
                            :apell_paterno "Lucero"
                            :apell_materno "Quihuis"
                            :pais "Mexico"
                            :ciudad "Mexicali"
                            :telefono "6861362245"
                            :email "hectorqlucero@gmail.com"
                            :sexo "M"
                            :fecha_nacimiento "1957-02-07"
                            :direccion "Islas Salomon #271, Fraccionamiento Santa Monica"
                            :club "Ciclismo Mexicali"
                            :carrera_id 1
                            :categoria_id 1}})
  (corredor-email-body {:nombre "Hector"
                        :apell_paterno "Lucero"
                        :apell_materno "Quihuis"
                        :email "hectorqlucero@gmail.com"
                        :carrera_id 1
                        } 1)
  (registrar-save {:params {:id nil
                            :nombre "Hector"
                            :apell_paterno "Lucero"
                            :apell_materno "Quihuis"
                            :pais "Mexico"
                            :ciudad "Mexicali"
                            :telefono "6861362245"
                            :email "hectorqlucero@gmail.com"
                            :sexo "M"
                            :fecha_nacimiento "1957-02-07"
                            :direccion "Islas Salomon #271, Fraccionamiento Santa Monica"
                            :club "Ciclismo Mexicali"
                            :carrera_id 1
                            :categoria_id 1}}))
