package com.worldremit.test.businessrules;

import com.worldremit.test.engine.ApplicationRule;
import com.worldremit.test.engine.ApplicationRuleOutput;
import com.worldremit.test.purchaseorder.ItemType;
import com.worldremit.test.purchaseorder.PurchaseOrder;
import org.springframework.stereotype.Component;

import static com.worldremit.test.engine.ApplicationRuleOutput.NONE;
import static com.worldremit.test.engine.ApplicationRuleOutput.SHIP_SLIP;

@Component
public class BusinessRule1 implements ApplicationRule<PurchaseOrder, ApplicationRuleOutput> {
    @Override
    public ApplicationRuleOutput apply(PurchaseOrder input) {
        return input.getItemLines().stream().anyMatch(l -> l.getType() == ItemType.MEMBERSHIP) ? ApplicationRuleOutput.APPLY_MEMBERSHIP : NONE;
    }
}
