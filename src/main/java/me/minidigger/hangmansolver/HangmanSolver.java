package me.minidigger.hangmansolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Solves hangman puzzles
 */
class HangmanSolver {

    private WordList wordList;

    /**
     * loads the word list
     */
    void start() {
        wordList = new WordList();
        try {
            wordList.load();
        } catch ( IOException e ) {
            System.out.println( "Error while loading the wordList: " + e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    Solution solve( String known, String incorrect ) {
        Date startDate = new Date();
        List<String> words = wordList.getWords( known.length() );
        List<String> possible = new ArrayList<>();

        if ( words == null ) {
            System.out.println( "Empty word list?!" );
            return new Solution( new ArrayList<String>(), new HashMap<Character, Integer>() );
        }

        for ( String word : words ) {
            boolean add = true;
            for ( int i = 0; i < known.length(); i++ ) {
                char wordChar = word.charAt( i );
                char knownChar = known.charAt( i );

                if ( incorrect.contains( wordChar + "" ) ) {
                    add = false;
                    break;
                }

                if ( knownChar == '_' ) {
                    if ( known.contains( wordChar + "" ) ) {
                        add = false;
                        break;
                    } else {
                        continue;
                    }
                }

                if ( wordChar == knownChar ) {
                    add = true;
                } else {
                    add = false;
                    break;
                }
            }

            if ( add ) {
                possible.add( word );
            }
        }

        System.out.println( "Showing " + possible.size() + " possible solutions" );
        for ( String s : possible ) {
            System.out.print( s + " " );
        }
        System.out.println();

        Map<Character, Integer> counts = new HashMap<>();
        for ( String word : possible ) {
            for ( char c : word.toCharArray() ) {
                if ( known.contains( c + "" ) ) {
                    continue;
                }

                Integer old = counts.get( c );
                if ( old == null ) {
                    old = 0;
                }

                counts.put( c, old + 1 );
            }
        }
        Map<Character, Integer> sortedCounts = sortByValue( counts );

        System.out.println( "Found " + sortedCounts.size() + " possible chars" );
        for ( char c : sortedCounts.keySet() ) {
            System.out.print( c + "=" + sortedCounts.get( c ) + " " );
        }
        System.out.println();
        long ms = new Date().getTime() - startDate.getTime();
        System.out.println( "took " + ms + "ms" );

        return new Solution( possible, sortedCounts );
    }

    private <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
        List<Map.Entry<K, V>> list = new LinkedList<>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 ) {
                return ( o2.getValue() ).compareTo( o1.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<>();
        for ( Map.Entry<K, V> entry : list ) {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

    class Solution {

        List<String> possible;
        Map<Character, Integer> counts;

        Solution( List<String> possible, Map<Character, Integer> counts ) {
            this.possible = possible;
            this.counts = counts;
        }
    }
}
