package com.revature.utils;

import java.util.List;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.exceptions.InvalidOptionException;

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
	private int optionsErrorPrompt() {
		logger.traceEntry("entry optionsErrorPrompt");
		System.out.println("Try again...");
		return logger.traceExit(promptThenReadOptions());
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
			return logger.traceExit(choice);
		} catch (InvalidOptionException IOE) {
			logger.catching(IOE);
			System.out.println(IOE);
		} catch (NoSuchElementException NSEE) {
			logger.catching(NSEE);
		} catch (IllegalStateException ISE) {
			logger.catching(ISE);
		} catch(Exception e) {
			logger.catching(e);
		} 
		return logger.traceExit(optionsErrorPrompt());
	}
	public int parseChoice(String choice) throws InvalidOptionException {
		//TODO make these custom exceptions so it can be JUnit tested
		logger.traceEntry("entry parseChoice Scanner input: {}", choice);
		if (choice.matches("\\s*")) {
			throw new InvalidOptionException("You didn't enter anything.");
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
			throw new InvalidOptionException("You entered too many different numbers.");
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
				throw new InvalidOptionException("You entered an invalid option.");
			}
		} else {
			String[] wordSS = choice.split("[^\\w+]");
			if (wordSS.length!=1) {
				throw new InvalidOptionException("You entered too many strings.");
			}
			for (int i = 0; i < validOptions.length; i++) {
				if (wordSS[0].toLowerCase().equals(validOptions[i].toLowerCase())) {
					result = i+1;
					return logger.traceExit(result);
				}
			}
			throw new InvalidOptionException("You entered an invalid option.");
		}
	}
	public String readUsernameOrPassword() {
		logger.traceEntry("entry readUsernameOrPassword");
		String password = null;
		try{
			password = validateUsernameOrPassword(JDBCBank.inStream.nextLine());
			return logger.traceExit(password);
		} catch (InvalidOptionException IOE) {
			logger.catching(IOE);
			System.out.println(IOE);
		} catch (NoSuchElementException NSEE) {
			logger.catching(NSEE);
		} catch (IllegalStateException ISE) {
			logger.catching(ISE);
		} catch(Exception e) {
			logger.catching(e);
		}
		return logger.traceExit(usernameOrPasswordErrorPrompt());
	}
	public String validateUsernameOrPassword(String usernameOrPassword) throws InvalidOptionException {
		//TODO make these custom exceptions so it can be JUnit tested
		logger.traceEntry("entry validateUsernameOrPassword");
		String invalidCharactersRegex = "[a-zA-Z\\d\\!\\(\\)\\-\\.\\?\\[\\]\\_\\`\\~\\;\\:\\@\\#\\$\\%\\^\\&\\*\\+\\=]+";
		if (usernameOrPassword.matches("\\s*")) {
			throw new InvalidOptionException("You can't enter an empty password.");
		} else if (!usernameOrPassword.matches(invalidCharactersRegex)) {
			throw new InvalidOptionException("System.out.println(\"You entered an invalid character.\r\n" + 
					"Valid characters include: 'a-z','A-Z','0-9','!','(',')','-','.',''?\r\n" + 
					"as well as: '[',']','_','`','~',';',':','@','#','$','^','&','*','+','='\r\n");
		} else {
			return logger.traceExit(usernameOrPassword);
		}
	}
	private String usernameOrPasswordErrorPrompt() {
		logger.traceEntry("entry usernameOrPasswordErrorPrompt");
		System.out.println("Try entering a valid username/password again...");
		return logger.traceExit(readUsernameOrPassword());
	}
	public int readPositiveInteger() {
		logger.traceEntry("Entry readPositiveInteger");
		int posInt = -1;
		try{
			posInt = validatePositiveInteger(JDBCBank.inStream.nextLine());
			return logger.traceExit(posInt);
		} catch (InvalidOptionException IOE) {
			logger.catching(IOE);
			System.out.println(IOE);
		} catch (NoSuchElementException NSEE) {
			logger.catching(NSEE);
		} catch (IllegalStateException ISE) {
			logger.catching(ISE);
		} catch(Exception e) {
			logger.catching(e);
		} 
		return logger.traceExit(positiveIntegerErrorPrompt());
	}
	public int validatePositiveInteger(String inputString) throws InvalidOptionException {
		logger.traceEntry("Entry validatePositiveInteger");
		String validString = "[0-9]+";
		if (inputString.matches("\\s*")) {
			throw new InvalidOptionException("You can't enter an empty string.");
		} else if (!inputString.matches(validString)) {
			throw new InvalidOptionException("Number must be of the format 123456789");
		} else if (inputString.length()> 20) {
			throw new InvalidOptionException("You need to input a number less than 20 digits long.");
		} else {
			if (inputString.length()==1) {
				int output = Integer.valueOf(inputString); 
				if (output==0) {
					throw new InvalidOptionException("You can't input 0.");
				}
				return logger.traceExit(output);
			} else {
				String invalidOrdering = "0+[0-9]*";
				if (inputString.matches(invalidOrdering)) {
					throw new InvalidOptionException("The number cannot begin with any leading zeros.");
				} else {
					int output = Integer.valueOf(inputString); 
					return logger.traceExit(output);
				}
			}
		}
	}
	private int positiveIntegerErrorPrompt() {
		logger.traceEntry("Entry positiveIntegerErrorPrompt");
		System.out.println("Try inputting a valid number again...");
		return logger.traceExit(readPositiveInteger());
	}
}
