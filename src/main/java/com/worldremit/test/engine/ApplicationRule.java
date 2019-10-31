package com.worldremit.test.engine;

import com.worldremit.test.purchaseorder.PurchaseOrder;

public interface ApplicationRule<I extends PurchaseOrder, O extends ApplicationRuleOutput> {
    O apply(I input);
}
