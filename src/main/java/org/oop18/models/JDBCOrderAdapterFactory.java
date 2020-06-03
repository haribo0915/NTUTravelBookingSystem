package org.oop18.models;

/**
 * @author - Haribo
 */
public class JDBCOrderAdapterFactory implements OrderAdapterFactory {
    @Override
    public OrderAdapter create() {
        return new JDBCOrderAdapter();
    }
}
