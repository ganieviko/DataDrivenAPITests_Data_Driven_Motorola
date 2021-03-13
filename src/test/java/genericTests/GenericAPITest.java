package genericTests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ExcelReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class GenericAPITest {
    private Cookies cookies;
    private List<String> idsForCleanedUp;
    private String apiPath;
    private ExcelReader reader;

    @BeforeClass
    public void setUp() throws IOException {
        RestAssured.baseURI = "https://test.campus.techno.study";

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "daulet2030@gmail.com");
        credentials.put("password", "TechnoStudy123@");
        ValidatableResponse response = given()
                .body(credentials)
                .contentType(ContentType.JSON)
                .when()
                .post("/auth/login")
                .then();

        response.statusCode(200);

        cookies = response.extract().detailedCookies();

        idsForCleanedUp = new ArrayList<>();
        apiPath = "/school-service/api/countries/"; // fee-types
        reader = new ExcelReader("src/test/resources/tests_excels/countryTestCases.xlsx"); // feeTypeTestCases.xlsx
    }

    @DataProvider
    private Object[][] testCases() throws IOException {
        List<Map<String, String>> rows = reader.getListOfMaps();
        Object[][] data = new Object[rows.size()][3];

        int index = 0;
        for (Map<String, String> row : rows) {
            data[index][1] = row.get("statusCode");
            data[index][2] = row.get("errorMessage");
            row.remove("statusCode");
            row.remove("errorMessage");
            data[index][0] = row;
            index++;
        }
        return data;
    }

    @Test(dataProvider = "testCases")
    public void creationTest(Map<String, String> body, String statusCode, String errorMessage) {

        ValidatableResponse validatableResponse = given()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .body(body).log().body()
                .when()
                .post(apiPath)
                .then().log().body();

        if(validatableResponse.extract().response().getStatusCode() == 201) {
            idsForCleanedUp.add(validatableResponse.extract().jsonPath().getString("id"));
        }

        validatableResponse.statusCode((int) Double.parseDouble(statusCode));

        if (errorMessage != null && !errorMessage.equalsIgnoreCase("NULL")) {
            validatableResponse.body("message", containsString(errorMessage));
        }
    }

    @AfterClass
    public void cleanup() {
        for (String id : idsForCleanedUp) {
            given()
                    .cookies(cookies)
                    .when()
                    .delete(apiPath + id)
                    .then()
                    .statusCode(200)
            ;
        }
    }
}
