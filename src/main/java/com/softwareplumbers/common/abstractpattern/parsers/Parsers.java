/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softwareplumbers.common.abstractpattern.parsers;

import com.softwareplumbers.common.abstractpattern.Pattern;
import java.util.ArrayList;

/** Functions that parse strings into Patterns.
 * 
 * Patterns may be expressed in different ways; regular expressions, and various forms of simpler
 * wildcard syntax. This class provides methods to parse different syntaxes into patterns.
 *
 * @author Jonathan Essex
 */
public class Parsers {
    
    private static final String[] UNIX_WILDCARD_OPERATORS = { "*", "?", "[" , "]" }; 
    private static final String[] SQL92_WILDCARD_OPERATORS = { "%", "_" }; 
    private static final char UNIX_WILDCARD_ESCAPE = '\\';
    
    private static Pattern parseUnixCharacterList(Tokenizer tokenizer) {
        StringBuilder characterList = new StringBuilder();
        while (tokenizer.hasNext() && !"]".contentEquals(tokenizer.current().data)) {
            characterList.append(tokenizer.next().data);
        }
        return Pattern.oneOf(characterList.toString());
    }
    
    private static Pattern parseUnixWildcard(Tokenizer tokenizer) {
        ArrayList<Pattern> patterns = new ArrayList<>();
        while (tokenizer.hasNext()) {
            switch (tokenizer.current().type) {
                case CHAR_SEQUENCE:
                    patterns.add(Pattern.of(tokenizer.next().data.toString()));
                    break;
                case OPERATOR:
                    CharSequence operator = tokenizer.next().data;
                    if ("*".contentEquals(operator)) 
                        patterns.add(Pattern.anyChar().atLeast(0));
                    if ("?".contentEquals(operator))
                        patterns.add(Pattern.anyChar());
                    if ("[".contentEquals(operator))
                        patterns.add(parseUnixCharacterList(tokenizer));
                    break;
            }
        }
        return Pattern.of(patterns.toArray(new Pattern[0]));
    }
    
    private static Pattern parseSQL92(Tokenizer tokenizer) {
        ArrayList<Pattern> patterns = new ArrayList<>();
        while (tokenizer.hasNext()) {
            switch (tokenizer.current().type) {
                case CHAR_SEQUENCE:
                    patterns.add(Pattern.of(tokenizer.next().data.toString()));
                    break;
                case OPERATOR:
                    CharSequence operator = tokenizer.next().data;
                    if ("%".contentEquals(operator)) 
                        patterns.add(Pattern.anyChar().atLeast(0));
                    if ("_".contentEquals(operator))
                        patterns.add(Pattern.anyChar());
                    break;
            }
        }
        return Pattern.of(patterns.toArray(new Pattern[0]));
    }
    
    public static Pattern parseUnixWildcard(String pattern) {
        return parseUnixWildcard(new Tokenizer(pattern, UNIX_WILDCARD_ESCAPE, UNIX_WILDCARD_OPERATORS));
    }
    
    public static Pattern parseSQL92(String pattern, char escape) {
        return parseSQL92(new Tokenizer(pattern, escape, SQL92_WILDCARD_OPERATORS));        
    }
    
}
