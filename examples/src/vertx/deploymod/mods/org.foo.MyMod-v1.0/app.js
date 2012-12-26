load("vertx.js")

var logger = vertx.logger;
logger.info("in MyMod!")
logger.info("some-var is " + vertx.config['some-var'])
