package prototype;

/**
 * PROTOTYPE interface: an object that is able to create a copy of itself.
 */
public interface Prototype<T> {
    T copy();
}
