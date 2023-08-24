package org.example;

import java.time.Instant;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.List;
import java.util.Objects;

@DataType()
public class Package {

    @Property()
    private final String id;
    @Property()
    private final List<Product> products;
    @Property()
    private final String from;
    @Property()
    private final String to;
    @Property()
    private final String current_location;
    @Property()
    private final Boolean delivered = false;
    @Property()
    private Instant timestamp; // Using Instant for timestamp

    public Package(@JsonProperty("id") final String id,
                   @JsonProperty("products") final List<Product> products,
                   @JsonProperty("from") final String from,
                   @JsonProperty("to") final String to,
                   @JsonProperty("current_location") final String current_location) {
        this.id = id;
        this.products = products;
        this.from = from;
        this.to = to;
        this.current_location = current_location;
        this.timestamp = Instant.now();
    }

    public String getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getCurrent_location() {
        return current_location;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
    @Override
    public int hashCode() {
        return Objects.hash(getId(),getProducts(),getFrom(),getTo(),getCurrent_location(),getDelivered(),getTimestamp());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Product product : products) {
            sb.append(product).append("\n");
        }
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [id=" + id + ", From="
                + from + ", to=" + to + ", current_location=" + current_location + ", delivered=" + delivered +
                ", timestamp=" + timestamp + "]"
                + "Products: {" + sb + " }";
    }
}
