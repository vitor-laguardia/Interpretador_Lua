package lexical;

public enum TokenType {
	// Keywords
	IF,						// if
	WHILE,		  	// while
	REPEAT,     	// repeat
	FOR,        	// for
	PRINT,      	// print
	THEN,       	// then
	ELSE_IF,    	// else if
	ELSE,      	  // else
	UNTIL,     	 	// until
	IN,        		// in
	DO,        		// do
	END,       		// end

	// Logic operators
	EQUAL,         // ==
	NOT_EQUAL,     // ~=
	LOWER,         // <
	LOWER_EQUAL,   // <=
	GREATER,       // >
	GREATER_EQUAL, // >=
	AND,           // and
	OR,            // or
	TRUE,          // true
	FALSE,         // false

	// Arithmetic operators
	ADD,           // +
	SUB,           // -
	MUL,           // *
	DIV,           // /
	MOD,					 // %

	// Symbols
	SEMICOLON,         // ;
	DOT,               // .
	COMMA,             // ,
	OPEN_PARENTHESES,	 // (
	CLOSE_PARENTHESES, // )
	OPEN_BRACKET,      // [
	CLOSE_BRACKET,     // ]
	OPEN_KEYS,         // {
	CLOSE_KEYS,        // }
	ASSIGN,            // =
	HASHTAG,           // #
	QUOTATION_MARKS,   // "" || ''
	COMMENTARY,       // --
	CONCAT,				// ..
	

	// Functions
	READ,             // read
	TO_STRING,	      // converter for string (number, double ..)
	TO_NUMBER,        // converter string for number

	// Others 
	NUMBER,           // type numeric
	VAR,              // string
	NIL,              // null
}
