(ns vertx-clj.file
  (:use vertx-clj.core)
  (:import [org.vertx.java.core AsyncResultHandler]))

(defn open-file [vertx filename handler]
  (-> vertx .fileSystem
      (.open filename handler)))

(defn close-file [f callback]
  (.close f callback))

(defmacro async-result-handler [expr & body]
  `(proxy [AsyncResultHandler] []
    (handle ~expr
      ~@body)))
