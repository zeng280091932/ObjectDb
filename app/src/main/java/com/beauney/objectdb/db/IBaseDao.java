package com.beauney.objectdb.db;

/**
 * @author zengjiantao
 * @since 2020-07-31
 */
public interface IBaseDao<T> {
    long insert(T entity);

    int update(T entity, T where);

    int delete(T where);
}
