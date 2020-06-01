package org.oop18.models;

import org.oop18.entities.User;
import org.oop18.exceptions.CreateException;
import org.oop18.exceptions.DeleteException;
import org.oop18.exceptions.QueryException;
import org.oop18.exceptions.UpdateException;

public class JDBCUserAdapter implements UserAdapter {
    @Override
    public User createUser(User user) throws CreateException {
        return null;
    }

    @Override
    public User updateUser(User user) throws UpdateException {
        return null;
    }

    @Override
    public User deleteUser(Integer userId) throws DeleteException {
        return null;
    }

    @Override
    public User queryUser(String account, String password) throws QueryException {
        return null;
    }
}
