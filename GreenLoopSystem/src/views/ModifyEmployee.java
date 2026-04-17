package views;

import models.Employee;
import models.Role;
import services.CS;
import views.FooterPanel;
import views.panels.EmployeesPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ModifyEmployee extends JFrame {
    private Employee employee;

    // Instance variables for input fields
    private JComboBox<String> titleComboBox;
    private JTextField fnField;
    private JTextField lnField;
    private TextArea addressArea;
    private JTextField mobileField;
    private JTextField emailField;
    private JTextField scheduleField;
    private JTextField unField;
    private JTextField passwordField;
    private JComboBox<String> roleComboBox;
    private List<Role> roles;

    private List<Integer> roleIds;
    private List<String> roleNames;
    EmployeesPanel parentView;

    public ModifyEmployee(EmployeesPanel parentView, String title, Employee employee, List<Role> roles) {
        super(title);
        this.parentView = parentView;
        this.employee = employee;
        this.roles = roles;


        roleIds = new ArrayList<>();
        roleNames = new ArrayList<>();

        roles.forEach(role -> {
            roleIds.add(role.getRoleId());
            roleNames.add(role.getRoleName());
        });


        this.setSize(500, 600);
        this.setLocation(200, 100);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.createUIComponents();
        FooterPanel footerLabel = CS.paintFooter((JPanel) getContentPane());
        footerLabel.setBounds(0, this.getHeight() - 20, this.getWidth(), 20);
        this.populateFieldsFromEmployee();
    }

    private void createUIComponents() {
        int x = 40, y = 40, lW = 150, lH = 25, fW = 250, fH = 25, g = 10, d = 10;

        if (employee.getEmployeeId() > 0) {
            JLabel employeeIdLabel = CS.paintLabels(this, "", "Employee ID", null, null, 2, false);
            employeeIdLabel.setBounds(x, y, lW, lH);

            JLabel employeeIdValueLabel = CS.paintLabels(this, "", String.valueOf(employee.getEmployeeId()), "#2c989c", "#ffffff", 0, true);
            employeeIdValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
            employeeIdValueLabel.setBounds(x + lW + g, y, 50, fH);
            this.add(employeeIdValueLabel);
            y += lH + d;
        }

        JLabel titleLabel = CS.paintLabels(this, "", "Title", null, null, 2, false);
        titleLabel.setBounds(x, y, lW, lH);
        String[] titles = {"Mr.", "Mrs.", "Ms.", "Dr.", "Prof.", "Rev."};
        titleComboBox = new JComboBox<>(titles);
        titleComboBox.setBounds(x + lW + g, y, 100, fH);
        this.add(titleComboBox);
        y += lH + d;

        JLabel fnLabel = CS.paintLabels(this, "", "First Name", null, null, 2, false);
        fnLabel.setBounds(x, y, lW, lH);
        fnField = new JTextField();
        fnField.setBounds(x + lW + g, y, fW, fH);
        this.add(fnField);
        y += lH + d;

        JLabel lnLabel = CS.paintLabels(this, "", "Last Name", null, null, 2, false);
        lnLabel.setBounds(x, y, lW, lH);
        lnField = new JTextField();
        lnField.setBounds(x + lW + g, y, fW, fH);
        this.add(lnField);
        y += lH + d;

        JLabel addressLabel = CS.paintLabels(this, "", "Address", null, null, 2, false);
        addressLabel.setBounds(x, y, lW, lH);
        addressArea = new TextArea();
        addressArea.setBounds(x + lW + g, y, fW, 3 * fH);
        this.add(addressArea);
        y += 3 * fH + d;

        JLabel mobileLabel = CS.paintLabels(this, "", "Mobile", null, null, 2, false);
        mobileLabel.setBounds(x, y, lW, lH);
        mobileField = new JTextField();
        mobileField.setBounds(x + lW + g, y, fW, fH);
        this.add(mobileField);
        y += lH + d;

        JLabel emailLabel = CS.paintLabels(this, "", "Email", null, null, 2, false);
        emailLabel.setBounds(x, y, lW, lH);
        emailField = new JTextField();
        emailField.setBounds(x + lW + g, y, fW, fH);
        this.add(emailField);
        y += lH + d;

        JLabel scheduleLabel = CS.paintLabels(this, "", "Schedule", null, null, 2, false);
        scheduleLabel.setBounds(x, y, lW, lH);
        scheduleField = new JTextField();
        scheduleField.setBounds(x + lW + g, y, fW, fH);
        this.add(scheduleField);
        y += lH + d;

        JLabel unLabel = CS.paintLabels(this, "", "Username", null, null, 2, false);
        unLabel.setBounds(x, y, lW, lH);
        unField = new JTextField();
        unField.setBounds(x + lW + g, y, fW, fH);
        this.add(unField);
        y += lH + d;

        JLabel passwordLabel = CS.paintLabels(this, "", "Password", null, null, 2, false);
        passwordLabel.setBounds(x, y, lW, lH);
        passwordField = new JTextField();
        passwordField.setBounds(x + lW + g, y, fW, fH);
        this.add(passwordField);
        y += lH + d;

        JLabel roleLabel = CS.paintLabels(this, "", "Role", null, null, 2, false);
        roleLabel.setBounds(x, y, lW, lH);
        roleComboBox = new JComboBox<>(roleNames.toArray(new String[0]));
        roleComboBox.setBounds(x + lW + g, y, 100, fH);
        this.add(roleComboBox);
        y += lH + d;

        //        int x = 40, y = 40, lW = 150, lH = 25, fW = 250, fH = 25, g = 10, d = 10;
        //500 - 40 - 150 - 10 - 250 = 50

        y += 35;
        int bW = 65, bH = 25;
        int g_2 = 15; //gap between buttons
        int g_1 = fW - 2*bW - g_2; // gap before buttons

        JButton saveBtn = new JButton("Save");
        saveBtn.setBounds(x + lW + g + g_1, y, bW, bH);
        this.add(saveBtn);

        saveBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEmployeeFromFields();
                boolean isNewEmployee = employee.getEmployeeId()==0 ;
                if (isNewEmployee) {
                    boolean created = parentView.createEmployee(employee);
                    if (created) {
                        JOptionPane.showMessageDialog(null, "Employee created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to create employee.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    boolean updated = parentView.updateEmployee(employee);
                    if (updated) {
                        JOptionPane.showMessageDialog(null, "Employee updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to update employee.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


        JButton cancelBtn = new JButton("Exit");
        cancelBtn.setBounds(x + lW + g + g_1 + bW + g_2, y, bW, bH);
        this.add(cancelBtn);

        cancelBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        if(employee.getEmployeeId()>0){
            populateFieldsFromEmployee();
        }



    }
    
    // Method to populate fields from the employee object
    private void populateFieldsFromEmployee() {
        titleComboBox.setSelectedItem(employee.getTitle());
        fnField.setText(employee.getFirstName());
        lnField.setText(employee.getLastName());
        addressArea.setText(employee.getAddress());
        mobileField.setText(employee.getMobile());
        emailField.setText(employee.getEmail());
        scheduleField.setText(employee.getSchedule());
        unField.setText(employee.getUsername());
        passwordField.setText(employee.getPassword());
        int roleIndex = roleIds.indexOf(employee.getRoleId());
        if(roleIndex<=0) roleIndex=1; //New Employee
        roleComboBox.setSelectedItem(roleNames.get(roleIndex));
    }

    // Method to update the employee object from the fields
    private void updateEmployeeFromFields() {
        employee.setTitle((String) titleComboBox.getSelectedItem());
        employee.setFirstName(fnField.getText());
        employee.setLastName(lnField.getText());
        employee.setAddress(addressArea.getText());
        employee.setMobile(mobileField.getText());
        employee.setEmail(emailField.getText());
        employee.setSchedule(scheduleField.getText());
        employee.setUsername(unField.getText());
        employee.setPassword(passwordField.getText());
        int roleIndex = roleNames.indexOf(roleComboBox.getSelectedItem());
        employee.setRoleId(roleIds.get(roleIndex));
        employee.setRole(roles.get(roleIndex));
    }

}
