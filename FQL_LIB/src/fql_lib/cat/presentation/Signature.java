package fql_lib.cat.presentation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import catdata.algs.Pair;
import catdata.algs.Triple;
import catdata.algs.Unit;
import catdata.algs.kb.KB_Thue;
import fql_lib.DEBUG;
import fql_lib.FUNCTION;
import fql_lib.Util;
import fql_lib.cat.Category;
import fql_lib.cat.LeftKan;

//this requires finite denotations (paths always normalized)
public class Signature<O,A> implements Serializable {
	
	public class Node implements Serializable{
		public O name;
		
		public String toString() {
			return name.toString();
		}
		
		private Node() { }

		public Node(O o) {
			if (o == null) {
				throw new RuntimeException();
			}
			this.name = o;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
		//	result = prime * result + getOuterType().hashCode();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			
			return ((Node)obj).name.equals(name);
		} //does not take signature into account (doing so causes infinite loop)

	
	}
	
	public class Edge implements Serializable{
		public A name;
		public Node source;
		public Node target;
		
		public String toString() {
			return name.toString() + " : " + source + " -> " + target;
		}
		
		public Edge(A a, Node src, Node dst) {
			this.name = a;
			this.source = src;
			this.target = dst;
			if (!nodes.contains(src)) {
				throw new RuntimeException("Unknown node: " + src);
			}
			if (!nodes.contains(dst)) {
				throw new RuntimeException("Unknown node: " + dst);
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((target == null) ? 0 : target.hashCode());
			result = prime * result + ((source == null) ? 0 : source.hashCode());
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			Edge other = (Edge) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (target == null) {
				if (other.target != null)
					return false;
			} else if (!target.equals(other.target))
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			return true;
		}

		
	}
	
	public class Path implements Serializable {
		public Node source, target;
		public List<Edge> path;
		
		public void validate() {
			if (!nodes.contains(source)) {
				throw new RuntimeException("Bad node: " + source + " in " + Signature.this) ;
			}
			if (!nodes.contains(target)) {
				throw new RuntimeException("Bad node: " + target + " in " + Signature.this);
			}
			Node n = source;
			for (Edge e : path) {
				if (!e.source.equals(n)) {
					throw new RuntimeException("Bad path: " + this + " it is not valid in " + Signature.this);
				}
				n = e.target;
			}
			if (!n.equals(target)) {
				throw new RuntimeException("Bad path: " + this + " it is not valid in " + Signature.this);
			}
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((path == null) ? 0 : path.hashCode());
			result = prime * result + ((source == null) ? 0 : source.hashCode());
			return result;
		}
		
		public Path head() {
			return new Path(source, new LinkedList<Edge>());
		}
		
		/* private Path append(Edge e) {
			List<Edge> p = new LinkedList<>(path);
			p.add(e);
			return new Path(source, p);
		} */
		
		private Path(Path p1, Path p2) {
			this.source = p1.source;
			this.path = new LinkedList<>();
			Node n = source;
			for (Edge e : p1.path) {
				n = e.target;
				this.path.add(e);
			}
			if (!p2.source.equals(n)) {
				throw new RuntimeException("Cannot append: " + p1 + " and " + p2 + ", not equal: " + p2.source + " and " + n);
			}
			for (Edge e : p2.path) {
				n = e.target;
				this.path.add(e);
			}
			this.target = n;
			validate();
		}
		
		private Path(Edge e) {
			this.source = e.source;
			this.target = e.target;
			this.path = new LinkedList<>();
			this.path.add(e);
			validate();
		}
				
		private Path(Node n) {
			this.source = n;
			this.target = n;
			this.path = new LinkedList<>();
			validate();
		}
		
		private Path(Node source, List<Edge> path) {
			this.source = source;
			this.path = path;
			Node n = source;
			for (Edge e : path) {
				n = e.target;
			}
			this.target = n;
			validate();
		}
		
		
		private Path(O source, List<A> path) {
			this.source = new Node(source);
			this.path = new LinkedList<>();
			Node n = this.source;
			for (A a : path) {
				Edge e = getEdge(a); 
				n = e.target;
				this.path.add(e);
			}
			this.target = n;
			validate();
		}
		
