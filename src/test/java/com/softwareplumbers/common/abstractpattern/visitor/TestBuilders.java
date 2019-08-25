/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwareplumbers.common.abstractpattern.visitor;

import com.softwareplumbers.common.abstractpattern.Pattern;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

/**
 *
 * @author jonathan.local
 */
public class TestBuilders {
    
    @Test
    public void testCharSequenceAsUnixWildcard() throws Visitor.PatternSyntaxException {
        Pattern pattern = Pattern.of("abc123");
        assertThat(pattern.build(Builders.toUnixWildcard()), equalTo("abc123"));
    }

    @Test
    public void testAnyCharAsUnixWildcard() throws Visitor.PatternSyntaxException {
        Pattern pattern = Pattern.anyChar();
        assertThat(pattern.build(Builders.toUnixWildcard()), equalTo("?"));
    }

    @Test
    public void testOneOfAsUnixWildcard() throws Visitor.PatternSyntaxException {
        Pattern pattern = Pattern.oneOf("abc234");
        assertThat(pattern.build(Builders.toUnixWildcard()), equalTo("[abc234]"));
    }

    @Test
    public void testZeroOrMoreAsUnixWildcard() throws Visitor.PatternSyntaxException {
        Pattern pattern = Pattern.anyChar().atLeast(0);
        assertThat(pattern.build(Builders.toUnixWildcard()), equalTo("*"));
        pattern = Pattern.of("abc123").atLeast(0);
        assertThat(pattern.build(Builders.toUnixWildcard()), equalTo("*"));
        pattern = Pattern.oneOf("abc123").atLeast(0);
        assertThat(pattern.build(Builders.toUnixWildcard()), equalTo("*"));
    }

    @Test
    public void testOneOrMoreAsUnixWildcard() throws Visitor.PatternSyntaxException {
        Pattern pattern = Pattern.anyChar().atLeast(1);
        assertThat(pattern.build(Builders.toUnixWildcard()), equalTo("?*"));
        pattern = Pattern.of("abc123").atLeast(1);
        assertThat(pattern.build(Builders.toUnixWildcard()), equalTo("abc123*"));
        pattern = Pattern.oneOf("abc123").atLeast(1);
        assertThat(pattern.build(Builders.toUnixWildcard()), equalTo("[abc123]*"));
    }

    @Test
    public void testGroupAsUnixWildcard() throws Visitor.PatternSyntaxException {
        Pattern pattern = Pattern.of(Pattern.of("abc"), Pattern.anyChar().atLeast(1));
        assertThat(pattern.build(Builders.toUnixWildcard()), equalTo("abc?*"));
    }
    
    
    @Test
    public void testCharSequenceAsSQL92() throws Visitor.PatternSyntaxException {
        Pattern pattern = Pattern.of("abc123");
        assertThat(pattern.build(Builders.toUnixWildcard()), equalTo("abc123"));
    }

    @Test
    public void testAnyCharAsSQL92() throws Visitor.PatternSyntaxException {
        Pattern pattern = Pattern.anyChar();
        assertThat(pattern.build(Builders.toUnixWildcard()), equalTo("?"));
    }

    @Test
    public void testOneOfAsSQL92() throws Visitor.PatternSyntaxException {
        Pattern pattern = Pattern.oneOf("abc234");
        assertThat(pattern.build(Builders.toUnixWildcard()), equalTo("[abc234]"));
    }

    @Test
    public void testZeroOrMoreAsSQL92() throws Visitor.PatternSyntaxException {
        Pattern pattern = Pattern.anyChar().atLeast(0);
        assertThat(pattern.build(Builders.toSQL92('\\')), equalTo("%"));
        pattern = Pattern.of("abc123").atLeast(0);
        assertThat(pattern.build(Builders.toSQL92('\\')), equalTo("%"));
        pattern = Pattern.oneOf("abc123").atLeast(0);
        assertThat(pattern.build(Builders.toSQL92('\\')), equalTo("%"));
    }

    @Test
    public void testOneOrMoreAsSQL92() throws Visitor.PatternSyntaxException {
        Pattern pattern = Pattern.anyChar().atLeast(1);
        assertThat(pattern.build(Builders.toSQL92('\\')), equalTo("_%"));
        pattern = Pattern.of("abc123").atLeast(1);
        assertThat(pattern.build(Builders.toSQL92('\\')), equalTo("abc123%"));
        pattern = Pattern.oneOf("abc123").atLeast(1);
        assertThat(pattern.build(Builders.toSQL92('\\')), equalTo("_%"));
    }

    @Test
    public void testGroupAsSQL92() throws Visitor.PatternSyntaxException {
        Pattern pattern = Pattern.of(Pattern.of("abc"), Pattern.anyChar().atLeast(1));
        assertThat(pattern.build(Builders.toSQL92('\\')), equalTo("abc_%"));
    }

}
