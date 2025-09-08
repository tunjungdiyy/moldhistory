package com.anoman.machinehistory.shared;

import com.anoman.machinehistory.commons.RepairTableName;
import com.anoman.machinehistory.model.repair.RepairMold;
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
public class RepairMoldBuilder {

    RepairTableName tableName = new RepairTableName();
    String table = tableName.getTableName() + ".";
    String id = tableName.getIdCol();
    String idProblem = tableName.getIdProblemCol();
    String code = tableName.getCodeCol();
    String action = tableName.getActionCol();
    String startRepair = tableName.getReapirStartCol();
    String endRepair = tableName.getRepairEndCol();
    String team = tableName.getTeamCol();
    String note = tableName.getNoteCol();

    ProblemBuilder problemBuilder = new ProblemBuilder();

    public RepairMold repairMold(ResultSet resultSet) {
        RepairMold repairMold = new RepairMold();
        try {
            repairMold.setId(resultSet.getInt(table + id));
            repairMold.setProblem(problemBuilder.problem(resultSet));
            repairMold.setCodeRepair(resultSet.getString(table + code));
            repairMold.setAction(resultSet.getString(table + action));
            repairMold.setRepairStart(resultSet.getLong(table + startRepair));
            repairMold.setReapirEnd(resultSet.getLong(table + endRepair));
            repairMold.setTeamRepair(resultSet.getString(table + team));
            repairMold.setNoteRepair(resultSet.getString(table + note));
        } catch (SQLException e) {
            log.error("failed build repair Mold : " + String.valueOf(e), getClass());
        }

        return  repairMold;
    }

}
