package com.example.furniture_factory.services;

import com.example.furniture_factory.exceptions.DataNotLoadedFromDBException;
import com.example.furniture_factory.exceptions.NotFoundException;
import com.example.furniture_factory.exceptions.SavingFailedException;
import com.example.furniture_factory.models.Shop;
import com.example.furniture_factory.utils.IdUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShopService extends Service<Shop> {
    public ShopService(Connection connection) {
        super(connection);
    }

    @Override
    public List<Shop> findAll() {
        String query = "select * " +
                "from furniture_factory.shop s";

        System.out.println("Executed query: " + query);
        return selectFromDataBase(query);
    }

    @Override
    public Shop findById(Long id) {
        String query = "select *\n" +
                "from furniture_factory.shop s\n" +
                "where s.id = " + id;
        System.out.println("Executed query: " + query);
        List<Shop> list = selectFromDataBase(query);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            throw new NotFoundException("Магазин не найден");
        }
    }

    @Override
    public Shop update(Shop shop) {
        try {
            findById(shop.getId());

            String query = "update furniture_factory.shop s\n" +
                    "set address              = '" + shop.getAddress() + "',\n" +
                    "    fax           = '" + shop.getFax() + "',\n" +
                    "    owner_id             = " + shop.getOwnerId() + ",\n" +
                    "where s.id = " + shop.getId();

            PreparedStatement ps = connection.prepareStatement(query);

            System.out.println("Executed query: " + query);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return shop;
    }

    @Override
    public void deleteById(Long id) {
        try {
            findById(id);

            String query = "delete\n" +
                    "from furniture_factory.shop s\n" +
                    "where s.id = " + id;

            PreparedStatement ps = connection.prepareStatement(query);

            System.out.println("Executed query: " + query);
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Shop create(Shop shop) {
        Long id = IdUtils.getNewShopId(this);
        try {
            String query = "insert into furniture_factory.shop (id, address, fax, owner_id)\n" +
                    "        VALUES (" +
                    id + ", " +
                    "'" + shop.getAddress() + "', '" +
                    shop.getFax() + "', " +
                    shop.getOwnerId() + ")";

            PreparedStatement ps = connection.prepareStatement(query);

            System.out.println("Executed query: " + query);
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
            throw new SavingFailedException("Не удалось сохранить магазин");
        }
        shop.setId(id);
        return shop;
    }

    public Long getLargestId() {
        List<Shop> list = findAll();
        if (list.size() == 0) {
            return 0L;
        } else {
            return list.stream()
                    .map(furniture -> furniture.getId())
                    .max(Long::compareTo)
                    .get();
        }
    }

    private List<Shop> selectFromDataBase(String query) {
        List<Shop> list = null;
        try {
            list = new ArrayList<>();

            PreparedStatement ps = connection.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Shop shop = new Shop(
                        rs.getLong("id"),
                        rs.getString("address"),
                        rs.getString("fax"),
                        rs.getLong("owner_id")
                );

                list.add(shop);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataNotLoadedFromDBException("Не удалось загрузить данные из базы");
        }
        return list;
    }
}
