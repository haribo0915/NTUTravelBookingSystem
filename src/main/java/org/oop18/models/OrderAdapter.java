package org.oop18.models;

import org.oop18.entities.Order;
import org.oop18.exceptions.CreateException;
import org.oop18.exceptions.EntryNotFoundException;
import org.oop18.exceptions.UpdateException;

import java.util.List;


public interface OrderAdapter {
    Order createOrder(Order order) throws CreateException;
    Order updateOrder(Order order) throws UpdateException, EntryNotFoundException;
    Order deleteOrder(Order order) throws EntryNotFoundException;
    List<Order> queryOrders(Integer userId) throws EntryNotFoundException;
}
