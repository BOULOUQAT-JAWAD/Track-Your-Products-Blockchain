package org.hyperledger;

import java.util.Objects;

public class Product {

    private final String id;
    private final String name;
    private final float quantity;

    public Product(final String id, final String name, final float quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getQuantity() {
        return quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),getName(),getQuantity());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [id=" + id + ", name="
                + name + ", quantity=" + quantity + "]";
    }
}