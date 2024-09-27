package com.example.furniture_factory.controllers;

import com.example.furniture_factory.enums.FurnitureTypeEnum;
import com.example.furniture_factory.exceptions.DataNotLoadedFromDBException;
import com.example.furniture_factory.exceptions.NotFoundException;
import com.example.furniture_factory.exceptions.SavingFailedException;
import com.example.furniture_factory.models.Furniture;
import com.example.furniture_factory.models.FurnitureLine;
import com.example.furniture_factory.services.FurnitureService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

public class HelloController {
    private final FurnitureService furnitureService;
    @FXML
    public TableView<Furniture> table;
    @FXML
    public TableColumn<Furniture, Long> idColumn;
    @FXML
    public TableColumn<String, FurnitureTypeEnum> typeColumn;
    @FXML
    public TableColumn<Furniture, Long> articleColumn;
    @FXML
    public TableColumn<Furniture, Long> priceColumn;
    @FXML
    public TableColumn<String, FurnitureLine> furnitureLineColumn;
    @FXML
    public final ObservableList<Furniture> furnitureList = FXCollections.observableArrayList();

    public HelloController(FurnitureService furnitureService) {
        this.furnitureService = furnitureService;
    }

    @FXML
    public void initialize() {
        table.setEditable(true);
        this.idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.typeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
            @Override
            public String toString(FurnitureTypeEnum s) {
                return null;
            }

            @Override
            public FurnitureTypeEnum fromString(String s) {
                return null;
            }
        }));
        this.articleColumn.setCellValueFactory(new PropertyValueFactory<>("article"));
        this.priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        this.furnitureLineColumn.setCellValueFactory(new PropertyValueFactory<>("furnitureLine"));
        this.furnitureLineColumn.setCellFactory(TextFieldTableCell.forTableColumn(
                new StringConverter<>() {
                    @Override
                    public String toString(FurnitureLine s) {
                        return s.getName();
                    }

                    @Override
                    public FurnitureLine fromString(String s) {
                        return null;
                    }
                }));
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
            Long id = this.table.getFocusModel().getFocusedItem().getId();
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
        Long id = this.table.getFocusModel().getFocusedItem().getId();
        this.furnitureService.deleteById(id);
        updatePage();
        System.out.println("deleteFurniture");
    }

    @FXML
    protected void updatePage() {
        System.out.println("updatePage");
        try {
            furnitureList.setAll(furnitureService.findAll());
            table.setItems(furnitureList);
        } catch (DataNotLoadedFromDBException e) {
            // Отобразить окно ошибки
        }
    }
}
