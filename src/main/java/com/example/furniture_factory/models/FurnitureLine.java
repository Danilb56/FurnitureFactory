package com.example.furniture_factory.models;

import java.util.List;

public class FurnitureLine {
    private Long id;
    private String name;

    private List<Furniture> furnitureList;

    public FurnitureLine(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "FurnitureLine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", furnitureList=" + furnitureList +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }
}
