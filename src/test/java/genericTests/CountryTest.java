package genericTests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.*;
import utils.ExcelReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class CountryTest {
    private Cookies cookies;
    private List<String> idsForCleanedUp;

    @BeforeClass
    public void setUp() {
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
    }

    @DataProvider
    private Object[][] testCases() throws IOException {
        ExcelReader reader = new ExcelReader("src/test/resources/tests_excels/countryTestCases.xlsx");
        List<Map<String, String>> rows = reader.getListOfMaps();
        Object[][] data = new Object[rows.size()][4];

        int index = 0;
        for (Map<String, String> row : rows) {
            data[index][0] = row.get("name");
            data[index][1] = row.get("code");
            data[index][2] = row.get("statusCode");
            data[index][3] = row.get("errorMessage");
            index++;
        }
        return data;
    }

    @Test(dataProvider = "testCases")
    public void creationTest(String name, String code, String statusCode, String errorMessage) {

        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("code", code);


        ValidatableResponse validatableResponse = given()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .body(body).log().body()
                .when()
                .post("/school-service/api/countries")
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
                    .delete("/school-service/api/countries/" + id)
                    .then()
                    .statusCode(200)
            ;
        }
    }
}
