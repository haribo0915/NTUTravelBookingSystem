package org.oop18.models;

import org.oop18.entities.User;
import org.oop18.exceptions.CreateException;
import org.oop18.exceptions.DeleteException;
import org.oop18.exceptions.QueryException;
import org.oop18.exceptions.UpdateException;


public interface UserAdapter {
    User createUser(User user) throws CreateException;
    User updateUser(User user) throws UpdateException;
    User deleteUser(Integer userId) throws DeleteException;
    User queryUser(String account, String password) throws QueryException;
}
