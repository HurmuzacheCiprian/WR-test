package com.worldremit.test.integration;

import com.worldremit.test.customer.Customer;
import com.worldremit.test.customer.CustomerService;
import com.worldremit.test.customer.MembershipType;
import com.worldremit.test.engine.RulesEngine;
import com.worldremit.test.purchaseorder.ItemLine;
import com.worldremit.test.purchaseorder.ItemType;
import com.worldremit.test.purchaseorder.PurchaseOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.worldremit.test.customer.MembershipType.*;
import static com.worldremit.test.purchaseorder.Status.PROCESSED;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(value = "classpath:application.yml")
public class RulesEngineIntegrationTest {

    @Autowired
    private RulesEngine underTest;

    @Autowired
    private CustomerService customerService;

    @Test
    @DisplayName("IT - Should activate a BOOK CLUB membership to the customer account if the customer has no memberships")
    public void test1() {
        Customer customer = customerService.createCustomer(UUID.randomUUID().toString() + "email@email.com");
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setCustomerId(customer.getId());
        purchaseOrder.setItemLines(createItemList(purchaseOrder, "BOOK_CLUB", ItemType.MEMBERSHIP));

        Customer updatedCustomer = customerService.getCustomer(customer.getId());
        assertEquals(NONE, updatedCustomer.getMembershipType());

        PurchaseOrder processed = underTest.apply(purchaseOrder);

        assertEquals(PROCESSED, processed.getStatus());

        Customer updatedCustomer1 = customerService.getCustomer(customer.getId());
        assertEquals(BOOK_CLUB, updatedCustomer1.getMembershipType());
    }

    @Test
    @DisplayName("IT - Should activate a PREMIUM membership to the customer account if the customer already has BOOK membership and the purchase order contains VIDEO membership")
    public void test2() {
        Customer customer = customerService.createCustomer(UUID.randomUUID().toString() + "email@email.com");
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setCustomerId(customer.getId());
        purchaseOrder.setItemLines(createItemList(purchaseOrder, "BOOK_CLUB", ItemType.MEMBERSHIP));

        //apply book membership
        underTest.apply(purchaseOrder);
        Customer updatedCustomer1 = customerService.getCustomer(customer.getId());
        assertEquals(BOOK_CLUB, updatedCustomer1.getMembershipType());

        PurchaseOrder purchaseOrder1 = new PurchaseOrder();
        purchaseOrder1.setCustomerId(customer.getId());
        purchaseOrder1.setItemLines(createItemList(purchaseOrder1, "VIDEO_CLUB", ItemType.MEMBERSHIP));

        //apply video membership
        underTest.apply(purchaseOrder1);

        Customer updatedCustomer2 = customerService.getCustomer(customer.getId());
        assertEquals(PREMIUM, updatedCustomer2.getMembershipType());

    }

    @Test
    @DisplayName("IT - Should add Basic First Aid training video to the purchase order if the purchase order contains Comprehensive First Aid Training video")
    public void test3() {

        Customer customer = customerService.createCustomer(UUID.randomUUID().toString() + "email@email.com");

        PurchaseOrder purchaseOrder1 = new PurchaseOrder();
        purchaseOrder1.setCustomerId(customer.getId());
        purchaseOrder1.setItemLines(createItemList(purchaseOrder1, "VIDEO_CLUB", ItemType.MEMBERSHIP));

        //apply video membership
        underTest.apply(purchaseOrder1);
        Customer updatedCustomer1 = customerService.getCustomer(customer.getId());
        assertEquals(VIDEO_CLUB, updatedCustomer1.getMembershipType());


        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId("1");
        purchaseOrder.setCustomerId(customer.getId());
        purchaseOrder.setItemLines(createItemList(purchaseOrder, "Comprehensive First Aid Training", ItemType.VIDEO));

        //set purchase order to comprehensive first aid training video
        underTest.apply(purchaseOrder);

        Customer updatedCustomer2 = customerService.getCustomer(customer.getId());
        assertEquals(1, updatedCustomer2.getPurchaseOrdersIds().size());
        // here I would also do purchaseOrderService.getPurchaseOrder(id) and I would assert to check that there are 2 item lines added,
        // one for Basic video and another one for Comprehensive video

    }

    private List<ItemLine> createItemList(PurchaseOrder purchaseOrder, String title, ItemType itemType) {
        ItemLine itemLine = new ItemLine();
        itemLine.setId(UUID.randomUUID().toString());
        itemLine.setTitle(title);
        itemLine.setPurchaseOrderId(purchaseOrder.getId());
        itemLine.setType(itemType);


        List<ItemLine> items = new ArrayList<>();
        items.add(itemLine);

        return items;
    }
}
