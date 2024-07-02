package org.StaionaryInventory.Mainverticle;
import io.vertx.core.json.JsonObject;

public class StationaryItem {
    private String itemId;
    private String name;
    private String category;
    private double price;
    private int quantityInStock;

    public StationaryItem() {}

    public StationaryItem(String itemId, String name, String category, double price, int quantityInStock) {
        this.itemId = itemId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantityInStock = quantityInStock;
    }

    public JsonObject toJson() {
        return new JsonObject()
                .put("itemId", itemId)
                .put("name", name)
                .put("category", category)
                .put("price", price)
                .put("quantityInStock", quantityInStock);
    }

    public static StationaryItem fromJson(JsonObject json) {
        return new StationaryItem(
                json.getString("itemId"),
                json.getString("name"),
                json.getString("category"),
                json.getDouble("price"),
                json.getInteger("quantityInStock")
        );
    }

    // Getters and setters...
}
