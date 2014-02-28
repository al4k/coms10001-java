import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Main visualising class
 *
 */
public class GUI extends GameVisualiser {
	JFrame w;
    JPanel sidebar;
    Box sidebarBox;
	
		GUI(){
			w = new JFrame();
			w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
	
		private JLabel makeMapLabel()
		{
			BufferedImage map = null;
			String filename = mapVisualisable.getMapFilename();
			try {
				map = ImageIO.read(new File(filename));
			} catch (Exception e) {
				System.err.println("Failed to read map filepath");
				System.exit(1);
			}
			return new JLabel(new ImageIcon(map));
		}

        private JButton makeInitButton()
        {
            JButton button = new JButton("Initialise game");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    initialisable.initialiseGame(3);
                    updateGameStatus();
                }
            });
            return button;
        }

        private void updateGameStatus()
        {
            String playerStr;
            Box labels = Box.createVerticalBox();
            for(Integer id : playerVisualisable.getDetectiveIdList())
            {
                playerStr = "Detective "+id+" : node "+playerVisualisable.getNodeId(id);
                labels.add(new JLabel(playerStr));
            }

            for(Integer id : playerVisualisable.getMrXIdList())
            {
                playerStr = "MrX "+id+" : node "+playerVisualisable.getNodeId(id);
                labels.add(new JLabel(playerStr));
            }
            if(sidebarBox.getComponentCount() > 1)
                sidebarBox.remove(1);

            sidebarBox.add(labels);
            sidebar.revalidate();
            sidebar.repaint();
            w.pack();
            /*playerVisualisable.getLocationX();
            playerVisualisable.getLocationY();
            playerVisualisable.getNodeId();*/
        }

		public void run()
		{
            // Setup display layout
            Box display = Box.createHorizontalBox();
            FlowLayout layout = new FlowLayout();
            w.setLayout(layout);

            // Setup sidebar layout
            //JPanel mapPanel = new JPanel();
            sidebar = new JPanel();
            sidebarBox = Box.createVerticalBox();
            sidebarBox.add(makeInitButton());
            sidebar.add(sidebarBox);

            // Add map and sidebar to display
			display.add(makeMapLabel());
            display.add(sidebar);

            // Implement JFrame
            w.add(display);
			w.pack();
			w.setLocationByPlatform(true);
			w.setVisible(true);

            Component wBox = w.getContentPane().getComponent(0);

            for( Component c : ((Container)wBox).getComponents())
            {
                System.out.println(c.getX());

            }
		}
		
		
}
