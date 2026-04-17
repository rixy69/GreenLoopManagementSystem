package services;


import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import views.FooterPanel;

//Common Service
public interface CS {

    static void drawVerticalDoubleLine(Graphics g, int x1, int x2, int y1, int y2){
        g.setColor(Color.decode("#7A8A99"));
        g.drawLine(x1, y1, x2, y2);
        g.setColor(Color.decode("#FFFFFF"));
        g.drawLine(x1+1, y1, x2+1, y2);
    }
    static void drawShaedBorder(Graphics g , Rectangle r){
        g.setColor(Color.decode("#FFFFFF"));
        g.drawRect(r.x+1, r.y+1, r.width, r.height+1);
        g.setColor(Color.decode("#7A8A99"));
        g.drawRect(r.x, r.y, r.width, r.height);
        g.setColor(Color.decode("#EEEEEE"));
        g.drawLine(r.x+1, r.y+11, r.x+1, r.y+1 + r.height);
    }

    static JLabel paintLabels(JPanel panel, String text){
        String s = "    ";
        JLabel jLabel = new JLabel(s + text);
        jLabel.setBackground(Color.decode("#AAAAAA"));
        jLabel.setForeground(Color.WHITE);
        jLabel.setOpaque(true);
        panel.add(jLabel);
        return jLabel;
    }

    static JLabel paintLabels(JFrame frame, String pre, String text, String bg, String fg, int align, boolean opaque) {
        JLabel jLabel = new JLabel(pre + text);
        if (bg != null) jLabel.setBackground(Color.decode(bg));
        if (fg != null) jLabel.setForeground(Color.decode(fg));
        jLabel.setHorizontalAlignment(align);
        jLabel.setOpaque(opaque);
        frame.add(jLabel);
        return jLabel;
    }

    static JLabel paintLabels(JPanel panel, String pre, String text, String bg, String fg, int align, boolean opaque) {
        JLabel jLabel = new JLabel(pre + text);
        if (bg != null) jLabel.setBackground(Color.decode(bg));
        if (fg != null) jLabel.setForeground(Color.decode(fg));
        jLabel.setHorizontalAlignment(align);
        jLabel.setOpaque(opaque);
        panel.add(jLabel);
        return jLabel;
    }

    static FooterPanel paintFooter(JPanel panel) {
        FooterPanel footer = new FooterPanel(new Color(70, 70, 70), new Color(255, 255, 255, 210));
        panel.add(footer);
        return footer;
    }

    static FooterPanel paintFooter(JFrame frame) {
        FooterPanel footer = new FooterPanel(new Color(70, 70, 70), new Color(255, 255, 255, 210));
        frame.add(footer);
        return footer;
    }

    static java.sql.Date parseSQLDateFromString(String dateString){
        try {
            LocalDate localDate = LocalDate.parse(dateString);
            Date sqlDate = java.sql.Date.valueOf(localDate);
            return sqlDate;
        }catch (DateTimeParseException dtpe){
            JOptionPane.showMessageDialog(null, "Please very your From Date and To Date inputs. " + dtpe.getMessage(), "Incorrect Date Values!", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception e){
            System.out.println(e.getClass());
            e.printStackTrace();

        }
        return null;
    }

}
