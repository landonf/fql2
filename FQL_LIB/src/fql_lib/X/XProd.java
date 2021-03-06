package fql_lib.X;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import catdata.algs.Chc;
import catdata.algs.Pair;
import catdata.algs.Triple;
import catdata.algs.Unit;
import fql_lib.Util;
import fql_lib.X.XExp.FLOWER2;
import fql_lib.X.XExp.Flower;
import fql_lib.X.XExp.XBool;
import fql_lib.X.XPoly.Block;

public class XProd {
	
	public static <X> XCtx<X> zero(XCtx<X> S) {
		return new XCtx<X>(new HashSet<>(), new HashMap<>(), new HashSet<>(), S.global, S, "instance");
	}
	
	public static <X> XCtx<X> one(XCtx<X> S) {
		if (S.schema != null) {
			throw new RuntimeException("foovar");
		}
		Map<X, Pair<X, X>> tys = new HashMap<>();
		Set<Pair<List<X>, List<X>>> eqs = new HashSet<>();
		
		for (X O : S.allIds()) {
			X x = (X) ("1_" + O);
			tys.put(x, new Pair<>((X)"_1", O));
			for (X f : S.allTerms()) {
				if (!S.type(f).first.equals(O)) {
					continue;
				}			
				X y = (X) ("1_" + S.type(f).second);
				List<X> lhs = new LinkedList<>();
				lhs.add(y);
				List<X> rhs = new LinkedList<>();
				rhs.add(x);
				rhs.add(f);
				eqs.add(new Pair<>(lhs, rhs));
			}
		}
		
		return new XCtx<X>(new HashSet<>(), tys, eqs, S.global, S, "instance");
	}
	
	public static <X> XMapping<X,X> tt(XCtx<X> I) {
		if (I.schema == null) {
			throw new RuntimeException("Not an instance");
		}
		
		Map em = new HashMap();
		
		for (X x : I.terms()) {
			Pair<X,X> t = I.type(x);
			if (!t.first.equals("_1")) {
				throw new RuntimeException();
			}
			X y = (X) ("1_" + t.second);
			List<X> l = new LinkedList<>();
			l.add(y);
			em.put(x, l);
		}
		
		for (X x : I.schema.allTerms()) {
			if (em.containsKey(x)) {
				continue;
			}
			List l = new LinkedList<>();
			l.add(x);
			em.put(x, l);
		}

		XMapping ret = new XMapping<X,X>(I, one(I.schema), em, "homomorphism");
		return ret;
	}
	
	public static <X> XMapping<X,X> ff(XCtx<X> I) {
		if (I.schema == null) {
			throw new RuntimeException();
		}
		
		Map em = new HashMap();
		
		for (X x : I.schema.allTerms()) {
			List l = new LinkedList<>();
			l.add(x);
			em.put(x, l);
		}

		return new XMapping<X,X>(zero(I.schema), I, em, "homomorphism");
	}
	
	public static <A,B,C> XMapping<Chc<A,B>, C> match(XMapping<A,C> l, XMapping<B,C> r) {
		if (!l.dst.equals(r.dst)) {
			throw new RuntimeException();
		}
		XCtx<Chc<A,B>> src = coprod(l.src, r.src);
		
		Map em = new HashMap<>();
		for (Chc<A,B> x : src.terms()) {
			if (x.left) {
				em.put(x, l.em.get(x.l));
			} else {
				em.put(x, r.em.get(x.r));
			}
		}
		
		for (Object x : src.allTerms()) {
			if (em.containsKey(x)) {
				continue;
			}
			List z = new LinkedList<>();
			z.add(x);
			em.put(x, z); 
		}

		
		return new XMapping<Chc<A,B>, C>(src, l.dst, em, "homomorphism");
	}
	
	public static <X,Y> XMapping<X, Chc<X,Y>> inl(XCtx<X> I, XCtx<Y> J) {
		XCtx<Chc<X,Y>> IJ = coprod(I,J);
		Map<X, List<Chc<X,Y>>> em = new HashMap<>();
		
		for (X x : I.types.keySet()) {
			List<Chc<X,Y>> l = new LinkedList<>();
			l.add(Chc.inLeft(x));
			em.put(x, l);
		}
		
		for (X x : I.allTerms()) {
			if (em.containsKey(x)) {
				continue;
			}
			List l = new LinkedList<>();
			l.add(x);
			em.put(x, l);
		}
		
		return new XMapping<>(I, IJ, em, "homomorphism");
	}
	
	public static <X,Y> XMapping<Y, Chc<X,Y>> inr(XCtx<X> I, XCtx<Y> J) {
		XCtx<Chc<X,Y>> IJ = coprod(I,J);
		Map<Y, List<Chc<X,Y>>> em = new HashMap<>();
		
		for (Y x : J.types.keySet()) {
			List<Chc<X,Y>> l = new LinkedList<>();
			l.add(Chc.inRight(x));
			em.put(x, l);
		}
		
		for (Y x : J.allTerms()) {
			if (em.containsKey(x)) {
				continue;
			}
			List l = new LinkedList<>();
			l.add(x);
			em.put(x, l);
		}
		
		return new XMapping<>(J, IJ, em, "homomorphism");
	}
	
