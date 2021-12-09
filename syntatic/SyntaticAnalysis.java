package syntatic;
// import interpreter.expr.BinaryIntExpr;
// import interpreter.expr.BoolExpr;
// import interpreter.expr.ConstBoolExpr;
// import interpreter.expr.ConstIntExpr;
// import interpreter.expr.IntExpr;
// import interpreter.expr.IntOp;
// import interpreter.expr.NegIntExpr;
// import interpreter.expr.NotBoolExpr;
// import interpreter.expr.ReadIntExpr;
// import interpreter.expr.RelOp;
// import interpreter.expr.SingleBoolExpr;
// import interpreter.expr.Variable;
import lexical.Lexeme;
import lexical.LexicalAnalysis;
import lexical.TokenType;
public class SyntaticAnalysis {

  private LexicalAnalysis lexical;
  private Lexeme current;     

  public SyntaticAnalysis(LexicalAnalysis lex) {
    this.lexical = lex;
    this.current = lex.nextToken();
  }

  // Pedir para o analixador léxico o pŕoximo token
  private void advance() {
    current = lexical.nextToken();
  }

  public void start() {
    procCode();
    eat(TokenType.END_OF_FILE);
  }

  // validar se podemos passar para o próximo token
  private void eat(TokenType type) {
    System.out.println("Expected (..., " + type + "), found (\"" +
          current.token + "\", " + current.type + ")");

    if (type == current.type) {
      advance();
    } else {
      showError();
    }
  }

  private void showError() {
    System.out.printf("%02d: ", lexical.getLine());

    switch (current.type) {
        case INVALID_TOKEN:
            System.out.printf("Lexema inválido [%s]\n", current.token);
            break;
        case UNEXPECTED_EOF:
        case END_OF_FILE:
            System.out.printf("Fim de arquivo inesperado\n");
            break;
        default:
            System.out.printf("Lexema não esperado [%s]\n", current.token);
            break;
    }
    System.exit(1);
  }

  // <code> ::= { <cmd> }
  private void procCode() { 
    while (current.type == TokenType.IF ||
            current.type == TokenType.WHILE ||
              current.type == TokenType.REPEAT ||
                current.type == TokenType.FOR ||
                  current.type == TokenType.PRINT ||
                    current.type == TokenType.VAR ||
                      current.type == TokenType.COMMENTARY) {
                      procCmd();
                    } 
  }

  // <cmd> ::= (<if> | <while> | <repeat> | <for> | <print> | <assign>) [';']
  private void procCmd() {
    switch (current.type) {
      case IF :
        procIf();
        break;
      case WHILE:
        procWhile();
        break;
      case REPEAT:
        procRepeat();
        break;
      case FOR:
        procFor();
        break;
      case PRINT:
        procPrint();
        break;
      case VAR:
        procAssign();
        break;
      case COMMENTARY:
        advance();
        break;
      default:
        showError();
        break;
    }

    if (current.type == TokenType.COMMA) 
      advance();

   }

  // <if> ::= if <expr> then <code> { elseif <expr> then <code> } [ else <code> ] end
  private void procIf() { 
    eat(TokenType.IF);
    procExpr();
    eat(TokenType.THEN);
    procCode();

    while( current.type == TokenType.ELSE_IF) {
      advance();
      procExpr();
      eat(TokenType.THEN);
      procCode();
    }

    if (current.type == TokenType.ELSE) {
      advance();
      procCode();
    }

    eat(TokenType.END);
  }

  // <while> ::= while <expr> do <code> end
  private void procWhile() {
    eat(TokenType.WHILE);
    procExpr();
    eat(TokenType.DO);
    procCode();
    eat(TokenType.END);
  }

  // <repeat> ::= repeat <code> until <expr>
  private void procRepeat() {
    eat(TokenType.REPEAT);
    procCode();
    eat(TokenType.UNTIL);
    procExpr();
  }

  // <for> ::= for <name> (('=' <expr> ',' <expr> [',' <expr>]) | ([',' <name>] in <expr>)) do <code> end
  private void procFor() {
    eat(TokenType.FOR);
    procName();

    switch (current.type) {
      case EQUAL:
        advance();
        procExpr();
        eat(TokenType.COMMA);
        procExpr();

        if (current.type == TokenType.COMMA) {
          advance();
          procExpr();
        }
        break;
        
      case COMMA:
        advance();
        procName();
        eat(TokenType.IN);
        procExpr();
        break;
      
      case IN:
        advance();
        procExpr();
        break;

      default:
        showError();
        break;
    }

    eat(TokenType.DO);
    procCode();
    eat(TokenType.END);

  }

