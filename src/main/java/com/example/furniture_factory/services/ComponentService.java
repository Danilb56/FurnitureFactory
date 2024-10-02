package com.example.furniture_factory.services;

import com.example.furniture_factory.enums.ComponentTypeEnum;
import com.example.furniture_factory.exceptions.DataNotLoadedFromDBException;
import com.example.furniture_factory.exceptions.NotFoundException;
import com.example.furniture_factory.exceptions.SavingFailedException;
import com.example.furniture_factory.models.Component;
import com.example.furniture_factory.utils.IdUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComponentService extends Service<Component> {

    public ComponentService(Connection connection) {
        super(connection);
    }

    @Override
    public List<Component> findAll() {
        String query = "select *" +
                "from furniture_factory.component c";

        System.out.println("Executed query: " + query);
        return selectFromDataBase(query);
    }

    @Override
    public Component findById(Long id) {
        String query = "select *\n" +
                "from furniture_factory.component c\n" +
                "where c.code = " + id;
        System.out.println("Executed query: " + query);
        List<Component> list = selectFromDataBase(query);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            throw new NotFoundException("Компонент не найден");
        }
    }

    @Override
    public Component update(Component component) {
        try {
            findById(component.getCode());

            String query = "update furniture_factory.component c\n" +
                    "set price              = '" + component.getPrice() + "',\n" +
                    "type              = (?)\n" +
                    "where c.code = " + component.getCode();

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, component.getType().getValue());

            System.out.println("Executed query: " + query);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return component;
    }

    @Override
    public void deleteById(Long id) {
        try {
            findById(id);

            String query = "delete\n" +
                    "from furniture_factory.component c\n" +
                    "where c.code = " + id;

            PreparedStatement ps = connection.prepareStatement(query);
            System.out.println("Executed query: " + query);

            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Component create(Component component) {
        Long code = IdUtils.getNewComponentId(this);
        try {
            String query = """
                            insert into furniture_factory.component (code, price, type) VALUES 
                            ((?), (?), (?))""";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, code);
            ps.setLong(2, component.getPrice());
            ps.setString(3, component.getType().getValue());

            System.out.println("Executed query: " + query);
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
            throw new SavingFailedException("Не удалось сохранить компонент");
        }
        component.setCode(code);
        return component;
    }

    public Long getLargestId() {
        List<Component> list = findAll();
        if (list.size() == 0) {
            return 0L;
        } else {
            return list.stream()
                    .map(furnitureLine -> furnitureLine.getCode())
                    .max(Long::compareTo)
                    .get();
        }
    }

    public List<Component> findAllByFurnitureId(Long furnitureId) {
        String query = "select code, price, type, count\n" +
                "from component c\n" +
                "         left join furniture_component_link fcl\n" +
                "                   on c.code = fcl.component_id\n" +
                "where fcl.furniture_id = " + furnitureId;
        System.out.println("Executed query: " + query);
        return selectFromDataBase(query);
    }

    private List<Component> selectFromDataBase(String query) {
        List<Component> list = null;
        try {
            list = new ArrayList<>();

            PreparedStatement ps = connection.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Component component = new Component(
                        rs.getLong("code"),
                        rs.getLong("price"),
                        ComponentTypeEnum.valueOf(rs.getString("type"))
                );

                if (rs.getMetaData().getColumnCount() > 3) {
                    component.setCount(rs.getLong("count"));
                }

                list.add(component);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataNotLoadedFromDBException("Не удалось загрузить данные из базы");
        }
        return list;
    }

    public void saveComponentList(List<Component> componentList, Long furnitureId) {
        try {
            String deleteQuery = """
                    delete from furniture_factory.furniture_component_link fcl
                    where fcl.furniture_id = (?)""";

            PreparedStatement ps1 = connection.prepareStatement(deleteQuery);
            ps1.setLong(1, furnitureId);
            System.out.println("Executed query: " + deleteQuery);

            ps1.execute();

            String insertQuery = """
                    insert into furniture_factory.furniture_component_link
                    (component_id, furniture_id, count) VALUES ((?), (?), (?))""";

            PreparedStatement ps2 = connection.prepareStatement(insertQuery);

            for (Component component: componentList) {
                ps2.setLong(1, component.getCode());
                ps2.setLong(2, furnitureId);
                ps2.setLong(3, component.getCount());
                ps2.addBatch();
            }
            System.out.println("Executed query " + componentList.size() + " times: " + insertQuery);

            ps2.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
