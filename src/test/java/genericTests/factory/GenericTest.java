package genericTests.factory;

import org.testng.annotations.Test;

public class GenericTest {
    private String parameter;

    public GenericTest(String parameter) {
        this.parameter = parameter;
    }

    @Test
    public void testCase1() {
        System.out.println(parameter);
    }

    @Test
    public void testCase2() {
        System.out.println(parameter +" in second test case");
    }
}
