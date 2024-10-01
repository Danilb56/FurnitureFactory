package com.example.furniture_factory.enums;

public enum MainViewWindowEnum {
    FURNITURE_LIST("furniture-list-view"),
    FURNITURE_LINE_LIST("furniture-line-list-view"),
    COMPONENT_LIST("component-list-view"),
    SHOP_LIST("shop-list-view"),
    ORDER_LIST("order-list-view"),
    ACCOUNT_PAGE("account-page");

    private final String fxmlFileName;

    MainViewWindowEnum(String fxmlFileName) {
        this.fxmlFileName = fxmlFileName;
    }

    public String getFxmlFileName() {
        return fxmlFileName;
    }
}
