package fql_lib.cat.properties;

public interface Exponentials<O,A> {

	public O exp(O o1, O o2);
	
	public A eval(O o1, O o2);
	
	public A curry(A a);
	
}
