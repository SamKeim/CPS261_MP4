package indentationChecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

class BadIndentationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	BadIndentationException(String error) {
		super(error);
	}
}

public class IndentChecker {
	Stack<Integer> indentStack = new Stack<Integer>();

	private int findFirstNonBlank(String line) {
		// return index of first non-blank character
		// return -1 if the line doesn't contain a nonblank character
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) != ' ') {
				return i;
			}
		}
		return -1;
	}

	private void processLine(String line, int lineNumber) {
		System.out.printf("%3d:%s\n", lineNumber, line);
		int index = findFirstNonBlank(line);

		// Skip blank lines ... i.e. return immediately
		if(index == -1) return;
		// If the stack is empty, then push this index
		//onto the stack and return
		if(indentStack.size() == 0) {
			indentStack.push(index);
			return;
		}
		// If this index > than the top of the stack, then
		// push this index onto the stack and return
		if(index > indentStack.peek()) {
			indentStack.push(index);
		}
		
		// Pop off all Indentation indexes > index
		while(indentStack.peek() > index) {
			indentStack.pop();
		}
		
		// At this point the top of the stack should match
		// the current index. If it
		// doesn't throw a BadIndentationException. In
		// the error message, include the source Line Number
		if(indentStack.peek() != index) {
			throw new BadIndentationException("Error found on line: " + lineNumber);
		}
		
	}

	public void checkIndentation(String fileName) {
		// Clear the stack
		indentStack.clear();
		int lineCounter = 1;
		Scanner input = null;
		try {
			input = new Scanner(new File(fileName));
			// read through the file line by line
			// for each line, call processLine to check
			//indentation
			while(input.hasNext()) {
				processLine(input.nextLine(), lineCounter++);				
			}
			System.out.println(fileName + " appears to be properly indented.");
		} catch (BadIndentationException error) {
			System.out.println(error);
		} catch (FileNotFoundException e) {
			System.out.println("Can't open file:" + fileName);
		} finally {
			if (input != null)
				input.close();
		}
	}

	public static void main(String[] args) {
		IndentChecker indentChecker = new IndentChecker();

		for (int i = 0; i < args.length; i++) {
			System.out.println("Processing file: " + args[i]);
			indentChecker.checkIndentation(args[i]);
		}
	}
}