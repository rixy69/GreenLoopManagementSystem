package views;

import models.Employee;
import models.Jobs;
import models.Part;
import services.CS;
import views.FooterPanel;
import views.panels.JobsPanel;
import views.panels.MyJobsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifyMyJob extends JFrame {
    private final MyJobsPanel parentView;
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
    private JTextField startDateYearField;
    private JTextField startDateMonthField;
    private JTextField startDateDayField;
    private JTextField endDateYearField;
    private JTextField endDateMonthField;
    private JTextField endDateDayField;



    public ModifyMyJob(MyJobsPanel parentView, Employee employee, String title, Jobs job) {
        super(title);
        this.parentView = parentView;
        this.job = job;
        this.employee = employee;

        this.setSize(400, 725);
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
        employeeIdField.setEditable(false);
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


        y += 40 ;

        JLabel statusLabel = CS.paintLabels(this, "", "Status", null, null, 2, false);
        statusLabel.setBounds(x, y, lW, lH);

        String[] statuses = {"Job Started", "Job Completed"};
        statusComboBox = new JComboBox(statuses);
        statusComboBox.setBounds(x + lW + g, y, 130, fH);
        this.add(statusComboBox);
        y += lH + d + 5;

        Font font = new Font("Dialog", Font.ITALIC,11);

        JLabel assignedDateLabel = CS.paintLabels(this, "", "Assigned Date", null, null, 2, false);
        assignedDateLabel.setBounds(x, y, lW, lH);

        assignedDateYearField = new JTextField();
        assignedDateYearField.setHorizontalAlignment(SwingConstants.CENTER);
        assignedDateYearField.setBounds(x + lW + g, y, 60, fH);
        assignedDateYearField.setEditable(false);
        this.add(assignedDateYearField);

        JLabel assignedYearHelpLabel = CS.paintLabels(this, "", "Year", null, null, 0, false);
        assignedYearHelpLabel.setFont(font);
        assignedYearHelpLabel.setBounds(x + lW + g, y+20, 60, fH);
        this.add(assignedYearHelpLabel);


        assignedDateMonthField = new JTextField();
        assignedDateMonthField.setHorizontalAlignment(SwingConstants.CENTER);
        assignedDateMonthField.setBounds(x + lW + g + 70, y, 60, fH);
        assignedDateMonthField.setEditable(false);
        this.add(assignedDateMonthField);

        JLabel assignedMonthHelpLabel = CS.paintLabels(this, "", "Month", null, null, 0, false);
        assignedMonthHelpLabel.setFont(font);
        assignedMonthHelpLabel.setBounds(x + lW + g + 70, y+20, 60, fH);
        this.add(assignedMonthHelpLabel);

        assignedDateDayField = new JTextField();
        assignedDateDayField.setHorizontalAlignment(SwingConstants.CENTER);
        assignedDateDayField.setBounds(x + lW + g + 140, y, 60, fH);
        assignedDateDayField.setEditable(false);
        this.add(assignedDateDayField);

        JLabel assignedDayHelpLabel = CS.paintLabels(this, "", "Day", null, null, 0, false);
        assignedDayHelpLabel.setFont(font);
        assignedDayHelpLabel.setBounds(x + lW + g + 140, y+20, 60, fH);
        this.add(assignedDayHelpLabel);

        y += lH + d;


        y += 20;


        JLabel startDateLabel = CS.paintLabels(this, "", "Started Date", null, null, 2, false);
        startDateLabel.setBounds(x, y, lW, lH);

        startDateYearField = new JTextField();
        startDateYearField.setHorizontalAlignment(SwingConstants.CENTER);
        startDateYearField.setBounds(x + lW + g, y, 60, fH);
        this.add(startDateYearField);

        JLabel startYearHelpLabel = CS.paintLabels(this, "", "Year", null, null, 0, false);
        startYearHelpLabel.setFont(font);
        startYearHelpLabel.setBounds(x + lW + g, y+20, 60, fH);
        this.add(startYearHelpLabel);


        startDateMonthField = new JTextField();
        startDateMonthField.setHorizontalAlignment(SwingConstants.CENTER);
        startDateMonthField.setBounds(x + lW + g + 70, y, 60, fH);
        this.add(startDateMonthField);

        JLabel startMonthHelpLabel = CS.paintLabels(this, "", "Month", null, null, 0, false);
        startMonthHelpLabel.setFont(font);
        startMonthHelpLabel.setBounds(x + lW + g + 70, y+20, 60, fH);
        this.add(startMonthHelpLabel);

        startDateDayField = new JTextField();
        startDateDayField.setHorizontalAlignment(SwingConstants.CENTER);
        startDateDayField.setBounds(x + lW + g + 140, y, 60, fH);
        this.add(startDateDayField);

        JLabel startDayHelpLabel = CS.paintLabels(this, "", "Day", null, null, 0, false);
        startDayHelpLabel.setFont(font);
        startDayHelpLabel.setBounds(x + lW + g + 140, y+20, 60, fH);
        this.add(startDayHelpLabel);

        y += lH + d;

        y += 20;


        JLabel endDateLabel = CS.paintLabels(this, "", "Completed Date", null, null, 2, false);
        endDateLabel.setBounds(x, y, lW, lH);

        endDateYearField = new JTextField();
        endDateYearField.setHorizontalAlignment(SwingConstants.CENTER);
        endDateYearField.setBounds(x + lW + g, y, 60, fH);
        this.add(endDateYearField);

        JLabel endYearHelpLabel = CS.paintLabels(this, "", "Year", null, null, 0, false);
        endYearHelpLabel.setFont(font);
        endYearHelpLabel.setBounds(x + lW + g, y+20, 60, fH);
        this.add(endYearHelpLabel);


        endDateMonthField = new JTextField();
        endDateMonthField.setHorizontalAlignment(SwingConstants.CENTER);
        endDateMonthField.setBounds(x + lW + g + 70, y, 60, fH);
        this.add(endDateMonthField);

        JLabel endMonthHelpLabel = CS.paintLabels(this, "", "Month", null, null, 0, false);
        endMonthHelpLabel.setFont(font);
        endMonthHelpLabel.setBounds(x + lW + g + 70, y+20, 60, fH);
        this.add(endMonthHelpLabel);

        endDateDayField = new JTextField();
        endDateDayField.setHorizontalAlignment(SwingConstants.CENTER);
        endDateDayField.setBounds(x + lW + g + 140, y, 60, fH);
        this.add(endDateDayField);

        JLabel endDayHelpLabel = CS.paintLabels(this, "", "Day", null, null, 0, false);
        endDayHelpLabel.setFont(font);
        endDayHelpLabel.setBounds(x + lW + g + 140, y+20, 60, fH);
        this.add(endDayHelpLabel);

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


        Date startDate = job.getStartDate();
        if(startDate!=null){
            startDateYearField.setText(Integer.toString(startDate.getYear() + 1900));
            startDateMonthField.setText(Integer.toString(startDate.getMonth() + 1));
            startDateDayField.setText(Integer.toString(startDate.getDate()));
        }

        Date endDate = job.getEndDate();
        if(endDate!=null){
            endDateYearField.setText(Integer.toString(endDate.getYear() + 1900));
            endDateMonthField.setText(Integer.toString(endDate.getMonth() + 1));
            endDateDayField.setText(Integer.toString(endDate.getDate()));
        }


    }

    // Method to update the job object from the fields
    private boolean updateJobFromFields() {
        try {

            job.setOrderId(Integer.parseInt(orderIdField.getText()));
            job.setEmployeeId(employee.getEmployeeId());
            job.setStatus(statusComboBox.getSelectedItem().toString());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String assignedYear = assignedDateYearField.getText();
            String assignedMonth = assignedDateMonthField.getText();
            String assignedDay = assignedDateDayField.getText();


            String assignedDateString = assignedYear + "-" + assignedMonth + "-" + assignedDay;
            job.setAssignedDate(dateFormat.parse(assignedDateString));


            String startYear = startDateYearField.getText();
            String startMonth = startDateMonthField.getText();
            String startDay = startDateDayField.getText();


            String startDateString = startYear + "-" + startMonth + "-" + startDay;
            try{
                job.setStartDate(dateFormat.parse(startDateString));
            }catch (Exception e){}


            String endYear = endDateYearField.getText();
            String endMonth = endDateMonthField.getText();
            String endDay = endDateDayField.getText();


            String endDateString = endYear + "-" + endMonth + "-" + endDay;
            try{
                job.setEndDate(dateFormat.parse(endDateString));
            }catch (Exception e){}



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
