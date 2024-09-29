package com.example.furniture_factory.enums;

public enum ComponentTypeEnum {
    BOLT("Болт"),
    LEG("Ножка"),
    PILLOW("Подушка"),
    FRAME("Каркас"),
    HANDLE("Ручка"),
    SEAT("Сиденье"),
    TABLETOP("Столешница"),
    LOOP("Петля"),
    LOCK("Замок"),
    DOOR("Каркас двери");

    private final String localization;

    ComponentTypeEnum(String localization) {
        this.localization = localization;
    }

    public String getValue() {
        return this.getValue();
    }

    @Override
    public String toString() {
        return localization;
    }
}
