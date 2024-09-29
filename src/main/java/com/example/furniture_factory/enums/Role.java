package com.example.furniture_factory.enums;

public enum Role {
    ADMIN(true, true, "ADMIN"),
    DEFAULT(false, false, "DEFAULT"),
    SHOP_OWNER(true, false, "SHOP_OWNER"),
    CAN_EDIT_FACTORY_TABLES(false, true, "CAN_EDIT_FACTORY_TABLES");

    private final Boolean canCreateOrder;
    private final Boolean canEditFactoryTables;
    private final String value;

    Role(Boolean canCreateOrder, Boolean canEditFactoryTables, String value) {
        this.canCreateOrder = canCreateOrder;
        this.canEditFactoryTables = canEditFactoryTables;
        this.value = value;
    }

    public Boolean getCanCreateOrder() {
        return canCreateOrder;
    }

    public Boolean getCanEditFactoryTables() {
        return canEditFactoryTables;
    }

    public String getValue() {
        return value;
    }
}
