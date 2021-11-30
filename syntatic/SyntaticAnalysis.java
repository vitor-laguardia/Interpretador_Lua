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

  // <program>   ::= program <cmdlist>
  private void procProgram() {

   }

  // <cmdlist>   ::= <cmd> { <cmd> }
  private void procCmdList() { }

  // <cmd>       ::= (<assign> | <output> | <if> | <while>) ;
  private void procCmd() { }

  // <assign>    ::= <var> = <intexpr>
  private void procAssign() { }

  // <output>    ::= output <intexpr>
  private void procOutput() { }

  // <if>        ::= if <boolexpr> then <cmdlist> [ else <cmdlist> ] done
  private void procIf() { }

  // <while>     ::= while <boolexpr> do <cmdlist> done
  private void procWhile() { }

  // <boolexpr>  ::= false | true |
  //                 not <boolexpr> |
  //                 <intterm> (== | != | < | > | <= | >=) <intterm>
  private void procBoolExpr() { }

  // <intexpr>   ::= [ + | - ] <intterm> [ (+ | - | * | / | %) <intterm> ]
  private void procIntExpr() { }

  // <intterm>   ::= <var> | <const> | read
  private void procIntTerm() { }

  // <var>       ::= id
  private void procVar() { }

  // <const>     ::= number
  private void procConst() { }

  private void procName() { }

  private void procNumber() { }

}