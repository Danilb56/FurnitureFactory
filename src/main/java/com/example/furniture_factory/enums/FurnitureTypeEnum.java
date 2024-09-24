package com.example.furniture_factory.enums;

public enum FurnitureTypeEnum {
    CHAIR("CHAIR"), TABLE("TABLE"), BED("BED"), DOOR("DOOR");

    private final String name;

    FurnitureTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
