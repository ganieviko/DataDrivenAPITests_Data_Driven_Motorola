import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.sql.*;

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
    }
}
