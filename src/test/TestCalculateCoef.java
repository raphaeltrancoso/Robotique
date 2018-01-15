package test;

import java.util.LinkedList;

public class TestCalculateCoef {

	public static void main(String[] args) {
		float coefDirecteur;
		String fieldsA[], fieldsB[];
		String A = "0,0";
		String B = "-2,2";
		fieldsA = A.split(",");
		fieldsB = B.split(",");
		coefDirecteur = (Integer.parseInt(fieldsB[0])- Integer.parseInt(fieldsA[0])) 
						/ (Integer.parseInt(fieldsB[1]) - Integer.parseInt(fieldsA[1]));
		System.out.println("Coefficient Directeur = "+coefDirecteur);
//		System.out.println(""+fieldsB[0]);
	}

}
