package com.hack3;

import javax.swing.*;
import java.awt.*;

public class UI {

    private JFrame f;
    private JPanel p;
    private JButton b1;
    private JLabel lab;

    public UI() {
        ui();
    }

    public void ui() {

        f = new JFrame("COVID Caution");
        f.setVisible(true);
        f.setSize(600,400);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        p = new JPanel();
        p.setBackground(Color.WHITE);

        b1 = new JButton("Alert");
        b1.setBackground(Color.YELLOW);
        lab = new JLabel("You have 3 Alerts");

        p.add(b1);
        p.add(lab);

        f.add(p);
    }
}
