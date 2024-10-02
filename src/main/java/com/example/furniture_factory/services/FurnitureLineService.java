package com.example.furniture_factory.services;

import com.example.furniture_factory.exceptions.DataNotLoadedFromDBException;
import com.example.furniture_factory.exceptions.NotFoundException;
import com.example.furniture_factory.exceptions.SavingFailedException;
import com.example.furniture_factory.models.FurnitureLine;
import com.example.furniture_factory.utils.IdUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FurnitureLineService extends Service<FurnitureLine> {

    public FurnitureLineService(Connection connection) {
        super(connection);
    }

    @Override
    public List<FurnitureLine> findAll() {
        String query = "select *" +
                "from furniture_factory.furniture_line fl";

        System.out.println("Executed query: " + query);
        return selectFromDataBase(query);
    }

    @Override
    public FurnitureLine findById(Long id) {
        String query = "select *\n" +
                "from furniture_factory.furniture_line fl\n" +
                "where fl.id = " + id;
        System.out.println("Executed query: " + query);
        List<FurnitureLine> list = selectFromDataBase(query);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            throw new NotFoundException("Мебель не найдена");
        }
    }

    @Override
    public FurnitureLine update(FurnitureLine furnitureLine) {
        try {
            findById(furnitureLine.getId());

            String query = "update furniture_factory.furniture_line fl\n" +
                    "set name              = '" + furnitureLine.getName() + "'\n" +
                    "where fl.id = " + furnitureLine.getId();

            PreparedStatement ps = connection.prepareStatement(query);

            System.out.println("Executed query: " + query);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return furnitureLine;
    }

    @Override
    public void deleteById(Long id) {
        throw new DataNotLoadedFromDBException("Невозможно удалить линейку мебели");
    }

    @Override
    public FurnitureLine create(FurnitureLine furnitureLine) {
        Long id = IdUtils.getNewFurnitureLineId(this);
        try {
            String query = "insert into furniture_factory.furniture_line (id, name)\n" +
                    "        VALUES (" +
                    id + ", '" +
                    furnitureLine.getName() + "')";

            System.out.println("Executed query: " + query);
            PreparedStatement ps = connection.prepareStatement(query);

            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
            throw new SavingFailedException("Не удалось сохранить линейку мебели");
        }
        furnitureLine.setId(id);
        return furnitureLine;
    }

    public Long getLargestId() {
        List<FurnitureLine> list = findAll();
        if (list.size() == 0) {
            return 0L;
        } else {
            return list.stream()
                    .map(furnitureLine -> furnitureLine.getId())
                    .max(Long::compareTo)
                    .get();
        }
    }

    private List<FurnitureLine> selectFromDataBase(String query) {
        List<FurnitureLine> list = null;
        try {
            list = new ArrayList<>();

            PreparedStatement ps = connection.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                FurnitureLine furnitureLine = new FurnitureLine(
                        rs.getLong("id"),
                        rs.getString("name")
                );

                list.add(furnitureLine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataNotLoadedFromDBException("Не удалось загрузить данные из базы");
        }
        return list;
    }
}
