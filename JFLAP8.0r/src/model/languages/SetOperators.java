package model.languages;

import java.util.Set;
import java.util.HashSet;

public class SetOperators {



	public static <T extends Object> Set<Set<T>> powerset (Set<T> originalSet) {
		
		return null;
	}


	public static void main(String[] args) {
		Set<Integer> nums = new HashSet<Integer>();
		nums.add(1);
		nums.add(2);
		nums.add(3);

		System.out.println(powerset(nums));
	}




}