	public static <X> XCtx<Pair<Triple<X, X, List<X>>,Triple<X, X, List<X>>>> prod(XCtx<X> I, XCtx<X> J) {
		if (I.global == null || J.global == null) {
			throw new RuntimeException();
		}
		if (!I.global.equals(J.global)) {
			throw new RuntimeException();
		}
		if (I.schema == null || J.schema == null) {
			throw new RuntimeException();
		}
		if (!I.schema.equals(J.schema)) {
			throw new RuntimeException();
		}
		
		Set ids = new HashSet<>();
		Set<Pair<List<Pair<Triple<X, X, List<X>>, Triple<X, X, List<X>>>>, List<Pair<Triple<X, X, List<X>>, Triple<X, X, List<X>>>>>> eqs = new HashSet<>();
		Map types = new HashMap<>();
		/* for each pair (i,j) of type X and each generating
edge f:X->Y in S (including edges in type, like length or succ),
(i,j);f = (i;f, j;f). */
		for (X x :I.schema.allIds()) {
			Set<Pair<Triple<X,X,List<X>>, Triple<X,X,List<X>>>> s = new HashSet<>();
			for (Triple<X, X, List<X>> i : I.cat().hom((X)"_1", x)) {
				for (Triple<X, X, List<X>> j : J.cat().hom((X)"_1", x)) {
					types.put(new Pair<>(i,j), new Pair<>("_1", x));
					s.add(new Pair<>(i,j));
				}
			}
			
			for (X f : I.schema.allTerms()) {
				Pair<X,X> t = I.type(f);
				if (!t.first.equals(x)) {
					continue;
				}
				for (Pair<Triple<X,X,List<X>>, Triple<X,X,List<X>>> xy : s) {
					List lhs = new LinkedList();
					lhs.add(xy);
					lhs.add(f);

					List<X> l1 = new LinkedList<>();
					l1.add(xy.first.first);
					l1.addAll(xy.first.third);
					l1.add(f);
					Triple<X,X,List<X>> tofind1 = new Triple<>((X)"_1", t.second, l1);
					Triple<X,X,List<X>> found1 = I.find_fast(tofind1);
					if (found1 == null) {
						throw new RuntimeException("foudn1");
					}
					
					List<X> l2 = new LinkedList<>();
					l2.add(xy.second.first);
					l2.addAll(xy.second.third);
					l2.add(f);
					Triple<X,X,List<X>> tofind2 = new Triple<>((X)"_1", t.second, l2);
					Triple<X,X,List<X>> found2 = J.find_fast(tofind2);
					if (found2 == null) {
						throw new RuntimeException("ouns 2");
					}
					
					Pair rhs0 = new Pair<>(found1, found2);
					List rhs = new LinkedList();
					rhs.add(rhs0);
					
					eqs.add(new Pair<>(lhs, rhs));
				}
			}
		}
		
		return new XCtx<Pair<Triple<X, X, List<X>>,Triple<X, X, List<X>>>>(ids, types, eqs, (XCtx)I.global, (XCtx)I.schema, "instance");
		
	}

	public static <X,Y> XCtx<Chc<X,Y>> coprod(XCtx<X> I, XCtx<Y> J) {
		if (I.global == null || J.global == null) {
			throw new RuntimeException();
		}
		if (!I.global.equals(J.global)) {
			throw new RuntimeException();
		}
		if (I.schema == null || J.schema == null) {
			throw new RuntimeException();
		}
		if (!I.schema.equals(J.schema)) {
			throw new RuntimeException();
		}
		
		Set<Chc<X,Y>> ids = new HashSet<>();
		
		Map<Chc<X,Y>, Pair<Chc<X,Y>, Chc<X,Y>>> types = new HashMap<>();
		for (X x : I.types.keySet()) {
			Pair y = I.types.get(x);
			types.put(Chc.inLeft(x), y);
		}
		for (Y x : J.types.keySet()) {
			Pair y = J.types.get(x);
			types.put(Chc.inRight(x), y);
		}
		
		Set<Pair<List<Chc<X,Y>>, List<Chc<X,Y>>>> eqs = new HashSet<>();
		for (Pair<List<X>, List<X>> eq : I.eqs) {
			List lhs = eq.first.stream().map(x -> I.terms().contains(x) ? Chc.inLeft(x) : x).collect(Collectors.toList());
			List rhs = eq.second.stream().map(x -> I.terms().contains(x) ? Chc.inLeft(x) : x).collect(Collectors.toList());
			eqs.add(new Pair(lhs, rhs));
		}
		for (Pair<List<Y>, List<Y>> eq : J.eqs) {
			List lhs = eq.first.stream().map(x -> J.terms().contains(x) ? Chc.inRight(x) : x).collect(Collectors.toList());
			List rhs = eq.second.stream().map(x -> J.terms().contains(x) ? Chc.inRight(x) : x).collect(Collectors.toList());
			eqs.add(new Pair(lhs, rhs));
		}
		
		XCtx<Chc<X,Y>> ret = new XCtx<Chc<X,Y>>(ids, types, eqs, (XCtx)I.global, (XCtx)I.schema, "instance");
		ret.saturated = I.saturated && J.saturated;
		return ret;
	}
	
	public static <X,Y> XMapping fst(XCtx<X> I, XCtx<X> J) {
		XCtx<Pair<Triple<X,X,List<X>>,Triple<X,X,List<X>>>> IJ = prod(I,J);
		Map em = new HashMap<>();
		
		for (Pair<Triple<X, X, List<X>>, Triple<X, X, List<X>>> x : IJ.types.keySet()) {
			List l = new LinkedList<>();
			l.add(x.first.first);
			l.addAll(x.first.third);
			em.put(x, l);
		}
		
		for (Object x : IJ.allTerms()) {
			if (em.containsKey(x)) {
				continue;
			}
			List l = new LinkedList<>();
			l.add(x);
			em.put(x, l);
		}
		
		return new XMapping<>(IJ, I, em, "homomorphism");
	}
	
	public static <X,Y> XMapping snd(XCtx<X> I, XCtx<X> J) {
		XCtx<Pair<Triple<X,X,List<X>>,Triple<X,X,List<X>>>> IJ = prod(I,J);
		Map em = new HashMap<>();
		
		for (Pair<Triple<X, X, List<X>>, Triple<X, X, List<X>>> x : IJ.types.keySet()) {
			List l = new LinkedList<>();
			l.add(x.second.first);
			l.addAll(x.second.third);
			em.put(x, l);
		}
		
		for (Object x : IJ.allTerms()) {
			if (em.containsKey(x)) {
				continue;
			}
			List l = new LinkedList<>();
			l.add(x);
			em.put(x, l);
		}
		
		return new XMapping<>(IJ, J, em, "homomorphism");
	}
	
	public static <A,B,C> XMapping<A, ?> pair(XMapping<A, A> l, XMapping<A, A> r) {
		if (!l.src.equals(r.src)) {
			throw new RuntimeException();
		}
		XCtx<Pair<Triple<A,A,List<A>>,Triple<A,A,List<A>>>> dst = prod(l.dst, r.dst);
		
		Map em = new HashMap<>();
		for (A x : l.src.terms()) {
			List<A> x1 = l.em.get(x);
			Triple t1 = l.dst.find_fast(new Triple<A,A,List<A>>((A)"_1", l.dst.type(x1).second, x1));

			List<A> x2 = r.em.get(x);
			Triple t2 = r.dst.find_fast(new Triple<A,A,List<A>>((A)"_1", r.dst.type(x2).second, x2));

			List xl = new LinkedList();
			xl.add(new Pair<>(t1, t2));
			em.put(x, xl);
		}
		
		for (Object x : l.src.allTerms()) {
			if (em.containsKey(x)) {
				continue;
			}
			List z = new LinkedList<>();
			z.add(x);
			em.put(x, z); 
		}

		
		return new XMapping(l.src, dst, em, "homomorphism");
	}

