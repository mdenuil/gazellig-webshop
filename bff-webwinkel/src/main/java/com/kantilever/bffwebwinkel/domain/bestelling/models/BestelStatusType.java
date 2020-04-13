package com.kantilever.bffwebwinkel.domain.bestelling.models;

/**
 * BestelStatusType holds all Status types an order can have.
 */
public enum BestelStatusType {
    BEHANDELBAAR("Ready"),
    IN_AFWACHTING("Pending"),
    IN_BEHANDELING("Collecting order"),

    VERSTUURD("Send"),
    BETAALD("Paid");

    private String name;

    BestelStatusType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
