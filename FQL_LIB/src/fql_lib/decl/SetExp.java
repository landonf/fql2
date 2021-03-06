package fql_lib.decl;

import java.io.Serializable;
import java.util.Set;
//import fql.parse.PrettyPrinter;





import fql_lib.Util;

public abstract class SetExp implements Serializable{
	
	public static class Apply extends SetExp {
		public String f;
		public SetExp set;
		
		public Apply(String f, SetExp set) {
			this.f = f;
			this.set = set;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((f == null) ? 0 : f.hashCode());
			result = prime * result + ((set == null) ? 0 : set.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Apply other = (Apply) obj;
			if (f == null) {
				if (other.f != null)
					return false;
			} else if (!f.equals(other.f))
				return false;
			if (set == null) {
				if (other.set != null)
					return false;
			} else if (!set.equals(other.set))
				return false;
			return true;
		}

		@Override
		public <R, E> R accept(E env, SetExpVisitor<R, E> v) {
			return v.visit(env, this);
		}

	}
	
	public static class Numeral extends SetExp {
		public int i;
		
		@Override
		public <R, E> R accept(E env, SetExpVisitor<R, E> v) {
			return v.visit(env, this);
		}

		public Numeral(int i) {
			this.i = i;
		}

		@Override
		public String toString() {
			return Integer.toString(i);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + i;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Numeral other = (Numeral) obj;
			if (i != other.i)
				return false;
			return true;
		}
		
	}
		
	public static class Const extends SetExp {
		
		@Override
		public String toString() {
			return Util.nice(s.toString());
		}
		
		Set<?> s;
		
		public Const(Set<?> s) {
			super();
			this.s = s;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((s == null) ? 0 : s.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Const other = (Const) obj;
			if (s == null) {
				if (other.s != null)
					return false;
			} else if (!s.equals(other.s))
				return false;
			return true;
		}

		@Override
		public <R, E> R accept(E env, SetExpVisitor<R, E> v) {
			return v.visit(env, this);
		}
	}

	public static class Var extends SetExp {
		public String v;

		public Var(String v) {
			if (v.contains(" ") || v.equals("void") || v.equals("unit")) {
				throw new RuntimeException("Cannot var " + v);
			}
			this.v = v;
		}

		@Override
		public String toString() {
			return v;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((v == null) ? 0 : v.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Var other = (Var) obj;
			if (v == null) {
				if (other.v != null)
					return false;
			} else if (!v.equals(other.v))
				return false;
			return true;
		}

		@Override
		public <R, E> R accept(E env, SetExpVisitor<R, E> v) {
			return v.visit(env, this);
		}
	}

	public static class Zero extends SetExp {

		public Zero() {
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public boolean equals(Object obj) {
			return (obj instanceof Zero);
		}

		@Override
		public String toString() {
			return "void";
		}

		@Override
		public <R, E> R accept(E env, SetExpVisitor<R, E> v) {
			return v.visit(env, this);
		}
	}

	public static class Dom extends SetExp {

		public Dom(FnExp f) {
			this.f = f;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((f == null) ? 0 : f.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Dom other = (Dom) obj;
			if (f == null) {
				if (other.f != null)
					return false;
			} else if (!f.equals(other.f))
				return false;
			return true;
		}

		public FnExp f;
		
		@Override
		public String toString() {
			return "dom " + f;
		}

		@Override
		public <R, E> R accept(E env, SetExpVisitor<R, E> v) {
			return v.visit(env, this);
		}
	}
	
	public static class Cod extends SetExp {

		public Cod(FnExp f) {
			this.f = f;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((f == null) ? 0 : f.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Cod other = (Cod) obj;
			if (f == null) {
				if (other.f != null)
					return false;
			} else if (!f.equals(other.f))
				return false;
			return true;
		}

		public FnExp f;
		
		@Override
		public String toString() {
			return "cod " + f;
		}

		@Override
		public <R, E> R accept(E env, SetExpVisitor<R, E> v) {
			return v.visit(env, this);
		}
	}

