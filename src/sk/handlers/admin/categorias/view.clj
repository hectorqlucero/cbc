(ns sk.handlers.admin.categorias.view
  (:require [hiccup.page :refer [include-js]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.util :refer [build-dialog build-field build-radio-buttons build-table build-toolbar]]))

(def dialog-fields
  (list
   [:input {:type "hidden" :id "id" :name "id"}]
   (build-field
    {:id "descripcion"
     :name "descripcion"
     :class "easyui-textbox"
     :data-options "label:'Descripción:',
                     labelPosition:'top',
                     width:'100%',
                     required:true"})
   (build-radio-buttons
    "Activa?"
    (list
     {:id "activa_no"
      :name "activa"
      :class "easyui-radiobutton"
      :value "N"
      :data-options "label:'No',checked:true"}
     {:id "activa_si"
      :name "activa"
      :class "easyui-radiobutton"
      :value "S"
      :data-options "label:'Si'"}))))

(defn categorias-view [title]
  (list
   (anti-forgery-field)
   (build-table
    title
    "/admin/categorias"
    (list
     [:th {:data-options "field:'id',sortable:true,fixed:false"} "ID"]
     [:th {:data-options "field:'descripcion',sortable:true,fixed:false"} "Categoria"]
     [:th {:data-options "field:'activa',sortable:true,fixed:false"} "Activa"]))
   (build-toolbar)
   (build-dialog title dialog-fields)))

(defn categorias-scripts []
  (include-js "/js/grid.js"))
