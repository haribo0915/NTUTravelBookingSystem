package org.oop18.models;

/**
 * @author - Haribo
 */
public class StubProductAdapterFactory implements ProductAdapterFactory {
    @Override
    public ProductAdapter create() {
        return new StubProductAdapter();
    }
}
