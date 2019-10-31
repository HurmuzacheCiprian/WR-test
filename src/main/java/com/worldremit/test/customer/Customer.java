package com.worldremit.test.customer;

import com.worldremit.test.purchaseorder.PurchaseOrder;

import java.util.List;
import java.util.Set;

public class Customer {
    private String id;
    private String email;
    private MembershipType membershipType;
    private int points; // to be used for business rules 4 and 5
    private Set<String> purchaseOrdersIds;

    public Customer(String email) {
        this.email = email;
    }


    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public Set<String> getPurchaseOrdersIds() {
        return purchaseOrdersIds;
    }

    public void setPurchaseOrdersIds(Set<String> purchaseOrdersIds) {
        this.purchaseOrdersIds = purchaseOrdersIds;
    }
}
