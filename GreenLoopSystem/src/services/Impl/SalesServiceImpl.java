package services.Impl;

import models.Sale;
import services.DatabaseConnectionService;
import services.SalesService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class SalesServiceImpl implements SalesService {

    private DatabaseConnectionService connectionService;

    public SalesServiceImpl(DatabaseConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public List<Sale> getSalesByRange(Date fromDate, Date toDate) {
        List<Sale> sales = new ArrayList<>();
        String query = "SELECT sum(repair_service_fee) as total_repair_service_fee, sum(repair_service_fee) as total_repair_service_fee\n" +
                "FROM greenloop.orders\n" +
                "WHERE order_date BETWEEN ? AND ?";


        try (Connection conn = connectionService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, fromDate);
            stmt.setDate(2, toDate);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                Sale repairSale = new Sale();
                repairSale.setService(true);
                repairSale.setServiceName("Total Repair Service Fee");
                repairSale.setUnitPrice(rs.getDouble("total_repair_service_fee"));
                repairSale.setTotalSales(rs.getDouble("total_repair_service_fee"));
                sales.add(repairSale);

                Sale repaintSale = new Sale();
                repaintSale.setService(true);
                repaintSale.setServiceName("Total Repaint Service Fee");
                repaintSale.setUnitPrice(rs.getDouble("total_repair_service_fee"));
                repaintSale.setTotalSales(rs.getDouble("total_repair_service_fee"));
                sales.add(repaintSale);

            }



        } catch (Exception e) {
            e.printStackTrace();
        }



        query = "SELECT p.part_id,\n" +
                "       p.name,\n" +
                "       p.price          AS unit_price,\n" +
                "       SUM(op.quantity) AS total_quantity,\n" +
                "       SUM(op.price)    AS total_sales\n" +
                "FROM parts p\n" +
                "         LEFT JOIN (SELECT *\n" +
                "                    FROM order_part\n" +
                "                    WHERE sales_date BETWEEN ? AND ?) op ON p.part_id = op.part_id\n" +
                "GROUP BY p.part_id\n" +
                "ORDER BY total_sales DESC;";
        try (Connection conn = connectionService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, fromDate);
            stmt.setDate(2, toDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Sale sale = new Sale();
                sale.setService(false);
                sale.setPartId(rs.getInt("part_id"));
                sale.setPartName(rs.getString("name"));
                sale.setUnitPrice(rs.getDouble("unit_price"));
                sale.setTotalSales(rs.getDouble("total_sales"));
                sale.setQuantity(rs.getInt("total_quantity"));
                sales.add(sale);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }





        return sales;
    }
}
