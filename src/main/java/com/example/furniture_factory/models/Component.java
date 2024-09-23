package com.example.furniture_factory.models;

import com.example.furniture_factory.enums.ComponentTypeEnum;

public class Component {
    private Long code;
    private Long price;
    private ComponentTypeEnum type;

    private Long count;

    public Component(Long code, Long price, ComponentTypeEnum type, Long count) {
        this.code = code;
        this.price = price;
        this.type = type;
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public ComponentTypeEnum getType() {
        return type;
    }

    public void setType(ComponentTypeEnum type) {
        this.type = type;
    }
}
