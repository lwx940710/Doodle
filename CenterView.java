import java.awt.*;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.RenderingHints;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;
import java.util.*;

/**
 * Created by lwx on 2017-02-21.
 */
public class CenterView extends JPanel implements Observer {
    private Model model;
    int width, height;
    int start_pts, end_pts;

    CenterView(Model model) {
        this.model = model;
        this.setFocusable(true);
        width = this.getWidth();
        height = this.getHeight();
        //System.out.println("width " + width + "   height " + height);
        start_pts = 0;
        end_pts = 0;
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                int new_width = getWidth();
                double width_ratio = (double) new_width / (double) width;
                int new_height = getHeight();
                double height_ratio = (double) new_height / (double) height;
                for (int i = 0; i < model.getStart_pts(); i++) {
                    ArrayList<Model.ColoredPoint> stroke = model.getStroke(i);
                    for (int j = 0; j < stroke.size(); j++) {
                        Point p = stroke.get(j).getPoint();
                        model.updatePoint(i, j, p.getX() * width_ratio, p.getY() * height_ratio);
                    }
                }
                width = new_width;
                height = new_height;
                model.update();
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                width = getWidth();
                height = getHeight();
                if (model.getPlayback() || model.getRewind()) {
                    model.setPlayback(false);
                    model.setRewind(false);
                    //System.out.println(model.getDrawing_stroke());
                    model.setTime(model.getDrawing_stroke());
                }

                if (model.getTime() != model.getStart_pts()) {
                    model.clearStrokes(model.getTime());
                    model.setStart_pts(model.getTime());
                    start_pts = model.getStart_pts();
                } else {
                    System.out.println("new stroke");

                    model.newStroke();
                }

                Point pt = new Point(e.getX(), e.getY());
                Color color = model.getCurColor();
                start_pts = model.getStart_pts();
                //System.out.println("start pts " + model.getStart_pts());
                model.addtoStroke(pt, color, model.getCurThickness(), start_pts);

                start_pts += 1;
                model.setStart_pts(start_pts);
                model.setTime(start_pts);
                model.update();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                end_pts += 1;
                model.setEnd_pts(end_pts);
                repaint();
            }
        });
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                Point pt = new Point(e.getX(), e.getY());
                Color color = model.getCurColor();
                model.addtoStroke(pt, color, model.getCurThickness(), start_pts-1);
                repaint();
            }
        });

        model.addObserver(this);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (model.getPlayback()) {

            int[] x_points,y_points;
           // System.out.println("playback model time  " + model.getTime());
           // System.out.println("playback drawing stroke  " + model.getDrawing_stroke());
            for (int i = 0; i < model.getDrawing_stroke(); i++) {
                int size = model.getStroke(i).size();
                x_points = new int[size];
                y_points = new int[size];
                for (int j = 0; j < size; j++) {
                    Point p = model.getPoint(model.getStroke(i), j);
                    x_points[j] = (int) p.getX();
                    y_points[j] = (int) p.getY();
                }
                int t = model.getThickness(model.getStroke(i));
                g2d.setStroke(new BasicStroke(t));
                g2d.setColor(model.getColor(model.getStroke(i)));
                g2d.drawPolyline(x_points, y_points, size);
            }
            int size = model.getStroke(model.getDrawing_stroke()).size();
            x_points = new int[size];
            y_points = new int[size];
            for (int j = 0; j < size; j++) {
                Point p = model.getPoint(model.getStroke(model.getDrawing_stroke()), j);
                x_points[j] = (int) p.getX();
                y_points[j] = (int) p.getY();
            }
            int t = model.getThickness(model.getStroke(model.getDrawing_stroke()));
            g2d.setStroke(new BasicStroke(t));
            g2d.setColor(model.getColor(model.getStroke(model.getDrawing_stroke())));
            g2d.drawPolyline(x_points, y_points, model.getPlaying_pts());

        } else if (model.getRewind()) {

            int[] x_points,y_points;
           // System.out.println("rewind model time  " + model.getTime());
           // System.out.println("rewind drawing stroke  " + model.getDrawing_stroke());
            for (int i = 0; i < model.getDrawing_stroke(); i++) {
                int size = model.getStroke(i).size();
                x_points = new int[size];
                y_points = new int[size];
                for (int j = 0; j < size; j++) {
                    Point p = model.getPoint(model.getStroke(i), j);
                    x_points[j] = (int) p.getX();
                    y_points[j] = (int) p.getY();
                }
                int t = model.getThickness(model.getStroke(i));
                g2d.setStroke(new BasicStroke(t));
                g2d.setColor(model.getColor(model.getStroke(i)));
                g2d.drawPolyline(x_points, y_points, size);
            }
            int size = model.getStroke(model.getDrawing_stroke()).size();
            x_points = new int[size];
            y_points = new int[size];
            for (int j = size - 1; j >= 0; j--) {
                Point p = model.getPoint(model.getStroke(model.getDrawing_stroke()), j);
                x_points[j] = (int) p.getX();
                y_points[j] = (int) p.getY();
            }
            int t = model.getThickness(model.getStroke(model.getDrawing_stroke()));
            g2d.setStroke(new BasicStroke(t));
            g2d.setColor(model.getColor(model.getStroke(model.getDrawing_stroke())));
            g2d.drawPolyline(x_points, y_points, model.getPlaying_pts());

        } else {

            int index = model.getStart_pts();
            if (model.getTime() != model.getStart_pts()) {
                index = model.getTime();
            }
            for (int i = 0; i < index; i++) {
                int size = model.getStroke(i).size();
                int[] x_points = new int[size];
                int[] y_points = new int[size];
                for (int j = 0; j < size; j++) {
                    Point p = model.getPoint(model.getStroke(i), j);
                    x_points[j] = (int) p.getX();
                    y_points[j] = (int) p.getY();
                }
                int t = model.getThickness(model.getStroke(i));
                g2d.setStroke(new BasicStroke(t));
                g2d.setColor(model.getColor(model.getStroke(i)));
                g2d.drawPolyline(x_points, y_points, size);
            }
        }
    }


    /**
     * Update with data from the model.
     */
    public void update(Observable o, Object obj) {
        if (model.getJust_new()) {
            start_pts = 0;
        }
        repaint();
    }
}