package com.anoman.machinehistory.commons;

import lombok.Getter;

@Getter
public class SquenceTableName {

    String tableName = "squence";
    String idColumn = "id";
    String entityColumn = "entity";
    String preficColumn = "prefix";
    String lastNumberColumn = "last_number";
    String periodeColumn = "periode";
    String descriptionColumn = "description";
}

/**
 * 'id', 'int', 'NO', 'PRI', NULL, 'auto_increment'
 * 'entity', 'varchar(50)', 'NO', 'UNI', NULL, ''
 * 'prefix', 'varchar(10)', 'NO', 'UNI', NULL, ''
 * 'last_number', 'int', 'NO', '', NULL, ''
 * 'periode', 'mediumtext', 'YES', '', NULL, ''
 * 'description', 'text', 'YES', '', NULL, ''
 */
