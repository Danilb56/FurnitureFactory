package com.example.furniture_factory.enums;

public enum Role {
    ADMIN("ADMIN", "администратор"),
    DEFAULT("DEFAULT", "сотрудник"),
    SHOP_OWNER("SHOP_OWNER", "владелец магазина"),
    CAN_EDIT_FACTORY_TABLES("CAN_EDIT_FACTORY_TABLES", "редактор");

    private final String value;
    private final String localization;

    Role(String value, String localization) {
        this.value = value;
        this.localization = localization;
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

    public String getLocalization() {
        return localization;
    }
}
