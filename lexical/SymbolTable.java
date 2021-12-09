package lexical;

import java.util.Map;
import java.util.HashMap;

public class SymbolTable {

	private Map<String, TokenType> symbolTable;

	public SymbolTable() {
		symbolTable = new HashMap<String, TokenType>();

		// Keywords
		symbolTable.put("if", TokenType.IF);	
		symbolTable.put("while", TokenType.WHILE);	
		symbolTable.put("repeat", TokenType.REPEAT);	
		symbolTable.put("for", TokenType.FOR);	
		symbolTable.put("print", TokenType.PRINT);	
		symbolTable.put("then", TokenType.THEN);	
		symbolTable.put("elseif", TokenType.ELSE_IF);	
		symbolTable.put("else", TokenType.ELSE);	
		symbolTable.put("until", TokenType.UNTIL);	
		symbolTable.put("in", TokenType.IN);	
		symbolTable.put("do", TokenType.DO);	
		symbolTable.put("end", TokenType.END);	
		symbolTable.put("not", TokenType.NOT);

		// Logic operators
		symbolTable.put("==", TokenType.EQUAL);	
		symbolTable.put("~=", TokenType.NOT_EQUAL);	
		symbolTable.put("<", TokenType.LOWER);	
		symbolTable.put("<=", TokenType.LOWER_EQUAL);	
		symbolTable.put(">", TokenType.GREATER);	
		symbolTable.put(">=", TokenType.GREATER_EQUAL);	
		symbolTable.put("and", TokenType.AND);	
		symbolTable.put("or", TokenType.OR);	
		symbolTable.put("true", TokenType.TRUE);	
		symbolTable.put("false", TokenType.FALSE);
		
		// Arithmetic operators
		symbolTable.put("+", TokenType.ADD);	
		symbolTable.put("-", TokenType.SUB);	
		symbolTable.put("*", TokenType.MUL);	
		symbolTable.put("/", TokenType.DIV);	
		symbolTable.put("%", TokenType.MOD);
		
		// Symbols
		symbolTable.put(";", TokenType.SEMICOLON);	
		symbolTable.put(".", TokenType.DOT);	
		symbolTable.put(",", TokenType.COMMA);	
		symbolTable.put("(", TokenType.OPEN_PARENTHESES);	
		symbolTable.put(")", TokenType.CLOSE_PARENTHESES);	
		symbolTable.put("[", TokenType.OPEN_BRACKET);	
		symbolTable.put("]", TokenType.CLOSE_BRACKET);	
		symbolTable.put("{", TokenType.OPEN_KEYS);	
		symbolTable.put("}", TokenType.CLOSE_KEYS);	
		symbolTable.put("=", TokenType.ASSIGN);	
		symbolTable.put("#", TokenType.HASHTAG);	
		symbolTable.put("\"", TokenType.QUOTATION_MARKS);	
		symbolTable.put("--", TokenType.COMMENTARY);
		symbolTable.put("..", TokenType.CONCAT);

		// Functions
		symbolTable.put("read", TokenType.READ);	
		symbolTable.put("tostring", TokenType.TO_STRING);	
		symbolTable.put("tonumber", TokenType.TO_NUMBER);	

		// Others
		symbolTable.put("nil", TokenType.NIL);		
	}

	public boolean contains(String token) {
		return symbolTable.containsKey(token);
	}

	public TokenType find(String token) {
		return this.contains(token) ?
					symbolTable.get(token) : TokenType.VAR;
	}
}