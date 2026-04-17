package views;

import controllers.OrderController;
import models.OrderModel;
import models.BillItemTableModel;
import models.OrderType;
import services.CS;
import views.FooterPanel;

import javax.swing.*;
import java.awt.*;

public class OrderView extends JFrame {
    private OrderModel orderModel;
    private OrderController orderController;
    private JLabel titleLabel;
    private BillItemTableModel tableModel;
    private JTable table;
    private JTextField partIdField;
    private JLabel partNameValueLabel;
    private JLabel unitPriceValueLabel;
    private JButton addPartBtn;
    private JTextField quantityField;
    private JComboBox typeComboBox;

    private JTextField customerIdField;
    private JTextField orderYearField;
    private JTextField orderMonthField;
    private JTextField orderDayField;
    private JComboBox statusComboBox;
    private JCheckBox isRepairCheckBox;
    private JTextField repairFeeField;
    private JCheckBox isRepaintCheckBox;
    private JTextField repaintFeeField;
    private JLabel customerNameValueLabel;
    private JLabel mobileLabel;
    private JLabel mobileValueLabel;
    private JLabel emailLabel;
    private JLabel emailValueLabel;
    private JLabel totalValueLabel;
    private JButton saveBtn;
    private JButton cancelBtn;
    private JPopupMenu contextMenu;
    private JMenuItem deleteItem;

    public OrderView(OrderModel orderModel, String title) {
        this.orderModel = orderModel;
        this.setTitle(title);

        this.createUIComponents();
        FooterPanel footerLabel = CS.paintFooter((JPanel) getContentPane());
        footerLabel.setBounds(0, this.getHeight() - 20, this.getWidth(), 20);
    }

