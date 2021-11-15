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

    int state = 1;
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
          break;
        case 4:
          break;
        case 5:
          break;
        case 6:
          break;
        case 7:
          break;
        case 8:
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
          break;
        case 14:
          break;
        case 15:
          break;
        case 16:
          break;
        case 17:
          break;
        case 18:
          break;
        default:
          throw new LexicalException("Unreachable");
      }
    }

    if(state == 17) 
      lexeme.type = symbolTable.find(lexeme.token);

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
