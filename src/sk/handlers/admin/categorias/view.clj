(ns sk.handlers.admin.categorias.view
  (:require [hiccup.page :refer [include-js]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.util :refer [build-dialog build-field build-radio-buttons build-table build-toolbar]]))

(def dialog-fields
  (list
   [:input {:type "hidden" :id "id" :name "id"}]
   (build-field
    {:id "carrera_id"
     :name "carrera_id"
     :class "easyui-combobox"
     :data-options "label:'Carrera:',
                    labelPosition:'top',
                    url:'/admin/carrera/carreras',
                    method:'GET',
                    required:true,
                    width:'100%'"})
   (build-field
    {:id "descripcion"
     :name "descripcion"
     :class "easyui-textbox"
     :data-options "label:'Descripción:',
                     labelPosition:'top',
                     width:'100%',
                     required:true"})))

(defn categorias-view [title]
  (list
   (anti-forgery-field)
   (build-table
    title
    "/admin/categorias"
    (list
     [:th {:data-options "field:'id',sortable:true,fixed:false"} "ID"]
     [:th {:data-options "field:'carrera_id',sortable:true,fixed:false"
           :formatter "carreraName"} "Carrera"]
     [:th {:data-options "field:'descripcion',sortable:true,fixed:false"} "Categoria"]))
   (build-toolbar)
   (build-dialog title dialog-fields)))

(defn categorias-scripts []
  (list
   (include-js "/js/grid.js")
   [:script
    "function carreraName(val, row, index) {
        var result = null;
        var scriptUrl = '/table_ref/get-carrera-name/' + val;
        $.ajax({
          url: scriptUrl,
          type: 'get',
          dataType: 'html',
          async: false,
          success: function(data) {
            result = data;
          }
        });
        return result;
      }
      "]))
