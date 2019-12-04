////////////////////////////////////////////////////////////////////
// TOMMASO AZZALIN 1169740
////////////////////////////////////////////////////////////////////
package it.unipd.tos.model;

import it.unipd.tos.model.ItemType;

public class MenuItem {
    private ItemType itemType;
    private String name;
    private double price;

    public MenuItem(ItemType itemType, String name, double price) {
        this.itemType = itemType;
        this.name = name;
        this.price = price;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}