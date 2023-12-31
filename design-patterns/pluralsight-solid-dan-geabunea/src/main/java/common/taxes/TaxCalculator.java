package common.taxes;

import common.personnel.Employee;

public class TaxCalculator {

    private final int RETIREMENT_TAX_PERCENTAGE = 10;

    private final int INCOME_TAX_PERCENTAGE = 16;

    private final int BASE_HEALTH_INSURANCE = 100;


    public double calculate(Employee employee) {
        return BASE_HEALTH_INSURANCE +
                (double) (employee.getMonthlyIncome() * RETIREMENT_TAX_PERCENTAGE) / 100 +
                (double) (employee.getMonthlyIncome() * INCOME_TAX_PERCENTAGE) / 100;

    }
}
