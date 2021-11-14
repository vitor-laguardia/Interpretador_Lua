package lexical;

public enum TokenType {
	// Keywords
	IF, 		
	WHILE, 
	REPEAT,
	FOR,
	PRINT,
	ASSIGN,
	THEN,
	ELSE_IF,
	ELSE,
	UNTIL,
	IN,
	DO,
	END,

	//Logic operators
	EQUAL,
	NOT_EQUAL,
	LOWER,
	LOWER_EQUAL,
	GREATER,
	GREATER_EQUAL,
	AND,
	OR,
	TRUE,
	FALSE,

	// Arithmetic operators
	ADD,           // +
	SUB,           // -
	MUL,           // *
	DIV,           // /
	MOD,

	// Symbols
	SEMICOLON, 
	DOT, 
	COMMA,
	OPEN_PARENTHESES,	
	CLOSE_PARENTHESES,	
	OPEN_BRACKET,
	CLOSE_BRACKET,
	OPEN_KEYS,
	CLOSE_KEYS,
	ASSIGN,
	HASHTAG,
	QUOTATION_MARKS,

	// Functions
	READ,
	TO_STRING,	
	TO_NUMBER,

	// Others
	NUMBER,
	VAR,
	NIL,
}
