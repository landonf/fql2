package fql_lib.cat.categories;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fql_lib.cat.Category;
import fql_lib.cat.presentation.FPTransform;
import fql_lib.cat.presentation.Instance;
import fql_lib.cat.presentation.Signature;

@SuppressWarnings({"unchecked", "rawtypes"})
public class FPInst<O, A> extends Category<Instance<O, A>, FPTransform<O, A>> {

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof FPInst)) {
			return false;
		}
		return ((FPInst<?, ?>) o).cat.equals(cat);
	}

	private static Map<Object, Object> map = new HashMap<>();
	public static <O,A> FPInst<O,A> get(Signature<O,A> cat) {
		if (map.containsKey(cat)) {
			return (FPInst<O, A>) map.get(cat);
		}
		map.put(cat, new FPInst(cat));
		return (FPInst<O, A>) map.get(cat);
	}
	
	private FPInst(Signature<O, A> cat) {
		this.cat = cat;
	}

	public Signature<O, A> cat;

	@Override
	public boolean isInfinite() {
		return true;
	}

	@Override
	public String toString() {
		return "(Set ^ " + cat + ")";
	}

	@Override
	public void validate() {
	}

	@Override
	public Set<Instance<O, A>> objects() {
		throw new RuntimeException("Cannot enumerate objects of " + this);
	}

	@Override
	public Set<FPTransform<O, A>> arrows() {
		throw new RuntimeException("Cannot enumerate arrows of " + this);
	}
	
	@Override
	public boolean isObject(Instance<O, A> o) {
		return o.thesig.equals(cat);
	}
	@Override
	public boolean isArrow(FPTransform<O, A> a) {
		return isObject(a.src) && isObject(a.dst);
	}

	@Override
	public Instance<O, A> source(FPTransform<O, A> a) {
		return a.src;
	}

	@Override
	public Instance<O, A> target(FPTransform<O, A> a) {
		return a.dst;
	}

	@Override
	public FPTransform<O, A> identity(Instance<O, A> o) {
		return FPTransform.id(o);
	}

	@Override
	public FPTransform<O, A> compose(FPTransform<O, A> f, FPTransform<O, A> g) {
		return FPTransform.compose(f, g);
	}

	//TODO products, exponentials for FPInst

}
