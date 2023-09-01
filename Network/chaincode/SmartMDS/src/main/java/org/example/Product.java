package org.example;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public class Product {

    @Property()
    private final String id;
    @Property()
    private final String name;
    @Property()
    private final float quantity;

    public Product(@JsonProperty("id") final String id,
                   @JsonProperty("name") final String name,
                   @JsonProperty("quantity") final float quantity) {
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