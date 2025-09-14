package com.anoman.machinehistory.shared;

import com.anoman.machinehistory.commons.MoldTableName;
import com.anoman.machinehistory.model.mold.Mold;
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
public class MoldBuilder {

    MoldTableName tableName = new MoldTableName();
    String table = tableName.getTableName();
    String id = tableName.getIdCol();
    String serialNumber = tableName.getSerialNumberCol();
    String code = tableName.getCodeCol();
    String thickness = tableName.getThicknessCol();
    String vertical = tableName.getVerticalCol();
    String horizontal = tableName.getHorizontalCol();
    String tonase = tableName.getTonase();

    public Mold mold(ResultSet resultSet) {
        Mold mold = new Mold();

        try {
            mold.setId(resultSet.getInt(table + "." + id));
            mold.setSerialNumber(resultSet.getString(table + "." +serialNumber));
            mold.setCodeMold(resultSet.getString(table + "." +code));
            mold.setThickness(resultSet.getDouble(table + "." +thickness));
            mold.setVertical(resultSet.getDouble(table + "." + vertical));
            mold.setHorizontal(resultSet.getDouble(table + "." +horizontal));
            mold.setTonase(resultSet.getDouble(table + "." + tonase));
        } catch (SQLException e) {
            log.error("Failed Build Mold : " + String.valueOf(e), getClass());
        }

        return mold;
    }

    public Mold moldAlias(ResultSet resultSet) {
        Mold mold = new Mold();

        try {
            mold.setId(resultSet.getInt("m." + id));
            mold.setSerialNumber(resultSet.getString("m." +serialNumber));
            mold.setCodeMold(resultSet.getString("m." +code));
            mold.setThickness(resultSet.getDouble("m." +thickness));
            mold.setVertical(resultSet.getDouble("m." + vertical));
            mold.setHorizontal(resultSet.getDouble("m." +horizontal));
            mold.setTonase(resultSet.getDouble("m." + tonase));
        } catch (SQLException e) {
            log.error("Failed Build Mold : " + String.valueOf(e), getClass());
        }

        return mold;
    }


}
