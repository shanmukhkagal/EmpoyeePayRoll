package com.payroll;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileOperationTest {

    @Test
    public void entriesShouldReturnNoEntriesSentToFile() throws IOException {
        ArrayList<EmployeePayRoll> employeePayRollDataArrayList = new ArrayList<>();
        EmployeePayRollService employeePayRollService = new EmployeePayRollService(employeePayRollDataArrayList);
        Scanner inputFromConsole = new Scanner(System.in);
        employeePayRollService.readEmployeeData(inputFromConsole);
        employeePayRollService.writeEmployeeData();
        int count = employeePayRollService.writeEmployeeDetailTofFile();
        Assert.assertEquals(1, count);
    }
}
