// Samuel Nunez
// Project: Plant Management System
package plantmonitor;

import javax.swing.*;

public class App {

    public static void main(String[] args) {
        
         JFrame frame = new JFrame("Plant Monitor");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setSize(800, 480);
         
         frame.add(new DashboardPanel());
         
         frame.setVisible(true);
    }
}
