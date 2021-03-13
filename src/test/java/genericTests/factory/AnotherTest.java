package genericTests.factory;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AnotherTest {

    @BeforeClass
    public void setUp()  {
        System.out.println("Before");
    }

    @Test
    public void test() {
        System.out.println("Before");
    }
}
