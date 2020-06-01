package org.oop18.models;

import org.oop18.entities.Order;
import org.oop18.exceptions.CreateException;
import org.oop18.exceptions.DeleteException;
import org.oop18.exceptions.QueryException;
import org.oop18.exceptions.UpdateException;

import java.util.List;


public class JDBCOrderAdapter implements OrderAdapter {
    @Override
    public Order createOrder(Integer userId, Integer productId, Integer adultCount, Integer childrenCount) throws CreateException {
        return null;
    }

    @Override
    public Order updateOrder(Integer orderId, Integer adultCount, Integer childrenCount) throws UpdateException {
        return null;
    }

    @Override
    public Order deleteOrder(Integer orderId) throws DeleteException {
        return null;
    }

    @Override
    public List<Order> queryOrders(Integer userId) throws QueryException {
        return null;
    }
}
