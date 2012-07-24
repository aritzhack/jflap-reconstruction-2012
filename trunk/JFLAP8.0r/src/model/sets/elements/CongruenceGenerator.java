package model.sets.elements;

public class CongruenceGenerator extends Generator {
	
	private int myModulus;
	private int myInitial;
	private int myOrder;
	
	public CongruenceGenerator (int modulus, int start) {
		myModulus = modulus;
		myInitial = wrap(start);
		myOrder = 0;
	}
	
	private int wrap (int n) {
		while (n >= myModulus) {
			n -= myModulus;
		}
		return n;
	}

	@Override
	public int generateNextValue() {
		int answer = myInitial + myModulus * myOrder;
		myOrder++;
		return answer;
	}

	@Override
	public String getName() {
		return "Congruence";
	}

}
