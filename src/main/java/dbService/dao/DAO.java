package dbService.dao;

import dbService.entity.DataBaseObject;

import java.util.List;

public interface DAO<T extends DataBaseObject> extends AutoCloseable {
    void insert(T entity);
    T getById(int id);
    List<T> getAll();
    void update(T entity);
    void deleteById(int id);
}