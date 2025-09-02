package com.anoman.machinehistory.shared;

import com.anoman.machinehistory.commons.ProductMoldTableName;
import com.anoman.machinehistory.model.mold.ProductMold;
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
    String id = tableName.getIdCol();
    String code = tableName.getCodeCol();
    String cvt = tableName.getCvtCol();
    String ct = tableName.getCtCol();
    String grammage = tableName.getGrammageCol();
    String desc = tableName.getDescCol();

    ProductBuilder productBuilder = new ProductBuilder();
    MoldBuilder moldBuilder = new MoldBuilder();

    public ProductMold productMold(ResultSet resultSet) {
        ProductMold productMold = new ProductMold();

        try {
            productMold.setId(resultSet.getInt(id));
            productMold.setProduct(productBuilder.productUpdateAndRead(resultSet));
            productMold.setMold(moldBuilder.mold(resultSet));
            productMold.setCode(resultSet.getString(code));
            productMold.setCvt(resultSet.getInt(cvt));
            productMold.setCt(resultSet.getDouble(ct));
            productMold.setGrammage(resultSet.getDouble(grammage));
            productMold.setDescription(resultSet.getString(desc));

        } catch (SQLException e) {
            log.error("failed build product mold : " + String.valueOf(e), getClass());
        }

        return productMold;
    }


}
