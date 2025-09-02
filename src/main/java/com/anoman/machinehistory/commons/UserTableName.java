package com.anoman.machinehistory.commons;

import lombok.Getter;

@Getter
public class UserTableName {

    String tableName = "users";
    String idColumn = "id";
    String uniqueCodeColumn = "unique_code_user";
    String usernameColumn = "username";
    String passwordColumn = "password";

}

/**
 * 'id', 'int', 'NO', 'PRI', NULL, 'auto_increment'
 * 'unique_code_user', 'varchar(100)', 'NO', 'UNI', NULL, ''
 * 'username', 'varchar(100)', 'NO', 'UNI', NULL, ''
 * 'password', 'varchar(100)', 'NO', '', NULL, ''
 */