	//going to need bigger schema here, for typing
/*	public static <C> Triple<C, C, List<C>> eval(List<String> eq, Map<String, Triple<C, C, List<C>>> tuple, XCtx S, XCtx<C> I) {
		Triple<C, C, List<C>> sofar = tuple.get(eq.get(0));
		if (sofar == null) {
			sofar = I.type(c)
		}

		///if (sofar == null) {
		//	return null;
		//}

		
	//	String s = eq.get(0);
		
		for (String edge : eq.subList(1, eq.size())) {
			Triple<C, C, List<C>> t = tuple.get(edge);
			if (t != null) {
				if (sofar == null) {
					sofar = t;
					continue;
				}
				sofar = I.cat().compose(sofar, t);
				continue;
			} 
			
			
		}
		
		return sofar;
	}
	*/
	
	public static <C,D,E> List subst_new(List<D> eq, Map<E, Triple<C, C, List<C>>> tuple, Set<E> keys, Set xxx) {
		List ret = eq.stream().flatMap(x -> { 
			List l = new LinkedList<>();
			if (tuple.containsKey(x)) {
				l.add(tuple.get(x).first);
				l.addAll(tuple.get(x).third);
				return l.stream();
			} else if (keys.contains(x)) { 
				l.add(x);
				xxx.add(new Unit());
				return l.stream();
			}	else {
				l.add(x);
				return l.stream();
			}
		}).collect(Collectors.toList());
		
		return ret;
	} 
	
	 public static <C> List subst2(String yyy, List<String> eq, Map<String, Triple<C, C, List<C>>> tuple, Set<String> keys, Set xxx) {
		List ret = eq.stream().flatMap(x -> { 
			List l = new LinkedList<>();
			if (x.equals("!__Q")) {
				l.add(yyy);
				return l.stream();
			}
			if (tuple.containsKey(x)) {
				l.add(yyy);
				l.add(tuple.get(x).first);
				l.addAll(tuple.get(x).third);
				return l.stream();
			} else if (keys.contains(x)) { 
				l.add(x);
				xxx.add(new Unit());
				return l.stream();
			}	else {
				l.add(x);
				return l.stream();
			}
		}).collect(Collectors.toList());
		
		return ret;
	} 
	
	 public static <C> List subst(List<String> eq, Map<String, Triple<C, C, List<C>>> tuple) {
		List ret = eq.stream().flatMap(x -> { 
			List l = new LinkedList<>();
			if (tuple.containsKey(x)) {
				l.add("!__Q");
				l.add(tuple.get(x).first);
				l.addAll(tuple.get(x).third);
				return l.stream();
			} else {
				l.add(x);
				return l.stream();
			}
		}).collect(Collectors.toList());
		
		return ret;
	} 

