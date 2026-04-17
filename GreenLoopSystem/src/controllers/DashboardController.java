package controllers;

import models.*;
import services.*;
import services.Impl.*;
import views.DashboardView;
import views.LoginView;
import views.OrderView;
import views.panels.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.util.List;

public class DashboardController {

    private DashboardView dashboardView;
    private DashboardModel dashboardModel;
    private DatabaseConnectionService databaseConnectionService;
    private EmployeeService employeeService;
    private RoleService roleService;
    private CustomerService customerService;
    private SupplierService supplierService;
    private PartService partService;
    private InventoryService inventoryService;
    private OrderService orderService;
    private JobsService jobsService;
    private JobTypeService jobTypeService;
    private OrderPartService orderPartService;
    private OrderTypeService orderTypeService;
    private NotificationService notificationService;
    private PropertyService propertyService;
    private EmailService emailService;
    private SalesService salesService;


    public DashboardController(DashboardView dashboardView, DashboardModel dashboardModel) {

        this.dashboardView = dashboardView;
        this.dashboardModel = dashboardModel;

        this.databaseConnectionService = new MySQLConnectionService();

        this.employeeService = new EmployeeServiceImpl(databaseConnectionService);
        this.roleService = new RoleServiceImpl(databaseConnectionService);
        this.customerService = new CustomerServiceImpl(databaseConnectionService);
        this.supplierService = new SupplierServiceImpl(databaseConnectionService);
        this.partService = new PartServiceImpl(databaseConnectionService);
        this.inventoryService = new InventoryServiceImpl(databaseConnectionService);
        this.orderService = new OrderServiceImpl(databaseConnectionService);
        this.jobsService = new JobsServiceImpl(databaseConnectionService);
        this.orderService = new OrderServiceImpl(databaseConnectionService);
        this.partService = new PartServiceImpl(databaseConnectionService);
        this.supplierService = new SupplierServiceImpl(databaseConnectionService);
        this.customerService = new CustomerServiceImpl(databaseConnectionService);
        this.orderPartService = new OrderPartServiceImpl(databaseConnectionService);
        this.orderTypeService = new OrderTypeServiceImpl(databaseConnectionService);
        this.jobTypeService = new JobTypeServiceImpl(databaseConnectionService);
        this.notificationService = new NotificationServiceImpl(databaseConnectionService);
        this.propertyService = new PropertyServiceImpl(databaseConnectionService);
        this.emailService = new EmailServiceImpl(propertyService);
        this.salesService = new SalesServiceImpl(databaseConnectionService);



        this.dashboardView.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                paintPanels(dashboardView);
                super.componentResized(e);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e);
            }

            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
            }

        });

        dashboardView.getSideBar().getButtons().forEach(jButton -> jButton.addActionListener(e -> handleNavigation(jButton.getText())));
        dashboardView.getTopBar().getLogoutButton().addActionListener(e -> handleNavigation("Logout"));

        paintPanels(dashboardView);
    }


    public void showOrderView(Order order, String title, OrderPanel orderPanel){

        List<Customer> customers = customerService.getAllCustomers();
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        List<Part> parts = partService.getAllParts();
        List<OrderType> orderTypes = orderTypeService.getAllOrderTypes();

        OrderModel orderModel = new OrderModel(order, customers, suppliers, parts, orderTypes);
        OrderView orderView = new OrderView(orderModel, title);

        OrderController orderController = new OrderController(databaseConnectionService, orderView, orderModel, orderService, partService, customerService, supplierService, inventoryService, orderPartService, jobsService, jobTypeService, orderTypeService, orderPanel);
        orderView.setOrderController(orderController);
        if(order.getOrderId() > 0) orderController.setOrderToView(order);
        orderView.setVisible(true);

        
    }

    public void paintPanels(DashboardView dashboardView) {

        JPanel mainPanel = dashboardView.getMainPanel();
        JPanel sideBar = dashboardView.getSideBar();
        JScrollPane sideBarScrollPane = dashboardView.getSideBarScrollPane();
        JPanel contentHost = dashboardView.getContentHost();
        JPanel contentPanel = dashboardView.getContentPanel();
        JPanel topBar = dashboardView.getTopBar();

        final int d = 14;
        final int sideBarWidth = 220;
        final int topBarHeight = 104;

        Dimension wh = mainPanel.getSize();

        Rectangle sideBarBounds = new Rectangle(d, d, sideBarWidth, wh.height - 2 * d);
        sideBarScrollPane.setBounds(sideBarBounds);
        sideBarScrollPane.setBorder(new LineBorder(new Color(255, 255, 255, 70), 1, true));
        sideBar.setPreferredSize(sideBarBounds.getSize());
        mainPanel.add(sideBarScrollPane);
        sideBar.setBorder(null);

        int contentX = 2 * d + sideBarWidth;
        int contentWidth = wh.width - sideBarWidth - 3 * d;
        int contentHeight = wh.height - 2 * d;

        topBar.setBounds(contentX, d, contentWidth, topBarHeight);
        contentHost.setBounds(contentX, d + topBarHeight + 12, contentWidth, contentHeight - topBarHeight - 12);
        if (contentPanel != null) {
            contentPanel.setBounds(0, 0, contentHost.getWidth(), contentHost.getHeight());
        }
        mainPanel.add(topBar);
        mainPanel.add(contentHost);
    }

    private void handleNavigation(String btn) {
        if ("Logout".equals(btn)) {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
            new LoginController(new Employee(), loginView, employeeService, roleService);
            dashboardView.setVisible(false);
            dashboardView.dispose();
            return;
        }

        if (!dashboardView.getCurrentPanelName().equals(btn)) {
            switch (btn) {
                case "Home":
                    dashboardView.setContentPanel(new HomePanel(dashboardView.getCurrentUser()));
                    break;
                case "Manage Staff":
                    List<Employee> employees = employeeService.getAllEmployees();
                    List<Role> roles = roleService.getAllRoles();
                    dashboardView.setContentPanel(new EmployeesPanel(this, employees, roles));
                    break;
                case "Manage Roles":
                    dashboardView.setContentPanel(new RolesPanel(this, roleService.getAllRoles()));
                    break;
                case "Manage Clients":
                    dashboardView.setContentPanel(new CustomersPanel(this, customerService.getAllCustomers()));
                    break;
                case "Manage Suppliers":
                    dashboardView.setContentPanel(new SuppliersPanel(this, supplierService.getAllSuppliers()));
                    break;
                case "Product Catalogue":
                    dashboardView.setContentPanel(new PartsPanel(this, partService.getAllParts()));
                    break;
                case "Manage Inventory":
                    dashboardView.setContentPanel(new InventoryPanel(this, inventoryService.getAllInventories()));
                    break;
                case "Manage Orders":
                    dashboardView.setContentPanel(new OrderPanel(this, orderService.getAllOrders()));
                    break;
                case "Delivery Assignments":
                    dashboardView.setContentPanel(new JobsPanel(this, jobsService.getAllJobs()));
                    break;
                case "Manage Notifications":
                    dashboardView.setContentPanel(new NotificationsPanel(this, notificationService.getAllNotifications(), propertyService.getAllPropertiesByType("email")));
                    break;
                case "Sales & Inventory Reports":
                    dashboardView.setContentPanel(new SalesReportPanel(this));
                    break;
                case "Email Settings":
                    dashboardView.setContentPanel(new EmailSettingsPanel(this, propertyService.getAllPropertiesByType("email")));
                    break;
                case "My Assignments":
                    dashboardView.setContentPanel(new MyJobsPanel(this, jobsService.getAllJobsByEmployeeID(dashboardView.getCurrentUser().getEmployeeId())));
                    break;
                default:
                    return;
            }
            dashboardView.setCurrentPanelName(btn);
            dashboardView.getTopBar().setPageTitle(btn);
            dashboardView.getSideBar().setActiveItem(btn);
        }
        paintPanels(dashboardView);
    }


    public boolean crudEmployee(Employee employee, char crudType) {
        try {
            switch (crudType) {
                case 'c':
                    return this.employeeService.addEmployee(employee);
                case 'r':
                    Employee employee_ = this.employeeService.getEmployeeById(employee.getEmployeeId());
                    if(employee_!=null){
                        employee.clone_(employee_);
                        employee.setRole(roleService.getRoleById(employee.getRoleId()));
                    }
                    return employee_ != null;
                case 'u':
                    return this.employeeService.updateEmployee(employee);
                case 'd':
                    return this.employeeService.deleteEmployee(employee.getEmployeeId());
                default:
                    break;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public List<Employee> getAllEmployees() {
        return this.employeeService.getAllEmployees();
    }


    public boolean crudRole(Role role, char crudType) {
        try {
            switch (crudType) {
                case 'c':
                    return this.roleService.addRole(role);
                case 'r':
                    break;
                case 'u':
                    return this.roleService.updateRole(role);
                case 'd':
                    return this.roleService.deleteRole(role.getRoleId());
                default:
                    break;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    public boolean crudCustomer(Customer customer, char crudType) {
        try {
            switch (crudType) {
                case 'c':
                    return this.customerService.addCustomer(customer);
                case 'r':
                    break;
                case 'u':
                    return this.customerService.updateCustomer(customer);
                case 'd':
                    return this.customerService.deleteCustomer(customer.getCustomerId());
                default:
                    break;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public List<Customer> getAllCustomers() {
        return this.customerService.getAllCustomers();
    }

    public boolean crudSupplier(Supplier supplier, char crudType) {
        try {
            switch (crudType) {
                case 'c':
                    return this.supplierService.addSupplier(supplier);
                case 'r':
                    Supplier supplier_ = this.supplierService.getSupplierById(supplier.getSupplierId());
                    if(supplier_ != null){
                        supplier.clone(supplier_);
                    }
                    return supplier_ != null;
                case 'u':
                    return this.supplierService.updateSupplier(supplier);
                case 'd':
                    return this.supplierService.deleteSupplier(supplier.getSupplierId());
                default:
                    break;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public List<Supplier> getAllSuppliers() {
        return this.supplierService.getAllSuppliers();
    }


    public boolean crudPart(Part part, char crudType) {
        try {
            switch (crudType) {
                case 'c':
                    return this.partService.addPart(part);
                case 'r':
                    Part part_ = this.partService.getPartWithRemainingQuantityById(part.getPartId());
                    if(part_!=null){
                        part.clone_(part_);
                    }
                    return part_ != null;
                case 'u':
                    return this.partService.updatePart(part);
                case 'd':
                    return this.partService.deletePart(part.getPartId());
                default:
                    break;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public List<Part> getAllParts() {
        return this.partService.getAllParts();
    }

    public boolean crudInventory(Inventory inventory, char crudType) {
        try {
            switch (crudType) {
                case 'c':
                    return this.inventoryService.addInventory(inventory);
                case 'r':
                    break;
                case 'u':
                    return this.inventoryService.updateInventory(inventory);
                case 'd':
                    return this.inventoryService.deleteInventory(inventory.getInventoryId());
                default:
                    break;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public List<Inventory> getAllInventories() {
        return this.inventoryService.getAllInventories();
    }


    public boolean crudJob(Jobs job, char crudType) {
        try {
            switch (crudType) {
                case 'c':
                    return this.jobsService.addJob(job);
                case 'r':
                    break;
                case 'u':
                    return this.jobsService.updateJob(job);
                case 'd':
                    return this.jobsService.deleteJob(job.getJobId());
                default:
                    break;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public List<Jobs> getAllJobs() {
        return this.jobsService.getAllJobs();
    }

    public boolean crudOrder(Order order, char crudType) {
        try {
            switch (crudType) {
                case 'c':
                    return this.orderService.addOrder(order);
                case 'r':
                    break;
                case 'u':
                    return this.orderService.updateOrder(order);
                case 'd':
                    return this.orderService.deleteOrder(order.getOrderId());
                default:
                    break;
            }
        } catch (Exception e) {
        }
        return false;
    }


    public boolean crudNotification(Notification notification, char crudType) {
        try {
            switch (crudType) {
                case 'c':
                    return this.notificationService.addNotification(notification);
                case 'r':
                    break;
                case 'u':
                    return this.notificationService.updateNotification(notification);
                case 'd':
                    return this.notificationService.deleteNotification(notification.getNotificationId());
                default:
                    break;
            }
        } catch (Exception e) {
        }
        return false;
    }


    public List<Notification> getAllNotifications() {
        return this.notificationService.getAllNotifications();
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

    public void sendJobAssignmentEmail(int employeeId, int jobId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        if(employee==null){
            JOptionPane.showMessageDialog(null, "Employee not found with id : " + employeeId, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String subject = "New Job Assigned ID : " + jobId;
            String message = "Dear " + employee.getTitle() + " " + employee.getFirstName() + ", You have assigned to a new job. JOB ID : " + jobId + ".";
            if(employee.getEmail()==null){
                JOptionPane.showMessageDialog(null, "Employee don't have an email.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            emailService.sendEmail(employee.getEmail(), subject, message);
            JOptionPane.showMessageDialog(null, "Job assignment email sent.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Unable to send job assignment email.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sendJobCompletionEmail(int orderId) {
        Order order = orderService.getOrder(orderId);
        Customer customer = customerService.getCustomerById(order.getCustomerId());
        try {
            String subject = "Job Completed - Order ID : " + orderId;
            String message = "Dear " + customer.getName() + " your repair/repaint job has been completed. - Order ID : " + orderId ;
            if(customer.getEmail()==null){
                JOptionPane.showMessageDialog(null, "Customer don't have an email.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            emailService.sendEmail(customer.getEmail(), subject, message);
            JOptionPane.showMessageDialog(null, "Job completion email sent.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Unable to send job completion email.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public List<Sale> getSalesByRange(String fromDateString, String toDateString) {


        java.sql.Date fromDate = CS.parseSQLDateFromString(fromDateString);
        java.sql.Date toDate = CS.parseSQLDateFromString(toDateString);

        if(fromDate==null || toDate==null){
            return null;
        }
        return salesService.getSalesByRange(fromDate, toDate);
    }

    public List<Property> getAllEmailProperties(){
        return  this.propertyService.getAllPropertiesByType("email");
    }

    public Property getPropertyByKeyAndType(String key, String type) {
        return  this.propertyService.getPropertyByKeyAndType(key, type);
    }

    public boolean addOrUpdateProperties(List<Property> properties) {
        boolean success = this.propertyService.addOrUpdateProperties(properties);
        if(success){
            JOptionPane.showMessageDialog(null, "Settings updates successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        }else {
            JOptionPane.showMessageDialog(null, "Error while updating settings successfully.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return success;
    }
}
