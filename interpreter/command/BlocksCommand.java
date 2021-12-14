package interpreter.command;

public class BlocksCommand extends Command {

    private List<Command> cmd;

    public BlocksCommand (int line, List<Command> cmds) {
        super(line);
        this.cmd = cmds;
    }

    @Override
    public void execute() {
        for (Command cmd : cmds) {
            cmd.execute();
        }
    }
    
}
