package org.dog.util;

public final class Data {

    private Data() {
    }

    public static String toSQLType(String javaType) {

        switch (javaType) {
            case "byte":
            case "short":
            case "int":
            case "long":
            case "Byte":
            case "Short":
            case "Integer":
            case "Long":
                return "INT";

            case "char":
            case "Character":
                return "CHAR(1)";
            case "String":
                return "VARCHAR(45)";

            case "float":
            case "double":
            case "Float":
            case "Double":
                return "DOUBLE";

            case "boolean":
            case "Boolean":
                return "BIT";

            case "Data":
                return "DATA";
            case "Time":
                return "TIME";

            case "Blob":
                return "BLOB";
            case "Clob":
                return "CLOB";
        }

        throw new RuntimeException("No such SQL Type: " + javaType);
    }

}
