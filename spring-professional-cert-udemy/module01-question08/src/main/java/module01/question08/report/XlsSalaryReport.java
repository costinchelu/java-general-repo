package module01.question08.report;

import module01.question08.ds.EmployeeSalary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class XlsSalaryReport implements SalaryReport {

    public void writeReport(List<EmployeeSalary> employeeSalaries) {
        System.out.println("Writing Xls Report");
    }
}
