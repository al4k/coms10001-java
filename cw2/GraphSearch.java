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

  private static boolean hasSameElements(List<Node> a, List<Node> b){
/*
    print("comparing lists "); printList(a); print(", "); printList(b); System.out.println();
*/
    if(a.size() != b.size()){return false;}
    for(Node n : a){
      if(!b.contains(n))
        return false;
    }
    return true;
  }

  private static boolean listContains(List<Node> list, List<Node> contains){
    int i = 0;
    for(Node ns : contains){
      if(list.contains(ns))
        i++;
      if(i==contains.size() || i==2)
        return true;
    }
    return false;
  }

  private static boolean listArrayContains(ArrayList<ArrayList<Node>> list, List<Node> contains){
    for(ArrayList<Node> ns : list){
      if(hasSameElements(ns,contains))
        return true;
    }
    return false;
  }

  public int findNumberOfCliques(Graph graph, int n){
    int total = 0;
    ArrayList<ArrayList<Node>> cliques = new ArrayList<ArrayList<Node>>();
    ArrayList<Node> valid = new ArrayList<Node>();
    List<Node> candidates = new ArrayList<Node>();

    for(Node ns : graph.nodes()){
      if(ns.neighbours().size() < n-1){continue;}

      candidates = ns.neighbours();
      candidates.add(ns);
      valid.clear();
      valid.add(ns);
/*
      print("node "+ns.name()+": candidates "); printList(candidates); System.out.println();
*/
      for(Node nb : ns.neighbours()){
        if(!valid.contains(nb) && listContains(nb.neighbours(),valid)){
          valid.add(nb);
/*
          print("--node "+nb.name()+" added. valid="); printList(valid); System.out.println();
*/
        }
        if(valid.size() == n){
          if(listArrayContains(cliques,valid)){
            break;
/*
            print("--clique already added : "); printList(valid); System.out.println();
*/
          }
          total++;
          cliques.add(new ArrayList<Node>(valid));
          break;
/*
          print("--clique found: "); printList(valid); System.out.println();
*/
        }
      }
    }
    return total;
  }

  private static int toInt(String s){
    return Integer.parseInt(s);
  }

  private static void print(Object s){
    System.out.print(s);
  }

  @Deprecated
  private static void printList(List<Node> l){
    print("{");
    for(Node n : l){
      print(n.name()+" ");
    }
    print("}");
  }

  @Deprecated
  public static void printGraph(Graph g){
    for(Node n : g.nodes()){
        print(n.name()+" ");
        for(Node nb : n.neighbours()){
          print(nb.name()+" ");
        }
        System.out.println();
    }
  }

  public static void main(String[] args){
    GraphSearch g = new GraphSearch();
    g.run(args);
  }
}
