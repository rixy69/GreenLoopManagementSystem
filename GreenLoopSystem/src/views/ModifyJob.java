package views;

import models.Employee;
import models.Jobs;
import models.Part;
import services.CS;
import views.FooterPanel;
import views.panels.JobsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifyJob extends JFrame {
    private final JobsPanel parentView;
    private final Jobs job;
    private Employee employee;
    private int initialEmployeeId;

    // Instance variables for input fields
    private JTextField jobIdField;
    private JTextField orderIdField;
    private JTextField employeeIdField;
    private JComboBox statusComboBox;
    private JTextField assignedDateYearField;
    private JTextField assignedDateMonthField;
    private JTextField assignedDateDayField;


    public ModifyJob(JobsPanel parentView, Employee employee, String title, Jobs job) {
        super(title);
        this.parentView = parentView;
        this.job = job;
        this.employee = employee;

        this.setSize(400, 615);
        this.setLocation(200, 100);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.createUIComponents();
        FooterPanel footerLabel = CS.paintFooter((JPanel) getContentPane());
        footerLabel.setBounds(0, this.getHeight() - 20, this.getWidth(), 20);
        this.populateFieldsFromJob();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.decode("#BBBBBB"));
        g.drawRect(45, 185, 310, 180); // (x, y, width, height)
    }


    private void createUIComponents() {
        int x = 40, y = 40, lW = 100, lH = 25, fW = 200, fH = 25, g = 10, d = 10;

        JLabel jobIdLabel = CS.paintLabels(this, "", "Job ID", null, null, 2, false);
        jobIdLabel.setBounds(x, y, lW, lH);
        jobIdField = new JTextField(String.valueOf(job.getJobId()));
        jobIdField.setBounds(x + lW + g, y, fW, fH);
        jobIdField.setEditable(false);
        this.add(jobIdField);
        y += lH + d;

        JLabel orderIdLabel = CS.paintLabels(this, "", "Order ID", null, null, 2, false);
        orderIdLabel.setBounds(x, y, lW, lH);
        orderIdField = new JTextField();
        orderIdField.setBounds(x + lW + g, y, fW, fH);
        orderIdField.setEditable(false);
        this.add(orderIdField);
        y += lH + d;

        JLabel employeeIdLabel = CS.paintLabels(this, "", "Employee ID", null, null, 2, false);
        employeeIdLabel.setBounds(x, y, lW, lH);
        employeeIdField = new JTextField();
        employeeIdField.setBounds(x + lW + g, y, fW, fH);
        this.add(employeeIdField);
        y += lH + d;  //40,145



        y += 35;



        JLabel employeeNameLabel = CS.paintLabels(this, "", "Name", "#7faab5", "#ffffff", 0, true);
        employeeNameLabel.setBounds(x+20, y, lW -20 , lH);

        JLabel employeeNameValueLabel = CS.paintLabels(this, "", "????", "#555555", "#ffffff", 0, true);
        employeeNameValueLabel.setBounds(x + lW + g, y, fW -25 , lH);
        
        y += lH + d;

        JLabel emailLabel = CS.paintLabels(this, "", "Email", "#7faab5", "#ffffff", 0, true);
        emailLabel.setBounds(x+20, y, lW-20, lH);

        JLabel employeeEmailValueLabel = CS.paintLabels(this, "", "????", "#555555", "#ffffff", 0, true);
        employeeEmailValueLabel.setBounds(x + lW + g, y, fW -25 , lH);

        y += lH + d;

        JLabel MobileLabel = CS.paintLabels(this, "", "Mobile", "#7faab5", "#ffffff", 0, true);
        MobileLabel.setBounds(x+20, y, lW-20, lH);

        JLabel employeeMobileValueLabel = CS.paintLabels(this, "", "????", "#555555", "#ffffff", 0, true);
        employeeMobileValueLabel.setBounds(x + lW + g, y, fW -25 , lH);

        y += lH + d;

        JLabel roleLabel = CS.paintLabels(this, "", "Role", "#7faab5", "#ffffff", 0, true);
        roleLabel.setBounds(x+20, y, lW-20, lH);

        JLabel employeeRoleValueLabel = CS.paintLabels(this, "", "????", "#555555", "#ffffff", 0, true);
        employeeRoleValueLabel.setBounds(x + lW + g, y, fW -25 , lH);

        y += lH + d;

        if (employee != null) {
            this.initialEmployeeId = employee.getEmployeeId();
            employeeNameValueLabel.setText(employee.getFirstName());
            employeeEmailValueLabel.setText(employee.getEmail());
            employeeMobileValueLabel.setText(employee.getMobile());
            employeeRoleValueLabel.setText(employee.getRole().getRoleName());
        }

        employeeIdField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = employeeIdField.getText().trim();
                if (!text.isEmpty()) {
                    int id = convertStringToInt(text);
                    System.out.println(id);
                    if (id > 0) {
                        employee = parentView.getEmployeeById(id);
                        if (employee != null) {
                            employeeNameValueLabel.setText(employee.getFirstName());
                            employeeEmailValueLabel.setText(employee.getEmail());
                            employeeMobileValueLabel.setText(employee.getMobile());
                            employeeRoleValueLabel.setText(employee.getRole().getRoleName());
                            return;
                        }
                    }
                }
                employee = null;
                employeeNameValueLabel.setText("????");
                employeeEmailValueLabel.setText("????");
                employeeRoleValueLabel.setText("????");
            }
        });



        y += 40 ;

        JLabel statusLabel = CS.paintLabels(this, "", "Status", null, null, 2, false);
        statusLabel.setBounds(x, y, lW, lH);

        String[] statuses = {"Job Created", "Job Assigned", "Job Started", "Job Completed"};
        statusComboBox = new JComboBox(statuses);
        statusComboBox.setBounds(x + lW + g, y, 130, fH);
        this.add(statusComboBox);
        y += lH + d + 5;

        JLabel assignedDateLabel = CS.paintLabels(this, "", "Assigned Date", null, null, 2, false);
        assignedDateLabel.setBounds(x, y, lW, lH);

        Font font = new Font("Dialog", Font.ITALIC,11);

        assignedDateYearField = new JTextField();
        assignedDateYearField.setHorizontalAlignment(SwingConstants.CENTER);
        assignedDateYearField.setBounds(x + lW + g, y, 60, fH);
        this.add(assignedDateYearField);

        JLabel yearHelpLabel = CS.paintLabels(this, "", "Year", null, null, 0, false);
        yearHelpLabel.setFont(font);
        yearHelpLabel.setBounds(x + lW + g, y+20, 60, fH);
        this.add(yearHelpLabel);


        assignedDateMonthField = new JTextField();
        assignedDateMonthField.setHorizontalAlignment(SwingConstants.CENTER);
        assignedDateMonthField.setBounds(x + lW + g + 70, y, 60, fH);
        this.add(assignedDateMonthField);

        JLabel monthHelpLabel = CS.paintLabels(this, "", "Month", null, null, 0, false);
        monthHelpLabel.setFont(font);
        monthHelpLabel.setBounds(x + lW + g + 70, y+20, 60, fH);
        this.add(monthHelpLabel);

        assignedDateDayField = new JTextField();
        assignedDateDayField.setHorizontalAlignment(SwingConstants.CENTER);
        assignedDateDayField.setBounds(x + lW + g + 140, y, 60, fH);
        this.add(assignedDateDayField);

        JLabel dayHelpLabel = CS.paintLabels(this, "", "Day", null, null, 0, false);
        dayHelpLabel.setFont(font);
        dayHelpLabel.setBounds(x + lW + g + 140, y+20, 60, fH);
        this.add(dayHelpLabel);




        y += lH + d;

        y += 50;
        int bW = 95, bH = 25;
        int g_2 = 10; // gap between buttons
        int g_1 = fW - 2 * bW - g_2; // gap before buttons

        JButton saveBtn = new JButton("Save");
        saveBtn.setBounds(x + lW + g + g_1, y, bW, bH);
        this.add(saveBtn);

        saveBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(updateJobFromFields()){
                    boolean updated = parentView.updateJob(job);
                    if (updated) {
                        JOptionPane.showMessageDialog(null, "Job updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        if(initialEmployeeId != employee.getEmployeeId()){
                            int response = JOptionPane.showConfirmDialog(
                                    null,
                                    "Do you want to notify " + employee.getTitle() + " " + employee.getFirstName() + " regarding this job assignment!",
                                    "Job notification email",
                                    JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.QUESTION_MESSAGE
                            );

                            // Check the user's response
                            if (response == JOptionPane.OK_OPTION) {
                                parentView.sendJobAssignmentEmail(employee.getEmployeeId(), job.getJobId());
                            }
                        }
                        if(job.getStatus().equals("Job Completed")){
                            int response = JOptionPane.showConfirmDialog(
                                    null,
                                    "Do you want to notify customer regarding this job completion.!",
                                    "Job completion email",
                                    JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.QUESTION_MESSAGE
                            );

                            // Check the user's response
                            if (response == JOptionPane.OK_OPTION) {
                                parentView.sendJobCompletionEmail(job.getOrderId());
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to update job.", "Error", JOptionPane.ERROR_MESSAGE);
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
    }

    public int convertStringToInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (Exception ignored) {
        }
        return -1;
    }
    
    // Method to populate fields from the job object
    private void populateFieldsFromJob() {
        orderIdField.setText(String.valueOf(job.getOrderId()));
        employeeIdField.setText(String.valueOf(job.getEmployeeId()));
        statusComboBox.setSelectedItem(job.getStatus());
        Date assignedDate = job.getAssignedDate();
        if(assignedDate==null){
            assignedDate = new Date();
        }
        assignedDateYearField.setText(Integer.toString(assignedDate.getYear() + 1900));
        assignedDateMonthField.setText(Integer.toString(assignedDate.getMonth() + 1));
        assignedDateDayField.setText(Integer.toString(assignedDate.getDate()));

    }

    // Method to update the job object from the fields
    private boolean updateJobFromFields() {
        try {

            if (employee == null) {
                String errorMsg = "No employee found for ID : " + employeeIdField.getText();
                JOptionPane.showMessageDialog(null, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            job.setOrderId(Integer.parseInt(orderIdField.getText()));
            job.setEmployeeId(employee.getEmployeeId());
            job.setStatus(statusComboBox.getSelectedItem().toString());

            String assignedYear = assignedDateYearField.getText();
            String assignedMonth = assignedDateMonthField.getText();
            String assignedDay = assignedDateDayField.getText();


            String dateString = assignedYear + "-" + assignedMonth + "-" + assignedDay;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            job.setAssignedDate(dateFormat.parse(dateString));
            job.setAssignedDate(dateFormat.parse(dateString));
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public int getInitialEmployeeId() {
        return initialEmployeeId;
    }

    public void setInitialEmployeeId(int initialEmployeeId) {
        this.initialEmployeeId = initialEmployeeId;
    }
}
