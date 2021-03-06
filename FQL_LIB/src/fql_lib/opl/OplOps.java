package fql_lib.opl;

import javax.script.Invocable;

import fql_lib.opl.OplExp.OplApply;
import fql_lib.opl.OplExp.OplCtx;
import fql_lib.opl.OplExp.OplDelta;
import fql_lib.opl.OplExp.OplEval;
import fql_lib.opl.OplExp.OplExpVisitor;
import fql_lib.opl.OplExp.OplFlower;
import fql_lib.opl.OplExp.OplId;
import fql_lib.opl.OplExp.OplInst;
import fql_lib.opl.OplExp.OplJavaInst;
import fql_lib.opl.OplExp.OplMapping;
import fql_lib.opl.OplExp.OplPres;
import fql_lib.opl.OplExp.OplSat;
import fql_lib.opl.OplExp.OplSchema;
import fql_lib.opl.OplExp.OplSchemaProj;
import fql_lib.opl.OplExp.OplSetInst;
import fql_lib.opl.OplExp.OplPresTrans;
import fql_lib.opl.OplExp.OplSetTrans;
import fql_lib.opl.OplExp.OplSig;
import fql_lib.opl.OplExp.OplSigma;
import fql_lib.opl.OplExp.OplString;
import fql_lib.opl.OplExp.OplTransEval;
import fql_lib.opl.OplExp.OplTyMapping;
import fql_lib.opl.OplExp.OplUberSat;
import fql_lib.opl.OplExp.OplUnSat;
import fql_lib.opl.OplExp.OplVar;

public class OplOps implements OplExpVisitor<OplObject, OplProgram> {
	
	OplEnvironment ENV;

	public OplOps(OplEnvironment env) {
		this.ENV = env;
	}

	@Override
	public OplObject visit(OplProgram env, OplSig e) {
		e.validate(); 
		return e;
	}

	@Override
	public OplObject visit(OplProgram env, OplSetInst e) {
		OplObject o = ENV.get(e.sig);
		if (!(o instanceof OplSig)) {
			throw new RuntimeException("Not a theory: " + e.sig);
		}
		OplSig s = (OplSig) o;
		e.validate(s);
		return e;
	}

	@Override
	public OplObject visit(OplProgram env, OplEval e) {
		OplObject i = ENV.get(e.I);
		if (i instanceof OplSetInst) {
			OplSetInst i0 = (OplSetInst) i;
			OplObject s = ENV.get(i0.sig);
			OplSig s0 = (OplSig) s;		
			e.e.type(s0, new OplCtx<String, String>());
			return new OplString(e.e.eval(s0, i0, new OplCtx<String, String>()).toString());
		}
		if (i instanceof OplJavaInst) {
			OplJavaInst i0 = (OplJavaInst) i;
			OplObject s = ENV.get(i0.sig);
			OplSig s0 = (OplSig) s;		
			e.e.type(s0, new OplCtx<String, String>());
			try {
				return new OplString(e.e.eval((Invocable)i0.engine).toString());
			} catch (Exception ee) {
				ee.printStackTrace();
				throw new RuntimeException(ee.getMessage());
			}
		}
		throw new RuntimeException("Not a set/js model: " + e.I);
	}

	@Override
	public OplObject visit(OplProgram env, OplVar e) {
		return ENV.get(e.name);
	}

	@Override
	public OplObject visit(OplProgram env, OplSetTrans e) {
		OplObject src = ENV.get(e.src);
		OplObject dst = ENV.get(e.dst);
		if (!(src instanceof OplSetInst)) {
			throw new RuntimeException("Source is not a model in " + e);
		}
		if (!(dst instanceof OplSetInst)) {
			throw new RuntimeException("Target is not a model in " + e);
		}
		OplSetInst src0 = (OplSetInst) src;
		OplSetInst dst0 = (OplSetInst) dst;
		if (!src0.sig.equals(dst0.sig)) {
			throw new RuntimeException("Theories of source and target do not match in " + e);
		}
		OplSig sig = (OplSig) ENV.get(src0.sig);
		e.validate(sig, src0, dst0);
		return e;
	}

	@Override
	public OplObject visit(OplProgram env, OplJavaInst e) {
		OplObject sig = ENV.get(e.sig);
		if (!(sig instanceof OplSig)) {
			throw new RuntimeException("Not a signature: " + e.sig);
		}
		OplSig sig0 = (OplSig) sig;
		e.validate(sig0, ENV);
		return e;
	}

