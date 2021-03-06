package fql_lib.opl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.script.Invocable;
import javax.script.ScriptException;

import catdata.algs.Chc;
import catdata.algs.Pair;
import catdata.algs.Triple;
import catdata.algs.kb.KB;
import catdata.algs.kb.KBExp;
import catdata.algs.kb.KBOrders;
import catdata.algs.kb.KBExp.KBApp;
import catdata.algs.kb.KBExp.KBVar;
import fql_lib.DEBUG;
import fql_lib.Util;
import fql_lib.cat.categories.FinSet;
import fql_lib.cat.categories.Operad;
import fql_lib.opl.OplExp.JSWrapper;
import fql_lib.opl.OplExp.OplCtx;
import fql_lib.opl.OplExp.OplJavaInst;
import fql_lib.opl.OplExp.OplSig;
import fql_lib.opl.OplExp.OplTerm;
import fql_lib.opl.OplParser.DoNotIgnore;

public class OplToKB<S,C,V> implements Operad<S, Pair<OplCtx<S,V>, OplTerm<C,V>>> {
	
	@Override
	public Set<S> objects() {
		return sig.sorts;
	}

	@Override
	public Set<Arrow<S, Pair<OplCtx<S, V>, OplTerm<C, V>>>> hom(List<S> src, S dst) {
		Set<Arrow<S, Pair<OplCtx<S, V>, OplTerm<C, V>>>> ret = new HashSet<>();
		for (Pair<OplCtx<S, V>, OplTerm<C, V>> x : hom0(src, dst)) {
			ret.add(new Arrow<>(src, dst, x));
		}
		return ret;
	}
	
	@Override
	public Arrow<S, Pair<OplCtx<S, V>, OplTerm<C, V>>> id(S o) {
		V v = fr.next();
		OplTerm<C,V> t = new OplTerm<>(v);
		OplCtx<S,V> g = new OplCtx<>(Collections.singletonList(new Pair<>(v, o)));
		return new Arrow<>(Collections.singletonList(o), o, new Pair<>(g, t));
	}

	@Override
	public Arrow<S, Pair<OplCtx<S, V>, OplTerm<C, V>>> comp(
			Arrow<S, Pair<OplCtx<S, V>, OplTerm<C, V>>> F,
			List<Arrow<S, Pair<OplCtx<S, V>, OplTerm<C, V>>>> A) {
		if (F.src.size() != A.size()) {
			throw new RuntimeException("Arity mismatch: " + F + " and " + A);
		}
		List<Pair<V,S>> new_vs = new LinkedList<>();
		List<OplTerm<C, V>> new_args = new LinkedList<>();
		List<S> new_sorts = new LinkedList<>();
		for (Arrow<S, Pair<OplCtx<S, V>, OplTerm<C, V>>> a : A) {
			Map<V, OplTerm<C,V>> mm = new HashMap<>();
			for (V v : a.a.first.names()) {
				V u = fr.next();
				mm.put(v, new OplTerm<>(u));
				S s = a.a.first.get(v);
				new_vs.add(new Pair<>(u, s));
				new_sorts.add(s);
			}
			new_args.add(a.a.second.subst(mm));
		}
		
		return new Arrow<>(new_sorts, F.dst, new Pair<>(new OplCtx<>(new_vs), F.a.second.subst(F.a.first, new_args)));
		
	}
	
	private OplSig<S, C, V> sig;
	private KB<C, V> KB;
	private Iterator<V> fr;
//	private OplJavaInst I;
	
	public OplToKB(Iterator<V> fr, OplSig<S, C, V> sig) {
		this.sig = sig;
		if (DEBUG.debug.opl_require_const) {
			checkEmpty();
		}
		this.fr = fr;
		KB = convert(this.sig);
		KB.complete();
	}
	
	private void checkEmpty() {
		Set<S> m = new HashSet<>(sig.sorts);
		for (C f : sig.symbols.keySet()) {
			Pair<List<S>, S> a = sig.symbols.get(f);
			if (a.first.isEmpty()) {
				m.remove(a.second);
			}
		}
		
		if (!m.isEmpty()) {
			throw new RuntimeException("Sort " + Util.sep(m, ",") + " has no 0-ary constants");
		}
	}
	
