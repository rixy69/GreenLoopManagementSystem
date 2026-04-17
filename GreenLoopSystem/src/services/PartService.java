package services;

import models.Part;

import java.util.List;

public interface PartService {
    boolean addPart(Part part);
    Part getPartById(int partId);
    List<Part> getAllParts();
    boolean updatePart(Part part);
    boolean deletePart(int partId);
    Part getPartWithRemainingQuantityById(int partId);
}
