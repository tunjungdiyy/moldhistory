package com.anoman.machinehistory.repository.productmold;

import com.anoman.machinehistory.commons.MoldTableName;
import com.anoman.machinehistory.commons.ProductMoldTableName;
import com.anoman.machinehistory.commons.ProductTableName;
import com.anoman.machinehistory.config.DatabaseConfig;
import com.anoman.machinehistory.model.mold.ProductMold;
import com.anoman.machinehistory.shared.ProductMoldBuilder;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProductMoldRepositoryImpl implements ProductMoldRepository{

    ProductMoldTableName tableName = new ProductMoldTableName();
    String table = tableName.getTableName();
    String id = tableName.getIdCol();
    String idMold = tableName.getIdMoldCol();
    String idProduct = tableName.getIdProductCol();
    String code = tableName.getCodeCol();
    String cvt = tableName.getCvtCol();
    String ct = tableName.getCtCol();
    String grammage = tableName.getGrammageCol();
    String desc = tableName.getDescCol();

    MoldTableName moldTableName = new MoldTableName();
    ProductTableName productTableName = new ProductTableName();

    @Override
    public void create(ProductMold productMold) {

        try {
            String query = "insert into " + table + " ( " + idMold + " , " + idProduct + " , " + code + " , " +
                    cvt + " , " + ct + " , " + grammage + " , " + desc + " ) value ( ? , ? , ? , ? , ? , ? , ? )";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, productMold.getMold().getId());
            preparedStatement.setInt(2, productMold.getProduct().getId());
            preparedStatement.setString(3, productMold.getCode());
            preparedStatement.setInt(4, productMold.getCvt());
            preparedStatement.setDouble(5, productMold.getCt());
            preparedStatement.setDouble(6, productMold.getGrammage());
            preparedStatement.setString(7, productMold.getDescription());

            preparedStatement.executeUpdate();

            log.info("success create product", getClass());
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed create product mold : " + String.valueOf(e), getClass());
        }

    }

    @Override
    public List<ProductMold> findByProduct(String productName) {

        List<ProductMold> productMoldList = new ArrayList<>();
        ProductMoldBuilder productMoldBuilder = new ProductMoldBuilder();
        /**
         * select * from product_mold
         * join product on (product.id = product_mold.id_product)
         * join mold on (mold.id = product_mold.id_mold);
         */
        try {
            String query = "select * from " + table +
                    " join " + productTableName.getTableName() + " on ( " + productTableName.getTableName() + "." + productTableName.getIdColumn() + " = " + table + "." + idProduct + " ) " +
                    "join " + moldTableName.getTableName() + " on ( " + moldTableName.getTableName() + "." + moldTableName.getIdCol() + " = " + table + "." + idMold + " ) " +
                    "where " + productTableName.getTableName() + "." + productTableName.getNameColumn() + " = ? ";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, productName);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                productMoldList.add(productMoldBuilder.productMold(resultSet));
            }


            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find by product name : " + productName + String.valueOf(e), getClass());
        }

        return productMoldList;
    }

    @Override
    public List<ProductMold> findByCodeMold(String codeMold) {
        List<ProductMold> productMoldList = new ArrayList<>();
        ProductMoldBuilder productMoldBuilder = new ProductMoldBuilder();
        /**
         * select * from product_mold
         * join product on (product.id = product_mold.id_product)
         * join mold on (mold.id = product_mold.id_mold);
         */
        try {
            String query = "select * from " + table +
                    " join " + productTableName.getTableName() + " on ( " + productTableName.getTableName() + "." + productTableName.getIdColumn() + " = " + table + "." + idProduct + " ) " +
                    "join " + moldTableName.getTableName() + " on ( " + moldTableName.getTableName() + "." + moldTableName.getIdCol() + " = " + table + "." + idMold + " ) " +
                    "where " + moldTableName.getTableName() + "." + moldTableName.getCodeCol() + " = ? ";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, codeMold);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                productMoldList.add(productMoldBuilder.productMold(resultSet));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find by mold code : " + codeMold + String.valueOf(e), getClass());
        }

        return productMoldList;
    }

    @Override
    public List<ProductMold> findByCodeProduct(String codeProduct) {
        List<ProductMold> productMoldList = new ArrayList<>();
        ProductMoldBuilder productMoldBuilder = new ProductMoldBuilder();
        /**
         * select * from product_mold
         * join product on (product.id = product_mold.id_product)
         * join mold on (mold.id = product_mold.id_mold);
         */
        try {
            String query = "select * from " + table +
                    " join " + productTableName.getTableName() + " on ( " + productTableName.getTableName() + "." + productTableName.getIdColumn() + " = " + table + "." + idProduct + " ) " +
                    "join " + moldTableName.getTableName() + " on ( " + moldTableName.getTableName() + "." + moldTableName.getIdCol() + " = " + table + "." + idMold + " ) " +
                    "where " + productTableName.getTableName() + "." + productTableName.getCodeProdukColumn() + " = ? ";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, codeProduct);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                productMoldList.add(productMoldBuilder.productMold(resultSet));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find by product code : " + codeProduct + String.valueOf(e), getClass());
        }

        return productMoldList;
    }

    @Override
    public List<ProductMold> findALl() {
        List<ProductMold> productMoldList = new ArrayList<>();
        ProductMoldBuilder productMoldBuilder = new ProductMoldBuilder();
        /**
         * select * from product_mold
         * join product on (product.id = product_mold.id_product)
         * join mold on (mold.id = product_mold.id_mold);
         */
        try {
            String query = "select * from " + table +
                    " join " + productTableName.getTableName() + " on ( " + productTableName.getTableName() + "." + productTableName.getIdColumn() + " = " + table + "." + idProduct + " ) " +
                    "join " + moldTableName.getTableName() + " on ( " + moldTableName.getTableName() + "." + moldTableName.getIdCol() + " = " + table + "." + idMold + " ) ";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                productMoldList.add(productMoldBuilder.productMold(resultSet));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find all : " + String.valueOf(e), getClass());
        }

        return productMoldList;
    }

}
