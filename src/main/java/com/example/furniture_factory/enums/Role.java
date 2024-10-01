package com.example.furniture_factory.enums;

public enum Role {
    ADMIN("ADMIN"),
    DEFAULT("DEFAULT"),
    SHOP_OWNER("SHOP_OWNER"),
    CAN_EDIT_FACTORY_TABLES("CAN_EDIT_FACTORY_TABLES");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public Boolean canCreateOrder() {
        return this.equals(ADMIN) || this.equals(SHOP_OWNER);
    }

    public Boolean canEditFactoryTables() {
        return this.equals(ADMIN) || this.equals(CAN_EDIT_FACTORY_TABLES);
    }

    public Boolean canEditShopTable() {
        return this.equals(ADMIN);
    }

    public String getValue() {
        return value;
    }
}
