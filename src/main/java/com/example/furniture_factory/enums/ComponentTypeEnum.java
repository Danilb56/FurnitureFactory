package com.example.furniture_factory.enums;

public enum ComponentTypeEnum {
    BOLT("BOLT", "Болт"),
    LEG("LEG", "Ножка"),
    PILLOW("PILLOW", "Подушка"),
    FRAME("FRAME", "Каркас"),
    HANDLE("HANDLE", "Ручка"),
    SEAT("SEAT", "Сиденье"),
    TABLETOP("TABLETOP", "Столешница"),
    LOOP("LOOP", "Петля"),
    LOCK("LOCK", "Замок"),
    DOOR("DOOR", "Каркас двери");

    private final String value;
    private final String localization;

    ComponentTypeEnum(String value, String localization) {
        this.value = value;
        this.localization = localization;
    }

    public String getValue() {
        return this.value;
    }

    public String getLocalization() {
        return localization;
    }

    @Override
    public String toString() {
        return localization;
    }
}
