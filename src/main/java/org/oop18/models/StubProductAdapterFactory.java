package org.oop18.models;

/**
 * Factory used to create StubProductAdapter dynamically
 *
 * @author - Haribo
 */
public class StubProductAdapterFactory implements ProductAdapterFactory {
    @Override
    public ProductAdapter create() {
        return new StubProductAdapter();
    }
}
