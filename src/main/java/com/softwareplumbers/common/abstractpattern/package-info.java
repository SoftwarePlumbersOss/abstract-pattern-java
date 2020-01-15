/**
 * Convert between pattern matching formats.
 *
 * Abstract Pattern is an abstraction of a (standard-ish) regular expression format.
 * As well as being able to match strings in a trivial way, Pattern provides utilities
 * for parsing other types of patterns (such as a Unix wildcard string that might be 
 * used to match filenames) into standard Pattern objects, and outputting patterns
 * in different formats.
 * 
 * ## Status
 * 
 * Abstract Pattern is under development. The eventual intention is for the Pattern
 * interface itself to support parsing standard regular expressions. 
 * 
 * ## Examples
 * 
 * To convert an pattern expression from unix format to a java regular expression:
 * 
 * ```java
 * Pattern pattern = Parsers.parseUnixWildcard("a?bc/*").build(Builders.toPattern());
 * ```
 * 
 * To convert an pattern expression from unix format to SQL-92 format:
 * 
 * ```java
 * String pattern = Parsers.parseUnixWildcard("a?bc/*").build(Builders.toSQL92('\\'));
 * ```
 */
package com.softwareplumbers.common.abstractpattern;
