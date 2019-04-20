package com.example.ratio.Entities;

public class Income extends Entity {
    private String parent;
    private String description;
    private String amount;
    private boolean attachments;
    private String timestamp;

    public Income() {
    }

    public Income(String parent, String description, String amount, boolean attachments, String timestamp) {
        this.parent = parent;
        this.description = description;
        this.amount = amount;
        this.attachments = attachments;
        this.timestamp = timestamp;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isAttachments() {
        return attachments;
    }

    public void setAttachments(boolean attachments) {
        this.attachments = attachments;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
