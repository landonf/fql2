package fql_lib.examples;

public class Patrick4Example extends Example {

	@Override
	public String isPatrick() {
		return "true";
	}
	
	@Override
	public String getName() {
		return "P Reverse";
	}

	@Override
	public String getText() {
		return s;
	}
	
	String s = "string : type"
			+ "\nnat : type"
			+ "\n"
			+ "\nlength : string -> nat"
			+ "\nreverse : string -> string "
			+ "\n"
			+ "\nC = schema {"
			+ "\n nodes X;"
			+ "\n edges att1 : X -> string, att2 : X -> nat;"
			+ "\n equations X.att1.length = X.att2;"
			+ "\n}"
			+ "\n"
			+ "\nF = mapping {"
			+ "\n nodes X -> X;"
			+ "\n edges att1 -> X.att1.reverse.reverse, att2 -> X.att2;"
			+ "\n} : C -> C"
			+ "\n"
			+ "\n//eq1 : reverse.reverse = string"
			+ "\n";



}
