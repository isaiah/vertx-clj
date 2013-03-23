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

Add [vertx-clj "0.1.1-SNAPSHOT"] to your project.clj

Checkout [examples
directory](https://github.com/isaiah/vertx-clj/tree/master/examples) for
more examples.

## License

Copyright © 2012 Isaiah P.

Distributed under the Eclipse Public License, the same as Clojure.
