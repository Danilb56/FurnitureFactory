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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
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
            if (!ignoreDialogResult) { // Как только окно было закрыто кнопкой "Сохранить" пытаемся сохранить в бд
                FurnitureLine savedFurnitureLine = service.create(newFurnitureLine);
                furnitureLineList.add(savedFurnitureLine);
            }
        } catch (SavingFailedException | IOException e) {
            e.printStackTrace();
            // Выводим окно с ошибкой
        } finally {
            // Выводим результат сохранения (Окно типа всё хорошо)
        }
    }

    @FXML
    public void editFurnitureLine() {
        try {
            Long id = this.table.getFocusModel().getFocusedItem().getId();
            FurnitureLine furnitureLineToEdit = null;
            int index = -1;
            for (int i = 0; i < furnitureLineList.size(); i++) {
                if (furnitureLineList.get(i).getId().equals(id)) {
                    furnitureLineToEdit = furnitureLineList.get(i);
                    index = i;
                }
            }
            if (furnitureLineToEdit == null) {
                throw new NotFoundException("Не удалось найти линейку мебели");
            }
            openDialog(furnitureLineToEdit); // Открыть окно с изменением линейки мебели
            if (!ignoreDialogResult) { // Как только окно было закрыто кнопкой "Сохранить" пытаемся сохранить в бд
                furnitureLineList.set(index, furnitureLineToEdit);
                service.update(furnitureLineToEdit);
            }
        } catch (SavingFailedException | IOException e) {
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
        this.ignoreDialogResult = true;
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
            if (ignoreDialogResult) {
                return;
            }
            furnitureLine.setName(nameTextField.getText());
        });

        if (furnitureLine.getId() != null) {
            nameTextField.setText(furnitureLine.getName());
        }

        //Создание мнимой кнопки закрытия для правильной работы "Х"
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);

        dialog.showAndWait();
    }

    public void dialogSaveButton() {
        this.ignoreDialogResult = false;
        this.dialog.close();
    }

    public void dialogCancelButton() {
        this.dialog.close();
    }
}
