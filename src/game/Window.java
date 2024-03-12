package game;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    // main function
    public Window()
    {
        Panel panel = new Panel();
        this.add(panel);
        this.setTitle("Jeu d'Othello");
        this.setSize(600,400);
        //this.pack(); //définir taille composants
        // Close operation
        this.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);
        // Make the frame visible
        this.setVisible(true);
    }

    public static void main(String[] args) {

        new Window();
        System.out.println("Hello world!");
    }
}
