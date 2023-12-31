package chapter_04.good.taxes;


import common.personnel.*;


public class PartTimeTaxCalculator implements TaxCalculator {

    private final int RETIREMENT_TAX_PERCENTAGE = 10;
    private final int INCOME_TAX_PERCENTAGE = 16;
    private final int BASE_HEALTH_INSURANCE = 100;


    @Override
    public double calculate(Employee employee) {
        int monthlyIncome = employee.getMonthlyIncome();

        return BASE_HEALTH_INSURANCE + (monthlyIncome * RETIREMENT_TAX_PERCENTAGE) / 100 +
                (monthlyIncome * INCOME_TAX_PERCENTAGE) /100;
    }
}
