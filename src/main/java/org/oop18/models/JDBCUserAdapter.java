package org.oop18.models;

import org.oop18.entities.User;
import org.oop18.exceptions.EntryExistsException;
import org.oop18.exceptions.EntryNotFoundException;
import org.oop18.exceptions.UpdateException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUserAdapter implements UserAdapter {
	private JDBCConnectionPool jdbcConnectionPool;

	public JDBCUserAdapter() {
		jdbcConnectionPool = JDBCConnectionPool.getInstance();
	}

    @Override
    /**
     * Method to create a user
     * @param user
     * @return user
     */
    public User createUser(User user) throws EntryExistsException {	
    	
        Integer id = null;
        String Qaccount = null;
        String Qpassword = null;

        Connection conn = jdbcConnectionPool.takeOut();
    	
    	try {
    		// Query account and password from DB
    		String query = String.format("SELECT * FROM User WHERE account = \"%s\" and password = \"%s\"", user.getAccount(), user.getPassword());
    		Statement st = conn.createStatement();
    		ResultSet rs = st.executeQuery(query);
    		
    		// insert account into DB if not exist
    		if (rs.next() == false) {
				query = String.format("INSERT INTO User (account, password) VALUES (\"%s\", \"%s\")", user.getAccount(), user.getPassword());
				st.executeUpdate(query);
				query = String.format("SELECT * FROM User WHERE account = \"%s\" and password = \"%s\"", user.getAccount(), user.getPassword());
				rs = st.executeQuery(query);
			
	    		rs.next();
	            id = rs.getInt("id");
	            Qaccount = rs.getString("account");
	            Qpassword = rs.getString("password");
    		}
    		else {
                //throw exception if account exists
    			throw new EntryExistsException("Account already exists!");
    		}
    	}
    	catch(SQLException ex) {
    		System.out.println(ex.getMessage());
    	}
    	finally {
    		jdbcConnectionPool.takeIn(conn);
		}
		return new User(id, Qaccount, Qpassword);
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
    	Integer id = null;
        String Qaccount = null;
        String Qpassword = null;

		Connection conn = jdbcConnectionPool.takeOut();
        
    	try {
			// Query user id from DB
    		String query = String.format("SELECT * FROM User WHERE id = %d", userId);
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

            if (rs.next() == false){
                // throw exception if no user if found
                throw new EntryNotFoundException("User not found");
            }

        	id = rs.getInt("id");
            Qaccount = rs.getString("account");
            Qpassword = rs.getString("password");
            
            // Delete user form DB
			query = String.format("DELETE FROM User WHERE id = %d", userId);
			st.executeUpdate(query);
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
		}
		finally {
			jdbcConnectionPool.takeIn(conn);
		}
		return new User(id, Qaccount, Qpassword);
    }

    @Override
    /**
     * Method to query user
     * @param account, password
     * @return user
     */
    public User queryUser(String account, String password) throws EntryNotFoundException {
    	Integer id = null;
        String Qaccount = null;
        String Qpassword = null;

		Connection conn = jdbcConnectionPool.takeOut();

    	try {
    		// Query account and password from DB
    		String query = String.format("SELECT * FROM User WHERE account = \"%s\" and password = \"%s\"", account, password);
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
    		
    		// Throw exception if no matched, else return the result
    		if (rs.next() == false) {
    			throw new EntryNotFoundException("No matched account. Perhaps a wrong account or an incorrect password?");
    		}
    		else {
            	id = rs.getInt("id");
                Qaccount = rs.getString("account");
                Qpassword = rs.getString("password");
    		}
			return new User(id, Qaccount, Qpassword);
    	}
    	catch(SQLException ex) {
    		System.out.println(ex.getMessage());
    	}
		finally {
			jdbcConnectionPool.takeIn(conn);
		}
		return new User(id, Qaccount, Qpassword);
    }
}
