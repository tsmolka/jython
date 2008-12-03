// Autogenerated AST node
package org.python.antlr.ast;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.python.antlr.AST;
import org.python.antlr.PythonTree;
import org.python.antlr.adapter.AstAdapters;
import org.python.core.AstList;
import org.python.core.Py;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyType;
import org.python.expose.ExposedGet;
import org.python.expose.ExposedMethod;
import org.python.expose.ExposedNew;
import org.python.expose.ExposedSet;
import org.python.expose.ExposedType;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

@ExposedType(name = "_ast.With", base = AST.class)
public class With extends stmtType {
public static final PyType TYPE = PyType.fromClass(With.class);
    private exprType context_expr;
    public exprType getInternalContext_expr() {
        return context_expr;
    }
    @ExposedGet(name = "context_expr")
    public PyObject getContext_expr() {
        return context_expr;
    }
    @ExposedSet(name = "context_expr")
    public void setContext_expr(PyObject context_expr) {
        this.context_expr = AstAdapters.py2expr(context_expr);
    }

    private exprType optional_vars;
    public exprType getInternalOptional_vars() {
        return optional_vars;
    }
    @ExposedGet(name = "optional_vars")
    public PyObject getOptional_vars() {
        return optional_vars;
    }
    @ExposedSet(name = "optional_vars")
    public void setOptional_vars(PyObject optional_vars) {
        this.optional_vars = AstAdapters.py2expr(optional_vars);
    }

    private java.util.List<stmtType> body;
    public java.util.List<stmtType> getInternalBody() {
        return body;
    }
    @ExposedGet(name = "body")
    public PyObject getBody() {
        return new AstList(body, AstAdapters.stmtAdapter);
    }
    @ExposedSet(name = "body")
    public void setBody(PyObject body) {
        this.body = AstAdapters.py2stmtList(body);
    }


    private final static PyString[] fields =
    new PyString[] {new PyString("context_expr"), new PyString("optional_vars"), new
                     PyString("body")};
    @ExposedGet(name = "_fields")
    public PyString[] get_fields() { return fields; }

    private final static PyString[] attributes =
    new PyString[] {new PyString("lineno"), new PyString("col_offset")};
    @ExposedGet(name = "_attributes")
    public PyString[] get_attributes() { return attributes; }

    public With() {
        this(TYPE);
    }
    public With(PyType subType) {
        super(subType);
    }
    @ExposedNew
    @ExposedMethod
    public void With___init__(PyObject[] args, String[] keywords) {}
    public With(PyObject context_expr, PyObject optional_vars, PyObject body) {
        setContext_expr(context_expr);
        setOptional_vars(optional_vars);
        setBody(body);
    }

    public With(Token token, exprType context_expr, exprType optional_vars,
    java.util.List<stmtType> body) {
        super(token);
        this.context_expr = context_expr;
        addChild(context_expr);
        this.optional_vars = optional_vars;
        addChild(optional_vars);
        this.body = body;
        if (body == null) {
            this.body = new ArrayList<stmtType>();
        }
        for(PythonTree t : this.body) {
            addChild(t);
        }
    }

    public With(Integer ttype, Token token, exprType context_expr, exprType optional_vars,
    java.util.List<stmtType> body) {
        super(ttype, token);
        this.context_expr = context_expr;
        addChild(context_expr);
        this.optional_vars = optional_vars;
        addChild(optional_vars);
        this.body = body;
        if (body == null) {
            this.body = new ArrayList<stmtType>();
        }
        for(PythonTree t : this.body) {
            addChild(t);
        }
    }

    public With(PythonTree tree, exprType context_expr, exprType optional_vars,
    java.util.List<stmtType> body) {
        super(tree);
        this.context_expr = context_expr;
        addChild(context_expr);
        this.optional_vars = optional_vars;
        addChild(optional_vars);
        this.body = body;
        if (body == null) {
            this.body = new ArrayList<stmtType>();
        }
        for(PythonTree t : this.body) {
            addChild(t);
        }
    }

    @ExposedGet(name = "repr")
    public String toString() {
        return "With";
    }

    public String toStringTree() {
        StringBuffer sb = new StringBuffer("With(");
        sb.append("context_expr=");
        sb.append(dumpThis(context_expr));
        sb.append(",");
        sb.append("optional_vars=");
        sb.append(dumpThis(optional_vars));
        sb.append(",");
        sb.append("body=");
        sb.append(dumpThis(body));
        sb.append(",");
        sb.append(")");
        return sb.toString();
    }

    public <R> R accept(VisitorIF<R> visitor) throws Exception {
        return visitor.visitWith(this);
    }

    public void traverse(VisitorIF visitor) throws Exception {
        if (context_expr != null)
            context_expr.accept(visitor);
        if (optional_vars != null)
            optional_vars.accept(visitor);
        if (body != null) {
            for (PythonTree t : body) {
                if (t != null)
                    t.accept(visitor);
            }
        }
    }

    private int lineno = -1;
    @ExposedGet(name = "lineno")
    public int getLineno() {
        if (lineno != -1) {
            return lineno;
        }
        return getLine();
    }

    @ExposedSet(name = "lineno")
    public void setLineno(int num) {
        lineno = num;
    }

    private int col_offset = -1;
    @ExposedGet(name = "col_offset")
    public int getCol_offset() {
        if (col_offset != -1) {
            return col_offset;
        }
        return getCharPositionInLine();
    }

    @ExposedSet(name = "col_offset")
    public void setCol_offset(int num) {
        col_offset = num;
    }

}
