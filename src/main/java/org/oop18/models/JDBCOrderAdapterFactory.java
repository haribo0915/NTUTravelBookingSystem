package org.oop18.models;

/**
 * Factory used to create JDBCOrderAdapter dynamically
 *
 * @author - Haribo
 */
public class JDBCOrderAdapterFactory implements OrderAdapterFactory {
    @Override
    public OrderAdapter create() {
        return new JDBCOrderAdapter();
    }
}
