(ns vertx-clj.route-matcher
  (:refer-clojure :exclude [get])
  (:import [org.vertx.java.core Handler]
           [org.vertx.java.core.http RouteMatcher]))

;; Helper methods for ```RouteMatcher```, check vertx docuement for
;; more info on route pattern matching.

(defn get
  [^RouteMatcher router path req-fn]
  (.get router path
    (proxy [Handler] []
      (handle [req]
        (req-fn req)))))

(defn get-with-regexp
  [^RouteMatcher router path req-fn]
  (.getWithRegEx router path
    (proxy [Handler] []
      (handle [req]
        (req-fn req)))))