  // <print> ::= print '(' [ <expr> ] ')'
  private void procPrint() {
    eat(TokenType.PRINT);
    eat(TokenType.OPEN_PARENTHESES);
    if (current.type == TokenType.OPEN_PARENTHESES ||
          current.type == TokenType.SUB ||
            current.type == TokenType.HASHTAG ||
              current.type == TokenType.NOT ||
                current.type == TokenType.NUMBER ||
                  current.type == TokenType.STRING ||
                    current.type == TokenType.FALSE ||
                      current.type == TokenType.TRUE ||
                        current.type == TokenType.NIL || 
                          current.type == TokenType.READ ||
                            current.type == TokenType.TO_NUMBER ||
                              current.type == TokenType.TO_STRING || 
                                current.type == TokenType.OPEN_KEYS ||
                                  current.type == TokenType.VAR) {
                                    procExpr();
                                  }
    eat(TokenType.CLOSE_PARENTHESES);
  }

  // <assign> ::= <lvalue> { ',' <lvalue> } '=' <expr> { ',' <expr> }
  private void procAssign() {
    procLValue();
    while (current.type == TokenType.COMMA) {
      advance();
      procLValue();
    }

    eat(TokenType.ASSIGN);
    procExpr();

    while (current.type == TokenType.COMMA) {
      advance();
      procExpr();
    }
  }

  // TAMO AQUI
  // <expr> ::= <rel> { (and | or) <rel> }
  private void procExpr() { 
    procRel();
    while(current.type == TokenType.AND || current.type == TokenType.OR) {
      advance();
      procRel();
    }
  }

  // <rel> ::= <concat> [ ('<' | '>' | '<=' | '>=' | '~=' | '==') <concat> ]
  private void procRel() {
    procConcat();
    if (current.type == TokenType.LOWER ||
          current.type == TokenType.GREATER ||
            current.type == TokenType.LOWER_EQUAL ||
              current.type == TokenType.GREATER_EQUAL ||
                current.type == TokenType.NOT_EQUAL ||
                  current.type == TokenType.EQUAL) {
                    advance();
                    procConcat();
                  }
   }

  // <concat> ::= <arith> { '..' <arith> }
  private void procConcat() { 
    procArith();
    while (current.type == TokenType.CONCAT) {
      advance();
      procArith();
    }
  }

  // <arith> ::= <term> { ('+' | '-') <term> }
  private void procArith() { 
    procTerm();
    while (current.type == TokenType.ADD || current.type == TokenType.SUB) {
      advance();
      procTerm();
    }
  }

  // <term> ::= <factor> { ('*' | '/' | '%') <factor> }
  private void procTerm() { 
    procFactor();
    while (current.type == TokenType.MUL || 
            current.type == TokenType.DIV || 
              current.type == TokenType.MOD) {
                advance();
                procFactor();
              }
  }

  // ERRADO CORRIGIR OPEN BRACKET
  // <factor> ::= '(' <expr> ')' | [ '-' | '#' | not] <rvalue>
  private void procFactor() { 
    if (current.type == TokenType.OPEN_PARENTHESES) {
      advance();
      procExpr();
      eat(TokenType.CLOSE_PARENTHESES);
    } 
    else if (current.type == TokenType.SUB ||
              current.type == TokenType.HASHTAG ||
                current.type == TokenType.NOT) {
                  advance();
                  procRValue();
                }
    else if (current.type == TokenType.NUMBER ||
              current.type == TokenType.STRING ||
                current.type == TokenType.FALSE ||
                  current.type == TokenType.TRUE ||
                    current.type == TokenType.NIL || 
                      current.type == TokenType.READ ||
                        current.type == TokenType.TO_NUMBER ||
                          current.type == TokenType.TO_STRING || 
                            current.type == TokenType.OPEN_KEYS ||
                              current.type == TokenType.VAR) {
                                procRValue();
                                }
    else showError();
  }

  // <lvalue> ::= <name> { '.' <name> | '[' <expr> ']' }
  private void procLValue() { 
    procName();
    while (current.type == TokenType.DOT || current.type == TokenType.OPEN_BRACKET) {
      if (current.type == TokenType.DOT) {
        advance();
        procName();
      } else if (current.type == TokenType.OPEN_BRACKET) {
          advance();
          procExpr();
          eat(TokenType.CLOSE_BRACKET);
      } else showError();
    }
  }

  // <rvalue> ::= <const> | <function> | <table> | <lvalue>
  private void procRValue() {
    if (current.type == TokenType.NUMBER ||
          current.type == TokenType.STRING ||
            current.type == TokenType.FALSE ||
              current.type == TokenType.TRUE ||
                current.type == TokenType.NIL) {
                  procConst();
                }
    else if (current.type == TokenType.READ ||
              current.type == TokenType.TO_NUMBER ||
                current.type == TokenType.TO_STRING) {
                  procFunction();
    }
    else if (current.type == TokenType.OPEN_KEYS) {
      procTable();
    }
    else if (current.type == TokenType.VAR) {
      procName();
    }
    else {
      showError();
    }
    
  }

