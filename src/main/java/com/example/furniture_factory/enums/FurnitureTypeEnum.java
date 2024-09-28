package com.example.furniture_factory.enums;

public enum FurnitureTypeEnum {
    CHAIR("CHAIR", "Стул"), TABLE("TABLE", "Стол"), BED("BED", "Кровать"), DOOR("DOOR", "Дверь");

    private final String name;
    private final String localization;

    FurnitureTypeEnum(String name, String localization) {
        this.name = name;
        this.localization = localization;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return localization;
    }
}
