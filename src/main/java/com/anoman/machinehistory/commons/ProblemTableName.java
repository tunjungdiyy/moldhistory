package com.anoman.machinehistory.commons;

import lombok.Getter;

@Getter
public class ProblemTableName {

    String tableName = "problem_mold";

    String idCol = "id_problem";

    String idProductMoldCol = "id_product_mold";

    String codeCol = "code_problem_mold";

    String partCol = "part";

    String problemCol = "problem";

    String problemDateCil = "problem_date";

    String descCol = "note";

    String statusCol = "status";

}

/**
 * 'id', 'int', 'NO', 'PRI', NULL, 'auto_increment'
 * 'id_product_mold', 'int', 'NO', 'MUL', NULL, ''
 * 'code_problem_mold', 'varchar(100)', 'NO', 'UNI', NULL, ''
 * 'part', 'varchar(100)', 'NO', '', NULL, ''
 * 'problem', 'text', 'NO', '', NULL, ''
 * 'problem_date', 'mediumtext', 'NO', '', NULL, ''
 * 'description', 'text', 'YES', '', NULL, ''
 * 'status', 'enum(\'SELESAI\',\'DALAM PROSES\',\'BELUM PROSES\')', 'YES', '', NULL, ''
 */
