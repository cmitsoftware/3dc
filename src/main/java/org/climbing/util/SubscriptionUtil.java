package org.climbing.util;

import org.climbing.domain.Configurations;
import org.climbing.domain.SubscriptionType;
import org.climbing.repo.BaseHibernateDAO;
import org.climbing.repo.ConfigurationsDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubscriptionUtil {

    private final static Integer MAX_NUMBER_OF_SUBSCRIPTION_TYPES = 5;
    private final static String SUBSCRIPTION_TYPE_CONFIGURATION_KEY_PREFIX = "subscription_type_";

    @Autowired
    private ConfigurationsDAO configurationsDAO;

    private static final Logger log = LoggerFactory.getLogger(SubscriptionUtil.class);

    public List<SubscriptionType> getSubscriptionTypes() {

        return buildSubscriptionTypesFromConfigurations();
    }

    private List<SubscriptionType> buildSubscriptionTypesFromConfigurations() {

        List<SubscriptionType> subscriptionTypes = new ArrayList<>();
        try {
            for (int i = 0; i < MAX_NUMBER_OF_SUBSCRIPTION_TYPES; i++ ) {
                Configurations subscriptionTypeConfiguration = configurationsDAO.findByKey(SUBSCRIPTION_TYPE_CONFIGURATION_KEY_PREFIX + i);
                try {
                    subscriptionTypes.add(buildSubscriptionTypeFromConfiguration(subscriptionTypeConfiguration));
                } catch (Exception e1) {
                    log.error("Configuration " + SUBSCRIPTION_TYPE_CONFIGURATION_KEY_PREFIX + i + " is not well formed. " + e1.getMessage());
                }
            }
        } catch (Exception e) {
            // fetched all the subscription types in DB
        }
        return subscriptionTypes;
    }

    /**
     * e.g. name=gennaio-luglio;start_month=1;end_month=7
     */
    private SubscriptionType buildSubscriptionTypeFromConfiguration(Configurations subscriptionTypeConfiguration) throws Exception {

        String value = subscriptionTypeConfiguration.getValue();
        if (value != null ? value.isEmpty() : true) {
            throw new Exception("Value is null or empty");
        }
        String[] elements = value.split(";");
        if (elements.length != 3) {
            throw new Exception("Value elements number is not correct");
        }
        SubscriptionType subscriptionType = new SubscriptionType();
        for (String element: elements) {
            KeyValue keyValue = getElementKeyValue(element);
            if (keyValue.getKey().startsWith("name")) {
                subscriptionType.setName(keyValue.getValue());
            }
            if (keyValue.getKey().startsWith("start_month")) {
                subscriptionType.setStartMonth(Integer.parseInt(keyValue.getValue()));
            }
            if (keyValue.getKey().startsWith("end_month")) {
                subscriptionType.setEndMonth(Integer.parseInt(keyValue.getValue()));
            }
        }
        return subscriptionType;
    }

    private KeyValue getElementKeyValue(String configurationValueElement) throws Exception {

        String elements[] = configurationValueElement.split("=");
        if (elements.length != 2) {
            throw new Exception("Value elements number is not correct");
        }
        KeyValue keyValue = new KeyValue();
        keyValue.setKey(elements[0].trim().toLowerCase());
        keyValue.setValue(elements[1].trim().toLowerCase());
        return keyValue;
    }
}

class KeyValue {

    String key;
    String value;

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
