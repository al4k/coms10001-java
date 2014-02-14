/**
 * Class representing a node of the graph
 */
public class Node
{
    private String name;

    /**
     * Node constructor
     * @param n Name that will be given to the node
     */
    Node(String n)
    {
        name = n;
    }

    /**
     * Function that gets the name that has been assigned to the node
     * @return
     */
    String name()
    {
        return name;
    }
}