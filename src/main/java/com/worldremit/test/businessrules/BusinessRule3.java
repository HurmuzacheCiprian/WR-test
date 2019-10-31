package com.worldremit.test.businessrules;

import com.worldremit.test.engine.ApplicationRule;
import com.worldremit.test.engine.ApplicationRuleOutput;
import com.worldremit.test.purchaseorder.ItemLine;
import com.worldremit.test.purchaseorder.PurchaseOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.worldremit.test.engine.ApplicationRuleOutput.COMPREHENSIVE_VIDEO;
import static com.worldremit.test.engine.ApplicationRuleOutput.NONE;
import static com.worldremit.test.purchaseorder.ItemType.VIDEO;

@Component
public class BusinessRule3 implements ApplicationRule<PurchaseOrder, ApplicationRuleOutput> {

    @Value("${application.rewards.videos.name1}")
    private String COMPREHENSIVE_FIRST_AID_TRAINING;

    @Override
    public ApplicationRuleOutput apply(PurchaseOrder input) {
        ItemLine item = input.getItemLines().stream()
                .filter(l -> l.getType() == VIDEO)
                .filter(l -> l.getTitle().equals(COMPREHENSIVE_FIRST_AID_TRAINING))
                .findFirst().orElse(null);
        return item == null ? NONE : COMPREHENSIVE_VIDEO;
    }
}
