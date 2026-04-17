package views.panels;

import services.ResourceHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BackgroundPanel extends JPanel {
    private static final Color OVERLAY_COLOR = new Color(255, 255, 255, 178);
    private final Image backgroundImage;

    public BackgroundPanel() {
        setOpaque(true);
        backgroundImage = ResourceHelper.loadImage(
                getClass(),
                "/resources/images/dashboard2.png",
                "resources/images/dashboard2.png",
                "GreenLoopSystem/resources/images/dashboard2.png"
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            GradientPaint paint = new GradientPaint(0, 0, new Color(224, 242, 230), 0, getHeight(), new Color(198, 226, 201));
            g2.setPaint(paint);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        g2.setColor(OVERLAY_COLOR);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
}
