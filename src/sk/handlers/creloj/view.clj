(ns sk.handlers.creloj.view
  (:require [hiccup.page :refer [html5]]
            [sk.handlers.registro.model :refer [get-active-carreras]]
            [sk.models.util :refer [parse-int]]
            [clojure.string :as string]
            [sk.handlers.creloj.model
             :refer [get-active-carrera-name get-oregistered get-register-row]]))

;; Start format seconds->duration
(def seconds-in-minute 60)
(def seconds-in-hour (* 60 seconds-in-minute))
(def seconds-in-day (* 24 seconds-in-hour))
(def seconds-in-week (* 7 seconds-in-day))

(defn seconds->duration [seconds]
  (let [weeks   ((juxt quot rem) seconds seconds-in-week)
        wk      (first weeks)
        days    ((juxt quot rem) (last weeks) seconds-in-day)
        d       (first days)
        hours   ((juxt quot rem) (last days) seconds-in-hour)
        hr      (first hours)
        min     (quot (last hours) seconds-in-minute)
        sec     (rem (last hours) seconds-in-minute)]
    (string/join ", "
                 (filter #(not (string/blank? %))
                         (conj []
                               (when (> wk 0) (str wk " wk"))
                               (when (> d 0) (str d " d"))
                               (when (> hr 0) (str hr " hr"))
                               (when (> min 0) (str min " min"))
                               (when (> sec 0) (str sec " sec")))))))
;; End format seconds->duration

;; Start registrados-view
(defn build-body [row]
  (let [href (str "/display/creloj/" (:id row))]
    [:tr
     [:td (:descripcion row)]
     [:td [:a.btn.btn-primary {:role "button"
                               :href href} "Contra Reloj"]]]))

(defn registrados-view []
  (list
   [:table.table-secondary.table-hover {:style "width:100%;height:auto;"}
    [:caption.table-info "Seleccione la carrera o paseo al cual desea ver los corredores. Nota: Necesita tener un numero asignado!!!"]
    [:thead.table-info
     [:tr
      [:th {:field "descripcion" :width "50"} "Carrera/Paseo"]
      [:th {:width "50"} "Procesar"]]]
    [:tbody
     (map build-body (get-active-carreras))]]))
;; End registrados-view

;; Start tiempo-view
(defn my-body [row]
  (let [slink (str "onclick = salida(" (:id row) ")")
        elink (str "onclick = llegada(" (:id row) ")")
        segundos  (parse-int (:tiempo row))
        tiempo (if-not (nil? segundos) (seconds->duration segundos) nil)]
    [:tr
     [:td (:id row)]
     [:td (str (:nombre row) " " (:apell_paterno row) " " (:apell_materno row))]
     [:td (:categoria row)]
     [:td [:strong (:numero_asignado row)]]
     [:td (or (:salida row) [:input {:type "checkbox"
                                     :onclick slink}])]
     [:td (or (:llegada row) [:input {:type "checkbox"
                                      :onclick elink}])]
     [:td tiempo]]))

(defn creloj-view [carrera_id]
  (let [rows (get-oregistered carrera_id)]
    [:div.container
     [:center
      [:h2 "CORREDORES REGISTRADOS"]
      [:h3 (get-active-carrera-name carrera_id)]]
     [:table.table.table-striped.table-hover.table-bordered
      [:thead.table-primary
       [:tr
        [:th "ID"]
        [:th "CORREDOR"]
        [:th "CATEGORIA"]
        [:th "NUMERO ASIGNADO"]
        [:th "SALIDA"]
        [:th "LLEGADA"]
        [:th "TIEMPO"]]]
      [:tbody (map my-body rows)]]]))
;; End tiempo-view

;; Start creloj-scripts
(defn creloj-js [carrera_id]
  [:script
   "
    function salida(id) {
      let carrera_id = " carrera_id ";
      $.get('/update/salida/'+id, function(data) {
        var dta = JSON.parse(data);
        $.messager.show({
          title: 'Procesado!',
          msg: dta.message
        })
      })
    }

    function llegada(id) {
      let carrera_id = " carrera_id ";
      $.get('/update/llegada/'+id, function(data) {
        var dta = JSON.parse(data);
        $.messager.show({
          title: 'Procesado!',
          msg: dta.message
        })
      })
    }
 "])
;; End crelog-scripts

(comment
  (seconds->duration 7249)
  (get-registered 5)
  (creloj-view 5))
