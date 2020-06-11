package org.oop18.models;

import org.oop18.entities.Order;
import org.oop18.exceptions.CreateException;
import org.oop18.exceptions.DBConnectException;
import org.oop18.exceptions.DeleteException;
import org.oop18.exceptions.QueryException;
import org.oop18.exceptions.UpdateException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class JDBCOrderAdapter implements OrderAdapter {
    private Connection con = null;
    private Statement st = null;
    private ResultSet rs = null;
    
    /**
     * Establish MySQL database connection when constructed
     */
    public JDBCOrderAdapter() throws DBConnectException {
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
    * Method to create an order
    * @param order
    * @return order
    */
    public Order createOrder(Order order) throws CreateException {
        return null;
    }

    @Override
    /**
    * Method to update guest number
    * @param order
    * @return order
    */
    public Order updateOrder(Order order) throws UpdateException {
    	try {
    		// Query with order ID
    		String query = String.format("SELECT * FROM `order` WHERE `id` = \'%d\'", order.getId());
    		rs = st.executeQuery(query);
    		if (rs.next() == false) {
    			throw new UpdateException("Order not found!");
    		}
    		
    		//Query with product ID
			query = String.format("SELECT * FROM `product` WHERE `id` = \'%d\'", order.getProductId());
			rs = st.executeQuery(query);
			
    		if (rs.next() == false) {
    			throw new UpdateException("Product not found!");
    		}
			//Check days between today and departure date
			LocalDateTime departure = rs.getTimestamp("start_date").toLocalDateTime();
			LocalDateTime today = LocalDateTime.now();
			if (ChronoUnit.DAYS.between(today, departure) < 10 ) {
				throw new UpdateException("You can only modify orders 10 days before departure!");
			}
			
			//update order
			query = String.format("UPDATE `order` SET `adult_count` = \'%d\', `children_count` = \'%d\' WHERE (`id` = \'%d\')", order.getAdultCount(), order.getChildrenCount(), order.getId());
			st.executeUpdate(query);
			
			
			query = String.format("SELECT * FROM `order` WHERE `id` = \'%d\'", order.getId());
			rs = st.executeQuery(query);
    		if (rs.next() == false) {
    			throw new UpdateException("Order not found!");
    		}
			
    		Integer id = rs.getInt("id");
    		Integer userId = rs.getInt("user_id");
    		Integer productId = rs.getInt("product_id");
    		Integer adultCount = rs.getInt("adult_count");
    		Integer childrenCount = rs.getInt("children_count");
    		Integer totalPrice = rs.getInt("total_price");
    		Timestamp createdTime = rs.getTimestamp("created_time");
    		
    		return new Order(id, userId, productId, adultCount, childrenCount, totalPrice, createdTime);
    	}
    	catch (Exception ex) {
    		throw new UpdateException(ex.getMessage());
    	}
    }

    @Override
    /**
    * Method to delete an order
    * @param order
    * @return order
    */
    public Order deleteOrder(Order order) throws DeleteException {
    	try {
            // Query by order id
    		String query = String.format("SELECT * FROM `order` WHERE `id` = \'%d\'", order.getId());
    		rs = st.executeQuery(query);
    		
    		if (rs.next() == false) {
    			throw new DeleteException("Order not found");
    		}
    		
    		Integer id = rs.getInt("id");
    		Integer userId = rs.getInt("user_id");
    		Integer productId = rs.getInt("product_id");
    		Integer adultCount = rs.getInt("adult_count");
    		Integer childrenCount = rs.getInt("children_count");
    		Integer totalPrice = rs.getInt("total_price");
    		Timestamp createdTime = rs.getTimestamp("created_time");		
    		
            // Delete the order
    		query = String.format("DELETE FROM `order` WHERE `id` = \'%d\'", order.getId());
    		st.executeUpdate(query);

    		return new Order(id, userId, productId, adultCount, childrenCount, totalPrice, createdTime);
    	}
    	catch (Exception ex) {
    		throw new DeleteException(ex.getMessage());
    	}
    }

    @Override
    /**
    * Method to query orders
    * @param order
    * @return order
    */
    public List<Order> queryOrders(Integer userId) throws QueryException {
    	
    	List<Order> queryResult = new ArrayList<>();
    	
    	try {
            // Query order by user id
    		String query = String.format("SELECT * FROM `order` WHERE `user_id` = \'%d\'", userId);
    		rs = st.executeQuery(query);
    		
    		if (rs.next() == false) {
    			throw new QueryException("You have not ordered anything yet!");
    		}
    		
            // Append all orders made by user
    		do {
	    		Integer id = rs.getInt("id");
	    		Integer QuserId = rs.getInt("user_id");
	    		Integer productId = rs.getInt("product_id");
	    		Integer adultCount = rs.getInt("adult_count");
	    		Integer childrenCount = rs.getInt("children_count");
	    		Integer totalPrice = rs.getInt("total_price");
	    		Timestamp createdTime = rs.getTimestamp("created_time");	
	    		
	    		queryResult.add(new Order(id, QuserId, productId, adultCount, childrenCount, totalPrice, createdTime));
	    		
    		} while (rs.next());
    		
    		return queryResult;	 
    	}
    	catch (Exception ex) {
    		throw new QueryException(ex.getMessage());
    	}
    }
}
