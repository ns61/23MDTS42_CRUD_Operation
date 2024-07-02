package org.StaionaryInventory.Mainverticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class InvertoryVerticle extends AbstractVerticle {

    private MongoClient mongoClient;

    @Override
    public void start(Promise<Void> startPromise) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        mongoClient = MongoClient.createShared(vertx, new JsonObject()
                .put("connection_string", "mongodb://localhost:27017")
                .put("db_name", "inventory"));

        router.post("/api/items").handler(this::addItem);
        router.get("/api/items/:itemId").handler(this::getItem);
        router.put("/api/items/:itemId").handler(this::updateItem);
        router.delete("/api/items/:itemId").handler(this::deleteItem);

        vertx.createHttpServer().requestHandler(router).listen(8080, http -> {
            if (http.succeeded()) {
                startPromise.complete();
            } else {
                startPromise.fail(http.cause());
            }
        });
    }

    private void addItem(RoutingContext context) {
        JsonObject item = context.getBodyAsJson();
        mongoClient.save("items", item, res -> {
            if (res.succeeded()) {
                context.response().setStatusCode(201).end();
            } else {
                context.response().setStatusCode(500).end();
            }
        });
    }

    private void getItem(RoutingContext context) {
        String itemId = context.pathParam("itemId");
        mongoClient.findOne("items", new JsonObject().put("itemId", itemId), null, res -> {
            if (res.succeeded()) {
                if (res.result() != null) {
                    context.response().setStatusCode(200).end(res.result().encode());
                } else {
                    context.response().setStatusCode(404).end();
                }
            } else {
                context.response().setStatusCode(500).end();
            }
        });
    }

    private void updateItem(RoutingContext context) {
        String itemId = context.pathParam("itemId");
        JsonObject item = context.getBodyAsJson();
        mongoClient.updateCollection("items", new JsonObject().put("itemId", itemId),
                new JsonObject().put("$set", item), res -> {
                    if (res.succeeded()) {
                        context.response().setStatusCode(200).end();
                    } else {
                        context.response().setStatusCode(500).end();
                    }
                });
    }

    private void deleteItem(RoutingContext context) {
        String itemId = context.pathParam("itemId");
        mongoClient.removeDocument("items", new JsonObject().put("itemId", itemId), res -> {
            if (res.succeeded()) {
                context.response().setStatusCode(204).end();
            } else {
                context.response().setStatusCode(500).end();
            }
        });
    }
}
