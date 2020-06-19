package org.oop18.models;

import org.oop18.entities.User;
import org.oop18.exceptions.EntryExistsException;
import org.oop18.exceptions.EntryNotFoundException;
import org.oop18.exceptions.UpdateException;

import java.util.Random;

/**
 * @author - Haribo
 */
public class StubUserAdapter implements UserAdapter {
    @Override
    public User createUser(User user) throws EntryExistsException {
        Random rand = new Random();
        Integer id = rand.nextInt(50);
        id += 1;
        return new User(id, user.getAccount(), user.getPassword());
    }

    @Override
    public User updateUser(User user) throws UpdateException {
        return user;
    }

    @Override
    public User deleteUser(Integer userId) throws EntryNotFoundException {
        return null;
    }

    @Override
    public User queryUser(String account, String password) throws EntryNotFoundException {
        if (account.equals("admin") && password.equals("admin")) {
            return new User(1, "admin", "admin");
        } else {
            throw new EntryNotFoundException("User Not Found");
        }
    }
}
