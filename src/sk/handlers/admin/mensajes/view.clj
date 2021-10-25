(ns sk.handlers.admin.mensajes.view
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
    {:id "registrar_mensaje"
     :name "registrar_mensaje"
     :class "easyui-textbox"
     :prompt "El mensaje que aparecera despues de precionar el boton de registrar"
     :data-options "label:'Registrar Mensaje:',
                 labelPosition:'top',
                 required:true,
                 multiline:true,
                 width:'100%',
                 height:120"})
   (build-field
    {:id "correo_mensaje"
     :name "correo_mensaje"
     :class "easyui-textbox"
     :prompt "Este mensaje aparecera en el correo del corredor"
     :data-options "label:'Correo Mensaje:',
                 labelPosition:'top',
                 required:true,
                 multiline:true,
                 width:'100%',
                 height:120"})))

(defn mensajes-view
  "Esto crea el grid y la forma en una ventana"
  [title]
  (list
   (anti-forgery-field)
   (build-table
    title
    "/admin/mensajes"
    (list
     [:th {:data-options "field:'id',sortable:true,fixed:false"} "ID"]
     [:th {:data-options "field:'carrera_id',sortable:true,fixed:false"
           :formatter "carreraName"} "Carrera"]
     [:th {:data-options "field:'registrar_mensaje',sortable:true,width:33"} "Registrar Mensaje"]
     [:th {:data-options "field:'correo_mensaje',sortable:true,width:33"} "Correo Mensaje"]))
   (build-toolbar)
   (build-dialog title dialog-fields)))

(defn mensajes-scripts
  "Esto crea el javascript necesario"
  []
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
