(ns sk.handlers.admin.certificado.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.models.crud :refer [build-form-save build-form-delete]]
            [sk.handlers.admin.certificado.model :refer [get-certificado get-certificado-id]]
            [sk.handlers.admin.certificado.view :refer [certificado-view certificado-edit-view certificado-add-view certificado-modal-script]]))

(defn certificado [_]
  (let [title "Certificado"
        ok (get-session-id)
        js nil
        rows (get-certificado)
        content (certificado-view title rows)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Only <strong>los administrators </strong> can access this option!!!"))))

(defn certificado-edit
  [id]
  (let [title "Modificar certificado"
        ok (get-session-id)
        js (certificado-modal-script)
        row (get-certificado-id  id)
        rows (get-certificado)
        content (certificado-edit-view title row rows)]
    (application title ok js content)))

(defn certificado-save
  [{params :params}]
  (let [table "certificado"
        result (build-form-save params table)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/certificado")
      (error-404 "No se pudo procesar el record!" "/admin/certificado"))))

(defn certificado-add
  [_]
  (let [title "Crear nuevo certificado"
        ok (get-session-id)
        js (certificado-modal-script)
        row nil
        rows (get-certificado)
        content (certificado-add-view title row rows)]
    (application title ok js content)))

(defn certificado-delete
  [id]
  (let [table "certificado"
        result (build-form-delete table id)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/certificado")
      (error-404 "No se pudo procesar el record!" "/admin/certificado"))))
