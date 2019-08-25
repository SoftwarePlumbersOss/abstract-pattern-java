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
public interface Builder<T> extends Visitor {
    T build();
}
