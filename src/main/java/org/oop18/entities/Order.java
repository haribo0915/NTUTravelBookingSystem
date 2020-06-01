package org.oop18.entities;

import java.sql.Timestamp;

/**
 * @author - Haribo
 */
public class Order {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer adultCount;
    private Integer childrenCount;
    private Integer totalPrice;
    private Timestamp createdTime;

    public Order() {
    }

    public Order(Integer id, Integer userId, Integer productId, Integer adultCount, Integer childrenCount, Integer totalPrice, Timestamp createdTime) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.adultCount = adultCount;
        this.childrenCount = childrenCount;
        this.totalPrice = totalPrice;
        this.createdTime = createdTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(Integer adultCount) {
        this.adultCount = adultCount;
    }

    public Integer getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(Integer childrenCount) {
        this.childrenCount = childrenCount;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }
}
