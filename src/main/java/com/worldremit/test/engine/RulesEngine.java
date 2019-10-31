package com.worldremit.test.engine;

import com.worldremit.test.businessrules.BusinessRule2;
import com.worldremit.test.businessrules.BusinessRule1;
import com.worldremit.test.businessrules.BusinessRule3;
import com.worldremit.test.customer.Customer;
import com.worldremit.test.customer.CustomerService;
import com.worldremit.test.customer.MembershipType;
import com.worldremit.test.purchaseorder.ItemLine;
import com.worldremit.test.purchaseorder.ItemType;
import com.worldremit.test.purchaseorder.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.worldremit.test.engine.ApplicationRuleOutput.NONE;
import static com.worldremit.test.purchaseorder.Status.NOT_PROCESSED;
import static com.worldremit.test.purchaseorder.Status.PROCESSED;

@Service
public class RulesEngine {
    private final List<ApplicationRule<PurchaseOrder, ApplicationRuleOutput>> rules;
    private final CustomerService customerService;

    @Autowired
    public RulesEngine(CustomerService customerService,
                       BusinessRule1 businessRule1,
                       BusinessRule2 businessRule2,
                       BusinessRule3 businessRule3) {
        this.rules = new ArrayList<>();
        this.customerService = customerService;

        rules.add(businessRule1);
        rules.add(businessRule2);
        rules.add(businessRule3);
    }

    public PurchaseOrder apply(PurchaseOrder purchaseOrder) {
        purchaseOrder.setStatus(NOT_PROCESSED);
        boolean wasProcessed = false;
        for (ApplicationRule<PurchaseOrder, ApplicationRuleOutput> a : rules) {
            ApplicationRuleOutput output = a.apply(purchaseOrder);
            if (output != NONE) {
                switch (output) {
                    case SHIP_SLIP:
                        wasProcessed = applyShippingRule(purchaseOrder);
                        break;
                    case APPLY_MEMBERSHIP:
                        wasProcessed = applyMembershipRule(purchaseOrder);
                        break;
                    case COMPREHENSIVE_VIDEO:
                        wasProcessed = applyBasicAidVideoRule(purchaseOrder);
                        break;
                    default:
                        System.out.println("Needs to be implemented");
                        break;
                }
            }
        }
        if (wasProcessed) {
            purchaseOrder.setStatus(PROCESSED);
        }
        return purchaseOrder;
    }

    private boolean applyShippingRule(PurchaseOrder purchaseOrder) {
        System.out.println("Shipping a slip to the customer");
        customerService.addPurchaseOrder(purchaseOrder);
        // shipSlipService.shipSlip(purchaseOrder)
        return true; // if something fails with the line above we would return false
    }

    private boolean applyBasicAidVideoRule(PurchaseOrder purchaseOrder) {
        ItemLine itemLine = new ItemLine();
        itemLine.setId(UUID.randomUUID().toString());
        itemLine.setPurchaseOrderId(purchaseOrder.getId());
        itemLine.setTitle("Basic Video Training");
        itemLine.setType(ItemType.VIDEO);
        purchaseOrder.getItemLines().add(itemLine);
        System.out.println("Added comprehensive video");
        customerService.addPurchaseOrder(purchaseOrder);
        return true;    // if something fails with saving of the purchase order we would return false;
    }

    private boolean applyMembershipRule(PurchaseOrder purchaseOrder) {
        Customer customer = customerService.getCustomer(purchaseOrder.getCustomerId());
        MembershipType membershipType = purchaseOrder.getItemLines()
                .stream()
                .filter(l -> l.getType() == ItemType.MEMBERSHIP)
                .map(l -> MembershipType.valueOf(l.getTitle()))
                .findFirst().orElse(MembershipType.NONE);
        customerService.activateMembership(customer.getId(), membershipType);
        System.out.println("Activated membership");
        return true; // if something fails with finding the customer of activating the membership we would return false;
    }
}
