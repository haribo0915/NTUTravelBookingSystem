package org.oop18.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.oop18.entities.User;
import org.oop18.exceptions.EntryExistsException;
import org.oop18.exceptions.EntryNotFoundException;
import org.oop18.exceptions.UpdateException;

public class JDBCUserAdapter implements UserAdapter {
	private JDBCConnectionPool jdbcConnectionPool;

	public JDBCUserAdapter() {
		jdbcConnectionPool = JDBCConnectionPool.getInstance();
	}

    private Statement st = null;
    private ResultSet rs = null;
	
    @Override
    /**
     * Method to create a user
     * @param user
     * @return user
     */
    public User createUser(User user) throws EntryExistsException {	
    	try {
    		// Query account and password from DB
    		String query = String.format("SELECT * FROM User WHERE account = \"%s\" and password = \"%s\"", user.getAccount(), user.getPassword()); 		
    		rs = st.executeQuery(query);
    		
    		// insert account into DB if not exist
    		if (rs.next() == false) {
				query = String.format("INSERT INTO User (account, password) VALUES (\"%s\", \"%s\")", user.getAccount(), user.getPassword());
				st.executeUpdate(query);
				query = String.format("SELECT * FROM User WHERE account = \"%s\" and password = \"%s\"", user.getAccount(), user.getPassword());
				rs = st.executeQuery(query);
			
	    		rs.next();
	            Integer id = rs.getInt("id");
	            String Qaccount = rs.getString("account");
	            String Qpassword = rs.getString("password");
	            return new User(id, Qaccount, Qpassword);
    		}
    		else {
    			throw new EntryExistsException("Account already exists!");
    		}
    	}
    	catch(Exception ex) {
    		System.out.println(ex.getMessage());
    		return new User();
    	}
    }

    @Override
    /**
     * Method to update user info
     * @param user
     * @return user
     */
    public User updateUser(User user) throws UpdateException {
        return user;
    }

    @Override
    /**
     * Method to delete user
     * @param userId
     * @return User
     */
    public User deleteUser(Integer userId) throws EntryNotFoundException {
		try {
			// Query user id from DB
    		String query = String.format("SELECT * FROM User WHERE id = %d", userId);
    		rs = st.executeQuery(query);
            if (rs.next() == false){
                throw new EntryNotFoundException("User not found");
            }

        	Integer id = rs.getInt("id");
            String Qaccount = rs.getString("account");
            String Qpassword = rs.getString("password");
            
            // Delete user form DB
			query = String.format("DELETE FROM User WHERE id = %d", userId);
			st.executeUpdate(query);
			
			return new User(id, Qaccount, Qpassword);
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
			return new User();
		}
    }

    @Override
    /**
     * Method to query user
     * @param account, password
     * @return user
     */
    public User queryUser(String account, String password) throws EntryNotFoundException {
    	try {
    		// Query account and password from DB
    		String query = String.format("SELECT * FROM User WHERE account = \"%s\" and password = \"%s\"", account, password); 		
    		rs = st.executeQuery(query);
    		
    		// Throw exception if no matched, else return the result
    		if (rs.next() == false) {
    			throw new EntryNotFoundException("No matched account. Perhaps a wrong account or an incorrect password?");
    		}
    		else {
            	Integer id = rs.getInt("id");
                String Qaccount = rs.getString("account");
                String Qpassword = rs.getString("password");
                return new User(id, Qaccount, Qpassword);
    		}
    	}
    	catch(Exception ex) {
    		System.out.println(ex.getMessage());
    		return new User();
    	}
    }
}
