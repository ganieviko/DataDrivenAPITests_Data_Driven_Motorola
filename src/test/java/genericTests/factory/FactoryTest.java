package genericTests.factory;

import org.testng.annotations.Factory;

public class FactoryTest {
    @Factory
    public Object[] factory(){
        Object[] test = new Object[3];

        test[0] = new GenericTest("parameter 1");
        test[1] = new AnotherTest();
        test[2] = new GenericTest("parameter 2");

        return test;
    }
}
