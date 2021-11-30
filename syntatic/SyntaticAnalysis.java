package syntatic;

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
  private void procCode() { }

  // <cmd> ::= (<if> | <while> | <repeat> | <for> | <print> | <assign>) [';']
  private void procCmd() { }

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
  private void procRel() { }

  // <concat> ::= <arith> { '..' <arith> }
  private void procConcat() { }

  // <arith> ::= <term> { ('+' | '-') <term> }
  private void procArith() { }

  // <term> ::= <factor> { ('*' | '/' | '%') <factor> }
  private void procTerm() { }

  // <factor> ::= '(' <expr> ')' | [ '-' | '#' | not] <rvalue>
  private void procFactor() { }

  // <lvalue> ::= <name> { '.' <name> | '[' <expr> ']' }
  private void procLValue() { }

  // <rvalue> ::= <const> | <function> | <table> | <lvalue>
  private void procRValue() { }

  // <const> ::= <number> | <string> | false | true | nil
  private void procConst() { }

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