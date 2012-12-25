(ns vertx.http.server
  (:require [vertx.core :as c])
  (:use  [clojure.tools.logging :only [info]]))

(c/defverticle http-server
  (c/http-listen 8080 "localhost"
                 (v/req-handler http-server [req]
                                (info "got request:" (.uri req))
                                (info "headers:")
                                (doseq [[k v] (c/headers req)]
                                  (info k ":" v))
                                (-> req .response .headers (.put "Content-Type" "text/html; charset=UTF-8"))
                                (-> req .response (.end "<html><body><h1>Hello from vertx!</h1></body></html>")))))
