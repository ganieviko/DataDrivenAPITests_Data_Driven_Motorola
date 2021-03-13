package genericTests;

import org.testng.annotations.Factory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class GenericFactoryTest {

    @Factory
    public Object[] tests() throws IOException {
        List<String> files = Files.list(Paths.get("src/test/resources/tests_excels"))
                        .filter(path -> Files.isRegularFile(path) && path.toString().endsWith(".xlsx"))
                         .map(path -> path.toString())
                         .collect(Collectors.toList());

        Object[] tests = new Object[files.size()];
        for (int i = 0; i < files.size(); i++) {
            tests[i] = new GenericAPITest(files.get(i));
        }
        return tests;
    }
}
