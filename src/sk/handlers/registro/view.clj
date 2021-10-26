(ns sk.handlers.registro.view
  (:require [hiccup.page :refer [include-js]]
            [sk.handlers.registro.model :refer [get-active-carreras]]))

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

(comment
  (registro-view))
