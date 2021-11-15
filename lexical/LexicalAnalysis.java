package lexical;

import java.io.FileInputStream;
import java.io.PushbackInputStream;

public class LexicalAnalysis implements AutoCloseable {

  private int line;
  private SymbolTable symbolTable;
  private PushbackInputStream input;

  public LexicalAnalysis(String fileName) {
    try {
      input = new PushbackInputStream(new FileInputStream(fileName));
    } catch (Exception e) {
      throw new LexicalException("Unable to open file: " + fileName);
    }

    symbolTable = new SymbolTable();
    line = 1;
  }

  public void close() {
    try {
      input.close();
    } catch (Exception e) {
      throw new LexicalException("Unable to close file");
    }
  }

  public int getLine() {
    return this.line;
  }

  public Lexeme nextToken() {
    Lexeme lexeme = new Lexeme("", TokenType.END_OF_FILE);

    int state = 11;
    while(state != 17 && state != 18) {
      int c = getc();
      switch(state) {
        case 1:
          break;
        case 2:
          if(c == '-'){
            lexeme.token += (char) c;
            state = 3;
          } else {
            ungetc(c);
            state = 17;
          }
          break;
        case 3:
          if(c == '[') {
            lexeme.token += (char) c;
            state = 4;
          } else if(c == -1) {
            lexeme.type = TokenType.UNEXPECTED_EOF;
            state = 17;
          } else {
            lexeme.type = TokenType.INVALID_TOKEN;
            state = 17;
          }
          break;
        case 4:
          if(c == '[') {
            lexeme.token += (char) c;
            state = 5;
          } else if(c == '\n') {
            line++;
            state = 1;
          } else if(c == -1) {
            lexeme.type = TokenType.UNEXPECTED_EOF;
            state = 17;
          } else {
            lexeme.type = TokenType.INVALID_TOKEN;
            state = 17;
          }
          break;
        case 5:
          if(c != '-') {
            state = 5;
          } else if(c == '-') {
            lexeme.token += (char) c;
            state = 6;
          } else if(c == -1) {
            lexeme.type = TokenType.UNEXPECTED_EOF;
            state = 17;
          } else {
            lexeme.type = TokenType.INVALID_TOKEN;
            state = 17;
          }
          break;
        case 6:
          if(c != '-') {
            state = 5;
          } else if(c == '-') {
            lexeme.token += (char) c;
            state = 7;
          } else if(c == -1) {
            lexeme.type = TokenType.UNEXPECTED_EOF;
            state = 17;
          } else {
            lexeme.type = TokenType.INVALID_TOKEN;
            state = 17;
          }
          break;
        case 7:
          break;
        case 8:
          if(c == '[') {
            lexeme.token += (char) c;
            state = 1;
          }
          else if ( c == '-') {
            lexeme.token += (char) c;
            state = 6;
          }
          else {
            state = 5; 
          }
          break;
        case 9:
         
          break;
        case 10:
          if(c == 'c') {
            lexeme.token += (char) c;
            state = 17;
          } else {
            ungetc(c);
            state = 17;
          }
          break;
        case 11:
          if(c == '=') {
            lexeme.token += (char) c;
            state = 17;
          } else if(c == -1) {
            lexeme.type = TokenType.UNEXPECTED_EOF;
            state = 17;
          } else {
            lexeme.type = TokenType.INVALID_TOKEN;
            state = 17;
          }
          break;
        case 12:
          if(c == '.') {
            lexeme.token += (char) c;
            state = 17;
          } else {
            ungetc(c);
            state = 17;
          }
          break;
        case 13:
          if(Character.isLetter(c) 
            || Character.isDigit(c)
            || c == '_'
          ) {
            lexeme.token += (char) c;
            state = 13;
          } else {
            ungetc(c);
            state = 17;
          }
          break;
        case 14:
          if(c != '"') {
            state = 14;
          } else if(c == '"') {
            lexeme.token += (char) c;
            state = 18;
          }
          break;
        case 15:
          if(Character.isDigit(c)) {
            lexeme.token += (char) c;
            state = 15;
          } else if(c == '.') {
            lexeme.token += (char) c;
            state = 16;
          } else {
            ungetc(c);
            state = 18;
          }
          break;
        case 16:
          if(Character.isDigit(c)) {
            lexeme.token += (char) c;
            state = 16;
          } else {
            lexeme.type = TokenType.NUMBER;
            ungetc(c);
            state = 18;
          }
          break;
        default:
          throw new LexicalException("Unreachable");
      }
    }

    if(state == 17){
      lexeme.type = symbolTable.find(lexeme.token);
      System.out.println(lexeme.token + " " + lexeme.type);

    } 

    System.out.println(lexeme);
    return lexeme;
  }
  
  private int getc() {
    try {
      return input.read();
    } catch(Exception e) {
      throw new LexicalException("Unable to read file");
    }
  }

  private void ungetc(int c) {
    if(c != -1) {
      try {
        input.unread(c);
      } catch (Exception e) {
        throw new LexicalException("Unable to ungetc");
      }
    }
  }
}
