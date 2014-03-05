import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

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
                	//TODO  relocate. set to 3 for testing
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
        
        private String listToString(java.util.List<Initialisable.TicketType> list)
        {
        	String output = "";
        	int i;
        	for(i=0; i<list.size(); i++) {
        		output += list.get(i).name() + ", ";
        	}
        	//output += list.get(i).name();
        	return output;
        }

        private Box currentStatusLabels()
        {
            Box statusLabels = Box.createVerticalBox();
            Integer currentPlayerId = visualisable.getNextPlayerToMove();
            statusLabels.add(new JLabel("Current player: "+currentPlayerId));
            statusLabels.add(new JLabel("~~Tickets available~~"));
            statusLabels.add(new JLabel("Bus: "+visualisable.getNumberOfTickets(Initialisable.TicketType.Bus, currentPlayerId)));
            statusLabels.add(new JLabel("Taxi: "+visualisable.getNumberOfTickets(Initialisable.TicketType.Taxi, currentPlayerId)));
            statusLabels.add(new JLabel("Underground: "+visualisable.getNumberOfTickets(Initialisable.TicketType.Underground, currentPlayerId)));

            if(visualisable.getMrXIdList().contains(currentPlayerId))
            {
                statusLabels.add(new JLabel("Double move: "+visualisable.getNumberOfTickets(Initialisable.TicketType.DoubleMove, currentPlayerId)));
                statusLabels.add(new JLabel("Secret move: "+visualisable.getNumberOfTickets(Initialisable.TicketType.SecretMove, currentPlayerId)));
            }
            return statusLabels;
        }

        private Box mrXStatusLabels()
        {
            Box mrXLabels = Box.createVerticalBox();
            mrXLabels.setBorder(new EmptyBorder(10,0,10,0));

            java.util.List<Integer> mrXIds = new ArrayList<Integer>( visualisable.getMrXIdList() );
            for(int id : mrXIds) {
                mrXLabels.add(new JLabel("Mr X details"));
                mrXLabels.add(new JLabel("~~Tickets available~~"));
                mrXLabels.add(new JLabel("Bus: "+visualisable.getNumberOfTickets(Initialisable.TicketType.Bus, id)));
                mrXLabels.add(new JLabel("Taxi: "+visualisable.getNumberOfTickets(Initialisable.TicketType.Taxi, id)));
                mrXLabels.add(new JLabel("Underground: "+visualisable.getNumberOfTickets(Initialisable.TicketType.Underground, id)));
                mrXLabels.add(new JLabel("Double move: "+visualisable.getNumberOfTickets(Initialisable.TicketType.DoubleMove, id)));
                mrXLabels.add(new JLabel("Secret move: "+visualisable.getNumberOfTickets(Initialisable.TicketType.SecretMove, id)));
                mrXLabels.add(new JLabel("~~Previous moves~~"));

                if(visualisable.getMoveList(id) != null) {
                    mrXLabels.add(new JLabel("previous moves: "+visualisable.getMoveList(id).toString()));
                } else {
                    mrXLabels.add(new JLabel("no moves made"));
                }
            }
            return mrXLabels;
        }

        private void updateGameStatus()
        {   
            // Reload map canvas (remove/add JLabel)
            JLabel label = new JLabel(new ImageIcon(createNewMapImage(img)));
            mapPanel.removeAll();
            mapPanel.add(label);
            mapPanel.repaint();
            
            // Repaint Detective nodes
            Box labels = Box.createVerticalBox();
            labels.setBorder(new EmptyBorder(10,0,10,0));
            
            gMap.setColor(Color.BLUE);
            gMap.setStroke(new BasicStroke(3));
            for(Integer id : playerVisualisable.getDetectiveIdList())
            {
                labels.add(new JLabel("Detective id"+id+" : node "+playerVisualisable.getNodeId(id)));
                int x = playerVisualisable.getLocationX(playerVisualisable.getNodeId(id));
                int y = playerVisualisable.getLocationY(playerVisualisable.getNodeId(id));
                drawCircle(gMap, x, y, 20);
            }

            // Repaint MrX nodes
            gMap.setColor(Color.RED);
            for(Integer id : playerVisualisable.getMrXIdList())
            {
                labels.add(new JLabel("MrX id"+id+" : node "+playerVisualisable.getNodeId(id)));
                int x = playerVisualisable.getLocationX(playerVisualisable.getNodeId(id));
                int y = playerVisualisable.getLocationY(playerVisualisable.getNodeId(id));
                drawCircle(gMap, x, y, 20);
            }

        	// Remove previous labels
            if(sidebarBox.getComponentCount() > 3)
            {
                sidebarBox.remove(1);
                sidebarBox.remove(1);
                sidebarBox.remove(1);
            }
            
            // Repack
            sidebarBox.add(labels);
            //Add ticket details for current players
            sidebarBox.add(currentStatusLabels());
            // Add ticket/move details for mr x
            sidebarBox.add(mrXStatusLabels());
            sidebarBox.revalidate();
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
        
        private void movePopup(final int fromNode)
        {
            Initialisable.TicketType[] options = {Initialisable.TicketType.Bus,
                        Initialisable.TicketType.Taxi,
                        Initialisable.TicketType.Underground};

            int ticket = JOptionPane.showOptionDialog(w, //Component parentComponent
                    "Ticket type",                          //Object message,
                    "Select a transport method",            //String title
                    JOptionPane.DEFAULT_OPTION,             //int optionType
                    JOptionPane.INFORMATION_MESSAGE,        //int messageType
                    null,                                   //Icon icon,
                    options,                                //Object[] options,
                    options[1]);                            //Object initialValue

            move(fromNode, options[ticket]);
        }
        
        private void move(Integer nodeId, Initialisable.TicketType type)
        {
        	boolean success = controllable.movePlayer(visualisable.getNextPlayerToMove(), nodeId, type);
            if(success) {
                updateGameStatus();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid move");
            }
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
            
            mapPanel.addMouseListener(new MouseListener() {

				public void mouseClicked(MouseEvent e) {
		    		try {
                        int clickedNode = controllable.getNodeIdFromLocation(e.getX(), e.getY());
                        movePopup(clickedNode);
					} catch(Exception ex) {
		    			System.err.println("Cannot invoke GameState method {"+ex);
		    		}
					
				}

				public void mouseEntered(MouseEvent arg0) {}
				public void mouseExited(MouseEvent arg0) {}
				public void mousePressed(MouseEvent arg0) {}
				public void mouseReleased(MouseEvent arg0) {}
            });
            
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
