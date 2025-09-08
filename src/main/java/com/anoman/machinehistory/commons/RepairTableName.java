package com.anoman.machinehistory.commons;

import lombok.Getter;

@Getter
public class RepairTableName {

    String tableName = "repair_mold";

    String idCol = "id_repair";

    String idProblemCol = "id_problem_mold";

    String codeCol = "code_repair";

    String actionCol = "action";

    String reapirStartCol = "repair_start_date";

    String repairEndCol = "repair_end_date";

    String teamCol = "team_repair";

    String noteCol = "note_repair";

}

/**
 * 'id_repair', 'int', 'NO', 'PRI', NULL, 'auto_increment'
 * 'id_problem_mold', 'int', 'NO', 'MUL', NULL, ''
 * 'code_repair', 'varchar(100)', 'NO', 'UNI', NULL, ''
 * 'action', 'text', 'NO', '', NULL, ''
 * 'repair_start_date', 'mediumtext', 'NO', '', NULL, ''
 * 'repair_end_date', 'mediumtext', 'NO', '', NULL, ''
 * 'team_repair', 'varchar(100)', 'NO', '', NULL, ''
 * 'note_repair', 'text', 'YES', '', NULL, ''
 */
