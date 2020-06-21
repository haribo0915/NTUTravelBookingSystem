package org.oop18.models;

import org.oop18.entities.Product;
import org.oop18.entities.TravelCode;
import org.oop18.exceptions.EntryNotFoundException;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JDBCProductAdapter implements ProductAdapter {
    private JDBCConnectionPool jdbcConnectionPool;

    public JDBCProductAdapter() {
        jdbcConnectionPool = JDBCConnectionPool.getInstance();
    }

    @Override
    //retrieve data by product id
    public Product queryProduct(Integer id) throws EntryNotFoundException {
        Integer productId = null;
        Integer travelCode=null;
        String title =null;
        String productKey=null;
        Integer price=null;
        Timestamp start = null;
        Timestamp end = null;
        Integer lowerBound=null;
        Integer upperBound=null;

        Connection conn = jdbcConnectionPool.takeOut();

        try {
            String query = String.format("SELECT * from product WHERE id=%d",id);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            if (rs.next() == false) {
                throw new EntryNotFoundException("Product not found!");
            }
            productId = rs.getInt("id");
            travelCode=rs.getInt("travel_code");
            title =rs.getString("title");
            productKey=rs.getString("product_key");
            price=rs.getInt("price");
            start = Timestamp.valueOf(sdf.format(rs.getTimestamp("start_date")));
            end = Timestamp.valueOf(sdf.format(rs.getTimestamp("end_date")));
            lowerBound=rs.getInt("lower_bound");
            upperBound=rs.getInt("upper_bound");


        }catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        finally {
            jdbcConnectionPool.takeIn(conn);
        }
        return new Product(productId, travelCode,title,productKey,price,start,end, lowerBound,upperBound);
    }

    @Override
    //retrieve data by travel code and start date
    public List<Product> queryProducts(TravelCode travelCode, Timestamp startDate) throws EntryNotFoundException {
        List<Product> productList = new ArrayList<>();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

        Connection conn = jdbcConnectionPool.takeOut();

        try {
            String query = String.format("SELECT * from product");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            //retrieve all product 
            if (rs.next() == false) {
                throw new EntryNotFoundException("Product not found!");
            }
            do {
                Integer productId = rs.getInt("id");
                Integer travelCodeForProduct=rs.getInt("travel_code");
                String title =rs.getString("title");
                String productKey=rs.getString("product_key");
                Integer price=rs.getInt("price");
                Timestamp start = Timestamp.valueOf(sdf.format(rs.getTimestamp("start_date")));
                Timestamp end = Timestamp.valueOf(sdf.format(rs.getTimestamp("end_date")));
                Integer lowerBound=rs.getInt("lower_bound");
                Integer upperBound=rs.getInt("upper_bound");
                productList.add(new Product(productId, travelCodeForProduct,title,productKey,price,start,end, lowerBound,upperBound));
            }
            while(rs.next());
            //filter out entries with expired start date
            productList = productList.stream()
                    .filter((Product product) -> (product.getStartDate().after(new Timestamp(System.currentTimeMillis()))))
                    .collect(Collectors.toList());
            //filter out entries according to start date and travel code
            if (travelCode != null) {
                productList = productList.stream()
                                    .filter((Product product) -> (product.getTravelCode().equals(travelCode.getTravelCode())))
                                    .collect(Collectors.toList());
            }
            if (startDate != null) {
                productList = productList.stream()
                                    .filter((Product product) -> (product.getStartDate().equals(startDate)))
                                    .collect(Collectors.toList());
            }
        }catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        finally {
            jdbcConnectionPool.takeIn(conn);
        }
        return productList;
    }

    @Override
    //retrieve all travel code
    public List<TravelCode> queryTravelCodes() throws EntryNotFoundException {
        List<TravelCode> travelCodeList = new ArrayList<>();

        Connection conn = jdbcConnectionPool.takeOut();

        try{
            String query = String.format("SELECT * from travel_code");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next() == false) {
                throw new EntryNotFoundException("Product not found!");
            }
            do {
                Integer travelCode =rs.getInt("travel_code");
                String travelCodeName=rs.getString("travel_code_name");

                travelCodeList.add(new TravelCode(travelCode, travelCodeName));
            }
            while(rs.next());

        }catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        finally {
            jdbcConnectionPool.takeIn(conn);
        }
        return travelCodeList;
    }

    @Override
    //retrieve travel code by its name
    public TravelCode queryTravelCode(String travelCodeName) throws EntryNotFoundException {
        Integer travelCode =null;
        String travelCodeNameForPara=null;

        Connection conn = jdbcConnectionPool.takeOut();

        try{
            String query = String.format("SELECT * from travel_code where travel_code_name=\"%s\"",travelCodeName);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next() == false) {
                throw new EntryNotFoundException("Product not found!");
            }
            travelCode =rs.getInt("travel_code");
            travelCodeNameForPara=rs.getString("travel_code_name");

        }catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        finally {
            jdbcConnectionPool.takeIn(conn);
        }
        return(new TravelCode(travelCode, travelCodeNameForPara));
    }
}
