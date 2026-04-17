package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FooterPanel extends JPanel {
    private static final String FOOTER_TEXT = "Powered by CodeX (PVT) LTD";
    private final Color backgroundColor;

    public FooterPanel() {
        this(new Color(238, 238, 238), new Color(0, 0, 0, 90));
    }

    public FooterPanel(Color textColor, Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        setOpaque(false);
        setBorder(new EmptyBorder(8, 12, 8, 12));
        setPreferredSize(new Dimension(10, 34));

        JLabel label = new JLabel(FOOTER_TEXT);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(textColor);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (backgroundColor != null && backgroundColor.getAlpha() > 0) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(backgroundColor);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
        super.paintComponent(g);
    }
}
