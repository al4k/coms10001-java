import java.util.*;

/**
 * This is the class that represents a graph. The graph is structured 
 * as a list of nodes. The list of nodes can be retrieved using the nodes()
 * method. The class also provides a method of finding a node by its name. 
 * New nodes can be added using the add(Node node) method.
 *
 */
public class Graph
{
    private List<Node> nodes;

    /**
     * Graph constructor. Initialises an empty list of nodes
     */
    public Graph()
    {
        nodes = new ArrayList<Node>();
    }

    /**
     * Function that retrieves the list of nodes contained in the graph
     * @return list of nodes
     */
    public List<Node> nodes()
    {
        return nodes;
    }

    /**
     * Function to a node by searching for its name
     * @param name Name of the node you wish to find
     * @return The found node or null if the graph does not contain a node of that
     * name
     */
    public Node find(String name)
    {
        for (Node n : nodes)
        {
            if (n.name().equals(name)) return n;
        }
        return null;
    }

    /**
     * Function to add a node to the graph
     * @param node The node you wish to add
     */
    public void add(Node node)
    {
        nodes.add(node);
    }
}
