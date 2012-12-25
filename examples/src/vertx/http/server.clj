(ns vertx.http.server
  (:require [vertx-clj.core :as c])
  (:use [vertx-clj http])
  (:use  [clojure.tools.logging :only [info]]))

(c/defverticle http-server
  (http-listen 8080 "localhost"
                 (req-handler http-server [req]
                                (info "got request:" (.uri req))
                                (info "headers:")
                                (doseq [[k v] (headers req)]
                                  (info k ":" v))
                                (-> req .response .headers (.put "Content-Type" "text/html; charset=UTF-8"))
                                (-> req .response (.end "<html><body><h1>Hello from vertx!</h1></body></html>")))))