	@Override
	public OplObject visit(OplProgram env, OplMapping e) {
		OplObject src = ENV.get(e.src0);
		OplObject dst = ENV.get(e.dst0);
		if (src instanceof OplSig && dst instanceof OplSig) {
			OplSig src0 = (OplSig) src;
			OplSig dst0 = (OplSig) dst;
			e.validate(src0, dst0);
			return e;
		} else if (src instanceof OplSchema && dst instanceof OplSchema) {
			OplSchema src0 = (OplSchema) src;
			OplSchema dst0 = (OplSchema) dst;
			//e.validate(src0.projEA(), dst0.projEA());
			OplTyMapping ret = new OplTyMapping<>(e.src0, e.dst0, src0, dst0, e);
			return ret;
		}
		throw new RuntimeException("Source or Target is not a theory/schema in " + e);
	}

	@Override
	public OplObject visit(OplProgram env, OplDelta e) {
		OplObject F = ENV.get(e.F);
		if (!(F instanceof OplMapping)) {
			throw new RuntimeException("Not a mapping: " + e.F);
		}
		OplMapping F0 = (OplMapping) F;
		
		OplObject I = ENV.get(e.I);
		if (I instanceof OplSetInst) {
			OplSetInst I0 = (OplSetInst) I;
			return F0.delta(I0);			
		}
		if (I instanceof OplSetTrans) {
			OplSetTrans h = (OplSetTrans) I;
			return F0.delta(h);
		}
		
		throw new RuntimeException("Not a model or transform: " + e.I);
	}

	@Override
	public OplObject visit(OplProgram env, OplTransEval e) {
	/*	OplObject i = ENV.get(e.I);
		OplObject F = ENV.get(e.F);
		if (!(F instanceof OplMapping)) {
			throw new RuntimeException("Not a mapping: " + e.F);
		}
		OplMapping F0 = (OplMapping) F;
		e.e.type(F0.src, new OplCtx<>());
		OplTerm t = F0.trans(new OplCtx<>(), e.e).second;
				
		if (i instanceof OplSetInst) {
			return new OplString(t.eval(F0.dst, (OplSetInst)i, new OplCtx<String, String>()).toString());
		}
		if (i instanceof OplJavaInst) {
			try {
				return new OplString(t.eval0((Invocable)((OplJavaInst)i).engine));
			} catch (Exception ee) {
				ee.printStackTrace();
				throw new RuntimeException(ee.getMessage());
			}
		}
		throw new RuntimeException("Not a set/js model: " + e.I); */
		throw new RuntimeException();
	}

	@Override
	public OplObject visit(OplProgram env, OplPres e) {
		OplObject i = ENV.get(e.S);
		if (!(i instanceof OplSig)) {
			throw new RuntimeException("Not a theory: " + e.S);
		}
		OplSig S = (OplSig) i;
		
		OplPres ret = OplPres.OplPres(e.prec, e.S, S, e.gens, e.equations);
		ret.toSig();
		return ret;
	}

	@Override
	public OplObject visit(OplProgram env, OplSat e) {
		OplObject i = ENV.get(e.I);
		if (i instanceof OplPres) {
			OplPres S = (OplPres) i;
			return e.saturate(S);
		}
		if (i instanceof OplSig) {
			OplSig S = (OplSig) i;
			return S.saturate(e.I);
		}
		throw new RuntimeException("Not a presentation or theory");
	}
	
	@Override
	public OplObject visit(OplProgram env, OplUberSat e) {
		OplObject p = ENV.get(e.P);
		if (!(p instanceof OplPres)) {
			throw new RuntimeException("Not a presentation: " + e.P);
		}
		OplPres S = (OplPres) p;
		
		OplObject i = ENV.get(e.I);
		if (!(i instanceof OplJavaInst)) {
			throw new RuntimeException("Not a javascript model: " + e.I);
		}
		OplJavaInst I = (OplJavaInst) i;
		
		return e.saturate(I, S);
	}
	
	@Override
	public OplObject visit(OplProgram env, OplUnSat e) {
		OplObject i = ENV.get(e.I);
		if (!(i instanceof OplSetInst)) {
			throw new RuntimeException("Not a model: " + e.I);
		}
		OplSetInst S = (OplSetInst) i;
		return e.desaturate(S.sig, S);
	}
	
	@Override
	public OplObject visit(OplProgram env, OplSigma e) {
		
		OplObject F = ENV.get(e.F);
		OplObject I = ENV.get(e.I);

		if (F instanceof OplMapping) {
			OplMapping F0 = (OplMapping) F;
			if (I instanceof OplPres) {
				OplPres I0 = (OplPres) I;
				return F0.sigma(I0);			
			} else if (I instanceof OplPresTrans) {
				OplPresTrans h = (OplPresTrans) I;
				return F0.sigma(h);
			}
			throw new RuntimeException("Not a presentation of an instance or transform: " + e.I);
		}
		if (F instanceof OplTyMapping) {
			OplTyMapping F0 = (OplTyMapping) F;
			if (I instanceof OplInst) {
				OplInst I0 = (OplInst) I;
				OplInst ret = new OplInst<>(F0.dst.sig0, "?", I0.J0);
				ret.validate(F0.dst, F0.extend().sigma(I0.P), I0.J);
				return ret;
			} 
			throw new RuntimeException("Not an instance: " + e.I);

		}
		throw new RuntimeException("Not a mapping: " + e.F);		
	}

