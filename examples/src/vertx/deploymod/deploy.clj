(ns vertx.deploymod.deploy
  (:use [vertx-clj core]
        [clojure.tools.logging :only [info]]))

;; Since this verticle is actually run from :compile-path defined in project.clj (default target/classes)
;; You have to copy the mods directory to :compile-path, it should be ok for modules that is in official
;; repository
(defverticle deploy-mod
  (deploy-module "org.foo.MyMod-v1.0" {:some-var "hello"} 1
                 (handler [deployment-id]
                          (info "This get called when deployment is complete, deployment id is" deployment-id))))
