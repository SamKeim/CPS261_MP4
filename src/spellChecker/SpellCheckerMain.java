package spellChecker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class SpellCheckerMain {

	// We could have also used TreeSet for the dictionary
	private HashSet<String> dictionary = new HashSet<String>();
	private TreeSet<String> misspelledWords = new TreeSet<String>();
	private static Scanner keyboard = new Scanner(System.in);

	public SpellCheckerMain() throws FileNotFoundException {
		// Add all of the words from "dictionary.txt" to the dictionary HashSet
		Scanner dictionaryScanner = null;
		try {
			dictionaryScanner = new Scanner(new FileInputStream("Dictionary.txt"));
			while(dictionaryScanner.hasNext()) {
				dictionary.add(dictionaryScanner.next());
			}
		} catch (FileNotFoundException fnfe) {
			System.out.println(fnfe);
		} catch (Exception e) {
			// just in case something goes screwy with the hash set
			System.out.println(e);
		} finally {
			if(dictionaryScanner != null) dictionaryScanner.close();
		}
	}

	public void checkSpelling(String fileName) throws FileNotFoundException {
		
		// declare variables
		String word, line;
		boolean flag = false;
		int lineCounter = 0;
		ArrayList<String> misspelledOnThisLine = new ArrayList<>();
		System.out.println("======== Spell checking " + fileName + " =========");
		// Clear miss_spelled_words
		misspelledWords.clear();
		
		Scanner fileScanner = new Scanner(new FileInputStream(fileName));
		while(fileScanner.hasNextLine()) {
			flag = false;
			// Read in each line from "fileName"
			line = fileScanner.nextLine();		
			lineCounter++;
			// For each line, break the line into words using the
			//following StringTokenizer
			StringTokenizer st = new StringTokenizer(line, " \t,.;:-%'\"");
			while(st.hasMoreTokens()) {		
				// lower case each word obtained from the StringTokenizer
				// skip word if the first character is not a letter
				word = st.nextToken().toLowerCase();
				if(!Character.isLetter(word.charAt(0))) continue;
				// Skip over word if it can be found in either
				//dictionary, or miss_spelled_words
				if(dictionary.contains(word) || misspelledWords.contains(word) || misspelledOnThisLine.contains(word)) continue;
				// If word ends with 's', then try the singular version
				//of the word in the dictionary and miss_spelled_words ... skip if
				//found
				if(word.charAt(word.length() - 1) == 's') {
					String wordMinusS = word.substring(0, word.length() - 1);
					if(dictionary.contains(wordMinusS) || misspelledWords.contains(wordMinusS) || misspelledOnThisLine.contains(wordMinusS)) continue;
				}
				misspelledOnThisLine.add(word);
				flag = true;
			}
			
			if(flag) {
				System.out.println(lineCounter + ": " + line);
				for(String misspelled : misspelledOnThisLine) {
					// Print line containing miss-spelled word(make sure you
					//only print it once if multiple miss-spelled words are found on
					//this line)
					// Ask the user if he wants the word added to the
					//dictionary or the miss-spelled words and add word as specified
					//by the user
					boolean addToDictionary = askToAdd(misspelled);
					
					if(addToDictionary) {
						dictionary.add(misspelled);
					} else {
						misspelledWords.add(misspelled);
					}
				}
			}
			misspelledOnThisLine.clear();
		}
		fileScanner.close();

	}
	
	public boolean askToAdd(String word) {
		char choice = 'N';
		boolean isValid = false;
		while (!isValid) {
			System.out.printf("%s is not in the dictionary. Add to Dictionary? (Y/N)\n", word);
			choice = keyboard.nextLine().toUpperCase().charAt(0);
			if(choice == 'Y' || choice == 'N') {
				isValid = true;
			} else {
				isValid = false;
				System.out.println("Please enter a valid choice.");
			}
		}
		return choice == 'Y';
	}
	public void dump_miss_spelled_words(String fileName) {
		// Print out the miss-spelled words
		System.out.println("======== Mis-Spelled Words in " + fileName + " =========");
		for(String word : misspelledWords) {
			System.out.println(word);
		}
	}

	public static void main(String[] args) {

		try {
			SpellCheckerMain spellCheck = new SpellCheckerMain();
			for (int i = 0; i < args.length; i++) {
				spellCheck.checkSpelling(args[i]);
				spellCheck.dump_miss_spelled_words(args[i]);
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		System.out.println("======== Spell Check Complete =========");
	}
}