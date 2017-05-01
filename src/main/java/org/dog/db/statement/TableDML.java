package org.dog.db.statement;

public interface TableDML {

    String selectOne();
    String select();
    String insert();
    String update();
    String deleteOne();

}
