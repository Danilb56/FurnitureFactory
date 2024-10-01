package com.example.furniture_factory.controllers;

import com.example.furniture_factory.exceptions.DataNotLoadedFromDBException;
import com.example.furniture_factory.exceptions.NotFoundException;
import com.example.furniture_factory.exceptions.SavingFailedException;
import com.example.furniture_factory.models.Shop;
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

import static com.example.furniture_factory.controllers.LoginController.user;

public class ShopController extends Controller<Shop>{
    @FXML
    public TableView<Shop> table;
    @FXML
    public TableColumn<Shop, Long> idColumn;
    @FXML
    public TableColumn<Shop, String> addressColumn;
    @FXML
    public TableColumn<Shop, String> faxColumn;
    @FXML
    public TableColumn<Shop, Long> ownerIdColumn;
    @FXML
    public final ObservableList<Shop> shopList = FXCollections.observableArrayList();

    @FXML
    public TextField addressTextField;
    @FXML
    public TextField faxTextField;
    @FXML
    public TextField ownerIdTextField;

    public Button deleteButton;
    public Button addButton;
    public Button editButton;

    private Dialog<Shop> dialog;

    public ShopController(Service<Shop> shopService) {
        super(shopService);
    }

    @FXML
    public void initialize() {
        if (!user.getRole().canEditShopTable()) {
            addButton.setDisable(true);
            editButton.setDisable(true);
            deleteButton.setDisable(true);
        }
        this.idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        this.faxColumn.setCellValueFactory(new PropertyValueFactory<>("fax"));
        this.ownerIdColumn.setCellValueFactory(new PropertyValueFactory<>("ownerId"));
        this.updatePage();
    }

    @FXML
    protected void addShop() {
        try {
            Shop newShop = new Shop();
            openDialog(newShop);
            // Как только окно было закрыто кнопкой "Сохранить" пытаемся сохранить в бд
            if (!ignoreDialogResult) {
                Shop savedShop = service.create(newShop);
                shopList.add(savedShop);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Выводим окно с ошибкой
        }
    }

    @FXML
    protected void editShop() {
        try {
            Shop selectedShop = this.table.getSelectionModel().getSelectedItem();
            if (selectedShop == null) {
                return;
            }
            Shop shopToEdit = null;
            int index = -1;
            for (int i = 0; i < shopList.size(); i++) {
                if (shopList.get(i).getId().equals(selectedShop.getId())) {
                    shopToEdit = shopList.get(i);
                    index = i;
                }
            }
            if (shopToEdit == null) {
                throw new NotFoundException("Не удалось найти магазин");
            }
            openDialog(shopToEdit); // Открыть окно с изменением мебели
            if (!ignoreDialogResult) { // Как только окно было закрыто кнопкой "Сохранить" пытаемся сохранить в бд
                shopList.set(index, shopToEdit);
                service.update(shopToEdit);
            }
        } catch (SavingFailedException | IOException e) {
            e.printStackTrace();
            // Выводим окно с ошибкой
        } finally {
            // Выводим результат изменения (Окно типа всё хорошо)
        }
    }

    @FXML
    protected void deleteShop() {
        Shop shopToDelete = this.table.getSelectionModel().getSelectedItem();
        if (shopToDelete == null) {
            return;
        }
        Long id = shopToDelete.getId();
        service.deleteById(id);
        shopList.remove(shopToDelete);
    }

    @FXML
    protected void updatePage() {
        try {
            shopList.setAll(service.findAll());
            table.setItems(shopList);
        } catch (DataNotLoadedFromDBException e) {
            e.printStackTrace();
            // Отобразить окно ошибки
        }
    }

    private void openDialog(Shop shop) throws IOException {
        this.dialog = new Dialog<>();
        this.ignoreDialogResult = true;
        dialog.setResult(shop);
        dialog.setTitle(shop.getId() == null ? "Создание магазина" : "Изменение магазина");
        dialog.setResizable(true);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/shop-registration-dialog.fxml"));
        loader.setControllerFactory(i -> this);
        Parent content = loader.load();

        dialog.getDialogPane().setContent(content);
        dialog.setOnCloseRequest((event) -> {
            if (ignoreDialogResult) {
                return;
            }
            shop.setAddress(addressTextField.getText());
            shop.setFax(faxTextField.getText());
            shop.setOwnerId(Long.valueOf(ownerIdTextField.getText()));
        });

        if (shop.getId() != null) {
            addressTextField.setText(shop.getAddress());
            faxTextField.setText(shop.getFax());
            ownerIdTextField.setText(String.valueOf(shop.getOwnerId()));
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
