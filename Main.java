import lexical.LexicalAnalysis;
import lexical.TokenType;
import syntatic.SyntaticAnalysis;

public class Main {
    public static void main(String args[]) {
        try {
            LexicalAnalysis obj = new LexicalAnalysis("./file.lua");
            // while(obj.nextToken().type != TokenType.END_OF_FILE) {
            //     obj.nextToken();
            // }
            SyntaticAnalysis obj2 = new SyntaticAnalysis(obj);
            obj2.start();

        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}