    private void createUIComponents() {
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBounds(40, 40, 1200, 750);

        titleLabel = CS.paintLabels(this, "", orderModel.getOrderTitle(), "#148c34", "#ffffff", 0, true);
        titleLabel.setBounds(40, 20, 100, 25);

        JLabel customerIDLabel = CS.paintLabels(this, "", "Customer ID", null, null, 2, true);
        customerIDLabel.setBounds(60, 90, 100, 25);

        JLabel customerNameLabel = CS.paintLabels(this, "", "Customer Name", null, null, 2, true);
        customerNameLabel.setBounds(60, 170, 100, 20);

        customerNameValueLabel = CS.paintLabels(this, "", "????", "#888888", "#ffffff", 0, true);
        customerNameValueLabel.setBounds(170, 170, 160, 20);

        mobileLabel = new JLabel("Mobile");
        mobileLabel.setBounds(360, 130, 100, 20);
        this.add(mobileLabel);

        mobileValueLabel = CS.paintLabels(this, "", "????", "#888888", "#ffffff", 0, true);
        mobileValueLabel.setBounds(430, 130, 160, 20);
        this.add(mobileValueLabel);

        emailLabel = new JLabel("Email");
        emailLabel.setBounds(360, 170, 100, 20);
        this.add(emailLabel);

        emailValueLabel = CS.paintLabels(this, "", "????", "#888888", "#ffffff", 0, true);
        emailValueLabel.setBounds(430, 170, 160, 20);
        this.add(emailValueLabel);


        customerIdField = new JTextField();
        customerIdField.setBounds(170, 90, 160, 25);
        this.add(customerIdField);

        JLabel orderDateLabel = CS.paintLabels(this, "", "Order Date", null, null, 2, true);
        orderDateLabel.setBounds(60, 130, 100, 25);

        orderYearField = new JTextField(orderModel.getYMD()[0]);
        orderMonthField = new JTextField(orderModel.getYMD()[1]);
        orderDayField = new JTextField(orderModel.getYMD()[2]);

        orderYearField.setBounds(170, 130, 50, 25);
        orderYearField.setHorizontalAlignment(0);

        orderMonthField.setBounds(225, 130, 50, 25);
        orderMonthField.setHorizontalAlignment(0);

        orderDayField.setBounds(280, 130, 50, 25);
        orderDayField.setHorizontalAlignment(0);
        this.add(orderYearField);
        this.add(orderMonthField);
        this.add(orderDayField);

        isRepairCheckBox = new JCheckBox();
        isRepairCheckBox.setBounds(650, 90, 20, 20);
        this.add(isRepairCheckBox);

        JLabel isRepairLabel = CS.paintLabels(this, "", "Is Repair Service Required", "#888888", "#FFFFFF", 0, true);
        isRepairLabel.setBounds(680, 90, 180, 20);
        this.add(isRepairLabel);

        JLabel repairServiceFeeLabel = CS.paintLabels(this, "", "Repair Fee", "#888888", "#FFFFFF", 0, true);
        repairServiceFeeLabel.setBounds(680, 140, 80, 20);
        this.add(repairServiceFeeLabel);

        repairFeeField = new JTextField();
        repairFeeField.setBounds(770, 140, 90, 20);
        repairFeeField.setEnabled(false);
        this.add(repairFeeField);

        isRepaintCheckBox = new JCheckBox();
        isRepaintCheckBox.setBounds(910, 90, 20, 20);
        this.add(isRepaintCheckBox);

        JLabel isRepaintLabel = CS.paintLabels(this, "", "Is Repaint Service Required", "#888888", "#FFFFFF", 0, true);
        isRepaintLabel.setBounds(940, 90, 180, 20);
        this.add(isRepaintLabel);

        JLabel repaintServiceFeeLabel = CS.paintLabels(this, "", "Repaint Fee", "#888888", "#FFFFFF", 0, true);
        repaintServiceFeeLabel.setBounds(940, 140, 80, 20);
        this.add(repaintServiceFeeLabel);

        repaintFeeField = new JTextField();
        repaintFeeField.setBounds(1030, 140, 90, 20);
        repaintFeeField.setEnabled(false);
        this.add(repaintFeeField);


        tableModel = new BillItemTableModel(orderModel.getBillItems());
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        tableScrollPane.setBounds(44, 250, 1101, 280);
        this.add(tableScrollPane);






        // Create a context menu for the table
        contextMenu = new JPopupMenu();
        deleteItem = new JMenuItem("Delete");
        contextMenu.add(deleteItem);


        JLabel partIdLabel = CS.paintLabels(this, "", "Part ID", "#7faab5", "#ffffff", 0, true);
        partIdLabel.setBounds(60, 560, 100, 25);
        this.add(partIdLabel);

        partIdField = new JTextField();
        partIdField.setBounds(170, 560, 150, 25);
        this.add(partIdField);

        JLabel partNameLabel = CS.paintLabels(this, "", "Part Name", "#7faab5", "#ffffff", 0, true);
        partNameLabel.setBounds(60, 595, 100, 25);
        this.add(partNameLabel);

        partNameValueLabel = CS.paintLabels(this, "", "????", "#7faab5", "#ffffff", 0, true);
        partNameValueLabel.setBounds(170, 595, 150, 25);
        this.add(partNameValueLabel);

        JLabel typeLabel = CS.paintLabels(this, "", "Type", "#7faab5", "#ffffff", 0, true);
        typeLabel.setBounds(360, 560, 100, 25);
        this.add(typeLabel);

        String[] types = orderModel.getOrderTypes().stream().map(OrderType::getTypeName).toArray(String[]::new);
        typeComboBox = new JComboBox<>(types);
        typeComboBox.setBounds(470, 560, 150, 25);
        this.add(typeComboBox);

        JLabel quantityLabel = CS.paintLabels(this, "", "Quantity", "#7faab5", "#ffffff", 0, true);
        quantityLabel.setBounds(360, 595, 100, 25);
        this.add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setBounds(470, 595, 150, 25);
        this.add(quantityField);

        JLabel unitPriceLabel = CS.paintLabels(this, "", "Unit Price", "#7faab5", "#ffffff", 0, true);
        unitPriceLabel.setBounds(60, 630, 100, 25);
        this.add(unitPriceLabel);

        unitPriceValueLabel = CS.paintLabels(this, "", "????", "#7faab5", "#ffffff", 0, true);
        unitPriceValueLabel.setBounds(170, 630, 150, 25);
        this.add(unitPriceValueLabel);

        addPartBtn = new JButton("Add To Bill");
        addPartBtn.setBounds(470, 630, 150, 25);
        this.add(addPartBtn);

        JLabel totalLabel = CS.paintLabels(this, "", "Total", "#888888", "#ffffff", 0, true);
        totalLabel.setBounds(720, 560, 200, 25);
        this.add(totalLabel);

        totalValueLabel = CS.paintLabels(this, "", "???", "#888888", "#ffffff", 0, true);
        totalValueLabel.setBounds(930, 560, 200, 25);
        this.add(totalValueLabel);

        saveBtn = new JButton("Save");
        saveBtn.setBounds(930, 620, 95, 25);
        this.add(saveBtn);

        cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(1035, 620, 95, 25);
        this.add(cancelBtn);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);

