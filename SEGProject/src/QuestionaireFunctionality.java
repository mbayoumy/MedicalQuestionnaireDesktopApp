import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class QuestionaireFunctionality extends MySQLInterface {

	private String questionnaireName, queryForSQL;
	private java.sql.Date dateCreated;
	private int  GMCNumber;
	private Connection connection;
	private Statement statement;
	private ResultSet result;

	public QuestionaireFunctionality(
			String questionnaireName, java.sql.Date dateCreated, int GMCNumber) {

		this.questionnaireName = questionnaireName;
		this.dateCreated = dateCreated;
		this.GMCNumber = GMCNumber;
	}
	
	// used with getQuestionnaireInfo to retrieve the last inserted questionnaire 
	public QuestionaireFunctionality(){
		
	}

	
	@Override
	public void addQuestionInfo() throws SQLException {

		
	}

	@Override
	public void addQuestionaireInfo() throws SQLException {
		connection = null;
		statement = null;
		result = null;

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:src\\doctorDB.db");
			statement = connection.createStatement();

			queryForSQL = "INSERT INTO questionnaire VALUES( NULL, ' "+ questionnaireName + "', '" + dateCreated + "' ," + GMCNumber
					+ ")";

			
			statement.execute(queryForSQL);

		} catch (ClassNotFoundException e) {
			System.err.print("CLASS NOT FOUND");
		}
	}


	@Override
	public ArrayList<String> getQuestionaireInfo() throws SQLException {
		connection = null;
		statement = null;
		result = null;
		ArrayList<String> info = new ArrayList<String>();

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:src\\doctorDB.db");
			statement = connection.createStatement();

			queryForSQL = "SELECT questionnaireID FROM questionnaire";

			//http://stackoverflow.com/questions/4233241/viewing-results-of-mysql-query-in-java

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
