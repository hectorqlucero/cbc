(ns sk.handlers.admin.certificado.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.handlers.admin.certificado.model :refer [carrera-options]]
            [sk.models.form :refer [form build-hidden-field build-image-field build-image-field-script build-field build-select build-radio build-modal-buttons build-textarea]]
            [sk.models.grid :refer [build-grid build-modal modal-script]]))

(defn certificado-view
  [title rows]
  (let [labels ["CARRERA" "DESCRIPCION" "IMAGEN"]
        db-fields [:carrera_formatted :descripcion :image]
        fields (zipmap db-fields labels)
        table-id "certificado_table"
        args {:new true :edit true :delete true}
        href "/admin/certificado"]
    (build-grid title rows table-id fields href args)))

(defn build-certificado-fields
  [row]
  (list
   (build-hidden-field {:id "id"
                        :name "id"
                        :value (:id row)})
   (build-image-field row)
   (build-select {:label "CARRERA_ID"
                  :type "text"
                  :id "carrera_id"
                  :name "carrera_id"
                  :placeholder "carrera_id aqui..."
                  :required false
                  :value (:carrera_id row)
                  :options (carrera-options)})
   (build-textarea {:label "DESCRIPCION"
                    :id "descripcion"
                    :name "descripcion"
                    :rows "3"
                    :placeholder "descripcion aqui..."
                    :required false
                    :value (:descripcion row)})))

(defn build-certificado-form
  [title row]
  (let [fields (build-certificado-fields row)
        href "/admin/certificado/save"
        buttons (build-modal-buttons)]
    (form href fields buttons)))

(defn build-certificado-modal
  [title row]
  (build-modal title row (build-certificado-form title row)))

(defn certificado-edit-view
  [title row rows]
  (list
   (certificado-view "certificado Manteniento" rows)
   (build-certificado-modal title row)))

(defn certificado-add-view
  [title row rows]
  (list
   (certificado-view "certificado Mantenimiento" rows)
   (build-certificado-modal title row)))

(defn certificado-modal-script
  []
  (list
   (build-image-field-script)
   (modal-script)))
