(ns sessiontest.handler
  (:use [ring.middleware.json :only (wrap-json-response)]   ;; use json response
        ring.middleware.session                             ;; use session
        [ring.util.response :only (response)]               ;; use response
        [hiccup.middleware :only (wrap-base-url)])          ;; use hiccup
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [compojure.handler :as handler]                 ;; require handler
            ))

(defroutes app-routes
  (GET "/" {session :session} []
    (let [counter (:counter session 0)
          session (assoc session :counter (inc counter))]
      (-> (response {:the_counter (:counter session)})
          (assoc :session session))))
  (route/not-found "Not Found"))

(def app
  (-> (handler/site
        app-routes {:session {:cookie-name "counter-session"
                              :cookie-attrs {:max-age 3600}}})
      (wrap-base-url)
      (wrap-json-response)
      (wrap-session))
  ;;(wrap-defaults app-routes site-defaults)
  )
