/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwareplumbers.common.abstractpattern.visitor;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import com.softwareplumbers.common.abstractpattern.Pattern.Type;
import java.util.stream.Stream;

/** Builders which allow a Pattern to be transformed into various different representations.
 * 
 * Where the target representation does not support the complete functionality required to fully implement
 * a Pattern, the target representation should match the smallest possible superset of strings that would
 * be matched by Jonathan Essex
 *
 * @author jonathan.local
 */
public class Builders {
    
    private static class RegexBuilder implements Builder<String> {
        
        public static final int[] PATTERN_SPECIAL_CHARS = { '[',']','(',')','*','+','\\','?','|','.',',','^','$','{','}' };
        
        static { Arrays.sort(PATTERN_SPECIAL_CHARS); }
        
        StringBuilder builder = new StringBuilder();
        
        @Override
        public String build() {
            return builder.toString();
        }

        @Override
        public void atLeastExpr(int count) {
            builder.append("*");
        }

        @Override
        public void anyCharExpr() {
            builder.append(".");
        }
        
        @Override
        public void beginGroup() {
            builder.append("(");
        }

        @Override
        public void endGroup() {
            builder.append(")");
        }

        @Override
        public void charSequence(String chars) {
            builder.append(escape(chars, PATTERN_SPECIAL_CHARS, '\\'));
        }
        
        @Override
        public void oneOfExpr(String charList) {
            builder.append("[").append(escape(charList, PATTERN_SPECIAL_CHARS, '\\')).append("]");
        }

    }
    
    private static class PatternBuilder extends DelegatingBuilder<Pattern,String> {

        public PatternBuilder() {
            super(new RegexBuilder());
        }
        
        @Override
        Pattern build(Builder<String> delegate) {
            return Pattern.compile(delegate.build());
        }  
    }
    
    private static class UnixWildcardBuilder extends GroupingBuilder {

        private final int escape;
        private final int[] specialChars;
        private static final int[] DEFAULT_SPECIAL_CHARS = { '[',']','?','*' };
        private static final int[] DEFAULT_EXTRA_CHARS = {};
        
        public UnixWildcardBuilder(int escape, int[] specialChars) {
            this.escape = escape;
            this.specialChars = Stream.of(IntStream.of(DEFAULT_SPECIAL_CHARS), IntStream.of(specialChars), IntStream.of(escape))
                .flatMapToInt(s->s)
                .sorted()
                .distinct()
                .toArray();
        }
        
        public UnixWildcardBuilder(int escape) {
            this(escape, DEFAULT_EXTRA_CHARS);
        }
        
        @Override
        void appendPattern(StringBuilder builder, Type type, String chars, int count) {
            switch(type) {
                case AT_LEAST:
                    for (int i = 0; i < count; i++) builder.append(chars);
                    builder.append('*');
                    break;
                case CHAR_SEQUENCE:
                    builder.append(escape(chars, specialChars, escape));
                    break;
                case ANY_CHAR_EXPR:
                    builder.append('?');
                    break;
                case ONE_OF_EXPR:
                    builder.append('[').append(escape(chars, specialChars, escape)).append(']');
                    break;
                case GROUP_EXPR:
                    builder.append(chars);
                    break;
                case EMPTY:
                    break;
            }
        }
    }
    
    private static class SQL92Builder extends GroupingBuilder {
    
        private final int[] specialChars;
        private final int escape;

        @Override
        void appendPattern(StringBuilder builder, Type type, String chars, int count) {
            switch(type) {
                case AT_LEAST:
                    for (int i = 0; i < count; i++) builder.append(chars);
                    builder.append("%");
                    break;
                case CHAR_SEQUENCE:
                    builder.append(escape(chars, specialChars, escape));
                    break;
                case ANY_CHAR_EXPR:
                case ONE_OF_EXPR:
                    builder.append("_");
                    break;
                case GROUP_EXPR:
                    builder.append(chars);
                    break;
                case EMPTY:
                    break;
            }
        }
        
        private SQL92Builder(char escape) {
            this.escape = escape;
            specialChars = new int[] { '%', '_', escape };
            Arrays.sort(specialChars);
        }
    }
    
    private static IntStream escape(int character, int[] special_chars, int escape_char) {
        if (Arrays.binarySearch(special_chars, character) >= 0)
            return IntStream.of(escape_char, character);
        else
            return IntStream.of(character);
    }
        
    private static String escape(String chars, final int[] special_chars, final int escape_char) {
        return chars.codePoints()
            .flatMap(cp -> escape(cp, special_chars, escape_char))
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }
        
    /** Get a builder that will format a Pattern as a Unix wildcard string.
     * 
     * Unix Wildcard syntax includes * for zero or more characters, ? for a single character,
     * and a list of characters enclosed by square brackets [] to match any one of the characters
     * in the list.
     * 
     * @return a builder that encodes the pattern (so far as is possible) as a Unix wildcard expression.
     */
    public static Builder<String> toUnixWildcard(int escape) { return new UnixWildcardBuilder(escape); }
    
    public static Builder<String> toUnixWildcard(int escape, int[] extraSpecialChars) { return new UnixWildcardBuilder(escape, extraSpecialChars); }
    
    public static Builder<String> toUnixWildcard() { return new UnixWildcardBuilder('\\'); }
    
    /** Get a builder that will create a java.util.regex.Pattern.
     * 
     * @return a builder that encodes a pattern as a java regular expression pattern.
     */
    public static Builder<Pattern> toPattern() { return new PatternBuilder(); }
    
    /** Get a builder that will create a java regex string
     * 
     * @return a builder that encodes a pattern as a java regex string
     */
    public static Builder<String> toRegex() { return new RegexBuilder(); }
    
    /** 
     *  Get a builder that will format a Pattern as a SQL-92 template string for the 'like' operator.
     * 
     * SQL-92 syntax includes % for zero or more characters and _ for a single character.
     *
     * @param escape character to use to escape wildcards 
     * @return a builder that encodes a pattern (so far as is possible) as an ANSI-92 compliant SQL LIKE expression.
     */
    public static Builder<String> toSQL92(char escape) { return new SQL92Builder(escape); }
}
