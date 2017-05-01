package org.dog.db.statement;

import org.dog.util.Data;

public class Column {
    private String name = "name";
    private String dbType = "dbType";
    private String JavaType = "JavaType";
    private boolean autoIncremment = false;
    private boolean notNull = false;

    private boolean primaryKey = false;
    private boolean unique = false;

    public Column(String name, String JavaType, boolean autoIncremment, boolean notNull, boolean primaryKey, boolean unique) {
        this.name = name;
        this.dbType = Data.toSQLType(JavaType);
        this.JavaType = JavaType;
        this.autoIncremment = autoIncremment;
        this.notNull = notNull;
        this.primaryKey = primaryKey;
        this.unique = unique;
    }

    boolean isPrimaryKey() {
        return primaryKey;
    }

    String getName() {
        return name;
    }


    String create() {
        StringBuilder sb = new StringBuilder(
                String.format("`%s` %s", name, dbType));
        if (notNull) {
            sb = sb.append(" NOT NULL");
        }
        if (autoIncremment) {
            sb = sb.append(" AUTO_INCREMENT");
        }
        return sb.toString();
    }

    String createFooter() {
        if (primaryKey && unique) {
            return String.format("PRIMARY KEY (`%s`),\n" +
                    "UNIQUE INDEX `%s_UNIQUE` (`%s` ASC)", name, name, name);
        }
        if (primaryKey) {
            return String.format("PRIMARY KEY (`%s`)", name);
        }
        if (unique) {
            return String.format("UNIQUE INDEX `%s_UNIQUE` (`%s` ASC)", name, name);
        }
        return null;
    }

}


