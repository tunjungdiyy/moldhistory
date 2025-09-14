package com.anoman.machinehistory.shared;

import com.anoman.machinehistory.commons.ProductTableName;
import com.anoman.machinehistory.model.produk.ProductUpdateAndRead;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class ProductBuilder {

    ProductTableName tableName = new ProductTableName();
    String table = tableName.getTableName();
    String id = tableName.getIdColumn();
    String productName = tableName.getNameColumn();
    String code = tableName.getCodeProdukColumn();
    String desc = tableName.getDescriptionColumn();

    public ProductUpdateAndRead productUpdateAndRead(ResultSet resultSet) {
        ProductUpdateAndRead productUpdateAndRead = new ProductUpdateAndRead();
        try {
            productUpdateAndRead.setId(resultSet.getInt(table + "."+ id));
            productUpdateAndRead.setName(resultSet.getString(table + "."+ productName));
            productUpdateAndRead.setCodeProduct(resultSet.getString(table + "."+ code));
            productUpdateAndRead.setDescription(resultSet.getString(table + "."+ desc));

        } catch (SQLException e) {
            log.error("faled build product : " + String.valueOf(e), getClass());
        }

        return productUpdateAndRead;
    }

    public ProductUpdateAndRead productUpdateAndReadAlias(ResultSet resultSet) {
        ProductUpdateAndRead productUpdateAndRead = new ProductUpdateAndRead();
        try {
            productUpdateAndRead.setId(resultSet.getInt("p."+ id));
            productUpdateAndRead.setName(resultSet.getString("p."+ productName));
            productUpdateAndRead.setCodeProduct(resultSet.getString("p."+ code));
            productUpdateAndRead.setDescription(resultSet.getString("p."+ desc));

        } catch (SQLException e) {
            log.error("faled build product : " + String.valueOf(e), getClass());
        }

        return productUpdateAndRead;
    }

}