	 //TODO: empty FROM
	 //FROM schema, SELECT schema, 
/*	public static <C> XCtx fast_flower(Flower flower, XCtx<C> I, XCtx<C> S, XCtx<C> Z) {
		Set<C> ids = new HashSet<>();
		ids.addAll(S.ids);
		ids.addAll(I.ids);
		ids.addAll(I.global.ids);
		Map types = new HashMap<>();
		types.putAll(S.types);
		types.putAll(I.types);
		types.putAll(I.global.types);
		Set<Pair<List<C>, List<C>>> eqs = new HashSet<>();
		eqs.addAll(S.eqs);
		eqs.addAll(I.eqs);
		eqs.addAll(I.global.eqs);
		
		XCtx<C> IS = new XCtx<C>(ids, types, eqs, I.global, S, "instance");
			
		Set<Map<String, Triple<C, C, List<C>>>> ret = new HashSet<>();
		Map<String, Triple<C, C, List<C>>> m = new HashMap<>();
		ret.add(m);
		
		for (String var : flower.from.keySet()) {
			String node = flower.from.get(var);
			Set<Map<String, Triple<C, C, List<C>>>> ret2 = new HashSet<>();
			for (Map<String, Triple<C, C, List<C>>> tuple : ret) {
				outer: for (Triple<C, C, List<C>> t : I.cat().hom((C)"_1", (C)node)) {
					Map<String, Triple<C, C, List<C>>> merged = new HashMap<>(tuple);
					merged.put(var, t);
					
					for (Pair<List<String>, List<String>> eq : flower.where) {
						Set xxx = new HashSet();
						List lhs = subst2("!__Q", eq.first, merged, flower.from.keySet(), xxx);
						Set yyy = new HashSet();
						List rhs = subst2("!__Q", eq.second, merged, flower.from.keySet(), yyy);
						if (xxx.isEmpty() && yyy.isEmpty() && !IS.getKB().equiv(lhs, rhs)) {
							continue outer;
						}
					}
					ret2.add(merged);
				}
			}
			ret = ret2;
		}
		
		ids = new HashSet<>();
		types = new HashMap<>();
		eqs = new HashSet<>();
		for (Map<String, Triple<C, C, List<C>>> k : ret) {
			types.put(k, new Pair("_1", "_Q"));
			for (String edge : flower.from.keySet()) {
				List lhs = new LinkedList();
				lhs.add(k);
				lhs.add(edge);
				List rhs = new LinkedList();
				rhs.add(k.get(edge).first);
				rhs.addAll(k.get(edge).third);
				eqs.add(new Pair(lhs, rhs));
			}
		} 
		
		//this is the from/where part
		XCtx<C> J = new XCtx<C>(ids, types, eqs, I.global, IS, "instance");
		J.saturated = I.saturated; 
		return J;
	}
	
*/
	 /*
	public static <C> XCtx fast_flower(Set<Flower> flowers, XCtx<C> I, XCtx<C> S, XCtx<C> Z) {
		Set<C> ids = new HashSet<>();
		ids.addAll(S.ids);
		ids.addAll(I.ids);
		ids.addAll(I.global.ids);
		Map types = new HashMap<>();
		types.putAll(S.types);
		types.putAll(I.types);
		types.putAll(I.global.types);
		Set<Pair<List<C>, List<C>>> eqs = new HashSet<>();
		eqs.addAll(S.eqs);
		eqs.addAll(I.eqs);
		eqs.addAll(I.global.eqs);
		
		XCtx<C> IS = new XCtx<C>(ids, types, eqs, I.global, S, "instance");
			
		int i = 0;
		Map<Flower, Set<Map<String, Triple<C, C, List<C>>>>> rets = new HashMap<>();
		for (Flower flower : flowers) {
			String qi = "_Q" + i;
			Set<Map<String, Triple<C, C, List<C>>>> ret = new HashSet<>();
			Map<String, Triple<C, C, List<C>>> m = new HashMap<>();
			ret.add(m);

			for (String var : flower.from.keySet()) {
				String node = flower.from.get(var);
				Set<Map<String, Triple<C, C, List<C>>>> ret2 = new HashSet<>();
				for (Map<String, Triple<C, C, List<C>>> tuple : ret) {
					outer: for (Triple<C, C, List<C>> t : I.cat().hom((C)"_1", (C)node)) {
						Map<String, Triple<C, C, List<C>>> merged = new HashMap<>(tuple);
						merged.put(var, t);
						for (Pair<List<String>, List<String>> eq : flower.where) {
							Set xxx = new HashSet();
							List lhs = subst2("!_" + qi, eq.first, merged, flower.from.keySet(), xxx);
							Set yyy = new HashSet();
							List rhs = subst2("!_" + qi, eq.second, merged, flower.from.keySet(), yyy);
							if (xxx.isEmpty() && yyy.isEmpty() && !IS.getKB().equiv(lhs, rhs)) {
								continue outer;
							}
						}
						ret2.add(merged);
					}
				}
				ret = ret2;
			}
			rets.put(flower, ret);
			i++;
		}
		
		ids = new HashSet<>();
		types = new HashMap<>();
		eqs = new HashSet<>();
		
		i = 0;
		for (Flower flower : flowers) {
			String qi = "_Q" + i;
			Set<Map<String, Triple<C, C, List<C>>>> ret = rets.get(flower);
			for (Map<String, Triple<C, C, List<C>>> k : ret) {
				types.put(k, new Pair("_1", qi));
				for (String edge : flower.from.keySet()) {
					List lhs = new LinkedList();
					lhs.add(k);
					lhs.add(qi + edge);
					List rhs = new LinkedList();
					rhs.add(k.get(edge).first);
					rhs.addAll(k.get(edge).third);
					eqs.add(new Pair(lhs, rhs));
				}
			} 
			i++;
		}
		
		//this is the from/where part
		XCtx<C> J = new XCtx<C>(ids, types, eqs, I.global, IS, "instance");
		J.saturated = I.saturated; 
		return J;
	} */
	
/*	public static <C> XCtx fast_flower(Flower flower, XCtx<C> I) {
		Set<Map<String, Triple<C, C, List<C>>>> ret = new HashSet<>();
		Map<String, Triple<C, C, List<C>>> m = new HashMap<>();
		ret.add(m);
		
		
		for (String var : flower.from.keySet()) {
			String node = flower.from.get(var);
			Set<Map<String, Triple<C, C, List<C>>>> ret2 = new HashSet<>();
			for (Map<String, Triple<C, C, List<C>>> tuple : ret) {
				for (Triple<C, C, List<C>> t : I.cat().hom((C)"_1", (C)node)) {
					Map<String, Triple<C, C, List<C>>> merged = new HashMap<>(tuple);
					merged.put(var, t);
					boolean add = true;
					for (Pair<List<String>, List<String>> eq : flower.where) {
						Object lhs = eval(eq.first, merged, I);
						Object rhs = eval(eq.second, merged, I);
						if (lhs != null && rhs != null && !lhs.equals(rhs)) {
							add = false;
							break;
						}
					}
					if (add) {
						ret2.add(merged);
					}
				}
			}
			ret = ret2;
		}
		
		
		throw new RuntimeException();
	} */
	
	private static void typecheck(XBool where, XCtx S) {
		if (where.not != null) {
			typecheck(where.not, S);
			return;
		}
		if (where.l != null && where.r != null) {
			typecheck(where.l, S);
			typecheck(where.r, S);
			return;
		}
		if (where.lhs != null && where.rhs != null) {
			S.type(where.lhs);
			S.type(where.rhs);
		}
	}
	
	public static XCtx frozen(FLOWER2 flower, XCtx S) {
		Set ids = new HashSet<>();
		Map types = new HashMap<>();
		Set eqs = new HashSet<>();
		
		for (Entry k : flower.from.entrySet()) {
			types.put(k.getKey(), new Pair<>("_1", k.getValue()));
		}
		//eqs.addAll(from.where);
		
		XCtx ret = new XCtx(ids, types, eqs, S.global, S, "instance");
		
		for (Object k : flower.select.keySet()) {
			List v = flower.select.get(k);
			ret.type(v);
		}
		
		typecheck(flower.where, ret);
		
		return ret;
	}
	
	public static XCtx frozen(Block flower, XCtx S) {
		Set ids = new HashSet<>();
		Map types = new HashMap<>();
		Set eqs = new HashSet<>();
		
		for (Object k0 : flower.from.entrySet()) {
			Entry k = (Entry) k0;
			types.put(k.getKey(), new Pair<>("_1", k.getValue()));
		}
		//eqs.addAll(from.where);
		
		XCtx ret = new XCtx(ids, types, eqs, S.global, S, "instance");
		
		typecheck(convert2(flower.where), ret);
		
		for (Object k : flower.attrs.keySet()) {
			Object v = flower.attrs.get(k);
			ret.type((List)v);
		}
				
		//TODO: typecheck edges?
		
		return ret;
	}

	
	public static <C> String eval(XExp.XBool where, Map<Object, Triple<C, C, List<C>>> k, Set<Object> keys, XCtx I) {
		if (where.isFalse != null) {
			return "false";
		}
		if (where.isTrue != null) {
			return "true";
		}
		if (where.not != null) {
			String x = eval(where.not, k, keys, I);
			if (x.equals("true")) {
				return "false";
			}
			if (x.equals("false")) {
				return "true";
			}
			return x;
		}
		if (where.l != null && where.r != null) {
			String l = eval(where.l, k, keys, I);
			String r = eval(where.r, k, keys, I);
			if (where.isAnd) {
				if (l.equals("false") || r.equals("false")) {
					return "false";
				}
				if (l.equals("true") && r.equals("true")) {
					return "true";
				}
				return "unknown";
			} else {
				if (l.equals("true") || r.equals("true")) {
					return "true";
				}
				if (l.equals("false") && r.equals("false")) {
					return "false";
				}
				return "unknown";
			}
		}
		
		Set xxx = new HashSet();
		List lhs = subst_new(where.lhs, k, keys, xxx);
		Set yyy = new HashSet();
		List rhs = subst_new(where.rhs, k, keys, yyy);
		if (!xxx.isEmpty() || !yyy.isEmpty()) {
			return "unknown";
		}
		boolean b = I.getKB().equiv(lhs, rhs);
		return b ? "true" : "false";
	}
	
