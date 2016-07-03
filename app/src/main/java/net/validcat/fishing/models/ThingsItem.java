package net.validcat.fishing.models;

public class ThingsItem {

    private String itemName;
    private boolean provided;

    public ThingsItem() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isProvided() {
        return provided;
    }

    public void setProvided(boolean provided) {
        this.provided = provided;
    }
}
