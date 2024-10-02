package com.example.furniture_factory.enums;

public enum MainViewWindowEnum {
    FURNITURE_LIST("furniture-list-view", "Мебель"),
    FURNITURE_LINE_LIST("furniture-line-list-view", "Линейки мебели"),
    COMPONENT_LIST("component-list-view", "Компоненты"),
    SHOP_LIST("shop-list-view", "Магазины"),
    ORDER_LIST("order-list-view", "Заказы"),
    ACCOUNT_PAGE("account-page", "Аккаунт");

    private final String fxmlFileName;
    private final String label;

    MainViewWindowEnum(String fxmlFileName, String label) {
        this.fxmlFileName = fxmlFileName;
        this.label = label;
    }

    public String getFxmlFileName() {
        return fxmlFileName;
    }

    public String getLabel() {
        return label;
    }
}
