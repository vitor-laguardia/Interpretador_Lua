import lexical.LexicalAnalysis;

public class Main {
    public static void main(String args[]) {
        try {
            LexicalAnalysis obj = new LexicalAnalysis("./teste");
            System.out.println(obj);
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}
