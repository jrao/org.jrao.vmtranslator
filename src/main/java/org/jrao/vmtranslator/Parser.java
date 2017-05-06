package org.jrao.vmtranslator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Parser {
	
	/*
	 * TODO: Avoid duplicating the String tokenizer code in commandType(), arg1, and arg2
	 */
	
	public enum COMMAND_TYPE {
		C_ARITHMETIC,
		C_PUSH,
		C_POP,
		C_LABEL,
		C_GOTO,
		C_IF,
		C_FUNCTION,
		C_RETURN,
		C_CALL,
		NOT_A_COMMAND
	}
	
	public Parser(File inputFile) {
		_lineIndex = -1;
		_lines = new ArrayList<String>();
		try (Scanner scanner = new Scanner(inputFile)) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.contains("//")) {
					line = line.substring(0, line.indexOf("//"));
				}
				line = line.trim();

				if (!line.equals("")) {
					_lines.add(line);
				}
			}
		}
		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
	}
	
	public boolean hasMoreCommands() {
		return _lineIndex < _lines.size() - 1;
	}
	
	public void advance() {
		_lineIndex++;
	}
	
	public COMMAND_TYPE commandType() {
		StringTokenizer tokenizer = new StringTokenizer(_lines.get(_lineIndex));
		int wordCount = tokenizer.countTokens();
		
		if (wordCount < 1) {
			return COMMAND_TYPE.NOT_A_COMMAND;
		}
		
		List<String> words = new ArrayList<String>(wordCount);
		while (tokenizer.hasMoreTokens()) {
			words.add(tokenizer.nextToken());
		}

		switch (words.get(0)) {
		case "add":
		case "sub":
		case "neg":
		case "eq":
		case "gt":
		case "lt":
		case "and":
		case "or":
		case "not":
			return COMMAND_TYPE.C_ARITHMETIC;
		case "push":
			return COMMAND_TYPE.C_PUSH;
		case "pop":
			return COMMAND_TYPE.C_POP;
		case "label":
			return COMMAND_TYPE.C_LABEL;
		case "goto":
			return COMMAND_TYPE.C_GOTO;
		case "if-goto":
			return COMMAND_TYPE.C_IF;
		case "function":
			return COMMAND_TYPE.C_FUNCTION;
		case "return":
			return COMMAND_TYPE.C_RETURN;
		case "call":
			return COMMAND_TYPE.C_CALL;
		default:
			return COMMAND_TYPE.NOT_A_COMMAND;
		}
	}
	
	public String arg0() {
		StringTokenizer tokenizer = new StringTokenizer(_lines.get(_lineIndex));
		int wordCount = tokenizer.countTokens();
		
		List<String> words = new ArrayList<String>(wordCount);
		while (tokenizer.hasMoreTokens()) {
			words.add(tokenizer.nextToken());
		}

		return words.get(0);
	}
	
	public String arg1() {
		StringTokenizer tokenizer = new StringTokenizer(_lines.get(_lineIndex));
		int wordCount = tokenizer.countTokens();
		
		List<String> words = new ArrayList<String>(wordCount);
		while (tokenizer.hasMoreTokens()) {
			words.add(tokenizer.nextToken());
		}

		return words.get(1);
	}
	
	public String arg2() {
		StringTokenizer tokenizer = new StringTokenizer(_lines.get(_lineIndex));
		int wordCount = tokenizer.countTokens();
		
		List<String> words = new ArrayList<String>(wordCount);
		while (tokenizer.hasMoreTokens()) {
			words.add(tokenizer.nextToken());
		}

		return words.get(2);
	}
	
	private int _lineIndex;
	private List<String> _lines;

}
