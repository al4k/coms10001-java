package cw3;

import java.text.*;
import java.util.*;

class SpanningTree {

	private void run(String[] args){
		if(args[0].equals("-p1") || args[0].equals("-p2") || args[0].equals("-p3")){
			Reader r = new Reader();
			try{
				r.read(args[1]);
			}catch(java.io.IOException e){
				println("Read error");
				System.exit(1);
			}
			DecimalFormat twodp = new DecimalFormat("#.##");
			if(args[0].equals("-p1")){
				println("Total cable needed: "+twodp.format(totalCable(r.graph()))+"m");
			}else if(args[0].equals("-p2")){
				println("Price: £"+twodp.format(governmentCost(r.graph())));
				println("Hours of disrupted travel: "+twodp.format(hourCost(r.graph()))+"h");
				println("Completion date: "+dateComplete(r.graph()));
			}else if(args[0].equals("-p3")){
				Graph g = minSpanningTree(r.graph());
				println("Price: £"+twodp.format(governmentCost(g)));
				println("Hours of disrupted travel: "+twodp.format(hourCost(g))+"h");
				println("Completion date: "+dateComplete(g));
			}
		}
	}

	private double governmentCost(Graph g){
		double total = 0;
		for(Edge e : g.edges()){
			switch(e.type()){
				case LocalRoad : {total += 5000 + (e.weight() * 4500);}
				case MainRoad : {total += e.weight() * 4000;}
				case Underground : {total += e.weight() * 1000;}
			}
		}
		return total;
	}
	
	private double hourCost(Graph g){
		double total = 0;
		for(Edge e : g.edges()){
			switch(e.type()){
				case LocalRoad : {total += e.weight() * 0.2;}
				case MainRoad : {total += e.weight() * 0.5;}
				case Underground : {total += e.weight();}
			}
		}
		return total;
	}
	
	private static long millisInDay = 86400000;
	
	private String dateComplete(Graph g){		
		Calendar c = Calendar.getInstance();
		c.set(2014, 1, 15, 00, 00);
		long current = c.getTimeInMillis();
		for(Edge e : g.edges()){ 
			switch(e.type()){
				case LocalRoad : {current += (e.weight()/0.2)*millisInDay;}
				case MainRoad : {current += (e.weight() / 0.6)*millisInDay;}
				case Underground : {current += (e.weight() / 0.9)*millisInDay;}
			}
		}
		return new SimpleDateFormat("EEEE, d, MMMM, YYYY HH:mm").format(new Date(current));
	}
	
	private double totalCable(Graph g){
		double total = 0;
		for(Edge e : g.edges()){
			total += e.weight() * 1000;
		}
		return total;
	}
	
	// Sort by edge weight
	private List<Edge> quickSort(List<Edge> edges){
		if(!edges.isEmpty()){
			Edge pivot = edges.get(0);
			List<Edge> less = new ArrayList<Edge>();
			List<Edge> pivotList = new ArrayList<Edge>();
			List<Edge> greater = new ArrayList<Edge>();

			for(Edge e : edges){
				if(e.weight() < pivot.weight()){
					less.add(e);
				}else if(e.weight() > pivot.weight()){
					greater.add(e);
				}else{
					pivotList.add(e);
				}
			}
			less = quickSort(less);
			greater = quickSort(greater);
			less.addAll(pivotList);
			less.addAll(greater);
			return less;
		}
		return edges;
	}
	
	private Graph minSpanningTree(Graph g){
		List<Edge> edges = quickSort(g.edges());
		HashMap<String, List<String>> forest = new HashMap<String, List<String>>();
		Graph output = new Graph();
		
		// Create a tree for each node and place in forest. Add nodes to output.
		for(Node n : g.nodes()){
			List<String> tree = new ArrayList<String>();
			tree.add(n.name());
			forest.put(n.name(), tree);
			output.add(n);
		}
		
		/* 
		 * In ascending edge weight, accumulate the connected nodes and build on trees in forest.
		 * Repeat to find the minimum weight graph
		*/
		while(!edges.isEmpty()){
			Edge e = edges.remove(0);
			List<String> nodes1 = forest.get(e.id1());
			List<String> nodes2 = forest.get(e.id2());
			if(nodes1.equals(nodes2))
				continue;
			
			output.add(e);
			nodes1.addAll(nodes2);
			
			for(String n : nodes1){
				forest.put(n, nodes1);
			}
			if(nodes1.size() == g.nodes().size())
				break;	
		}
		
		return output;
	}
	
	private static void println(Object s){
		System.out.println(s);
	}
	
	public static void main(String[] args) {
		SpanningTree s = new SpanningTree();
		s.run(args);
	}
}
