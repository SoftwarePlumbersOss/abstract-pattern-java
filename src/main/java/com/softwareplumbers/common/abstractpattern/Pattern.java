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
import java.util.stream.Stream;

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
    
    /** A universal empty pattern, which matches nothing.
     * 
     */
    public static Pattern EMPTY = new Pattern() {
        @Override
        public Type getType() {
            return Type.EMPTY;
        }

        @Override
        public void visit(Visitor visitor) throws Visitor.PatternSyntaxException {
        }
        
        @Override 
        public boolean isSimple() { return true; }
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

        @Override 
        public boolean isSimple() { return false; }
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
        
        @Override 
        public boolean isSimple() { return true; }

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
        
        @Override 
        public boolean isSimple() { return false; }
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
        
        @Override 
        public boolean isSimple() { return false; }
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
        
        @Override 
        public boolean isSimple() { return Stream.of(patterns).allMatch(Pattern::isSimple); }
    }
    
    /** Match this pattern against some string.
     * 
     * @param matchable String to match
     * @return true if matchable matches this pattern
     * @throws Visitor.PatternSyntaxException if this patten is invalid
     */    
    public boolean match(String matchable) throws Visitor.PatternSyntaxException {
        return build(Builders.toPattern()).matcher(matchable).matches();
    }
    
    /** Create a pattern that matches first this pattern then another.
     * 
     * @param next Next pattern to match
     * @return A pattern matching first this pattern, then the next
     */
    public Pattern then(Pattern next) {
        return new GroupExpr(this, next);
    }
    
    /** Create a pattern that matches several instances of this pattern. 
     * 
     * @param count The minimum number of instances if this pattern to match
     * @return A pattern matching at least the specified number of instances of this pattern
     */
    public Pattern atLeast(int count) {
        return new AtLeast(new GroupExpr(this), count);
    }
    
    /** Build something from this pattern.
     * 
     * @param <T> the type of the thing we are building
     * @param builder builder used to build something
     * @return the item built
     * @throws Visitor.PatternSyntaxException if the given builder cannot process this pattern
     */
    public <T> T build(Builder<T> builder) throws Visitor.PatternSyntaxException { 
        visit(builder); return builder.build(); 
    }

    /** Create a pattern that matches the given character sequence.
     * 
     * @param chars
     * @return a pattern that matches the given character sequence
     */
    public static Pattern of(String chars) {
        return new CharSequence(chars);
    }
    
    /** Create a pattern that matches one of the given characters.
     * 
     * @param charList
     * @return a pattern that matches one of the given character list
     */
    public static Pattern oneOf(String charList) {
        return new OneOfExpr(charList);
    }

    /** Create a pattern that matches several sub-patterns in sequence.
     * 
     * @param patterns the sequence of patterns to match (in order...)
     * @return a pattern that matches all of the given patterns in sequence
     */
    public static Pattern of(Pattern... patterns) {
        return new GroupExpr(patterns);
    }
    
    /** Create a pattern that matches any character.
     * 
`    * @return a pattern that matches all of the given patterns in sequence 
     */
    public static Pattern anyChar() {
        return new AnyCharExpr();
    }
    
    /** Get the type of this pattern.
     * 
     * @return the type of this pattern. (one of Pattern.Type)
     */
    public abstract Type getType();
    
    /** Check to see if this is a simple pattern.
     * 
     * A simple pattern matches for a single character sequence
     * 
     * @return true if this is a simple pattern
     */
    public abstract boolean isSimple();

}