  // <const> ::= <number> | <string> | false | true | nil
  private void procConst() { 
    if (current.type == TokenType.NUMBER) {
      procNumber();
    } else if (current.type == TokenType.STRING) {
      procString();  
    } else if (current.type == TokenType.FALSE) {
      advance();
    } else if (current.type == TokenType.TRUE) {
      advance();
    } else if (current.type == TokenType.NIL) {
      advance();
    } else {
      showError();
    }
  }
  //AQUI
  // <function> ::= (read | tonumber | tostring) '(' [ <expr> ] ')'
  private void procFunction() {
    if (current.type == TokenType.READ) {
      advance();
      eat(TokenType.OPEN_PARENTHESES);
      if (current.type == TokenType.OPEN_PARENTHESES || 
            current.type == TokenType.SUB ||
              current.type == TokenType.HASHTAG ||
                current.type ==TokenType.NOT ||
                  current.type == TokenType.NUMBER ||
                    current.type == TokenType.STRING ||
                      current.type == TokenType.FALSE ||
                        current.type == TokenType.TRUE ||
                          current.type == TokenType.NIL || 
                            current.type == TokenType.READ ||
                              current.type == TokenType.TO_NUMBER ||
                                current.type == TokenType.TO_STRING || 
                                  current.type == TokenType.OPEN_KEYS ||
                                    current.type == TokenType.VAR) {
                                      procExpr();
                                    }
        eat(TokenType.CLOSE_PARENTHESES);
    } 
    else if  (current.type == TokenType.TO_NUMBER) {
      advance();
      eat(TokenType.OPEN_PARENTHESES);
      if (current.type == TokenType.OPEN_PARENTHESES || 
            current.type == TokenType.SUB ||
              current.type == TokenType.HASHTAG ||
                current.type ==TokenType.NOT ||
                  current.type == TokenType.NUMBER ||
                    current.type == TokenType.STRING ||
                      current.type == TokenType.FALSE ||
                        current.type == TokenType.TRUE ||
                          current.type == TokenType.NIL || 
                            current.type == TokenType.READ ||
                              current.type == TokenType.TO_NUMBER ||
                                current.type == TokenType.TO_STRING || 
                                  current.type == TokenType.OPEN_KEYS ||
                                    current.type == TokenType.VAR) {
                                      procExpr();
                                    } 
        eat(TokenType.CLOSE_PARENTHESES);
    }
    else if (current.type == TokenType.TO_STRING) {
      advance();
      eat(TokenType.OPEN_PARENTHESES);
      if (current.type == TokenType.OPEN_PARENTHESES || 
            current.type == TokenType.SUB ||
              current.type == TokenType.HASHTAG ||
                current.type ==TokenType.NOT ||
                  current.type == TokenType.NUMBER ||
                    current.type == TokenType.STRING ||
                      current.type == TokenType.FALSE ||
                        current.type == TokenType.TRUE ||
                          current.type == TokenType.NIL || 
                            current.type == TokenType.READ ||
                              current.type == TokenType.TO_NUMBER ||
                                current.type == TokenType.TO_STRING || 
                                  current.type == TokenType.OPEN_KEYS ||
                                    current.type == TokenType.VAR) {
                                      procExpr();
                                    } 
        eat(TokenType.CLOSE_PARENTHESES);
    } 
    else showError();
  }

  // <table> ::= '{' [ <elem> { ',' <elem> } ] '}'
  private void procTable() {
    eat(TokenType.OPEN_KEYS);
    if (current.type == TokenType.OPEN_BRACKET ||
         current.type == TokenType.OPEN_PARENTHESES ||
          current.type == TokenType.SUB ||
            current.type == TokenType.HASHTAG ||
              current.type ==TokenType.NOT ||
                current.type == TokenType.NUMBER ||
                  current.type == TokenType.STRING ||
                    current.type == TokenType.FALSE ||
                      current.type == TokenType.TRUE ||
                        current.type == TokenType.NIL || 
                          current.type == TokenType.READ ||
                            current.type == TokenType.TO_NUMBER ||
                              current.type == TokenType.TO_STRING || 
                                current.type == TokenType.OPEN_KEYS ||
                                  current.type == TokenType.VAR) {
                                    procElem();
                                    while (current.type == TokenType.COMMA) {
                                      advance();
                                      procElem();
                                    }
                                  }
    eat(TokenType.CLOSE_KEYS);
  }

  // <elem> ::= [ '[' <expr> ']' '=' ] <expr>
  private void procElem() {
    if (current.type == TokenType.OPEN_BRACKET) {
      advance();
      procExpr();
      eat(TokenType.CLOSE_BRACKET);
      eat(TokenType.ASSIGN);
    }
    procExpr();
  }

  private void procName() {
      eat(TokenType.VAR);
  }

  private void procNumber() {
      eat(TokenType.NUMBER);
  }

  private void procString() {
      eat(TokenType.STRING);
  }

}