	public static <C> XCtx<C> flower2(FLOWER2 flower, XCtx<C> I) {
		XCtx c = frozen(flower, I.schema); 
		
		Set<Map<Object, Triple<C, C, List<C>>>> ret = new HashSet<>();
		Map<Object, Triple<C, C, List<C>>> m = new HashMap<>();
		ret.add(m);
		
		//TODO: check from vars do not occur in I
		if (!Collections.disjoint(flower.from.keySet(), I.allTerms())) {
			throw new RuntimeException("FROM variable is also in instance");
		}
		for (Object var : flower.from.keySet()) {
			Object node = flower.from.get(var);
			Set<Map<Object, Triple<C, C, List<C>>>> ret2 = new HashSet<>();
			for (Map<Object, Triple<C, C, List<C>>> tuple : ret) {
				outer: for (Triple<C, C, List<C>> t : I.cat().hom((C)"_1", (C)node)) {
					Map<Object, Triple<C, C, List<C>>> merged = new HashMap<>(tuple);
					merged.put(var, t);
					String result = eval(flower.where, merged, flower.from.keySet(), I);
					if (result.equals("false")) {
						continue outer;
					}
					ret2.add(merged);
				}
			}
			ret = ret2;
		}
		
		//instance
		Set ids = new HashSet<>();
		Map types = new HashMap<>();
		Set eqs = new HashSet<>();
		//schema
		Set ids2 = new HashSet<>();
		Map types2 = new HashMap<>();
		Set eqs2 = new HashSet<>();
		ids2.add("_Q");
		types2.put("_Q", new Pair<>("_Q", "_Q"));
		for (Map<Object, Triple<C, C, List<C>>> k : ret) {
			types.put(k, new Pair("_1", "_Q"));
			for (Object edge : flower.select.keySet()) {
				Object tgt = c.type(flower.select.get(edge)).second;
				if (!I.global.ids.contains(tgt)) {
					throw new RuntimeException("Selection path " + edge + " does not target a type");
				}
				types2.put(edge, new Pair<>("_Q", tgt));

				List lhs = new LinkedList();
				lhs.add(k);
				lhs.add(edge);
				//must normalize in I
				List<C> rhs0 = subst_new(flower.select.get(edge), k, new HashSet(), new HashSet());
				Triple<C,C,List<C>> rhs = I.find_fast(new Triple((C)"_1", (C)tgt, rhs0));
				List rhsX = new LinkedList();
				if (I.schema.cat().hom((C)"_1", (C)tgt).contains(rhs)) {
					if (rhs.third.isEmpty()) {
						rhsX.add(rhs.first);
					} else {
						rhsX.addAll(rhs.third);
					}
				} else {
					rhsX.add(rhs);
				}
			//	System.out.println("adding 1: " + new Pair(lhs, rhsX));
				eqs.add(new Pair(lhs, rhsX));
				
			}
		} 
		
		for (C t : I.global.ids) {
			for (Triple<C, C, List<C>> arr : I.cat().hom((C)"_1", t)) {
				if (I.global.cat().hom((C)"_1", t).contains(arr)) {
					continue;
				}
				types.put(arr, new Pair<>("_1", t));
				for (Entry<C, Pair<C, C>> e : I.global.types.entrySet()) {
					if (!e.getValue().first.equals(t)) {
						continue;
					}
					List lhs = new LinkedList();
					lhs.add(arr);
					lhs.add(e.getKey());
					
					List<C> rhs0 = new LinkedList<>();
					//rhs0.add(arr.second);
					rhs0.addAll(arr.third);
					rhs0.add(e.getKey());
					Triple<C,C,List<C>> rhsX = I.find_fast(new Triple<>((C)"_1", e.getValue().second, rhs0));
					List rhs = new LinkedList();
					if (I.schema.cat().hom((C)"_1", (C)e.getValue().second).contains(rhsX)) {
						if (rhsX.third.isEmpty()) {
							rhs.add(rhsX.first);
						} else {
							rhs.addAll(rhsX.third);
						}
					} else {
						rhs.add(rhsX);
					}
					//System.out.println("adding 2: " + new Pair(lhs, rhs));
					
					eqs.add(new Pair<>(lhs, rhs));
				}
			}
		}
		
		XCtx c2 = new XCtx(ids2, types2, eqs2, I.global, null, "schema");		
		XCtx<C> J = new XCtx<C>(ids, types, eqs, I.global, c2, "instance");
		J.saturated = true; 
		return J;
	}
	
	public static XCtx flower(Flower e, XCtx I) {
		return flower2(convert(e), I);
	}
	
	public static <C> XBool convert2(Set<Pair<List<C>, List<C>>> eqs) {
		XBool where = new XBool(true);
		for (Pair<List<C>, List<C>> eq : eqs) {
			where = new XBool(new XBool((List<Object>)eq.first, (List<Object>)eq.second), where, true);
		}
		return where;
	}
	
	public static FLOWER2 convert(Flower flower) {
		XBool where = new XBool(true);
		for (Pair<List<Object>, List<Object>> eq : flower.where) {
			where = new XBool(new XBool(eq.first, eq.second), where, true);
		}
		return new FLOWER2(flower.select, flower.from, where, flower.src);
	}
	
