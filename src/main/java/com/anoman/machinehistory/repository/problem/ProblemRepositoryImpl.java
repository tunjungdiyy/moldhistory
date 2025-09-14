package com.anoman.machinehistory.repository.problem;

import com.anoman.machinehistory.commons.MoldTableName;
import com.anoman.machinehistory.commons.ProblemTableName;
import com.anoman.machinehistory.commons.ProductMoldTableName;
import com.anoman.machinehistory.commons.ProductTableName;
import com.anoman.machinehistory.config.DatabaseConfig;
import com.anoman.machinehistory.model.problem.Problem;
import com.anoman.machinehistory.model.problem.ProblemUpdate;
import com.anoman.machinehistory.shared.ProblemBuilder;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProblemRepositoryImpl implements ProblemRepository{

    ProblemTableName problemTableName = new ProblemTableName();
    String table = problemTableName.getTableName();
    String id = problemTableName.getIdCol();
    String code = problemTableName.getCodeCol();
    String idProductMold = problemTableName.getIdProductMoldCol();
    String part = problemTableName.getPartCol();
    String problemCol = problemTableName.getProblemCol();
    String problemDate = problemTableName.getProblemDateCil();
    String status = problemTableName.getStatusCol();
    String desc = problemTableName.getDescCol();


    ProductMoldTableName productMoldTableName = new ProductMoldTableName();
    MoldTableName moldTableName = new MoldTableName();
    ProductTableName productTableName = new ProductTableName();

    ProblemBuilder problemBuilder = new ProblemBuilder();

    @Override
    public void create(Problem problem) {
        try {
            String query = "insert into " + table + " ( " + code + " , " + idProductMold + " , " +
                    part + " , " + problemCol + " , " + problemDate + " , " + status + " , " +
                    desc + " ) value (? , ? , ? , ? , ? , ? , ? )";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, problem.getCodeProblem());
            preparedStatement.setInt(2, problem.getProductMold().getId());
            preparedStatement.setString(3, problem.getPart());
            preparedStatement.setString(4, problem.getProblem());
            preparedStatement.setLong(5, problem.getProblemDate());
            preparedStatement.setString(6, problem.getStatus().toUpperCase());
            preparedStatement.setString(7, problem.getDescriptionProblem());

            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed create proble " + String.valueOf(e), getClass());
        }
    }

    @Override
    public void update(ProblemUpdate problemUpdate) {

        try {
            String query = "update " + table + " set " + idProductMold + " = ? , " + part + " = ? , " +
                    problemCol + " = ? , " + problemDate + " = ? , " + status + " = ? , " + desc + " = ? " +
                    "where " + id + " = ?";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, problemUpdate.getProductMold().getId());
            preparedStatement.setString(2, problemUpdate.getPart());
            preparedStatement.setString(3, problemUpdate.getProblem());
            preparedStatement.setLong(4, problemUpdate.getProblemDate());
            preparedStatement.setString(5, problemUpdate.getStatus().toUpperCase());
            preparedStatement.setString(6, problemUpdate.getDescription());
            preparedStatement.setInt(7, problemUpdate.getId());

            log.info(query, getClass());

            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed update " + String.valueOf(e), getClass());
        }

    }

    @Override
    public List<Problem> findALl() {

        List<Problem> problemList = new ArrayList<>();

        /**
         * select * from problem_mold
         * join product_mold on (product_mold.id = problem_mold.id_product_mold)
         * join product on (product.id = product_mold.id_product)
         * join mold on (mold.id = product_mold.id_mold);
         */

        try {
            String query = "select * from " + table + " join " +
                    productMoldTableName.getTableName() + " on ( " + productMoldTableName.getTableName() + "." + productMoldTableName.getIdCol() + " = " +
                    table + "." + idProductMold + " ) join " +
                    productTableName.getTableName() + " on ( " + productTableName.getTableName() + "." + productTableName.getIdColumn() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdProductCol() + ") join " +
                    moldTableName.getTableName() + " on ( " + moldTableName.getTableName() + "." + moldTableName.getIdCol() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdMoldCol() + " ) ";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            log.info(query, getClass());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                problemList.add(problemBuilder.problem(resultSet));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find all : " + String.valueOf(e), getClass());
        }

        return problemList;
    }

    @Override
    public List<Problem> findByStatus(String statusfind) {
        List<Problem> problemList = new ArrayList<>();

        /**
         * select * from problem_mold
         * join product_mold on (product_mold.id = problem_mold.id_product_mold)
         * join product on (product.id = product_mold.id_product)
         * join mold on (mold.id = product_mold.id_mold);
         */

        try {
            String query = "select * from " + table + " join " +
                    productMoldTableName.getTableName() + " on ( " + productMoldTableName.getTableName() + "." + productMoldTableName.getIdCol() + " = " +
                    table + "." + idProductMold + " ) join " +
                    productTableName.getTableName() + " on ( " + productTableName.getTableName() + "." + productTableName.getIdColumn() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdProductCol() + ") join " +
                    moldTableName.getTableName() + " on ( " + moldTableName.getTableName() + "." + moldTableName.getIdCol() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdMoldCol() + " ) " +
                    "where " + table + "." + status + " = ? ";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, statusfind);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                problemList.add(problemBuilder.problem(resultSet));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find by status : " + String.valueOf(e), getClass());
        }

        return problemList;
    }

    @Override
    public List<Problem> findByCodeProductMold(String codeProductMold) {
        List<Problem> problemList = new ArrayList<>();

        /**
         * select * from problem_mold
         * join product_mold on (product_mold.id = problem_mold.id_product_mold)
         * join product on (product.id = product_mold.id_product)
         * join mold on (mold.id = product_mold.id_mold);
         */

        try {
            String query = "select * from " + table + " join " +
                    productMoldTableName.getTableName() + " on ( " + productMoldTableName.getTableName() + "." + productMoldTableName.getIdCol() + " = " +
                    table + "." + idProductMold + " ) join " +
                    productTableName.getTableName() + " on ( " + productTableName.getTableName() + "." + productTableName.getIdColumn() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdProductCol() + ") join " +
                    moldTableName.getTableName() + " on ( " + moldTableName.getTableName() + "." + moldTableName.getIdCol() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdMoldCol() + " ) " +
                    "where " + productMoldTableName.getTableName() + "." + productMoldTableName.getCodeCol() + " = ? ";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, codeProductMold);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                problemList.add(problemBuilder.problem(resultSet));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find by status : " + String.valueOf(e), getClass());
        }

        return problemList;
    }

    @Override
    public Problem findbyCodeProblem(String codeProblem) {
        Problem problem = new Problem();

        /**
         * select * from problem_mold
         * join product_mold on (product_mold.id = problem_mold.id_product_mold)
         * join product on (product.id = product_mold.id_product)
         * join mold on (mold.id = product_mold.id_mold);
         */

        try {
            String query = "select * from " + table + " join " +
                    productMoldTableName.getTableName() + " on ( " + productMoldTableName.getTableName() + "." + productMoldTableName.getIdCol() + " = " +
                    table + "." + idProductMold + " ) join " +
                    productTableName.getTableName() + " on ( " + productTableName.getTableName() + "." + productTableName.getIdColumn() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdProductCol() + ") join " +
                    moldTableName.getTableName() + " on ( " + moldTableName.getTableName() + "." + moldTableName.getIdCol() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdMoldCol() + " ) " +
                    "where " + table + "." + code + " = ? ";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, codeProblem);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                problem = problemBuilder.problem(resultSet);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find by code problem : " + String.valueOf(e), getClass());
        }

        return problem;
    }

    @Override
    public Problem findById(Integer idProblem) {
        Problem problem = new Problem();

        /**
         * select * from problem_mold
         * join product_mold on (product_mold.id = problem_mold.id_product_mold)
         * join product on (product.id = product_mold.id_product)
         * join mold on (mold.id = product_mold.id_mold);
         */

        try {
            String query = "select * from " + table + " join " +
                    productMoldTableName.getTableName() + " on ( " + productMoldTableName.getTableName() + "." + productMoldTableName.getIdCol() + " = " +
                    table + "." + idProductMold + " ) join " +
                    productTableName.getTableName() + " on ( " + productTableName.getTableName() + "." + productTableName.getIdColumn() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdProductCol() + ") join " +
                    moldTableName.getTableName() + " on ( " + moldTableName.getTableName() + "." + moldTableName.getIdCol() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdMoldCol() + " ) " +
                    "where " + table + "." + id + " = ? ";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, idProblem);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                problem = problemBuilder.problem(resultSet);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find by code problem : " + String.valueOf(e), getClass());
        }

        return problem;
    }

    @Override
    public List<Problem> findByDateAndStatus(Long milisfrom, Long milisto, String statusFind) {
        List<Problem> problemList = new ArrayList<>();

        /**
         * select * from problem_mold
         * join product_mold on (product_mold.id = problem_mold.id_product_mold)
         * join product on (product.id = product_mold.id_product)
         * join mold on (mold.id = product_mold.id_mold);
         */

        try {
            String query = "select * from " + table + " join " +
                    productMoldTableName.getTableName() + " on ( " + productMoldTableName.getTableName() + "." + productMoldTableName.getIdCol() + " = " +
                    table + "." + idProductMold + " ) join " +
                    productTableName.getTableName() + " on ( " + productTableName.getTableName() + "." + productTableName.getIdColumn() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdProductCol() + ") join " +
                    moldTableName.getTableName() + " on ( " + moldTableName.getTableName() + "." + moldTableName.getIdCol() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdMoldCol() + " ) " +
                    "where " + status + " = ? and " +
                    problemDate + " between " + milisfrom + " and " + milisto +
                    " order by " + table + "." + problemDate;
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, statusFind);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                problemList.add(problemBuilder.problem(resultSet));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find by date and status : " + String.valueOf(e), getClass());
        }

        return problemList;
    }

    @Override
    public List<Problem> findByDate(Long milisfrom, Long milisTo) {
        List<Problem> problemList = new ArrayList<>();

        /**
         * select * from problem_mold
         * join product_mold on (product_mold.id = problem_mold.id_product_mold)
         * join product on (product.id = product_mold.id_product)
         * join mold on (mold.id = product_mold.id_mold);
         */

        try {
            String query = "select * from " + table + " join " +
                    productMoldTableName.getTableName() + " on ( " + productMoldTableName.getTableName() + "." + productMoldTableName.getIdCol() + " = " +
                    table + "." + idProductMold + " ) join " +
                    productTableName.getTableName() + " on ( " + productTableName.getTableName() + "." + productTableName.getIdColumn() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdProductCol() + ") join " +
                    moldTableName.getTableName() + " on ( " + moldTableName.getTableName() + "." + moldTableName.getIdCol() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdMoldCol() + " ) " +
                    "where " + problemDate + " between " + milisfrom + " and " + milisTo +
                    " order by " + table + "." + problemDate ;
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                problemList.add(problemBuilder.problem(resultSet));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find by date : " + String.valueOf(e), getClass());
        }

        return problemList;
    }

    @Override
    public List<Problem> findByProductName(String productNameKeyword) {
        List<Problem> problemList = new ArrayList<>();

        /**
         * select * from problem_mold
         * join product_mold on (product_mold.id = problem_mold.id_product_mold)
         * join product on (product.id = product_mold.id_product)
         * join mold on (mold.id = product_mold.id_mold);
         */

        try {
            String query = "select * from " + table + " join " +
                    productMoldTableName.getTableName() + " on ( " + productMoldTableName.getTableName() + "." + productMoldTableName.getIdCol() + " = " +
                    table + "." + idProductMold + " ) join " +
                    productTableName.getTableName() + " on ( " + productTableName.getTableName() + "." + productTableName.getIdColumn() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdProductCol() + ") join " +
                    moldTableName.getTableName() + " on ( " + moldTableName.getTableName() + "." + moldTableName.getIdCol() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdMoldCol() + " ) " +
                    "where " + productTableName.getTableName() + "." + productTableName.getNameColumn() + " like " + "'%" + productNameKeyword + "%'";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                problemList.add(problemBuilder.problem(resultSet));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find by status : " + String.valueOf(e), getClass());
        }

        return problemList;
    }

    @Override
    public List<Problem> findByCodeContain(String keyword) {
        List<Problem> problemList = new ArrayList<>();

        /**
         * select * from problem_mold
         * join product_mold on (product_mold.id = problem_mold.id_product_mold)
         * join product on (product.id = product_mold.id_product)
         * join mold on (mold.id = product_mold.id_mold);
         */

        try {
            String query = "select * from " + table + " join " +
                    productMoldTableName.getTableName() + " on ( " + productMoldTableName.getTableName() + "." + productMoldTableName.getIdCol() + " = " +
                    table + "." + idProductMold + " ) join " +
                    productTableName.getTableName() + " on ( " + productTableName.getTableName() + "." + productTableName.getIdColumn() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdProductCol() + ") join " +
                    moldTableName.getTableName() + " on ( " + moldTableName.getTableName() + "." + moldTableName.getIdCol() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdMoldCol() + " ) " +
                    "where " + table + "." + code + " like " + "'%" + keyword + "%'" +
                    " and " + table + "." + status + " != 'SELESAI' "  ;
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                problemList.add(problemBuilder.problem(resultSet));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find by code contain : " + String.valueOf(e), getClass());
        }

        return problemList;
    }

    @Override
    public List<Problem> findByCodeProductMoldAndDate(String codeProductMold, Long milisFrom, Long milisTo) {
        List<Problem> problemList = new ArrayList<>();

        /**
         * select * from problem_mold
         * join product_mold on (product_mold.id = problem_mold.id_product_mold)
         * join product on (product.id = product_mold.id_product)
         * join mold on (mold.id = product_mold.id_mold);
         */

        try {
            String query = "select * from " + table + " join " +
                    productMoldTableName.getTableName() + " on ( " + productMoldTableName.getTableName() + "." + productMoldTableName.getIdCol() + " = " +
                    table + "." + idProductMold + " ) join " +
                    productTableName.getTableName() + " on ( " + productTableName.getTableName() + "." + productTableName.getIdColumn() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdProductCol() + ") join " +
                    moldTableName.getTableName() + " on ( " + moldTableName.getTableName() + "." + moldTableName.getIdCol() + " = " +
                    productMoldTableName.getTableName() + "." + productMoldTableName.getIdMoldCol() + " ) " +
                    "where " + productMoldTableName.getTableName() + "." + productMoldTableName.getCodeCol() + " = ? and " +
                    problemDate + " between " + milisFrom + " and " + milisTo +
                    " order by " + table + "." + problemDate;
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, codeProductMold);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                problemList.add(problemBuilder.problem(resultSet));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find by status : " + String.valueOf(e), getClass());
        }

        return problemList;
    }
}
