package com.example.furniture_factory.controllers;

import com.example.furniture_factory.enums.ComponentTypeEnum;
import com.example.furniture_factory.enums.FurnitureTypeEnum;
import com.example.furniture_factory.exceptions.DataNotLoadedFromDBException;
import com.example.furniture_factory.exceptions.NotFoundException;
import com.example.furniture_factory.exceptions.SavingFailedException;
import com.example.furniture_factory.models.Component;
import com.example.furniture_factory.models.Furniture;
import com.example.furniture_factory.models.FurnitureLine;
import com.example.furniture_factory.services.ComponentService;
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
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.Arrays;

import static com.example.furniture_factory.controllers.LoginController.user;

public class FurnitureController extends Controller<Furniture> {
    private final Service<FurnitureLine> furnitureLineService;
    private final ComponentService componentService;

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

    public Button deleteButton;
    public Button addButton;
    public Button editButton;
    public TableView<Component> componentTable;
    public TableColumn<Component, Long> codeColumn1;
    public TableColumn<Component, Long> priceColumn1;
    public TableColumn<String, ComponentTypeEnum> typeColumn1;
    public Button saveComponentListButton;
    public TableView<Component> componentFromDatabaseTable;
    public TableColumn<Component, Long> codeColumn2;
    public TableColumn<Component, Long> priceColumn2;
    public TableColumn<String, ComponentTypeEnum> typeColumn2;
    public Button cancel;
    public HBox componentFromDatabaseSection;
    public Button editComponentListButton;
    public TableColumn<Component, Long> countColumn1;

    private Dialog<Furniture> dialog;
    private Long currentEditingFurnitureId;
    private final ObservableList<Component> componentList = FXCollections.observableArrayList();
    private final ObservableList<Component> componentFromDatabaseList = FXCollections.observableArrayList();

    public FurnitureController(Service<Furniture> furnitureService,
                               Service<FurnitureLine> furnitureLineService,
                               ComponentService componentService) {
        super(furnitureService);
        this.furnitureLineService = furnitureLineService;
        this.componentService = componentService;
    }

    @FXML
    public void initialize() {
        if (!user.getRole().canEditFactoryTables()) {
            addButton.setDisable(true);
            editButton.setDisable(true);
            editComponentListButton.setDisable(true);
            deleteButton.setDisable(true);
        }
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

        this.codeColumn1.setCellValueFactory(new PropertyValueFactory<>("code"));
        this.countColumn1.setCellValueFactory(new PropertyValueFactory<>("count"));
        this.typeColumn1.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.typeColumn1.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
            @Override
            public String toString(ComponentTypeEnum s) {
                return s.getLocalization();
            }

            @Override
            public ComponentTypeEnum fromString(String s) {
                return null;
            }
        }));
        this.priceColumn1.setCellValueFactory(new PropertyValueFactory<>("price"));

        this.codeColumn2.setCellValueFactory(new PropertyValueFactory<>("code"));
        this.typeColumn2.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.typeColumn2.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
            @Override
            public String toString(ComponentTypeEnum s) {
                return s.getLocalization();
            }

            @Override
            public ComponentTypeEnum fromString(String s) {
                return null;
            }
        }));
        this.priceColumn2.setCellValueFactory(new PropertyValueFactory<>("price"));

        this.table.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection == null) {
                        this.componentList.clear();
                    } else {
                        this.componentList.setAll(newSelection.getComponents());
                    }
                });

        hideComponentDatabaseSection();

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
            Furniture selectedFurniture = this.table.getSelectionModel().getSelectedItem();
            if (selectedFurniture == null) {
                return;
            }
            Furniture furnitureToEdit = null;
            int index = -1;
            for (int i = 0; i < furnitureList.size(); i++) {
                if (furnitureList.get(i).getId().equals(selectedFurniture.getId())) {
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
        Furniture furnitureToDelete = this.table.getSelectionModel().getSelectedItem();
        if (furnitureToDelete == null) {
            return;
        }
        Long id = furnitureToDelete.getId();
        service.deleteById(id);
        furnitureList.remove(furnitureToDelete);
    }

    @FXML
    protected void updatePage() {
        try {
            furnitureList.setAll(service.findAll());
            table.setItems(furnitureList);
            componentList.clear();
            componentTable.setItems(componentList);
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

    public void moveComponentRight() {
        Component component = componentTable.getSelectionModel().getSelectedItem();
        if (component == null) {
            return;
        }

        for (int i = 0; i < componentList.size(); i++) {
            if (componentList.get(i).equals(component)) {
                if (component.getCount() > 1) {
                    component.setCount(component.getCount() - 1);
                    componentList.set(i, componentList.get(i));
                } else {
                    componentList.remove(i);
                }
                break;
            }
        }
    }

    public void moveComponentLeft() {
        Component component = componentFromDatabaseTable.getSelectionModel().getSelectedItem();
        if (component == null) {
            return;
        }
        for (int i = 0; i < componentList.size(); i++) {
            if (componentList.get(i).equals(component)) {
                componentList.get(i).setCount(componentList.get(i).getCount() + 1);
                componentList.set(i, componentList.get(i));
                return;
            }
        }
        component.setCount(1L);
        componentList.add(component);
    }

    public void editComponentList() {
        Furniture furniture = table.getSelectionModel().getSelectedItem();
        if (furniture == null) {
            return;
        }
        this.currentEditingFurnitureId = furniture.getId();
        if (!this.componentFromDatabaseSection.isVisible()) {
            this.componentFromDatabaseList.setAll(componentService.findAll());
            this.componentFromDatabaseTable.setItems(this.componentFromDatabaseList);
        }
        this.table.setDisable(true);
        showComponentDatabaseSection();
    }

    public void saveComponentList() {
        Furniture furniture = table.getSelectionModel().getSelectedItem();
        if (furniture == null) {
            return;
        }
        this.table.setDisable(false);
        hideComponentDatabaseSection();
        componentService.saveComponentList(componentList, currentEditingFurnitureId);
        furniture.addComponents(componentList);
    }

    public void cancel() {
        hideComponentDatabaseSection();
        this.table.setDisable(false);
        updatePage();
    }

    private void hideComponentDatabaseSection() {
        this.componentFromDatabaseSection.setVisible(false);
        this.componentFromDatabaseSection.setMaxWidth(1);
    }

    private void showComponentDatabaseSection() {
        this.componentFromDatabaseSection.setVisible(true);
        this.componentFromDatabaseSection.setMaxWidth(1000);
    }
}
