package org.oop18.models;

import org.oop18.entities.User;
import org.oop18.exceptions.*;



public interface UserAdapter {
    User createUser(User user) throws EntryExistsException;
    User updateUser(User user) throws UpdateException;
    User deleteUser(Integer userId) throws EntryNotFoundException;
    User queryUser(String account, String password) throws EntryNotFoundException;
}
