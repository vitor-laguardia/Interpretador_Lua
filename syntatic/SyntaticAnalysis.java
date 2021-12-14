package syntatic;
import java.util.ArrayList;

import javax.management.StringValueExp;

import interpreter.command.BlocksCommand;
import interpreter.command.PrintCommand;
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
    System.out.println("Advanced (\"" + current.token + "\", " + 
      current.type + ")");
      
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
  private BlocksCommand procCode() { 
    int line = lexical.getLine();
    List<Command> cmds = new ArrayList<Command>();
    while (current.type == TokenType.IF ||
            current.type == TokenType.WHILE ||
              current.type == TokenType.REPEAT ||
                current.type == TokenType.FOR ||
                  current.type == TokenType.PRINT ||
                    current.type == TokenType.VAR ||
                      current.type == TokenType.COMMENTARY) {
                      Command cmd = procCmd();
                      cmds.add(cmd);
                    } 
      BlocksCommand bc = new BlocksCommand(line, cmds);

      return bc;
  }

  // <cmd> ::= (<if> | <while> | <repeat> | <for> | <print> | <assign>) [';']
  private Command procCmd() {
    Command cmd = null;
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

    return cmd;
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
      case ASSIGN:
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
  private PrintCommand procPrint() {
    eat(TokenType.PRINT);
    int line = lexical.getLine();
    eat(TokenType.OPEN_PARENTHESES);

    Expr expr = null;
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
                                    expr = procExpr();
                                  }
    eat(TokenType.CLOSE_PARENTHESES);
    PrintCommand pc = PrintCommand(line, expr);

    return pc;
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

  // <expr> ::= <rel> { (and | or) <rel> }
  private Expr procExpr() { 
    Expr expr = procRel();   
    while(current.type == TokenType.AND || current.type == TokenType.OR) {
      advance();
      procRel();
    }

    return expr;
  }

  // <rel> ::= <concat> [ ('<' | '>' | '<=' | '>=' | '~=' | '==') <concat> ]
  private Expr procRel() {
    Expr expr = procConcat();
    if (current.type == TokenType.LOWER ||
          current.type == TokenType.GREATER ||
            current.type == TokenType.LOWER_EQUAL ||
              current.type == TokenType.GREATER_EQUAL ||
                current.type == TokenType.NOT_EQUAL ||
                  current.type == TokenType.EQUAL) {
                    advance();
                    // VERIFICAR
                    expr = procConcat();
                  }

      return expr;
   }

  // <concat> ::= <arith> { '..' <arith> }
  private Expr procConcat() { 
    Expr expr = procArith();
    while (current.type == TokenType.CONCAT) {
      advance();
      procArith();
    }

    return expr;
  }

  // <arith> ::= <term> { ('+' | '-') <term> }
  private Expr procArith() { 
    Expr expr = procTerm();
    while (current.type == TokenType.ADD || current.type == TokenType.SUB) {
      advance();
      procTerm();
    }

    return expr;
  }

  // <term> ::= <factor> { ('*' | '/' | '%') <factor> }
  private Expr procTerm() { 
    Expr expr = procFactor();
    while (current.type == TokenType.MUL || 
            current.type == TokenType.DIV || 
              current.type == TokenType.MOD) {
                advance();
                procFactor();
              }  

    return expr;
  }

  // <factor> ::= '(' <expr> ')' | [ '-' | '#' | not] <rvalue>
  private Expr procFactor() { 
    Expr expr = null;
    if (current.type == TokenType.OPEN_PARENTHESES) {
      advance();
      procExpr();
      eat(TokenType.CLOSE_PARENTHESES);
    } 
    else if (current.type == TokenType.SUB ||
              current.type == TokenType.HASHTAG ||
                current.type == TokenType.NOT) {
                  advance();
                  expr = procRValue();
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
                                expr = procRValue();
                                }
    else showError();

      return expr;                         
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
  private Expr procRValue() {
    Expr expr = null; 
    if (current.type == TokenType.NUMBER ||
          current.type == TokenType.STRING ||
            current.type == TokenType.FALSE ||
              current.type == TokenType.TRUE ||
                current.type == TokenType.NIL) {

                  Value<?> v = procConst();
                  int line = lexical.getLine();
                  expr = new ConstExpr(line,v);

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
      procLValue();
    }
    else {
      showError();
    }

    return expr;
  }

  // <const> ::= <number> | <string> | false | true | nil
  private Value<?> procConst() { 
    Value<?> v = null;
    if (current.type == TokenType.NUMBER) {
      procNumber();
    } else if (current.type == TokenType.STRING) {
      v = procString();  
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

  private StringValue procString() {
    String tmp = current.token;
    eat(TokenType.STRING);
    StringValue sv = new StringValue(tmp);
    return sv;
  }

}