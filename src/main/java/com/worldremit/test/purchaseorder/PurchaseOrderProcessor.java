package com.worldremit.test.purchaseorder;

import com.worldremit.test.engine.RulesEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderProcessor {

    private final RulesEngine rulesEngine;

    @Autowired
    public PurchaseOrderProcessor(RulesEngine rulesEngine) {
        this.rulesEngine = rulesEngine;
    }

    public void processPurchaseOrder(PurchaseOrder purchaseOrder) {
        rulesEngine.apply(purchaseOrder);
    }
}
