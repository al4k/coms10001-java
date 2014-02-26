import java.text.DecimalFormat;
import java.util.*;

class ShortestPath {
    Map<String, Double> dist;
    Node startNode; Node endNode;
    List<Node> shortestPath;
    int taxiUsed; int busUsed; int underUsed;

    ShortestPath(Graph g, String startNodeStr, String endNodeStr) {
        taxiUsed = 0;
        busUsed = 0;
        underUsed = 0;
        shortestPath = new ArrayList<Node>();
        dist = new HashMap<String, Double>();
        startNode = g.find(startNodeStr);
        endNode = g.find(endNodeStr);
    }

    public static void main(String[] args) {
        Reader r = new Reader();
        Graph g;
        try {
            r.read(args[1]);
            g = r.graph();
            ShortestPath s = new ShortestPath(g, args[2], args[3]);
            s.run(g, args);
        } catch(Exception e) {
            System.out.println("Error with arguments");
            System.exit(1);
        }
    }

    private void run(Graph g, String[] args) {
        if( args[0].equals("-p1")) {
            println("Number of moves: "+dijkstraSimple(g));
        }else if( args[0].equals("-p2")) {
            DecimalFormat twodp = new DecimalFormat("0.00");
            println("Distance travelled: "+twodp.format(dijkstraWeighted(g)) + "km");
        }else if( args[0].equals("-p3")) {
            dijkstraWeighted(g);
            printRoute(shortestPath);
        }else if( args[0].equals("-p4")) {
            dijkstraSimple(g);
            ticketsUsed(g, shortestPath, true);
        }else if( args[0].equals("-p5")) {
            dijkstraSimple(g);
            ticketsUsed(g, shortestPath, false);
            if(isTraversable(toInt(args[4]), toInt(args[5]), toInt(args[6])) ){
                println("Route is traversable");
            } else {
                println("Route is not traversable");
            }
        }
    }

    private Node minDistance(List<Node> ns, Map<String, Double> dist) {
        double minD = Double.MAX_VALUE;
        Node output = ns.get(0);
        for(Node n : ns) {
            if(dist.get(n.name()) < minD) {
                minD = dist.get(n.name());
                output = n;
            }
        }
        return output;
    }

    private List<Node> findNeighbours(Node n, Graph g) {
        List<Node> output = new ArrayList<Node>();

        for(Edge e : g.edges()) {
            if( e.id1().equals(n.name()) ) {
                output.add( g.find( e.id2() ));
            }
            else if(e.id2().equals(n.name())) {
                output.add( g.find( e.id1() ));
            }
        }
        return output;
    }

    private List<Edge> connectingEdges(Node n, Graph g) {
        List<Edge> output = new ArrayList<Edge>();

        for(Edge e : g.edges()) {
            if( e.id1().equals(n.name()) || e.id2.equals(n.name())) {
                output.add(e);
            }
        }
        return output;
    }

    private double dijkstraSimple(Graph g) {
        List<Node> unvisited = new ArrayList<Node>(g.nodes());
        List<Node> visited = new ArrayList<Node>();
        Map<Node, Node> route = new HashMap<Node, Node>();

        for(Node n : unvisited) {
            dist.put(n.name(),Double.MAX_VALUE);
        }

        Node u = startNode;
        dist.put(u.name(),0.0);

        while(!unvisited.isEmpty()) {
            visited.add(u);
            unvisited.remove(u);
             for(Node nb : findNeighbours(u, g)) {
                if(!visited.contains(nb)) {
                    if(dist.get(u.name())+1 < dist.get(nb.name())) {
                        dist.put(nb.name(), dist.get(u.name())+1);
                        route.put(nb, u);
                    }
                }
            }
            u = minDistance(unvisited, dist);
            if(u.equals(endNode))
                break;
        }
        makeRoute(route, endNode);
        return dist.get(endNode.name());
    }

    private double dijkstraWeighted(Graph g) {
        List<Node> unvisited = new ArrayList<Node>(g.nodes());
        List<Node> visited = new ArrayList<Node>();
        Map<Node, Node> route = new HashMap<Node, Node>();

        for(Node n : unvisited) {
            dist.put(n.name(), Double.MAX_VALUE);
        }

        Node u = startNode;
        dist.put(u.name(),0.0);

        while(!unvisited.isEmpty()) {
            visited.add(u);
            unvisited.remove(u);

            for(Edge e : connectingEdges(u, g)) {
                Node nb = g.find(relevantNode(u,e));

                if(!visited.contains(nb)) {
                    if(dist.get(u.name())+e.weight() < dist.get(nb.name()) ) {
                        dist.put(nb.name(), dist.get(u.name())+e.weight());
                        route.put(nb, u);
                    }
                }
            }
            u = minDistance(unvisited, dist);
            if(u.equals(endNode))
                break;
        }
        makeRoute(route, endNode);
        return dist.get(endNode.name());
    }

    private void makeRoute(Map<Node, Node> nodes, Node targetNode) {
        List<Node> path = new ArrayList<Node>();
        Node cur = targetNode;
        while(nodes.get(cur) != null) {
            path.add(cur);
            cur = nodes.get(cur);
        }
        path.add(cur);
        Collections.reverse(path);
        shortestPath = path;
    }

    private void printRoute(List<Node> path) {
        String out = "";
        for(Node n : path) {
            out += n.name() + " ";
        }
        println("Route: " + out);
    }

    // Given an edge and a node, return the neighbour connected by the edge
    private String relevantNode(Node n, Edge e) {
        if(e.id1.equals(n.name())){
            return e.id2;
        }else{
            return e.id1;
        }
    }

    // Given several edges and a node, return the one connected to the node
    private Edge relevantEdge(List<Edge> es, Node n) {
        for(Edge e : es) {
            if(e.id1().equals(n.name()))
                return e;
        }
        return es.get(0);
    }

    private void ticketsUsed(Graph g, List<Node> path, boolean print) {
        int taxi = 0, bus = 0, underground = 0;
        for(int i=1; i<path.size(); i++) {
            Edge e = relevantEdge(g.edges(path.get(i).name()), path.get(i - 1));
            if(e.type().equals(Edge.EdgeType.Taxi) ) {
                taxi++;
            }else if(e.type().equals(Edge.EdgeType.Bus) ) {
                bus++;
            }else {
                underground++;
            }
        }
        taxiUsed = taxi;
        busUsed = bus;
        underUsed = underground;
        if(print) {
            println("Taxi: " + taxi);
            println("Bus: " + bus);
            println("Underground: " + underground);
        }
    }

    private boolean isTraversable(int taxi, int bus, int under) {
        if(taxiUsed > taxi ||
                busUsed > bus ||
                underUsed > under) {
            return false;
        }
        return true;
    }

    private int toInt(String s) throws Error {
        return Integer.parseInt(s);
    }

    private void println(Object s){
        System.out.println(s);
    }
}
