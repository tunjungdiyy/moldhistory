package com.anoman.machinehistory.repository.product;

import com.anoman.machinehistory.commons.ProductTableName;
import com.anoman.machinehistory.config.DatabaseConfig;
import com.anoman.machinehistory.model.produk.ProductRegister;
import com.anoman.machinehistory.model.produk.ProductUpdateAndRead;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProductRepositoryImpl implements ProductRepository{

    ProductTableName tableName = new ProductTableName();

    @Override
    public void create(ProductRegister productRegister) {

        try {
            String query = "insert into " + tableName.getTableName() + " ( " + tableName.getNameColumn() + " , " +
                    tableName.getCodeProdukColumn() + " , " + tableName.getDescriptionColumn() + " ) value (? , ? , ?)";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, productRegister.getName().toUpperCase());
            preparedStatement.setString(2, productRegister.getCodeProduk().toUpperCase());
            preparedStatement.setString(3, productRegister.getDescription());

            preparedStatement.executeUpdate();

            log.info("Success create product", getClass());
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed create product : " + String.valueOf(e), getClass());
        }


    }

    @Override
    public void update(ProductUpdateAndRead productUpdateAndRead) {

        try {
            String query = "update " + tableName.getTableName() + " set " + tableName.getNameColumn() + " = ? , " +
                    tableName.getCodeProdukColumn() + " = ? , " + tableName.getDescriptionColumn() + " = ? " +
                    "where " + tableName.getIdColumn() + " = ?";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, productUpdateAndRead.getName().toUpperCase());
            preparedStatement.setString(2, productUpdateAndRead.getCodeProduct().toUpperCase());
            preparedStatement.setString(3, productUpdateAndRead.getDescription());
            preparedStatement.setInt(4, productUpdateAndRead.getId());

            preparedStatement.executeUpdate();

            log.info("Success update product ", getClass());
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("Failed update produck : " + String.valueOf(e), getClass());
        }

    }

    @Override
    public ProductUpdateAndRead findbyName(String name) {
        ProductUpdateAndRead productUpdateAndRead = new ProductUpdateAndRead();

        try {
            String query = "select * from " + tableName.getTableName() + " where " + tableName.getNameColumn() + " = ?";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                productUpdateAndRead.setId(resultSet.getInt(tableName.getIdColumn()));
                productUpdateAndRead.setName(resultSet.getString(tableName.getNameColumn()));
                productUpdateAndRead.setCodeProduct(resultSet.getString(tableName.getCodeProdukColumn()));
                productUpdateAndRead.setDescription(resultSet.getString(tableName.getDescriptionColumn()));
            }

            log.info("Success find product by name", getClass());

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find product by name : " + name + " " + String.valueOf(e), getClass());
        }

        return productUpdateAndRead;
    }

    @Override
    public ProductUpdateAndRead findById(Integer id) {
        ProductUpdateAndRead productUpdateAndRead = new ProductUpdateAndRead();

        try {
            String query = "select * from " + tableName.getTableName() + " where " + tableName.getIdColumn()+ " = ?";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                productUpdateAndRead.setId(resultSet.getInt(tableName.getIdColumn()));
                productUpdateAndRead.setName(resultSet.getString(tableName.getNameColumn()));
                productUpdateAndRead.setCodeProduct(resultSet.getString(tableName.getCodeProdukColumn()));
                productUpdateAndRead.setDescription(resultSet.getString(tableName.getDescriptionColumn()));
            }

            log.info("Success find product by id", getClass());

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find product by id : " + id + " " + String.valueOf(e), getClass());
        }

        return productUpdateAndRead;
    }

    @Override
    public List<ProductUpdateAndRead> findAll() {
        List<ProductUpdateAndRead> productUpdateAndReadList = new ArrayList<>();

        try {
            String query = "select * from " + tableName.getTableName();
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                productUpdateAndReadList.add(ProductUpdateAndRead.builder()
                        .id(resultSet.getInt(tableName.getIdColumn()))
                        .codeProduct(resultSet.getString(tableName.getCodeProdukColumn()))
                        .name(resultSet.getString(tableName.getNameColumn()))
                        .description(resultSet.getString(tableName.getDescriptionColumn()))
                        .build());
            }

            log.info("Success find all product", getClass());

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find all product : "  + String.valueOf(e), getClass());
        }

        return productUpdateAndReadList;
    }

    @Override
    public List<ProductUpdateAndRead> findByContain(String keyword) {
        List<ProductUpdateAndRead> productUpdateAndReadList = new ArrayList<>();

        try {
            String query = "select * from " + tableName.getTableName() + " where " + tableName.getNameColumn() + " like " + "'%" + keyword + "%'";
            PreparedStatement preparedStatement = DatabaseConfig.getConnection().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                productUpdateAndReadList.add(ProductUpdateAndRead.builder()
                        .id(resultSet.getInt(tableName.getIdColumn()))
                        .codeProduct(resultSet.getString(tableName.getCodeProdukColumn()))
                        .name(resultSet.getString(tableName.getNameColumn()))
                        .description(resultSet.getString(tableName.getDescriptionColumn()))
                        .build());
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            log.error("failed find product berisi kata : " + keyword  + String.valueOf(e), getClass());
        }

        return productUpdateAndReadList;
    }
}
