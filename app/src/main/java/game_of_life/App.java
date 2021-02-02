package game_of_life;

import javax.swing.*;

public class App {
    
    public static void main(String[] args) {
        
        System.out.println("basics.collections.Main thread name: " + Thread.currentThread().getName() + " started.");
        
        SwingUtilities.invokeLater(GameOfLife::new);
        
    }
    
}
