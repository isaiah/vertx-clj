(ns vertx.route-matcher
  (:refer-clojure :exclude [get])
  (:import (org.vertx.java.core Handler)))

(defn get [router path req-fn]
  (.get router path
    (proxy [Handler] []
      (handle [req]
        (req-fn req)))))

(defn get-with-regexp [router path req-fn]
  (.getWithRegEx router path
    (proxy [Handler] []
      (handle [req]
        (req-fn req)))))