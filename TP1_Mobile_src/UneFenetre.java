package TP1_Mobile_src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

class UneFenetre extends JFrame {
    UnMobile sonMobile1, sonMobile2;
    private final int LARG = 400, HAUT = 250;
    private Thread telThread1, telThread2;
    private boolean isRunning1 = true, isRunning2 = true;

    public UneFenetre() {
        super("Mobile");
        Container telConteneur = getContentPane();
        telConteneur.setLayout(new GridLayout(2, 2));

        sonMobile1 = new UnMobile(LARG, HAUT);
        JButton sonBouton1 = new JButton("Start/Stop");
        sonBouton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunning1) {
                    telThread1.suspend();
                } else {
                    telThread1.resume();
                }
                isRunning1 = !isRunning1;
            }
        });

        sonMobile2 = new UnMobile(LARG, HAUT);
        JButton sonBouton2 = new JButton("Start/Stop");
        sonBouton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunning2) {
                    telThread2.suspend();;
                } else {
                    telThread2.resume();
                }
                isRunning2 = !isRunning2;
            }
        });

        telConteneur.add(sonBouton1);
        telConteneur.add(sonMobile1);
        telConteneur.add(sonBouton2);
        telConteneur.add(sonMobile2);

        telThread1 = new Thread(sonMobile1);
        telThread2 = new Thread(sonMobile2);
        telThread1.start();
        telThread2.start();

        setSize(LARG, HAUT);
        setVisible(true);
    }
}