	/*
	public static XCtx flower(Flower e, XCtx I) {
		Set ids = new HashSet<>(I.schema.ids);
		Map types = new HashMap<>(I.schema.types);
		Set eqs = new HashSet<>(I.schema.eqs);
		
		ids.add("_Q");
		types.put("_Q", new Pair<>("_Q", "_Q"));
//		types.put("!__Q", new Pair<>("_Q", "_1"));  needed?
		
		for (Entry<String, String> k : e.from.entrySet()) {
			types.put(k.getKey(), new Pair<>("_Q", k.getValue()));
		}
		eqs.addAll(e.where);

		XCtx c = new XCtx(ids, types, eqs, I.global, null, "schema");
		Map em = new HashMap<>();
		for (Object o : I.schema.allTerms()) {
			List l = new LinkedList<>();
			l.add(o);
			em.put(o, l);
		}
		XMapping m = new XMapping(I.schema, c, em, "mapping");
		
		
		Set ids2 = new HashSet<>();
		Map types2 = new HashMap<>();
		Set eqs2 = new HashSet<>();
		Map em2 = new HashMap<>();
		ids2.add("_Q");
		types2.put("_Q", new Pair<>("_Q", "_Q"));
		List ll = new LinkedList<>();
		ll.add("_Q");
		em2.put("_Q", ll);
//		System.out.println("C is " + c);
		for (Entry o : e.select.entrySet()) {
			//System.out.println("trying " + o.getValue());
			Object tgt = c.type((List)o.getValue()).second;
			types2.put(o.getKey(), new Pair<>("_Q", tgt));
			if (!I.global.allTerms().contains(tgt)) {
				ids2.add(tgt);
				types2.put(tgt, new Pair<>(tgt, tgt));
			}
			em2.put(o.getKey(), o.getValue());
		}
		XCtx c2 = new XCtx(ids2, types2, eqs2, I.global, null, "schema");
		for (Object o : c2.allTerms()) {
			if (em2.containsKey(o)) {
				continue;
			}
			List l = new LinkedList<>();
			l.add(o);
			em2.put(o, l);
		}

		XMapping m2 = new XMapping(c2, c, em2, "mapping"); 
		
		XCtx J = null;
		if (DEBUG.debug.direct_flower) {
			J = fast_flower(e, I, c, c2);
		} else {
			J = m.pi(I);			
		}
				
		return m2.delta(J);
	}
	*/
	/*
	private static Set<Flower> normalize(FLOWER2 e) {
		Set<Flower> ret = new HashSet<>();
		
		if (e.where == null) {
			Flower f = new Flower(e.select, e.from, new LinkedList<>(), e.src);
			f.ty = e.ty;
			ret.add(f);
			return ret;
		}
		
		if (e.where.lhs != null && e.where.rhs != null) {
			List<Pair<List<String>, List<String>>> l = new LinkedList<>();
			l.add(new Pair<>(e.where.lhs, e.where.rhs));
			Flower f = new Flower(e.select, e.from, l, e.src);
			f.ty = e.ty;
			ret.add(f);
			return ret;
		}
		
		e.where.normalize();
		
		List<List<Pair<List<String>, List<String>>>> ll = e.where.fromOr();
		for (List<Pair<List<String>, List<String>>> l : ll) {
			Flower f = new Flower(e.select, e.from, l, e.src);
			f.ty = e.ty;
			ret.add(f);
		}
		return ret;
	}
	*/
	/*
	public static XCtx FLOWER(FLOWER2 e0, XCtx I) {
		Set ids = new HashSet<>(I.schema.ids);
		Map types = new HashMap<>(I.schema.types);
		Set eqs = new HashSet<>(I.schema.eqs);
		
		Set<Flower> flowers = normalize(e0);
		
		int i = 0;
		for (Flower e : flowers) {
			String qi = "_Q" + i;
			ids.add(qi);
			types.put(qi, new Pair<>(qi, qi));

			for (Entry<String, String> k : e.from.entrySet()) {
				types.put(qi + k.getKey(), new Pair<>(qi, k.getValue()));
			}
			//!__Q -> !__Q1
			for (Pair<List<String>, List<String>> eq : e.where) {
				Function<String, String> f = x -> e.from.keySet().contains(x) ? qi + x : x;
				List<String> lhs = eq.first.stream().map(f).collect(Collectors.toList());
				List<String> rhs = eq.second.stream().map(f).collect(Collectors.toList());
				Function<String, String> g = x -> x.equals("!__Q") ? "!_" + qi : x;
				lhs = lhs.stream().map(g).collect(Collectors.toList());
				rhs = rhs.stream().map(g).collect(Collectors.toList());
				eqs.add(new Pair<>(lhs, rhs));
			}
			i++;
		}
		
		XCtx c = new XCtx(ids, types, eqs, I.global, null, "schema");
		Map em = new HashMap<>();
		for (Object o : I.schema.allTerms()) {
			List l = new LinkedList<>();
			l.add(o);
			em.put(o, l);
		}
		XMapping m = new XMapping(I.schema, c, em, "mapping");
		
		Set ids2 = new HashSet<>();
		Map types2 = new HashMap<>();
		Set eqs2 = new HashSet<>();
		Map em2 = new HashMap<>();
		
		i = 0;
		for (Flower e : flowers) {
			String qi = "_Q" + i;
			ids2.add(qi);
			types2.put(qi, new Pair<>(qi, qi));
			List ll = new LinkedList<>();
			ll.add(qi);
			em2.put(qi, ll);
			for (Entry<String, List<String>> o : e.select.entrySet()) { 
				Function<String, String> f = x -> e.from.keySet().contains(x) ? qi + x : x;
				List<String> lll = o.getValue().stream().map(f).collect(Collectors.toList());

				Object tgt = c.type(lll).second;
				types2.put(qi + o.getKey(), new Pair<>(qi, tgt));
				if (!I.global.allTerms().contains(tgt)) {
					ids2.add(tgt);
					types2.put(tgt, new Pair<>(tgt, tgt));
				}
				em2.put(qi + o.getKey(), lll);
			}
			i++;
		}
		XCtx c2 = new XCtx(ids2, types2, eqs2, I.global, null, "schema");
		for (Object o : c2.allTerms()) {
			if (em2.containsKey(o)) {
				continue;
			}
			List l = new LinkedList<>();
			l.add(o);
			em2.put(o, l);
		}
		XMapping m2 = new XMapping(c2, c, em2, "mapping"); 
		
		Set ids3 = new HashSet<>();
		Map types3 = new HashMap<>();
		Set eqs3 = new HashSet<>();
		Map em3 = new HashMap<>();
		
		ids3.add("_Q");
		types3.put("_Q", new Pair<>("_Q", "_Q"));

		i = 0;
		for (Flower e : flowers) {
			String qi = "_Q" + i;
			List ll = new LinkedList<>();
			ll.add("_Q");
			em3.put(qi, ll);
			List jj = new LinkedList<>();
			jj.add("!__Q");
			em3.put("!_" + qi, jj);

			for (Entry<String, List<String>> o : e.select.entrySet()) { 
				Function<String, String> f = x -> e.from.keySet().contains(x) ? qi + x : x;
				List<String> lll = o.getValue().stream().map(f).collect(Collectors.toList());

				Object tgt = c.type(lll).second;
				types3.put(o.getKey(), new Pair<>("_Q", tgt));
				if (!I.global.allTerms().contains(tgt)) {
					ids3.add(tgt);
					types3.put(tgt, new Pair<>(tgt, tgt));
				}
				List u = new LinkedList();
				u.add(o.getKey());
				em3.put(qi + o.getKey(), u);
			}
			i++;
		}
		XCtx c3 = new XCtx(ids3, types3, eqs3, I.global, null, "schema");
		for (Object o : c2.allTerms()) {
			if (em3.containsKey(o)) {
				continue;
			}
			List l = new LinkedList<>();
			l.add(o);
			em3.put(o, l);
		}

		XMapping m3 = new XMapping(c2, c3, em3, "mapping"); 
		
		XCtx J = null;
		if (DEBUG.debug.direct_flower) {
			J = fast_flower(flowers, I, c, c2);
		} else {
			J = m.pi(I);			
		}
//		System.out.println(J);
		XCtx K = m2.delta(J);
	//	System.out.println(K);
		XCtx L = m3.apply0(K);
	//	System.out.println(L);
		
		return L.rel();
	}
	
	*/
	
