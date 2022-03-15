(ns sk.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [clj-time.core :as t]
            [sk.models.crud :refer [config]]
            [sk.models.util :refer [user-level user-name]]))

(defn build-admin []
  (list
   [:a.dropdown-item {:href "/admin/users"} "Usuarios"]
   [:a.dropdown-item {:href "/admin/eventos"} "Eventos"]
   [:a.dropdown-item {:href "/admin/carrera"} "Carrera Configurar"]
   [:a.dropdown-item {:href "/admin/categorias"} "Categorias Configurar"]
   [:a.dropdown-item {:href "/admin/mensajes"} "Mensajes Configurar"]
   [:a.dropdown-item {:href "/admin/limpiar"} "Limpiar Contrareloj"]))

(defn user-menus []
  [:li.nav-item [:a.nav-link {:href "/eventos/list"} "Eventos"]]
  [:li.nav-item [:a.nav-link {:href "/registro"} "Registrar aquí"]])

(defn admin-menus []
  (list
   [:li.nav-item [:a.nav-link {:href "/eventos/list"} "Eventos"]]
   [:li.nav-item [:a.nav-link {:href "/registro"} "Registrar aquí"]]
   [:li.nav-item [:a.nav-link {:href "/display/registered"} "Registrados"]]
   [:li.nav-item [:a.nav-link {:href "/display/creloj"} "Contra-Reloj"]]))

(defn system-menus []
  (list
   [:li.nav-item [:a.nav-link {:href "/eventos/list"} "Eventos"]]
   [:li.nav-item [:a.nav-link {:href "/registro"} "Registrar aquí"]]
   [:li.nav-item [:a.nav-link {:href "/display/registered"} "Registrados"]]
   [:li.nav-item [:a.nav-link {:href "/display/creloj"} "Contra-Reloj"]]))

(defn menus-private []
  (list
   [:nav.navbar.navbar-expand-sm.navbar-light.bg-secondary.fixed-top
    [:a.navbar-brand {:href "/"} (:site-name config)]
    [:button.navbar-toggler {:type "button"
                             :data-toggle "collapse"
                             :data-target "#collapsibleNavbar"}
     [:span.navbar-toggler-icon]]
    [:div#collapsibleNavbar.collapse.navbar-collapse
     [:ul.navbar-nav
      (cond
        (= (user-level) "U") (user-menus)
        (= (user-level) "A") (admin-menus)
        (= (user-level) "S") (system-menus))
      (when (= (user-level) "S")
        [:li.nav-item.dropdown
         [:a.nav-link.dropdown-toggle {:href "#"
                                       :id "navdrop"
                                       :data-toggle "dropdown"} "Administrar"]
         [:div.dropdown-menu
          (build-admin)]])
      [:li.nav-item [:a.nav-link {:href "/home/logoff"} (str "Salir [" (user-name) "]")]]]]]))

(defn menus-public []
  (list
   [:nav.navbar.navbar-expand-sm.navbar-light.bg-secondary.fixed-top
    [:a.navbar-brand {:href "/"} (:site-name config)]
    [:button.navbar-toggler {:type "button"
                             :data-toggle "collapse"
                             :data-target "#collapsibleNavbar"}
     [:span.navbar-toggler-icon]]
    [:div#collapsibleNavbar.collapse.navbar-collapse
     [:ul.navbar-nav
      [:li.nav-item [:a.nav-link {:href "/eventos/list"} "Eventos"]]
      [:li.nav-item [:a.nav-link {:href "/registro"} "Registrar aquí"]]
      [:li.nav-item [:a.nav-link {:href "/display/oregistered"} "Registrados"]]
      [:li.nav-item [:a.nav-link {:href "/home/login"} "Conectar"]]]]]))

(defn menus-none []
  (list
   [:nav.navbar.navbar-expand-sm.navbar-light.bg-secondary.fixed-top
    [:a.navbar-brand {:href "#"} (:site-name config)]
    [:button.navbar-toggler {:type "button"
                             :data-toggle "collapse"
                             :data-target "#collapsibleNavbar"}
     [:span.navbar-toggler-icon]]
    [:div#collapsibleNavbar.collapse.navbar-collapse]]))

(defn app-css []
  (list
   (include-css "/bootstrap/css/bootstrap.min.css")
   (include-css "/bootstrap/css/lumen.min.css")
   (include-css "https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css")
   (include-css "/easyui/themes/metro-gray/easyui.css")
   (include-css "/easyui/themes/icon.css")
   (include-css "/easyui/themes/color.css")
   (include-css "/css/main.css")
   (include-css "/RichText/src/richtext.min.css")))

(defn app-js []
  (list
   (include-js "/easyui/jquery.min.js")
   (include-js "/popper/popper.min.js")
   (include-js "/bootstrap/js/bootstrap.min.js")
   (include-js "/easyui/jquery.easyui.min.js")
   (include-js "/easyui/jquery.edatagrid.js")
   (include-js "/easyui/datagrid-detailview.js")
   (include-js "/easyui/datagrid-groupview.js")
   (include-js "/easyui/datagrid-bufferview.js")
   (include-js "/easyui/datagrid-scrollview.js")
   (include-js "/easyui/datagrid-filter.js")
   (include-js "/easyui/locale/easyui-lang-es.js")
   (include-js "/RichText/src/jquery.richtext.min.js")
   (include-js "/js/main.js")))

(defn application [title ok js & content]
  (html5 {:ng-app (:site-name config) :lang "es"}
         [:head
          [:title (if title title (:site-name config))]
          [:meta {:charset "UTF-8"}]
          [:meta {:name "viewport"
                  :content "width=device-width, initial-scale=1"}]
          (app-css)]
         [:body.easyui-layout
          [:div#p {:data-options "region:'north'"
                   :style "height:60px;"}]
          (cond
            (= ok -1) (menus-none)
            (= ok 0) (menus-public)
            (> ok 0) (menus-private))
          [:div#q {:data-options "region:'center'"
                   :style "padding:5px;background:#eee;"}
           content]
          (app-js)
          js
          [:div#r {:data-options "region:'south'"
                   :style "height:25px;text-align:center;"}
           [:span "Copyright &copy" (t/year (t/now)) " PescaSoft - All Rights Reserved"]]]))

(defn error-404 [content return-url]
  (html5 {:ng-app (:site-name config) :lang "es"}
         [:head
          [:title "Mesaje"]
          [:meta {:charset "UTF-8"}]
          [:meta {:name "viewport"
                  :content "width=device-width, initial-scale=1"}]
          (app-css)
          [:link {:rel "shortcut icon"
                  :type "image/x-icon"
                  :href "data:image/x-icon;,"}]]
         [:body {:style "width:100vw;height:98vh;border:1px solid #000;"}
          [:div.container {:style "height:88vh;margin-top:75px;"}
           (menus-none)
           [:div.easyui-panel {:data-options "fit:true,border:false" :style "padding-left:14px;"}
            [:div
             [:p [:h3 [:b "Mensaje: "]] content]
             [:p [:h3 [:a {:href return-url} "Clic aqui para " [:strong "Continuar"]]]]]]]

          (app-js)
          nil]
         [:footer.bg-secondary.text-center.fixed-bottom
          [:span  "Copyright &copy" (t/year (t/now)) " PescaSoft - All Rights Reserved"]]))
