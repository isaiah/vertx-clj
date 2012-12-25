(ns vertx-clj.core
  (:import (org.vertx.java.core Vertx Handler AsyncResultHandler)
           (org.vertx.java.core.http HttpServerRequest RouteMatcher)
           (org.vertx.java.core.streams Pump)
           (clojure.lang ArityException))
  (:require [clojure.string :as s]))

(defn- verticlize [x]
  (s/join (map s/capitalize (s/split x #"-"))))

(defmacro run-verticles [& verts]
  `(fn [_# container#]
     (doseq [v# (list ~@verts)]
       (doseq [name# (vals (ns-interns v#))]
         (when-let [verticle# (:verticle (meta name#))]
           (.deployVerticle container# verticle#))))))

(defmacro defverticle
  "Define a vertx verticle instance."
  [vert body]
  (let [this (gensym "this")
        prefix (gensym "prefix-")
        vert-class (-> vert str verticlize)]
    `(do
       (defn ~(vary-meta (symbol (str prefix "start")) assoc :verticle vert-class) [~this]
         (let [vertx# (.getVertx ~this)
               container# (.getContainer ~this)]
           (~body vertx# container#)))
       (gen-class
        :name ~vert-class
        :extends org.vertx.java.deploy.Verticle
        :prefix ~(str prefix)))))

(defmacro handler [expr & body]
  `(proxy [Handler] []
    (handle ~expr
      ~@body)))

(defmacro deploy-verticles [& args]
  `(defverticle ~(gensym "container")
     (run-verticles ~@args)))

(defmacro data-handler [sock expr & body]
  `(.dataHandler ~sock
                 (handler ~expr ~@body)))

(defmacro frame-handler [sock expr & body]
  `(.dataHandler ~sock
                 (RecordParser/newDelimited "\n"
                                            (handler ~expr ~@body))))

(defn pump
  ([sock1 sock2]
     (Pump/createPump sock1 sock2))
  ([sock1 sock2 start]
     (if start
       (.start (pump sock1 sock2))
       (pump sock1 sock2))))
