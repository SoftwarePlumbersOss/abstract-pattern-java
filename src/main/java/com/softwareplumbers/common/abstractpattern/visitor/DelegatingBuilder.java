/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwareplumbers.common.abstractpattern.visitor;

/** Builder that wraps some other builder.
 *
 * @author jonathan.local
 * @param <T> result of this builder
 * @param <U> result of wrapped builder
 */
public abstract class DelegatingBuilder<T,U>  implements Builder<T> {
    
    private Builder<U> delegate;

    protected abstract T build(Builder<U> delegate) throws Visitor.PatternSyntaxException;
    
    public DelegatingBuilder(Builder<U> delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public T build()  throws Visitor.PatternSyntaxException {
        return build(delegate);
    }

    @Override
    public void atLeastExpr(int count) throws PatternSyntaxException {
        delegate.atLeastExpr(count);
    }

    @Override
    public void anyCharExpr()  throws PatternSyntaxException {
        delegate.anyCharExpr();
    }

    @Override
    public void charSequence(String chars)  throws PatternSyntaxException {
        delegate.charSequence(chars);
    }

    @Override
    public void oneOfExpr(String charList)  throws PatternSyntaxException {
        delegate.oneOfExpr(charList);
    }

    @Override
    public void endGroup() throws PatternSyntaxException {
        delegate.endGroup();
    }

    @Override
    public void beginGroup()  throws PatternSyntaxException {
        delegate.beginGroup();
    }
    
}
