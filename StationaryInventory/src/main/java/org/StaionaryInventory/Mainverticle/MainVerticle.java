package org.StaionaryInventory.Mainverticle;

import io.vertx.core.Vertx;

public class MainVerticle {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new org.StaionaryInventory.Mainverticle.InvertoryVerticle(), res -> {
            if (res.succeeded()) {
                System.out.println("Deployment id is: " + res.result());
            } else {
                System.out.println("Deployment failed!");
            }
        });
    }
}
