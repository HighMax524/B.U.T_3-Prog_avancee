package TP1_Mobile_src;

import java.awt.*;
import javax.swing.*;

class UneFenetre extends JFrame 
{
    UnMobile sonMobile;
    JButton sonBouton;
    private final int LARG=400, HAUT=250;
    
    public UneFenetre()
    {
    super("le mobile");
    Container leContainer = getContentPane();
    sonMobile = new UnMobile(LARG, HAUT);
    sonBouton = new JButton("Start/Stop");
    sonBouton.setPreferredSize(new Dimension(10, 10));
    leContainer.add(sonMobile);
    leContainer.add(sonBouton);
    Thread laTache = new Thread(sonMobile);
    laTache.start();
    setSize(LARG, HAUT);
    setVisible(true);
    }
}
