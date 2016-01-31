import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LoginFunctionality extends MySQLInterface {

	private String username, password, firstName, lastName, queryForSQL;
	private int GMCNumber;
	private Connection connection;
	private Statement statement;
	private ResultSet result;

	public LoginFunctionality(String username, String password) {
		this.username = username.toLowerCase();
		this.password = password;
	}

	public LoginFunctionality(String username, String password,
			String firstName, String lastName, int GMCNumber) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.GMCNumber = GMCNumber;
	}

	public void addUserInfo() throws SQLException {

		connection = null;
		statement = null;
		result = null;

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager
					.getConnection("jdbc:sqlite:src\\doctorDB.db");
			statement = connection.createStatement();

			queryForSQL = "INSERT INTO login VALUES(" + GMCNumber + ", '"
					+ username + "' , '" + password + "' , '" + firstName
					+ "' , '" + lastName + "' )";

			statement.execute(queryForSQL);

		} catch (ClassNotFoundException e) {
			System.err.print("CLASS NOT FOUND");
		}

	}

	public ArrayList<String> getUserInfo() throws SQLException {
		connection = null;
		statement = null;
		result = null;
		ArrayList<String> info = new ArrayList<String>();

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager
					.getConnection("jdbc:sqlite:src\\doctorDB.db");

			statement = connection.createStatement();

			queryForSQL = "SELECT GMC, FirstName, LastName  FROM login WHERE UserName= '"
					+ username + "' AND Password= '" + password + "'";

			// http://stackoverflow.com/questions/4233241/viewing-results-of-mysql-query-in-java

			result = statement.executeQuery(queryForSQL);

			int columns = result.getMetaData().getColumnCount();

			while (result.next()) {
				for (int i = 1; i <= columns; i++) {
					info.add(result.getString(i));
				}
			}

		} catch (ClassNotFoundException e) {
			System.err.print("CLASS NOT FOUND");
		}

		return info;

	}

	@Override
	public ArrayList<String> checkIfUserAlreadyExistByGMC() throws SQLException {

		connection = null;
		statement = null;
		result = null;
		ArrayList<String> info = new ArrayList<String>();

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager
					.getConnection("jdbc:sqlite:src\\doctorDB.db");

			statement = connection.createStatement();

			queryForSQL = "SELECT GMC FROM login WHERE GMC = " + GMCNumber
					+ " ;";

			// http://stackoverflow.com/questions/4233241/viewing-results-of-mysql-query-in-java

			result = statement.executeQuery(queryForSQL);

			int columns = result.getMetaData().getColumnCount();

			while (result.next()) {
				for (int i = 1; i <= columns; i++) {
					info.add(result.getString(i));
				}
			}

		} catch (ClassNotFoundException e) {
			System.err.print("CLASS NOT FOUND");
		}

		return info;
	}

	@Override
	public ArrayList<String> checkIfUserAlreadyExistByUsername()
			throws SQLException {
		connection = null;
		statement = null;
		result = null;
		ArrayList<String> info = new ArrayList<String>();

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager
					.getConnection("jdbc:sqlite:src\\doctorDB.db");

			statement = connection.createStatement();

			queryForSQL = "SELECT UserName FROM login WHERE UserName = '"
					+ username + "' ;";

			// http://stackoverflow.com/questions/4233241/viewing-results-of-mysql-query-in-java

			result = statement.executeQuery(queryForSQL);

			int columns = result.getMetaData().getColumnCount();

			while (result.next()) {
				for (int i = 1; i <= columns; i++) {
					info.add(result.getString(i));
				}
			}

		} catch (ClassNotFoundException e) {
			System.err.print("CLASS NOT FOUND");
		}

		return info;

	}

}
