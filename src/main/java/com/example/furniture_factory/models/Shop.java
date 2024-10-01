package com.example.furniture_factory.models;

public class Shop {
    private Long id;
    private String address;
    private String fax;
    private Long ownerId;

    public Shop(Long id, String address, String fax, Long ownerId) {
        this.id = id;
        this.address = address;
        this.fax = fax;
        this.ownerId = ownerId;
    }

    public Shop() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
