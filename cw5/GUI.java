import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * Main visualising class
 *
 */
public class GUI extends GameVisualiser {
	JFrame w;
	JPanel panel;
	
		GUI(){
			w = new JFrame();
			w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            panel = new JPanel();
		}
	
		private void addMapToPanel(JPanel panel)
		{
			BufferedImage map = null;
			String filename = mapVisualisable.getMapFilename();
			try {
				map = ImageIO.read(new File(filename));
			} catch (Exception e) {
				System.err.println("Failed to read map filepath");
				System.exit(1);
			}
			JLabel mapLabel = new JLabel(new ImageIcon(map));
			panel.add(mapLabel);
		}

        private void addInitButtonToPanel()
        {

        }

		public void run()
		{
            FlowLayout layout = new FlowLayout();
            w.setLayout(layout);

            JButton button = new JButton("Initialise game");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    initialisable.initialiseGame(3);
                }
            });

			addMapToPanel(panel);
			w.add(panel);
            w.add(button);

			w.pack();
			w.setLocationByPlatform(true);
			w.setVisible(true);
			//entry point for implementing your graphics window
		}
		
		
}
