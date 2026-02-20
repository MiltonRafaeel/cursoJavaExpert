package tests.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import entities.Financing;

public class FinancingTests {

	@Test
	public void constructorShouldCreateObjectWhenValidDate() {
		
		//ARRANGE
		
		//ACTION
		Financing f = new Financing(100000.0, 2000.0, 80);
		//ASSERTIONS
		Assertions.assertEquals(100000.0, f.getTotalAmount());
		Assertions.assertEquals(2000.0, f.getIncome());
		Assertions.assertEquals(80, f.getMonths());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void constructorShouldThrowIllegalArgumentExceptionObjectWhenInvalidDate() {
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Financing f = new Financing(100000.0, 2000.0, 20);
		});
		
	}
	
	@Test
	public void setTotalAmountShouldSetDataWhenValidDate() {
		
		//ARRANGE(instanciar os obj necessarios)
		Financing f = new Financing(100000.0, 2000.0, 80);
		
		//ACTION(suas acoes)
		f.setTotalAmount(90000.0);
		
		//ASSERTIONS(resultado esperado)
		Assertions.assertEquals(90000.0, f.getTotalAmount());
	}
	
	@Test
	public void setTotalAmountShouldThrowIllegalArgumentExceptionObjectWhenInvalidDate() {
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Financing f = new Financing(100000.0, 2000.0, 80);
			f.setTotalAmount(110000.0);
		});	
	}
	
	@Test
	public void setIncomeShouldSetDataWhenValidDate() {
		
		//ARRANGE(instanciar os obj necessarios)
		Financing f = new Financing(100000.0, 2000.0, 80);
		
		//ACTION(suas acoes)
		f.setIncome(2100.0);
		
		//ASSERTIONS(resultado esperado)
		Assertions.assertEquals(2100.0, f.getIncome());
	}
	
	@Test
	public void setIncomeShouldThrowIllegalArgumentExceptionObjectWhenInvalidDate() {
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Financing f = new Financing(100000.0, 1900.0, 80);
			f.setIncome(1900.0);
		});	
	}
	
	@Test
	public void setMonthsShouldSetDataWhenValidDate() {
		
		//ARRANGE(instanciar os obj necessarios)
		Financing f = new Financing(100000.0, 2000.0, 80);
		
		//ACTION(suas acoes)
		f.setMonths(81);
		
		//ASSERTIONS(resultado esperado)
		Assertions.assertEquals(81, f.getMonths());
	}
	
	@Test
	public void setMonthsShouldThrowIllegalArgumentExceptionObjectWhenInvalidDate() {
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Financing f = new Financing(100000.0, 2000.0, 79);
			f.setMonths(79);
		});	
	}
	
	@Test
	public void entryShouldCalculateEntryCorrectly() {
		
		//ARRANGE(instanciar os obj necessarios)
		Financing f = new Financing(100000.0, 2000.0, 80);
		
		//ACTION(suas acoes)
		double result = f.entry();
		double aux = result;
		
		//ASSERTIONS(resultado esperado)
		Assertions.assertEquals(result, aux);
	}
	
	@Test
	public void quotaShouldCalculateQuotaCorrectly() {
		
		//ARRANGE(instanciar os obj necessarios)
		Financing f = new Financing(100000.0, 2000.0, 80);
		
		//ASSERTIONS(resultado esperado)
		Assertions.assertEquals(1000, f.quota());
	}
}
