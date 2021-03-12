import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ExcelReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

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
            index++;
        }
        return data;
    }

    @Test(dataProvider="loginTestCases")
    public void loginTest(String username, String password, String statusCode, String errorMessage) {
        System.out.println(username + "\t\t\t" + password + "\t\t\t" + statusCode + "\t\t\t" + errorMessage);

        Map<String, String> body = new HashMap<>();
        if (username != null) {
            body.put("username", username);
        }
        if (password != null) {
            body.put("password", password);
        }

        ValidatableResponse validatableResponse = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("https://test.campus.techno.study/auth/login")
                .then();

        validatableResponse.statusCode((int)Double.parseDouble(statusCode));

        if(errorMessage != null && !errorMessage.equalsIgnoreCase("NULL")) {
            validatableResponse.body("title", equalTo(errorMessage));
        }
    }

}
