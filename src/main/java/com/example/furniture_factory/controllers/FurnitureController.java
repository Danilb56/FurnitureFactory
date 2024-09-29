package com.example.furniture_factory.controllers;

import com.example.furniture_factory.enums.FurnitureTypeEnum;
import com.example.furniture_factory.exceptions.DataNotLoadedFromDBException;
import com.example.furniture_factory.exceptions.NotFoundException;
import com.example.furniture_factory.exceptions.SavingFailedException;
import com.example.furniture_factory.models.Furniture;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.Arrays;

public class FurnitureController extends Controller<Furniture> {
    private final Service<FurnitureLine> furnitureLineService;

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

    @FXML
    public ChoiceBox<FurnitureTypeEnum> furnitureTypeChoiceBox;
    @FXML
    public TextField articleTextField;
    @FXML
    public TextField priceTextField;
    @FXML
    public ChoiceBox<FurnitureLine> furnitureLineChoiceBox;

    private Dialog<Furniture> dialog;

    public FurnitureController(Service<Furniture> furnitureService,
                               Service<FurnitureLine> furnitureLineService) {
        super(furnitureService);
        this.furnitureLineService = furnitureLineService;
    }

    @FXML
    public void initialize() {
        this.idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.typeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
            @Override
            public String toString(FurnitureTypeEnum s) {
                return s.getLocalization();
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
        try {
            // Открыть окно с созданием мебели
            Furniture newFurniture = new Furniture();
            openDialog(newFurniture);
            // Как только окно было закрыто кнопкой "Сохранить" пытаемся сохранить в бд
            if (!ignoreDialogResult) {
                Furniture savedFurniture = service.create(newFurniture);
                furnitureList.add(savedFurniture);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Выводим окно с ошибкой
        }
    }

    @FXML
    protected void editFurniture() {
        try {
            Long id = this.table.getFocusModel().getFocusedItem().getId();
            Furniture furnitureToEdit = null;
            int index = -1;
            for (int i = 0; i < furnitureList.size(); i++) {
                if (furnitureList.get(i).getId().equals(id)) {
                    furnitureToEdit = furnitureList.get(i);
                    index = i;
                }
            }
            if (furnitureToEdit == null) {
                throw new NotFoundException("Не удалось найти мебель");
            }
            openDialog(furnitureToEdit); // Открыть окно с изменением мебели
            if (!ignoreDialogResult) { // Как только окно было закрыто кнопкой "Сохранить" пытаемся сохранить в бд
                furnitureList.set(index, furnitureToEdit);
                service.update(furnitureToEdit);
            }
        } catch (SavingFailedException | IOException e) {
            e.printStackTrace();
            // Выводим окно с ошибкой
        } finally {
            // Выводим результат изменения (Окно типа всё хорошо)
        }
    }

    @FXML
    protected void deleteFurniture() {
        Furniture furnitureToDelete = table.getFocusModel().getFocusedItem();
        Long id = furnitureToDelete.getId();
        service.deleteById(id);
        furnitureList.remove(furnitureToDelete);
    }

    @FXML
    protected void updatePage() {
        try {
            furnitureList.setAll(service.findAll());
            table.setItems(furnitureList);
        } catch (DataNotLoadedFromDBException e) {
            e.printStackTrace();
            // Отобразить окно ошибки
        }
    }

    private void openDialog(Furniture furniture) throws IOException {
        this.dialog = new Dialog<>();
        this.ignoreDialogResult = true;
        dialog.setResult(furniture);
        dialog.setTitle(furniture.getId() == null ? "Создание мебели" : "Изменение мебели");
        dialog.setResizable(true);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/furniture-registration-dialog.fxml"));
        loader.setControllerFactory(i -> this);
        Parent content = loader.load();

        dialog.getDialogPane().setContent(content);
        dialog.setOnCloseRequest((event) -> {
            if (ignoreDialogResult) {
                return;
            }
            furniture.setArticle(Long.valueOf(articleTextField.getText()));
            furniture.setPrice(Long.valueOf(priceTextField.getText()));
            furniture.setType(furnitureTypeChoiceBox.getValue());
            furniture.setFurnitureLineId(1L);
            furniture.setFurnitureLine(furnitureLineChoiceBox.getValue());
            furniture.setFurnitureLineId(furnitureLineChoiceBox.getValue().getId());
        });
        furnitureTypeChoiceBox.setItems(FXCollections.observableList(Arrays.asList(FurnitureTypeEnum.values())));
        furnitureLineChoiceBox.setItems(FXCollections.observableList(furnitureLineService.findAll()));

        if (furniture.getId() != null) {
            articleTextField.setText(String.valueOf(furniture.getArticle()));
            priceTextField.setText(String.valueOf(furniture.getPrice()));
            furnitureTypeChoiceBox.setValue(furniture.getType());
            furnitureLineChoiceBox.setValue(furniture.getFurnitureLine());
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
