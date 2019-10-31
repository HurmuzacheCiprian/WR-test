package com.worldremit.test.customer;

import com.worldremit.test.purchaseorder.PurchaseOrder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.worldremit.test.customer.MembershipType.*;

@Service
public class CustomerService {

    private final Map<String, Customer> customerStore;

    public CustomerService() {
        customerStore = new ConcurrentHashMap<>();
    }

    public Customer createCustomer(String email) {
        if (customerStore.containsKey(email)) {
            throw new DuplicateCustomerEmailException();
        }
        Customer customer = newCustomer(email);
        customerStore.put(email, customer);
        return customer;
    }

    public Customer getCustomer(String customerId) {
        Customer customer = customerStore.values().stream().filter(c -> c.getId().equals(customerId)).findFirst().orElseThrow(CustomerNotFoundException::new);
        return customerStore.get(customer.getEmail());
    }

    public boolean activateMembership(String customerId, MembershipType membershipType) {
        Customer customer = getCustomer(customerId);
        if(customer.getMembershipType() == NONE) {
            customer.setMembershipType(membershipType);
            return true;
        } else if(customer.getMembershipType() == BOOK_CLUB && membershipType == VIDEO_CLUB) {
            customer.setMembershipType(PREMIUM);
            return true;
        } else if(customer.getMembershipType() == VIDEO_CLUB && membershipType == BOOK_CLUB) {
            customer.setMembershipType(PREMIUM);
            return true;
        }
        return false;
    }

    private Customer newCustomer(String email) {
        Customer customer = new Customer(email);
        customer.setMembershipType(NONE);
        customer.setId(UUID.randomUUID().toString());
        return customer;
    }

    public void addPurchaseOrder(PurchaseOrder purchaseOrder) {
        // here we should do a check to see if a customer has a membership in order to be allowed to purchase stuff
        Customer customer = getCustomer(purchaseOrder.getCustomerId());
        if(customer.getPurchaseOrdersIds() == null) {
            Set<String> purchaseOrderIds = new HashSet<>();
            purchaseOrderIds.add(purchaseOrder.getId());
            customer.setPurchaseOrdersIds(purchaseOrderIds);
        } else {
            customer.getPurchaseOrdersIds().add(purchaseOrder.getId());
        }
    }
}
