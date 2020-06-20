package org.oop18.models;

/**
 * Factory used to create StubUserAdapter dynamically
 *
 * @author - Haribo
 */
public class StubUserAdapterFactory implements UserAdapterFactory {
    @Override
    public UserAdapter create() {
        return new StubUserAdapter();
    }
}
