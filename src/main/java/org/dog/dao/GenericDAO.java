package org.dog.dao;

import java.io.Serializable;

public interface GenericDAO<T, ID extends Serializable> {

    T insert(T t);
    T update(T t);
    void delete(T t);
    void delete(ID id);
    T find(T t);
    T find(ID id);
    Iterable<T> findAll();

}
