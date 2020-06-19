package org.oop18.models;

import org.oop18.entities.Order;
import org.oop18.exceptions.UpdateException;
import org.oop18.exceptions.EntryExistsException;
import org.oop18.exceptions.EntryNotFoundException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author - Haribo
 */
public class StubOrderAdapter implements OrderAdapter {
    @Override
    public Order createOrder(Order order) throws EntryExistsException {
        Random rand = new Random();
        Integer randomId = rand.nextInt(50);
        randomId += 1;
        return new Order(randomId, randomId, randomId, 5, 3, 10000, new Timestamp(System.currentTimeMillis()));
    }

    @Override
    public Order updateOrder(Order order) throws UpdateException {
        return order;
    }

    @Override
    public Order deleteOrder(Order order) throws EntryNotFoundException {
        return order;
    }

    @Override
    public List<Order> queryOrders(Integer userId) throws EntryNotFoundException {
        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Random rand = new Random();
            Integer randomId = rand.nextInt(50);
            randomId += 1;
            Order order = new Order(randomId, userId, randomId, 5, 3, 12345, new Timestamp(System.currentTimeMillis()));
            orderList.add(order);
        }
        return orderList;
    }
}