	private Map<Pair<List<S>, S>, Collection<Pair<OplCtx<S, V>, OplTerm<C, V>>>> hom = new HashMap<>();
	public Collection<Pair<OplCtx<S,V>, OplTerm<C,V>>> hom0(List<S> s, S t) {
		Collection<Pair<OplCtx<S,V>, OplTerm<C,V>>> ret = hom.get(new Pair<>(s, t));
		if (!sig.sorts.contains(t)) {
			throw new DoNotIgnore("Bad target sort " + t);
		}
		if (!sig.sorts.containsAll(s)) {
			throw new DoNotIgnore("Bad source sort " + s);		
		}
		
		if (ret == null) {
//			ret = new LinkedList<>(ret)
			List<Pair<V, S>> vars = new LinkedList<>();
			Map<S, Set<V>> vars2 = new HashMap<>();
			//int i = 0;
			for (S z : s) {
				vars.add(new Pair<>(fr.next(), z));
				//i++;
			}
			OplCtx<S,V> ctx = new OplCtx<>(vars);
			for (S sort : sig.sorts) {
				vars2.put(sort, new HashSet<>());
			}
			for (Pair<V, S> k : vars) {
				vars2.get(k.second).add(k.first);
			}
			
		//	int count = 0;
			Map<S, Set<OplTerm<C, V>>> j = arity0(vars2);
			for (;;) {
				Map<S, Set<OplTerm<C, V>>> k = inc(j);
				Map<S, Set<OplTerm<C, V>>> k2= red(k);
				if (j.equals(k2)) {
					break;
				} 
				j = k2;
			}	
			ret = j.get(t).stream().map(g -> { return nice(ctx, g); }).collect(Collectors.toList());
			//ret = new LinkedList<>(ret);
			((List)ret).sort(Util.ToStringComparator);
			hom.put(new Pair<>(s,t), ret);
		}
		
		return ret; 
	}
	
	Pair<OplCtx<S,V>, OplTerm<C,V>> nice(OplCtx<S,V> G, OplTerm<C,V> e) {
		int i = 0;
		Map m = new HashMap();
		List<Pair> l = new LinkedList<>();
		for (V v : G.names()) {
			l.add(new Pair("v" + i, G.get(v)));
			m.put(v, new OplTerm("v" + i++));
		}
		OplCtx ret = new OplCtx(l);
		return new Pair(ret, e.subst(m));
	}
	
	
	private Map<S, Set<OplTerm<C,V>>> red(Map<S, Set<OplTerm<C,V>>> in) {
		Map<S, Set<OplTerm<C,V>>> ret = new HashMap<>();
		
		for (S k : in.keySet()) {
			Set<OplTerm<C,V>> v = in.get(k);
			Set<OplTerm<C,V>> v2 = new HashSet<>();
			for (OplTerm<C,V> a : v) {
				KBExp<C, V> b = convert(a);
				KBExp<C, V> c = KB.nf(b);
				OplTerm<C, V> d = convert(c);
				v2.add(d);
			}
			ret.put(k, v2);
		}
		
		return ret;
	}

	public KBExp<C,V> nf(KBExp<C,V> r) {
		return KB.nf(r);
	}
	public OplTerm<C,V> nf(OplTerm<C,V> r) {
		return convert(KB.nf(convert(r)));
	} 
	
	
	/*
	Map<String, Set<OplTerm>> upto(Map<String, Set<String>> vars, int n) {
		Map<String, Set<OplTerm>> m = arity0(vars);
		for (int i = 0; i < n; i++) {
			m = inc(m);
		}
		return m;
	}
	*/
	private Map<S, Set<OplTerm<C,V>>> arity0(Map<S, Set<V>> vars) {
		Map<S, Set<OplTerm<C,V>>> m = new HashMap<>();
		for (S s : sig.sorts) {
			Set<OplTerm<C,V>> set = new HashSet<>();
			for (V v : vars.get(s)) {
				set.add(new OplTerm<>(v));
			}
			m.put(s, set);
		}
		for (C f : sig.symbols.keySet()) {
			Pair<List<S>, S> a = sig.symbols.get(f);
			if (a.first.isEmpty()) {
				m.get(a.second).add(new OplTerm<>(f, new LinkedList<>()));
			}
		}
		
		return m;
	}
	
