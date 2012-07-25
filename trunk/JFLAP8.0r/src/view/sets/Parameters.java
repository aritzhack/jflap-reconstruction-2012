package view.sets;

import javax.swing.JOptionPane;

public class Parameters {
	
	private int myFirst;
	private int mySecond;
	
	public Parameters (int i, int j) {
		myFirst = i;
		mySecond = j;
	}
	
	public static int getParameters() {
		String ans = JOptionPane.showInputDialog("Enter value: ");
		int i;
		try {
			i = Integer.parseInt(ans);
		} catch (NumberFormatException e) {
			i = getParameters();
		}
		
//		System.out.println(i);
		return i;
	}

	public static void main (String[] args) throws SecurityException, NoSuchMethodException {
//		Class cl = Parameters.class;
//		for (Class c : cl.getConstructor(int.class, int.class).getParameterTypes()) {
//			System.out.println(c);
//		}
		System.out.println(getParameters());
	}
	
}
