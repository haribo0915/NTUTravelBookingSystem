package org.oop18.models;

/**
 * Factory used to create StubOrderAdapter dynamically
 *
 * @author - Haribo
 */
public class StubOrderAdapterFactory implements OrderAdapterFactory {
    @Override
    public OrderAdapter create() {
        return new StubOrderAdapter();
    }
}
