(ns vertx.container
  (:use vertx-clj.core)
  (:require (vertx.http client server)))

;;; This example shows how to deploy verticles programatically
(deploy-verticles 'vertx.http.server 'vertx.http.client)
