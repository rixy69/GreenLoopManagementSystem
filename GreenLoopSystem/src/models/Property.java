package models;

public class Property {
    private String propertyKey;
    private String value;
    private String type;

    public Property() {
    }

    public Property(String propertyKey, String value, String type) {
        this.propertyKey = propertyKey;
        this.value = value;
        this.type = type;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Property{" +
                "propertyKey='" + propertyKey + '\'' +
                ", value='" + value + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
