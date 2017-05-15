/**
 * Created by lwx on 2017-02-21.
 */


import java.awt.image.BufferedImage;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;
import java.util.*;

public class LeftView extends JPanel implements Observer {
    private Model model;
    private String color_code[] = {"#000000", "#606060", "#C0C0C0", "#660000",
                                   "#FF0000", "#336600", "#FFFF00", "#006600", "#00FF00",
                                   "#009999", "#00FFFF", "#000066", "#0000FF", "#660066",
                                   "#FF00FF", "#999900", "#FFFF66", "#193300", "#00FF80"};
    JButton preColor;
    Color select;
    JPanel color_panel;
    JPanel thickness_panel;

    LeftView(Model model) {
        this.model = model;
        this.setLayout(new GridLayout(2, 1));
        preColor = null;
        select = Color.BLACK;

        color_panel = new JPanel();
        thickness_panel = new JPanel();
        color_panel.setLayout(new GridLayout(11, 2));
        thickness_panel.setLayout(new GridLayout(5, 0));

        for (int i = 0; i < 19; i++) {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(10, 10));
            Color current = Color.decode(color_code[i]);
            button.setBackground(current);
            button.setForeground(current);
            button.setBorder(new LineBorder(Color.WHITE, 1));
            button.setOpaque(true);
            if (i == 0) {
                preColor = button;
                button.setBorder(BorderFactory.createLoweredBevelBorder());
            }

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateCurButton(button);
                    Color current = button.getForeground();
                    repaintThickness(current, thickness_panel, color_panel);
                    model.setCurColor(current);
                }
            });
            color_panel.add(button);
        }


        JButton more_color = new JButton("More colors");
        more_color.setBackground(Color.WHITE);
        more_color.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                select = JColorChooser.showDialog(null, "More Colors", Color.BLACK);
                repaintThickness(select, thickness_panel, color_panel);
                model.setCurColor(select);
            }
        });
        color_panel.add(more_color);
        this.add(color_panel);


        for (int i = 0; i < 5; i++) {
            int t = 1;
            model.setCurThickness(1);
            if (i > 0) {
                t = i * 2;
            }
            JButton button = new JButton();
            Color stroke_color = model.getCurColor();
            button.setPreferredSize(new Dimension(30, 30));
            button.setBackground(Color.WHITE);
            button.setForeground(stroke_color);
            BufferedImage rectImg = new BufferedImage(30, t, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = rectImg.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(stroke_color);
            g2d.fillRect(0, 0, 30, t);
            g2d.dispose();
            button.setIcon(new ImageIcon(rectImg));
            int th = t;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    model.setCurThickness(th);
                }
            });

            thickness_panel.add(button);
        }
        this.add(thickness_panel);

        model.addObserver(this);
    }

    private void updateCurButton(JButton curPressed) {
        preColor.setBorder(new LineBorder(Color.WHITE, 1));
        curPressed.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        preColor = curPressed;
    }

    private void repaintThickness(Color curColor, JPanel thickness_panel, JPanel color_panel) {
        JButton more_c = (JButton) color_panel.getComponent(19);
        more_c.setForeground(curColor);
        color_panel.remove(19);
        color_panel.add(more_c, 19);
        this.remove(color_panel);
        this.add(color_panel);

        for (int i = 0; i < 5; i++) {
            int t = 1;
            if (i > 0) {
                t = i * 2;
            }
            JButton but = (JButton) thickness_panel.getComponent(i);
            if (curColor.getRed() == 255 && curColor.getGreen() == 255 && curColor.getBlue() == 255) {
                but.setBackground(Color.BLACK);
            }
            but.setForeground(curColor);
            but.setIcon(null);
            BufferedImage rectImg = new BufferedImage(30, t, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = rectImg.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(curColor);
            g2d.fillRect(0, 0, 30, t);
            g2d.dispose();
            but.setIcon(new ImageIcon(rectImg));
            thickness_panel.remove(i);
            thickness_panel.add(but, i);
        }
        this.remove(thickness_panel);
        this.add(thickness_panel);

        repaint();
    }

    /**
     * Update with data from the model.
     */
    public void update(Observable o, Object obj) {
        if (model.getJust_new()) {
            updateCurButton((JButton) color_panel.getComponent(0));
            repaintThickness(Color.BLACK, thickness_panel, color_panel);
        }
        repaint();
    }
}
