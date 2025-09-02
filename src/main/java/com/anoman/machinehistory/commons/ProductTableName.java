package com.anoman.machinehistory.commons;

import lombok.Getter;

@Getter
public class ProductTableName {

    String tableName = "product";

    String idColumn = "id";

    String codeProdukColumn = "code_product";

    String nameColumn = "name";

    String descriptionColumn = "description";
}

/**
 * 'id', 'int', 'NO', 'PRI', NULL, 'auto_increment'
 * 'code_product', 'varchar(100)', 'NO', 'UNI', NULL, ''
 * 'name', 'varchar(100)', 'NO', 'UNI', NULL, ''
 * 'description', 'text', 'YES', '', NULL, ''
 */
