package org.oop18.models;

import org.oop18.entities.Product;
import org.oop18.entities.TravelCode;
import org.oop18.exceptions.QueryException;

import java.sql.Timestamp;
import java.util.List;


public interface ProductAdapter {
    Product queryProduct(Integer id) throws QueryException;
    List<Product> queryProducts() throws QueryException;
    List<Product> queryProducts(TravelCode travelCode) throws QueryException;
    List<Product> queryProducts(Timestamp startDate) throws QueryException;
    List<Product> queryProducts(TravelCode travelCode, Timestamp startDate) throws QueryException;
    List<TravelCode> queryTravelCodes() throws QueryException;
    TravelCode queryTravelCode(String travelCodeName) throws QueryException;

}
