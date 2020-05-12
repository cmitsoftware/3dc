package org.climbing.util;

import org.climbing.domain.Configurations;
import org.climbing.domain.Subscription;
import org.climbing.domain.SubscriptionType;
import org.climbing.repo.BaseHibernateDAO;
import org.climbing.repo.ConfigurationsDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SubscriptionUtil {

    private final static Integer MAX_NUMBER_OF_SUBSCRIPTION_TYPES = 5;
    private final static String SUBSCRIPTION_TYPE_CONFIGURATION_KEY_PREFIX = "subscription_type_";
    private final static String SUBSCRIPTION_TYPE_CONFIGURATION_VALUE_NAME = "name";
    private final static String SUBSCRIPTION_TYPE_CONFIGURATION_VALUE_STARTMONTH = "start_month";
    private final static String SUBSCRIPTION_TYPE_CONFIGURATION_VALUE_ENDMONTH = "end_month";

    private Integer savedSubscriptionTypesNumber = -1;

    @Autowired
    private ConfigurationsDAO configurationsDAO;

    private static final Logger log = LoggerFactory.getLogger(SubscriptionUtil.class);

    public List<SubscriptionType> getSubscriptionTypes() {

        List<SubscriptionType> subscriptionTypes = new ArrayList<>();
        int i=0;
        try {
            for (; i < (savedSubscriptionTypesNumber.equals(-1) ? MAX_NUMBER_OF_SUBSCRIPTION_TYPES : savedSubscriptionTypesNumber); i++ ) {
                Configurations subscriptionTypeConfiguration = configurationsDAO.findByKey(SUBSCRIPTION_TYPE_CONFIGURATION_KEY_PREFIX + i);
                try {
                    subscriptionTypes.add(buildSubscriptionTypeFromConfiguration(subscriptionTypeConfiguration));
                } catch (Exception e1) {
                    log.error("Configuration " + SUBSCRIPTION_TYPE_CONFIGURATION_KEY_PREFIX + i + " is not well formed. " + e1.getMessage());
                }
            }
        } catch (Exception e) {
            // fetched all the subscription types in DB
            log.info("Subscriptions types found in configurations: " + i);
            savedSubscriptionTypesNumber = i;
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
            if (keyValue.getKey().startsWith(SUBSCRIPTION_TYPE_CONFIGURATION_VALUE_NAME)) {
                subscriptionType.setName(keyValue.getValue());
            }
            if (keyValue.getKey().startsWith(SUBSCRIPTION_TYPE_CONFIGURATION_VALUE_STARTMONTH)) {
                subscriptionType.setStartMonth(Integer.parseInt(keyValue.getValue()));
            }
            if (keyValue.getKey().startsWith(SUBSCRIPTION_TYPE_CONFIGURATION_VALUE_ENDMONTH)) {
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

    public static boolean isSubscriptionCurrentlyValid(Subscription subscription) {

        if (subscription != null) {
            return isSubscriptionCurrentlyValid(subscription.getStartDate(), subscription.getEndDate());
        }
        return false;
    }

    public static boolean isSubscriptionCurrentlyValid(Date startDate, Date endDate) {

        if (startDate != null && endDate != null) {
            LocalDate today = LocalDate.now(ZoneId.systemDefault());
            LocalDate startLocalDate = Instant.ofEpochMilli(startDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endLocalDate = Instant.ofEpochMilli(endDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            if (today.compareTo(startLocalDate) >= 0 && today.compareTo(endLocalDate) <= 0) {
                return true;
            }
        }
        return false;
    }

    public Set<Subscription> filterSubscriptions(Set<Subscription> subscriptions) {

        List<SubscriptionType> subscriptionTypes = getSubscriptionTypes();
        Set<Subscription> filteredSubscriptions = new HashSet<>();
        for (Subscription subscription : (subscriptions != null ? subscriptions : new ArrayList<Subscription>())){
            if (subscriptionTypes != null ? subscriptionTypes.contains(subscription.getTypeName()) : false) {
                filteredSubscriptions.add(subscription);
            }
        }
        return filteredSubscriptions;
    }

    public Subscription buildSubscriptionFromType(SubscriptionType subscriptionType, Integer referenceYear) {

        Subscription subscription = new Subscription();
        subscription.setTypeName(subscriptionType.getName());
        subscription.setReferenceYear(referenceYear);
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.YEAR, referenceYear);
        startDate.set(Calendar.DAY_OF_MONTH, 1);
        startDate.set(Calendar.MONTH, subscriptionType.getStartMonth()-1);
        startDate.set(Calendar.HOUR_OF_DAY, 6);
        subscription.setStartDate(new Date(startDate.getTimeInMillis()));
        Integer monthsOfValidityOfSubscriptionType = getMonthsOfValidityOfSubscriptionType(subscriptionType);
        Calendar endDate = (Calendar)startDate.clone();
        endDate.add(Calendar.MONTH, monthsOfValidityOfSubscriptionType-1);
        endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH)); //set to last day of month
        subscription.setEndDate(new Date(endDate.getTimeInMillis()));
        return subscription;
    }

    private Integer getMonthsOfValidityOfSubscriptionType(SubscriptionType subscriptionType) {
        Integer startMonth = subscriptionType.getStartMonth();
        Integer endMonth = subscriptionType.getEndMonth();
        if (endMonth > startMonth) {
            return (endMonth - startMonth) + 1;
        } else if (endMonth == startMonth) {
            return  1;
        } else {
            return ((12-startMonth) + endMonth) + 1;
        }
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
