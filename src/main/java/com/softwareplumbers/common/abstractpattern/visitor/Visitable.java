/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwareplumbers.common.abstractpattern.visitor;

/** Visitable interface for Patterns
 *
 * @author jonathan.local
 */
public interface Visitable {
    public void visit(Visitor visitor) throws Visitor.PatternSyntaxException;
}