	@Override
	public OplObject visit(OplProgram env, OplPresTrans e) {
		OplObject src = ENV.get(e.src0);
		OplObject dst = ENV.get(e.dst0);
		if (!(src instanceof OplPres)) {
			throw new RuntimeException("Source is not a presentation in " + e);
		}
		if (!(dst instanceof OplPres)) {
			throw new RuntimeException("Target is not a presentation in " + e);
		}
		OplPres src0 = (OplPres) src;
		OplPres dst0 = (OplPres) dst;
		e.validateNotReally(src0, dst0);
		e.toMapping();
		return e;
	}

	@Override
	public OplObject visit(OplProgram env, OplFlower e) {
		OplObject I0 = ENV.get(e.I0);
		if (I0 instanceof OplSetInst) {
			OplSetInst I = (OplSetInst) I0;
			return (OplObject) e.eval(I).second;
		}
		if (I0 instanceof OplSetTrans) {
			OplSetTrans h = (OplSetTrans) I0;
			return e.eval(h);
		}
		throw new RuntimeException("Not a set model or transform: " + e.I0);
	}

	@Override
	public OplObject visit(OplProgram env, OplSchema e) {
		OplObject I0 = ENV.get(e.sig0);
		if (I0 instanceof OplSig) {
			OplSig I = (OplSig) I0;
			e.validate(I);
			return e;
		}
		throw new RuntimeException("Not a theory: " + e.sig0);
	}
	
	@Override
	public OplObject visit(OplProgram env, OplSchemaProj e) {
		OplObject I0 = ENV.get(e.sch0);
		if (I0 instanceof OplSchema) {
			OplSchema I = (OplSchema) I0;
			return e.proj(I);
		}
		throw new RuntimeException("Not a schema: " + e.sch0);
	}
	
	@Override
	public OplObject visit(OplProgram env, OplInst e) {
		OplSchema S;
		OplPres P;
		OplJavaInst J = null;
		
		OplObject S0 = ENV.get(e.S0);
		if (S0 instanceof OplSchema) {
			S = (OplSchema) S0;
		} else {
			throw new RuntimeException("Not a schema: " + e.S0);
		}

		OplObject P0 = ENV.get(e.P0);
		if (P0 instanceof OplPres) {
			P = (OplPres) P0;
		} else {
			throw new RuntimeException("Not a presentation: " + e.P0);
		}
		
		if (e.J0.equals("none")) {
			
		} else {
			OplObject J0 = ENV.get(e.J0);
			if (J0 instanceof OplJavaInst) {
				J = (OplJavaInst) J0;
			} else {
				throw new RuntimeException("Not a JS model: " + e.J0);
			}
		}

		e.validate(S, P, J);
		return e;
	}

	@Override
	public OplObject visit(OplProgram env, OplQuery e) {
		OplSchema I, J;

		OplObject I0 = ENV.get(e.src_e);
		if (I0 instanceof OplSchema) {
			I = (OplSchema) I0;
		} else {
			throw new RuntimeException("Not a schema: " + e.src_e);
		}
		
		OplObject J0 = ENV.get(e.dst_e);
		if (J0 instanceof OplSchema) {
			J = (OplSchema) J0;
		} else {
			throw new RuntimeException("Not a schema: " + e.dst_e);
		}
		
		e.validate(I, J);
		return e;
	}

	@Override
	public OplObject visit(OplProgram env, OplId e) {
		OplObject I0 = ENV.get(e.s);
		if (I0 instanceof OplSchema) {
			return OplQuery.id(e.s, (OplSchema)I0);
		} 
		throw new RuntimeException("Not a schema: " + e.s);
	}
	
	@Override
	public OplObject visit(OplProgram env, OplApply e) {
		OplObject Q0 = ENV.get(e.Q0);
		if (!(Q0 instanceof OplQuery)) {
			throw new RuntimeException("Not a query: " + e.Q0);
		} 
		OplObject I0 = ENV.get(e.I0);
		if (!(I0 instanceof OplInst)) {
			throw new RuntimeException("Not an instance: " + e.I0);
		} 
		e.validate((OplQuery)Q0, (OplInst)I0);
		return e.Q.eval(e.I);
	}

	@Override
	public OplObject visit(OplProgram env, OplTyMapping e) {
		return e;
	}
	
}
