package indentationChecker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

class IndentationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
}

public class IndentationChecker {
	Stack<String> parens;

//	public static void main(String[] args) {
//		IndentationChecker ic;
//		for(String arg : args) {
//			ic = new IndentationChecker(arg);
//		}
//	}

	public IndentationChecker(String fileName) {
		System.out.println("============= checking " + fileName);
		Scanner fileScanner = null;
		int lineCounter = 0;
		parens = new Stack<>();
		try {
			fileScanner = new Scanner(new FileInputStream(fileName));
			while (fileScanner.hasNextLine()) {
				lineCounter++;
				String line = fileScanner.nextLine();
				int index = countSpaces(line);
				int tabs = index / 4;

				// check for -1 cases ie. skip blank line
				if (index == -1)
					continue;

				if (tabs != parens.size()) {
					throw new IndentationException();
				}
				// debugging
				System.out.println("Index: " + index);
				System.out.println("Tabs: " + tabs);
				System.out.println("Line: " + lineCounter);
				System.out.println("First non-whitespace char: " + line.substring(index, index + 1));

				if (line.substring(index, index + 1).matches("[\\{\\[\\(]")) {
					parens.push(line.substring(index, index + 1));
					// debug
					System.out.println("Found a paren! " + parens.size() + " parens.");
				} else if (line.substring(index, index + 1).matches("[\\)\\]\\}]")) {
					System.out.println("Found a paren!");
					System.out.println("Take a peek: " + parens.peek());
					switch (line.substring(index, index + 1)) {
					case "}":
						if(parens.peek().equals("{"))   System.out.println("Popped " + parens.pop() + ". " + parens.size() + " parens left.");
						break;
					case "]":
						if(parens.peek().equals("[")) System.out.println("Popped " + parens.pop() + ". " + parens.size() + " parens left.");
						break;
					case ")":
						if(parens.peek().equals("(")) System.out.println("Popped " + parens.pop() + ". " + parens.size() + " parens left.");
						break;
					default:
						System.out.println("How'd you get here?");
					}

				}

				System.out.println();
			}
		} catch (FileNotFoundException fnfe) {
			System.out.println(fnfe);
		} finally {
			if (fileScanner != null)
				fileScanner.close();
		}

	}

	public int countSpaces(String line) {
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) != ' ') {
				return i;
			}
		}
		return -1;
	}

}
