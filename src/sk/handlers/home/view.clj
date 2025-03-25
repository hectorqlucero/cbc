(ns sk.handlers.home.view
  (:require [sk.models.form :refer [login-form password-form]]
            [sk.handlers.home.model :refer [get-active-eventos]]
            [hiccup.core :refer [html]]))

(defn main-view
  "This creates the login form and we are passing the title from the controller"
  [title]
  (let [href "/home/login"]
    (login-form title href)))

(defn change-password-view
  [title]
  (password-form title))

(defn slideshow-body [row first-id]
  (let [imagen-src (str "uploads/" (:imagen row))]
    (list
     (if (= (:id row) first-id)
       [:div.carousel-item.active [:img.d-block.w-100 {:src imagen-src :alt (:titulo row)}]]
       [:div.carousel-item [:img.d-block.w-100 {:src imagen-src :alt (:titulo row)}]]))))

(defn build-slideshow
  []
  (let [rows (get-active-eventos)
        first-id (-> rows
                     first
                     :id)]
    (list
     [:div#carouselExample.carousel.slide {:data-bs-ride "carousel"}
      [:div.carousel-inner
       (doall (map (partial (fn [row]
                              (slideshow-body row first-id))) rows))]
      [:button.carousel-control-prev {:type "button"
                                      :data-bs-target "#carouselExample"
                                      :data-bs-slide "prev"}
       [:span.carousel-control-prev-icon {:aria-hidden "true"}]
       [:span.visually-hidden "Previous"]]
      [:button.carousel-control-next {:type "button"
                                      :data-bs-target "#carouselExample"
                                      :data-bs-slide "next"}
       [:span.carousel-control-next-icon {:aria-hidden "true"}]
       [:span.visually-hidden "Next"]]])))

(defn carousel-view []
  (html
   [:div.container
    (build-slideshow)]))

(comment
  (carousel-view))