	private Map<S, Set<OplTerm<C,V>>> inc(Map<S, Set<OplTerm<C,V>>> in) {
		Map<S, Set<OplTerm<C,V>>> out = new HashMap<>();
		for (S k : in.keySet()) {
			out.put(k, new HashSet<>(in.get(k)));
		}
		
		for (C f : sig.symbols.keySet()) {
			Pair<List<S>, S> a = sig.symbols.get(f);
			if (a.first.isEmpty()) {
				continue;
			}
			Map<Integer, List<OplTerm<C,V>>> arg_ts = new HashMap<>();
			int i = 0;
			for (S t : a.first) {
				arg_ts.put(i++, new LinkedList<>(in.get(t)));
			}
			List<LinkedHashMap<Integer, OplTerm<C,V>>> cands = FinSet.homomorphs(arg_ts);
			for (LinkedHashMap<Integer, OplTerm<C,V>> cand : cands) {
				List<OplTerm<C,V>> actual = new LinkedList<>();
				for (int j = 0; j < i; j++) {
					actual.add(cand.get(j));
				}
				out.get(a.second).add(new OplTerm<>(f, actual));
			}
		}
		
		return out;
	}
	
	
	public String printKB() {
		return this.KB.printKB();
	}
	
	public static <C,V> KBExp<C, V> convert(OplTerm<C,V> t) {
		if (t.var != null) {
			return new KBVar<>(t.var);
		}
		List<KBExp<C, V>> l = t.args.stream().map(x -> { return convert(x); }).collect(Collectors.toList());
		return new KBApp<>(t.head, l);
	}
	
	public static <C,V> OplTerm<C,V> convert(KBExp<C, V> t) {
		if (t.isVar) {
			return new OplTerm<>(t.getVar().var);
		}
		KBApp<C, V> t2 = t.getApp();
		List<OplTerm<C,V>> l = new LinkedList<>();
		for (KBExp<C, V> p : t2.args) {
			l.add(convert(p));
		}
		return new OplTerm<>(t2.f, l);
	}
	
	private KB<C, V> convert(OplSig<?,C,V> s) {
		if (s.prec.keySet().size() != new HashSet<>(s.prec.values()).size()) {
			throw new RuntimeException("Cannot duplicate precedence");
		}
		Function<Pair<C, C>, Boolean> gt = x -> {
			Integer l = s.prec.get(x.first);
			Integer r = s.prec.get(x.second);
			if (l != null && r != null) {
				return l > r;				
			}
			if (l == null && r != null) {
				return false;
			}
			if (r == null && l != null) {
				return true;
			}
			String lx = x.first.toString();
			String rx = x.second.toString();
			//System.out.println("1" + x.first + " and " + x.second);
			int la = s.symbols.get(x.first).first.size();
			int ra = s.symbols.get(x.second).first.size();
			if (la == ra) {
				if (lx.length() == rx.length()) {
					return lx.compareTo(rx) < 0;
				}
				return lx.length() < rx.length();
			}
		//	System.out.println("2" + x.first + " > " + x.second);
			if (la >= 3 && ra >= 3) {
				return la > ra;
			}
			if (la == 0 && ra > 0) {
				return false;
			}
			if (la == 1 && (ra == 0 || ra == 2)) {
		//		System.out.println("true");
				return true;
			}
			if (la == 1 && ra > 2) {
		//		System.out.println("false");
				return false;
			}
			if (la == 2 && ra == 0) {
		//		System.out.println("true");
				return true;
			}
			if (la == 2 && (ra == 1 || ra > 2)) {
		//		System.out.println("false");
				return false;
			}
			throw new RuntimeException("Bug in precedence, report to Ryan");
			//function symbols: arity-0 < arity-2 < arity-1 < arity-3 < arity-4
			
			
		};

		Set<Pair<KBExp<C, V>, KBExp<C, V>>> eqs = new HashSet<>();
		for (Triple<?, OplTerm<C, V>, OplTerm<C, V>> eq : s.equations) {
			eqs.add(new Pair<>(convert(eq.second), convert(eq.third)));
		}
		return new KB(eqs, KBOrders.lpogt(gt), fr, DEBUG.debug.opl_unfailing, DEBUG.debug.opl_sort_cps, DEBUG.debug.opl_iterations, DEBUG.debug.opl_red_its);			
	}

