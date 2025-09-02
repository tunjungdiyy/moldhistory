package com.anoman.machinehistory.commons;

import lombok.Getter;

@Getter
public class MoldTableName {

    String tableName = "mold";

    String idCol = "id";

    String codeCol = "code_mold";

    String serialNumberCol = "serial_number";

    String thicknessCol = "thickness";

    String verticalCol = "vertical";

    String horizontalCol = "horizontal";

    String tonase = "tonase";


}

/**
 * 'id', 'int', 'NO', 'PRI', NULL, 'auto_increment'
 * 'code_mold', 'varchar(100)', 'NO', 'UNI', NULL, ''
 * 'serial_number', 'varchar(100)', 'YES', '', NULL, ''
 * 'thickness', 'double', 'YES', '', '0', ''
 * 'vertical', 'double', 'YES', '', '0', ''
 * 'horizontal', 'double', 'YES', '', '0', ''
 * 'tonase', 'double', 'YES', '', '0', ''
 */