		@Override
		 public String toString() {
			/* if (cat == null) {
				Pair<Category<Signature<O, A>.Node, Signature<O, A>.Path>, Function<Signature<O, A>.Path, Signature<O, A>.Path>> xxx = Signature.this.toCategory();
				cat = xxx.first;
				fn = xxx.second;
			}
			return fn.apply(this).toStringStrict(); */
			return toStringStrict();
		} 
		
		private String toStringStrict() {
			String ret = source.toString();
			for (Edge e : path) {
				ret += "." + e.name.toString();
			}
			return ret;	
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object o) {
			/* if (cat == null) {
				Pair<Category<Signature<O, A>.Node, Signature<O, A>.Path>, Function<Signature<O, A>.Path, Signature<O, A>.Path>> xxx = LeftKanCat.toCategory(Signature.this);
				cat = xxx.first;
				fn = xxx.second;
			}
			return fn.apply((Path)o).strict_equals(fn.apply(this)); */
			return strict_equals((Path)o);
		}
		
		public boolean strict_equals(Path obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			Path other = (Path) obj;
			
			if (path == null) {
				if (other.path != null)
					return false;
			} else if (!path.equals(other.path))
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			return true;
		}
		
		public Signature<O,A> sig() {
			return Signature.this;
		}
		
	}
	
	public Path path(Path p1, Path p2) {
		return normalize(new Path(p1, p2));
	}
	
	public Path path(Edge e) {
		return normalize(new Path(e));
	}
	
	public Path path(Node n) {
		return normalize(new Path(n));
	}
	
	public Path path(Path p1, Edge e) {
		return path(p1, new Path(e));
	}
	
	public Path path(O o, List<A> a) {
		return normalize(new Path(o, a));
	}
	
	public Edge getEdge(A a) {
		for (Edge e : edges) {
			if (e.name.equals(a)) {
				return e;
			}
		}
		throw new RuntimeException("Cannot find arrow " + a + " in " + this);
	}
		
	public Node getNode(O a) {
		for (Node e : nodes) {
			if (e.name.equals(a)) {
				return e;
			}
		}
		throw new RuntimeException("Cannot find object " + a + " in " + this);
	}
	
	public class Eq implements Serializable {
		public Path lhs, rhs;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((lhs == null) ? 0 : lhs.hashCode());
			result = prime * result + ((rhs == null) ? 0 : rhs.hashCode());
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			Eq other = (Eq) obj;
			
			if (lhs == null) {
				if (other.lhs != null)
					return false;
			} else if (!lhs.equals(other.lhs))
				return false;
			if (rhs == null) {
				if (other.rhs != null)
					return false;
			} else if (!rhs.equals(other.rhs))
				return false;
			return true;
		}


		public Eq(Path lhs, Path rhs) {
			this.lhs = lhs;
			this.rhs = rhs;
			if (!lhs.source.equals(rhs.source)) {
				throw new RuntimeException("Sources do not agree: " + lhs + " and " + rhs);
			}
			if (!lhs.target.equals(rhs.target)) {
				throw new RuntimeException("Targets do not agree: " + lhs + " and " + rhs);
			}
		}		
		
		@Override
		public String toString() {
			return lhs + " = " + rhs;
		}
	}
	
	public Set<Node> nodes = new HashSet<>();
	public Set<Edge> edges = new HashSet<>();
	public Set<Eq> eqs = new HashSet<>();
	
	private Path normalize(Path p) {
		return getCat().second.apply(p);
	}
	
	private Pair<Category<Node, Path>, FUNCTION<Path, Path>> cat;
	
	private Pair<Category<Node, Path>, FUNCTION<Path, Path>> getCat() {
		if (cat == null) {
			cat = toCategory();
		}
		return cat;
	}
	
	public Category<Node, Path> toCat() {
		return getCat().first;
	}
	
