package org.oop18.models;

import org.oop18.entities.Product;
import org.oop18.entities.TravelCode;
import org.oop18.exceptions.EntryNotFoundException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JDBCProductAdapter implements ProductAdapter {

    private Connection con = null;
    private Statement st = null;
    private ResultSet rs = null;
    
    /**
     * Establish MySQL database connection when constructed
     */
    public JDBCProductAdapter() {
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
    //retrieve data by product id
    public Product queryProduct(Integer id) throws EntryNotFoundException {
        try {
            String query = String.format("SELECT * from product WHERE id=%d",id);
            rs = st.executeQuery(query);
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    		if (rs.next() == false) {
    			throw new EntryNotFoundException("Product not found!");
    		}
            Integer product_id = rs.getInt("id");
           	Integer travel_code_id=rs.getInt("travel_code_id");
           	String title =rs.getString("title");
           	String product_key=rs.getString("product_key");
           	Integer price=rs.getInt("price");
			Timestamp start = Timestamp.valueOf(sdf.format(rs.getTimestamp("start_date")));
			Timestamp end = Timestamp.valueOf(sdf.format(rs.getTimestamp("end_date")));
           	Integer lower_bound=rs.getInt("lower_bound");
           	Integer upper_bound=rs.getInt("upper_bound");

           	return new Product(product_id, travel_code_id,title,product_key,price,start,end, lower_bound,upper_bound);         
        }catch (Exception ex) {
    		System.out.println(ex.getMessage());
    		return new Product();
    	}
    }


    //TODO: travelCode and startDate may be null, please select all the products first and filter them according to given travelCode and startDate,
    //      you can take StubProductAdapter as reference.
    @Override
    public List<Product> queryProducts(TravelCode travelCode, Timestamp startDate) throws EntryNotFoundException {
    	List<Product> productList = new ArrayList<>();
    	DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        try {
            String query = String.format("SELECT * from product");
            rs = st.executeQuery(query);
    		if (rs.next() == false) {
    			throw new EntryNotFoundException("Product not found!");
    		} 
    		do {
    			Integer product_id = rs.getInt("id");
    			Integer travel_code_id=rs.getInt("travel_code_id");
    			String title =rs.getString("title");
    			String product_key=rs.getString("product_key");
    			Integer price=rs.getInt("price");
    			Timestamp start = Timestamp.valueOf(sdf.format(rs.getTimestamp("start_date")));
    			Timestamp end = Timestamp.valueOf(sdf.format(rs.getTimestamp("end_date")));
    			Integer lower_bound=rs.getInt("lower_bound");
    			Integer upper_bound=rs.getInt("upper_bound");       
    			productList.add(new Product(product_id, travel_code_id,title,product_key,price,start,end, lower_bound,upper_bound));
    		}
    		while(rs.next());
            if (travelCode != null) {
                productList = productList.stream()
                                    .filter((Product product) -> (product.getTravelCodeId().equals(travelCode.getId())))
                                    .collect(Collectors.toList());
            }
            if (startDate != null) {
                productList = productList.stream()
                                    .filter((Product product) -> (product.getStartDate().equals(startDate)))
                                    .collect(Collectors.toList());
            }
            return productList;
        }catch (Exception ex) {
    		System.out.println(ex.getMessage());
    		return productList;
    	}    	
    }

    @Override
    public List<TravelCode> queryTravelCodes() throws EntryNotFoundException {
    	List<TravelCode> travelCodeList = new ArrayList<>();
        try{
            String query = String.format("SELECT * from travel_code");
            rs = st.executeQuery(query);
    		if (rs.next() == false) {
    			throw new EntryNotFoundException("Product not found!");
    		}  
    		do {
    			Integer travel_code_id=rs.getInt("id");
    			Integer travel_code =rs.getInt("travel_code");
    			String travel_code_name=rs.getString("travel_code_name");
           	
    			travelCodeList.add(new TravelCode(travel_code_id, travel_code, travel_code_name));
    		}
    		while(rs.next());
    		
    		return travelCodeList;	 
        }catch (Exception ex) {
    		System.out.println(ex.getMessage());
    		return travelCodeList;
    	} 
    }

    @Override
    public TravelCode queryTravelCode(String travelCodeName) throws EntryNotFoundException {
        try{
            String query = String.format("SELECT * from travel_code where travel_code_name=\"%s\"",travelCodeName);
            rs = st.executeQuery(query);
    		if (rs.next() == false) {
    			throw new EntryNotFoundException("Product not found!");
    		}
    		Integer travel_code_id=rs.getInt("id");
    		Integer travel_code =rs.getInt("travel_code");
    		String travel_code_name=rs.getString("travel_code_name");
           	
    		return(new TravelCode(travel_code_id, travel_code, travel_code_name));
    		
        }catch (Exception ex) {
    		System.out.println(ex.getMessage());
    		return new TravelCode();
    	}
    }
}
