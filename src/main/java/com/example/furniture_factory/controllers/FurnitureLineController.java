package com.example.furniture_factory.controllers;

import com.example.furniture_factory.exceptions.DataNotLoadedFromDBException;
import com.example.furniture_factory.exceptions.NotFoundException;
import com.example.furniture_factory.exceptions.SavingFailedException;
import com.example.furniture_factory.models.FurnitureLine;
import com.example.furniture_factory.services.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class FurnitureLineController extends Controller<FurnitureLine> {
    @FXML
    public TableView<FurnitureLine> table;
    @FXML
    public TableColumn<FurnitureLine, Long> idColumn;
    @FXML
    public TableColumn<FurnitureLine, String> nameColumn;
    public TextField nameTextField;

    @FXML
    protected ObservableList<FurnitureLine> furnitureLineList = FXCollections.observableArrayList();
    private Dialog<FurnitureLine> dialog;

    public FurnitureLineController(Service<FurnitureLine> furnitureLineService) {
        super(furnitureLineService);
    }

    @FXML
    public void initialize() {
        this.idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.updatePage();
    }

    @FXML
    public void addFurnitureLine() {
        try {
            // Открыть окно с созданием линейки мебели
            FurnitureLine newFurnitureLine = new FurnitureLine();
            openDialog(newFurnitureLine);
            // Как только окно было закрыто кнопкой "Сохранить" пытаемся сохранить в бд
            FurnitureLine savedFurnitureLine = service.create(newFurnitureLine);
            furnitureLineList.add(savedFurnitureLine);
        } catch (SavingFailedException | IOException e) {
            e.printStackTrace();
            // Выводим окно с ошибкой
        } finally {
            // Выводим результат сохранения (Окно типа всё хорошо)
        }
    }

    @FXML
    public void editFurnitureLine() {
        Long id = this.table.getFocusModel().getFocusedItem().getId();
        //TODO fix edit reload
        FurnitureLine furnitureLineToEdit = this.furnitureLineList
                .stream()
                .filter(furniture -> furniture.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Мебель не найдена"));
        try {
            furnitureLineList.remove(furnitureLineToEdit);
            openDialog(furnitureLineToEdit); // Открыть окно с изменением линейки мебели
            furnitureLineList.add(furnitureLineToEdit);
            service.update(furnitureLineToEdit); // Как только окно было закрыто кнопкой "Сохранить" пытаемся сохранить в бд
        } catch (SavingFailedException | IOException e) {
            if (furnitureLineToEdit != null) {
                furnitureLineList.add(furnitureLineToEdit); // Добавляем удалённую перед ошибкой линейку мебели
            }
            e.printStackTrace();
            // Выводим окно с ошибкой
        } finally {
            // Выводим результат изменения (Окно типа всё хорошо)
        }
    }

    @FXML
    public void updatePage() {
        try {
            furnitureLineList.setAll(service.findAll());
            table.setItems(furnitureLineList);
        } catch (DataNotLoadedFromDBException e) {
            e.printStackTrace();
            // Отобразить окно ошибки
        }
    }

    private void openDialog(FurnitureLine furnitureLine) throws IOException {
        this.dialog = new Dialog<>();
        dialog.setResult(furnitureLine);
        dialog.setTitle(furnitureLine.getId() == null ? "Создание линейки мебели" : "Изменение линейки мебели");
        dialog.setResizable(true);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/furniture-line-registration-dialog.fxml"));
        loader.setControllerFactory(i -> this);
        Parent content = loader.load();

        dialog.getDialogPane().setContent(content);
        dialog.setOnCloseRequest((event) -> {
            furnitureLine.setName(nameTextField.getText());
        });

        if (furnitureLine.getId() != null) {
            nameTextField.setText(furnitureLine.getName());
        }
        //TODO fix close button
        dialog.showAndWait();
    }

    public void closeDialog() {
        this.dialog.close();
    }
}
