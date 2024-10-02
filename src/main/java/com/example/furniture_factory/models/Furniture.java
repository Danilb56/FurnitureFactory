package com.example.furniture_factory.models;

import com.example.furniture_factory.enums.FurnitureTypeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Furniture {
    private Long id;
    private FurnitureTypeEnum type;
    private Long article;
    private Long price;
    private Long furnitureLineId;

    private FurnitureLine furnitureLine;
    private final List<Component> components = new ArrayList<>();
    private Integer count = 0;

    public Furniture() {
    }

    public Furniture(Long id, FurnitureTypeEnum type, Long article, Long price, Long furnitureLineId, FurnitureLine furnitureLine) {
        this.id = id;
        this.type = type;
        this.article = article;
        this.price = price;
        this.furnitureLineId = furnitureLineId;
        this.furnitureLine = furnitureLine;
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void addComponents(List<Component> components) {
        this.components.addAll(components);
    }

    public List<Component> getComponents() {
        return components;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FurnitureTypeEnum getType() {
        return type;
    }

    public void setType(FurnitureTypeEnum type) {
        this.type = type;
    }

    public Long getArticle() {
        return article;
    }

    public void setArticle(Long article) {
        this.article = article;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getFurnitureLineId() {
        return furnitureLineId;
    }

    public void setFurnitureLineId(Long furnitureLineId) {
        this.furnitureLineId = furnitureLineId;
    }

    public FurnitureLine getFurnitureLine() {
        return furnitureLine;
    }

    public void setFurnitureLine(FurnitureLine furnitureLine) {
        this.furnitureLine = furnitureLine;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Furniture{" +
                "id=" + id +
                ", type=" + type +
                ", article=" + article +
                ", price=" + price +
                ", furnitureLineId=" + furnitureLineId +
                ", furnitureLine=" + furnitureLine +
                ", components=" + components +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Furniture furniture = (Furniture) o;
        return Objects.equals(id, furniture.id) && type == furniture.type && Objects.equals(article, furniture.article) && Objects.equals(price, furniture.price) && Objects.equals(furnitureLineId, furniture.furnitureLineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, article, price, furnitureLineId);
    }
}
