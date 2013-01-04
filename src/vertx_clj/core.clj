(ns vertx-clj.core
  (:import (org.vertx.java.core Vertx Handler AsyncResultHandler)
           (org.vertx.java.core.http HttpServerRequest RouteMatcher)
           (org.vertx.java.core.streams Pump)
           (clojure.lang ArityException))
  (:require [clojure.string :as s])
  (:use vertx-clj.utils))

(defn- camelize
  "Converts a string to UpperCamelCase."
  [x]
  (s/join (map s/capitalize (s/split x #"-"))))

(defn deploy-module
  "Deploy a module programmatically. Please see the modules manual for more information
   about modules."
  ([module-name conf]
     (fn [vertx# container#]
       (.deployModule container# module-name (json conf))))
  ([module-name conf instances]
     (fn [vertx# container#]
       (.deployModule container# module-name (json conf) instances)))
  ([module-name conf instances done-handler]
     (fn [vertx# container#]
       (.deployModule container# module-name (json conf) instances done-handler)))  )

(defmacro run-verticles [& verts]
  `(fn [_# container#]
     (doseq [v# (list ~@verts)]
       (doseq [name# (vals (ns-interns v#))]
         (when-let [verticle# (:verticle (meta name#))]
           (.deployVerticle container# verticle#))))))

(defmacro defverticle
  "Define a vertx verticle:

     (defverticle http-server
       (http-listen 8080 \"localhost\"
                    (req-handler http-server [req]
                                 (end-req req \"hello vertx\"))))

   As in a java verticle, vertx and container is available for the body.

   The name is used to generate the Verticle, e.g. the previous verticle
   will generate a HttpServer.class file in your project :compile-path, which can be
   run as ```vertx run HttpServer```. This is transparent to the user if the lein-vertx
   plugin is used to run the verticles."
  [vert body]
  (let [this (gensym "this")
        prefix (gensym "prefix-")
        vert-class (-> vert str camelize)]
    `(do
       (defn ~(vary-meta (symbol (str prefix "start")) assoc :verticle vert-class) [~this]
         (let [vertx# (.getVertx ~this)
               container# (.getContainer ~this)]
           (~body vertx# container#)))
       (gen-class
        :name ~vert-class
        :extends org.vertx.java.deploy.Verticle
        :prefix ~(str prefix)))))

(defmacro handler
  "Syntax sugar to create a ```org.vertx.java.core.Handler``` instance:

     (handler [data]
       (println data))
  "
  [expr & body]
  `(proxy [Handler] []
     (handle ~expr
       ~@body)))

(defmacro deploy-verticles
  "Take a list of verticles and deploy them programmatically."
  [& args]
  `(defverticle ~(gensym "container")
     (run-verticles ~@args)))

(defmacro data-handler
  "Create a handler and attach it the ```.dataHandler``` callback of the first argument,
   usually a socket"
  [sock expr & body]
  `(.dataHandler ~sock
                 (handler ~expr ~@body)))

(defmacro frame-handler
  "The same as ```data-handler```, except the passed in data is devided by a delimited record parser."
  [sock expr & body]
  `(.dataHandler ~sock
                 (RecordParser/newDelimited "\n"
                                            (handler ~expr ~@body))))

(defn pump
  "Create a Pump on the passed two sockets, takes an optional parameter for whether start the pump
   immediately, Default false."
  ([sock1 sock2]
     (Pump/createPump sock1 sock2))
  ([sock1 sock2 start]
     (if start
       (.start (pump sock1 sock2))
       (pump sock1 sock2))))
