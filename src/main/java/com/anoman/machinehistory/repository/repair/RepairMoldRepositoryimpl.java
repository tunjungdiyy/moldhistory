package com.anoman.machinehistory.repository.repair;

import com.anoman.machinehistory.commons.*;
import com.anoman.machinehistory.config.DatabaseConfig;
import com.anoman.machinehistory.model.repair.RepairMold;
import com.anoman.machinehistory.shared.RepairMoldBuilder;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RepairMoldRepositoryimpl implements RepairMoldRepository{

    RepairTableName repairTableName = new RepairTableName();
    String table = repairTableName.getTableName();
    String id = repairTableName.getIdCol();
    String idProblem = repairTableName.getIdProblemCol();
    String code = repairTableName.getCodeCol();
    String action = repairTableName.getActionCol();
    String startRepair = repairTableName.getReapirStartCol();
    String endRepair = repairTableName.getRepairEndCol();
    String team = repairTableName.getTeamCol();
    String note = repairTableName.getNoteCol();

    ProblemTableName problemTableName = new ProblemTableName();
    ProductMoldTableName productMoldTableName = new ProductMoldTableName();
    ProductTableName productTableName = new ProductTableName();
    MoldTableName moldTableName = new MoldTableName();

    RepairMoldBuilder repairMoldBuilder = new RepairMoldBuilder();

    @Override
    public void create(RepairMold repairMold) {

        try {
            String query = "insert into " + table + " ( " + idProblem + " , " + code + " , " + action + " , " +
                    startRepair + " , " + endRepair + " , " + team + " , " + note + " ) value ( ? , ? , ? , ? , ? , ? , ? )";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, repairMold.getProblem().getId());
            preparedStatement.setString(2, repairMold.getCodeRepair());
            preparedStatement.setString(3, repairMold.getAction());
            preparedStatement.setLong(4, repairMold.getRepairStart());
            preparedStatement.setLong(5, repairMold.getReapirEnd());
            preparedStatement.setString(6, repairMold.getTeamRepair());
            preparedStatement.setString(7, repairMold.getNoteRepair());

            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed create repair mold : " + String.valueOf(e), getClass());
        }

    }

    @Override
    public void update(RepairMold repairMold) {

        try {
            String query = "update " + table + " set " + idProblem + " = ? , " + code + " = ? , " +
                    action + " = ? , " + startRepair + " = ? , " + endRepair + " = ? , " + team + " = ? , " +
                    note + " = ? where " + id + " = ? ";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, repairMold.getProblem().getId());
            preparedStatement.setString(2, repairMold.getCodeRepair());
            preparedStatement.setString(3, repairMold.getAction());
            preparedStatement.setLong(4, repairMold.getRepairStart());
            preparedStatement.setLong(5, repairMold.getReapirEnd());
            preparedStatement.setString(6, repairMold.getTeamRepair());
            preparedStatement.setString(7, repairMold.getNoteRepair());
            preparedStatement.setInt(8, repairMold.getId());

            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed Update repair Mold : " + String.valueOf(e), getClass());
        }

    }

    @Override
    public List<RepairMold> findByProductMold(Integer idProductMoldFind) {

        List<RepairMold> repairMoldList = new ArrayList<>();
        try {
            /**
             * select * from repair_mold
             * join problem_mold on (problem_mold.id_problem = repair_mold.id_problem_mold)
             * join product_mold on (product_mold.id = problem_mold.id_product_mold)
             * join product on (product.id = product_mold.id_product)
             * join mold on (mold.id = product_mold.id_mold);
             */

            String query = "select * from " + table +
                    " join " + problemTableName.getTableName() + " on ( " + problemTableName.getTableName() + "." + problemTableName.getIdCol() + " = " +
                    table + "." + idProblem + " ) " +
                    "join " + productMoldTableName.getTableName() + " on ( " + productMoldTableName.getTableName() + "." + productMoldTableName.getIdCol() + " = " +
                    problemTableName.getTableName() + "." + problemTableName.getIdProductMoldCol() + " ) " +
                    "join " + productTableName.getTableName() + " on ( " + productTableName.getTableName() + "." + productTableName.getIdColumn() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdProductCol() + " ) " +
                    "join " + moldTableName.getTableName() + " on ( " + moldTableName.getTableName() + "." + moldTableName.getIdCol() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdMoldCol() + " ) " +
                    "where " + productMoldTableName.getTableName() + "." + productMoldTableName.getIdCol() + " = ?";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, idProductMoldFind);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                repairMoldList.add(repairMoldBuilder.repairMold(resultSet));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find by id product mold :" + String.valueOf(e), getClass());
        }

        return repairMoldList;
    }

    @Override
    public List<RepairMold> findByTeamRepairContaint(String keyword) {
        List<RepairMold> repairMoldList = new ArrayList<>();
        try {
            /**
             * select * from repair_mold
             * join problem_mold on (problem_mold.id_problem = repair_mold.id_problem_mold)
             * join product_mold on (product_mold.id = problem_mold.id_product_mold)
             * join product on (product.id = product_mold.id_product)
             * join mold on (mold.id = product_mold.id_mold);
             */

            String query = "select * from " + table +
                    " join " + problemTableName.getTableName() + " on ( " + problemTableName.getTableName() + "." + problemTableName.getIdCol() + " = " +
                    table + "." + idProblem + " ) " +
                    "join " + productMoldTableName.getTableName() + " on ( " + productMoldTableName.getTableName() + "." + productMoldTableName.getIdCol() + " = " +
                    problemTableName.getTableName() + "." + problemTableName.getIdProductMoldCol() + " ) " +
                    "join " + productTableName.getTableName() + " on ( " + productTableName.getTableName() + "." + productTableName.getIdColumn() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdProductCol() + " ) " +
                    "join " + moldTableName.getTableName() + " on ( " + moldTableName.getTableName() + "." + moldTableName.getIdCol() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdMoldCol() + " ) " +
                    "where " + table + "." + team + " like " + "'%" + keyword + "%'";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                repairMoldList.add(repairMoldBuilder.repairMold(resultSet));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find by team repair :" + String.valueOf(e), getClass());
        }

        return repairMoldList;
    }

    @Override
    public List<RepairMold> findByDateEndRepair(Long datefrom, Long dateto) {
        List<RepairMold> repairMoldList = new ArrayList<>();
        try {
            /**
             * select * from repair_mold
             * join problem_mold on (problem_mold.id_problem = repair_mold.id_problem_mold)
             * join product_mold on (product_mold.id = problem_mold.id_product_mold)
             * join product on (product.id = product_mold.id_product)
             * join mold on (mold.id = product_mold.id_mold);
             */

            String query = "select * from " + table +
                    " join " + problemTableName.getTableName() + " on ( " + problemTableName.getTableName() + "." + problemTableName.getIdCol() + " = " +
                    table + "." + idProblem + " ) " +
                    "join " + productMoldTableName.getTableName() + " on ( " + productMoldTableName.getTableName() + "." + productMoldTableName.getIdCol() + " = " +
                    problemTableName.getTableName() + "." + problemTableName.getIdProductMoldCol() + " ) " +
                    "join " + productTableName.getTableName() + " on ( " + productTableName.getTableName() + "." + productTableName.getIdColumn() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdProductCol() + " ) " +
                    "join " + moldTableName.getTableName() + " on ( " + moldTableName.getTableName() + "." + moldTableName.getIdCol() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdMoldCol() + " ) " +
                    "where " + endRepair + " between " + datefrom + " and " + dateto;
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                repairMoldList.add(repairMoldBuilder.repairMold(resultSet));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find date endRepair :" + String.valueOf(e), getClass());
        }

        return repairMoldList;
    }

    @Override
    public List<RepairMold> findByProductName(String nameProductFind) {
        List<RepairMold> repairMoldList = new ArrayList<>();
        try {
            /**
             * select * from repair_mold
             * join problem_mold on (problem_mold.id_problem = repair_mold.id_problem_mold)
             * join product_mold on (product_mold.id = problem_mold.id_product_mold)
             * join product on (product.id = product_mold.id_product)
             * join mold on (mold.id = product_mold.id_mold);
             */

            String query = "select * from " + table +
                    " join " + problemTableName.getTableName() + " on ( " + problemTableName.getTableName() + "." + problemTableName.getIdCol() + " = " +
                    table + "." + idProblem + " ) " +
                    "join " + productMoldTableName.getTableName() + " on ( " + productMoldTableName.getTableName() + "." + productMoldTableName.getIdCol() + " = " +
                    problemTableName.getTableName() + "." + problemTableName.getIdProductMoldCol() + " ) " +
                    "join " + productTableName.getTableName() + " on ( " + productTableName.getTableName() + "." + productTableName.getIdColumn() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdProductCol() + " ) " +
                    "join " + moldTableName.getTableName() + " on ( " + moldTableName.getTableName() + "." + moldTableName.getIdCol() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdMoldCol() + " ) " +
                    "where " + productTableName.getTableName() + "." + productTableName.getNameColumn() + " like " + "'%" + nameProductFind + "%'";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                repairMoldList.add(repairMoldBuilder.repairMold(resultSet));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find by id product name :" + String.valueOf(e), getClass());
        }

        return repairMoldList;
    }
}
