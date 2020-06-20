package org.oop18.models;

/**
 * Factory used to create JDBCProductAdapter dynamically
 *
 * @author - Haribo
 */
public class JDBCProductAdapterFactory implements ProductAdapterFactory {
    @Override
    public ProductAdapter create() {
        return new JDBCProductAdapter();
    }
}
