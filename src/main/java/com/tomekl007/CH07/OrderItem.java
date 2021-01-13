package com.tomekl007.CH07;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

public class OrderItem {
    private static final Period RETURNS_PERIOD = Period.ofMonths(3);
    
    public ShippingDetails getShippingDetails() {
        return null;
    }

    public Order getOrder() {
        return null;
    }

    public LocalDate getFinalReturnsDate() {
        Instant shippingTime = getShippingDetails().getWarehouseExitTime();
        ZoneId deliveryTimeZone = getOrder().getDeliveryAddress().getTimeZone();
        return getFinalReturnsDate(shippingTime, deliveryTimeZone);
    }

    /**
     * Computes the final date on which this item can be returned in the simple
     * "click a button" workflow. This is base on the date on which the item is
     * shipped from the warehouse, from the perspective of the delivery location.
     * The returns period (currently three months; see {@link #RETURNS_PERIOD}) is
     * added to the shipping date to obtain the final returns date. When adding
     * the returns period, if the day-of-month goes beyond the end of the resulting
     * month, the result should be the start of the following month.
     * 
     * @param shippingTime The instant at which the item shipped from the warehouse.
     * @param destinationTimeZone The time zone where the item will be delivered.
     * @return
     */
    @VisibleForTesting
    static LocalDate getFinalReturnsDate(Instant shippingTime, ZoneId destinationTimeZone) {
        LocalDate shippingDateAtDestination = shippingTime
            .atZone(destinationTimeZone)
            .toLocalDate();
        LocalDate candidateResult = shippingDateAtDestination.plus(RETURNS_PERIOD);
        // LocalDate.plus truncates if the day-of-month overflows. For example,
        // March 31st + 1 month is April 30th, not May 1st in java.time. Our
        // requirements say we need to move to the next day in such cases instead.
        // The simplest way of checking for this is to subtract the returns period and
        // see whether we get back to the original shipping date. If we don't, we know
        // there's been an overflow and we need to add a day.
        return candidateResult.minus(RETURNS_PERIOD).equals(shippingDateAtDestination)
            ? candidateResult : candidateResult.plusDays(1);
    }
}
