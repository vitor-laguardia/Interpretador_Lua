package lexical;

public class Lexeme {

	// public variables for easy access.
	public String token;
	public TokenType type;

	// Lexeme constructor 
	public Lexeme(String token, TokenType type) {
		this.token = token;
		this.type = type;
	}
}
