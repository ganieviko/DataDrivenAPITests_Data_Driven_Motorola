import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ExcelReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LoginWithExcelTest {

    @DataProvider
    private Object[][] loginTestCases() throws IOException {
        ExcelReader reader = new ExcelReader("src/test/resources/loginTestCases.xlsx");
        List<Map<String, String>> rows = reader.getListOfMaps();
        Object[][] data = new Object[rows.size()][4];

        int index = 0 ;
        for (Map<String, String> row : rows) {
            data[index][0] = row.get("username");
            data[index][1] = row.get("password");
            data[index][2] = row.get("statusCode");
            data[index][3] = row.get("errorMessage");
        }
        return data;
    }

    @Test(dataProvider="loginTestCases")
    public void loginTest(String username, String password, String statusCode, String errorMessage) {
        System.out.println(username + "\t\t\t" + password + "\t\t\t" + statusCode + "\t\t\t" + errorMessage);

    }

}
