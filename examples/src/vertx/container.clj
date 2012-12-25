(ns vertx.container
  (:use vertx.core)
  (:require (vertx.http client server)))

(deploy-verticles 'vertx.http.server 'vertx.http.client)
