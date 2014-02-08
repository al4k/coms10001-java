import java.util.*;

class GraphSearch {

  public GraphSearch(){}

  private void run(String[] args){
    if(args[0].equals("-p1") || args[0].equals("-p2") || args[0].equals("-p3") || args[0].equals("-p4")){
      Reader r = new Reader();
      try{
        r.read(args[1]);
      }catch(java.io.IOException e){
        System.out.println("read error");
        System.exit(1);
      }
      if(args[0].equals("-p1")){
        printGraph(r.graph());
      }else if(args[0].equals("-p2")){
        System.out.println("Number of nodes with at least "+args[2]+" neighbours: "+neighbourSearch(r.graph(),toInt(args[2])).size());
      }else if(args[0].equals("-p3")){
        System.out.println("Number of nodes with fully connected neighbours: "+findFullyConnectedNeighbours(r.graph()).size());
      }else if(args[0].equals("-p4")){
        System.out.println("Number of cliques of size "+args[2]+": "+findNumberOfCliques(r.graph(),toInt(args[2])));
      }
    }
  }

  public static void printGraph(Graph g){
    for(Node n : g.nodes()){
        print(n.name()+" ");
        for(Node nb : n.neighbours()){
          print(nb.name()+" ");
        }
        System.out.println();
    }
  }

  public static List<Node> neighbourSearch(Graph graph, int n){
    List<Node> output = new ArrayList<Node>();
    for(Node ns : graph.nodes()){
      if(ns.degree() >= n)
        output.add(ns);      
    }
    return output;
  }

  public static List<Node> findFullyConnectedNeighbours(Graph graph){
    boolean flag;
    List<Node> output = new ArrayList<Node>();
    for(Node ns : graph.nodes()){
      flag = true;
      for(Node nb : ns.neighbours()){
        if(!flag){break;}
        for(Node nb2 : ns.neighbours()){
          if(!flag){break;}
          if(!nb.name().equals(nb2.name()) && !graph.find(nb.name()).neighbours().contains(nb2)){
            flag = false;
          }
        }
      }
      if(flag)
        output.add(ns);
    }
    return output;
  }

  public static int findNumberOfCliques(Graph graph, int n){
    boolean flag;
    int total = 0;
    // TODO
    return total;
  }

  private static int toInt(String s){
    return Integer.parseInt(s);
  }

  private static void print(Object s){
    System.out.print(s);
  }

  public static void main(String[] args){
    GraphSearch g = new GraphSearch();
    g.run(args);
  }
}
