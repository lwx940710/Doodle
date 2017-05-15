
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

public class Model extends Observable {
    /** The observers that are watching this model for changes. */
    private ArrayList<Observer> observers;

//    public JFrame frame;
    public class ColoredPoint {
        Point pt;
        Color color;
        int thickness;

        ColoredPoint(Point p, Color c, int t) {
            pt = p;
            color = c;
            thickness = t;
        }

        public Point getPoint() {
            return pt;
        }

        public Color getColor() {
            return color;
        }

        public int getThickness() {
            return thickness;
        }
    }

    //private ArrayList<ColoredPoint> stroke;
    private ArrayList<ArrayList<ColoredPoint>> strokes;
    private Boolean playback, rewind;
    private Color curColor;
    private int curThickness;
    private int time;
    private int start_pts, end_pts;
    private int playing_pts;
    private int drawing_stroke;
    private Boolean just_new;
    private Boolean just_load;
    private JFrame frame;

    public Model(JFrame frame) {
        this.observers = new ArrayList();
        strokes = new ArrayList<ArrayList<ColoredPoint>>();
        playback = false;
        rewind = false;
        curColor = Color.BLACK;
        time = 0;
        start_pts = 0;
        playing_pts = 0;
        drawing_stroke = 0;
        just_new = false;
        this.frame = frame;
        setChanged();
    }


    public ArrayList<ColoredPoint> getStroke(int i) {
        return strokes.get(i);
    }

    public void clearAll() {
        clearStrokes(0);
        playback = false;
        rewind = false;
        curColor = Color.BLACK;
        curThickness = 1;
        time = 0;
        start_pts = 0;
        playing_pts = 0;
        drawing_stroke = 0;
        just_new = false;
        just_load = false;
    }

    public void clearStrokes(int from) {
        for (int i = from; i < strokes.size(); i++) {
            strokes.get(i).clear();
        }
    }

    public ArrayList<ArrayList<ColoredPoint>> getAllStrokes() {
        return strokes;
    }

    public Point getPoint(ArrayList<ColoredPoint> cp, int i) {
        return cp.get(i).getPoint();
    }

    public Color getColor(ArrayList<ColoredPoint> cp) {
        return cp.get(0).getColor();
    }

    public int getThickness(ArrayList<ColoredPoint> cp) {
        return cp.get(0).getThickness();
    }

    public void newArr_stroke() { strokes = new ArrayList<ArrayList<ColoredPoint>>(); }

    public void newStroke() {
        strokes.add(new ArrayList<ColoredPoint>());
    }

    public void addtoStroke(Point p, Color c, int t, int i) {
        ColoredPoint cp = new ColoredPoint(p, c, t);
        strokes.get(i).add(cp);
    }

    public void updatePoint(int i, int j, double new_x, double new_y) {
        strokes.get(i).get(j).getPoint().setLocation(new_x, new_y);
    }

    public Boolean getPlayback() {
        return playback;
    }

    public void setPlayback(Boolean b) {
        playback = b;
    }

    public Boolean getRewind() {
        return rewind;
    }

    public void setRewind(Boolean b) {
        rewind = b;
    }

    public Color getCurColor() {
        return curColor;
    }

    public void setCurColor(Color c) {
        curColor = c;
    }

    public int getCurThickness() {
        return curThickness;
    }

    public void setCurThickness(int t) {
        curThickness = t;
    }

    public int getTotal_points() {
        int total = 0;
        for (int i = 0; i < strokes.size(); i++) {
            total += strokes.get(i).size();
        }
        return total;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int t) {
        time = t;
    }

    public int getStart_pts() {
        return start_pts;
    }

    public void setStart_pts(int sp) {
        start_pts = sp;
    }

    public int getEnd_pts() {
        return end_pts;
    }

    public void setEnd_pts(int sp) {
        end_pts = sp;
    }

    public int getPlaying_pts() {
        return playing_pts;
    }

    public void setPlaying_pts(int pp) {
        playing_pts = pp;
    }

    public int getDrawing_stroke() {
        return drawing_stroke;
    }

    public void setDrawing_stroke(int ds) {
        drawing_stroke = ds;
    }

    public Boolean getJust_new() {
        return just_new;
    }

