/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwareplumbers.common.abstractpattern.parsers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

/**
 *
 * @author jonathan.local
 */
public class TestTokenizer {
    
    
    @Test 
    public void testTokenizeSimpleString() {
        Tokenizer tokenizer = new Tokenizer("abc123", '\\', "[", "]", "*", "?");
        assertThat(tokenizer.hasNext(), is(true));
        Token token = tokenizer.next();
        assertThat(token.data.toString(), equalTo("abc123"));
        assertThat(token.type, equalTo(Token.Type.CHAR_SEQUENCE));
        assertThat(tokenizer.hasNext(), is(false));
    }

    @Test 
    public void testTokenizeStringWithOperators() {
        Tokenizer tokenizer = new Tokenizer("ab*c1?23", '\\', "[", "]", "*", "?");
        assertThat(tokenizer.hasNext(), is(true));
        Token token = tokenizer.next();
        assertThat(token.data.toString(), equalTo("ab"));
        assertThat(token.type, equalTo(Token.Type.CHAR_SEQUENCE));
        token = tokenizer.next();
        assertThat(token.data.toString(), equalTo("*"));
        assertThat(token.type, equalTo(Token.Type.OPERATOR));
        token = tokenizer.next();
        assertThat(token.data.toString(), equalTo("c1"));
        assertThat(token.type, equalTo(Token.Type.CHAR_SEQUENCE));
        token = tokenizer.next();
        assertThat(token.data.toString(), equalTo("?"));
        assertThat(token.type, equalTo(Token.Type.OPERATOR));
        token = tokenizer.next();
        assertThat(token.data.toString(), equalTo("23"));
        assertThat(token.type, equalTo(Token.Type.CHAR_SEQUENCE));
        assertThat(tokenizer.hasNext(), is(false));
    }

    @Test 
    public void testTokenizeStringWithEscape() {
        Tokenizer tokenizer = new Tokenizer("abc\\*123", '\\', "[", "]", "*", "?");
        assertThat(tokenizer.hasNext(), is(true));
        Token token = tokenizer.next();
        assertThat(token.data.toString(), equalTo("abc*123"));
        assertThat(token.type, equalTo(Token.Type.CHAR_SEQUENCE));
        assertThat(tokenizer.hasNext(), is(false));
    }

}
