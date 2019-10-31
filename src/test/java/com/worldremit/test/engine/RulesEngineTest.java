package com.worldremit.test.engine;

import com.worldremit.test.businessrules.BusinessRule1;
import com.worldremit.test.businessrules.BusinessRule2;
import com.worldremit.test.businessrules.BusinessRule3;
import com.worldremit.test.customer.Customer;
import com.worldremit.test.customer.CustomerService;
import com.worldremit.test.customer.MembershipType;
import com.worldremit.test.purchaseorder.ItemLine;
import com.worldremit.test.purchaseorder.ItemType;
import com.worldremit.test.purchaseorder.PurchaseOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.worldremit.test.purchaseorder.ItemType.BOOK;
import static com.worldremit.test.purchaseorder.ItemType.VIDEO;
import static com.worldremit.test.purchaseorder.Status.NOT_PROCESSED;
import static com.worldremit.test.purchaseorder.Status.PROCESSED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RulesEngineTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private RulesEngine underTest;

    @BeforeEach
    public void init() {
        underTest = new RulesEngine(customerService, new BusinessRule1(), new BusinessRule2(), new BusinessRule3());
    }

    @Test
    @DisplayName("Should process purchase and ship a slip if the purchase contains a physical book product")
    public void shouldProcessPurchaseOrderAndApplyBusinessRule1() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setItemLines(createItemList("Book title", BOOK));

        PurchaseOrder processed = underTest.apply(purchaseOrder);

        assertEquals(PROCESSED, processed.getStatus());
    }

    @Test
    @DisplayName("Should process purchase and ship a slip if the purchase contains a physical video product")
    public void shouldProcessPurchaseOrderForBusinessRule2() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setItemLines(createItemList("Video title", VIDEO));
        PurchaseOrder processed = underTest.apply(purchaseOrder);

        assertEquals(PROCESSED, processed.getStatus());
    }

    @Test
    @DisplayName("Should activate a BOOK CLUB membership to the customer account if the customer has no memberships")
    public void shouldNotProcessOrderIfBusinessRulesAreNotMet() {
        Customer customer = createCustomer();
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setCustomerId(customer.getId());
        purchaseOrder.setItemLines(createItemList("BOOK_CLUB", ItemType.MEMBERSHIP));

        when(customerService.getCustomer(purchaseOrder.getCustomerId())).thenReturn(customer);
        when(customerService.activateMembership(customer.getId(), MembershipType.BOOK_CLUB)).thenReturn(true);

        PurchaseOrder processed = underTest.apply(purchaseOrder);

        assertEquals(PROCESSED, processed.getStatus());
    }

    private List<ItemLine> createItemList(String title, ItemType itemType) {
        ItemLine itemLine = new ItemLine();
        itemLine.setTitle(title);
        itemLine.setPurchaseOrderId("1234");
        itemLine.setType(itemType);


        List<ItemLine> items = new ArrayList<>();
        items.add(itemLine);

        return items;
    }

    private Customer createCustomer() {
        final String id = UUID.randomUUID().toString();

        Customer customer = new Customer(id + "email@email.com");
        customer.setMembershipType(MembershipType.BOOK_CLUB);
        customer.setId(id);
        customer.setMembershipType(MembershipType.NONE);
        return customer;
    }

}