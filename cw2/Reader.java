import java.util.*;
import java.io.*;

/**
 * This is a utility class that enables you to load a graph from a text file.
 */
public class Reader
{
    private Graph graph;

    /**
     * Constructor for the reader class
     */
    public Reader()
    {   	
    }
    
    /**
     * Function to get the loaded graph from the reader
     * @return The loaded if graph if has been loaded ok. It will be null if not
     */
    public Graph graph()
    {
    	return graph;
    }
    
    /**
     * Function to read the graph from the file
     * @param filename The filename of the graph
     * @throws IOException 
     */
    public void read(String filename) throws IOException
    {
    	// initialise the graph
    	graph = new Graph();
    	
    	// load the file
        File file = new File(filename);
        Scanner in = new Scanner(file);
        
        while (in.hasNextLine())
        {
            String line = in.nextLine();
            
            String[] names = line.split(" ");
            Node node = findOrCreate(names[0]);
            for (int i = 1; i < names.length; i++)
            {
                Node other = findOrCreate(names[i]);
                node.addNeighbour(other);
            }
        }
    }

    // Find a node, or create a new one, with a given name.
    private Node findOrCreate(String name)
    {
        Node node = graph.find(name);
        if (node != null) return node;
        node = new Node(name);
        graph.add(node);
        return node;
    }
}