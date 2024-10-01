package com.example.furniture_factory.controllers;

import com.example.furniture_factory.enums.ComponentTypeEnum;
import com.example.furniture_factory.exceptions.DataNotLoadedFromDBException;
import com.example.furniture_factory.exceptions.NotFoundException;
import com.example.furniture_factory.exceptions.SavingFailedException;
import com.example.furniture_factory.models.Component;
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

import static com.example.furniture_factory.controllers.LoginController.user;

public class ComponentController extends Controller<Component> {
    @FXML
    public TableView<Component> table;
    @FXML
    public TableColumn<Component, Long> codeColumn;
    @FXML
    public TableColumn<String, ComponentTypeEnum> typeColumn;
    @FXML
    public TableColumn<Component, Long> priceColumn;
    @FXML
    public final ObservableList<Component> componentList = FXCollections.observableArrayList();

    @FXML
    public ChoiceBox<ComponentTypeEnum> componentTypeChoiceBox;
    @FXML
    public TextField priceTextField;

    private Dialog<Component> dialog;

    public Button deleteButton;
    public Button addButton;
    public Button editButton;

    public ComponentController(Service<Component> service) {
        super(service);
    }

    @FXML
    public void initialize() {
        if (!user.getRole().canEditFactoryTables()) {
            addButton.setDisable(true);
            editButton.setDisable(true);
            deleteButton.setDisable(true);
        }
        this.codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        this.typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.typeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
            @Override
            public String toString(ComponentTypeEnum s) {
                return s.getLocalization();
            }

            @Override
            public ComponentTypeEnum fromString(String s) {
                return null;
            }
        }));
        this.priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        this.updatePage();
    }

    @FXML
    protected void addComponent() {
        try {
            // Открыть окно с созданием мебели
            Component newComponent = new Component();
            openDialog(newComponent);
            if (!ignoreDialogResult) {
                Component savedComponent = service.create(newComponent);
                componentList.add(savedComponent);
            }
            // Как только окно было закрыто кнопкой "Сохранить" пытаемся сохранить в бд
        } catch (Exception e) {
            e.printStackTrace();
            // Выводим окно с ошибкой
        }
    }

    @FXML
    protected void editComponent() {
        try {
            Component selectedComponent = this.table.getSelectionModel().getSelectedItem();
            if (selectedComponent == null) {
                return;
            }
            Component componentToEdit = null;
            int index = -1;
            for (int i = 0; i < componentList.size(); i++) {
                if (componentList.get(i).getCode().equals(selectedComponent.getCode())) {
                    componentToEdit = componentList.get(i);
                    index = i;
                }
            }
            if (componentToEdit == null) {
                throw new NotFoundException("Не удалось найти компонент");
            }
            openDialog(componentToEdit); // Открыть окно с изменением компонента
            if (!ignoreDialogResult) { // Как только окно было закрыто кнопкой "Сохранить" пытаемся сохранить в бд
                componentList.set(index, componentToEdit);
                service.update(componentToEdit);
            }
        } catch (SavingFailedException | IOException e) {
            e.printStackTrace();

            // Выводим окно с ошибкой
        } finally {
            // Выводим результат изменения (Окно типа всё хорошо)
        }
    }

    @FXML
    protected void deleteComponent() {
        Component componentToDelete = this.table.getSelectionModel().getSelectedItem();
        if (componentToDelete == null) {
            return;
        }
        Long id = componentToDelete.getCode();
        service.deleteById(id);
        componentList.remove(componentToDelete);
    }

    @FXML
    protected void updatePage() {
        try {
            componentList.setAll(service.findAll());
            table.setItems(componentList);
        } catch (DataNotLoadedFromDBException e) {
            e.printStackTrace();
            // Отобразить окно ошибки
        }
    }

    private void openDialog(Component component) throws IOException {
        this.dialog = new Dialog<>();
        this.ignoreDialogResult = true;
        dialog.setResult(component);
        dialog.setTitle(component.getCode() == null ? "Создание компонента" : "Изменение компонента");
        dialog.setResizable(true);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/component-registration-dialog.fxml"));
        loader.setControllerFactory(i -> this);
        Parent content = loader.load();

        dialog.getDialogPane().setContent(content);
        dialog.setOnCloseRequest((event) -> {
            if (ignoreDialogResult) {
                return;
            }
            component.setPrice(Long.valueOf(priceTextField.getText()));
            component.setType(componentTypeChoiceBox.getValue());
        });
        componentTypeChoiceBox.setItems(FXCollections.observableList(Arrays.asList(ComponentTypeEnum.values())));

        if (component.getCode() != null) {
            priceTextField.setText(String.valueOf(component.getPrice()));
            componentTypeChoiceBox.setValue(component.getType());
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

    public void disableDeleteButton() {
        this.deleteButton.setDisable(true);
    }
}
