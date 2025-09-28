(ns sk.handlers.admin.rfid.controller
  (:require [ring.util.response :refer [redirect]]
            [sk.layout :refer [application error-404]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.models.crud :refer [build-form-save build-form-delete]]
            [sk.handlers.admin.rfid.model :refer [get-rfid get-rfid-id]]
            [sk.handlers.admin.rfid.view :refer [rfid-view rfid-edit-view rfid-add-view rfid-modal-script]]))

(defn rfid [_]
  (let [title "Rfid"
        ok (get-session-id)
        js nil
        rows (get-rfid)
        content (rfid-view title rows)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Only <strong>los administrators </strong> can access this option!!!"))))

(defn rfid-edit
  [id]
  (let [title "Modificar rfid"
        ok (get-session-id)
        js (rfid-modal-script)
        row (get-rfid-id  id)
        rows (get-rfid)
        content (rfid-edit-view title row rows)]
    (application title ok js content)))

(defn rfid-save
  [{params :params}]
  (let [table "rfid"
        result (build-form-save params table)]
    (redirect "/admin/rfid")))

(defn rfid-add
  [_]
  (let [title "Crear nuevo rfid"
        ok (get-session-id)
        js (rfid-modal-script)
        row nil
        rows (get-rfid)
        content (rfid-add-view title row rows)]
    (application title ok js content)))

(defn rfid-delete
  [id]
  (let [table "rfid"
        result (build-form-delete table id)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/rfid")
      (error-404 "No se pudo procesar el record!" "/admin/rfid"))))
