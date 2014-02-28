import javax.swing.text.Position;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Class that will hold the state of the game. This is the class that will need
 * to implement the interfaces that we have provided you with
 */
public class GameState implements MapVisualisable, PlayerVisualisable, Initialisable {
	
	/**
	 * Vairable that will hold the filename for the map
	 */
	private String mapFilename;
    Player[] players;
    HashMap<Integer, Point> positions;
	
	/**
	 * Concrete implementation of the MapVisualisable getMapFilename function
	 * @return The map filename
	 */
	public String getMapFilename()
	{
		return mapFilename;
	}

	public void setMapFilename(String filename)
	{
		mapFilename = filename;
	}

    private void setPositions()
    {
        PositionReader p = new PositionReader();
        try {
            p.read("pos.txt");
        } catch (Exception e) {
            System.err.print("Error reading pos.txt");
        }
        positions = p.positions();
    }

	private int randomNode(List<Integer> exclude) {
		Random r = new Random();
		int nodeNo = r.nextInt(200);
		while(exclude.contains(nodeNo))
		{
			nodeNo = r.nextInt(200);
		}
		return nodeNo;
	}

	public Boolean initialiseGame(Integer numberOfDetectives) {
        try
        {
            // Reads pos.txt and renders positions for nodes
            setPositions();

            // Clear all game resources
            setMapFilename("");
            players = new Player[numberOfDetectives + 1];
            List<Integer> startNodes = new ArrayList<Integer>();
            int[] detectiveTickets = {8, 10, 4, 0, 0};
            int[] mrXTickets =       {3, 4, 3, 2, numberOfDetectives};

            //Initialise detectives
            for(int i=0; i<numberOfDetectives; i++)
            {
                int node = randomNode(startNodes);
                players[i] = new Player(i, detectiveTickets, node);
                startNodes.add(node);
            }

            //Initialise Mr X
            Player misterX = new Player(numberOfDetectives + 1, mrXTickets , randomNode(startNodes));
            System.out.print("GAME SETUP COMPLETE");
            return true;
        }
        catch (Exception e)
        {
            System.err.println("Error initialising game");
        }
		return false;
	}

    public Integer getLocationX(Integer nodeId) {
        return (int) positions.get(nodeId).getX();
    }

    public Integer getLocationY(Integer nodeId) {
        return (int) positions.get(nodeId).getY();
    }

    public List<Integer> getDetectiveIdList() {
        return null;
    }

    public List<Integer> getMrXIdList() {
        return null;
    }

    public Integer getNodeId(Integer playerId) {
        return players[playerId].getNode();
    }
}