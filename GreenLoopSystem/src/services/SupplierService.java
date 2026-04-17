package services;

import models.Supplier;

import java.util.List;

public interface SupplierService {
    boolean addSupplier(Supplier supplier);
    Supplier getSupplierById(int supplierId);
    List<Supplier> getAllSuppliers();
    boolean updateSupplier(Supplier supplier);
    boolean deleteSupplier(int supplierId);
}
