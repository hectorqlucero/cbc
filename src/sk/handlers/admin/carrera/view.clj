(ns sk.handlers.admin.carrera.view
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
   (build-field
    {:id "p1"
     :name "p1"
     :class "easyui-textbox"
     :prompt "Aqui el parrafo #1..."
     :data-options "label:'Parrafo #1:',
                    labelPosition:'top',
                    required:true,
                    multiline:true,
                    width:'100%',
                    height:120"})
   (build-field
    {:id "p2"
     :name "p2"
     :class "easyui-textbox"
     :prompt "Aqui el parrafo #1..."
     :data-options "label:'Parrafo #2:',
                    labelPosition:'top',
                    required:true,
                    multiline:true,
                    width:'100%',
                    height:120"})
   (build-field
    {:id "p3"
     :name "p3"
     :class "easyui-textbox"
     :prompt "Aqui el parrafo #1..."
     :data-options "label:'Parrafo #3:',
                    labelPosition:'top',
                    required:true,
                    multiline:true,
                    width:'100%',
                    height:120"})
   (build-field
    {:id "p4"
     :name "p4"
     :class "easyui-textbox"
     :prompt "Aqui el parrafo #1..."
     :data-options "label:'Parrafo #4:',
                    labelPosition:'top',
                    required:true,
                    multiline:true,
                    width:'100%',
                    height:120"})
   (build-field
    {:id "d1"
     :name "d1"
     :class "easyui-textbox"
     :prompt "Aqui las notas ej: Nota: imprimir 2 copias y Entregar..."
     :data-options "label:'Nota:',
                    labelPosition:'top',
                    required:true,
                    multiline:true,
                    width:'100%',
                    height:120"})
   (build-field
    {:id "d2"
     :name "d2"
     :class "easyui-textbox"
     :prompt "Aqui el link ej: https://www.facebook.com/granfondosanfelipe"
     :data-options "label:'Link:',
                    labelPosition:'top',
                    required:true,
                    multiline:true,
                    width:'100%',
                    height:120"})
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

(defn carrera-view [title]
  (list
   (anti-forgery-field)
   (build-table
    title
    "/admin/carrera"
    (list
     [:th {:data-options "field:'id',sortable:true"} "ID"]
     [:th {:data-options "field:'descripcion',sortable:true"} "Carrera Descripción"]
     [:th {:data-options "field:'activa',sortable:true"} "Activa"]))
   (build-toolbar)
   (build-dialog title dialog-fields)))

(defn carrera-scripts []
  (include-js "/js/carreras_grid.js"))
