package cw3;

/**
 * Class that represents a graph edge. The edges 
 * define the structure of the graph as they constitute
 * the connections between nodes. 
 * 
 * In addition, they have both an edge type and an edge weight.
 * It is important that you understand this class.
 */
public class Edge {

	/**
	 * Enum type which defines the different
	 * types of edge. (similar to the different types
	 * of transport in the game 'Scotland Yard')
	 */
	public enum EdgeType {
		LocalRoad, MainRoad, Underground;
	}
	
	
	String id1;
	String id2;
	double weight;
	EdgeType type;
	
	
	/**
	 * Returns the id of the first node that this edge connects
	 * @return The id of the first node
	 */
	public String id1()
	{
		return id1;
	}

	/**
	 * Returns the id of the second node that this edge connects
	 * @return The id of the second node
	 */
	public String id2()
	{
		return id2;
	}
	

	/**
	 * Function to get the weight of the edge
	 * @return The weight assigned to the edge
	 */
	public double weight()
	{
		return weight;
	}
	
	
	/**
	 * Function to get the type of the edge
	 * @return The type of the edge
	 */
	public EdgeType type()
	{
		return type;
	}
	
	
	/**
	 * Class constructor. All of the information about the edge is set in 
	 * this function and cannot be changed afterwards
	 * @param id1 The id of the first node that this edge connects
	 * @param id2 The id of the second node that this edge connects
	 * @param weight The weight of the edge
	 * @param type The type of the edge
	 */
	public Edge(String id1, String id2, double weight, EdgeType type)
	{
		this.id1 = id1;
		this.id2 = id2;
		this.weight = weight;
		this.type = type;
	}
	
	
}
