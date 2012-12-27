(ns vertx-clj.net
  (:use vertx-clj.core))

(defmacro sock-listen
  "Create a ```NetServer``` and listen on specified port and host,
   vertx and sock-server is available in the body."
  [port host & body]
  `(fn [vertx# _#]
     (let [sock-server# (.createNetServer vertx#)]
       ((fn [~'vertx ~'sock-server] ~@body) vertx# sock-server#)
       (.listen sock-server# ~port ~host))))

(defmacro sock-connect
  "Create a ```NetClient```, and connect specified port and host."
  [port host body]
  `(fn [vertx# container#]
     (let [client# (.createNetClient vertx#)]
       (.connect client# ~port ~host ~body))))

(defmacro connect-handler
  "Used by a ```NetServer```, handle new connection."
  [sock-server expr & body]
  `(.connectHandler ~sock-server
                    (handler ~expr ~@body)))

(defmacro closed-handler
  "Used by a ```NetServer```, handle connection close."
  [sock expr & body]
  `(.closedHandler ~sock
                  (handler ~expr ~@body)))
