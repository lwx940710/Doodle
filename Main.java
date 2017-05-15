import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("DOODLE");
        Model model = new Model(frame);
        

        // Set up the window.
        frame.setTitle("DOODLE");
        frame.setMinimumSize(new Dimension(128, 128));
        frame.setSize(new Dimension(800, 800));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        View view = new View(model);
        LeftView left = new LeftView(model);
        BotView but = new BotView(model);
        CenterView center = new CenterView(model);

        model.notifyObservers();

        frame.setJMenuBar(view.getResult());
        frame.add(left, BorderLayout.WEST);
        frame.add(but, BorderLayout.SOUTH);
        frame.add(center, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
