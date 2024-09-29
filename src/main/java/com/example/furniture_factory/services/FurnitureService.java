package com.example.furniture_factory.services;

import com.example.furniture_factory.enums.FurnitureTypeEnum;
import com.example.furniture_factory.exceptions.DataNotLoadedFromDBException;
import com.example.furniture_factory.exceptions.NotFoundException;
import com.example.furniture_factory.exceptions.SavingFailedException;
import com.example.furniture_factory.models.Component;
import com.example.furniture_factory.models.Furniture;
import com.example.furniture_factory.models.FurnitureLine;
import com.example.furniture_factory.utils.IdUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FurnitureService extends Service<Furniture> {
    private final ComponentService componentService;

    public FurnitureService(Connection connection, ComponentService componentService) {
        super(connection);
        this.componentService = componentService;
    }

    @Override
    public List<Furniture> findAll() {
        String query = "select *" +
                "from furniture_factory.furniture f" +
                "         left join furniture_factory.furniture_line fl" +
                "                   on f.furniture_line_id = fl.id";

        return selectFromDataBase(query);
    }

    @Override
    public Furniture findById(Long id) {
        String query = "select *\n" +
                "from furniture_factory.furniture f\n" +
                "         left join furniture_factory.furniture_line fl\n" +
                "                   on f.furniture_line_id = fl.id\n" +
                "where f.id = " + id;
        List<Furniture> list = selectFromDataBase(query);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            throw new NotFoundException("Мебель не найдена");
        }
    }

    @Override
    public Furniture update(Furniture furniture) {
        try {
            findById(furniture.getId());

            String query = "update furniture_factory.furniture f\n" +
                    "set type              = '" + furniture.getType().getName() + "',\n" +
                    "    article           = " + furniture.getArticle() + ",\n" +
                    "    price             = " + furniture.getPrice() + ",\n" +
                    "    furniture_line_id = " + furniture.getFurnitureLineId() + "\n" +
                    "where f.id = " + furniture.getId();

            PreparedStatement ps = connection.prepareStatement(query);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return furniture;
    }

    @Override
    public void deleteById(Long id) {
        try {
            findById(id);

            String query = "delete\n" +
                    "from furniture_factory.furniture f\n" +
                    "where f.id = " + id;

            PreparedStatement ps = connection.prepareStatement(query);

            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Furniture create(Furniture furniture) {
        Long id = IdUtils.getNewFurnitureId(this);
        try {
            String query = "insert into furniture_factory.furniture (id, type, article, price, furniture_line_id)\n" +
                    "        VALUES (" +
                    id + ", " +
                    "'" + furniture.getType().getName() + "', " +
                    furniture.getArticle() + ", " +
                    furniture.getPrice() + ", " +
                    furniture.getFurnitureLineId() + ")";

            PreparedStatement ps = connection.prepareStatement(query);

            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
            throw new SavingFailedException("Не удалось сохранить мебель");
        }
        furniture.setId(id);
        return furniture;
    }

    public Long getLargestId() {
        List<Furniture> list = findAll();
        if (list.size() == 0) {
            return 0L;
        } else {
            return list.stream()
                    .map(furniture -> furniture.getId())
                    .max(Long::compareTo)
                    .get();
        }
    }

    private List<Furniture> selectFromDataBase(String query) {
        List<Furniture> list = null;
        try {
            list = new ArrayList<>();

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

                List<Component> components =
                        componentService.findAllByFurnitureId(furniture.getId());
                furniture.addComponents(components);

                list.add(furniture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataNotLoadedFromDBException("Не удалось загрузить данные из базы");
        }
        return list;
    }
}
