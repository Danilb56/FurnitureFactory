package com.example.furniture_factory.controllers;

import com.example.furniture_factory.exceptions.DataNotLoadedFromDBException;
import com.example.furniture_factory.exceptions.NotFoundException;
import com.example.furniture_factory.exceptions.SavingFailedException;
import com.example.furniture_factory.models.Furniture;
import com.example.furniture_factory.services.FurnitureService;
import javafx.beans.property.LongProperty;
import javafx.beans.property.LongPropertyBase;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class HelloController {
    private final FurnitureService furnitureService;
    @FXML
    public TableView<List<Furniture>> table;
    @FXML
    private TableColumn<Furniture, Long> idColumn;
    @FXML
    private TableColumn<Furniture, String> typeColumn;
    @FXML
    private TableColumn<Furniture, Long> articleColumn;
    @FXML
    private TableColumn<Furniture, Long> priceColumn;
    @FXML
    private TableColumn<Furniture, String> furnitureLineColumn;
    @FXML
    public final ObservableList<Furniture> furnitureList = FXCollections.observableArrayList();

    public HelloController(FurnitureService furnitureService) {
        this.furnitureService = furnitureService;
        this.idColumn.setCellValueFactory(cellData -> {
            LongProperty lp = new SimpleLongProperty();
            lp.setValue(cellData.getValue().getId());
            return lp.orElse(cellData.getValue().getId());
        });
        this.typeColumn.setCellValueFactory(cellData -> cellData.getValue().getType().getName());
        this.articleColumn.setCellValueFactory(cellData -> cellData.getValue().getArticle());
        this.priceColumn.setCellValueFactory(cellData -> cellData.getValue().getPrice());
        this.furnitureLineColumn.setCellValueFactory(cellData -> cellData.getValue().getFurnitureLine().getName());
    }

    @FXML
    public void initialize() {
        this.updatePage();
    }

    @FXML
    protected void addFurniture() {
        // Открыть окно с созданием мебели
        System.out.println("addFurniture");
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
        System.out.println("editFurniture");
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
        System.out.println("deleteFurniture");
    }

    @FXML
    protected void updatePage() {
        System.out.println("updatePage");
        try {
            furnitureList.setAll(furnitureService.findAll());
        } catch (DataNotLoadedFromDBException e) {
            // Отобразить окно ошибки
        }
    }
}
