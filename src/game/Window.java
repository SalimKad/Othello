package game;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    // main function
    public Window() throws InterruptedException {
        Panel panel = new Panel();
        this.add(panel);
        this.setTitle("Jeu d'Othello");
        this.setSize(800,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) throws InterruptedException {

        new Window();
        System.out.println("Hello world!");
    }
}
