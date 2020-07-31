package com.beauney.objectdb.db;

/**
 * @author zengjiantao
 * @since 2020-07-31
 */
public class UserDao<T> extends BaseDao<T> {

    @Override
    protected String createTable() {
        return "create table if not exists t_user (t_username varchar,t_password varchar)";
    }
}
