import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class LoginTest {
    private Connection connection;

    @BeforeClass
    public void connection() throws SQLException {
        String url = "jdbc:mysql://test.medis.mersys.io:33306/ts_data_driven_dauke";
        String user = "technostudy";
        String password = "zhTPis0l9#$&";
        connection = DriverManager.getConnection(url, user, password);
    }

    @DataProvider
    public Object[][] loginTestCases() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select username, password, statusCode, errorMessage from loginTestCases;");
        resultSet.last();
        int numberOfRows = resultSet.getRow();
        Object[][] data = new Object[numberOfRows][4];

        resultSet.beforeFirst();

        int index = 0;
        while (resultSet.next()) {
            data[index][0] = resultSet.getString("username");
            data[index][1] = resultSet.getString("password");
            data[index][2] = resultSet.getInt("statusCode");
            data[index][3] = resultSet.getString("errorMessage");
            index++;
        }

        return data;
    }

    @Test(dataProvider = "loginTestCases")
    public void loginTest(String username, String password, int statusCode, String errorMessage) {
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

        validatableResponse.statusCode(statusCode);

        if (errorMessage != null) {
            validatableResponse.body("title", equalTo(errorMessage));
        }

    }
}
