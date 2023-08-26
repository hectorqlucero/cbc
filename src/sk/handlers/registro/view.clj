(ns sk.handlers.registro.view
  (:require [hiccup.page :refer
             [html5 include-js]]
            [pdfkit-clj.core :refer [as-stream gen-pdf]]
            [sk.handlers.registered.model :refer [create-barcode]]
            [sk.handlers.registro.model :refer
             [get-active-carrera-name get-registered get-register-row get-active-carreras]]
            [sk.models.util :refer
             [build-button build-field build-form build-radio-buttons]]))

(defn build-body [row]
  (let [href (str "/registrar/" (:id row))]
    [:tr
     [:td (:descripcion row)]
     [:td [:a.btn.btn-primary {:role "button"
                               :href href
                               :target "_blank"} "Registrarse"]]]))

(defn registro-view []
  (list
   [:table.table-secondary.table-hover {:style "width:100%;height:auto;"}
    [:caption.table-info "Seleccione la carrera o paseo al cual desea registrarse"]
    [:thead.table-info
     [:tr
      [:th {:field "descripcion" :width "50"} "Carrera/Paseo"]
      [:th {:width "50"} "Procesar"]]]
    [:tbody
     (map build-body (get-active-carreras))]]))

;; Start registrar view
(defn registrar-fields
  [carrera_id]
  (let [data-options (str "label:'Categoria:',
                          labelPosition:'top',
                          method:'GET',
                          required:true,
                          width:'100%',
                          url:'/table_ref/get-categorias/" carrera_id "'")]
    (list
     [:input {:type "hidden" :name "id" :id "id"}]
     [:input {:type "hidden" :name "carrera_id" :id "carrera_id" :value (str carrera_id)}]
     (build-field
      {:id "nombre"
       :name "nombre"
       :class "easyui-textbox"
       :prompt "Nombre aqui..."
       :data-options "label:'Nombre:',
                       labelPosition:'top',
                       required:true,
                       width:'100%'"})
     (build-field
      {:id "apell_paterno"
       :name "apell_paterno"
       :class "easyui-textbox"
       :prompt "Apellido paterno aqui..."
       :data-options "label:'Apellido paterno:',
                       labelPosition:'top',
                       required:true,
                       width:'100%'"})
     (build-field
      {:id "apell_materno"
       :name "apell_materno"
       :class "easyui-textbox"
       :prompt "Apellido materno aqui..."
       :data-options "label:'Apellido materno:',
                       labelPosition:'top',
                       required:false,
                       width:'100%'"})
     (build-field
      {:id "pais"
       :name "pais"
       :class "easyui-textbox"
       :prompt "Pais aqui..."
       :data-options "label:'Pais:',
                       labelPosition:'top',
                       required:false,
                       width:'100%'"})
     (build-field
      {:id "ciudad"
       :name "ciudad"
       :class "easyui-textbox"
       :prompt "Ciudad aqui..."
       :data-options "label:'Ciudad:',
                       labelPosition:'top',
                       required:false,
                       width:'100%'"})
     (build-field
      {:id "telefono"
       :name "telefono"
       :class "easyui-textbox"
       :data-options "label:'Telefono:',labelPosition:'top',width:'100%'"})
     (build-field
      {:id "email"
       :name "email"
       :class "easyui-textbox easyui-validatebox"
       :prompt "Email aqui..."
       :validType "email"
       :data-options "label:'Email:',labelPosition:'top',required:true,width:'100%'"})
     (build-radio-buttons
      "Sexo:"
      (list
       {:id "sexo_m"
        :name "sexo"
        :class "easyui-radiobutton"
        :value "M"
        :data-options "label:'Masculino',checked:true"}
       {:id "sexo_f"
        :name "sexo"
        :class "easyui-radiobutton"
        :value "F"
        :data-options "label:'Femenino'"}))
     (build-field
      {:id "edad"
       :name "edad"
       :class "easyui-textbox"
       :prompt "Edad ej. 20 años"
       :data-options "label:'Edad:',
                       labelPosition:'top',
                       required:false,
                       width:'100%'"})
     (build-field
      {:id "direccion"
       :name "direccion"
       :class "easyui-textbox"
       :prompt "Domicilio aqui..."
       :data-options "label:'Domicilio:', labelPosition:'top', required:false, width:'100%'"})
     (build-field
      {:id "club"
       :name "club"
       :class "easyui-textbox"
       :prompt "Si no pertenece a un club responda 'ninguno'"
       :data-options "label:'Club:', labelPosition:'top', required:false, width:'100%'"})
     (build-field
      {:id "categoria_id"
       :name "categoria_id"
       :class "easyui-combobox"
       :data-options data-options}))))

(def registrar-buttons
  (build-button
   {:href "javascript:void(0)"
    :id "submit"
    :text "Registrarse"
    :class "easyui-linkbutton c6"
    :onClick "saveItem()"}))

(defn registrar-view [token crow]
  (build-form
   (str "Registrarse - " (:descripcion crow))
   token
   (registrar-fields (:id crow))
   (if (nil? crow) nil registrar-buttons)))

(defn registrar-view-scripts
  []
  (include-js "/js/form.js"))
;; End registrar view

;; Start registered-view
(def cnt (atom 0))

(defn my-body [row]
  (let [button-path (str "'" "/imprimir/registered/" (:id row) "'")
        comando (str "location.href = " button-path)]
    [:tr
     [:td (swap! cnt inc)]
     [:td (:id row)]
     [:td (:nombre row)]
     [:td (:apell_paterno row)]
     [:td (:apell_materno row)]
     [:td (:pais row)]
     [:td (:ciudad row)]
     [:td (:telefono row)]
     [:td (:email row)]
     [:td (:club row)]
     [:td [:input#numero {:name "numero_asignado"
                          :type "textbox"
                          :label ""
                          :prompt "No asignado aqui..."
                          :value (:numero_asignado row)
                          :onblur (str "postValue(" (:id row) ", this.value)")}]]
     [:td [:button.btn.btn-outline-primary.c6 {:type "button"
                                               :onclick comando
                                               :target "_blank"} "Registro"]]]))

(defn registered-view []
  (let [rows (get-registered)
        cnt (reset! cnt 0)]
    [:div.container
     [:center
      [:h2 "CORREDORES REGISTRADOS"]
      [:h3 (get-active-carrera-name)]]
     [:table.table.table-striped.table-hover.table-bordered
      [:thead.table-primary
       [:tr
        [:th "#"]
        [:th "ID"]
        [:th "Nombre"]
        [:th "Apellido Paterno"]
        [:th "Apellido Materno"]
        [:th "Pais"]
        [:th "Ciudad"]
        [:th "Telefono"]
        [:th "Email"]
        [:th "Club"]
        [:th "No Asignado"]
        [:th "Imprimir"]]]
      [:tbody (map my-body rows)]]]))

(def table-style
  "border:1px solid black;border-padding:0;")

(defn build-html [id]
  (let [row (get-register-row id)]
    (html5
     [:div
      [:img {:src (create-barcode id)}]
      [:center [:h2 [:strong (:carrera row)]]]
      [:center [:h3 [:strong "FORMATO DE REGISTRO"]]] [:span {:style "float:right;margin-left:10px;"} [:strong "identificador: " (:id row)]]
      [:span "DATOS PERSONALES:"] [:span {:style "float:right;"} [:strong "Fecha: " (:date row)]]
      [:table {:style "margin-top:5px;border:1px solid black;border-radius:13px;border-spacing:0;padding:13px;font-size:0.9em;width:100%;"}
       [:tbody
        [:tr
         [:td {:colspan 3
               :style table-style} [:strong "Nombre: "] (str (:nombre row) "&nbsp;" (:apell_paterno row) "&nbsp;" (:apell_materno row))]]
        [:tr
         [:td {:style table-style} [:strong "Categoria: "] (:categoria row)]
         [:td {:colspan 2
               :style table-style} [:strong  "Telefono: "] (:telefono row)]]
        [:tr
         [:td {:style table-style} [:strong "Ciudad: "] (:ciudad row)]
         [:td {:style table-style} [:strong "Club: "] (:club row)]
         [:td {:style table-style} [:strong "Sexo: "] (:sexo row)]]
        [:tr
         [:td {:colspan 3
               :style table-style} [:strong "Email: "] (:email row)]]
        [:tr
         [:td {:colspan 3
               :style table-style} [:strong "Dirección: "] (:direccion row)]]]]
      [:table {:style "margin-top:5px;border:1px solid black;border-radius:13px;border-spacing:0;padding:13px;font-size:0.9em;width:100%;"}
       [:tbody
        [:tr
         [:td {:style table-style} [:strong "Fecha de Registro: "] [:span {:style "float:right;"} [:strong "No: "] "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]]]
        [:tr
         [:td {:style table-style} [:strong "Costo del Evento el día de registro $ : "]]]]]
      [:table {:style "margin-top:5px;border:1px solid black;border-radius:13px;border-spacing:0;padding:13px;font-size:0.9em;width:100%;"}
       [:tbody
        [:tr {:align "center"}
         [:td [:strong "Políticas y reglas del Evento"]]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} (:p1 row)]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} "&nbsp;&nbsp;"]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} (:p2 row)]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} "&nbsp;&nbsp;"]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} (:p3 row)]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} "&nbsp;&nbsp;"]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} (:p4 row)]]
        [:tr {:align "center"}
         [:td {:style "font-size:0.7em;"} [:strong (:d1 row)]]]
        [:tr {:align "center"}
         [:td {:style "font-size:0.7em;"} [:strong (:d2 row)]]]]]
      [:br]
      [:br]
      [:br]
      [:div {:style "width:100%;font-size:1em;font-weight:bold;text:center;"}
       [:center [:hr {:style "width:70%;"}]]
       [:center [:p [:strong "Firma y Aceptación del participante y/o tutor:"]]]]])))

(defn registered-pdf [id]
  (let [html (build-html id)
        filename (str "registro_" id ".pdf")]
    {:headers {"Content-Type" "application/pdf"
               "Content-Disposition" (str "attachment;filename=" filename)
               "Cache-Control" "no-cache,no-store,max-age=0,must-revalidate,pre-check=0,post-check=0"}
     :body (as-stream (gen-pdf html
                               :margin {:top 20 :right 15 :bottom 50 :left 15}))}))

(defn registered-js []
  [:script
   (str "function postValue(id,no) {
         $.get('/update/registered/'+id+'/'+no, function(data) {
           var dta = JSON.parse(data);
           $.messager.show({
             title: 'Estatus',
             msg: dta.message
           })
         })
   }")])
;; End registered view

(comment
  (registro-view))
