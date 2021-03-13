package genericTests.factory;

import org.testng.annotations.Test;

public class GenericTest {
    private String parameter;

    public GenericTest(String parameter) {
        this.parameter = parameter;
    }

    @Test
    public void testCase() {
        System.out.println(parameter);
    }
}
