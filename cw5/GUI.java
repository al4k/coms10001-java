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
	private JFrame w;
    private JPanel sidebar;
    private Box sidebarBox;
    private Graphics2D gMap; // Use gMap to draw on the map
    private JPanel mapPanel;
    private Image img; // Original map image. Do not use directly, create a BufferedImage copy.

		GUI(){
			w = new JFrame();
			w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

		private Image makeMapImage()
		{
			BufferedImage map = null;
			String filename = mapVisualisable.getMapFilename();
			try {
				map = ImageIO.read(new File(filename));
			} catch (Exception e) {
				System.err.println("Failed to read map filepath");
				System.exit(1);
			}
			return map;
		}
		
        private JButton makeInitButton()
        {
            JButton button = new JButton("Initialise game");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	//TODO ########## initialiseGame(numberOfPlayers) argument should be declared elsewhere. set to 3 for testing ###########
                    initialisable.initialiseGame(3);
                    updateGameStatus();
                }
            });
            return button;
        }

        private void drawCircle(Graphics g, int x, int y, int radius)
        {
        	g.drawOval(x-(radius/2), y-(radius/2), radius, radius);
        }
        
        private void updateGameStatus()
        {
            String playerStr;
            Box labels = Box.createVerticalBox();
            
            // Reload map canvas (remove/add JLabel)
            JLabel label = new JLabel(new ImageIcon(createNewMapImage(img)));
            mapPanel.removeAll();
            mapPanel.add(label);
            mapPanel.repaint();
            
            // Repaint Detective nodes
            gMap.setColor(Color.BLUE);
            gMap.setStroke(new BasicStroke(3));
            for(Integer id : playerVisualisable.getDetectiveIdList())
            {
                playerStr = "Detective id"+id+" : node "+playerVisualisable.getNodeId(id);
                labels.add(new JLabel(playerStr));
                int x = playerVisualisable.getLocationX(playerVisualisable.getNodeId(id));
                int y = playerVisualisable.getLocationY(playerVisualisable.getNodeId(id));
                drawCircle(gMap, x, y, 20);
            }

            // Repaint MrX nodes
            gMap.setColor(Color.RED);
            for(Integer id : playerVisualisable.getMrXIdList())
            {
                playerStr = "MrX id"+id+" : node "+playerVisualisable.getNodeId(id);
                labels.add(new JLabel(playerStr));
                int x = playerVisualisable.getLocationX(playerVisualisable.getNodeId(id));
                int y = playerVisualisable.getLocationY(playerVisualisable.getNodeId(id));
                drawCircle(gMap, x, y, 20);
            }
        
            if(sidebarBox.getComponentCount() > 1)
                sidebarBox.remove(1);
            
            sidebarBox.add(labels);
            w.pack();
        }

        // Creates a copy of an image. Sets new Graphics reference.
        private BufferedImage createNewMapImage(Image img)
        {
        	BufferedImage mapCopy = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics g = mapCopy.createGraphics();
            g.drawImage(img, 0, 0, null);
            gMap = (Graphics2D) g;
            return mapCopy;
        }
        
		public void run()
		{
            // Setup display layout
            Box display = Box.createHorizontalBox();
            FlowLayout layout = new FlowLayout();
            w.setLayout(layout);
            
            // Setup map JPanel
            mapPanel = new JPanel();
        	img = makeMapImage();
            JLabel mapLabel = new JLabel(new ImageIcon(createNewMapImage(img)));
            mapPanel.add(mapLabel);
            
            // Setup sidebar JPanel
            sidebar = new JPanel();
            sidebarBox = Box.createVerticalBox();
            sidebarBox.add(makeInitButton());
            sidebar.add(sidebarBox);

            // Add map and sidebar to display
			display.add(mapPanel);
            display.add(sidebar);

            // Implement JFrame
            w.add(display);
			w.pack();
			w.setLocationByPlatform(true);
			w.setVisible(true);
			mapPanel.repaint();
      
		}
		
		
}