	public static class Range extends SetExp {

		public Range(FnExp f) {
			this.f = f;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((f == null) ? 0 : f.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Range other = (Range) obj;
			if (f == null) {
				if (other.f != null)
					return false;
			} else if (!f.equals(other.f))
				return false;
			return true;
		}

		public FnExp f;
		
		@Override
		public String toString() {
			return "range " + f;
		}

		@Override
		public <R, E> R accept(E env, SetExpVisitor<R, E> v) {
			return v.visit(env, this);
		}
	}
	
	public static class One extends SetExp {

		public One() {
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public boolean equals(Object obj) {
			return (obj instanceof One);
		}

		@Override
		public String toString() {
			return "unit";
		}

		@Override
		public <R, E> R accept(E env, SetExpVisitor<R, E> v) {
			return v.visit(env, this);
		}
	}
	
	public static class Prop extends SetExp {

		public Prop() {
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public boolean equals(Object obj) {
			return (obj instanceof Zero);
		}

		@Override
		public String toString() {
			return "prop";
		}

		@Override
		public <R, E> R accept(E env, SetExpVisitor<R, E> v) {
			return v.visit(env, this);
		}
	}
	

	public static class Plus extends SetExp {
		SetExp a, b;

		public Plus(SetExp a, SetExp b) {
			this.a = a;
			this.b = b;
			if (a == null || b == null) {
				throw new RuntimeException();
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((a == null) ? 0 : a.hashCode());
			result = prime * result + ((b == null) ? 0 : b.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Plus other = (Plus) obj;
			if (a == null) {
				if (other.a != null)
					return false;
			} else if (!a.equals(other.a))
				return false;
			if (b == null) {
				if (other.b != null)
					return false;
			} else if (!b.equals(other.b))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "(" + a + " + " + b + ")";
		}

		@Override
		public <R, E> R accept(E env, SetExpVisitor<R, E> v) {
			return v.visit(env, this);
		}
	}

	public static class Times extends SetExp {
		public SetExp a, b;

		public Times(SetExp a, SetExp b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((a == null) ? 0 : a.hashCode());
			result = prime * result + ((b == null) ? 0 : b.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Times other = (Times) obj;
			if (a == null) {
				if (other.a != null)
					return false;
			} else if (!a.equals(other.a))
				return false;
			if (b == null) {
				if (other.b != null)
					return false;
			} else if (!b.equals(other.b))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "(" + a + " * " + b + ")";
		}

		@Override
		public <R, E> R accept(E env, SetExpVisitor<R, E> v) {
			return v.visit(env, this);
		}
	}

	public static class Exp extends SetExp {
		public SetExp a, b;

		public Exp(SetExp a, SetExp b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((a == null) ? 0 : a.hashCode());
			result = prime * result + ((b == null) ? 0 : b.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Exp other = (Exp) obj;
			if (a == null) {
				if (other.a != null)
					return false;
			} else if (!a.equals(other.a))
				return false;
			if (b == null) {
				if (other.b != null)
					return false;
			} else if (!b.equals(other.b))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "(" + a + " ^ " + b + ")";
		}

		@Override
		public <R, E> R accept(E env, SetExpVisitor<R, E> v) {
			return v.visit(env, this);
		}

	}

	@Override
	public abstract boolean equals(Object o);

	public abstract <R, E> R accept(E env, SetExpVisitor<R, E> v);

	@Override
	public abstract int hashCode();
	
	public interface SetExpVisitor<R, E> {
		public R visit (E env, Zero e);
		public R visit (E env, One e);
		public R visit (E env, Prop e);
		public R visit (E env, Plus e);
		public R visit (E env, Times e);
		public R visit (E env, Exp e);
		public R visit (E env, Var e);
		public R visit (E env, Dom e);
		public R visit (E env, Cod e);
		public R visit (E env, Range e);
		public R visit (E env, Const e);
		public R visit (E env, Numeral e);
		public R visit (E env, Apply e);
	}
	
}
