package org.oop18.models;

import org.oop18.entities.Order;
import org.oop18.exceptions.CreateException;
import org.oop18.exceptions.EntryNotFoundException;
import org.oop18.exceptions.UpdateException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public JDBCOrderAdapter() {
        try {
            //Class 的靜態 forName() 方法實現動態加載類別
            Class.forName("com.mysql.jdbc.Driver");
            //3306|MySQL開放此端口
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ntu_travel_booking_system","root","root");
            st = con.createStatement();   
        } 
        catch(Exception ex) {
        	System.out.println(ex.getMessage());
        }
    }
	
    @Override
    /**
    * Method to create an order
    * @param order
    * @return order
    */
    public Order createOrder(Order order) throws CreateException {
        /*
            order.getId()null
            order.getTotalPrice() : calculated
        */
        try{
            // calculate and check # of people
            rs = st.executeQuery( String.format("SELECT * from product WHERE id=%d", order.getProductId()));
            rs.next();
            if( rs.getInt("id") != order.getProductId() ) 
                throw new CreateException("Invalid Product ID");
            Integer min_people = rs.getInt("lower_bound");
            Integer max_people = rs.getInt("upper_bound");
            Timestamp start_date = rs.getTimestamp("start_date");

            Integer sum = 0;
            rs = st.executeQuery( String.format("SELECT SUM(adult_count) FROM `order` WHERE product_id=%d", order.getProductId()));
            rs.next(); sum += rs.getInt("SUM(adult_count)");
            rs = st.executeQuery( String.format("SELECT SUM(children_count) FROM `order` WHERE product_id=%d", order.getProductId()));
            rs.next(); sum += rs.getInt("SUM(children_count)");

            // non-negative
            if( order.getAdultCount() < 0 )
                throw new CreateException("Number of Adult needs to be non-negative.");
            if( order.getChildrenCount() < 0 )
                throw new CreateException("Number of Children needs to be non-negative.");
            // lower / upper bound
            //if( (sum+order.getAdultCount()+order.getChildrenCount()) < min_people )
               // throw new CreateException( String.format("This trip requires at least %d people.\nAn order of %d people is placed.\nCurrently registered: %d.", min_people, order.getAdultCount()+order.getChildrenCount(), sum));
            if( (sum+order.getAdultCount()+order.getChildrenCount()) > max_people )
                throw new CreateException( String.format("This trip handles at most %d people.\nAn order of %d people is placed.\nCurrently registered: %d.", max_people, order.getAdultCount()+order.getChildrenCount(), sum));

            // cannot make for incoming 10 day
            long oneDay = 1 * 24 * 60 * 60 * 1000;
            Timestamp deadline = new Timestamp(start_date.getTime() - 10*oneDay);
            if( (order.getCreatedTime()).after(deadline) )
                throw new CreateException("Cannot place orders for incoming 10 days.");
            
            // create order id : append to `order` table
            rs = st.executeQuery("SELECT COUNT(*) FROM `order`");
            rs.next();
            order.setId(rs.getInt("COUNT(*)")+1); // after last one

            //add to database
            st.executeUpdate( String.format("INSERT INTO `order` VALUES (\"%d\", \"%s\", \"%s\", \"%d\", \"%d\", \"%d\", \"%s\")", 
                order.getId(), order.getUserId(), order.getProductId(), 
                order.getAdultCount(), order.getChildrenCount(), order.getTotalPrice(), order.getCreatedTime() ));        

            return order;
        }
        catch(Exception e){ throw new CreateException(e.getMessage());}
    }

    @Override
    /**
    * Method to update guest number
    * @param order
    * @return order
    */
    public Order updateOrder(Order order) throws UpdateException, EntryNotFoundException {
		Integer id = null;
		Integer userId = null;
		Integer productId = null;
		Integer adultCount = null;
		Integer childrenCount = null;
		Integer totalPrice = null;
		Timestamp createdTime = null;
    	
    	try {
    		// Query with order ID
    		String query = String.format("SELECT * FROM `order` WHERE `id` = \'%d\'", order.getId());
    		rs = st.executeQuery(query);
    		if (rs.next() == false) {
    			throw new EntryNotFoundException("Order not found!");
    		}
    		
    		Integer originalAdultCount = rs.getInt("adult_count");
    		Integer originalChildrenCount = rs.getInt("children_count");
    		
    		//Query with product ID
			query = String.format("SELECT * FROM `product` WHERE `id` = \'%d\'", order.getProductId());
			rs = st.executeQuery(query);
			
    		if (rs.next() == false) {
    			throw new EntryNotFoundException("Product not found!");
    		}
			//Check days between today and departure date
			LocalDateTime departure = rs.getTimestamp("start_date").toLocalDateTime();
			LocalDateTime today = LocalDateTime.now();
			if (ChronoUnit.DAYS.between(today, departure) < 10 ) {
				throw new UpdateException("You can only modify orders 10 days before departure!");
			}
			
            // calculate and check # of people
            if( rs.getInt("id") != order.getProductId() ) 
                throw new UpdateException("Invalid Product ID");
            
            Integer max_people = rs.getInt("upper_bound");

            Integer sum = 0;
            rs = st.executeQuery( String.format("SELECT SUM(adult_count) FROM `order` WHERE product_id=%d", order.getProductId()));
            rs.next(); sum += rs.getInt("SUM(adult_count)");
            rs = st.executeQuery( String.format("SELECT SUM(children_count) FROM `order` WHERE product_id=%d", order.getProductId()));
            rs.next(); sum += rs.getInt("SUM(children_count)");

            // non-negative
            if( order.getAdultCount() < 0 )
                throw new UpdateException("Number of Adult needs to be non-negative.");
            if( order.getChildrenCount() < 0 )
                throw new UpdateException("Number of Children needs to be non-negative.");
            
            // upper bound
            if( (sum+order.getAdultCount()+order.getChildrenCount()-originalAdultCount-originalChildrenCount) > max_people )
                throw new UpdateException( String.format("This trip handles at most %d people.\nAn order of %d people is placed.\nCurrently registered: %d.", max_people, order.getAdultCount()+order.getChildrenCount(), sum));
			
			//update order
			query = String.format("UPDATE `order` SET `adult_count` = \'%d\', `children_count` = \'%d\' WHERE (`id` = \'%d\')", order.getAdultCount(), order.getChildrenCount(), order.getId());
			st.executeUpdate(query);
			
			
			query = String.format("SELECT * FROM `order` WHERE `id` = \'%d\'", order.getId());
			rs = st.executeQuery(query);
    		if (rs.next() == false) {
    			throw new EntryNotFoundException("Order not found!");
    		}
			
    		id = rs.getInt("id");
    		userId = rs.getInt("user_id");
    		productId = rs.getInt("product_id");
    		adultCount = rs.getInt("adult_count");
    		childrenCount = rs.getInt("children_count");
    		totalPrice = rs.getInt("total_price");
    		createdTime = rs.getTimestamp("created_time");
    	}
    	catch (SQLException ex) {
    		System.out.println(ex.getMessage());
    	}
    	
    	return new Order(id, userId, productId, adultCount, childrenCount, totalPrice, createdTime);
    }

    @Override
    /**
    * Method to delete an order
    * @param order
    * @return order
    */
    public Order deleteOrder(Order order) throws EntryNotFoundException {
		Integer id = null;
		Integer userId = null;
		Integer productId = null;
		Integer adultCount = null;
		Integer childrenCount = null;
		Integer totalPrice = null;
		Timestamp createdTime = null;	
		
    	try {
            // Query by order id
    		String query = String.format("SELECT * FROM `order` WHERE `id` = \'%d\'", order.getId());
    		rs = st.executeQuery(query);
    		
    		if (rs.next() == false) {
    			throw new EntryNotFoundException("Order not found");
    		}
    		
    		id = rs.getInt("id");
    		userId = rs.getInt("user_id");
    		productId = rs.getInt("product_id");
    		adultCount = rs.getInt("adult_count");
    		childrenCount = rs.getInt("children_count");
    		totalPrice = rs.getInt("total_price");
    		createdTime = rs.getTimestamp("created_time");	
    		
    		//Query with product ID
			query = String.format("SELECT * FROM `product` WHERE `id` = \'%d\'", order.getProductId());
			rs = st.executeQuery(query);
			
    		if (rs.next() == false) {
    			throw new EntryNotFoundException("Product not found!");
    		}
    		
			//Check days between today and departure date
			LocalDateTime departure = rs.getTimestamp("start_date").toLocalDateTime();
			LocalDateTime today = LocalDateTime.now();
			if (ChronoUnit.DAYS.between(today, departure) < 10 ) {
				throw new UpdateException("You can only delete orders 10 days before departure!");
			}	
    		
            // Delete the order
    		query = String.format("DELETE FROM `order` WHERE `id` = \'%d\'", order.getId());
    		st.executeUpdate(query);
    	}
    	catch (SQLException ex) {
    		System.out.println(ex.getMessage());
    	}
    	return new Order(id, userId, productId, adultCount, childrenCount, totalPrice, createdTime);
    }

    @Override
    /**
    * Method to query orders
    * @param order
    * @return order
    */
    public List<Order> queryOrders(Integer userId) throws EntryNotFoundException {
    	
    	List<Order> queryResult = new ArrayList<>();
    	
    	try {
            // Query order by user id
    		String query = String.format("SELECT * FROM `order` WHERE `user_id` = \'%d\'", userId);
    		rs = st.executeQuery(query);
    		
    		if (rs.next() == false) {
    			throw new EntryNotFoundException("You have not ordered anything yet!");
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
    	}
    	catch (Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    	return queryResult;	
    }
}
