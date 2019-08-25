/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwareplumbers.common.abstractpattern;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
/**
 *
 * @author jonathan.local
 */
public class TestPattern {

    @Test
    public void testEquals() {
        assertThat(Pattern.of("abc123"), equalTo(Pattern.of("abc123")));
        assertThat(Pattern.of("abc123"), not(Pattern.of("abd123")));
        assertThat(Pattern.anyChar(), equalTo(Pattern.anyChar()));
        assertThat(Pattern.anyChar(), not(Pattern.of("xyz")));
        assertThat(Pattern.oneOf("123"), equalTo(Pattern.oneOf("123")));
        assertThat(Pattern.oneOf("123"), not(Pattern.of("123")));
        assertThat(Pattern.anyChar().atLeast(2), equalTo(Pattern.anyChar().atLeast(2)));
        assertThat(Pattern.anyChar().atLeast(2), not(Pattern.anyChar().atLeast(1)));
        assertThat(Pattern.of("x").atLeast(1), not(Pattern.anyChar().atLeast(1)));
        assertThat(Pattern.of("xy").then(Pattern.anyChar()), equalTo(Pattern.of("xy").then(Pattern.anyChar())));
    }
    
    @Test
    public void testCharSequenceLowerBound() {
        Pattern pattern = Pattern.of("abc123");
        assertThat(pattern.lowerBound(), equalTo("abc123"));
    }

    @Test
    public void testAnyCharLowerBound() {
        Pattern pattern = Pattern.anyChar();
        assertThat(pattern.lowerBound(), equalTo(""));
    }

    @Test
    public void testOneOfLowerBound() {
        Pattern pattern = Pattern.oneOf("abc234");
        assertThat(pattern.lowerBound(), equalTo("2"));
    }

    @Test
    public void testZeroOrMoreLowerBound() {
        Pattern pattern = Pattern.anyChar().atLeast(0);
        assertThat(pattern.lowerBound(), equalTo(""));
        pattern = Pattern.of("abc123").atLeast(0);
        assertThat(pattern.lowerBound(), equalTo(""));
        pattern = Pattern.oneOf("abc123").atLeast(0);
        assertThat(pattern.lowerBound(), equalTo(""));
    }

    @Test
    public void testOneOrMoreLowerBound() {
        Pattern pattern = Pattern.anyChar().atLeast(1);
        assertThat(pattern.lowerBound(), equalTo(""));
        pattern = Pattern.of("abc123").atLeast(1);
        assertThat(pattern.lowerBound(), equalTo("abc123"));
        pattern = Pattern.oneOf("abc123").atLeast(1);
        assertThat(pattern.lowerBound(), equalTo("1"));
    }

    @Test
    public void testGroupLowerBound() {
        Pattern pattern = Pattern.of(Pattern.of("abc"), Pattern.anyChar().atLeast(1));
        assertThat(pattern.lowerBound(), equalTo("abc"));
    }
    
}
