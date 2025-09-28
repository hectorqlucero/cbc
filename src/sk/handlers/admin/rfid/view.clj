(ns sk.handlers.admin.rfid.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.form :refer [form build-hidden-field build-field build-select build-radio build-modal-buttons build-textarea]]
            [sk.models.grid :refer [build-grid build-modal modal-script]]))

(defn rfid-view
  [title rows]
  (let [labels ["RFID" "NUMERO" "HORA ASIGNACION"]
        db-fields [:rfid :numero :hora]
        fields (zipmap db-fields labels)
        table-id "rfid_table"
        args {:new true :edit true :delete true}
        href "/admin/rfid"]
    (build-grid title rows table-id fields href args)))

(defn build-rfid-fields
  [row]
  (list
   (build-hidden-field {:id "id"
                        :name "id"
                        :value (:id row)})
   (build-textarea {:label "NUMERO"
                    :id "numero"
                    :name "numero"
                    :rows "1"
                    :placeholder "numero aqui..."
                    :required false
                    :value (:numero row)})
   (build-field {:label "RFID"
                 :type "text"
                 :id "rfid"
                 :name "rfid"
                 :placeholder "rfid aqui..."
                 :required false
                 :autofocus true
                 :value (:rfid row)})))

(defn build-rfid-form
  [title row]
  (let [fields (build-rfid-fields row)
        href "/admin/rfid/save"
        buttons (build-modal-buttons)]
    (form href fields buttons)))

(defn build-rfid-modal
  [title row]
  (build-modal title row (build-rfid-form title row)))

(defn rfid-edit-view
  [title row rows]
  (list
   (rfid-view "rfid Manteniento" rows)
   (build-rfid-modal title row)))

(defn rfid-add-view
  [title row rows]
  (list
   (rfid-view "rfid Mantenimiento" rows)
   (build-rfid-modal title row)))

(defn rfid-modal-script
  []
  (modal-script))
