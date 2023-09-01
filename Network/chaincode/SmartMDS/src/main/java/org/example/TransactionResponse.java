package org.example;

public class TransactionResponse{

    private String transactionId;
    private String timestamp;
    private String value;

    public TransactionResponse(String transactionId, String timestamp, String value) {
        this.transactionId = transactionId;
        this.timestamp = timestamp;
        this.value = value;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getValue() {
        return value;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TransactionResponse{" +
                "transactionId='" + transactionId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
