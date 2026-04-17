package views.panels;

import controllers.DashboardController;
import models.Property;
import services.CS;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EmailSettingsPanel extends DashboardModulePanel {


    private final DashboardController dashboardController;
    private JTextField protocolField;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField value1Field;
    private JTextField value2Field;
    private JTextField value3Field;
    private JTextField value4Field;
    private JTextField value5Field;
    private String bgColor = "#2c5f6b";
    private String fgColor = "#FFFFFF";
    private List<Property> properties;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton resetButton;

    public EmailSettingsPanel(DashboardController dashboardController, List<Property> properties) {


        this.dashboardController = dashboardController;
        this.properties = properties;
        addPageTitle("Email Settings", "Keep email credentials readable, aligned, and ready for shared project setup.");

        createUIComponents();

    }


    private void createUIComponents() {
        int x = 80, y = 120, lW = 230, lH = 22, fW = 300, fH = 34, g = 30, d = 18;

        JLabel protocolLabel = DashboardTheme.createMutedLabel("mail.transport.protocol");
        protocolLabel.setBounds(x, y, lW, lH);
        this.add(protocolLabel);
        protocolField = new JTextField();
        protocolField.setEditable(false);
        DashboardTheme.styleTextField(protocolField);
        protocolField.setBounds(x + lW + g, y - 4, fW, fH);
        this.add(protocolField);
        y += fH + d;

        JLabel usernameLabel = DashboardTheme.createMutedLabel("Username");
        usernameLabel.setBounds(x, y, lW, lH);
        this.add(usernameLabel);
        usernameField = new JTextField();
        DashboardTheme.styleTextField(usernameField);
        usernameField.setBounds(x + lW + g, y - 4, fW, fH);
        this.add(usernameField);
        y += fH + d;

        JLabel passwordLabel = DashboardTheme.createMutedLabel("Password");
        passwordLabel.setBounds(x, y, lW, lH);
        this.add(passwordLabel);
        passwordField = new JTextField();
        DashboardTheme.styleTextField(passwordField);
        passwordField.setBounds(x + lW + g, y - 4, fW, fH);
        this.add(passwordField);
        y += fH + d;

        JLabel key1Label = DashboardTheme.createMutedLabel("mail.smtp.host");
        key1Label.setBounds(x, y, lW, lH);
        this.add(key1Label);
        value1Field = new JTextField();
        DashboardTheme.styleTextField(value1Field);
        value1Field.setBounds(x + lW + g, y - 4, fW, fH);
        this.add(value1Field);
        y += fH + d;

        JLabel keyy2Label = DashboardTheme.createMutedLabel("mail.smtp.port");
        keyy2Label.setBounds(x, y, lW, lH);
        this.add(keyy2Label);
        value2Field = new JTextField();
        DashboardTheme.styleTextField(value2Field);
        value2Field.setBounds(x + lW + g, y - 4, fW, fH);
        this.add(value2Field);
        y += fH + d;

        JLabel key3Label = DashboardTheme.createMutedLabel("mail.smtp.auth");
        key3Label.setBounds(x, y, lW, lH);
        this.add(key3Label);
        value3Field = new JTextField();
        DashboardTheme.styleTextField(value3Field);
        value3Field.setBounds(x + lW + g, y - 4, fW, fH);
        this.add(value3Field);
        y += fH + d;

        JLabel key4Label = DashboardTheme.createMutedLabel("mail.debug");
        key4Label.setBounds(x, y, lW, lH);
        this.add(key4Label);
        value4Field = new JTextField();
        DashboardTheme.styleTextField(value4Field);
        value4Field.setBounds(x + lW + g, y - 4, fW, fH);
        this.add(value4Field);
        y += fH + d;

        JLabel key5Label = DashboardTheme.createMutedLabel("mail.smtp.starttls.enable");
        key5Label.setBounds(x, y, lW, lH);
        this.add(key5Label);
        value5Field = new JTextField();
        DashboardTheme.styleTextField(value5Field);
        value5Field.setBounds(x + lW + g, y - 4, fW, fH);
        this.add(value5Field);
        y += fH + d;


        // Create Save button
        saveButton = DashboardTheme.createPrimaryButton("Save");
        saveButton.setBounds(80, y + 28, 120, 36);
        saveButton.addActionListener(e -> updatePropertiesFromFields());
        this.add(saveButton);

        // Create Cancel button
        cancelButton = DashboardTheme.createSecondaryButton("Cancel");
        cancelButton.setBounds(220, y + 28, 120, 36);
        cancelButton.addActionListener(e -> populateFields());
        this.add(cancelButton);

        // Create Reset button
        resetButton = DashboardTheme.createDangerButton("Hard Reset");
        resetButton.setBounds(360, y + 28, 130, 36);
        resetButton.addActionListener(e -> hardResetEmailSettings());
        this.add(resetButton);


        populateFields();

    }

    public void populateFields(){
        for (int i = 0; i < properties.size(); i++) {
            Property property = properties.get(i);
            if (property.getPropertyKey().startsWith("username")) {
                usernameField.setText(property.getValue());
            } else if (property.getPropertyKey().startsWith("password")) {
                passwordField.setText(property.getValue());
            } else if (property.getPropertyKey().startsWith("mail.smtp.host")) {
                value1Field.setText(property.getValue());
            } else if (property.getPropertyKey().startsWith("mail.smtp.port")) {
                value2Field.setText(property.getValue());
            } else if (property.getPropertyKey().startsWith("mail.smtp.auth")) {
                value3Field.setText(property.getValue());
            } else if (property.getPropertyKey().startsWith("mail.transport.protocol")) {
                protocolField.setText(property.getValue());
            } else if (property.getPropertyKey().startsWith("mail.debug")) {
                value4Field.setText(property.getValue());
            } else if (property.getPropertyKey().startsWith("mail.smtp.starttls.enable")) {
                value5Field.setText(property.getValue());
            }
        }
    }


    public void hardResetEmailSettings() {
        List<Property> properties = new ArrayList<>();
        properties.add(new Property("mail.debug", "true", "email"));
        properties.add(new Property("mail.smtp.auth", "true", "email"));
        properties.add(new Property("mail.smtp.host", "smtp.gmail.com", "email"));
        properties.add(new Property("mail.smtp.port", "587", "email"));
        properties.add(new Property("mail.smtp.starttls.enable", "true", "email"));
        properties.add(new Property("mail.transport.protocol", "smtp", "email"));
        properties.add(new Property("password", "uhwkzenjrjiyjcdo", "email"));
        properties.add(new Property("username", "kamshika236@gmail.com", "email"));
        boolean updated = this.dashboardController.addOrUpdateProperties(properties);
        if(updated){
            this.properties = properties;
            this.populateFields();
        }
    }

    public void updatePropertiesFromFields() {
        List<Property> updatedProperties = new ArrayList<>();
        updatedProperties.add(new Property("mail.transport.protocol", protocolField.getText(), "email"));
        updatedProperties.add(new Property("username", usernameField.getText(), "email"));
        updatedProperties.add(new Property("password", passwordField.getText(), "email"));
        updatedProperties.add(new Property("mail.smtp.host", value1Field.getText(), "email"));
        updatedProperties.add(new Property("mail.smtp.port", value2Field.getText(), "email"));
        updatedProperties.add(new Property("mail.smtp.auth", value3Field.getText(), "email"));
        updatedProperties.add(new Property("mail.debug", value4Field.getText(), "email"));
        updatedProperties.add(new Property("mail.smtp.starttls.enable", value5Field.getText(), "email"));
        boolean updated = this.dashboardController.addOrUpdateProperties(updatedProperties);
        if(updated){
            this.properties = properties;
            this.populateFields();
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw a rectangle
        CS.drawShaedBorder(g, new Rectangle(40, 80, 850, 500));
        //CS.drawVerticalDoubleLine(g, 380,380,81,174);
        //CS.drawDoubleBorder(g, new Rectangle(40, 500, 850, 170));
        //CS.drawDoubleBorder(g, new Rectangle(50, 510, 330, 150));
        //CS.drawDoubleBorder(g, new Rectangle(390, 510, 490, 150));
        //50 515, 380 595
    }


}
