
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Object;
import java.util.*;

public class View extends JFrame implements Observer {

    private Model model;
    private JMenuBar menubar;
    private JMenuItem save;
    /**
     * Create a new View.
     */
    public View(Model model) {

        menubar = new JMenuBar();
        JMenu file = new JMenu("File");

        // Create a new doodle button
        JMenuItem create = new JMenuItem("New");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getAllStrokes().size() > 0) {
                    int result = JOptionPane.showConfirmDialog(model.getFrame(),
                            "Do you want to save current doodle? Any unsaved changes will be lost", "New",
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        if (!model.save()) {
                            JOptionPane.showMessageDialog(model.getFrame(), "Save failed!");
                            return;
                        }
                    }
                }
                model.setJust_new(true);
                model.clearAll();
                model.newArr_stroke();
                model.newStroke();
                model.update();
                model.setJust_new(false);
            }
        });

        // Save button
        save = new JMenuItem("Save");
        if (model.getStart_pts() == 0) {
            save.setEnabled(false);
        }
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.save()) {
                    JOptionPane.showMessageDialog(model.getFrame(), "Saved!");
                } else {
                    JOptionPane.showMessageDialog(model.getFrame(), "Save failed!");
                }
            }
        });

        // Load button
        JMenuItem load = new JMenuItem("Load");
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getAllStrokes().size() > 0) {
                    int result = JOptionPane.showConfirmDialog(model.getFrame(),
                            "Do you want to save current doodle? Any unsaved changes will be lost", "New",
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        if (!model.save()) {
                            JOptionPane.showMessageDialog(model.getFrame(), "Save failed!");
                            return;
                        }
                    }
                }
                if (model.load()) {
                    JOptionPane.showMessageDialog(model.getFrame(), "Loaded!");
                } else {
                    JOptionPane.showMessageDialog(model.getFrame(), "Load failed!");
                }

            }
        });

        // Exit button
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        file.add(create);
        file.add(save);
        file.add(load);
        file.add(exit);

        menubar.add(file);
        //this.setJMenuBar(menubar);

        // Hook up this observer so that it will be notified when the model
        // changes.
        this.model = model;
        model.addObserver(this);
        //setVisible(true);

    }

    public JMenuBar getResult() {
        return menubar;
    }

    /**
     * Update with data from the model.
     */
    public void update(Observable o, Object obj) {
        // XXX Fill this in with the logic for updating the view when the model
        // changes.
        if (model.getStart_pts() == 0) {
            save.setEnabled(false);
        } else {
            save.setEnabled(true);
        }
        System.out.println("Model changed!");
    }
}