	public Signature(Set<O> nodes_str,
			Set<Triple<A, O, O>> arrows,
			Set<Pair<Pair<O,List<A>>, Pair<O,List<A>>>> equivs)  {

		Set<A> as = new HashSet<A>();
		
		for (O s : nodes_str) {
			Node n = new Node(s);
			if (nodes.contains(s)) {
				throw new RuntimeException("Duplicate node: " + s);
			}
			nodes.add(n);
		}

		for (Triple<A, O, O> arrow : arrows) {
			if (as.contains(arrow.first)) {
				throw new RuntimeException("Duplicate arrow: " + arrow.first);
			}
			as.add(arrow.first);
			Edge e = new Edge(arrow.first, new Node(arrow.second), new Node(arrow.third));
			edges.add(e);
		}

		for (Pair<Pair<O, List<A>>, Pair<O, List<A>>> equiv : equivs) {
			Path lhs = new Path(equiv.first.first, equiv.first.second);
			Path rhs = new Path(equiv.second.first, equiv.second.second);
			Eq eq = new Eq(lhs, rhs);
			eqs.add(eq);
		}
		
	} 

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((edges == null) ? 0 : edges.hashCode());
		result = prime * result + ((eqs == null) ? 0 : eqs.hashCode());
		result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Signature other = (Signature) obj;
		if (edges == null) {
			if (other.edges != null)
				return false;
		} else if (!edges.equals(other.edges))
			return false;
		if (eqs == null) {
			if (other.eqs != null)
				return false;
		} else if (!eqs.equals(other.eqs))
			return false;
		if (nodes == null) {
			if (other.nodes != null)
				return false;
		} else if (!nodes.equals(other.nodes))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		String x = "\n objects\n";
		boolean b = false;
		for (Node n : nodes) {
			if (b) {
				x += ",\n";
			}
			x += "  " + n;
			b = true;
		}

		x = x.trim();
		x += ";\n";
		x += " arrows\n";

		b = false;
		for (Edge a : edges) {
			if (b) {
				x += ",\n";
			}
			x += "  " + a.toString();
			b = true;
		}

		x = x.trim();
		x += ";\n";
		x += " equations\n";

