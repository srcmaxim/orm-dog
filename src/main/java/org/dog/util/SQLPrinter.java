package org.dog.util;

import java.sql.PreparedStatement;

public final class SQLPrinter {

    private SQLPrinter() {
    }

    public static String printSqlStatement(PreparedStatement ps) {
        String sql = ps.toString();
        sql = sql.substring(sql.indexOf(": ") + 2);
        System.out.println(sql);
        return sql;
    }
}
