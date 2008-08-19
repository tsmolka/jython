package org.python.antlr;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;

import org.python.core.Py;
import org.python.core.PyComplex;
import org.python.core.PyFloat;
import org.python.core.PyInteger;
import org.python.core.PyLong;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyUnicode;
import org.python.antlr.ParseException;
import org.python.antlr.ast.aliasType;
import org.python.antlr.ast.argumentsType;
import org.python.antlr.ast.boolopType;
import org.python.antlr.ast.comprehensionType;
import org.python.antlr.ast.Context;
import org.python.antlr.ast.cmpopType;
import org.python.antlr.ast.excepthandlerType;
import org.python.antlr.ast.exprType;
import org.python.antlr.ast.expr_contextType;
import org.python.antlr.ast.keywordType;
import org.python.antlr.ast.modType;
import org.python.antlr.ast.operatorType;
import org.python.antlr.ast.sliceType;
import org.python.antlr.ast.stmtType;
import org.python.antlr.ast.unaryopType;
import org.python.antlr.ast.Assert;
import org.python.antlr.ast.Assign;
import org.python.antlr.ast.Attribute;
import org.python.antlr.ast.AugAssign;
import org.python.antlr.ast.BinOp;
import org.python.antlr.ast.BoolOp;
import org.python.antlr.ast.Break;
import org.python.antlr.ast.Call;
import org.python.antlr.ast.ClassDef;
import org.python.antlr.ast.Compare;
import org.python.antlr.ast.Continue;
import org.python.antlr.ast.Delete;
import org.python.antlr.ast.Dict;
import org.python.antlr.ast.Ellipsis;
import org.python.antlr.ast.ErrorStmt;
import org.python.antlr.ast.Exec;
import org.python.antlr.ast.Expr;
import org.python.antlr.ast.Expression;
import org.python.antlr.ast.ExtSlice;
import org.python.antlr.ast.For;
import org.python.antlr.ast.FunctionDef;
import org.python.antlr.ast.GeneratorExp;
import org.python.antlr.ast.Global;
import org.python.antlr.ast.If;
import org.python.antlr.ast.IfExp;
import org.python.antlr.ast.Index;
import org.python.antlr.ast.Import;
import org.python.antlr.ast.ImportFrom;
import org.python.antlr.ast.Interactive;
import org.python.antlr.ast.Lambda;
import org.python.antlr.ast.ListComp;
import org.python.antlr.ast.Module;
import org.python.antlr.ast.Name;
import org.python.antlr.ast.Num;
import org.python.antlr.ast.Slice;
import org.python.antlr.ast.Subscript;
import org.python.antlr.ast.TryExcept;
import org.python.antlr.ast.TryFinally;
import org.python.antlr.ast.Tuple;
import org.python.antlr.ast.Pass;
import org.python.antlr.ast.Print;
import org.python.antlr.ast.Raise;
import org.python.antlr.ast.Repr;
import org.python.antlr.ast.Return;
import org.python.antlr.ast.Str;
import org.python.antlr.ast.UnaryOp;
import org.python.antlr.ast.With;
import org.python.antlr.ast.While;
import org.python.antlr.ast.Yield;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class GrammarActions {
    private ErrorHandler errorHandler = null;
    public GrammarActions() {
    }

    public void setErrorHandler(ErrorHandler eh) {
        this.errorHandler = eh;
    }

    String makeFromText(List dots, String name) {
        StringBuffer d = new StringBuffer();
        if (dots != null) {
            for (int i=0;i<dots.size();i++) {
                d.append(".");
            }
        }
        if (name != null) {
            d.append(name);
        }
        return d.toString();
    }

    int makeLevel(List lev) {
        if (lev == null) {
            return 0;
        }
        return lev.size();
    }

    aliasType[] makeStarAlias(Token t) {
        return new aliasType[]{new aliasType(t, "*", null)};
    }

    aliasType[] makeAliases(aliasType[] atypes) {
        if (atypes == null) {
            return new aliasType[0];
        }
        return atypes;
    }

    exprType[] makeBases(exprType etype) {
        if (etype != null) {
            if (etype instanceof Tuple) {
                return ((Tuple)etype).elts;
            }
            return new exprType[]{etype};
        }
        return new exprType[0];
    }

    String[] makeNames(List names) {
        List<String> s = new ArrayList<String>();
        for(int i=0;i<names.size();i++) {
            s.add(((Token)names.get(i)).getText());
        }
        return s.toArray(new String[s.size()]);
    }

    void errorGenExpNotSoleArg(PythonTree t) {
        errorHandler.error("Generator expression must be parenthesized if not sole argument", t);
    }

    exprType[] makeExprs(List exprs) {
        return makeExprs(exprs, 0);
    }

    exprType[] makeExprs(List exprs, int start) {
        if (exprs != null) {
            List<exprType> result = new ArrayList<exprType>();
            for (int i=start; i<exprs.size(); i++) {
                Object o = exprs.get(i);
                if (o instanceof exprType) {
                    result.add((exprType)o);
                } else if (o instanceof PythonParser.test_return) {
                    result.add((exprType)((PythonParser.test_return)o).tree);
                }
            }
            return result.toArray(new exprType[result.size()]);
        }
        return new exprType[0];
    }
    
    stmtType[] makeElses(List elseSuite, List elifs) {
        stmtType[] o;
        o = makeStmts(elseSuite);
        if (elifs != null) {
            ListIterator iter = elifs.listIterator(elifs.size());
            while (iter.hasPrevious()) {
                If elif = (If)iter.previous();
                elif.orelse = o;
                o = new stmtType[]{elif};
            }
        }
        return o;
    }

    stmtType[] makeStmts(List stmts) {
        if (stmts != null) {
            List<stmtType> result = new ArrayList<stmtType>();
            for (int i=0; i<stmts.size(); i++) {
                Object o = stmts.get(i);
                if (o instanceof stmtType) {
                    result.add((stmtType)o);
                } else {
                    result.add((stmtType)((PythonParser.stmt_return)o).tree);
                }
            }
            return (stmtType[])result.toArray(new stmtType[result.size()]);
        }
        return new stmtType[0];
    }

    exprType makeDottedAttr(Token nameToken, List attrs) {
        exprType current = new Name(nameToken, nameToken.getText(), expr_contextType.Load);
        for (int i=attrs.size() - 1; i > -1; i--) {
            Token t = ((PythonTree)attrs.get(i)).token;
            current = new Attribute(t, current, t.getText(),
                expr_contextType.Load);
        }
        return current;
    }

    stmtType makeWhile(Token t, exprType test, List body, List orelse) {
        if (test == null) {
            return errorHandler.errorStmt(new PythonTree(t));
        }
        stmtType[] o = makeStmts(orelse);
        stmtType[] b = makeStmts(body);
        return new While(t, test, b, o);
    }

    stmtType makeFor(Token t, exprType target, exprType iter, List body, List orelse) {
        if (target == null || iter == null) {
            return errorHandler.errorStmt(new PythonTree(t));
        }

        stmtType[] o = makeStmts(orelse);
        stmtType[] b = makeStmts(body);
        return new For(t, target, iter, b, o);
    }

    stmtType makeTryExcept(Token t, List body, List handlers, List orelse, List finBody) {
        stmtType[] b = (stmtType[])body.toArray(new stmtType[body.size()]);
        excepthandlerType[] e = (excepthandlerType[])handlers.toArray(new excepthandlerType[handlers.size()]);
        stmtType[] o;
        if (orelse != null) {
            o = (stmtType[])orelse.toArray(new stmtType[orelse.size()]);
        } else {
            o = new stmtType[0];
        }
 
        stmtType te = new TryExcept(t, b, e, o);
        if (finBody == null) {
            return te;
        }
        stmtType[] f = (stmtType[])finBody.toArray(new stmtType[finBody.size()]);
        stmtType[] mainBody = new stmtType[]{te};
        return new TryFinally(t, mainBody, f);
    }

    TryFinally makeTryFinally(Token t,  List body, List finBody) {
        stmtType[] b = (stmtType[])body.toArray(new stmtType[body.size()]);
        stmtType[] f = (stmtType[])finBody.toArray(new stmtType[finBody.size()]);
        return new TryFinally(t, b, f);
    }
 
    stmtType makeFunctionDef(PythonTree t, PythonTree nameToken, argumentsType args, List funcStatements, List decorators) {
        if (nameToken == null) {
            return errorHandler.errorStmt(t);
        }
        cantBeNone(nameToken);
        argumentsType a;
        if (args != null) {
            a = args;
        } else {
            a = new argumentsType(t, new exprType[0], null, null, new exprType[0]); 
        }
        stmtType[] s = (stmtType[])funcStatements.toArray(new stmtType[funcStatements.size()]);
        exprType[] d;
        if (decorators != null) {
            d = (exprType[])decorators.toArray(new exprType[decorators.size()]);
        } else {
            d = new exprType[0];
        }
        return new FunctionDef(t, nameToken.getText(), a, s, d);
    }

    exprType[] makeAssignTargets(exprType lhs, List rhs) {
        exprType[] e = new exprType[rhs.size()];
        e[0] = lhs;
        for(int i=0;i<rhs.size() - 1;i++) {
            Object o = rhs.get(i);
            if (o instanceof PythonParser.testlist_return) {
                //XXX: Check to see if this is really happening anymore
                PythonParser.testlist_return r = (PythonParser.testlist_return)o;
                e[i] = (exprType)r.getTree();
            } else {
                e[i] = (exprType)o;
            }
        }
        return e;
    }

    exprType makeAssignValue(List rhs) {
        exprType value = (exprType)rhs.get(rhs.size() -1);

        if (value instanceof Context) {
            //XXX: for Tuples, etc -- will need to recursively set to expr_contextType.Load.
            ((Context)value).setContext(expr_contextType.Load);
        }
        return value;
    }

    argumentsType makeArgumentsType(Token t, List params, Token snameToken,
        Token knameToken, List defaults) {

        exprType[] p;
        if (params == null) {
            p = new exprType[0];
        } else {
            p = (exprType[])params.toArray(new exprType[params.size()]);
        }
        exprType[] d = (exprType[])defaults.toArray(new exprType[defaults.size()]);
        String s;
        String k;
        if (snameToken == null) {
            s = null;
        } else {
            s = snameToken.getText();
        }
        if (knameToken == null) {
            k = null;
        } else {
            k = knameToken.getText();
        }
        return new argumentsType(t, p, s, k, d);
    }

    exprType[] extractArgs(List args) {
        if (args == null) {
            return new exprType[0];
        }
        return (exprType[])args.toArray(new exprType[args.size()]);
    }

    keywordType[] makeKeywords(List args) {
        List<keywordType> k = new ArrayList<keywordType>();
        if (args != null) {
            for(int i=0;i<args.size();i++) {
                exprType[] e = (exprType[])args.get(i);
                Name arg = (Name)e[0];
                k.add(new keywordType(arg, arg.id, e[1]));
            }
            return k.toArray(new keywordType[k.size()]);
        }
        return new keywordType[0];
    }

    Object makeFloat(Token t) {
        return Py.newFloat(Double.valueOf(t.getText()));
    }

    Object makeComplex(Token t) {
        String s = t.getText();
        s = s.substring(0, s.length() - 1);
        return Py.newImaginary(Double.valueOf(s));
    }

    Object makeInt(Token t) {
        String s = t.getText();
        int radix = 10;
        if (s.startsWith("0x") || s.startsWith("0X")) {
            radix = 16;
            s = s.substring(2, s.length());
        } else if (s.startsWith("0")) {
            radix = 8;
        }
        if (s.endsWith("L") || s.endsWith("l")) {
            s = s.substring(0, s.length()-1);
            return Py.newLong(new BigInteger(s, radix));
        }
        int ndigits = s.length();
        int i=0;
        while (i < ndigits && s.charAt(i) == '0')
            i++;
        if ((ndigits - i) > 11) {
            return Py.newLong(new BigInteger(s, radix));
        }

        long l = Long.valueOf(s, radix).longValue();
        if (l > 0xffffffffl || (l > Integer.MAX_VALUE)) {
            return Py.newLong(new BigInteger(s, radix));
        }
        return Py.newInteger((int) l);
    }

    class StringPair {
        private String s;
        private boolean unicode;

        StringPair(String s, boolean unicode) {
            this.s = s;
            this.unicode = unicode;
        }
        String getString() {
            return s;
        }
        
        boolean isUnicode() {
            return unicode;
        }
    }

    PyString extractStrings(List s) {
        boolean ustring = false;
        Token last = null;
        StringBuffer sb = new StringBuffer();
        Iterator iter = s.iterator();
        while (iter.hasNext()) {
            last = (Token)iter.next();
            StringPair sp = extractString(last);
            if (sp.isUnicode()) {
                ustring = true;
            }
            sb.append(sp.getString());
        }
        if (ustring) {
            return new PyUnicode(sb.toString());
        }
        return new PyString(sb.toString());
    }

    StringPair extractString(Token t) {
        String s = t.getText();
        char quoteChar = s.charAt(0);
        int start=0;
        boolean ustring = false;
        if (quoteChar == 'u' || quoteChar == 'U') {
            ustring = true;
            start++;
        }
        quoteChar = s.charAt(start);
        boolean raw = false;
        if (quoteChar == 'r' || quoteChar == 'R') {
            raw = true;
            start++;
        }
        int quotes = 3;
        if (s.length() - start == 2) {
            quotes = 1;
        }
        if (s.charAt(start) != s.charAt(start+1)) {
            quotes = 1;
        }

        if (raw) {
            return new StringPair(s.substring(quotes+start, s.length()-quotes), ustring);
        } else {
            StringBuffer sb = new StringBuffer(s.length());
            char[] ca = s.toCharArray();
            int n = ca.length-quotes;
            int i=quotes+start;
            int last_i=i;
            return new StringPair(PyString.decode_UnicodeEscape(s, i, n, "strict", ustring), ustring);
            //return decode_UnicodeEscape(s, i, n, "strict", ustring);
        }
    }

    Token extractStringToken(List s) {
        return (Token)s.get(s.size() - 1);
    }

    //FROM Walker:
    modType makeMod(PythonTree t, List stmts) {
        stmtType[] s;
        if (stmts != null) {
            s = (stmtType[])stmts.toArray(new stmtType[stmts.size()]);
        } else {
            s = new stmtType[0];
        }
        return new Module(t, s);
    }

    modType makeExpression(PythonTree t, exprType e) {
        return new Expression(t, e);
    }

    modType makeInteractive(PythonTree t, List stmts) {
        stmtType[] s;
        if (stmts == null) {
            s = new stmtType[0];
        } else {
            s = (stmtType[])stmts.toArray(new stmtType[stmts.size()]);
        }
        return new Interactive(t, s);
    }

    stmtType makeClassDef(PythonTree t, PythonTree nameToken, List bases, List body) {
        if (nameToken == null) {
            return errorHandler.errorStmt(t);
        }
        cantBeNone(nameToken);
        exprType[] b = (exprType[])bases.toArray(new exprType[bases.size()]);
        stmtType[] s = (stmtType[])body.toArray(new stmtType[body.size()]);
        return new ClassDef(t, nameToken.getText(), b, s);
    }

    argumentsType makeArgumentsType(PythonTree t, List params, PythonTree snameToken,
        PythonTree knameToken, List defaults) {

        exprType[] p = (exprType[])params.toArray(new exprType[params.size()]);
        exprType[] d = (exprType[])defaults.toArray(new exprType[defaults.size()]);
        String s;
        String k;
        if (snameToken == null) {
            s = null;
        } else {
            s = snameToken.getText();
        }
        if (knameToken == null) {
            k = null;
        } else {
            k = knameToken.getText();
        }
        return new argumentsType(t, p, s, k, d);
    }

    stmtType makeTryExcept(PythonTree t, List body, List handlers, List orelse, List finBody) {
        stmtType[] b = (stmtType[])body.toArray(new stmtType[body.size()]);
        excepthandlerType[] e = (excepthandlerType[])handlers.toArray(new excepthandlerType[handlers.size()]);
        stmtType[] o;
        if (orelse != null) {
            o = (stmtType[])orelse.toArray(new stmtType[orelse.size()]);
        } else {
            o = new stmtType[0];
        }
 
        stmtType te = new TryExcept(t, b, e, o);
        if (finBody == null) {
            return te;
        }
        stmtType[] f = (stmtType[])finBody.toArray(new stmtType[finBody.size()]);
        stmtType[] mainBody = new stmtType[]{te};
        return new TryFinally(t, mainBody, f);
    }

    TryFinally makeTryFinally(PythonTree t,  List body, List finBody) {
        stmtType[] b = (stmtType[])body.toArray(new stmtType[body.size()]);
        stmtType[] f = (stmtType[])finBody.toArray(new stmtType[finBody.size()]);
        return new TryFinally(t, b, f);
    }

    stmtType makeIf(PythonTree t, exprType test, List body, List orelse) {
        if (test == null) {
            return errorHandler.errorStmt(t);
        }
        stmtType[] o;
        if (orelse != null) {
            o = (stmtType[])orelse.toArray(new stmtType[orelse.size()]);
        } else {
            o = new stmtType[0];
        }
        stmtType[] b;
        if (body != null) {
            b = (stmtType[])body.toArray(new stmtType[body.size()]);
        } else {
            b = new stmtType[0];
        }
        return new If(t, test, b, o);
    }

    stmtType makeWhile(PythonTree t, exprType test, List body, List orelse) {
        if (test == null) {
            return errorHandler.errorStmt(t);
        }
        stmtType[] o;
        if (orelse != null) {
            o = (stmtType[])orelse.toArray(new stmtType[orelse.size()]);
        } else {
            o = new stmtType[0];
        }
        stmtType[] b = (stmtType[])body.toArray(new stmtType[body.size()]);
        return new While(t, test, b, o);
    }

    stmtType makeFor(PythonTree t, exprType target, exprType iter, List body, List orelse) {
        if (target == null || iter == null) {
            return errorHandler.errorStmt(t);
        }
        cantBeNone(target);
        stmtType[] o;
        if (orelse != null) {
            o = (stmtType[])orelse.toArray(new stmtType[orelse.size()]);
        } else {
            o = new stmtType[0];
        }
        stmtType[] b = (stmtType[])body.toArray(new stmtType[body.size()]);
        return new For(t, target, iter, b, o);
    }
    
    exprType makeCall(PythonTree t, exprType func) {
        return makeCall(t, func, null, null, null, null);
    }

    exprType makeCall(PythonTree t, exprType func, List args, List keywords, exprType starargs, exprType kwargs) {
        if (func == null) {
            return errorHandler.errorExpr(t);
        }
        exprType[] a;
        keywordType[] k;
        if (args == null) {
            a = new exprType[0];
        } else {
            a = (exprType[])args.toArray(new exprType[args.size()]);
        }
        if (keywords == null) {
            k = new keywordType[0];
        } else {
            k = (keywordType[])keywords.toArray(new keywordType[keywords.size()]);
        }
        return new Call(t, func, a, k, starargs, kwargs);
    }

    exprType negate(Token t, exprType o) {
        return negate(new PythonTree(t), o);
    }

    exprType negate(PythonTree t, exprType o) {
        if (o instanceof Num) {
            Num num = (Num)o;
            if (num.n instanceof PyInteger) {
                int v = ((PyInteger)num.n).getValue();
                if (v > 0) {
                    num.n = new PyInteger(-v);
                    return num;
                }
            } else if (num.n instanceof PyLong) {
                BigInteger v = ((PyLong)num.n).getValue();
                if (v.compareTo(BigInteger.ZERO) == 1) {
                    num.n = new PyLong(v.negate());
                    return num;
                }
            } else if (num.n instanceof PyFloat) {
                double v = ((PyFloat)num.n).getValue();
                if (v > 0) {
                    num.n = new PyFloat(-v);
                    return num;
                }
            } else if (num.n instanceof PyComplex) {
                double v = ((PyComplex)num.n).imag;
                if (v > 0) {
                    num.n = new PyComplex(0,-v);
                    return num;
                }
            }
        }
        return new UnaryOp(t, unaryopType.USub, o);
    }

    void cantBeNone(PythonTree e) {
        if (e.getText().equals("None")) {
            errorHandler.error("can't be None", e);
        }
    }

    void checkAssign(exprType e) {
        if (e instanceof Name && ((Name)e).id.equals("None")) {
            errorHandler.error("assignment to None", e);
        } else if (e instanceof GeneratorExp) {
            errorHandler.error("can't assign to generator expression", e);
        } else if (e instanceof Num) {
            errorHandler.error("can't assign to number", e);
        } else if (e instanceof Yield) {
            errorHandler.error("can't assign to yield expression", e);
        } else if (e instanceof Tuple) {
            //XXX: performance problem?  Any way to do this better?
            exprType[] elts = ((Tuple)e).elts;
            for (int i=0;i<elts.length;i++) {
                checkAssign(elts[i]);
            }
        }
    }

    void checkDelete(exprType[] exprs) {
        for(int i=0;i<exprs.length;i++) {
            if (exprs[i] instanceof Call) {
                errorHandler.error("can't delete function call", exprs[i]);
            }
        }
    }

    sliceType makeSubscript(PythonTree lower, Token colon, PythonTree upper, PythonTree sliceop) {
            boolean isSlice = false;
        exprType s = null;
        exprType e = null;
        exprType o = null;
        if (lower != null) {
            s = (exprType)lower;
        }
        if (colon != null) {
            isSlice = true;
            if (upper != null) {
                e = (exprType)upper;
            }
        }
        if (sliceop != null) {
            isSlice = true;
            if (sliceop != null) {
                o = (exprType)sliceop;
            } else {
                o = new Name(sliceop, "None", expr_contextType.Load);
            }
        }

        PythonTree tok = lower;
        if (lower == null) {
            tok = new PythonTree(colon);
        }
        if (isSlice) {
           return new Slice(tok, s, e, o);
        }
        else {
           return new Index(tok, s);
        }
    }

    cmpopType[] makeCmpOps(List cmps) {
        if (cmps != null) {
            List<cmpopType> result = new ArrayList<cmpopType>();
            for (Object o: cmps) {
                result.add((cmpopType)o);
            }
            return result.toArray(new cmpopType[result.size()]);
        }
        return new cmpopType[0];
    }
}