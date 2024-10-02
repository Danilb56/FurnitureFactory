package com.example.furniture_factory.controllers;

import com.example.furniture_factory.enums.FurnitureTypeEnum;
import com.example.furniture_factory.exceptions.DataNotLoadedFromDBException;
import com.example.furniture_factory.models.Furniture;
import com.example.furniture_factory.models.FurnitureLine;
import com.example.furniture_factory.models.Order;
import com.example.furniture_factory.services.FurnitureService;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.furniture_factory.controllers.LoginController.user;
import static com.example.furniture_factory.controllers.LoginController.usersShopId;

public class OrderController extends Controller<Order> {
    private final FurnitureService furnitureService;

    @FXML
    public TableView<Order> table;
    @FXML
    public TableColumn<Order, Long> orderNumberColumn;
    @FXML
    public TableColumn<String, LocalDate> dateColumn;
    @FXML
    public TableColumn<Order, Long> shopIdColumn;
    @FXML
    public final ObservableList<Order> orderList = FXCollections.observableArrayList();

    @FXML
    public TableView<Furniture> furnitureTable;
    @FXML
    public TableColumn<Furniture, Long> idColumn;
    @FXML
    public TableColumn<String, FurnitureTypeEnum> typeColumn;
    @FXML
    public TableColumn<Furniture, Long> articleColumn;
    @FXML
    public TableColumn<Furniture, Long> priceColumn;
    @FXML
    public TableColumn<Furniture, Integer> countColumn;
    @FXML
    public TableColumn<String, FurnitureLine> furnitureLineColumn;
    @FXML
    public final ObservableList<Furniture> furnitureList = FXCollections.observableArrayList();

    @FXML
    public TableView<Furniture> furnitureFromDatabaseTable;
    @FXML
    public TableColumn<Furniture, Long> idColumn2;
    @FXML
    public TableColumn<String, FurnitureTypeEnum> typeColumn2;
    @FXML
    public TableColumn<Furniture, Long> articleColumn2;
    @FXML
    public TableColumn<Furniture, Long> priceColumn2;
    @FXML
    public TableColumn<String, FurnitureLine> furnitureLineColumn2;
    @FXML
    public final ObservableList<Furniture> furnitureFromDatabaseList = FXCollections.observableArrayList();

    @FXML
    public TextField shopIdTextField;
    @FXML
    public DatePicker dateCalendar;

    public Button addButton;
    public Button deleteButton;
    public Button editFurnitureListButton;
    public HBox furnitureFromDatabaseSection;

    private Dialog<Order> dialog;

    private Long currentEditingOrderNumber;

    public OrderController(Service<Order> orderService,
                           FurnitureService furnitureService) {
        super(orderService);
        this.furnitureService = furnitureService;
    }

