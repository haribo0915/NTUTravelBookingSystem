package org.oop18.models;

import org.oop18.entities.Product;
import org.oop18.entities.TravelCode;
import org.oop18.exceptions.EntryNotFoundException;

import java.sql.Timestamp;
import java.util.List;


public interface ProductAdapter {
    Product queryProduct(Integer id) throws EntryNotFoundException;
    List<Product> queryProducts(TravelCode travelCode, Timestamp startDate) throws EntryNotFoundException;
    List<TravelCode> queryTravelCodes() throws EntryNotFoundException;
    TravelCode queryTravelCode(String travelCodeName) throws EntryNotFoundException;

}
