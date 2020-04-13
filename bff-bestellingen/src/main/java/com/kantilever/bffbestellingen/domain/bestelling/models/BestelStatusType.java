package com.kantilever.bffbestellingen.domain.bestelling.models;

public enum BestelStatusType {
    IN_AFWACHTING("Pending"),
    BEHANDELBAAR("Ready"),
    IN_BEHANDELING("Collecting"),
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
