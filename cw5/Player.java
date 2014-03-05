import java.util.ArrayList;
import java.util.List;

public class Player 
{
    enum PlayerType { DETECTIVE, MISTERX };

	private int id;
	private int[] tickets;
	private int node;
    private PlayerType type;
    private List<Initialisable.TicketType> previousMoves;
	
	Player(PlayerType type, int id, int[] tickets, int startNode) {
        this.type = type;
		this.id = id;
		this.node = startNode;
		this.tickets = tickets;
        previousMoves = new ArrayList<Initialisable.TicketType>();
	}

    public PlayerType getType() { return type; }

	public int getId() { return id; }
	
	public int getNode() { return node; }

	public int getTicketNum(Initialisable.TicketType type) {
		return tickets[type.ordinal()];
	}
	
	public void setTicketNum(Initialisable.TicketType type, int n) {
		tickets[type.ordinal()] = n;
	}
	
	public List<Initialisable.TicketType> getPreviousMoves() {
		return previousMoves;
	}
	
	public boolean makeMove(Initialisable.TicketType moveType, int newNode) {
        node = newNode;
		previousMoves.add(moveType);
		return node == newNode && previousMoves.contains(moveType);
	}
	
}
