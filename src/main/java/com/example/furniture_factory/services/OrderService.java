package com.example.furniture_factory.services;

import com.example.furniture_factory.controllers.LoginController;
import com.example.furniture_factory.exceptions.DataNotLoadedFromDBException;
import com.example.furniture_factory.exceptions.NotFoundException;
import com.example.furniture_factory.exceptions.SavingFailedException;
import com.example.furniture_factory.models.Furniture;
import com.example.furniture_factory.models.Order;
import com.example.furniture_factory.utils.IdUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderService extends Service<Order> {
    private final FurnitureService furnitureService;

    public OrderService(Connection connection, FurnitureService furnitureService) {
        super(connection);
        this.furnitureService = furnitureService;
    }

    @Override
    public List<Order> findAll() {
        String query = """
                select * from furniture_factory.orders o""";

        if (LoginController.usersShopId != null) {
            query += "\nwhere o.shop_id = " + LoginController.usersShopId;
        }

        System.out.println("Executed query: " + query);
        return selectFromDataBase(query);
    }

    @Override
    public Order findById(Long id) {
        String query = """
                select * from furniture_factory.orders o
                where o.id=""" + id;
        System.out.println("Executed query: " + query);
        List<Order> list = selectFromDataBase(query);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            throw new NotFoundException("Заказ не найден");
        }
    }

    @Override
    public Order update(Order order) {
        throw new DataNotLoadedFromDBException("Невозможно изменить заказ");
    }

    @Override
    public void deleteById(Long id) {
        try {
            findById(id);

            String query = "delete\n" +
                    "from furniture_factory.orders o\n" +
                    "where o.id = " + id;

            PreparedStatement ps = connection.prepareStatement(query);

            System.out.println("Executed query: " + query);
            ps.execute();

            query = "delete\n" +
                    "from furniture_factory.order_furniture_link ofl\n" +
                    "where ofl.order_id = " + id;

            ps = connection.prepareStatement(query);

            System.out.println("Executed query: " + query);
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Order create(Order order) {
        Long orderNumber = IdUtils.getNewOrderId(this);
        try {
            String query = "insert into furniture_factory.orders (id, date, shop_id)\n" +
                    "        VALUES (" +
                    orderNumber + ", '" +
                    order.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "', " +
                    order.getShopId() + ")";

            PreparedStatement ps = connection.prepareStatement(query);

            System.out.println("Executed query: " + query);
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
            throw new SavingFailedException("Не удалось сохранить заказ");
        }
        order.setOrderNumber(orderNumber);
        return order;
    }

    public Long getLargestId() {
        List<Order> list = findAll();
        if (list.size() == 0) {
            return 0L;
        } else {
            return list.stream()
                    .map(furniture -> furniture.getOrderNumber())
                    .max(Long::compareTo)
                    .get();
        }
    }

    private List<Order> selectFromDataBase(String query) {
        List<Order> list = null;
        try {
            list = new ArrayList<>();

            PreparedStatement ps = connection.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            while (rs.next()) {

                Order order = new Order(
                        rs.getLong("id"),
                        LocalDate.parse(rs.getString("date"), dateTimeFormatter),
                        rs.getLong("shop_id")
                );

                Map<Furniture, Integer> furnitureCountMap =
                        furnitureService.getFurnitureCountMapByOrderNumber(order.getOrderNumber());
                order.copyFromFurnitureCountMap(furnitureCountMap);

                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataNotLoadedFromDBException("Не удалось загрузить данные из базы");
        }
        return list;
    }
}
