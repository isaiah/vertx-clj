(ns vertx-clj.sockjs
  (:import [org.vertx.java.core.json JsonObject JsonArray])
  (:use vertx-clj.utils))

;; Helper methods for ```SockJSServer```.

(declare bound-obj prefix)

(defn install-app
  "Setting up handlers for requests from sockjs client."
  [sockjs-server pfx handler]
  (.installApp sockjs-server (prefix pfx) handler))

(defn bridge
  "Setting up SockJS EventBus bridge."
  [sockjs-server pfx inbound outbound]
  (let [in-permitted (bound-obj inbound)
        out-permitted (bound-obj outbound)
        eb-prefix (prefix pfx)]
    (.bridge sockjs-server eb-prefix in-permitted out-permitted)))

(defn- ^JsonArray bound-obj [clj-map]
  (-> (JsonArray.) (.add (json clj-map))))

(defn- ^JsonObject prefix [s]
  (-> (JsonObject.) (.putString "prefix" s)))
