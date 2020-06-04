package org.oop18.models;

/**
 * @author - Haribo
 */
public class StubOrderAdapterFactory implements OrderAdapterFactory {
    @Override
    public OrderAdapter create() {
        return new StubOrderAdapter();
    }
}
