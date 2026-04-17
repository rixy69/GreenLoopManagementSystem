package services;

import models.Sale;

import java.sql.Date;
import java.util.List;

public interface SalesService {
    List<Sale> getSalesByRange(Date fromDate, Date toDate);
}
