import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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
    private int whoseTurnIndex;
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

    private List<Integer> findNeighbourIds(Node n, Graph g)
    {
        List<Integer> output = new ArrayList<Integer>();

        for(Edge e : g.edges())
        {
            if( e.id1().equals(n.name()) )
            {
                output.add( toInt(e.id2()) );
            }
            else if(e.id2().equals(n.name()))
            {
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
                ticketType.equals(TicketType.DoubleMove) )
        {
            return true;
        }

        for (Edge e : connectingEdges)
        {
            if (e.type().toString().equals( ticketType.toString() ))
                return true;
        }
        return false;
    }

	private int randomNode(List<Integer> exclude)
    {
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
                players[i] = new Player(Player.PlayerType.DETECTIVE, i, detectiveTickets.clone(), node);
                startNodes.add(node);
            }
            //Initialise Mr X
            players[i] = new Player(Player.PlayerType.MISTERX, i, mrXTickets.clone(), randomNode(startNodes));
            
            //Set first player to move
            whoseTurnIndex = 0;
            return true;
        }
        catch (Exception e)
        {
            System.err.println("Error initialising game: "+e.getMessage());
        }
		return false;
	}

    public Integer getLocationX(Integer nodeId)
    {
        return (int) positions.get(nodeId).getX();
    }

    public Integer getLocationY(Integer nodeId)
    {
        return (int) positions.get(nodeId).getY();
    }

    public List<Integer> getDetectiveIdList()
    {
        List<Integer> output = new ArrayList<Integer>();
        for(Player p : players)
        {
            if (p.getType().equals(Player.PlayerType.DETECTIVE))
                output.add( p.getId() );
        }
        return output;
    }

    public List<Integer> getMrXIdList()
    {
        List<Integer> output = new ArrayList<Integer>();
        for(Player p : players)
        {
            if (p.getType().equals(Player.PlayerType.MISTERX))
                output.add( p.getId() );
        }
        return output;
    }

    public Integer getNodeId(Integer playerId)
    {
        return players[playerId].getNode();
    }

	public Integer getNumberOfTickets(TicketType type, Integer playerId)
	{
		return players[playerId].getTicketNum(type);
	}

	public List<TicketType> getMoveList(Integer playerId)
	{
		return players[playerId].getPreviousMoves();
	}

	public Boolean isVisible(Integer playerId) {
		// TODO Edit GUI to make MrX invisible
		return null;
	}

	public Boolean isGameOver() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getNextPlayerToMove() {
		return whoseTurnIndex;
	}

	public Integer getWinningPlayerId() {
		// TODO Auto-generated method stub
		return null;
	}

	private void nextTurn()
	{
		if( whoseTurnIndex == players.length-1 ) {
			whoseTurnIndex = 0;
		} else {
			whoseTurnIndex++;
		}
	}
	
	@Override
	public Boolean movePlayer(Integer playerId, Integer targetNodeId, TicketType ticketType)
	{
        if( canReachNode(graph, players[playerId].getNode(), targetNodeId, ticketType) &&
                players[playerId].makeMove(ticketType, targetNodeId) )
        {
        	players[playerId].setTicketNum( ticketType, players[playerId].getTicketNum(ticketType) - 1);
        	nextTurn();
        	return true;
        }
        return false;	
	}

	@Override
	public Integer getNodeIdFromLocation(Integer xPosition, Integer yPosition)
	{
		int numberOfNodes = positions.size();
    	int clickRadius = 20;
    	int tX; int tY;
    	for(int i=1; i<numberOfNodes+1; i++)
    	{
        	tX = getLocationX(i);
        	tY = getLocationY(i);		
    		if(Math.abs(xPosition - tX) < clickRadius && Math.abs(yPosition - tY) < clickRadius)
        		return i;
    	}
    	return -1;
	}

    private java.util.List<Integer> getAllTickets(int id)
    {
    	java.util.List<Integer> ticketNum = new ArrayList<Integer>();
		for (TicketType type : TicketType.values())
		{
			ticketNum.add(getNumberOfTickets(type, id));
		}
		return ticketNum;
    }
    
    private <T> String listToStr(List<T> list)
    {
    	if ( list == null || list.isEmpty() ) { return null; }
    	String s = "" + list.get(0);
    	for (int i=1; i<list.size(); i++)
    	{
    		s += "," + list.get(i);
    	}
    	return s;
    }
	
	@Override
	// format: playerId playerType currentNode moveList ticketList
	public Boolean saveGame(String filename)
	{
		//Compile game data
    	java.util.List<String> lines = new ArrayList<String>();
    	java.util.List<Integer> detectives = getDetectiveIdList();
    	java.util.List<Integer> mrX = getMrXIdList();
    	for (int id : detectives)
    	{
    		lines.add(id+" det "+getNodeId(id)+" "+listToStr(getMoveList(id))+" "+listToStr(getAllTickets(id)));
    	}

    	for (int id : mrX)
    	{
    		lines.add(id+" mrx "+getNodeId(id)+" "+listToStr(getMoveList(id))+" "+listToStr(getAllTickets(id)));
    	}
    	
    	// Save file
		try
		{
			FileWriter fw = new FileWriter(filename);
			fw.write(getNextPlayerToMove()+ System.getProperty("line.separator"));
			for (String s : lines) {
				fw.write(s + System.getProperty("line.separator"));
			}
			fw.close();
			return true;
		}
		catch (Exception e)
		{
			System.err.println("Error saving file");
		}
		return false;
	}

	private List<TicketType> strToTicketTypeList(String s)
	{
		List<TicketType> list = new ArrayList<TicketType>();
		String[] split = s.split(",");
		for( String part : split )
		{
			for( TicketType type : TicketType.values() )
			{
				if( type.toString().equals(part) )
				{
					list.add(type);
					break;
				}
			}
		}
		return list;
	}
	
	private int[] strToIntArr(String s)
	{
		String[] split = s.split(",");
		int[] list = new int[split.length];
		
		for (int i = 0; i<split.length; i++)
		{
			list[i] = toInt( split[i] );
		}
		return list;
	}
	
	@Override
	//split[0] playerId
	//split[1] playerType {det/mrx}
	//split[2] nodeId
	//split[3] moveList
	//split[4] ticketList
	public Boolean loadGame(String filename) {
		List<Player> players = new ArrayList<Player>();
		try
		{
			File file = new File(filename);
			Scanner in = new Scanner(file);
			whoseTurnIndex = toInt( in.nextLine() );
			Player p = null;
			while ( in.hasNextLine() )
			{
				String[] split = in.nextLine().split(" ");
				if ( split[1].equals("det") )
				{
					p = new Player(toInt(split[0]), Player.PlayerType.DETECTIVE, toInt(split[2]), strToTicketTypeList(split[3]), strToIntArr(split[4]));
				}
				else if ( split[1].equals("mrx") )
				{
					p = new Player(toInt(split[0]), Player.PlayerType.MISTERX, toInt(split[2]), strToTicketTypeList(split[3]), strToIntArr(split[4]));
				}
				players.add(p);
			}
			in.close();
			initialiseGame(players.size());
			this.players = players.toArray(new Player[1]);
			return true;
		}
		catch (Exception e)
		{
			System.err.println("Error loading file");
			e.printStackTrace();
		}
		return false;
	}


}
