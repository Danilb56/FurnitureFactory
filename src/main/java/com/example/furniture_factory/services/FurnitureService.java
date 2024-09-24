package com.example.furniture_factory.services;

import com.example.furniture_factory.enums.FurnitureTypeEnum;
import com.example.furniture_factory.models.Furniture;
import com.example.furniture_factory.models.FurnitureLine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FurnitureService {
    private final Connection connection;

    public FurnitureService(Connection connection) {
        this.connection = connection;
    }

    public List<Furniture> findAll() {
        List<Furniture> list = null;
        try {
            list = new ArrayList<>();

            String query = "select *" +
                    "from furniture_factory.furniture f" +
                    "         left join furniture_factory.furniture_line fl" +
                    "                   on f.furniture_line_id = fl.id";
            PreparedStatement ps = connection.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                FurnitureLine furnitureLine = new FurnitureLine(
                        rs.getLong("fl.id"),
                        rs.getString("name")
                );

                Furniture furniture = new Furniture(
                        rs.getLong("f.id"),
                        FurnitureTypeEnum.valueOf(rs.getString("type")),
                        rs.getLong("article"),
                        rs.getLong("price"),
                        rs.getLong("furniture_line_id"),
                        furnitureLine
                );
                list.add(furniture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Furniture findById(Long id) {
        //Находит в базе данных мебель с id = id
        return null;
    }

    public Furniture update(Furniture furniture) {
        //Изменяет поля в базе данных на основе переданного объекта
        return null;
    }

    public void deleteById(Long id) {
        //Удаляет мебель из таблицы по id
        return;
    }

    public Furniture create(Furniture furniture) {
        //Добавить (вставить) в базу данных запись о мебели
        return null;
    }

    //CRUD
}
