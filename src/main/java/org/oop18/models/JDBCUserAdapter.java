package org.oop18.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.oop18.entities.User;
import org.oop18.exceptions.CreateException;
import org.oop18.exceptions.DBConnectException;
import org.oop18.exceptions.DeleteException;
import org.oop18.exceptions.QueryException;
import org.oop18.exceptions.UpdateException;

public class JDBCUserAdapter implements UserAdapter {
	
    private Connection con = null;
    private Statement st = null;
    private ResultSet rs = null;
    
    /**
     * Establish MySQL database connection when constructed
     */
    public JDBCUserAdapter() throws DBConnectException {
        try {
            //Class 的靜態 forName() 方法實現動態加載類別
            Class.forName("com.mysql.jdbc.Driver");
            //3306|MySQL開放此端口
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ntu_travel_booking_system","root","root");
            st = con.createStatement();   
        } 
        catch(Exception ex) {
        	throw new DBConnectException(ex.getMessage());
        }
    }
	
    @Override
    /**
     * Method to create a user
     * @param user
     * @return user
     */
    public User createUser(User user) throws CreateException {	
    	try {
    		// Query account and password from DB
    		String query = String.format("SELECT * FROM User WHERE username = \"%s\" and password = \"%s\"", user.getAccount(), user.getPassword()); 		
    		rs = st.executeQuery(query);
    		
    		// insert account into DB if not exist
    		if (rs.next() == false) {
				query = String.format("INSERT INTO User (account, password) VALUES (\"%s\", \"%s\")", user.getAccount(), user.getPassword());
				st.executeUpdate(query);
				query = String.format("SELECT * FROM User WHERE username = \"%s\" and password = \"%s\"", user.getAccount(), user.getPassword());
				rs = st.executeQuery(query);
			
	    		rs.next();
	            Integer id = rs.getInt("id");
	            String Qaccount = rs.getString("account");
	            String Qpassword = rs.getString("password");
	            return new User(id, Qaccount, Qpassword);
    		}
    		else {
    			throw new CreateException("Account already exists!");
    		}
    	}
    	catch(Exception ex) {
    		throw new CreateException(ex.getMessage());
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
    public User deleteUser(Integer userId) throws DeleteException {
		try {
			// Query user id from DB
    		String query = String.format("SELECT * FROM User WHERE id = %d", userId);
    		rs = st.executeQuery(query);
        	Integer id = rs.getInt("id");
            String Qaccount = rs.getString("account");
            String Qpassword = rs.getString("password");
            
            // Delete user form DB
			query = String.format("DELETE FROM User WHERE id = %d", userId);
			st.executeUpdate(query);
			
			return new User(id, Qaccount, Qpassword);
		}
		catch(Exception ex) {
			throw new DeleteException(ex.getMessage());
		}
    }

    @Override
    /**
     * Method to query user
     * @param account, password
     * @return user
     */
    public User queryUser(String account, String password) throws QueryException {
    	try {
    		// Query account and password from DB
    		String query = String.format("SELECT * FROM User WHERE username = \"%s\" and password = \"%s\"", account, password); 		
    		rs = st.executeQuery(query);
    		
    		// Throw exception if no matched, else return the result
    		if (rs.next() == false) {
    			throw new QueryException("No matched account. Perhaps a wrong account or an incorrect password?");
    		}
    		else {
            	Integer id = rs.getInt("id");
                String Qaccount = rs.getString("account");
                String Qpassword = rs.getString("password");
                return new User(id, Qaccount, Qpassword);
    		}
    	}
    	catch(Exception ex) {
    		throw new QueryException(ex.getMessage());
    	}
    }
}