        //Insets insets = this.getInsets();
        //int contentX = insets.left;
        //int contentY = insets.top;
        //int contentWidth = this.getWidth() - insets.left - insets.right; // Width of content area
        //int contentHeight = this.getHeight() - insets.top - insets.bottom; // Height of content area

        CS.drawShaedBorder(g , new Rectangle(40, 90, 1120, 150));
        CS.drawShaedBorder(g , new Rectangle(640, 100, 250, 130));
        CS.drawShaedBorder(g , new Rectangle(900, 100, 250, 130));
        CS.drawShaedBorder(g , new Rectangle(40, 260, 1120, 450));
        CS.drawShaedBorder(g , new Rectangle(50, 570, 595, 130));
        CS.drawShaedBorder(g , new Rectangle(655, 570, 495, 130));

    }


    private void populateFieldsFromOrder() {
        // Method implementation here
    }

    private void updateOrderFromFields() {
        // Method implementation here
    }

    public OrderModel getOrderModel() {
        return orderModel;
    }

    public void setOrderModel(OrderModel orderModel) {
        this.orderModel = orderModel;
    }

    public OrderController getOrderController() {
        return orderController;
    }

    public void setOrderController(OrderController orderController) {
        this.orderController = orderController;
    }

    public JTextField getPartIdField() {
        return partIdField;
    }

    public void setPartIdField(JTextField partIdField) {
        this.partIdField = partIdField;
    }

    public JLabel getPartNameValueLabel() {
        return partNameValueLabel;
    }

    public void setPartNameValueLabel(JLabel partNameValueLabel) {
        this.partNameValueLabel = partNameValueLabel;
    }

    public JLabel getUnitPriceValueLabel() {
        return unitPriceValueLabel;
    }

    public void setUnitPriceValueLabel(JLabel unitPriceValueLabel) {
        this.unitPriceValueLabel = unitPriceValueLabel;
    }

    public int convertStringToInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (Exception e) {
            return -1;
        }
    }

    public BillItemTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(BillItemTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public JButton getAddPartBtn() {
        return addPartBtn;
    }

    public void setAddPartBtn(JButton addPartBtn) {
        this.addPartBtn = addPartBtn;
    }

    public JTextField getQuantityField() {
        return quantityField;
    }

    public void setQuantityField(JTextField quantityField) {
        this.quantityField = quantityField;
    }

    public JComboBox getTypeComboBox() {
        return typeComboBox;
    }

    public void setTypeComboBox(JComboBox typeComboBox) {
        this.typeComboBox = typeComboBox;
    }

    public JTextField getCustomerIdField() {
        return customerIdField;
    }

    public void setCustomerIdField(JTextField customerIdField) {
        this.customerIdField = customerIdField;
    }

    public JTextField getOrderYearField() {
        return orderYearField;
    }

    public void setOrderYearField(JTextField orderYearField) {
        this.orderYearField = orderYearField;
    }

    public JTextField getOrderMonthField() {
        return orderMonthField;
    }

    public void setOrderMonthField(JTextField orderMonthField) {
        this.orderMonthField = orderMonthField;
    }

    public JTextField getOrderDayField() {
        return orderDayField;
    }

    public void setOrderDayField(JTextField orderDayField) {
        this.orderDayField = orderDayField;
    }

    public JComboBox getStatusComboBox() {
        return statusComboBox;
    }

    public void setStatusComboBox(JComboBox statusComboBox) {
        this.statusComboBox = statusComboBox;
    }

    public JCheckBox getIsRepairCheckBox() {
        return isRepairCheckBox;
    }

    public void setIsRepairCheckBox(JCheckBox isRepairCheckBox) {
        this.isRepairCheckBox = isRepairCheckBox;
    }

    public JTextField getRepairFeeField() {
        return repairFeeField;
    }

    public void setRepairFeeField(JTextField repairFeeField) {
        this.repairFeeField = repairFeeField;
    }

    public JCheckBox getIsRepaintCheckBox() {
        return isRepaintCheckBox;
    }

    public void setIsRepaintCheckBox(JCheckBox isRepaintCheckBox) {
        this.isRepaintCheckBox = isRepaintCheckBox;
    }

    public JTextField getRepaintFeeField() {
        return repaintFeeField;
    }

    public void setRepaintFeeField(JTextField repaintFeeField) {
        this.repaintFeeField = repaintFeeField;
    }

    public JLabel getCustomerNameValueLabel() {
        return customerNameValueLabel;
    }

    public void setCustomerNameValueLabel(JLabel customerNameValueLabel) {
        this.customerNameValueLabel = customerNameValueLabel;
    }

    public JLabel getMobileLabel() {
        return mobileLabel;
    }

    public void setMobileLabel(JLabel mobileLabel) {
        this.mobileLabel = mobileLabel;
    }

    public JLabel getMobileValueLabel() {
        return mobileValueLabel;
    }

    public void setMobileValueLabel(JLabel mobileValueLabel) {
        this.mobileValueLabel = mobileValueLabel;
    }

    public JLabel getEmailLabel() {
        return emailLabel;
    }

    public void setEmailLabel(JLabel emailLabel) {
        this.emailLabel = emailLabel;
    }

    public JLabel getEmailValueLabel() {
        return emailValueLabel;
    }

    public void setEmailValueLabel(JLabel emailValueLabel) {
        this.emailValueLabel = emailValueLabel;
    }

    public JLabel getTotalValueLabel() {
        return totalValueLabel;
    }

    public void setTotalValueLabel(JLabel totalValueLabel) {
        this.totalValueLabel = totalValueLabel;
    }

    public JButton getSaveBtn() {
        return saveBtn;
    }

    public void setSaveBtn(JButton saveBtn) {
        this.saveBtn = saveBtn;
    }

    public JButton getCancelBtn() {
        return cancelBtn;
    }

    public void setCancelBtn(JButton cancelBtn) {
        this.cancelBtn = cancelBtn;
    }

    public JPopupMenu getContextMenu() {
        return contextMenu;
    }

    public void setContextMenu(JPopupMenu contextMenu) {
        this.contextMenu = contextMenu;
    }

    public JMenuItem getDeleteItem() {
        return deleteItem;
    }

    public void setDeleteItem(JMenuItem deleteItem) {
        this.deleteItem = deleteItem;
    }

    public JLabel getTitleLabel() {
        return titleLabel;
    }

    public void setTitleLabel(JLabel titleLabel) {
        this.titleLabel = titleLabel;
    }
}
