package genericTests;

import org.testng.annotations.Factory;

import java.io.IOException;

public class GenericFactoryTest {

    @Factory
    public Object[] tests() throws IOException {
        Object[] tests = new Object[2];
        tests[0] = new GenericAPITest("src/test/resources/tests_excels/countryTestCases.xlsx");
        tests[1] = new GenericAPITest("src/test/resources/tests_excels/feeTypeTestCases.xlsx");
        return tests;
    }
}