	public static <C, D> XMapping<Pair<Object, Map<Object, Triple<C, C, List<C>>>>, Pair<Object, Map<Object, Triple<C, C, List<C>>>>> uberflower(XPoly<C, D> poly, XMapping<C,C> h) {
		//poly.initConjs(); works on full ubers
		XCtx<Pair<Object, Map<Object, Triple<C, C, List<C>>>>> hsrc = uberflower(poly, h.src);
		XCtx<Pair<Object, Map<Object, Triple<C, C, List<C>>>>> hdst = uberflower(poly, h.dst);
		Map em = new HashMap<>();
		
		for (Pair<Object, Map<Object, Triple<C, C, List<C>>>> k : hsrc.ids) {
			Map<Object, Triple<C, C, List<C>>> cand = new HashMap<>();
			for (Object v : k.second.keySet()) {
				Triple<C, C, List<C>> p = k.second.get(v);
				cand.put(v, h.dst.find_fast(new Triple<>(p.first, p.second, h.apply(p.third))));
			}
			em.put(k, new Pair<>(k.first, cand));
		}
		
		for (Object k : hsrc.allTerms()) {
			if (!em.containsKey(k)) {
				em.put(k, Util.singList(k));
			}
		}
		return new XMapping<Pair<Object, Map<Object, Triple<C, C, List<C>>>>, Pair<Object, Map<Object, Triple<C, C, List<C>>>>>(uberflower(poly, h.src), uberflower(poly, h.dst), em, "homomorphism");
	}
	
	//TODO: make sure this is conjunctive otherwise throw an error //duplicate for later
	//TODO: on saturated with discrete op will be saturated
	//TODO: must add (not query label) (TARGET NODE) EVEN FOR THE CONJUNCTIVE CASE //add label here
	//TODO: do pre-filtering based on lhs = const (ground) here //won't help
	public static <C,D> XCtx<Pair<Object, Map<Object, Triple<C, C, List<C>>>>> uberflower(XPoly<C,D> poly, XCtx<C> I) {
		//XCtx c = frozen(flower, I.schema); 
		
		Map<Object, Set<Map<Object, Triple<C, C, List<C>>>>> top = new HashMap<>();
		Map<Object, XCtx> frozens = new HashMap();
		
		for (Object flower_name : poly.blocks.keySet()) {
			Pair<D, Block<C, D>> flowerX = poly.blocks.get(flower_name);
			D flower_dst = flowerX.first;
			Block<C, D> flower = flowerX.second;

			Set<Map<Object, Triple<C, C, List<C>>>> ret = new HashSet<>();
			Map<Object, Triple<C, C, List<C>>> m = new HashMap<>();
			ret.add(m);
			for (Object var : flower.from.keySet()) {
				C node = flower.from.get(var);
				Set<Map<Object, Triple<C, C, List<C>>>> ret2 = new HashSet<>();
				for (Map<Object, Triple<C, C, List<C>>> tuple : ret) {
					outer: for (Triple<C, C, List<C>> t : I.cat().hom((C)"_1", (C)node)) {
						Map<Object, Triple<C, C, List<C>>> merged = new HashMap<>(tuple);
						merged.put(var, t);
						String result = eval(convert2(flower.where), merged, flower.from.keySet(), I);
						if (result.equals("false")) {
							continue outer;
						}
						ret2.add(merged);
					}
				}
				ret = ret2;
			}
		
			top.put(flower_name, ret);
			frozens.put(flower_name, frozen(flower, I.schema));
		}
		
		checkEdges(poly, frozens);
				
		//instance
		Set<Pair<Object, Map<Object, Triple<C, C, List<C>>>>> ids = new HashSet<>();
		Map<Pair<Object, Map<Object, Triple<C, C, List<C>>>>, Pair<Pair<Object, Map<Object, Triple<C, C, List<C>>>>,Pair<Object, Map<Object, Triple<C, C, List<C>>>>>> types = new HashMap<>();
		Set<Pair<List<Pair<Object, Map<Object, Triple<C, C, List<C>>>>>, List<Pair<Object, Map<Object, Triple<C, C, List<C>>>>>>> eqs = new HashSet<>();
		
		for (Object flower_name : poly.blocks.keySet()) {
			Set<Map<Object, Triple<C, C, List<C>>>> ret = top.get(flower_name);
			Pair<D, Block<C, D>> flowerX = poly.blocks.get(flower_name);
			D flower_dst = flowerX.first;
			Block<C, D> flower = flowerX.second;
			XCtx c = frozens.get(flower_name);
			
			for (Map<Object, Triple<C, C, List<C>>> k : ret) {
				types.put(new Pair<>(flower_name, k), new Pair("_1", flower_dst));
				for (D edge : flower.attrs.keySet()) {
					Object tgt = c.type(flower.attrs.get(edge)).second;
					if (!I.global.ids.contains(tgt)) {
						throw new RuntimeException("Selection path " + edge + " does not target a type");
					}
	
					List lhs = new LinkedList();
					lhs.add(new Pair<>(flower_name, k));
					lhs.add(edge);
					//must normalize in I
					List<C> rhs0 = subst_new(flower.attrs.get(edge), k, new HashSet(), new HashSet());
					Triple<C,C,List<C>> rhs = I.find_fast(new Triple((C)"_1", (C)tgt, rhs0));
					List rhsX = new LinkedList();
					if (I.schema.cat().hom((C)"_1", (C)tgt).contains(rhs)) {
						if (rhs.third.isEmpty()) {
							rhsX.add(rhs.first);
						} else {
							rhsX.addAll(rhs.third);
						}
					} else {
						rhsX.add(rhs);
					}
					eqs.add(new Pair(lhs, rhsX));
				}
				
				for (D edge : flower.edges.keySet()) {
					D tgt = poly.dst.type(edge).second;
	
					List lhs = new LinkedList();
					lhs.add(new Pair<>(flower_name, k));
					lhs.add(edge);
					//must normalize in I
					
					Map rhs0Q = new HashMap();
					for (Object str : flower.edges.get(edge).second.keySet()) {
						List<C> rhs0Z = subst_new(flower.edges.get(edge).second.get(str), k, new HashSet(), new HashSet());
						rhs0Q.put(str,  rhs0Z);
					}
					
					Map found = null;
					outer: for (Map<Object, Triple<C, C, List<C>>> map : top.get(flower.edges.get(edge).first)) {
						for (Object str : map.keySet()) {
							if (!I.getKB().equiv(map.get(str).third, (List<C>)rhs0Q.get(str))) {
								continue outer;
							}
						}
						if (found != null) {
							throw new RuntimeException();
						}
						found = map;
					}
					if (found == null) {
						throw new RuntimeException("Cannot find ID " + rhs0Q + " in " + top.get(flower.edges.get(edge)));
					}
					List rhsX = new LinkedList();
					rhsX.add(new Pair<>(flower.edges.get(edge).first, found));
					eqs.add(new Pair(lhs, rhsX));
				}
			} 
		}
		
		Map types0 = types;
		for (C t : I.global.ids) {
			for (Triple<C, C, List<C>> arr : I.cat().hom((C)"_1", t)) {
				if (I.global.cat().hom((C)"_1", t).contains(arr)) {
					continue;
				}
				types0.put(arr, new Pair<>("_1", t));
				for (Entry<C, Pair<C, C>> e : I.global.types.entrySet()) {
					if (!e.getValue().first.equals(t)) {
						continue;
					}
					List lhs = new LinkedList();
					lhs.add(arr);
					lhs.add(e.getKey());
					
					List<C> rhs0 = new LinkedList<>();
					//rhs0.add(arr.second);
					rhs0.addAll(arr.third);
					rhs0.add(e.getKey());
					Triple<C,C,List<C>> rhsX = I.find_fast(new Triple<>((C)"_1", e.getValue().second, rhs0));
					List rhs = new LinkedList();
					if (I.schema.cat().hom((C)"_1", (C)e.getValue().second).contains(rhsX)) {
						if (rhsX.third.isEmpty()) {
							rhs.add(rhsX.first);
						} else {
							rhs.addAll(rhsX.third);
						}
					} else {
						rhs.add(rhsX);
					}
					//System.out.println("adding 2: " + new Pair(lhs, rhs));
					
					eqs.add(new Pair<>(lhs, rhs));
				}
			}
		}
		
		XCtx J = new XCtx(ids, types, eqs, I.global, poly.dst, "instance");
		J.saturated = true; 
		return J;
	}
	
