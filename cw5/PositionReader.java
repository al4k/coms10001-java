import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Reads x-y-locations associated with nodes on map
 */
public class PositionReader
{
    private HashMap<Integer, Point> locations;

    public HashMap<Integer, Point> positions()
    {
        return locations;
    }

    public void read(String filename) throws IOException
    {
        locations = new HashMap<Integer, Point>();

        // load the file
        File file = new File(filename);
        Scanner in = new Scanner(file);

        // skip the top line
        in.nextLine();

        // read the nodes
        while(in.hasNextLine())
        {
            String line = in.nextLine();
            String[] parts = line.split(" ");
            Point p = new Point(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            locations.put(Integer.parseInt(parts[0]), p);
        }
        in.close();
    }

}