	//ok to use null here
	/*public static <C,X,V> KBExp<Chc<Chc<C,X>,JSWrapper>,V> redBy(OplJavaInst I, KBExp<Chc<Chc<C,X>,JSWrapper>,V> e) {				
		if (I == null) {
			return e;
		}
		try {
			if (e.isVar) {
				return e;
			}
				
			KBApp<Chc<Chc<C,X>,JSWrapper>,V> e0 = e.getApp();
				
			List<KBExp<Chc<Chc<C,X>,JSWrapper>,V>> l = new LinkedList<>();
			List<Object> r = new LinkedList<>();
			for (KBExp<Chc<Chc<C, X>, JSWrapper>, V> a : e0.args) {
				KBExp<Chc<Chc<C,X>,JSWrapper>,V> b = redBy(I, a);
				l.add(b);
				if (!b.isVar && b.getApp().args.isEmpty() && !b.getApp().f.left) {
					JSWrapper js = b.getApp().f.r;
					r.add(js.o);
				}	
			}
			if (l.size() == r.size() && e0.f.left && e0.f.l.left) { 
				Object o = ((Invocable)I.engine).invokeFunction((String)e0.f.l.l, r);
				return new KBApp<>(Chc.inRight(new JSWrapper(o)), new LinkedList<>());
			} 
			return new KBApp<>(e0.f, l);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		} 
	} */

	public static <Z,V> KBExp<Chc<Z,JSWrapper>,V> redBy(OplJavaInst I, KBExp<Chc<Z,JSWrapper>,V> e) {				
		if (I == null) {
			return e;
		}
		try {
			if (e.isVar) {
				return e;
			}
				
			KBApp<Chc<Z,JSWrapper>,V> e0 = e.getApp();
				
			List<KBExp<Chc<Z,JSWrapper>,V>> l = new LinkedList<>();
			List<Object> r = new LinkedList<>();
			for (KBExp<Chc<Z, JSWrapper>, V> a : e0.args) {
				KBExp<Chc<Z,JSWrapper>,V> b = redBy(I, a);
				l.add(b);
				if (!b.isVar && b.getApp().args.isEmpty() && !b.getApp().f.left) {
					JSWrapper js = b.getApp().f.r;
					r.add(js.o);
				}	
			}
			if (l.size() == r.size() && e0.f.left) {
				Pair<Function, Object> xxx = Util.stripChcs(e0.f.l);
				if (I.defs.containsKey(xxx.second)) {
					Object o = ((Invocable)I.engine).invokeFunction((String)xxx.second, r);
					return new KBApp<>(Chc.inRight(new JSWrapper(o)), new LinkedList<>());
				}
			} 
			return new KBApp<>(e0.f, l);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		} 
	} 
	
	public Map<S, Set<OplTerm<C, V>>> doHoms() {
		HashMap<S, Set<OplTerm<C, V>>> sorts = new HashMap<>();
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
				for (S s : sig.sorts) {
					sorts.put(s, hom0(new LinkedList<>(), s).stream().map(x -> x.second).collect(Collectors.toSet()));
				}
				} catch (Exception ex) {
					ex.printStackTrace();
			//		throw new RuntimeException(ex.getMessage());
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
		try {
			t.join(DEBUG.debug.opl_hom_its);
			t.stop();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
		if (sorts.keySet().size() == sig.sorts.size()) {
			return sorts;
		}
		throw new RuntimeException("Timeout exceeded");
	}


	
	
}
