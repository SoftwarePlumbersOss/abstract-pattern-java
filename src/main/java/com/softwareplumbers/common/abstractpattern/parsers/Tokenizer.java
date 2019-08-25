/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwareplumbers.common.abstractpattern.parsers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author jonathan.local
 */
public class Tokenizer implements Iterator<Token> {
    
    private final CharSequence input;
    private final char escape;
    private final List<String> operators;
    private Token nextToken;
    private int pos;
    
    public Tokenizer(CharSequence input, char escape, String... operators) {
        this.input = input;
        this.escape =escape;
        this.pos = 0;
        this.operators = Arrays.asList(operators);
        moveNext();
    }
    
    private char lookahead() {
        return input.charAt(pos);
    }
    
    private CharSequence consume(int chars) {
        CharSequence result = input.subSequence(pos, pos+chars);
        pos+=chars;
        return result;
    }
    
    public boolean hasNext() {
        return nextToken != null;
    }
    
    public Token next() {
        Token result = nextToken;
        moveNext();
        return result;
    }
    
    public Token current() {
        return nextToken;
    }
    
    private boolean matchOperator(String operator) {
        int length = operator.length();
        if (length > input.length() - pos) return false;
        return operator.contentEquals(input.subSequence(pos, pos + length));
    }

    private Optional<String> matchAnyOperator() { 
        return operators
                .stream()
                .filter(this::matchOperator)
                .findAny();
    }
    
    private void moveNext() {
        
        StringBuilder builder = new StringBuilder();
        Optional<String> matchedOperator = matchAnyOperator();
        
        while (!matchedOperator.isPresent() && pos < input.length()) {
            if (lookahead() == escape) {
                consume(1);
            } 
            builder.append(consume(1));
            matchedOperator = matchAnyOperator();
        }
        
        if (builder.length() > 0) {
            nextToken = new Token(Token.Type.CHAR_SEQUENCE, builder);
        } else {
            if (matchedOperator.isPresent()) {
                String op = matchedOperator.get();
                consume(op.length());
                nextToken = new Token(Token.Type.OPERATOR, op);
            } else {
                nextToken = null;
            }
        }  
    }
    
    
}