	//TODO: check that INSTANCEs are saturated?
	
	static <C,D> void checkEdges(XPoly<C,D> poly, Map<Object, XCtx> frozens) {
		for (Object k : poly.blocks.keySet()) {
			Pair<D, Block<C, D>> b = poly.blocks.get(k);
			XCtx src = frozens.get(k);
			
			for (D term : poly.dst.terms()) {
				Pair<D, D> t = poly.dst.type(term);
				if (!t.first.equals(b.first)) {
					continue;
				}
				if (t.second.equals("_1")) {
					continue;
				}
				if (poly.dst.allIds().contains(term)) {
					continue;
				}
				if (poly.dst.global.allTerms().contains(t.second)) {
					continue;
				}
				if (!b.second.edges.containsKey(term)) {
					throw new RuntimeException("Missing mapping for edge " + term + " in " + k);
				}
			}
			
			Set atts = new HashSet();
			for (D arr : poly.dst.allTerms()) {
				if (poly.dst.allIds().contains(arr)) {
					continue;
				}
				Pair<D, D> ty = poly.dst.type(arr);
				if (ty.second.equals("_1")) {
					continue;
				}
				if (!ty.first.equals(b.first)) {
					continue;
				}
				if (!poly.dst.ids.contains(ty.second)) {
					atts.add(arr);
				}
			}
			if (!atts.equals(b.second.attrs.keySet())) {
				throw new RuntimeException("Bad attributes in " + k + ": " + atts + " vs " + b.second.attrs.keySet());
			}
			
			for (D k2 : b.second.edges.keySet()) {
				Pair<Object, Map<Object, List<Object>>> v2 = b.second.edges.get(k2);
				XCtx dst = frozens.get(v2.first);
				if (dst == null) {
					throw new RuntimeException("Edge " + k2 + " goes to non-existent node ");
				}
				Map em = new HashMap<>(v2.second);
				for (Object o : dst.schema.allTerms()) {
					if (em.containsKey(o)) {
						continue;
					}
					List l = new LinkedList();
					l.add(o);
					em.put(o, l);
				}
				new XMapping(dst, src, em, "transform");
			}			
		}
	}

	public static <X,A,B> XCtx<Chc<A,B>> pushout(XMapping<X,A> f, XMapping<X,B> g) {
		if (!f.src.equals(g.src)) {
			throw new RuntimeException("not common source");
		}
		XCtx<Chc<A,B>> I = coprod(f.dst, g.dst);
		XMapping<A,Chc<A,B>> inj1 = inl(f.dst, g.dst);
		XMapping<B,Chc<A,B>> inj2 = inr(f.dst, g.dst);
		
		Set<Pair<List<Chc<A, B>>, List<Chc<A, B>>>> eqs = new HashSet<>(I.eqs);
		for (X gen : f.src.terms()) {
			List<A> fgen = f.em.get(gen);
			List<B> ggen = g.em.get(gen);
			List<Chc<A,B>> inj1fgen = inj1.apply(fgen);
			List<Chc<A,B>> inj2ggen = inj2.apply(ggen);
			eqs.add(new Pair<>(inj1fgen, inj2ggen));
		}
		
		XCtx<Chc<A,B>> ret = new XCtx<Chc<A,B>>(I.ids, I.types, eqs , (XCtx)I.global, (XCtx)I.schema, "instance");
		ret.saturated = false;
		//	ret.saturated = f.dst.saturated && g..saturated;
		return ret;
	}
	
}
