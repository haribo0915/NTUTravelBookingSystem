package org.oop18.models;

import org.oop18.entities.Order;
import org.oop18.exceptions.CreateException;
import org.oop18.exceptions.DeleteException;
import org.oop18.exceptions.QueryException;
import org.oop18.exceptions.UpdateException;

import java.util.List;


public interface OrderAdapter {
    Order createOrder(Integer userId, Integer productId, Integer adultCount, Integer childrenCount) throws CreateException;
    Order updateOrder(Integer orderId, Integer adultCount, Integer childrenCount) throws UpdateException;
    Order deleteOrder(Integer orderId) throws DeleteException;
    List<Order> queryOrders(Integer userId) throws QueryException;
}
