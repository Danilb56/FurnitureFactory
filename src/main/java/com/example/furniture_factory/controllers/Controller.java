package com.example.furniture_factory.controllers;

import com.example.furniture_factory.services.Service;

public abstract class Controller<E> {
    protected final Service<E> service;

    public Controller(Service<E> service) {
        this.service = service;
    }

    public abstract void initialize();
}
