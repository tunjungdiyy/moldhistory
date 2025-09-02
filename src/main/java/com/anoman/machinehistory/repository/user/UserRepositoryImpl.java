package com.anoman.machinehistory.repository.user;

import com.anoman.machinehistory.commons.UserTableName;
import com.anoman.machinehistory.config.DatabaseConfig;
import com.anoman.machinehistory.model.user.UserRead;
import com.anoman.machinehistory.model.user.UserRegister;
import com.anoman.machinehistory.model.user.UserUpdate;
import com.anoman.machinehistory.security.BCrypt;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserRepositoryImpl implements UserRepository{

    UserTableName userTableName = new UserTableName();

    @Override
    public void createUser(UserRegister register) {

        try {
            String query = "insert into " + userTableName.getTableName() +
                    " ( " + userTableName.getUsernameColumn() + " , " + userTableName.getPasswordColumn() + " , " + userTableName.getUniqueCodeColumn() +" ) " +
                    "value ( ? , ? , ?)";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, register.getUsername());
            preparedStatement.setString(3, register.getUniqueCode());
            preparedStatement.setString(2, BCrypt.hashpw(register.getPassword(), BCrypt.gensalt()));

            preparedStatement.executeUpdate();
            log.info("Berhasil Cretae User", getClass());

            preparedStatement.close();
        } catch (SQLException e) {
            log.info("Gagal create : " + String.valueOf(e), getClass());
        }

    }

    @Override
    public void updateUser(UserUpdate update) {

        try {
            String query = "update " + userTableName.getTableName() + " set " + userTableName.getUsernameColumn() + " = ? , " +
                    userTableName.getPasswordColumn() + " = ? " + "where " + userTableName.getIdColumn() + " = ?";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, update.getUsername());
            preparedStatement.setString(2, BCrypt.hashpw(update.getPassword(), BCrypt.gensalt()));
            preparedStatement.setInt(3, update.getId());

            preparedStatement.executeUpdate();

            log.info("Success update", getClass());
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("Failed Update : " + String.valueOf(e), getClass());
        }

    }

    @Override
    public UserRead findByUsername(String username) {
        UserRead userRead = new UserRead();

        try {
            String quesry = "select " + userTableName.getIdColumn() + " , " + userTableName.getUsernameColumn() + " , " +
                    userTableName.getPasswordColumn() + " from " + userTableName.getTableName() + " where " +
                    userTableName.getUsernameColumn() + " = ?";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(quesry);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userRead.setId(resultSet.getInt(userTableName.getIdColumn()));
                userRead.setUsername(resultSet.getString(userTableName.getUsernameColumn()));
                userRead.setPassword(resultSet.getString(userTableName.getPasswordColumn()));
            }

            log.info("Success find by user ", getClass());

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failde find by User : " + String.valueOf(e), getClass());
        }

        return userRead;
    }

    @Override
    public UserRead findById(Integer id) {
        UserRead userRead = new UserRead();

        try {
            String quesry = "select " + userTableName.getIdColumn() + " , " + userTableName.getUsernameColumn() + " , " +
                    userTableName.getPasswordColumn() + " from " + userTableName.getTableName() + " where " +
                    userTableName.getIdColumn() + " = ?";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(quesry);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userRead.setId(resultSet.getInt(userTableName.getIdColumn()));
                userRead.setUsername(resultSet.getString(userTableName.getUsernameColumn()));
                userRead.setPassword(resultSet.getString(userTableName.getPasswordColumn()));
            }

            log.info("Success find by id ", getClass());

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failde find by id : " + String.valueOf(e), getClass());
        }

        return userRead;
    }

    @Override
    public List<UserRead> findAll() {
        List<UserRead> userReadList = new ArrayList<>();

        try {
            String quesry = "select " + userTableName.getIdColumn() + " , " + userTableName.getUsernameColumn() + " , " +
                    userTableName.getPasswordColumn() + " from " + userTableName.getTableName();
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(quesry);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                userReadList.add(UserRead.builder()
                        .id(resultSet.getInt(userTableName.getIdColumn()))
                        .username(resultSet.getString(userTableName.getUsernameColumn()))
                        .password(resultSet.getString(userTableName.getPasswordColumn()))
                        .build());
            }

            log.info("Success findall ", getClass());

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failde findall : " + String.valueOf(e), getClass());
        }

        return userReadList;
    }
}
