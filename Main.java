import lexical.LexicalAnalysis;
import lexical.TokenType;
import syntatic.SyntaticAnalysis;

public class Main {
    public static void main(String args[]) {
        try {
            LexicalAnalysis obj = new LexicalAnalysis("./file.lua");
            // Rodar a parte Sintatica
            while(obj.nextToken().type != TokenType.END_OF_FILE) {
                obj.nextToken();
            }
            // Rodar a parte Lexica
            // SyntaticAnalysis obj2 = new SyntaticAnalysis(obj);
            // obj2.start();

        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}
