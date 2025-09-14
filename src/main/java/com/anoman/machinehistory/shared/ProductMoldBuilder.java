package com.anoman.machinehistory.shared;

import com.anoman.machinehistory.commons.ProductMoldTableName;
import com.anoman.machinehistory.model.mold.ProductMold;
import com.anoman.machinehistory.model.mold.ProductMoldReview;
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
public class ProductMoldBuilder {

    ProductMoldTableName tableName = new ProductMoldTableName();
    String table = tableName.getTableName();
    String id = tableName.getIdCol();
    String code = tableName.getCodeCol();
    String cvt = tableName.getCvtCol();
    String ct = tableName.getCtCol();
    String grammage = tableName.getGrammageCol();
    String desc = tableName.getDescriptionCol();

    ProductBuilder productBuilder = new ProductBuilder();
    MoldBuilder moldBuilder = new MoldBuilder();

    public ProductMold productMold(ResultSet resultSet) {
        ProductMold productMold = new ProductMold();

        try {
            productMold.setId(resultSet.getInt(table + "." + id));
            productMold.setProduct(productBuilder.productUpdateAndRead(resultSet));
            productMold.setMold(moldBuilder.mold(resultSet));
            productMold.setCode(resultSet.getString(table + "." + code));
            productMold.setCvt(resultSet.getInt(table + "." + cvt));
            productMold.setCt(resultSet.getDouble(table + "." + ct));
            productMold.setGrammage(resultSet.getDouble(table + "." + grammage));
            productMold.setDescription(resultSet.getString(table + "." + desc));

        } catch (SQLException e) {
            log.error("failed build product mold : " + String.valueOf(e), getClass());
        }

        return productMold;
    }

    public ProductMoldReview productMoldWithProblem (ResultSet resultSet) {
        ProductMoldReview productMoldReview = new ProductMoldReview();

        try {
            productMoldReview.setId(resultSet.getInt("pm." + id));
            productMoldReview.setProduct(productBuilder.productUpdateAndReadAlias(resultSet));
            productMoldReview.setMold(moldBuilder.moldAlias(resultSet));
            productMoldReview.setCode(resultSet.getString("pm." + code));
            productMoldReview.setCvt(resultSet.getInt("pm."  + cvt));
            productMoldReview.setCt(resultSet.getDouble("pm."  + ct));
            productMoldReview.setGrammage(resultSet.getDouble("pm."  + grammage));
            productMoldReview.setDescription(resultSet.getString("pm."  + desc));
            productMoldReview.setCountProblem(resultSet.getInt("problem"));


        } catch (SQLException e) {
            log.error("failed build product mold : " + String.valueOf(e), getClass());
        }

        return productMoldReview;
    }


}
