vertx-clj is out-dated, please checkout the official [mod=lang-clojure[(https://github.com/vert-x/mod-lang-clojure) project.

# vertx-clj

A clojure wrapper for vertx development.

## Usage

Echo server:

```clojure
(ns vertx.echo.server
  (:use (vertx-clj core net)))

(defverticle echo-server
  (sock-listen 1234 "localhost"
               (connect-handler sock-server [sock]
                                (pump sock sock true))))
```

Echo client:
```clojure
(ns vertx.echo.client
  (:use [vertx-clj core net]
        [clojure.tools.logging :only [info]])
  (:import (org.vertx.java.core.buffer Buffer)))

(defverticle echo-client
  (sock-connect 1234 "localhost"
                (handler [sock]
                         (.dataHandler sock
                                       (handler [buf]
                                                (info "net client receiving:" buf)))
                         (doseq [i (range 10)]
                           (let [s (str "hello" i "\n")]
                             (info "net client sending:" s)
                             (->> s (Buffer.) (.write sock)))))))
```

Sample project.clj

```clj
(defproject vertx-examples "0.1.0-SNAPSHOT"
  :description "Vertx examples in clojure"
  :url "http://vertx.io"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/core.match "0.2.0-alpha11"]
                 [org.clojure/tools.logging "0.2.3"]
                 [org.vert-x/vertx-core "1.3.0.final"]
                 [org.vert-x/vertx-platform "1.3.0.final"]
                 [vertx-clj "0.1.1-SNAPSHOT"]]
  :plugins [[lein-vertx "0.1.0-SNAPSHOT"]]
  :min-lein-version "2.0.0"
)
```

Checkout [examples
directory](https://github.com/isaiah/vertx-clj/tree/master/examples) for
more examples.

## How does it work?

Behind the scene it uses :gen-class to generate wrapper classes for
verticles, and executes them with vertx by the
[lein-vertx](https://github.com/isaiah/lein-vertx) plugin

## Document

http://isaiah.github.io/vertx-clj/

## License

Copyright © 2012 Isaiah P.

Distributed under the Eclipse Public License, the same as Clojure.
