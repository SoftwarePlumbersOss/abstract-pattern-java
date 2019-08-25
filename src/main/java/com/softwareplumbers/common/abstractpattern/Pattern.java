/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwareplumbers.common.abstractpattern;

import com.softwareplumbers.common.abstractpattern.visitor.Builder;
import com.softwareplumbers.common.abstractpattern.visitor.Builders;
import com.softwareplumbers.common.abstractpattern.visitor.Visitable;
import com.softwareplumbers.common.abstractpattern.visitor.Visitor;

/** Abstract representation of a matching pattern.
 *
 * @author jonathan.local
 */
public abstract class Pattern implements Visitable {
    
    public static enum Type {
        AT_LEAST,
        CHAR_SEQUENCE,
        ANY_CHAR_EXPR,
        ONE_OF_EXPR,
        GROUP_EXPR,
        EMPTY
    }
    
    public abstract Type getType();
    
    public static Pattern EMPTY= new Pattern() {
        @Override
        public Type getType() {
            return Type.EMPTY;
        }

        @Override
        public void visit(Visitor visitor) throws Visitor.PatternSyntaxException {
        }
    };

        
    private static class AtLeast extends Pattern {
        private final Pattern pattern;
        private final int count;

        @Override
        public void visit(Visitor visitor) throws Visitor.PatternSyntaxException {
            pattern.visit(visitor);
            visitor.atLeastExpr(count);
        }
        
        public AtLeast(Pattern pattern, int count) {
            this.pattern = pattern;
            this.count = count;
        }
        
        @Override
        public Type getType() { return Type.AT_LEAST; }
    }
    
    private static class CharSequence extends Pattern {
        private final String sequence;
        
        @Override
        public void visit(Visitor visitor) {
            visitor.charSequence(sequence);
        }
        
        public CharSequence(String sequence) {
            this.sequence = sequence;
        }       

        @Override
        public Type getType() { return Type.CHAR_SEQUENCE; }
    }
    
    private static class AnyCharExpr extends Pattern {
        @Override
        public void visit(Visitor visitor) {
            visitor.anyCharExpr();
        }
        
        public AnyCharExpr() {
        }       
        
        @Override
        public Type getType() { return Type.ANY_CHAR_EXPR; }
    }
    
    private static class OneOfExpr extends Pattern {
        private final String charList;
        
        @Override
        public void visit(Visitor visitor) {
            visitor.oneOfExpr(charList);
        }
        
        public OneOfExpr(String charList) {
            this.charList = charList;
        }               
        
        @Override
        public Type getType() { return Type.ONE_OF_EXPR; }
    }
    
    private static class GroupExpr extends Pattern {
        private Pattern[] patterns;
        
        @Override
        public void visit(Visitor visitor) throws Visitor.PatternSyntaxException {
            visitor.beginGroup();
            for (Pattern pattern : patterns) pattern.visit(visitor);
            visitor.endGroup();
        }
        
        public GroupExpr(Pattern... patterns) {
            this.patterns = patterns;
        }                   
        
        @Override
        public Type getType() { return Type.GROUP_EXPR; }
    }
        
    public boolean match(String matchable) throws Visitor.PatternSyntaxException {
        return toPattern().matcher(matchable).matches();
    }
    
    public java.util.regex.Pattern toPattern() throws Visitor.PatternSyntaxException {
        Builder<java.util.regex.Pattern> builder = Builders.toPattern();
        visit(builder);
        return builder.build();
    }
    
    public Pattern then(Pattern next) {
        return new GroupExpr(this, next);
    }
    
    public Pattern atLeast(int count) {
        return new AtLeast(new GroupExpr(this), count);
    }
    
    public <T> T build(Builder<T> builder) throws Visitor.PatternSyntaxException { 
        visit(builder); return builder.build(); 
    }

    
    public static Pattern of(String chars) {
        return new CharSequence(chars);
    }
    
    public static Pattern oneOf(String charList) {
        return new OneOfExpr(charList);
    }

    public static Pattern of(Pattern... patterns) {
        return new GroupExpr(patterns);
    }
    
    public static Pattern anyChar() {
        return new AnyCharExpr();
    }
    
}
