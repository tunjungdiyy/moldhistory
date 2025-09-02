package com.anoman.machinehistory.repository.squence;

import com.anoman.machinehistory.commons.SquenceTableName;
import com.anoman.machinehistory.config.DatabaseConfig;
import com.anoman.machinehistory.model.squence.SquenceRead;
import com.anoman.machinehistory.model.squence.SquenceRegister;
import com.anoman.machinehistory.model.squence.SquenceUpdate;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class SquenceRepositoryImpl implements SquenceRepository{

    SquenceTableName tableName = new SquenceTableName();

    @Override
    public void create(SquenceRegister register) {
        try {
            String query = "insert into " + tableName.getTableName() + " ( " +
                    tableName.getEntityColumn() + " , " + tableName.getPreficColumn() + " , " +
                    tableName.getLastNumberColumn() + " , " + tableName.getPeriodeColumn() + " , " +
                    tableName.getDescriptionColumn() + " ) value (? , ? , ? , ? , ?)";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, register.getEntityName());
            preparedStatement.setString(2, register.getPrefix().toUpperCase());
            preparedStatement.setInt(3, register.getLastNumber());
            preparedStatement.setLong(4,register.getPeriode());
            preparedStatement.setString(5, register.getDescription());
            preparedStatement.executeUpdate();

            log.info("Success Create", getClass());

            preparedStatement.close();
        } catch (SQLException e) {
            log.error("Failed For Create : " + String.valueOf(e), getClass());
        }

    }

    @Override
    public void update(SquenceUpdate update) {

        try {
            String query = "update " + tableName.getTableName() + " set " + tableName.getLastNumberColumn() +
                    " = " + "?"+ " , " + tableName.getPeriodeColumn() + " = " +
                    "?" + " where " + tableName.getIdColumn() + " = " + "?";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, update.getLastNumber());
            preparedStatement.setLong(2, update.getPeriode());
            preparedStatement.setInt(3, update.getId());
            preparedStatement.executeUpdate();

            log.info("success update", getClass());

            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed update : " + String.valueOf(e), getClass());
        }

    }

    @Override
    public SquenceRead findbyEntityWithoutPeriode(String nameEntity) {

        SquenceRead squenceRead = new SquenceRead();

        try {
            String query = "select " + tableName.getIdColumn() + " , " + tableName.getPreficColumn() +
                    " , " + tableName.getLastNumberColumn() + " , " + tableName.getPeriodeColumn() +
                    " from " + tableName.getTableName() + " where " + tableName.getEntityColumn() + " = ?";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, nameEntity);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                squenceRead.setId(resultSet.getInt(tableName.getIdColumn()));
                squenceRead.setLastNumber(resultSet.getInt(tableName.getLastNumberColumn()));
                squenceRead.setPeriode(resultSet.getLong(tableName.getPeriodeColumn()));
                squenceRead.setPrefix(resultSet.getString(tableName.getPreficColumn()));
            }

            log.info("Success find by entity", getClass());
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find by entity : " + String.valueOf(e), getClass());
        }

        return squenceRead;
    }

    @Override
    public SquenceRead findByEntityWithPeriode(String nameEntity, Long periode) {
        SquenceRead squenceRead = new SquenceRead();

        try {
            String query = "select " + tableName.getIdColumn() + " , " + tableName.getPreficColumn() +
                    " , " + tableName.getLastNumberColumn() + " , " + tableName.getPeriodeColumn() +
                    " from " + tableName.getTableName() + " where " + tableName.getEntityColumn() + " = ? and " +
                    tableName.getPeriodeColumn() + " = ?";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, nameEntity);
            preparedStatement.setLong(2, periode);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                squenceRead.setId(resultSet.getInt(tableName.getIdColumn()));
                squenceRead.setLastNumber(resultSet.getInt(tableName.getLastNumberColumn()));
                squenceRead.setPeriode(resultSet.getLong(tableName.getPeriodeColumn()));
                squenceRead.setPrefix(resultSet.getString(tableName.getPreficColumn()));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find by entity & periode : " + String.valueOf(e), getClass());
        }

        return squenceRead;
    }
}
