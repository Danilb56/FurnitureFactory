package com.example.furniture_factory.services;

import com.example.furniture_factory.enums.Role;
import com.example.furniture_factory.exceptions.DataNotLoadedFromDBException;
import com.example.furniture_factory.exceptions.NotFoundException;
import com.example.furniture_factory.exceptions.SavingFailedException;
import com.example.furniture_factory.models.User;
import com.example.furniture_factory.utils.IdUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService extends Service<User> {

    public UserService(Connection connection) {
        super(connection);
    }

    @Override
    public List<User> findAll() {
        String query = "select * " +
                "from furniture_factory.user u";

        System.out.println("Executed query: " + query);
        return selectFromDataBase(query);
    }

    @Override
    public User findById(Long id) {
        String query = "select *\n" +
                "from furniture_factory.user u\n" +
                "where u.id = " + id;
        System.out.println("Executed query: " + query);
        List<User> list = selectFromDataBase(query);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public User update(User user) {
        try {
            findById(user.getId());

            String query = "update furniture_factory.user u\n" +
                    "set login              = " + user.getLogin() + ",\n" +
                    "    password           = " + user.getPassword() + "\n" +
                    "where u.id = " + user.getId();

            PreparedStatement ps = connection.prepareStatement(query);

            System.out.println("Executed query: " + query);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void deleteById(Long id) {
        throw new DataNotLoadedFromDBException("Невозможно удалить пользователя");
    }

    @Override
    public User create(User user) {
        Long id = IdUtils.getNewUserId(this);
        try {
            String query = "insert into furniture_factory.user (id, login, password, role)\n" +
                    "        VALUES (" +
                    id + ", " +
                    user.getLogin() + ", " +
                    user.getPassword() + ", '" +
                    user.getRole().getValue() + "')";

            PreparedStatement ps = connection.prepareStatement(query);

            System.out.println("Executed query: " + query);
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
            throw new SavingFailedException("Не удалось сохранить пользователя");
        }
        user.setId(id);
        return user;
    }

    public User findByLogin(String login) {
        String query = "select * from furniture_factory.user u" +
                "where u.login = " + login;
        List<User> foundUsers = selectFromDataBase(query);
        if (foundUsers.size() > 0) {
            return foundUsers.get(0);
        } else {
            return null;
        }
    }

    public Long getLargestId() {
        List<User> list = findAll();
        if (list.size() == 0) {
            return 0L;
        } else {
            return list.stream()
                    .map(user -> user.getId())
                    .max(Long::compareTo)
                    .get();
        }
    }

    private List<User> selectFromDataBase(String query) {
        List<User> list = null;
        try {
            list = new ArrayList<>();

            PreparedStatement ps = connection.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                User user = new User(
                        rs.getLong("id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        Role.valueOf(rs.getString("role"))
                );

                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataNotLoadedFromDBException("Не удалось загрузить данные из базы");
        }
        return list;
    }
}
