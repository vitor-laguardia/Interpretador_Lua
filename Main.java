import lexical.LexicalAnalysis;
import lexical.Lexeme;

public class Main {
    public static void main(String args[]) {
        try {
            LexicalAnalysis obj = new LexicalAnalysis("./teste.lua");
            System.out.println(obj.nextToken());
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}
