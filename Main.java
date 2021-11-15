import lexical.LexicalAnalysis;
import lexical.TokenType;

public class Main {
    public static void main(String args[]) {
        try {
            LexicalAnalysis obj = new LexicalAnalysis("./file.lua");
            while(obj.nextToken().type != TokenType.END_OF_FILE) {
                obj.nextToken();
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}
