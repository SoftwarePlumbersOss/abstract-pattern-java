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
    public void testParseUnixWildcardWithEscapedPlaceholder() throws Visitor.PatternSyntaxException {
        Pattern pattern = Parsers.parseUnixWildcard("a\\?c");
        assertThat(pattern.match("a?c"), equalTo(true));
        assertThat(pattern.match("abc"), equalTo(false));
        assertThat(pattern.match("abcx"), equalTo(false));
        assertThat(pattern.match("xabc"), equalTo(false));
        assertThat(pattern.match("axc"), equalTo(false));
        assertThat(pattern.match("axyc"), equalTo(false));
    }
    
    @Test
    public void testParseUnixWildcardWithNonstandardEscapedPlaceholder() throws Visitor.PatternSyntaxException {
        Pattern pattern = Parsers.parseUnixWildcard("a+?c",'+');
        assertThat(pattern.match("a?c"), equalTo(true));
        assertThat(pattern.match("abc"), equalTo(false));
        assertThat(pattern.match("abcx"), equalTo(false));
        assertThat(pattern.match("xabc"), equalTo(false));
        assertThat(pattern.match("axc"), equalTo(false));
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
    public void testParseUnixWildcardWithCharListPlaceholder() throws Visitor.PatternSyntaxException {
        Pattern pattern = Parsers.parseUnixWildcard("a[bc]d");
        assertThat(pattern.match("abd"), equalTo(true));
        assertThat(pattern.match("acd"), equalTo(true));
        assertThat(pattern.match("xabd"), equalTo(false));
        assertThat(pattern.match("abdx"), equalTo(false));
        assertThat(pattern.match("axd"), equalTo(false));
    }
    
    @Test
    public void testParseUnixWildcardWithEscapedCharListPlaceholder() throws Visitor.PatternSyntaxException {
        Pattern pattern = Parsers.parseUnixWildcard("a\\[bc\\]d");
        assertThat(pattern.match("abd"), equalTo(false));
        assertThat(pattern.match("acd"), equalTo(false));
        assertThat(pattern.match("xabd"), equalTo(false));
        assertThat(pattern.match("abdx"), equalTo(false));
        assertThat(pattern.match("axd"), equalTo(false));
        assertThat(pattern.match("a[bc]d"), equalTo(true));
    }    

    @Test
    public void testParseUnixWildcardWithNonstandardEscapedCharListPlaceholder() throws Visitor.PatternSyntaxException {
        Pattern pattern = Parsers.parseUnixWildcard("a+[bc+]d",'+');
        assertThat(pattern.match("abd"), equalTo(false));
        assertThat(pattern.match("acd"), equalTo(false));
        assertThat(pattern.match("xabd"), equalTo(false));
        assertThat(pattern.match("abdx"), equalTo(false));
        assertThat(pattern.match("axd"), equalTo(false));
        assertThat(pattern.match("a[bc]d"), equalTo(true));
    }    
    
    @Test
    public void testParseUnixWildcardWithQuotedPlaceholders() throws Visitor.PatternSyntaxException {
        Pattern pattern = Parsers.parseUnixWildcard("\"a[bc]d\"");
        assertThat(pattern.match("abd"), equalTo(false));
        assertThat(pattern.match("acd"), equalTo(false));
        assertThat(pattern.match("xabd"), equalTo(false));
        assertThat(pattern.match("abdx"), equalTo(false));
        assertThat(pattern.match("axd"), equalTo(false));
        assertThat(pattern.match("a[bc]d"), equalTo(true));
    }  
    
    @Test
    public void testParseUnixWildcardWithEscapedQuotedPlaceholders() throws Visitor.PatternSyntaxException {
        Pattern pattern = Parsers.parseUnixWildcard("\\\"a[bc]d\\\"");
        assertThat(pattern.match("\"abd\""), equalTo(true));
        assertThat(pattern.match("\"acd\""), equalTo(true));
        assertThat(pattern.match("x\"abd"), equalTo(false));
        assertThat(pattern.match("\"abd\"x"), equalTo(false));
        assertThat(pattern.match("\"axd\""), equalTo(false));
        assertThat(pattern.match("\"a[bc]d\""), equalTo(false));
    }  
        
    @Test
    public void testParseUnixWildcardWithNonstandardEscapedQuotedPlaceholders() throws Visitor.PatternSyntaxException {
        Pattern pattern = Parsers.parseUnixWildcard("+\"a[bc]d+\"",'+');
        assertThat(pattern.match("\"abd\""), equalTo(true));
        assertThat(pattern.match("\"acd\""), equalTo(true));
        assertThat(pattern.match("x\"abd"), equalTo(false));
        assertThat(pattern.match("\"abd\"x"), equalTo(false));
        assertThat(pattern.match("\"axd\""), equalTo(false));
        assertThat(pattern.match("\"a[bc]d\""), equalTo(false));
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
