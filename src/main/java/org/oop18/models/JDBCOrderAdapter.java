package org.oop18.models;

import org.oop18.entities.Order;
import org.oop18.exceptions.CreateException;
import org.oop18.exceptions.DeleteException;
import org.oop18.exceptions.QueryException;
import org.oop18.exceptions.UpdateException;

import java.util.List;


public class JDBCOrderAdapter implements OrderAdapter {
    @Override
    public Order createOrder(Order order) throws CreateException {
        return null;
    }

    @Override
    public Order updateOrder(Order order) throws UpdateException {
        return null;
    }

    @Override
    public Order deleteOrder(Order order) throws DeleteException {
        return null;
    }

    @Override
    public List<Order> queryOrders(Integer userId) throws QueryException {
        return null;
    }
}
