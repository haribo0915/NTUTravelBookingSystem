package org.oop18.models;

import org.oop18.entities.Product;
import org.oop18.entities.TravelCode;
import org.oop18.exceptions.QueryException;

import java.sql.Timestamp;
import java.util.List;


public class JDBCProductAdapter implements ProductAdapter {

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
    public Product queryProduct(Integer id) throws QueryException {
        return null;
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
