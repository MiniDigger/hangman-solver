# hangman-solver

Small irc bot to solve hangman puzzels

# Building

hangman-solver uses maven, built it with 'mvn install'

# Running

The bot is preconfigured to play against the "Angstman" hangman bot at irc.spi.gt#hangman, but all messages are configureable here: 

https://github.com/MiniDigger/hangman-solver/blob/master/src/main/java/me/minidigger/hangmansolver/HangmanSolverBot.java#L14

Start the bot with 'java -jar hangman-solver-1.0-SNAPSHOT.jar < server> < channel> < owner> < botname>'. 

You can leave the arguments empty to join irc.spi.gt#hangman, have HangmanSolver as name and MiniDigger as owner.

# Commands

The bot has currently two commands:
- .toggleautosolve:
  - Toggels if the bot should automaticly try to solve the puzzle
  - Executeable only by the owner
- .hint:
  - Shows the char with the highest chance to be in the word
  - Executeable by everyone
