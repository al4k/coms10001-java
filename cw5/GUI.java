import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

/**
 * Main visualising class
 *
 */
public class GUI extends GameVisualiser {
	private JFrame w;
    private JPanel sidebar;
    private Box statusBox;
    private Graphics2D gMap; // Use gMap to draw on the map
    private JPanel mapPanel;
    private Image img; // Original map image. Do not use directly, create a BufferedImage copy.

		GUI()
		{
			w = new JFrame();
			w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

		private Image readMapImage()
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
		
		private void throwInitialisationPopup()
		{
			// Query number of players
        	String[] options = {"1", "2", "3", "4", "5"};
            String ticket = (String) JOptionPane.showInputDialog(w,
            		"Select the number of detectives",
            		"Game startup",
                    JOptionPane.DEFAULT_OPTION,
                    null,
                    options,
                    options[0]);
            initialisable.initialiseGame( Integer.parseInt(ticket) );
		}
		
        private JButton makeInitButton()
        {
            JButton button = new JButton("Initialise game");
            button.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                	throwInitialisationPopup();
                    updateGameStatus();
                }
            });
            return button;
        }
        
        private JButton makeSaveButton()
        {
        	JButton button = new JButton("Save game");
            button.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                	saveGame();
                }
            });
            return button;
        }

        private JButton makeLoadButton()
        {
        	JButton button = new JButton("Load game");
            button.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                	loadGame();
                	updateGameStatus();
                }
            });
            return button;
        }
        
        private Box makeCurrentPlayerLabels()
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

        private Box makeMrXStatusLabels()
        {
            Box mrXLabels = Box.createVerticalBox();
            mrXLabels.setBorder(new EmptyBorder(10,0,10,0));

            java.util.List<Integer> mrXIds = new ArrayList<Integer>( visualisable.getMrXIdList() );
            for(int id : mrXIds)
            {
                mrXLabels.add(new JLabel("Mr X details"));
                mrXLabels.add(new JLabel("~~Tickets available~~"));
                mrXLabels.add(new JLabel("Bus: "+visualisable.getNumberOfTickets(Initialisable.TicketType.Bus, id)));
                mrXLabels.add(new JLabel("Taxi: "+visualisable.getNumberOfTickets(Initialisable.TicketType.Taxi, id)));
                mrXLabels.add(new JLabel("Underground: "+visualisable.getNumberOfTickets(Initialisable.TicketType.Underground, id)));
                mrXLabels.add(new JLabel("Double move: "+visualisable.getNumberOfTickets(Initialisable.TicketType.DoubleMove, id)));
                mrXLabels.add(new JLabel("Secret move: "+visualisable.getNumberOfTickets(Initialisable.TicketType.SecretMove, id)));
                mrXLabels.add(new JLabel("~~Previous moves~~"));

                if( !visualisable.getMoveList(id).isEmpty() )
                {
                    mrXLabels.add(new JLabel(visualisable.getMoveList(id).toString()));
                }
                else
                {
                    mrXLabels.add(new JLabel("no moves made"));
                }
            }
            return mrXLabels;
        }

        private void drawCircle(Graphics g, int x, int y, int radius) {
        	g.drawOval(x-(radius/2), y-(radius/2), radius, radius);
        }
        
        private void drawText(Graphics g, int x, int y, String text) {
        	g.drawString(text, x, y);
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
            
            gMap.setStroke(new BasicStroke(3));
            Font f = new Font("", Font.BOLD, 14);
            Color c = Color.BLUE;
            gMap.setColor(c);
            gMap.setFont(f);
            for(Integer id : playerVisualisable.getDetectiveIdList())
            {
                labels.add(new JLabel("Detective id"+id+" : node "+playerVisualisable.getNodeId(id)));
                int x = playerVisualisable.getLocationX(playerVisualisable.getNodeId(id));
                int y = playerVisualisable.getLocationY(playerVisualisable.getNodeId(id));
                drawCircle(gMap, x, y, 20);
                
                // Fancy paint to highlight current player
                if(visualisable.getNextPlayerToMove() == id)
                {
                	gMap.setFont( new Font("", Font.BOLD, 20) );
                	drawText(gMap, x+12, y+12, ">p"+id);
                	gMap.setColor(Color.WHITE);
                	drawCircle(gMap, x, y, 26);
                	gMap.setColor(c);
                	gMap.setFont(f);
                }
                else
                {
                	drawText(gMap, x+12, y+12, "p"+id);
                }  
            }

            // Repaint MrX nodes
            c = Color.RED;
            gMap.setColor(c);
            for(Integer id : playerVisualisable.getMrXIdList())
            {
                labels.add(new JLabel("MrX id"+id+" : node "+playerVisualisable.getNodeId(id)));
                int x = playerVisualisable.getLocationX(playerVisualisable.getNodeId(id));
                int y = playerVisualisable.getLocationY(playerVisualisable.getNodeId(id));
                drawCircle(gMap, x, y, 20);
                
                if(visualisable.getNextPlayerToMove() == id) {
                	gMap.setFont( new Font("", Font.BOLD, 20) );
                	drawText(gMap, x+12, y+12, ">p"+id);
                	gMap.setColor(Color.WHITE);
                	drawCircle(gMap, x, y, 26);
                	gMap.setColor(c);
                	gMap.setFont(f);
                } else {
                	drawText(gMap, x+12, y+12, "p"+id);
                }  
            }

        	// Remove previous labels
            if(statusBox.getComponentCount() > 2)
            {
            	statusBox.remove(0);
            	statusBox.remove(0);
            	statusBox.remove(0);
            }
            
            // Repack
            statusBox.add(labels);
            //Add ticket details for current players
            statusBox.add(makeCurrentPlayerLabels());
            // Add ticket/move details for mr x
            statusBox.add(makeMrXStatusLabels());
            statusBox.revalidate();
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
        
        private final Initialisable.TicketType[] moveOptions()
        {
        	// Create array of valid tickets for current player
        	java.util.List<Initialisable.TicketType> options = new ArrayList<Initialisable.TicketType>();
        	options.add(Initialisable.TicketType.Bus);
        	options.add(Initialisable.TicketType.Taxi);
            options.add(Initialisable.TicketType.Underground);
            if(visualisable.getMrXIdList().contains( visualisable.getNextPlayerToMove() ))
            {
            	options.add(Initialisable.TicketType.DoubleMove);
            	options.add(Initialisable.TicketType.SecretMove);
            }
            return options.toArray(new Initialisable.TicketType[1]);
        }

        private void throwMovePopup(final int fromNode)
        {
        	// Display move-type selection query
        	Initialisable.TicketType[] options = moveOptions();
            int ticket = JOptionPane.showOptionDialog(w, //Component parentComponent
                    "Ticket type",                          //Object message,
                    "Select a transport method",            //String title
                    JOptionPane.DEFAULT_OPTION,             //int optionType
                    JOptionPane.INFORMATION_MESSAGE,        //int messageType
                    null,                                   //Icon icon,
                    options,                                //Object[] options,
                    options[1]);                            //Object initialValue

            makeMove(fromNode, options[ticket]);
        }
        
        private void makeMove(Integer nodeId, Initialisable.TicketType type)
        {
        	boolean success = controllable.movePlayer(visualisable.getNextPlayerToMove(), nodeId, type);
            if(success) {
                updateGameStatus();
                if ( visualisable.isGameOver() ) {
                	String message = "Game Over";
                	if (visualisable.getDetectiveIdList().contains( visualisable.getWinningPlayerId() ) ) {
                		message += ", Detectives won!";
                	} else
                		message += ", Mr X won!";
                	JOptionPane.showMessageDialog(null, message);
                	System.exit(0);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid move. Try again");
            }
        }
        
        private void throwGameOverPopup(final boolean detectivesWin)
        {
        	
        }
        
        private boolean saveGame()
        {
        	// Prompt user to choose file location
        	JFileChooser fc = new JFileChooser();
    		fc.setCurrentDirectory(new File("/home"));
    		int selection = fc.showSaveDialog(null);
    		if (selection == JFileChooser.APPROVE_OPTION)
    		{
    			return controllable.saveGame(fc.getSelectedFile().getAbsolutePath()+".txt");
    		}
        	return false;
        }
        
        private boolean loadGame()
        {
        	JFileChooser fc = new JFileChooser();
    		fc.setCurrentDirectory(new File("/home"));
    		int selection = fc.showOpenDialog(null);
    		if (selection == JFileChooser.APPROVE_OPTION)
    		{
    			return controllable.loadGame(fc.getSelectedFile().getAbsolutePath());
    		}
        	return false;
        }
        
		public void run()
		{
            // Setup display layout
            Box display = Box.createHorizontalBox();
            FlowLayout layout = new FlowLayout();
            w.setLayout(layout);
            
            // Setup map JPanel
            mapPanel = new JPanel();
        	img = readMapImage();
            JLabel mapLabel = new JLabel(new ImageIcon(createNewMapImage(img)));
            mapPanel.add(mapLabel);
            
            mapPanel.addMouseListener(new MouseListener() {

				public void mouseClicked(MouseEvent e) {
		    		try {
                        int clickedNode = controllable.getNodeIdFromLocation(e.getX(), e.getY());
                        throwMovePopup(clickedNode);
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
            Box sidebarBox = Box.createVerticalBox();
            sidebarBox.add(makeInitButton());
            sidebarBox.add(makeSaveButton());
            sidebarBox.add(makeLoadButton());
            
            statusBox = Box.createVerticalBox();
            sidebarBox.add(statusBox);
            
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
