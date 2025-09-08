package com.anoman.machinehistory.commons;

import lombok.Getter;

@Getter
public class ProductMoldTableName {

    String tableName = "product_mold";

    String idCol = "id";

    String idMoldCol = "id_mold";

    String idProductCol = "id_product";

    String codeCol = "code_product_mold";

    String cvtCol = "cvt";

    String ctCol = "ct";

    String grammageCol = "grammage";

    String descriptionCol = "description";

}

/**
 * 'id', 'int', 'NO', 'PRI', NULL, 'auto_increment'
 * 'id_mold', 'int', 'NO', 'MUL', NULL, ''
 * 'id_product', 'int', 'NO', 'MUL', NULL, ''
 * 'code_product_mold', 'varchar(100)', 'NO', 'UNI', NULL, ''
 * 'cvt', 'int', 'YES', '', '0', ''
 * 'ct', 'double', 'YES', '', '0', ''
 * 'grammage', 'double', 'YES', '', '0', ''
 * 'description', 'text', 'YES', '', NULL, ''
 */
