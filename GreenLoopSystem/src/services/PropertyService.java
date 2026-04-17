package services;

import models.Property;

import java.util.List;

public interface PropertyService {
    boolean addProperty(Property property);
    Property getPropertyByKey(String propertyKey);
    List<Property> getAllProperties();
    boolean updateProperty(Property property);
    boolean deleteProperty(String propertyKey);
    List<Property> getAllPropertiesByType(String type);
    Property getPropertyByKeyAndType(String propertyKey, String type);
    boolean addOrUpdateProperties(List<Property> properties);

}
