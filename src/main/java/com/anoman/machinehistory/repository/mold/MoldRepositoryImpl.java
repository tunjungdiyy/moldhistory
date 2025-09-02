package com.anoman.machinehistory.repository.mold;

import com.anoman.machinehistory.commons.MoldTableName;
import com.anoman.machinehistory.config.DatabaseConfig;
import com.anoman.machinehistory.model.mold.Mold;
import com.anoman.machinehistory.shared.MoldBuilder;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MoldRepositoryImpl implements MoldRepository {

    MoldTableName tableName = new MoldTableName();
    String table = tableName.getTableName();
    String id = tableName.getIdCol();
    String seri = tableName.getSerialNumberCol();
    String code = tableName.getCodeCol();
    String thickness = tableName.getThicknessCol();
    String vertical = tableName.getVerticalCol();
    String horizontal = tableName.getHorizontalCol();
    String tonase = tableName.getTonase();

    MoldBuilder moldBuilder = new MoldBuilder();

    @Override
    public void create(Mold mold) {
        try {
            String query = "insert into " + table + " ( "  + seri + " , " + code + " , " +
                    thickness + " , " + vertical + " , " + horizontal + " , " + tonase + " ) value ( ? , ? , ? , ? , ? , ?)";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, mold.getSerialNumber());
            preparedStatement.setString(2, mold.getCodeMold());
            preparedStatement.setDouble(3, mold.getThickness());
            preparedStatement.setDouble(4, mold.getVertical());
            preparedStatement.setDouble(5, mold.getVertical());
            preparedStatement.setDouble(6, mold.getTonase());

            preparedStatement.executeUpdate();

            log.info("success create mold ", getClass());
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed create : " + String.valueOf(e), getClass());
        }
    }

    @Override
    public void update(Mold mold) {
        try {
            String query = "update " + table + " set " + seri + " = ? , " + code + " = ? , " + thickness + " = ? , " +
                    vertical + " = ? , " + horizontal + " = ? , " + tonase + " = ? where " + id + " = ? ";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, mold.getSerialNumber());
            preparedStatement.setString(2, mold.getCodeMold());
            preparedStatement.setDouble(3, mold.getThickness());
            preparedStatement.setDouble(4, mold.getVertical());
            preparedStatement.setDouble(5, mold.getHorizontal());
            preparedStatement.setDouble(6, mold.getTonase());
            preparedStatement.setInt(7, mold.getId());

            preparedStatement.executeUpdate();
            log.info("success update mold", getClass());

            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed update mold : " + String.valueOf(e), getClass());
        }

    }

    @Override
    public List<Mold> findAll() {
        List<Mold> moldLis = new ArrayList<>();

        try {
            String quesry = " select * from " + table;
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(quesry);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                moldLis.add(moldBuilder.mold(resultSet));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find All Mold " + String.valueOf(e), getClass());
        }

        return moldLis;
    }

    @Override
    public Mold findById(Integer idfind) {

        Mold mold = new Mold();

        try {
            String query = "select * from " + table + " where " + id + " = ? ";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, idfind);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                mold = moldBuilder.mold(resultSet);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed finc mold by Id : " + idfind + String.valueOf(e), getClass());
        }

        return mold;
    }

    @Override
    public Mold findByCode(String codeFind) {
        Mold mold = new Mold();

        try {
            String query = "select * from " + table + " where " + code + " = ? ";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, codeFind);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                mold = moldBuilder.mold(resultSet);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed finc mold by code : " + codeFind + String.valueOf(e), getClass());
        }

        return mold;
    }
}
