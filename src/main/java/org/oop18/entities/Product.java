package org.oop18.entities;

import java.sql.Timestamp;

/**
 * @author - Haribo
 */
public class Product {
    private Integer id;
    private String title;
    private Integer travelCode;
    private String productKey;
    private Integer price;
    private Timestamp startDate;
    private Timestamp endDate;
    private Integer lowerBound;
    private Integer upperBound;

    public Product() {
    }

    public Product(Integer id, Integer travelCode, String title, String productKey, Integer price, Timestamp startDate, Timestamp endDate, Integer lowerBound, Integer upperBound) {
        this.id = id;
        this.travelCode = travelCode;
        this.title = title;
        this.productKey = productKey;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTravelCode() {
        return travelCode;
    }

    public void setTravelCode(Integer travelCode) {
        this.travelCode = travelCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public Integer getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(Integer lowerBound) {
        this.lowerBound = lowerBound;
    }

    public Integer getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(Integer upperBound) {
        this.upperBound = upperBound;
    }
}
