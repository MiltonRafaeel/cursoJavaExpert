package tests.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import entities.Account;
import tests.factory.AccountFactory;

public class AccountTests {

	@Test
	public void depositShouldIncreaceBalanceWhenPositiveAmount() {
		
		double amount = 200;
		double expectedValue = 196;
		Account acc = AccountFactory.createEmptyAccount();
		
		acc.deposit(amount);
		
		Assertions.assertEquals(expectedValue, acc.getBalance());
	}
	
	@Test
	public void depositShouldDoNothingWhenNegativeAmount() {
		
		double expectedValue = 100.0;
		Account acc = AccountFactory.createEmptyAccount(expectedValue);
		double amount = -200;
		
		acc.deposit(amount);
		
		Assertions.assertEquals(expectedValue, acc.getBalance());
	}
	
	@Test
	public void fullWithdrawShouldClearBalanceAndReturnFullBalance() {
		
		double expectedValue = 0.0;
		double initialBalance = 800.0;
		Account acc = AccountFactory.createEmptyAccount(initialBalance);
		
		double result = acc.fullWithdraw();;
		
		Assertions.assertTrue(expectedValue == acc.getBalance());
		//outra forma
		Assertions.assertTrue(result == initialBalance);
		//outra forma
		Assertions.assertEquals(expectedValue, acc.getBalance());
	}
	 
	@Test
	public void withdrawShouldDecreaseBalanceWhenSufficientBalance() {
		
		Account acc = AccountFactory.createEmptyAccount(800.0);
		
		acc.withdraw(500.0);
		
		Assertions.assertEquals(300.0, acc.getBalance());
	}
	
	@Test
	public void withdrawShouldThrowExceptionWhenSufficientBalance() {
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> { 
			Account acc = AccountFactory.createEmptyAccount(800.0);
			acc.withdraw(900.0);
		});
	}
}
