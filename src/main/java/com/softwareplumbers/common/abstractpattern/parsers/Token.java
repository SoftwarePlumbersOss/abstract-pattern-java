/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwareplumbers.common.abstractpattern.parsers;

/**
 *
 * @author jonathan.local
 */
public class Token {
    
    public enum Type { OPERATOR, CHAR_SEQUENCE };
    
    public final Type type;
    public final CharSequence data;
    
    public Token(Type type, CharSequence data) {
        this.type = type;
        this.data = data;
    }
    
}
