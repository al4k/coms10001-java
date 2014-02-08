import java.util.*;

/**
 * Class representing a graph node. This class has a property that contains 
 * the name of the node. It also contains a list of the nodes that are its 
 * neighbours
 */
public class Node
{
    private String name;
    private List<Node> neighbours;

    /**
     * Constructor for a node. Must pass in a string to give the node a name
     * @param n The name you wish to assign to the node
     */
    public Node(String n)
    {
        name = n;
        neighbours = new ArrayList<Node>();
    }

    /**
     * Function to get the name of the node
     * @return The name of the node
     */
    public String name()
    {
        return name;
    }

    /**
     * Function to get the list of nodes that are the node's neighbours
     * @return The list of the nodes neighbours
     */
    public List<Node> neighbours()
    {
        return neighbours;
    }
    
    /**
     * Function to get the degree of the node (number of neighbours)
     * @return The degree of the node
     */
    public int degree()
    {
        return neighbours.size();
    }

    /**
     * Function to add a node to the list of neighbours
     * @param node The node you wish to add
     */
    public void addNeighbour(Node node)
    {
        neighbours.add(node);
    }
}