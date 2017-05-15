/**
 * Created by lwx on 2017-02-21.
 */


import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Object;

public class BotView extends JPanel implements Observer {
    private Model model;
    JSlider slider;
    JButton playback;
    JButton rewind;
    JButton start;
    JButton end;
    Timer timer;
    int i, j;

    BotView(Model model) {
        this.model = model;
        i = 0;
        j = 0;
        this.timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getPlayback()) {
                    if (i == model.getStart_pts()) {
                        model.setPlayback(false);
                        timer.stop();
                        return;
                    }
                    slider.setValue(i);
                    model.setDrawing_stroke(i);
                    if (j < model.getStroke(i).size()) {
                        model.setPlaying_pts(j);
                        j += 1;

                    } else {
                        i += 1;
                        j = 0;
                        slider.setValue(i);
                        repaint();
                    }
                    model.update();
                } else if (model.getRewind()) {
                    if (i < 0) {
                        model.setRewind(false);
                        timer.stop();
                        return;
                    }
                    slider.setValue(i);
                    model.setDrawing_stroke(i);
                    if (j >= 0) {
                        model.setPlaying_pts(j);
                        j -= 1;

                    } else {
                        i -= 1;
                        if (i < 0) {
                            model.setRewind(false);
                            timer.stop();
                            return;
                        }
                        j = model.getStroke(i).size();
                        slider.setValue(i);
                        repaint();
                    }
                    model.update();
                } else {
                    timer.stop();
                }
            }
        });
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        playback = new JButton("Play");
        if (model.getStart_pts() == 0) {
            playback.setEnabled(false);
        }
        playback.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getRewind()) {
                    model.setRewind(false);
                }
                model.setTime(slider.getValue());
                model.setPlayback(true);
                i = model.getTime();
                j = 0;
                timer.start();
                // model.update();
            }
        });
        this.add(playback);

        rewind = new JButton("Rewind");
        if (model.getStart_pts() == 0) {
            rewind.setEnabled(false);
        }
        rewind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getPlayback()) {
                    model.setPlayback(false);
                }
                model.setTime(slider.getValue());
                model.setRewind(true);
                i = model.getTime() - 1;
                j = model.getStroke(i).size();
                timer.start();
            }
        });
        this.add(rewind);

        slider = new JSlider(0, model.getTotal_points());
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.setMajorTickSpacing(1);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (slider.getValueIsAdjusting()) {
                    //System.out.println("slider value  " + slider.getValue());
                    if (model.getPlayback()) {
                        model.setPlayback(false);
                    }
                    model.setTime(slider.getValue());
                    model.update();
                }
            }
        });
        this.add(slider);

        start = new JButton("Start");
        if (slider.getValue() == 0) {
            start.setEnabled(false);
        }
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getPlayback()) {
                    model.setPlayback(false);
                }
                model.setTime(0);
                slider.setValue(0);
                model.update();
            }
        });
        this.add(start);

        end = new JButton("End");
        if (slider.getValue() == model.getStart_pts()) {
            end.setEnabled(false);
        }
        end.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getPlayback()) {
                    model.setPlayback(false);
                }
                model.setTime(model.getStart_pts());
                slider.setValue(model.getStart_pts());
                model.update();
            }
        });
        this.add(end);

        model.addObserver(this);
    }



    /**
     * Update with data from the model.
     */
    @Override
    public void update(Observable o, Object obj) {
        //System.out.println(model.getAllStrokes().size());
        if (slider.getValue() == 0) {
            start.setEnabled(false);
        } else {
            start.setEnabled(true);
        }
        if (slider.getValue() == model.getStart_pts()) {
            end.setEnabled(false);
        } else {
            end.setEnabled(true);
        }

        if (model.getStart_pts() > 0) {
            playback.setEnabled(true);
            rewind.setEnabled(true);
        } else {
            playback.setEnabled(false);
            rewind.setEnabled(false);
        }

        if (slider.getValue() == 0) {
            rewind.setEnabled(false);
        }
        //System.out.println("slider value  " + slider.getValue() + "  start pts  " + model.getStart_pts());
        if (slider.getValue() == model.getStart_pts()) {
            playback.setEnabled(false);
        }

        this.slider.setMaximum(model.getStart_pts());

        if (!model.getPlayback() && !model.getRewind()) {
            this.slider.setValue(model.getTime());
            if (slider.getValue() == 0) {
                rewind.setEnabled(false);
            }
            //System.out.println("slider value  " + slider.getValue() + "  start pts  " + model.getStart_pts());
            if (slider.getValue() == model.getStart_pts()) {
                playback.setEnabled(false);
            }

            if (slider.getValue() == 0) {
                start.setEnabled(false);
            } else {
                start.setEnabled(true);
            }
            if (slider.getValue() == model.getStart_pts()) {
                end.setEnabled(false);
            } else {
                end.setEnabled(true);
            }
        }
    }
}
