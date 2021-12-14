package interpreter.command;

import interpreter.expr.Expr;

public class PrintCommand extends Command {

    private Expr expr;

    public PrintCommand (int line, Expr expr) {
        super(line);
        this.expr = expr;
    }

    @Override
    public void execute () {
        System.out.println("cheguei aq menorzada");
    }

}
