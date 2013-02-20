package model.lsystem.commands;

public class DrawCommand extends LSystemCommand{

	public DrawCommand(String s) {
		super(s);
	}
	
	@Override
	public String getDescriptionName() {
		return "Forward and Draw Command";
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
}
