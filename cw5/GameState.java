import javax.swing.text.Position;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Class that will hold the state of the game. This is the class that will need
 * to implement the interfaces that we have provided you with
 */
public class GameState implements MapVisualisable, PlayerVisualisable, Initialisable, Visualisable, Controllable {
	
	/**
	 * Vairable that will hold the filename for the map
	 */
	private String mapFilename;
    private Player[] players;
    private HashMap<Integer, Point> positions;
    private Player whoseTurn;
	private Graph graph;

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

    private void setGraph()
    {
        Reader r = new Reader();
        try {
            r.read("graph.txt");
        } catch (Exception e) {
            System.err.println("Error reading graph.txt");
        }
        graph = r.graph();
    }

    private int toInt(String s) {
        return Integer.parseInt(s);
    }

    private String toString(Object o) {
        return "" + o;
    }

    private List<Integer> findNeighbourIds(Node n, Graph g)
    {
        List<Integer> output = new ArrayList<Integer>();

        for(Edge e : g.edges())
        {
            if( e.id1().equals(n.name()) ) {
                output.add( toInt(e.id2()) );
            }
            else if(e.id2().equals(n.name())) {
                output.add( toInt(e.id1()) );
            }
        }
        return output;
    }

    private boolean canReachNode(Graph g, int fromNode, int toNode, TicketType ticketType)
    {
        List<Edge> connectingEdges = new ArrayList<Edge>();
        // Check that nodes are connected
        for(Edge e : g.edges())
        {
            if ( (toInt(e.id1()) == fromNode && toInt(e.id2()) == toNode) ||
                    (toInt(e.id2()) == fromNode && toInt(e.id1()) == toNode) )
            {
                connectingEdges.add(e);
            }
        }

        if (connectingEdges.isEmpty()) { return false; }

        if ( ticketType.equals(TicketType.SecretMove) ||
                ticketType.equals(TicketType.DoubleMove) ) {
            return true;
        }

        for (Edge e : connectingEdges) {
            if (e.type().toString().equals( ticketType.toString() ))
                return true;
        }
        return false;
    }

	private int randomNode(List<Integer> exclude)
    {
		Random r = new Random();
		int nodeNo = r.nextInt(200);
		while(exclude.contains(nodeNo)) {
			nodeNo = r.nextInt(200);
		}
		return nodeNo;
	}

	public Boolean initialiseGame(Integer numberOfDetectives) {
        try
        {
            // Reads pos.txt and renders positions for nodes
            setPositions();
            setGraph();

            // Clear all game resources
            setMapFilename("");
            players = new Player[numberOfDetectives + 1];
            List<Integer> startNodes = new ArrayList<Integer>();
            int[] detectiveTickets = {8, 10, 4, 0, 0};
            int[] mrXTickets =       {3, 4, 3, 2, numberOfDetectives};

            //Initialise detectives
            int i;
            for(i=0; i<numberOfDetectives; i++)
            {
                int node = randomNode(startNodes);
                players[i] = new Player(Player.PlayerType.DETECTIVE, i, detectiveTickets, node);
                startNodes.add(node);
            }
            //Initialise Mr X
            players[i] = new Player(Player.PlayerType.MISTERX, i, mrXTickets, randomNode(startNodes));
            
            //Set first move //TODO relocate
            whoseTurn = players[0];
            return true;
        }
        catch (Exception e)
        {
            System.err.println("Error initialising game: "+e.getMessage());
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
        List<Integer> output = new ArrayList<Integer>();
        for(Player p : players)
        {
            if (p.getType().equals(Player.PlayerType.DETECTIVE))
                output.add( p.getId() );
        }
        return output;
    }

    public List<Integer> getMrXIdList() {
        List<Integer> output = new ArrayList<Integer>();
        for(Player p : players)
        {
            if (p.getType().equals(Player.PlayerType.MISTERX))
                output.add( p.getId() );
        }
        return output;
    }

    public Integer getNodeId(Integer playerId) {
        return players[playerId].getNode();
    }

	public Integer getNumberOfTickets(TicketType type, Integer playerId) {
		
		return players[playerId].getTicketNum(type);
	}

	public List<TicketType> getMoveList(Integer playerId) {
		return players[playerId].getPreviousMoves();
	}

	public Boolean isVisible(Integer playerId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean isGameOver() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getNextPlayerToMove() {
		return whoseTurn.getId();
	}

	public Integer getWinningPlayerId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean movePlayer(Integer playerId, Integer targetNodeId,
			TicketType ticketType) {
        players[playerId].setTicketNum( ticketType, players[playerId].getTicketNum(ticketType) - 1);

		return canReachNode(graph, players[playerId].getNode(), targetNodeId, ticketType) &&
                players[playerId].makeMove(ticketType, targetNodeId);
	}

	@Override
	public Integer getNodeIdFromLocation(Integer xPosition, Integer yPosition) {
		int numberOfNodes = positions.size();
    	int clickRadius = 20;
    	int tX; int tY;
    	for(int i=1; i<numberOfNodes+1; i++) {
        		tX = getLocationX(i);
        		tY = getLocationY(i);		
    		if(Math.abs(xPosition - tX) < clickRadius && Math.abs(yPosition - tY) < clickRadius)
        		return i;
    	}
    	return -1;
	}

	@Override
	public Boolean saveGame(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean loadGame(String filename) {
		// TODO Auto-generated method stub
		return null;
	}


}