    @FXML
    public void initialize() {
        if (!user.getRole().canCreateOrder()) {
            deleteButton.setDisable(true);
            addButton.setDisable(true);
            editFurnitureListButton.setDisable(true);
        }
        this.orderNumberColumn.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.dateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
            @Override
            public String toString(LocalDate s) {
                return s.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            }

            @Override
            public LocalDate fromString(String s) {
                return null;
            }
        }));
        this.shopIdColumn.setCellValueFactory(new PropertyValueFactory<>("shopId"));

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
        this.countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
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

        this.table.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection == null) {
                        this.furnitureList.clear();
                    } else {
                        Map<Furniture, Integer> furnitureCountMap = this.table.getSelectionModel()
                                .getSelectedItem().getFurnitureCountMap();
                        List<Furniture> list = new ArrayList<>();

                        for (Furniture furniture : furnitureCountMap.keySet()) {
                            Integer count = furnitureCountMap.get(furniture);
                            furniture.setCount(count);
                            list.add(furniture);
                        }
                        this.furnitureList.setAll(list);
                    }
                });

        this.idColumn2.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.typeColumn2.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.typeColumn2.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
            @Override
            public String toString(FurnitureTypeEnum s) {
                return s.getLocalization();
            }

            @Override
            public FurnitureTypeEnum fromString(String s) {
                return null;
            }
        }));
        this.articleColumn2.setCellValueFactory(new PropertyValueFactory<>("article"));
        this.priceColumn2.setCellValueFactory(new PropertyValueFactory<>("price"));
        this.furnitureLineColumn2.setCellValueFactory(new PropertyValueFactory<>("furnitureLine"));
        this.furnitureLineColumn2.setCellFactory(TextFieldTableCell.forTableColumn(
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

        hideFurnitureDatabaseSection();

        this.updatePage();
    }

    @FXML
    protected void addOrder() {
        try {
            Order newOrder = new Order();
            openDialog(newOrder);
            // Как только окно было закрыто кнопкой "Сохранить" пытаемся сохранить в бд
            if (!ignoreDialogResult) {
                Order savedOrder = service.create(newOrder);
                orderList.add(savedOrder);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Выводим окно с ошибкой
        }
    }

    @FXML
    protected void deleteOrder() {
        Order orderToDelete = this.table.getSelectionModel().getSelectedItem();
        if (orderToDelete == null) {
            return;
        }
        Long id = orderToDelete.getOrderNumber();
        service.deleteById(id);
        orderList.remove(orderToDelete);
    }

    @FXML
    protected void updatePage() {
        try {
            orderList.setAll(service.findAll());
            table.setItems(orderList);
            furnitureList.clear();
            furnitureTable.setItems(furnitureList);
        } catch (DataNotLoadedFromDBException e) {
            e.printStackTrace();
            // Отобразить окно ошибки
        }
    }

    private void openDialog(Order order) throws IOException {
        this.dialog = new Dialog<>();
        this.ignoreDialogResult = true;
        dialog.setResult(order);
        dialog.setTitle("Создание заказа");
        dialog.setResizable(true);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/order-registration-dialog.fxml"));
        loader.setControllerFactory(i -> this);
        Parent content = loader.load();

        dialog.getDialogPane().setContent(content);
        dialog.setOnCloseRequest((event) -> {
            if (ignoreDialogResult) {
                return;
            }
            order.setDate(dateCalendar.getValue());
            order.setShopId(Long.valueOf(shopIdTextField.getText()));
        });

        if (usersShopId != null) {
            this.shopIdTextField.setText(String.valueOf(usersShopId));
            this.shopIdTextField.setEditable(false);
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

    public void moveFurnitureRight() {
        Furniture furniture = furnitureTable.getSelectionModel().getSelectedItem();
        if (furniture == null) {
            return;
        }

        for (int i = 0; i < furnitureList.size(); i++) {
            if (furnitureList.get(i).equals(furniture)) {
                if (furniture.getCount() > 1) {
                    furniture.setCount(furniture.getCount() - 1);
                    furnitureList.set(i, furnitureList.get(i));
                } else {
                    furnitureList.remove(i);
                }
                break;
            }
        }
    }

    public void moveFurnitureLeft() {
        Furniture furniture = furnitureFromDatabaseTable.getSelectionModel().getSelectedItem();
        if (furniture == null) {
            return;
        }
        for (int i = 0; i < furnitureList.size(); i++) {
            if (furnitureList.get(i).equals(furniture)) {
                furnitureList.get(i).setCount(furnitureList.get(i).getCount() + 1);
                furnitureList.set(i, furnitureList.get(i));
                return;
            }
        }
        furniture.setCount(1);
        furnitureList.add(furniture);
    }

    public void editFurnitureList() {
        Order order = table.getSelectionModel().getSelectedItem();
        if (order == null) {
            return;
        }
        this.currentEditingOrderNumber = order.getOrderNumber();
        if (!this.furnitureFromDatabaseSection.isVisible()) {
            this.furnitureFromDatabaseList.setAll(furnitureService.findAll());
            this.furnitureFromDatabaseTable.setItems(this.furnitureFromDatabaseList);
        }
        this.table.setDisable(true);
        showFurnitureDatabaseSection();
    }

    public void saveFurnitureList() {
        Order order = table.getSelectionModel().getSelectedItem();
        if (order == null) {
            return;
        }
        this.table.setDisable(false);
        hideFurnitureDatabaseSection();
        furnitureService.saveFurnitureList(furnitureList, currentEditingOrderNumber);
        order.copyFromFurnitureList(furnitureList);
    }


    public void cancel() {
        hideFurnitureDatabaseSection();
        this.table.setDisable(false);
        updatePage();
    }

    private void hideFurnitureDatabaseSection() {
        this.furnitureFromDatabaseSection.setVisible(false);
        this.furnitureFromDatabaseSection.setMaxWidth(1);
    }

    private void showFurnitureDatabaseSection() {
        this.furnitureFromDatabaseSection.setVisible(true);
        this.furnitureFromDatabaseSection.setMaxWidth(1000);
    }
}
