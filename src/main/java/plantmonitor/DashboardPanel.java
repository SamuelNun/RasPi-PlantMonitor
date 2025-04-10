
package plantmonitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 1. Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        headerPanel.setBackground(new Color(230, 230, 250));
        JLabel tempLabel = new JLabel("Temp: 80°F");
        JLabel humidLabel = new JLabel("Humidity: 20%");
        JLabel timeLabel = new JLabel("Time: 2:00 PM");

        headerPanel.add(tempLabel);
        headerPanel.add(Box.createHorizontalStrut(220)); // spacing
        headerPanel.add(humidLabel);
        headerPanel.add(Box.createHorizontalStrut(220)); // spacing
        headerPanel.add(timeLabel);

        add(headerPanel, BorderLayout.NORTH);

        // 2. Grid for Plants (2 rows x 3 columns)
        JPanel plantGrid = new JPanel(new GridLayout(2, 3, 10, 10));
        plantGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        plantGrid.setBackground(Color.WHITE);

        // 3. Add Plant Panels
        for (int i = 1; i <= 5; i++) {
            plantGrid.add(createPlantPanel("Plant " + i));
        }

        // 4. Create New + Panel
        JPanel createNewPanel = new JPanel();
        createNewPanel.setLayout(new BorderLayout());
        createNewPanel.setBackground(new Color(240, 240, 240));
        JLabel plus = new JLabel("Create New +", SwingConstants.CENTER);
        plus.setFont(new Font("Arial", Font.BOLD, 14));
        createNewPanel.add(plus, BorderLayout.CENTER);
        plantGrid.add(createNewPanel);

        add(plantGrid, BorderLayout.CENTER);
    }

    private JPanel createPlantPanel(String name) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setBackground(new Color(245, 255, 250));

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        JLabel tempLabel = new JLabel("Temp: 72°F", SwingConstants.CENTER);
        JLabel humidLabel = new JLabel("Humidity: 30%", SwingConstants.CENTER);

        panel.add(nameLabel);
        panel.add(tempLabel);
        panel.add(humidLabel);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(name + " clicked. Navigate to detail page.");
                // You can trigger screen change here
            }
        });

        return panel;
    }
}