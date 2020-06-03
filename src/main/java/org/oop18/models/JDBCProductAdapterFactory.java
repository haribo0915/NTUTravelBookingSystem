package org.oop18.models;

/**
 * @author - Haribo
 */
public class JDBCProductAdapterFactory implements ProductAdapterFactory {
    @Override
    public ProductAdapter create() {
        return new JDBCProductAdapter();
    }
}
