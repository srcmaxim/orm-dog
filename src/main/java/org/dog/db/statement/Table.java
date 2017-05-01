package org.dog.db.statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Table implements TableDDL, TableDML {

    private static Map<Class, Table> tables = new HashMap<>();


    private List<Column> columns = new ArrayList<>();

    private String schema = "schema";
    private String name = "name";

    public static Table newInstance(Class entity, String schema, String name) {
        tables.remove(entity);
        return new Table(entity, schema, name);
    }

    public static Table getTable(Class entity) {
        return tables.get(entity);
    }

    private Table(Class entity, String schema, String name) {
        tables.put(entity, this);
        this.schema = schema;
        this.name = name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    @Override
    public String create() {
        ArrayList<String> strings = new ArrayList<>();
        for (Column s : columns) {
            strings.add(s.create());
        }
        createFooter(strings);

        String columnsString = strings.stream().collect(Collectors.joining(",\n"));
        return String.format("CREATE TABLE `%s`.`%s` (\n%s)", schema, name, columnsString);
    }

    private void createFooter(ArrayList<String> strings) {
        for (Column s : columns) {
            String footer = s.createFooter();
            if (footer != null) {
                strings.add(footer);
            }
        }
    }

    @Override
    public String drop() {
        return String.format("DROP TABLE `%s`.`%s`", schema, name);
    }

    @Override
    public String selectOne() {
        String columnsString = columns.stream().map(Column::getName).collect(Collectors.joining(", "));
        return String.format("SELECT %s FROM `%s`.`%s` WHERE `id`=?", columnsString, schema, name);
    }

    @Override
    public String select() {
        String columnsString = this.columns.stream().map(Column::getName).collect(Collectors.joining(", "));
        return String.format("SELECT %s FROM `%s`.`%s`", columnsString, schema, name);
    }

    @Override
    public String insert() {
        String columnsString = columns.stream().filter(column -> !column.isPrimaryKey())
                .map(Column::getName).collect(Collectors.joining(", "));
        String q = columns.stream().skip(1).map(column -> "?").collect(Collectors.joining(", "));
        return String.format("INSERT INTO `%s`.`%s` (%s) VALUES (%s)", schema, name, columnsString, q);
    }

    @Override
    public String update() {
        String columnsString = columns.stream().filter(column -> !column.isPrimaryKey())
                .map(column -> column.getName() + "=?").collect(Collectors.joining(", "));
        return String.format("UPDATE `%s`.`%s` SET %s WHERE `id`=?", schema, name, columnsString);
    }

    @Override
    public String deleteOne() {
        return String.format("DELETE FROM `%s`.`%s` WHERE `id`=?", schema, name);
    }
}
