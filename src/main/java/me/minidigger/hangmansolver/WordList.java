package me.minidigger.hangmansolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A list with all possible words, sorted by length
 */
class WordList {

    private static final String wordListUrl = "http://sockspls.com/wordlist.txt";
    private Map<Integer, List<String>> words;

    /**
     * Loads the list and sorts it by length
     *
     * @throws IOException
     */
    void load() throws IOException {
        Date startDate = new Date();
        words = new HashMap<>();

        URL url = new URL( wordListUrl );
        URLConnection conn = url.openConnection();

        BufferedReader br = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );

        String inputLine;
        while ( ( inputLine = br.readLine() ) != null ) {
            List<String> list = words.get( inputLine.length() );
            if ( list == null ) {
                list = new ArrayList<>();
            }
            list.add( inputLine );
            words.put( inputLine.length(), list );
        }
        br.close();

        int total = 0;
        for ( int i : words.keySet() ) {
            List<String> list = words.get( i );
            System.out.println( "Found " + list.size() + " words with length " + i );
            total += list.size();
        }

        long ms = new Date().getTime() - startDate.getTime();
        System.out.println( "loaded " + total + " words in " + ms + "ms" );
    }

    /**
     * Returns all words with the length size
     *
     * @param size the desired size
     * @return a list with all words with that size
     */
    List<String> getWords( int size ) {
        return words.get( size );
    }
}
