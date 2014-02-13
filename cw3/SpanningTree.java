package cw3;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
				println("Total cable needed: "+twodp.format(totalEdgeWeight(r.graph()))+"m");
			}else if(args[0].equals("-p2")){
				println("Price: £"+twodp.format(governmentCost(r.graph())));
				println("Hours of disrupted travel: "+twodp.format(hourCost(r.graph()))+"h");
				println("Completion date: "+dateComplete(r.graph()));
			}else if(args[0].equals("-p3")){
				
			}
		}
	}

	private double governmentCost(Graph g){
		double total = 0;
		for(Edge e : g.edges()){
			switch(e.type()){
				case LocalRoad : {total += 5000 + (e.weight() * 4000);}
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
	
	private double totalEdgeWeight(Graph g){
		double total = 0;
		for(Edge e : g.edges()){
			total += e.weight();
		}
		return total;
	}
	
	private static void println(Object s){
		System.out.println(s);
	}
	
	private static void print(Object s){
		System.out.print(s);
	}
	
	public static void main(String[] args) {
		SpanningTree s = new SpanningTree();
		s.run(args);
	}
}
