package com.example.furniture_factory.models;

import com.example.furniture_factory.enums.ComponentTypeEnum;

import java.util.Objects;

public class Component {
    private Long code;
    private Long price;
    private ComponentTypeEnum type;

    private Long count = 0L;

    public Component() {}

    public Component(Long code, Long price, ComponentTypeEnum type) {
        this.code = code;
        this.price = price;
        this.type = type;
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

    @Override
    public String toString() {
        return "Component{" +
                "code=" + code +
                ", price=" + price +
                ", type=" + type +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return Objects.equals(code, component.code) && Objects.equals(price, component.price) && type == component.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, price, type);
    }
}