    public void setJust_new(Boolean jn) {
        just_new = jn;
    }

    public Boolean getJust_load() {
        return just_load;
    }

    public void setJust_load(Boolean jn) {
        just_load = jn;
    }

    public JFrame getFrame() {
        return frame;
    }

    public Boolean save() {
        JFileChooser chooser = new JFileChooser();
        File file = null;
        int result = chooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            String file_name = file.getName();
            if (!file_name.endsWith(".dtxt")) {
                file = new File(file.toString() + ".dtxt");
            }
            chooser.setCurrentDirectory(file.getParentFile());

            if (file != null) {
                file_name = file.getName();
                try {
                    FileWriter fw = new FileWriter(file);
                    BufferedWriter bw = new BufferedWriter(fw);
                    /*
                        private int start_pts;
                        private int time;
                        private int playing_pts;
                        private int drawing_stroke;
                        private Color curColor;
                        private int curThickness;

                        size of strokers;
                        private ArrayList<ArrayList<ColoredPoint>> strokes;

                        ---------- no need to save ----------
                        private Boolean playback;
                        private Boolean just_new;
                     */

                    bw.write(Integer.toString(start_pts));
                    bw.newLine();

                    bw.write(Integer.toString(time));
                    bw.newLine();

                    bw.write(Integer.toString(playing_pts));
                    bw.newLine();

                    bw.write(Integer.toString(drawing_stroke));
                    bw.newLine();

                    bw.write(curColor.getRed()
                                    + " "
                                    + curColor.getGreen()
                                    + " "
                                    + curColor.getBlue());
                    bw.newLine();

                    bw.write(Integer.toString(curThickness));
                    bw.newLine();

                    int size = strokes.size();
                    bw.write(Integer.toString(size));
                    bw.newLine();

                    for (int i = 0; i < size; i++) {
                        int num = strokes.get(i).size();
                        bw.write(Integer.toString(num));
                        bw.newLine();
                        for (int j = 0; j < num; j++) {
                            Point p = strokes.get(i).get(j).getPoint();
                            Color c = strokes.get(i).get(j).getColor();
                            int t = strokes.get(i).get(j).getThickness();
                            bw.write(Double.toString(p.getX())
                                            + " "
                                            + Double.toString(p.getY())
                                            + " "
                                            + c.getRed()
                                            + " "
                                            + c.getGreen()
                                            + " "
                                            + c.getBlue()
                                            + " "
                                            + Integer.toString(t));
                            bw.newLine();
                        }
                    }

                    bw.close();
                    return true;
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        return false;
    }

    public Boolean load() {
        JFileChooser chooser = new JFileChooser();
        File file = null;
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            chooser.setCurrentDirectory(file.getParentFile());
            String file_name = file.getName();
            if (file_name.endsWith(".dtxt")) {
                try {
                    Scanner scanner = new Scanner(file);
                    start_pts = scanner.nextInt();
                    time = scanner.nextInt();
                    playing_pts = scanner.nextInt();
                    drawing_stroke = scanner.nextInt();
                    int r = scanner.nextInt();
                    int g = scanner.nextInt();
                    int b = scanner.nextInt();
                    Color cl = new Color(r, g, b);
                    curColor = cl;
                    curThickness = scanner.nextInt();

                    strokes = new ArrayList<ArrayList<ColoredPoint>>();
                    int size = scanner.nextInt();
                    for (int i = 0; i < size; i++) {
                        newStroke();
                        int num = scanner.nextInt();
                        for (int j = 0; j < num; j++) {
                            double x = scanner.nextDouble();
                            double y = scanner.nextDouble();
                            Point p = new Point();
                            p.setLocation(x, y);
                            r = scanner.nextInt();
                            g = scanner.nextInt();
                            b = scanner.nextInt();
                            Color c = new Color(r, g, b);
                            int t = scanner.nextInt();
                            addtoStroke(p, c, t, i);
                        }
                    }

                    playback = false;
                    just_new = false;
                    just_load = true;
                    update();
                    return true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    public void update() {
        setChanged();
        notifyObservers();
    }

//    /**
//     * Add an observer to be notified when this model changes.
//     */
//    public void addObserver(Observer2 observer) {
//        this.observers.add(observer);
//    }
}
