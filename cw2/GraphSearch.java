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
    if(a.size() != b.size()){return false;}
    for(Node n : a){
      if(!b.contains(n))
        return false;
    }
    return true;
  }

  private static boolean listContains(List<Node> list, List<Node> contains){
    for(Node ns : contains){
      if(!list.contains(ns))
        return false;    
    }
    return true;
  }

  private static boolean listArrayContains(ArrayList<ArrayList<Node>> list, List<Node> contains){
    for(ArrayList<Node> ns : list){
      if(hasSameElements(ns,contains))
        return true;
    }
    return false;
  }

  private List<List<Node>> findPossibleCliques(List<Node> set, Node current, int k){
    List<List<Node>> output = new ArrayList<List<Node>>();
    for(List<Node> s : processSubsets(set, k)){
      if(s.contains(current)){
        s.remove(current);
        output.add(s);
      }
    }
    return output;
  }

  private List<List<Node>> processSubsets(List<Node> set, int k) {
    if(k > set.size()){
      k = set.size();
    }
    List<List<Node>> result = new ArrayList<List<Node>>();
    List<Node> subset = new ArrayList<Node>(k);
    for(int i = 0; i < k; i++) {
      subset.add(null);
    }
    return processLargerSubsets(result, set, subset, 0, 0);
  }

  private List<List<Node>> processLargerSubsets(List<List<Node>> result, List<Node> set, List<Node> subset, int subsetSize, int nextIndex) {
    if(subsetSize == subset.size()){
      result.add(new ArrayList<Node>(subset));
    }else{
      for(int j = nextIndex; j < set.size(); j++) {
        subset.set(subsetSize, set.get(j));
        processLargerSubsets(result, set, subset, subsetSize+1, j+1);
      }
    }
    return result;
  }
  
  public int findNumberOfCliques(Graph graph, int n){
    if(n==1)
      return graph.nodes().size();

    int total = 0;
    ArrayList<ArrayList<Node>> cliques = new ArrayList<ArrayList<Node>>();
    Collection<List<Node>> possibleCliques = new ArrayList<List<Node>>();
    List<Node> valid = new ArrayList<Node>();
    List<Node> candidates = new ArrayList<Node>();

    for(Node ns : graph.nodes()){
      if(ns.neighbours().size() < n-1){continue;}

      candidates = ns.neighbours();
      candidates.add(ns);
      possibleCliques = findPossibleCliques(candidates,ns,n);
      valid.clear();
      valid.add(ns);

      //print("node "+ns.name()+": candidates "); printList(candidates); System.out.println();
      //print("possible permutations: "); printArrayList(possibleCliques); System.out.println();

      for(List<Node> clique : possibleCliques){
        for(Node node : clique){
          if(!valid.contains(node) && listContains(node.neighbours(),valid)){
            valid.add(node);
            //print("--node "+node.name()+" added. valid="); printList(valid); System.out.println();
            if(valid.size() == n){
              if(listArrayContains(cliques,valid)){   
                //print("--clique already added : "); printList(valid); System.out.println();	          
              }else{
                total++;
                cliques.add(new ArrayList<Node>(valid)); 
                //print("--clique found: "); printList(valid); System.out.println();
              }
              valid.clear();
              valid.add(ns);
              break;
            }
          }else{
            //print("--node "+node.name()+" invalid"); System.out.println();
            valid.clear();
            valid.add(ns);
            break;
          }
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
  private static void printArrayList(Collection<List<Node>> ls){
    print("{");
    for(List<Node> l : ls){
      printList(l); print(",");
    }
    print("}");
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

  public static void main(String[] args){
    GraphSearch g = new GraphSearch();
    g.run(args);
  }
}
