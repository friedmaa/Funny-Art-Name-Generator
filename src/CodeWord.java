
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CodeWord {
	public static final int WORDLENGTH = 5;
	public static final int DICTIONARYSIZE = 8548;
	// Holds all words
	public static String[] dictionary = new String[DICTIONARYSIZE];
	//all entries are in play at first
	//chooses 1 word at start of game
	//read in user input
	//have input checking to give an error message if bad input
	//find in place
	//find out of place
	//no more global variables!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	//Indicates whether words can logically be ruled out
	public static boolean[] inPlay = new boolean[DICTIONARYSIZE];
	//should be all true at begining

	public static void main(String[] args) throws FileNotFoundException{
		readDictionary(); // reads in dictionary
		Random r = new Random();
		String codeWord = dictionary[r.nextInt(DICTIONARYSIZE)].toString();
		Scanner console = new Scanner(System.in);
		//System.out.println(codeWord);

		while (yesTo("Do you want to play Code Word?", console)){
			for(int i = 0; i < DICTIONARYSIZE; i++){//sets all words in the dictionary to in play
				inPlay[i] = true;
			}
			int numGuesses = howManyGuesses();
			int guessesUntilWin = 0;
			boolean win = false;
			String[] tracking = new String[numGuesses];
			for(int i = numGuesses; i >= 1; i--){//Schrum said this loop needed to be in the main method
				System.out.println(i + ": Guess a 5-letter word (enter 'r' for remaining words):");
				Scanner input = new Scanner(System.in);
				if(!input.hasNext()){//if it is not a string
					System.out.println("Please enter a 5-letter word");
					i++;//makes sure the user does not lose a guess

				}else if(input.hasNext()){//if there is a string
					String userGuess = input.next();
					if(userGuess.equals("r")){
						printDictionary();
						i++;
					}else if(userGuess.length() != WORDLENGTH || userGuess.equals("r")){//if the string is the wrong size
						System.out.println("Please enter a 5-letter word");
						i++;
					}else if(userGuess.equals(codeWord)){
						guessesUntilWin++;
						win = true;
						System.out.println("You won in " + guessesUntilWin + " guesses! The word was: " + codeWord);
						break;
					}else{
						String feedbackPrint = feedback(userGuess, codeWord);
						tracking[i-1] = userGuess + " :" + feedbackPrint;
						guessesUntilWin++;
						trackingPrint(tracking, i);
						removeWordsWithDifferentFeedback(userGuess, feedbackPrint);
					}
				}
			}
			if(win == false){
				System.out.println("You lost. The word was: " + codeWord);
			}

			// play the game...
			// Since there is nothing in here yet, it will repeatedly
			// prompt Do you want to play Code Word?
			// An entire game should happen here, but
			// write separate methods whenever appropriate
			// for a task.

		}
	}

// more methods here -- a few headers have been provided
// you will need to fill in some of them 
	// and add more
	
	/**
	 * reads the dictionary from the file into the dictionary array
	 * @throws FileNotFoundException 
	 */
	public static void readDictionary() //Alex Rollins helped to work out how to do random
			throws FileNotFoundException{
		Scanner wordList = new Scanner(new File("wordList.txt"));
		for(int i = 0; i < DICTIONARYSIZE; i++){
			dictionary[i] = wordList.next();
		}
		
	}
	/**
	 * Compare the feedback from comparing a guess to the codeword to the feedback from
	 * comparing the guess to each dictionary word, and filter out all dictionary words
	 * with differing feedback.
	 *
	 * @param guess A user guess from the console
	 * @param feedback String of *'s and +'s resulting from comparing the guess to the codeword
	 * @throws FileNotFoundException 
	 */
	public static void removeWordsWithDifferentFeedback(String guess, String feedback) 
			throws FileNotFoundException {
		String[] dictionaryFeedback = new String[DICTIONARYSIZE];
		for(int i = 0; i < DICTIONARYSIZE; i++){
			dictionaryFeedback[i] =	feedback(guess, dictionary[i]);
		}
		for(int i = 0; i < DICTIONARYSIZE; i++){
			if(!dictionaryFeedback[i].equals(feedback)){
			inPlay[i] = false;
			}
		}
			
	}
	
	public static void printDictionary(){
		for(int i = 0; i < DICTIONARYSIZE; i++){
			if(inPlay[i] == true){
				System.out.print(dictionary[i] + " ");				
			}
		}
	}
	
	public static void trackingPrint(String[] a, int x){
		for(int i= a.length-1; i >= x -1; i--){
			System.out.print(a[i] + "\n");
		}
	}

	/**
	 * This method takes two 5-letter words and returns a String
	 * of asterisks containing the feedback that the user would get
	 * for comparing word1 to word2. Recall that the number of asterisks (*'s)
	 * equals the number of in-place matches, and the number of plus signs (+'s)
	 * equals the number of out-of-place matches. All asterisks should
	 * precede all plus signs.
	 *
	 * @param word1 5-letter word, e.g. a user guess
	 * @param word2 Another 5-letter word, e.g. the secret code word
	 * @return Feedback of asterisks and plus signs indicating the number
	 *         of in- and out-of-place matches.
	 */
	public static String feedback(String word1, String word2) {
		char[] userInput = new char[WORDLENGTH];
		for(int i = 0; i < userInput.length; i++){
			userInput[i] = word1.charAt(i);
		}
		char[] codeWord = new char[WORDLENGTH];
		for(int i = 0; i < codeWord.length; i++){
			codeWord[i] = word2.charAt(i);
		}
		boolean[] userInputMatched = new boolean[WORDLENGTH];
		boolean[] codeWordMatched = new boolean[WORDLENGTH];
		//look for in place matches
		String feedbackReturn= "";
		//feedbackReturn += word1 + ": ";
		for(int i = 0; i < WORDLENGTH; i++){//length of word will always be 5
			if(codeWord[i] == userInput[i]){
				feedbackReturn += "*";
				userInputMatched[i] = true;
				codeWordMatched[i] = true;
			}
		}
		//look for out of place matches
		//boolean[] matched for each word init to false
		//once you find an in place match mark position as used
		//mark individual cells as true

		for(int i =0; i < WORDLENGTH; i++){
			for(int j = 0; j < WORDLENGTH; j++){
				if(i != j && codeWord[i] == userInput[j] && userInputMatched[i]!= true && codeWordMatched[i] != true){//Eric Rodriguez helped us w/ need for &&
					feedbackReturn += "+";
				}
			}
		}
		
		return feedbackReturn; //TODO
	}
	
	/**
	 * Utility function to ask user yes or no.
     * No modifications are necessary for this method.
     * It uses a forever loop -- but the loop stops when something is returned.
	 * @param prompt Text presented to user
	 * @param console Source of user input
	 * @return Whether user types 'y' (as opposed to 'n').
	 */
	public static boolean yesTo(String prompt, Scanner console) {
		for (;;) {
			System.out.print(prompt + " (y/n)? ");
			String response = console.next().trim().toLowerCase();
			if (response.equals("y")){
				return true;
			}else if (response.equals("n")){
				return false;
			}else
				System.out.println("Please answer y or n.");
		}
	}
	
	public static int howManyGuesses(){
		Scanner guessesChoice = new Scanner(System.in);
		System.out.println("How many guesses do you want?");
		int numberOfGuesses = 0;
		if(!guessesChoice.hasNextInt()){
			System.out.println("Please enter an integer number");
			if(guessesChoice.nextInt() <= 0){//Dr. Schrum explained the need for a nested if statement
				System.out.println("Please enter a positive integer number");
				return howManyGuesses();
			}
		}else
			numberOfGuesses = guessesChoice.nextInt();
			return numberOfGuesses;
	}

}