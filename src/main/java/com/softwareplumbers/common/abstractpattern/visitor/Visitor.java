/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwareplumbers.common.abstractpattern.visitor;

/**
 *
 * @author jonathan.local
 */
public interface Visitor {
    
    public static class PatternSyntaxException extends Exception {
        public PatternSyntaxException(String reason) { super(reason); }
    }
    
    void atLeastExpr(int count) throws PatternSyntaxException;
    void anyCharExpr();
    void charSequence(String chars);
    void oneOfExpr(String charList);
    void endGroup() throws PatternSyntaxException;
    void beginGroup();
}
