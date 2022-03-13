(ns sk.handlers.admin.users.view
  (:require [hiccup.page :refer [include-js]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.crud :refer [config]]
            [sk.models.util :refer [build-dialog build-dialog-buttons build-field build-image-field build-image-field-script build-radio-buttons build-table build-toolbar]]))

(def dialog-fields
  (list
   [:input {:type "hidden" :id "id" :name "id"}]
   (build-field
    {:id           "username"
     :name         "username"
     :class        "easyui-textbox easyui-validatebox"
     :validType    "email"
     :prompt       "someone@server.com - correo electronico"
     :data-options "label:'Usuario:',
                     labelPosition:'top',
                     width:'100%',required: true"})
   (build-field
    {:id "password"
     :name "password"
     :class "easyui-passwordbox"
     :prompt "Contraseña"
     :data-options "label:'Contraseña:',
                 labelPosition:'top',
                 required:true,
                 width:'100%'"})
   (build-field
    {:id           "firstname"
     :name         "firstname"
     :class        "easyui-textbox"
     :prompt       "Nombre ej. Pedro"
     :data-options "label:'Nombre:',
                     labelPosition:'top',
                     width:'100%',required: true"})
   (build-field
    {:id           "lastname"
     :name         "lastname"
     :class        "easyui-textbox"
     :prompt       "Apellidos ej. Lopez Contreras"
     :data-options "label:'Apellidos:',
                     labelPosition:'top',
                     width:'100%',required: true"})
   (build-field
    {:id           "dob"
     :name         "dob"
     :class        "easyui-datebox"
     :prompt       "mm/dd/yyyy"
     :data-options "label:'Fecha de nacimiento:',
                     labelPosition:'top',
                     width:'100%',required: false"})
   (build-field
    {:id           "email"
     :name         "email"
     :class        "easyui-textbox easyui-validatebox"
     :prompt       "Email aqui..."
     :validType    "email"
     :data-options "label:'Email:',
                     labelPosition:'top',
                     width:'100%',required: false"})
   (build-field
    {:id "level"
     :name "level"
     :class "easyui-combobox"
     :data-options "label:'Nivel de Usuario:',
                     labelPosition:'top',
                     url:'/table_ref/levels',
                     method:'GET',
                     required:true,
                     width:'100%'"})
   (build-radio-buttons
    "Activo?"
    (list
     {:id "active_no"
      :name "active"
      :class "easyui-radiobutton"
      :value "F"
      :data-options "label:'No', checked:true"}
     {:id "active_yes"
      :name "active"
      :class "easyui-radiobutton"
      :value "T"
      :data-options "label:'Si'"}))))

(defn users-view [title]
  (list
   (anti-forgery-field)
   (build-table
    title
    "/admin/users"
    (list
     [:th {:data-options "field:'username',sortable:true,fixed:false,width:100"} "Usuario [clic para seleccionar]"]
     [:th {:data-options "field:'lastname',sortable:true,fixed:false,width:100"} "Apellidos"]
     [:th {:data-options "field:'firstname',sortable:true,fixed:true,width:100"} "Nombre"]
     [:th {:data-options "field:'level',sortable:true,fixed:true,width:100"
           :formatter "levelDesc"} "Nivel"]
     [:th {:data-options "field:'active',sortable:true,fixed:false,width:100"
           :formatter "statusDesc"} "Activo?"]))
   (build-toolbar)
   (build-dialog title dialog-fields)))

(defn users-scripts []
  (list
   (include-js "/js/grid.js")
   [:script
    (str
     "
  function levelDesc(val, row, index) {
    if(row.level == 'A') {
      return 'Administrador';
    } else if(row.level == 'U') {
      return 'Usuario';
    } else if(row.level == 'S') {
      return 'Sistema';
    }
  }

  function statusDesc(val, row, index) {
    if(row.active == 'T') {
      return 'Si';
    } else {
      return 'No';
    }
  }
  ")]))
