package org.oop18.models;

import org.oop18.entities.Order;
import org.oop18.entities.Product;
import org.oop18.entities.TravelCode;
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
import java.util.List;


public class JDBCProductAdapter implements ProductAdapter {

    private Connection con = null;
    private Statement st = null;
    private ResultSet rs = null;
    
    /**
     * Establish MySQL database connection when constructed
     */
    public JDBCProductAdapter() throws DBConnectException {
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
    //retrieve data by product id?
    public Product queryProduct(Integer id) throws QueryException {
        try {
            String query = String.format("SELECT * from product WHERE id=%d",id);
            rs = st.executeQuery(query);
    		if (rs.next() == false) {
    			throw new QueryException("Product not found!");
    		}
            Integer product_id = rs.getInt("id");
           	Integer travel_code_id=rs.getInt("travel_code_id");
           	String product_key=rs.getString("product_key");
           	Integer price=rs.getInt("price");
           	Timestamp start = rs.getTimestamp("start_date");
           	Timestamp end = rs.getTimestamp("end_date");
           	Integer lower_bound=rs.getInt("lower_bound");
           	Integer upper_bound=rs.getInt("upper_bound");
           	String title =rs.getString("title");
           	return new Product(product_id, travel_code_id,title,product_key,price,start,end, lower_bound,upper_bound);         
        }catch (Exception ex) {
    		throw new QueryException(ex.getMessage());
    	}
    }


    //TODO: travelCode and startDate may be null, please select all the products first and filter them according to given travelCode and startDate,
    //      you can take StubProductAdapter as reference.
    @Override
    public List<Product> queryProducts(TravelCode travelCode, Timestamp startDate) throws QueryException {
        return null;
    }

    @Override
    public List<TravelCode> queryTravelCodes() throws QueryException {
        return null;
    }

    @Override
    public TravelCode queryTravelCode(String travelCodeName) throws QueryException {
        return null;
    }
}
