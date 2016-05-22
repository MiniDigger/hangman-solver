package me.minidigger.hangmansolver;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import java.io.IOException;

/**
 * Created by Martin on 22.05.2016.
 */
public class HangmanSolverBot extends PircBot {

    // hangman bot messages
    private static final String correctLetter = "You guessed the correct letter ";
    private static final String incorrectGuesses = "Incorrect guesses: ";
    private static final String guessIncorrectLetter = "You have guessed the incorrect letter ";
    private static final String guessIncorrectWord = "You guessed the incorrect word ";

    private static final String guessCommand = ".g ";

    // the name of the hangman bot
    private final String hangmanBotName;
    private final String botOwner;

    private final HangmanSolver solver;
    private String incorrect = "";
    private String known = "";

    private boolean shouldAutoSolve = false;

    private HangmanSolverBot( String senderName, String botName, String botOwner ) {
        this.setName( botName );
        this.hangmanBotName = senderName;
        this.botOwner = botOwner;
        this.solver = new HangmanSolver();
        solver.start();
    }

    @Override
    public void onMessage( String channel, String sender, String login, String hostname, String message ) {
        if ( sender.equals( hangmanBotName ) ) {
            if ( message.startsWith( correctLetter ) ) {
                String letter = message.replace( correctLetter, "" );
                System.out.println( "Letter " + letter + " was correct" );
            } else if ( message.startsWith( incorrectGuesses ) ) {
                incorrect = message.replace( incorrectGuesses, "" );
                System.out.println( "Found incorrect guesses: " + incorrect );
            } else if ( message.startsWith( guessIncorrectLetter ) ) {
                String letter = message.replace( guessIncorrectLetter, "" );
                System.out.println( "Letter " + letter + " was incorrect" );
            } else if ( message.startsWith( "You win! Resetting board" ) ) {
                System.out.println( "won" );
                incorrect = "";
                known = "";
            } else if ( message.startsWith( guessIncorrectWord ) ) {
                String word = message.replace( guessIncorrectWord, "" );
                System.out.println( "Word " + word + " was incorrect" );
            } else {
                known = message.replace( " ", "" );
                if ( !shouldAutoSolve ) {
                    return;
                }

                System.out.println( "Start solving " + known );
                HangmanSolver.Solution solution = solver.solve( known, incorrect );
                if ( solution.possible.size() == 1 ) {
                    String word = solution.possible.get( 0 );
                    System.out.println( "solution can only be " + word );
                    sendMessage( channel, guessCommand + word );
                } else {
                    char c = solution.counts.keySet().iterator().next();
                    System.out.println( "guessing " + c );
                    sendMessage( channel, guessCommand + c );
                }
            }
        } else {
            if ( sender.equals( botOwner ) ) {
                switch ( message ) {
                    case ".toggleautosolve":
                        shouldAutoSolve = !shouldAutoSolve;
                        sendMessage( channel, "Autosolving " + ( shouldAutoSolve ? "enabled" : "disabled" ) );
                        break;
                }
            }

            switch ( message ) {
                case ".hint":
                    HangmanSolver.Solution solution = solver.solve( known, incorrect );
                    char c = solution.counts.keySet().iterator().next();
                    sendMessage( channel, "char " + c + " is worth a try" );
                    break;
            }
        }
    }

    public static void main( String[] args ) throws IOException, IrcException {
        String server = "irc.spi.gt";
        String channel = "#hangman";

        if ( args.length == 2 ) {
            server = args[0];
            channel = args[1];
        }

        HangmanSolverBot bot = new HangmanSolverBot( "Angstman", "HangManSolver", "MiniDigger" );
        bot.setVerbose( false );
        bot.connect( server );
        bot.setLogin( "HangmanSolver by MiniDigger - " );
        bot.joinChannel( channel );
    }
}