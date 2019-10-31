package com.worldremit.test.purchaseorder;

import java.util.List;

public class PurchaseOrder {
    private String id;
    private String customerId;
    private long total;
    private List<ItemLine> itemLines;
    private Status status;

    public List<ItemLine> getItemLines() {
        return itemLines;
    }

    public String getCustomerId() {
        return customerId;
    }

    public long getTotal() {
        return total;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setItemLines(List<ItemLine> itemLines) {
        this.itemLines = itemLines;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
