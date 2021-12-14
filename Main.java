import interpreter.command.Command;
import lexical.LexicalAnalysis;
import lexical.TokenType;
import syntatic.SyntaticAnalysis;

public class Main {
    public static void main(String args[]) {
        try {
            LexicalAnalysis obj = new LexicalAnalysis("./test.lua");
            // Rodar a parte Sintatica
            // while(obj.nextToken().type != TokenType.END_OF_FILE) {
            //     obj.nextToken();
            // }
            // Rodar a parte Lexica
            SyntaticAnalysis obj2 = new SyntaticAnalysis(obj);
            Command c = obj2.start();
            c.execute();

        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}
