package views;

import models.DashboardModel;
import models.Employee;
import views.panels.BackgroundPanel;
import views.panels.DashboardTheme;
import views.panels.HomePanel;
import views.panels.SideBar;
import views.panels.TopBar;

import javax.swing.*;
import java.awt.BorderLayout;

public class DashboardView extends JFrame {

    private JPanel rootPanel;
    private JPanel mainPanel;
    private SideBar sideBar;
    private JScrollPane sideBarScrollPane;
    private TopBar topBar;
    private JPanel contentHost;
    private JPanel contentPanel;
    private HomePanel homePanel;
    private DashboardModel dashboardModel;
    private String currentPanelName = "Home";


    public DashboardView(DashboardModel dashboardModel){
        this.dashboardModel = dashboardModel;
        this.createUIComponents();
    }
    public DashboardView() {
        this.createUIComponents();
    }

    private void createUIComponents() {

        this.rootPanel = new JPanel(new BorderLayout());
        this.mainPanel = new BackgroundPanel();
        this.mainPanel.setLayout(null);
        this.sideBar = new SideBar(this.dashboardModel.getCurrentUser().getRole().getRoleName());
        this.sideBarScrollPane = new JScrollPane(this.sideBar);
        this.topBar = new TopBar(this.dashboardModel.getCurrentUser());
        this.contentHost = new JPanel(null);
        this.contentHost.setOpaque(false);
        this.homePanel = new HomePanel(this.dashboardModel.getCurrentUser());
        this.contentPanel = this.homePanel;

        this.sideBarScrollPane.setBorder(null);
        this.sideBarScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.sideBarScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.sideBarScrollPane.setOpaque(true);
        this.sideBarScrollPane.getViewport().setOpaque(true);
        this.sideBarScrollPane.setBackground(DashboardTheme.SIDEBAR_GREEN);
        this.sideBarScrollPane.getViewport().setBackground(DashboardTheme.SIDEBAR_GREEN);

        this.mainPanel.add(this.sideBarScrollPane);
        this.mainPanel.add(this.topBar);
        this.mainPanel.add(this.contentHost);
        this.setContentPanel(this.homePanel);
        this.sideBar.setActiveItem("Home");
        this.rootPanel.add(this.mainPanel, BorderLayout.CENTER);
        this.rootPanel.add(new FooterPanel(), BorderLayout.SOUTH);

        this.setContentPane(rootPanel);

        this.setTitle("GreenLoop");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(40,40,1200,860);
    }


    public Employee getCurrentUser() {
        return this.dashboardModel.getCurrentUser();
    }

    public void setCurrentUser(Employee currentUser) {
        if(this.dashboardModel==null){
            this.dashboardModel = new DashboardModel(currentUser);
        }else {
            this.dashboardModel.setCurrentUser(currentUser);
        }
    }

    public DashboardModel getDashboardModel() {
        return dashboardModel;
    }

    public void setDashboardModel(DashboardModel dashboardModel) {
        this.dashboardModel = dashboardModel;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public SideBar getSideBar() {
        return sideBar;
    }

    public void setSideBar(SideBar sideBar) {
        this.sideBar = sideBar;
    }

    public JScrollPane getSideBarScrollPane() {
        return sideBarScrollPane;
    }

    public void setSideBarScrollPane(JScrollPane sideBarScrollPane) {
        this.sideBarScrollPane = sideBarScrollPane;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public void setContentPanel(JPanel contentPanel) {
        if (this.contentHost != null && this.contentPanel != null) {
            this.contentHost.remove(this.contentPanel);
        }
        this.contentPanel = contentPanel;
        if (this.contentPanel != null) {
            this.contentPanel.setOpaque(false);
            DashboardTheme.recursivelyStyle(this.contentPanel);
            this.contentHost.add(this.contentPanel);
            this.contentHost.revalidate();
            this.contentHost.repaint();
        }
    }

    public HomePanel getHomePanel() {
        return homePanel;
    }

    public void setHomePanel(HomePanel homePanel) {
        this.homePanel = homePanel;
    }

    public TopBar getTopBar() {
        return topBar;
    }

    public JPanel getContentHost() {
        return contentHost;
    }

    public String getCurrentPanelName() {
        return currentPanelName;
    }

    public void setCurrentPanelName(String currentPanelName) {
        this.currentPanelName = currentPanelName;
    }
}
