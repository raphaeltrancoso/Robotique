package test;

import java.util.LinkedList;
import java.util.List;

public class testList {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> l = new LinkedList();
		l.add("premier arrive");
	    l.add("deuxieme arrive");
	    l.add("troisieme arrive");

	    for(int i = 0; i < l.size(); i++){
	      System.out.println("Élément à l'index " + i + " = " + l.get(i));
	  	}
	    l.remove(1);
	    for(int i = 0; i < l.size(); i++){
		      System.out.println(" après remove Élément à l'index " + i + " = " + l.get(i));
	  	}
	    l.add("voilà !");
	    for(int i = 0; i < l.size(); i++){
		      System.out.println(" après remove Élément à l'index " + i + " = " + l.get(i));
	  	}
	    System.out.println(((LinkedList<String>) l).getFirst());
	    System.out.println(((LinkedList<String>) l).getLast());

	}

}
