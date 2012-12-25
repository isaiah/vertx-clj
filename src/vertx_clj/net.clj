(ns vertx-clj.net
  (:use vertx-clj.core))

(defmacro sock-listen [port host & body]
  `(fn [vertx# _#]
     (let [sock-server# (.createNetServer vertx#)]
       ((fn [~'vertx ~'sock-server] ~@body) vertx# sock-server#)
       (.listen sock-server# ~port ~host))))

(defmacro sock-connect [port host body]
  `(fn [vertx# container#]
     (let [client# (.createNetClient vertx#)]
       (.connect client# ~port ~host ~body))))

(defmacro connect-handler [sock-server expr & body]
  `(.connectHandler ~sock-server
                    (handler ~expr ~@body)))

(defmacro closed-handler [sock expr & body]
  `(.closedHandler ~sock
                  (handler ~expr ~@body)))
