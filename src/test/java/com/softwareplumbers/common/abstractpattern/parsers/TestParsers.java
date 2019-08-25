/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwareplumbers.common.abstractpattern.parsers;

import com.softwareplumbers.common.abstractpattern.Pattern;
import com.softwareplumbers.common.abstractpattern.visitor.Visitor;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


/**
 *
 * @author Jonathan Essex
 */
public class TestParsers {
    
    @Test
    public void testParseUnixWildcardSimpleText() throws Visitor.PatternSyntaxException {
        Pattern pattern = Parsers.parseUnixWildcard("abc");
        assertThat(pattern.match("abc"), equalTo(true));
        assertThat(pattern.match("abcx"), equalTo(false));
        assertThat(pattern.match("xabc"), equalTo(false));
        assertThat(pattern.match("ab"), equalTo(false));
        assertThat(pattern.match("bc"), equalTo(false));
        assertThat(pattern.match(""), equalTo(false));
    }

    @Test
    public void testParseUnixWildcardWithSinglePlaceholder() throws Visitor.PatternSyntaxException {
        Pattern pattern = Parsers.parseUnixWildcard("a?c");
        assertThat(pattern.match("abc"), equalTo(true));
        assertThat(pattern.match("abcx"), equalTo(false));
        assertThat(pattern.match("xabc"), equalTo(false));
        assertThat(pattern.match("axc"), equalTo(true));
        assertThat(pattern.match("axyc"), equalTo(false));
    }

    @Test
    public void testParseUnixWildcardWithMultiplePlaceholder() throws Visitor.PatternSyntaxException {
        Pattern pattern = Parsers.parseUnixWildcard("a*c");
        assertThat(pattern.match("abc"), equalTo(true));
        assertThat(pattern.match("abcx"), equalTo(false));
        assertThat(pattern.match("xabc"), equalTo(false));
        assertThat(pattern.match("axc"), equalTo(true));
        assertThat(pattern.match("axyc"), equalTo(true));
    }
    
    @Test
    public void testParseSQL92SimpleText() throws Visitor.PatternSyntaxException {
        Pattern pattern = Parsers.parseSQL92("abc",'\\');
        assertThat(pattern.match("abc"), equalTo(true));
        assertThat(pattern.match("abcx"), equalTo(false));
        assertThat(pattern.match("xabc"), equalTo(false));
        assertThat(pattern.match("ab"), equalTo(false));
        assertThat(pattern.match("bc"), equalTo(false));
        assertThat(pattern.match(""), equalTo(false));
    }

    @Test
    public void testParseSQL92WithSinglePlaceholder() throws Visitor.PatternSyntaxException {
        Pattern pattern = Parsers.parseSQL92("a_c",'\\');
        assertThat(pattern.match("abc"), equalTo(true));
        assertThat(pattern.match("abcx"), equalTo(false));
        assertThat(pattern.match("xabc"), equalTo(false));
        assertThat(pattern.match("axc"), equalTo(true));
        assertThat(pattern.match("axyc"), equalTo(false));
    }

    @Test
    public void testParseSQL92WithMultiplePlaceholder() throws Visitor.PatternSyntaxException {
        Pattern pattern = Parsers.parseSQL92("a%c",'\\');
        assertThat(pattern.match("abc"), equalTo(true));
        assertThat(pattern.match("abcx"), equalTo(false));
        assertThat(pattern.match("xabc"), equalTo(false));
        assertThat(pattern.match("axc"), equalTo(true));
        assertThat(pattern.match("axyc"), equalTo(true));
    }
    
}
