package genericTests.factory;

import org.testng.annotations.Factory;

public class Factory2 {

    @Factory
    public Object[] factory(){
        Object[] tests = new Object[10];
        for (int i = 0; i < 5; i++) {
            tests[i] = new GenericTest(String.valueOf(i));
        }
        for (int i = 5; i < 10; i++) {
            tests[i] = new AnotherTest();
        }
        return tests;
    }
}