		b = false;
		for (Eq a : eqs) {
			if (b) {
				x += ",\n";
			}
			x += "  " + a;
			b = true;
		}
		x = x.trim();
		return "{\n " + x + ";\n}";
	}
	
	
	//need to be careful, since the enclosed instance of the nodes and edges will not be this
	private Signature(Set<Signature<O, A>.Node> nodes,
			Set<Signature<O, A>.Edge> edges, Set<Signature<O, A>.Eq> eqs, Unit i) {
		this.nodes = nodes;
		this.edges = edges;
		this.eqs = eqs;
		
	}

	public Pair<Signature<O, A>, Mapping<O, A, O, A>> onlyObjects() {
		Signature<O,A> x = new Signature<>(nodes, new HashSet<Signature<O,A>.Edge>(), new HashSet<Signature<O, A>.Eq>(), new Unit());
		Map<Signature<O, A>.Node, Signature<O, A>.Node> nm = new HashMap<>();
		for (Signature<O, A>.Node k : nodes) {
			nm.put(k, k);
		}
		Map<Signature<O, A>.Edge, Signature<O, A>.Path> em = new HashMap<>();
		Mapping<O,A,O,A> m = new Mapping<O,A,O,A>(nm, em, x, this);
		return new Pair<>(x, m);
	}

	public Set<Edge> outEdges(Node n) {
		Set<Edge> ret = new HashSet<Edge>();
		for (Edge e : edges) {
			if (e.source.equals(n)) {
				ret.add(e);
			}
		}
		return ret;
	}

	private void doInfiniteCheck() {
		if (eqs.size() > 0) {
			return;
		}
		for (Signature<O, A>.Edge e : edges) {
			if (e.source.equals(e.target)) {
				throw new RuntimeException("Category is infinite (contains self-loop and no equations)");
			}
		}
	}
	
	private Pair<Category<Signature<O, A>.Node, Signature<O, A>.Path>, FUNCTION<Signature<O, A>.Path, Signature<O, A>.Path>> toCategory() {
		doInfiniteCheck();		
		
		Pair<Signature<O, A>, Mapping<O, A, O, A>> AF = onlyObjects();
		Signature<O, A> A = AF.first;
		Mapping<O, A, O, A> F = AF.second;
		Instance<O, A> X = Instance.terminal(A);

		LeftKan<O, A, O, A> lk = new LeftKan<>(0, F, X);
		if (!lk.compute()) {
			throw new RuntimeException(
					"Category computation has exceeded allowed iterations.");
		}

		return helper(lk);
	}

	private Pair<Category<Signature<O, A>.Node, Signature<O, A>.Path>, FUNCTION<Signature<O, A>.Path, Signature<O, A>.Path>> helper(
			LeftKan<O, A, O, A> lk) {
		// System.out.println("doing " + B);
		Set<Signature<O, A>.Node> objects = nodes;

		Set<Signature<O, A>.Path> arrows = new HashSet<>();
		Map<Signature<O, A>.Node, Signature<O, A>.Path> identities = new HashMap<>();

		final Function<Signature<O, A>.Path, Object> fn = makeFunction(lk);
		List<Signature<O, A>.Path> paths = new LinkedList<>();
		final Map<Object, Signature<O, A>.Path> fn2 = new HashMap<>();

		int numarrs = numarrs(lk);
		for (Signature<O, A>.Node n : nodes) {
			paths.add(new Path(n));
		}
		outer: for (int iter = 0; iter < DEBUG.debug.MAX_PATH_LENGTH; iter++) {
			for (Signature<O, A>.Path p : paths) {
				Object i = fn.apply(p);
				if (fn2.get(i) == null) {
					fn2.put(i, p);
				}
				if (fn2.size() == numarrs) {
					break outer;
				}
			}
			List<Signature<O, A>.Path> paths0 = new LinkedList<>();
			for (Signature<O, A>.Path p : paths) {
				for (Signature<O, A>.Edge e : outEdges(p.target)) {
					paths0.add(new Path(p, new Path(e)));
				}
			}
			paths = paths0;
		}

		if (fn2.size() < numarrs) {
			String old_str = "Basis path lengths exceed allowed limit ("
					+ DEBUG.debug.MAX_PATH_LENGTH
					+ ").  Only have "
					+ fn2.size()
					+ " basis paths out of required "
					+ numarrs
					+ "."
					+ "  Probable cause: using parallel or hybrid left-kan algorithm (see options).";
			throw new RuntimeException(old_str);
		}

		for (Object i : fn2.keySet()) {
			Signature<O, A>.Path p = fn2.get(i);
			arrows.add(p);
		}

		for (Signature<O, A>.Node n : objects) {
			Signature<O, A>.Path a = fn2.get(getOne(lk.ua2.get(n)).second);
			identities.put(n, a);
			for (Pair<Object, Object> i : lk.Pb2.get(n)) {
				Signature<O, A>.Path p = fn2.get(i.first);
				arrows.add(p);
			}
		}

		FUNCTION<Signature<O, A>.Path, Signature<O, A>.Path> r2 = x -> {
			if (fn2.get(fn.apply(x)) == null) {
				throw new RuntimeException("Given path " + x
						+ ", transforms to " + fn.apply(x)
						+ ", which is not in " + fn2);
			}
			return fn2.get(fn.apply(x));
		};

/*		Map<Pair<Signature<O, A>.Path, Signature<O, A>.Path>, Signature<O, A>.Path> composition = new HashMap<>();
		for (Signature<O, A>.Path x : arrows) {
			for (Signature<O, A>.Path y : arrows) {
				if (x.target.equals(y.source)) {
					composition.put(new Pair<>(x, y),
							r2.apply(B.new Path(x, y)));
				}
			}
		} */

		/* Map<Signature<O, A>.Path, Signature<O, A>.Node> sources = new HashMap<>();
		Map<Signature<O, A>.Path, Signature<O, A>.Node> targets = new HashMap<>();
		for (Signature<O, A>.Path p : arrows) {
			sources.put(p, p.source);
			targets.put(p, p.target);
		} */

//		Signature<Signature<O, A>.Node, Signature<O, A>.Path> injected = inject();
		
		Category<Signature<O, A>.Node, Signature<O, A>.Path> r1 = new Category<Signature<O, A>.Node, Signature<O, A>.Path>() {
			
			@Override
			public boolean isArrow(Signature<O, A>.Path o) {
				return arrows.contains(r2.apply(o));
			}

			@Override
			public Set<Signature<O, A>.Node> objects() {
				return objects;
			}

			@Override
			public Set<Signature<O, A>.Path> arrows() {
				return arrows;
			}

			@Override
			public Signature<O, A>.Node source(Signature<O, A>.Path a) {
				return r2.apply(a).source;
			}

			@Override
			public Signature<O, A>.Node target(Signature<O, A>.Path a) {
				return r2.apply(a).target;
			}

			@Override
			public Signature<O, A>.Path identity(Signature<O, A>.Node o) {
				return identities.get(o);
			}

			@Override
			public Signature<O, A>.Path compose(Signature<O, A>.Path a1, Signature<O, A>.Path a2) {
				Signature<O, A>.Path x = r2.apply(a1);
				Signature<O, A>.Path y = r2.apply(a2);
				return r2.apply(new Path(x, y));
			}
			
//			@Override
//			public Signature<Signature<O, A>.Node, Signature<O, A>.Path> toSig() {
//				return injected;
//			}
			
		};
		
		r1.origin = this;
		
//		FiniteCategory<Signature<O, A>.Node, Signature<O, A>.Path> r1 = new FiniteCategory<Signature<O, A>.Node, Signature<O, A>.Path>(
	//			objects, arrows, sources, targets, composition, identities);

		// System.out.println(r1);
		
		//TODO KB stuff
		
		return new Pair<>(r1, r2);
	}
	
	public void KB() {
		List<Path> paths = new LinkedList<>();
		for (Signature<O, A>.Node n : nodes) {
			paths.add(new Path(n));
		}
		for (int iter = 0; iter < DEBUG.debug.MAX_PATH_LENGTH; iter++) {
			List<Signature<O, A>.Path> paths0 = new LinkedList<>();
			for (Signature<O, A>.Path p : paths) {
				for (Signature<O, A>.Edge e : outEdges(p.target)) {
					paths0.add(new Path(p, new Path(e)));
				}
			}
			paths = paths0;
		}
		
		Set<Pair<List<Edge>, List<Edge>>> rules = new HashSet<>();
		for (Eq eq : eqs) {
			rules.add(new Pair<>(eq.lhs.path, eq.rhs.path));
		}
		KB_Thue<Edge> kb = new KB_Thue<>(rules, 100);
		for (Path path1 : paths) {
			for (Path path2 : paths) {
				if (path1.source.equals(path2.source) && path1.target.equals(path2.target)) {
					System.out.println("Trying " + path1 + " and " + path2);
					boolean b1 = normalize(path1).equals(normalize(path2));
					System.out.println("starting kb");
					boolean b2 = kb.equiv(path1.path, path2.path);
					if (b1 != b2) {
						throw new RuntimeException("Mismatch on " + path1 + " and " + path2 + " b1 " + b1 + " b2 " + b2);
					}
					System.out.println("ok");
				}
			}
		}

	}
	
	
	public Signature<Signature<O, A>.Node, Signature<O, A>.Path> inject() {
		Set<Triple<Signature<O, A>.Path,Signature<O, A>.Node,Signature<O, A>.Node>> arrows = new HashSet<>();
		for (Edge e : edges) {
			arrows.add(new Triple<>(new Path(e), e.source, e.target));
		}
		Set<Pair<Pair<Signature<O, A>.Node,List<Signature<O, A>.Path>>, Pair<Signature<O, A>.Node, List<Signature<O, A>.Path>>>> equivs = new HashSet<>();
		for (Eq eq : eqs) {
			equivs.add(new Pair<>(new Pair<>(eq.lhs.source, eq.lhs.path.stream().map(x -> new Path(x)).collect(Collectors.toList())), 
					              new Pair<>(eq.rhs.source, eq.rhs.path.stream().map(x -> new Path(x)).collect(Collectors.toList()))));
		}
		return new Signature<>(nodes, arrows, equivs);
	}

	private static <O, A> int numarrs(LeftKan<O, A, O, A> lk) {
		int ret = 0;

		for (Signature<O, A>.Node k : lk.Pb2.keySet()) {
			ret += lk.Pb2.get(k).size();
		}

		return ret;
	}

	private static <X> X getOne(Set<X> set) {
		if (set.size() != 1) {
			throw new RuntimeException("cannot get one from " + set);
		}
		for (X x : set) {
			return x;
		}
		throw new RuntimeException();
	}

	private static <O, A> FUNCTION<Signature<O, A>.Path, Object> makeFunction(
			final LeftKan<O, A, O, A> lk) {
		Map<Pair<Signature<O,A>.Node, List<Signature<O, A>.Edge>>, Object> cache = new HashMap<>();
		return p -> {
			if (cache.containsKey(new Pair<>(p.source, p.path))) {
				return cache.get(new Pair<>(p.source, p.path));
			}
			Set<Pair<Object, Object>> s = lk.eval2(p);
			Set<Pair<Object, Object>> set = Util.compose(lk.ua2.get(p.source), s);
			Object ret = getOne(set).second;
			cache.put(new Pair<>(p.source, p.path), ret);
			return ret;
		};
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
