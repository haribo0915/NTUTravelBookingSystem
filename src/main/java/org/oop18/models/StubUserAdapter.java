package org.oop18.models;

import org.oop18.entities.User;
import org.oop18.exceptions.CreateException;
import org.oop18.exceptions.DeleteException;
import org.oop18.exceptions.QueryException;
import org.oop18.exceptions.UpdateException;

import java.util.Random;

/**
 * @author - Haribo
 */
public class StubUserAdapter implements UserAdapter {
    @Override
    public User createUser(User user) throws CreateException {
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
    public User deleteUser(Integer userId) throws DeleteException {
        return null;
    }

    @Override
    public User queryUser(String account, String password) throws QueryException {
        if (account.equals("admin") && password.equals("admin")) {
            return new User(1, "admin", "admin");
        } else {
            throw new QueryException("User Not Found");
        }
    }
}
