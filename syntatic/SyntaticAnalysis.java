package syntatic;
import interpreter.expr.BinaryIntExpr;
import interpreter.expr.BoolExpr;
import interpreter.expr.ConstBoolExpr;
import interpreter.expr.ConstIntExpr;
import interpreter.expr.IntExpr;
import interpreter.expr.IntOp;
import interpreter.expr.NegIntExpr;
import interpreter.expr.NotBoolExpr;
import interpreter.expr.ReadIntExpr;
import interpreter.expr.RelOp;
import interpreter.expr.SingleBoolExpr;
import interpreter.expr.Variable;
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

  // validar se podemos passar para o próximo token
  private void eat(TokenType type) {
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
                    current.type == TokenType.ASSIGN) {
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
      case ASSIGN:
        procAssign();
        break;
      default:
        showError();
    }
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
  }

  // <print> ::= print '(' [ <expr> ] ')'
  private void procPrint() {

  }

  // <assign> ::= <lvalue> { ',' <lvalue> } '=' <expr> { ',' <expr> }
  private void procAssign() {

    procLValue();
    while (current.type == TokenType.COMMA) {
      advance();
      procLValue();
    }

    eat(TokenType.EQUAL);
    procExpr();

    while (current.type == TokenType.COMMA) {
      advance();
      procExpr();
    }
  }

  // <expr> ::= <rel> { (and | or) <rel> }
  private void procExpr() { }

  // <rel> ::= <concat> [ ('<' | '>' | '<=' | '>=' | '~=' | '==') <concat> ]
  private void procRel() {
    procConcat();
   }

  // <concat> ::= <arith> { '..' <arith> }
  private void procConcat() { }

  // <arith> ::= <term> { ('+' | '-') <term> }
  private void procArith() { }

  // <term> ::= <factor> { ('*' | '/' | '%') <factor> }
  private IntExpr procTerm() { 
    procFactor();
    IntExpr left = procFactor();
    if(current.type == TokenType.MUL 
      || current.type == TokenType.DIV 
      || current.type == TokenType.DIV) {
        int line = lexical.getLine();
        IntOp op;
        switch(current.type) {
          case MUL:
            op = IntOp.Mul;
            break;
          case DIV:
            op = IntOp.Div;
            break;
          case MOD:
            op = IntOp.Mod;
            break;
          default:
            op = IntOp.Mod;
            break;
        }

        advance();
        IntExpr right = procFactor();
        return new BinaryIntExpr(line, left, op, right);
      } else {
        return left;
      }
  }

  // <factor> ::= '(' <expr> ')' | [ '-' | '#' | not] <rvalue>
  private IntExpr procFactor() { 
    return null;
  }

  // <lvalue> ::= <name> { '.' <name> | '[' <expr> ']' }
  private void procLValue() { 
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
    else if (current.type == TokenType.OPEN_BRACKET) {
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
      advance();
    } else if (current.type == TokenType.STRING) {
      advance();  
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

  // <function> ::= (read | tonumber | tostring) '(' [ <expr> ] ')'
  private void procFunction() {
  }

  // <table> ::= '{' [ <elem> { ',' <elem> } ] '}'
  private void procTable() {
  }

  // <elem> ::= [ '[' <expr> ']' '=' ] <expr>
  private void procElem() {
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