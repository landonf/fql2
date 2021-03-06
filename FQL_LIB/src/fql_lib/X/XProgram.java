package fql_lib.X;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import catdata.algs.Pair;
import catdata.algs.Triple;
import fql_lib.decl.FQLProg;
import fql_lib.decl.LineException;

public class XProgram implements FQLProg {
	
	public Map<String, String> kinds() {
		Map<String, String> ret = new HashMap<>();
		for (String k : order) {
			String x = exps.get(k).kind(exps);
			ret.put(k, x);
		}
		return ret;
	}
	
	public Map<String, Pair<String, String>> types() {
		Map<String, Pair<String, String>> ret = new HashMap<>();
		for (String k : order) {
			try {
				String s1 = "?";
				String s2 = "?";
				Pair<XExp, XExp> x = exps.get(k).type(exps);
				if (x == null) { 
				} else {
					String s1x = x.first.toString();
					String s2x = x.second.toString();
					if (s1x.length() > 32) {
						s1x = "...";
					}
					if (s2x.length() > 32) {
						s2x = "...";
					}
					s1 = s1x;
					s2 = s2x;
				}
				ret.put(k, new Pair<>(s1, s2));
		
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException("Type Error in " + k + ":" + ex.getMessage());
			}
		}
		return ret;
	}
	
	public String typeReport() {
		String ret = "";
		Map<String, Pair<String, String>> types = types();
		for (String k : order) {
			Pair<String, String> v = types.get(k);
			ret += k + " : " + v.first + " -> " + v.second + "\n";
		}
		return ret;
	} 

	
	public List<String> order = new LinkedList<>();
	public LinkedHashMap<String, Integer> lines = new LinkedHashMap<>();	
	public LinkedHashMap<String, XExp> exps = new LinkedHashMap<>();
	
	public XProgram(List<Triple<String, Integer, XExp>> decls) {
			Set<String> seen = new HashSet<>();
			for (Triple<String, Integer, XExp> decl : decls) { 
				checkDup(seen, decl.first);
				exps.put(decl.first, decl.third);
				lines.put(decl.first, decl.second);
				order.add(decl.first);				
			}
	}

	private void checkDup(Set<String> seen, String name)
			throws LineException {
		if (seen.contains(name)) {
			throw new RuntimeException("Duplicate name: " + name);
		}
		seen.add(name);
	}

	@Override
	public Integer getLine(String s) {
		return lines.get(s);
	}
	
	private Map<String, Pair<String, String>> tys = null;
	public Pair getType(String s) {
		if (tys == null) {
			return new Pair("?", "?");
			
		}
		Pair p = tys.get(s);
		if (p == null) {
			return new Pair("?", "?");
		}
		return tys.get(s);
	}

	public void doTypes() {
		tys = types();
	}

}
