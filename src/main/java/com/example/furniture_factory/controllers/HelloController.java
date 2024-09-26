package com.example.furniture_factory.controllers;

import com.example.furniture_factory.exceptions.DataNotLoadedFromDBException;
import com.example.furniture_factory.exceptions.NotFoundException;
import com.example.furniture_factory.exceptions.SavingFailedException;
import com.example.furniture_factory.models.Furniture;
import com.example.furniture_factory.services.FurnitureService;
import javafx.fxml.FXML;

import java.util.List;

public class HelloController {
    private final FurnitureService furnitureService;
    private List<Furniture> furnitureList;

    public HelloController(FurnitureService furnitureService) {
        this.furnitureService = furnitureService;
    }

    @FXML
    public void initialize() {
        System.out.println("init");
    }

    @FXML
    protected void addFurniture() {
        // Открыть окно с созданием мебели
        try {
            // Как только окно было закрыто кнопкой "Сохранить" пытаемся сохранить в бд
//            Furniture furniture = getFromWindow();
//            furnitureService.create(furniture);
        } catch (SavingFailedException e) {
            // Выводим окно с ошибкой
        } finally {
            // Выводим результат сохранения (Окно типа всё хорошо)
        }
    }

    @FXML
    protected void editFurniture() {
        // Открыть окно с изменением мебели
        try {
            Long id = 1L;
            Furniture furnitureToEdit = this.furnitureList
                    .stream()
                    .filter(furniture -> furniture.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Мебель не найдена"));
            // Как только окно было закрыто кнопкой "Сохранить" пытаемся сохранить в бд
//            Furniture furniture = getFromWindow();
//            furnitureService.update(furniture);
        } catch (SavingFailedException e) {
            // Выводим окно с ошибкой
        } finally {
            // Выводим результат изменения (Окно типа всё хорошо)
        }
    }

    @FXML
    protected void deleteFurniture() {

    }

    @FXML
    protected void updatePage() {
        try {
            furnitureList = furnitureService.findAll();
        } catch (DataNotLoadedFromDBException e) {
            // Отобразить окно ошибки
        }
    }
}
