
public class Player 
{
	private int id;
	private int[] tickets;
	private int node;
	
	Player(int id, int[] tickets, int startNode)
	{
		tickets = new int[Initialisable.TicketType.values().length];
		this.id = id;
		this.node = startNode;
		this.tickets = tickets;
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getNode()
	{
		return node;
	}

	public int getTicketNum(Initialisable.TicketType type)
	{
		return tickets[type.ordinal()];
	}
	
	public void setTicketNum(Initialisable.TicketType type, int n)
	{
		tickets[type.ordinal()] = n;
	}
	
}
