package com.anoman.machinehistory.shared;

import com.anoman.machinehistory.commons.ProblemTableName;
import com.anoman.machinehistory.model.problem.Problem;
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
public class ProblemBuilder {

    ProblemTableName tableName = new ProblemTableName();
    String table = tableName.getTableName();
    String id = tableName.getIdCol();;
    String code = tableName.getCodeCol();
    String part = tableName.getPartCol();
    String problem = tableName.getProblemCol();
    String problemDate = tableName.getProblemDateCil();
    String desc = tableName.getDescCol();
    String status = tableName.getStatusCol();

    ProductMoldBuilder productMoldBuilder = new ProductMoldBuilder();

    public Problem problem(ResultSet resultSet) {
        Problem problemValue = new Problem();

        try {
            problemValue.setId(resultSet.getInt(table + "." + id));
            problemValue.setProductMold(productMoldBuilder.productMold(resultSet));
            problemValue.setCodeProblem(resultSet.getString(table + "." + code));
            problemValue.setPart(resultSet.getString(table + "." + part));
            problemValue.setProblem(resultSet.getString(table + "." + problem));
            problemValue.setProblemDate(resultSet.getLong(table + "." + problemDate));
            problemValue.setDescriptionProblem(resultSet.getString(table + "." + desc));
            problemValue.setStatus(resultSet.getString(table + "." + status));
        } catch (SQLException e) {
            log.error("Failed Build Problem : " + String.valueOf(e), getClass());
        }

        return problemValue;
    }

}
