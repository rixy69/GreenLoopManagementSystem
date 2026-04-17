package services;

import models.Inventory;
import java.util.List;

public interface InventoryService {
    boolean addInventory(Inventory inventory);
    Inventory getInventoryById(int inventoryId);
    Inventory getInventoryByPartId(int partID);
    List<Inventory> getAllInventories();
    boolean updateInventory(Inventory inventory);
    boolean deleteInventory(int inventoryId);
    int getQuantityByPartID(int partID);
}
