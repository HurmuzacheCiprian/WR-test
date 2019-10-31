package com.worldremit.test.purchaseorder;

public class ItemLine {
    private String id;
    private String title;
    private ItemType type;
    private String purchaseOrderId;

    public ItemType getType() {
        return type;
    }

    public String getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public void setPurchaseOrderId(String purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public void setId(String id) {
        this.id = id;
    }
}
