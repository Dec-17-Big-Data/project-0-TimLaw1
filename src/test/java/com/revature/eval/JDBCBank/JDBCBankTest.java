package com.revature.eval.JDBCBank;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.revature.exceptions.InvalidOptionException;
import com.revature.utils.ReadInput;

public class JDBCBankTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/*******************************************************************
	 * Test Read Input parseChoice
	 * @throws InvalidOptionException 
	 ******************************************************************/
	@Test
	public void testAnEmptyString() throws InvalidOptionException {
		String[] options = {"Hello","World"};
		ReadInput ri = new ReadInput(options);
		expectedException.expect(InvalidOptionException.class);
		ri.parseChoice("");
	}
	@Test
	public void testAValidString() throws InvalidOptionException {
		String[] options = {"Hello","World"};
		ReadInput ri = new ReadInput(options);
		assertEquals(1,ri.parseChoice("Hello"));
		assertEquals(2,ri.parseChoice("World"));
	}
	@Test
	public void testAnInvalidString() throws InvalidOptionException {
		String[] options = {"Hello","World"};
		ReadInput ri = new ReadInput(options);
		expectedException.expect(InvalidOptionException.class);
		ri.parseChoice("Foo");
		expectedException.expect(InvalidOptionException.class);
		ri.parseChoice("Bar");
	}
	@Test
	public void testAValidInt() throws InvalidOptionException {
		String[] options = {"Hello","World"};
		ReadInput ri = new ReadInput(options);
		assertEquals(1,ri.parseChoice("1"));
		assertEquals(2,ri.parseChoice("2"));
	}
	@Test
	public void testAnInvalidInt() throws InvalidOptionException {
		String[] options = {"Hello","World"};
		ReadInput ri = new ReadInput(options);
		expectedException.expect(InvalidOptionException.class);
		ri.parseChoice("3");
		expectedException.expect(InvalidOptionException.class);
		ri.parseChoice("299098");
	}
	@Test
	public void testMultipleValidInts() throws InvalidOptionException {
		String[] options = {"Hello","World"};
		ReadInput ri = new ReadInput(options);
		expectedException.expect(InvalidOptionException.class);
		ri.parseChoice("1 2");
		expectedException.expect(InvalidOptionException.class);
		ri.parseChoice("2 1");
	}
	@Test
	public void testMultipleValidStrings() throws InvalidOptionException {
		String[] options = {"Hello","World"};
		ReadInput ri = new ReadInput(options);
		expectedException.expect(InvalidOptionException.class);
		ri.parseChoice("Hello World");
		expectedException.expect(InvalidOptionException.class);
		ri.parseChoice("World Hello");
	}
	/*******************************************************************
	 * Test Read Input validateUsernameOrPassword
	 * @throws InvalidOptionException 
	 ******************************************************************/
	@Test
	public void testvalidateUsernameOrPasswordEmptyString() throws InvalidOptionException {
		String s = "";
		ReadInput ri = new ReadInput();
		expectedException.expect(InvalidOptionException.class);
		ri.validateUsernameOrPassword(s);
	}
	@Test
	public void testvalidateUsernameOrPasswordInvalidSpecialChars() throws InvalidOptionException {
		String s = ",";
		ReadInput ri = new ReadInput();
		expectedException.expect(InvalidOptionException.class);
		ri.validateUsernameOrPassword(s);
		s = "'";
		expectedException.expect(InvalidOptionException.class);
		ri.validateUsernameOrPassword(s);
		s = "\"";
		expectedException.expect(InvalidOptionException.class);
		ri.validateUsernameOrPassword(s);
		s = "/";
		expectedException.expect(InvalidOptionException.class);
		ri.validateUsernameOrPassword(s);
		s = ">";
		expectedException.expect(InvalidOptionException.class);
		ri.validateUsernameOrPassword(s);
		s = "<";
		expectedException.expect(InvalidOptionException.class);
		ri.validateUsernameOrPassword(s);
		s = "{";
		expectedException.expect(InvalidOptionException.class);
		ri.validateUsernameOrPassword(s);
		s = "}";
		expectedException.expect(InvalidOptionException.class);
		ri.validateUsernameOrPassword(s);
		s = "\\";
		expectedException.expect(InvalidOptionException.class);
		ri.validateUsernameOrPassword(s);
		s = "|";
		expectedException.expect(InvalidOptionException.class);
		ri.validateUsernameOrPassword(s);	
	}
	@Test
	public void testvalidateUsernameOrPasswordAllButSpecialChars() throws InvalidOptionException {
		String username = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!()-.?[]_`~;:@#$%^&*+=";
		ReadInput ri = new ReadInput();
		assertEquals(username,ri.validateUsernameOrPassword(username));
	}
	/*******************************************************************
	 * Test Read Input validatePositiveInteger
	 * @throws InvalidOptionException 
	 ******************************************************************/
	@Test
	public void testValidatePositiveIntegerEmptyString() throws InvalidOptionException {
		String s = "";
		ReadInput ri = new ReadInput();
		expectedException.expect(InvalidOptionException.class);
		ri.validatePositiveInteger(s);
	}
	@Test
	public void testValidatePositiveIntegerInvalidChars() throws InvalidOptionException {
		String s = "120,398,000";
		ReadInput ri = new ReadInput();
		expectedException.expect(InvalidOptionException.class);
		ri.validatePositiveInteger(s);
		s = "120_398_000";
		expectedException.expect(InvalidOptionException.class);
		ri.validatePositiveInteger(s);
		s = "3*10^3";
		expectedException.expect(InvalidOptionException.class);
		ri.validatePositiveInteger(s);
		s = "12345six";
		expectedException.expect(InvalidOptionException.class);
		ri.validatePositiveInteger(s);
		s = " 23 	";
		expectedException.expect(InvalidOptionException.class);
		ri.validatePositiveInteger(s);
	}
	@Test
	public void testValidatePositiveIntegerValidChars() throws InvalidOptionException {
		String s = "120398000";
		ReadInput ri = new ReadInput();
		assertEquals(120398000,ri.validatePositiveInteger(s));
	}
	@Test
	public void testValidatePositiveIntegerLeadingZeros() throws InvalidOptionException {
		String s = "000120398000";
		ReadInput ri = new ReadInput();
		expectedException.expect(InvalidOptionException.class);
		ri.validatePositiveInteger(s);
	}
	@Test
	public void testValidatePositiveIntegerZero() throws InvalidOptionException {
		String s = "0";
		ReadInput ri = new ReadInput();
		expectedException.expect(InvalidOptionException.class);
		ri.validatePositiveInteger(s);
	}
	@Test
	public void testValidatePositiveIntegerValidOver20Long() throws InvalidOptionException {
		String s = "912835015931257329870392470391248710239847890";
		ReadInput ri = new ReadInput();
		expectedException.expect(InvalidOptionException.class);
		ri.validatePositiveInteger(s);
	}
	@Test
	public void testValidatePositiveIntegerOne() throws InvalidOptionException {
		String s = "1";
		ReadInput ri = new ReadInput();
		assertEquals(1,ri.validatePositiveInteger(s));
	}
}
