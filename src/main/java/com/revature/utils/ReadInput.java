package com.revature.utils;

import java.util.List;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReadInput {
	private static final Logger logger = LogManager.getLogger(ReadInput.class);
	private String[] validOptions;
	public ReadInput() {}
	public ReadInput(String[] options) { this.validOptions = options; }
	public ReadInput(List<String> validIDs) {
		this.validOptions = new String[validIDs.size()];
		for (int i = 0; i < validOptions.length; i++) {
			this.validOptions[i] = validIDs.get(i);
		}
	}
	private int errorPrompt(int error) {
		logger.traceEntry("Params: {}", error);
		if (error==0) {
			System.out.println("You didn't enter a valid option, try again...");	
		} else if (error==1) {
			System.out.println("You didn't enter anything, try again...");
		} else if (error==2) {
			System.out.println("You entered too many numbers, try again...");
		} else if (error==3) {
			System.out.println("You entered too many strings, try again...");
		} else {
			System.out.println("Unknown Exception, try again...");	
		}
		for (int i = 0; i<validOptions.length; i++) {
			System.out.println(String.valueOf(i+1)+"."+ validOptions[i]);
		}
		System.out.println("Please type in the name or number of the choice and hit enter.");	
		return logger.traceExit(readOptions());
	}
	public int promptThenReadOptions() {
		logger.traceEntry();
		for (int i = 0; i<validOptions.length; i++) {
			System.out.println(String.valueOf(i+1)+"."+ validOptions[i]);
		}
		System.out.println("Please type in the name or number of the choice and hit enter.");	
		return logger.traceExit(readOptions());
	}
	public int readOptions() {
		logger.traceEntry();
		int choice = -1;
		try{
			choice = parseChoice(JDBCBank.inStream.nextLine());	
		} catch (NoSuchElementException NSEE) {
			logger.catching(NSEE);
		} catch (IllegalStateException ISE) {
			logger.catching(ISE);
		} catch(Exception e) {
			logger.catching(e);
		} 
		return logger.traceExit(choice);
	}
	private int parseChoice(String choice) {
		//TODO make these custom exceptions so it can be JUnit tested
		logger.traceEntry("Scanner input: {}", choice);
		if (choice.matches("\\s*")) {
			return logger.traceExit(errorPrompt(1));
		}
		String[] digitSS = choice.split("[^\\d+]");
		int digitCount = 0;
		for (String i: digitSS) {
			if (i.equals("")) {
				continue;
			} else {
				digitCount++;
			}
		}
		int result = 0;
		if (digitCount>1) {
			for (String i: digitSS) {
				System.out.println(i);
			}
			return logger.traceExit(errorPrompt(2));	
		} else if (digitCount==1) {
			for (String i: digitSS) {
				if (i.equals("")) {
					continue;
				} else {
					result = Integer.valueOf(i);
					break;
				}
			}
			if (result > 0 && result <= validOptions.length) {
				return logger.traceExit(result);
			} else {
				return logger.traceExit(errorPrompt(0));	
			}
		} else {
			String[] wordSS = choice.split("[^\\w+]");
			if (wordSS.length!=1) {
				return logger.traceExit(errorPrompt(3));
			}
			for (int i = 0; i < validOptions.length; i++) {
				if (wordSS[0].toLowerCase().equals(validOptions[i].toLowerCase())) {
					result = i+1;
					return logger.traceExit(result);
				}
			}
			return logger.traceExit(errorPrompt(0));
		}
	}
	public String readUsernameOrPassword() {
		logger.traceEntry("entry readUsernameOrPassword");
		String password = null;
		try{
			password = validateUsernameOrPassword(JDBCBank.inStream.nextLine());
		} catch (NoSuchElementException NSEE) {
			logger.catching(NSEE);
		} catch (IllegalStateException ISE) {
			logger.catching(ISE);
		} catch(Exception e) {
			logger.catching(e);
		} 
		return logger.traceExit(password);
	}
	private String validateUsernameOrPassword(String usernameOrPassword) {
		//TODO make these custom exceptions so it can be JUnit tested
		logger.traceEntry("entry validateUsernameOrPassword");
		String invalidCharactersRegex = "[^[[a-z][A-Z]\\d\\!\\(\\)\\-\\.\\?\\[\\]\\_\\`\\~\\;\\:\\@\\#\\$\\%\\^\\&\\*\\+\\=]]";
		if (usernameOrPassword.matches("\\s*")) {
			return logger.traceExit(usernameOrPasswordErrorPrompt(0));
		} else if (usernameOrPassword.matches(invalidCharactersRegex)) {
			return logger.traceExit(usernameOrPasswordErrorPrompt(1));
		} else {
			return logger.traceExit(usernameOrPassword);
		}
	}
	private String usernameOrPasswordErrorPrompt(Integer error) {
		logger.traceEntry("entry usernameOrPasswordErrorPrompt");
		if (error==0) {
			System.out.println("You can't enter an empty password.");
		} else if (error==1) {
			System.out.println("You entered an invalid character.");
			System.out.println("Valid characters include: 'a-z','A-Z','0-9','!','(',')','-','.',''?");
			System.out.println("as well as: '[',']','_','`','~',';',':','@','#','$','^','&','*','+','='");
		}
		System.out.println("Try entering a valid password again...");
		return logger.traceExit(readUsernameOrPassword());
	}
	public int readPositiveInteger() {
		logger.traceEntry("Entry readPositiveInteger");
		int posInt = -1;
		try{
			posInt = validatePositiveInteger(JDBCBank.inStream.nextLine());
		} catch (NoSuchElementException NSEE) {
			logger.catching(NSEE);
		} catch (IllegalStateException ISE) {
			logger.catching(ISE);
		} catch(Exception e) {
			logger.catching(e);
		} 
		return logger.traceExit(posInt);
	}
	private int validatePositiveInteger(String inputString) {
		logger.traceEntry("Entry validatePositiveInteger");
		String invalidCharacters = "[^0-9]";
		if (inputString.matches("\\s*")) {
			//TODO make these custom exceptions so it can be JUnit tested
			return logger.traceExit(positiveIntegerErrorPrompt(0));
		} else if (inputString.matches(invalidCharacters)) {
			return logger.traceExit(positiveIntegerErrorPrompt(1));
		} else if (inputString.length()> 20) {
			return logger.traceExit(positiveIntegerErrorPrompt(2));
		} else {
			int output = Integer.valueOf(inputString); 
			return logger.traceExit(output);
		}
	}
	private int positiveIntegerErrorPrompt(int error) {
		logger.traceEntry("Entry positiveIntegerErrorPrompt");
		if (error==0) {
			System.out.println("You can't input an empty number.");
		} else if (error==1) {
			System.out.println("You need to input only digit values and a positive number.");
		} else if (error==2) {
			System.out.println("You need to input a number less than 20 digits long.");
		}
		return logger.traceExit(readPositiveInteger());
	}
}
