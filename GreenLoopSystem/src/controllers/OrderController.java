package controllers;

import models.*;
import services.*;
import services.Impl.EmailServiceImpl;
import services.Impl.NotificationServiceImpl;
import services.Impl.PropertyServiceImpl;
import views.OrderView;
import views.panels.OrderPanel;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class OrderController {

    private final DatabaseConnectionService databaseConnectionService;
    private final OrderView orderView;
    private final OrderModel orderModel;
    private final OrderService orderService;
    private final PartService partService;
    private final CustomerService customerService;
    private final SupplierService supplierService;
    private final OrderPartService orderPartService;
    private final JobsService jobsService;
    private final JobTypeService jobTypeService;
    private final OrderTypeService orderTypeService;
    private InventoryService inventoryService;
    private PropertyService propertyService;
    private EmailService emailService;
    private NotificationService notificationService;
    private OrderPanel orderPanel;

    public OrderController(DatabaseConnectionService databaseConnectionService, OrderView orderView, OrderModel orderModel, OrderService orderService, PartService partService, CustomerService customerService, SupplierService supplierService, InventoryService inventoryService, OrderPartService orderPartService, JobsService jobsService, JobTypeService jobTypeService, OrderTypeService orderTypeService, OrderPanel orderPanel) {
        this.databaseConnectionService = databaseConnectionService;
        this.orderView = orderView;
        this.orderModel = orderModel;
        this.orderService = orderService;
        this.partService = partService;
        this.customerService = customerService;
        this.supplierService = supplierService;
        this.orderPartService = orderPartService;
        this.jobsService = jobsService;
        this.jobTypeService = jobTypeService;
        this.orderTypeService = orderTypeService;
        this.propertyService = new PropertyServiceImpl(databaseConnectionService);
        this.emailService = new EmailServiceImpl(propertyService);
        this.notificationService = new NotificationServiceImpl(this.databaseConnectionService); //change
        this.orderPanel = orderPanel;

        this.orderView.getSaveBtn().addActionListener(e -> {

            boolean noErrors = true;


            if (orderModel.getCurrentCustomer() == null) {
                JOptionPane.showMessageDialog(null, "Invalid Customer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String orderYear = orderView.getOrderYearField().getText();
            String orderMonth = orderView.getOrderMonthField().getText();
            String orderDay = orderView.getOrderDayField().getText();

            String dateString = orderYear + "-" + orderMonth + "-" + orderDay;


            if (orderYear.isEmpty() || orderMonth.isEmpty() || orderDay.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Invalid Order Date", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (orderModel.getOrder().getTotalPrice() == 0) {
                String message = "Order is empty, add parts or services to continue";
                JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Convert the date string to a java.util.Date object
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date orderDate;
            try {
                orderDate = dateFormat.parse(dateString);
            } catch (Exception e2) {
                e2.printStackTrace();
                JOptionPane.showMessageDialog(null, e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            Order order = orderModel.getOrder();
            order.setOrderDate(orderDate);


            boolean isRepairService = orderView.getIsRepairCheckBox().isSelected();
            boolean isRepaintService = orderView.getIsRepaintCheckBox().isSelected();




            boolean success;
            boolean isNewOrder = (order.getOrderId() == 0);
            int orderId = 0;

            if (isNewOrder) {
                //New Order need to add
                success = orderService.addOrder(order);
            } else {
                //Existing order need to update
                success = orderService.updateOrder(order);
            }

            if (success) {

                orderId = order.getOrderId();

                if(isNewOrder){
                    orderView.getTitleLabel().setText(orderModel.getOrderTitle());
                    orderView.getSaveBtn().setText("Update");
                    orderView.setTitle("Update Order - " + orderModel.getOrderTitle());
                }

                try {
                    List<BillItem> billItems = orderModel.getBillItems();

                    for (int i = 0; i < billItems.size(); i++) {
                        BillItem billItem = billItems.get(i);
                        if(billItem.getOrderPartID()>0){
                            OrderPart orderPart = orderPartService.getOrderPartById(billItem.getOrderPartID());
                            orderPart.setQuantity(billItem.getQuantity());
                            orderPart.setPrice(billItem.getTotalPrice());
                            orderPartService.updateOrderPart(orderPart);
                        }else {
                            OrderPart orderPart = new OrderPart(billItem, order);
                            orderPartService.createOrderPart(orderPart);
                            orderModel.getBillItems().get(i).setOrderPartID(orderPart.getOrderPartID());
                        }

                        if(billItem.getNotSavedQuantity()>0){
                            Inventory inventory = inventoryService.getInventoryByPartId(billItem.getPartId());
                            inventory.setQuantity(inventory.getQuantity()-billItem.getNotSavedQuantity());
                            boolean updated = inventoryService.updateInventory(inventory);
                            if(!updated){
                                JOptionPane.showMessageDialog(null, "Unable to update inventory", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }catch (Exception e2){
                    e2.printStackTrace();
                    noErrors = false;
                }


                if(isRepairService){
                    if(!jobsService.findJobByOrderIdAndJobTypeId(orderId,1)){
                        jobsService.addJobByOrderIdAndJobTypeId(orderId,1, "Repair Service");
                    }
                }else {
                    jobsService.deleteJobByOrderIdAndJobTypeId(orderId, 1);
                }

                if(isRepaintService){
                    if(!jobsService.findJobByOrderIdAndJobTypeId(orderId,2)){
                        jobsService.addJobByOrderIdAndJobTypeId(orderId,2, "Repaint Service");
                    }
                }else{
                    jobsService.deleteJobByOrderIdAndJobTypeId(orderId, 2);
                }

            } else {
                String message = "Unable to " + (isNewOrder?"create new":"update") + " order";
                JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            }


            String cu = isNewOrder ? "Created" : "Updated";
            String postFix = noErrors ? "." : ", With Errors";

            String message = MessageFormat.format("Order {0} successfully{1}", cu, postFix);

            int type = noErrors ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;

            JOptionPane.showMessageDialog(null, message, noErrors ? "Success" : "Error", type);

            this.orderPanel.setOrders(orderService.getAllOrders());
            this.orderPanel.refreshTable();

        });

        this.orderView.getCancelBtn().addActionListener(e -> {
            this.orderView.setVisible(false);
            this.orderView.dispose();
        });

        this.orderView.getCustomerIdField().addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = orderView.getCustomerIdField().getText().trim();
                if (!text.isEmpty()) {
                    int id = convertStringToInt(text);
                    if (id > 0) {
                        Customer customer = customerService.getCustomerById(id);
                        if (customer != null) {
                            setCustomerToView(customer);
                            return;
                        }
                    }
                }
                orderModel.getOrder().setCustomerId(0);
                orderView.getCustomerNameValueLabel().setText("????");
                orderView.getMobileValueLabel().setText("????");
                orderView.getEmailValueLabel().setText("????");
                orderModel.setCurrentCustomer(null);
            }
        });


        this.orderView.getPartIdField().addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = orderView.getPartIdField().getText().trim();
                if (!text.isEmpty()) {
                    int id = convertStringToInt(text);
                    if (id > 0) {
                        Part part = partService.getPartById(id);
                        if (part != null) {
                            orderModel.setCurrentPart(part);
                            orderView.getPartNameValueLabel().setText(part.getName());
                            orderView.getUnitPriceValueLabel().setText(String.format("%.2f", part.getPrice()));
                            return;
                        }
                    }
                }
                orderView.getPartNameValueLabel().setText("????");
                orderView.getUnitPriceValueLabel().setText("????");
                orderModel.setCurrentPart(null);
            }
        });


        this.orderView.getAddPartBtn().addActionListener(e -> {
            if (orderModel.getCurrentPart() == null) {
                JOptionPane.showMessageDialog(null, "No parts found for id : ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String qText = this.orderView.getQuantityField().getText().trim();
            if (qText.length() > 0) {
                int newlyAddedQuantity = convertStringToInt(qText);
                if (newlyAddedQuantity > 0) {


                    int previousIndex = this.orderView.getTableModel().getPreviousIndex(orderModel.getCurrentPart().getPartId());
                    int notSavedQuantity = this.orderView.getTableModel().getNotSavedQuantity(previousIndex);




                    int remainingQuantityInInventory = inventoryService.getQuantityByPartID(orderModel.getCurrentPart().getPartId());
                    Notification notification = this.notificationService.getNotificationByPartId(orderModel.getCurrentPart().getPartId());

                    int minimumQuantity = 0;
                    if(notification!=null){
                        minimumQuantity = notification.getMinQuantity();
                    }
                    if(notification!=null && ((remainingQuantityInInventory - newlyAddedQuantity - notSavedQuantity)< minimumQuantity)){
                        int response = JOptionPane.showConfirmDialog(
                                null,
                                "Available newlyAddedQuantity is lesser than minimum Quantity. Do you want to request supplier for item?",
                                "Minimum Quantity",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                        );

                        // Check the user's response
                        if (response == JOptionPane.OK_OPTION) {
                            sendOrderRequestEmail(orderModel.getCurrentPart().getPartId());
                        }
                    }
                    System.out.println("remainingQuantityInInventory  " + remainingQuantityInInventory);
                    System.out.println("newlyAddedQuantity " + newlyAddedQuantity);
                    System.out.println("notSavedQuantity " + notSavedQuantity);
                    System.out.println("remainingQuantityInInventory < ( newlyAddedQuantity + notSavedQuantity) " + (remainingQuantityInInventory < ( newlyAddedQuantity + notSavedQuantity)));
                    if (remainingQuantityInInventory < ( newlyAddedQuantity + notSavedQuantity)) {
                        String message = MessageFormat.format("Not enough newlyAddedQuantity in inventory,  Available : {0} but  Required : {1}.", remainingQuantityInInventory-notSavedQuantity, newlyAddedQuantity);
                        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    BillItem billItem;
                    if(previousIndex>=0){
                        billItem = this.orderView.getTableModel().getBillItems().get(previousIndex);
                        billItem.setQuantity(billItem.getQuantity() + newlyAddedQuantity);
                        billItem.setNotSavedQuantity(billItem.getNotSavedQuantity() + newlyAddedQuantity);
                        billItem.setTotalPrice(billItem.getQuantity()*billItem.getUnitPrice());
                    }else {
                        billItem = new BillItem(orderModel, newlyAddedQuantity, this.orderView.getTypeComboBox().getSelectedItem().toString());
                    }

                    this.orderView.getTableModel().addBillItem(billItem, previousIndex);//**//
                    this.orderView.getTotalValueLabel().setText(String.format("%.2f", this.orderModel.calcTotal()));
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Value of quantity is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
        });


        this.orderView.getIsRepairCheckBox().addActionListener(e -> {
            boolean checked = orderView.getIsRepairCheckBox().isSelected();
            orderView.getRepairFeeField().setEnabled(checked);
            Order order = OrderController.this.orderModel.getOrder();
            order.setRepair(checked);
            if (checked) {
                double fee = getRepairFeeFromField();
                if (fee > 0) {
                    order.setRepairServiceFee(fee);
                }
            } else {
                order.setRepairServiceFee(0.0);
            }
            order.setTotalPrice(orderModel.calcTotal());
            orderView.getTotalValueLabel().setText(String.format("%.2f", order.getTotalPrice()));
        });

        this.orderView.getIsRepaintCheckBox().addActionListener(e -> {
            boolean checked = orderView.getIsRepaintCheckBox().isSelected();
            orderView.getRepaintFeeField().setEnabled(checked);
            Order order = OrderController.this.orderModel.getOrder();
            order.setRepaint(checked);
            if (checked) {
                double fee = getRepaintFeeFromField();
                if (fee > 0) {
                    order.setRepaintServiceFee(fee);
                }
            } else {
                order.setRepaintServiceFee(0.0);
            }
            order.setTotalPrice(OrderController.this.orderModel.calcTotal());
            orderView.getTotalValueLabel().setText(String.format("%.2f", order.getTotalPrice()));
        });


        this.orderView.getRepairFeeField().addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = orderView.getRepairFeeField().getText().trim();
                if (!text.isEmpty()) {
                    double fee = convertStringToDouble(text);
                    if (fee > 0) {
                        Order order = orderModel.getOrder();
                        order.setRepairServiceFee(fee);
                        order.setTotalPrice(OrderController.this.orderModel.calcTotal());
                        orderView.getTotalValueLabel().setText(String.format("%.2f", order.getTotalPrice()));
                    }
                }
            }
        });

        this.orderView.getRepaintFeeField().addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = orderView.getRepaintFeeField().getText().trim();
                if (!text.isEmpty()) {
                    double fee = convertStringToDouble(text);
                    if (fee > 0) {
                        Order order = orderModel.getOrder();
                        order.setRepaintServiceFee(fee);
                        order.setTotalPrice(OrderController.this.orderModel.calcTotal());
                        orderView.getTotalValueLabel().setText(String.format("%.2f", order.getTotalPrice()));
                    }
                }
            }
        });

        // Add mouse listener to table to show context menu
        orderView.getTable().addMouseListener(new MouseAdapter() {


            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) { // For Windows/Linux
                    showContextMenu(e);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) { // For macOS
                    showContextMenu(e);
                }
            }

            private void showContextMenu(MouseEvent e) {
                int row = orderView.getTable().rowAtPoint(e.getPoint());
                if (row >= 0 && row < orderView.getTable().getRowCount()) {
                    orderView.getTable().setRowSelectionInterval(row, row);
                    orderView.getContextMenu().show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });


        orderView.getDeleteItem().addActionListener(e -> {
            int selectedRow = orderView.getTable().getSelectedRow();
            if (selectedRow != -1) {
                orderView.getTableModel().removeRow(selectedRow);
            }
        });


    }

    private void setCustomerToView(Customer customer) {
        orderModel.setCurrentCustomer(customer);
        orderModel.getOrder().setCustomerId(customer.getCustomerId());
        orderView.getCustomerNameValueLabel().setText(customer.getName());
        orderView.getMobileValueLabel().setText(customer.getMobile());
        orderView.getEmailValueLabel().setText(customer.getEmail());
    }


    public Double getRepairFeeFromField() {
        String text = orderView.getRepairFeeField().getText().trim();
        if (!text.isEmpty()) {
            return convertStringToDouble(text);
        }
        return -1.0;
    }

    public Double getRepaintFeeFromField() {
        String text = orderView.getRepaintFeeField().getText().trim();
        if (!text.isEmpty()) {
            return convertStringToDouble(text);
        }
        return -1.0;
    }


    public int convertStringToInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (Exception ignored) {
        }
        return -1;
    }

    public Double convertStringToDouble(String text) {
        try {
            return Double.parseDouble(text);
        } catch (Exception ignored) {
        }
        return -1.0;
    }

    public void setOrderToView(Order order) {


        List<OrderPart> orderParts = orderPartService.getAllOrderPartsByOrderId(order.getOrderId());
        List<BillItem> billItems = new ArrayList<>();

        int customerID = order.getCustomerId();
        Customer customer = customerService.getCustomerById(customerID);

        setCustomerToView(customer);
        orderView.getCustomerIdField().setText(Integer.toString(customerID));


        Date d = order.getOrderDate();
        orderView.getOrderYearField().setText(Integer.toString(d.getYear() + 1900));
        orderView.getOrderMonthField().setText(Integer.toString(d.getMonth() + 1));
        orderView.getOrderDayField().setText(Integer.toString(d.getDate()));

        Double partsCost = 0.0;
        for (OrderPart orderPart : orderParts) {
            BillItem billItem = new BillItem(orderPart, partService.getPartById(orderPart.getPartId()).getPrice(), orderTypeService.getOrderTypeById(orderPart.getOrderTypeId()).getTypeName());
            billItems.add(billItem);
            partsCost += billItem.getTotalPrice();
        }
        orderView.getTableModel().setBillItems(billItems);
        order.setPartsCost(partsCost);
        orderModel.setOrder(order);
        orderModel.setBillItems(billItems);
        orderView.getTotalValueLabel().setText(String.format("%.2f", order.getTotalPrice()));
        orderView.getIsRepairCheckBox().setSelected(order.isRepair());
        orderView.getIsRepaintCheckBox().setSelected(order.isRepaint());
        orderView.getRepairFeeField().setText(String.format("%.2f", order.getRepairServiceFee()));
        orderView.getRepaintFeeField().setText(String.format("%.2f", order.getRepairServiceFee()));
        orderView.getRepairFeeField().setEnabled(order.isRepair());
        orderView.getRepaintFeeField().setEnabled(order.isRepaint());

    }


    public void sendOrderRequestEmail(int partId) {
        Part part = partService.getPartById(partId);
        if(part==null){
            JOptionPane.showMessageDialog(null, "Part not found with id : " + partId, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Supplier supplier = supplierService.getSupplierById(part.getSupplierId());
        if(supplier==null){
            JOptionPane.showMessageDialog(null, "Supplier not found with id : " + part.getSupplierId(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(supplier.getContactEmail()==null){
            JOptionPane.showMessageDialog(null, "Supplier don't have an email.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String subject = "Order request : " + part.getName();
            String message = "Please contact our purchase department regarding order of " + part.getName();
            emailService.sendEmail(supplier.getContactEmail(), subject, message);
            JOptionPane.showMessageDialog(null, "Order request email sent.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Unable to send order request email.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
