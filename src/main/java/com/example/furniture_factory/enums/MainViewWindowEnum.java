package com.example.furniture_factory.enums;

public enum MainViewWindowEnum {
    FURNITURE_LIST("furniture-list-view"),
    FURNITURE_LINE_LIST("furniture-line-list-view"),
    COMPONENT_LIST("component-list-view"),
    ACCOUNT_PAGE(" ");

    private final String fxmlFileName;

    MainViewWindowEnum(String fxmlFileName) {
        this.fxmlFileName = fxmlFileName;
    }

    public String getFxmlFileName() {
        return fxmlFileName;
    }
}
