package tests.factory;

import entities.Financing;

public class FinancingFactory {
		
	public static Financing creatFinancing() {
		return new Financing();
	}
	
	public static Financing creatFinancing(double totalAmount, double income, int months) {
		return new Financing(totalAmount, income, months);
	}
	
}
