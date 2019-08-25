/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwareplumbers.common.abstractpattern.visitor;

import com.softwareplumbers.common.abstractpattern.Pattern;
import java.util.Stack;

/**
 *
 * @author jonathan.local
 */
public abstract class GroupingBuilder implements Builder<String> {
                
    private final Stack<StringBuilder> groups = new Stack<>();
    private Pattern.Type currentType;
    private String currentChars;
    private int currentCount;
    
    abstract void appendPattern(StringBuilder builder, Pattern.Type type, String chars, int count);

    private void setCurrent(Pattern.Type type, String chars, int count) {
        this.currentType = type;
        this.currentChars = chars;
        this.currentCount = count;
    }
    
    public GroupingBuilder() {
        groups.push(new StringBuilder());
        setCurrent(Pattern.Type.EMPTY, null, 0);
    }
    
    @Override
    public String build() {
        StringBuilder result = new StringBuilder();
        groups.forEach(group -> result.append(group));
        appendPattern(result, currentType, currentChars, currentCount);
        return result.toString();
    }

    @Override
    public void atLeastExpr(int count) {
        StringBuilder param = new StringBuilder();
        appendPattern(param, currentType, currentChars, currentCount);
        setCurrent(Pattern.Type.AT_LEAST, param.toString(), count);        
    }

    @Override
    public void anyCharExpr() {
        appendPattern(groups.lastElement(), currentType, currentChars, currentCount);
        setCurrent(Pattern.Type.ANY_CHAR_EXPR, null, 0);
    }

    @Override
    public void charSequence(String chars) {
        appendPattern(groups.lastElement(), currentType, currentChars, currentCount);
        setCurrent(Pattern.Type.CHAR_SEQUENCE, chars, 0);
    }

    @Override
    public void oneOfExpr(String charList) {
        appendPattern(groups.lastElement(), currentType, currentChars, currentCount);
        setCurrent(Pattern.Type.ONE_OF_EXPR, charList, 0);
    }

    @Override
    final public void endGroup() {
        appendPattern(groups.lastElement(), currentType, currentChars, currentCount);
        setCurrent(Pattern.Type.GROUP_EXPR, groups.pop().toString(), 0);
    }

    @Override
    final public void beginGroup() {
        appendPattern(groups.lastElement(), currentType, currentChars, currentCount);
        groups.push(new StringBuilder());   
        setCurrent(Pattern.Type.EMPTY, null, 0);
    }
    
}
