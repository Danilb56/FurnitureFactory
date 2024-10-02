package com.example.furniture_factory.models;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {
    private Long orderNumber;
    private LocalDate date;
    private Long shopId;

    private final Map<Furniture, Integer> furnitureCountMap = new HashMap<>();

    public Order(Long orderNumber, LocalDate date, Long shopId) {
        this.orderNumber = orderNumber;
        this.date = date;
        this.shopId = shopId;
    }

    public Order() {

    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Map<Furniture, Integer> getFurnitureCountMap() {
        return furnitureCountMap;
    }

    public void copyFromFurnitureCountMap(Map<Furniture, Integer> furnitureCountMap) {
        this.furnitureCountMap.putAll(furnitureCountMap);
    }

    public void copyFromFurnitureList(List<Furniture> furnitureList) {
        this.furnitureCountMap.clear();
        for (Furniture furniture: furnitureList) {
            this.furnitureCountMap.put(furniture, furniture.getCount());
        }
    }
}
