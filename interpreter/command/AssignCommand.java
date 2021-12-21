package interpreter.command;

import java.util.Vector;

import interpreter.expr.Expr;
import interpreter.expr.SetExpr;

public class AssignCommand extends Command {

    private Vector<SetExpr> lhs;
    private Vector<Expr> rhs;

    public AssignCommand(int line, Vector<SetExpr> lhs, Vector<Expr> rhs) {
        super(line);
        this.rhs = rhs;
        this.lhs = lhs;
    }

    @Override
    public void execute() {
        System.out.println("fazendo atribuição");
    }
    
}
