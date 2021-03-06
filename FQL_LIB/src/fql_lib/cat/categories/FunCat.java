package fql_lib.cat.categories;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import catdata.algs.Chc;
import catdata.algs.Pair;
import catdata.algs.Unit;
import fql_lib.FUNCTION;
import fql_lib.cat.Category;
import fql_lib.cat.Functor;
import fql_lib.cat.Transform;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FunCat<O, A> extends Category<Functor<O, A, Category, Functor>, Transform<O, A, Category, Functor>> {
	// implements Products<Set, Fn>, Coproducts<Set, Fn>, Exponentials<Set, Fn>,
	// Subobjects<Set, Fn>, Isomorphisms<Set, Fn> {

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Inst)) {
			return false;
		}
		return ((Inst<?, ?>) o).cat.equals(cat);
	}

	private static Map map = new HashMap<>();
	public static <O,A> FunCat<O,A> get(Category<O,A> cat) {
		if (map.containsKey(cat)) {
			return (FunCat<O, A>) map.get(cat);
		}
		map.put(cat, new FunCat(cat));
		return (FunCat<O, A>) map.get(cat);
	}
	
	private FunCat(Category<O, A> cat) {
		if (cat.isInfinite()) {
			throw new RuntimeException("Cannot construct Cat^C for infinite C, given " + cat);
		}
		this.cat = cat;
	}

	public Category<O, A> cat;

	@Override
	public boolean isInfinite() {
		return true;
	}

	@Override
	public String toString() {
		return "(Cat ^ " + cat + ")";
	}

	@Override
	public void validate() {
	}

	@Override
	public Set<Functor<O, A, Category, Functor>> objects() {
		throw new RuntimeException("Cannot enumerate objects of " + this);
	}

	@Override
	public Set<Transform<O, A, Category, Functor>> arrows() {
		throw new RuntimeException("Cannot enumerate arrows of " + this);
	}

	@Override
	public Functor<O, A, Category, Functor> source(Transform<O, A, Category, Functor> a) {
		return a.source;
	}

	@Override
	public Functor<O, A, Category, Functor> target(Transform<O, A, Category, Functor> a) {
		return a.target;
	}

	@Override
	public Transform<O, A, Category, Functor> identity(Functor<O, A, Category, Functor> o) {
		return Transform.id(o);
	}

	@Override
	public Transform<O, A, Category, Functor> compose(Transform<O, A, Category, Functor> f, Transform<O, A, Category, Functor> g) {
		return Transform.compose(f, g);
	}

	public Functor<O, A, Category, Functor> terminal() {
		return new Functor(cat, FinCat.FinCat, x -> FinCat.FinCat.terminal(), x -> FinCat.FinCat.terminal(FinCat.FinCat.terminal()));
	}

	public Transform<O, A, Category, Functor> terminal(Functor<O, A, Category, Functor> o) {
		FUNCTION<O, Functor> fn = x -> new Functor<>(o.applyO(x), FinCat.FinCat.terminal(), y -> new Unit(), y -> new Unit());
		return new Transform(o, terminal(), fn);
	}
	
	public Functor<O, A, Category, Functor> initial() {
		return new Functor(cat, FinCat.FinCat, x -> FinCat.FinCat.initial(), x -> FinCat.FinCat.initial(FinCat.FinCat.initial()));
	}
		 
	public Transform<O, A, Category, Functor> initial(Functor<O, A, Category, Functor> o) {
		FUNCTION<O, Functor> fn = x -> new Functor<>(FinCat.FinCat.initial(), o.applyO(x), y -> { throw new RuntimeException(); }, y -> { throw new RuntimeException(); });
		return new Transform(initial(), o, fn);
	}

	public Functor<O, A, Category, Functor> product(Functor<O, A, Category, Functor> f, Functor<O, A, Category, Functor> g) {
		FUNCTION<O, Category> h = x -> FinCat.FinCat.product(f.applyO(x), g.applyO(x));
		FUNCTION<A, Functor> i = x -> FinCat.FinCat.pairF(f.applyA(x), g.applyA(x));
		return new Functor(cat, FinCat.FinCat, h, i);
	}
	
	public Functor<O, A, Category, Functor> coproduct(Functor<O, A, Category, Functor> f, Functor<O, A, Category, Functor> g) {
		FUNCTION<O, Category> h = x -> FinCat.FinCat.coproduct(f.applyO(x), g.applyO(x));
		FUNCTION<A, Functor> i = x -> FinCat.FinCat.matchF(f.applyA(x), g.applyA(x));
		return new Functor(cat, FinCat.FinCat, h, i);
	}
	
	public Transform<O, A, Category, Functor> first(Functor<O, A, Category, Functor> o1, Functor<O, A, Category, Functor> o2) {
		FUNCTION<O, Functor> f = o -> new Functor<>(FinCat.FinCat.product(o1.applyO(o),
				o2.applyO(o)), o1.applyO(o), x -> x.first, x -> x.first);
		return new Transform(product(o1, o2), o1, f);
	}
	public Transform<O, A, Category, Functor> second(Functor<O, A, Category, Functor> o1, Functor<O, A, Category, Functor> o2) {
		FUNCTION<O, Functor> f = o -> new Functor<>(FinCat.FinCat.product(o1.applyO(o),
				o2.applyO(o)), o2.applyO(o), x -> x.second, x -> x.second);
		return new Transform(product(o1, o2), o2, f);
	}
	
	public Transform<O, A, Category, Functor> inleft(Functor<O, A, Category, Functor> o1, Functor<O, A, Category, Functor> o2) {
		FUNCTION<O, Functor> f = o -> new Functor<>(o1.applyO(o), FinCat.FinCat.coproduct(o1.applyO(o),
				o2.applyO(o)), x -> Chc.inLeft(x), x -> Chc.inLeft(x));
		return new Transform(o1, coproduct(o1, o2), f);
	}

	public Transform<O, A, Category, Functor> inright(Functor<O, A, Category, Functor> o1, Functor<O, A, Category, Functor> o2) {
		FUNCTION<O, Functor> f = o -> new Functor<>(o2.applyO(o), FinCat.FinCat.coproduct(o1.applyO(o),
				o2.applyO(o)), x -> Chc.inRight(x), x -> Chc.inRight(x));
		return new Transform(o2, coproduct(o1, o2), f);
	}
	
	public Transform<O, A, Category, Functor> pair(Transform<O, A, Category, Functor> f, Transform<O, A, Category, Functor> g) {
		if (!f.source.equals(g.source)) {
			throw new RuntimeException();
		}
		FUNCTION<O, Functor> fn = o -> 
		  new Functor<>(f.source.applyO(o), FinCat.FinCat.product(f.target.applyO(o), g.target.applyO(o)), 
				  x -> new Pair<>(f.apply(o).applyO(x), g.apply(o).applyO(x)), 
				  x -> new Pair<>(f.apply(o).applyA(x), g.apply(o).applyA(x)));
		return new Transform(f.source, product(f.target, g.target), fn);
	}
	public Transform<O, A, Category, Functor> match(Transform<O, A, Category, Functor> f, Transform<O, A, Category, Functor> g) {
		if (!f.target.equals(g.target)) {
			throw new RuntimeException();
		}
		FUNCTION<O, Functor> fn = o -> new Functor<>(FinCat.FinCat.coproduct(f.source.applyO(o), g.source.applyO(o)), 
				f.target.applyO(o), 
				x -> x.left ? f.apply(o).applyO(x.l) : g.apply(o).applyO(x.r),
				x -> x.left ? f.apply(o).applyA(x.l) : g.apply(o).applyA(x.r));
		return new Transform(coproduct(f.source, g.source), f.target, fn);
	}

	
}
