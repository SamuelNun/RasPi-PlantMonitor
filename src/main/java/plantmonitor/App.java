// Samuel Nunez
// Project: Plant Management System
package plantmonitor;

import javax.swing.*;

public class App {

    public static void main(String[] args) {
        
        /////////////////// SETS UP DISPLAY WINDOW ////////////////////////
         JFrame frame = new JFrame("Plant Monitor");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setSize(800, 480);
       
         //////////// CALLS DASHBOARD /////////////////
         frame.add(new DashboardPanel());
         frame.setVisible(true);
    }
}
