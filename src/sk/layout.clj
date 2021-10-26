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
   [:a.dropdown-item {:href "/admin/mensajes"} "Mensajes Configurar"]))

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
      [:li.nav-item [:a.nav-link {:href "/eventos/list"} "Eventos"]]
      [:li.nav-item [:a.nav-link {:href "/registro"} "Registrar aquí"]]
      [:li.nav-item.dropdown
       [:a.nav-link.dropdown-toggle {:href "#"
                                     :id "navdrop"
                                     :data-toggle "dropdown"} "Administrar"]
       [:div.dropdown-menu
        (build-admin)]]
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
           [:span "Copyright &copy" (t/year (t/now)) " - All Rights Reserved"]]]))

(defn error-404 [error return-url]
  [:div
   [:p [:h3 [:b "Error: "]] error]
   [:p [:h3 [:a {:href return-url} "Clic here to " [:strong "Return"]]]]])
