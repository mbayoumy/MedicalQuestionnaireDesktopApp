import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class FeedbackFunctionality extends MySQLInterface {

	private String patientID,answer,firstName,lastName,dateOfBirth, queryForSQL;
	private int  questionID,QuestionnreID;
	private Connection connection;
	private Statement statement;
	private ResultSet result;
	
	
	
	//for add feedback method
	FeedbackFunctionality(int QuestionnreID , String patientID, int questionID, String answer ){
		this.QuestionnreID = QuestionnreID;
		this.patientID = patientID;
		this.questionID = questionID;
		this.answer = answer;
	}
	FeedbackFunctionality(String patientID, String firstName, String lastName, String dateOfBirth  ){
		this.patientID = patientID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
	}
	FeedbackFunctionality(){};



	@Override
	public ArrayList<String> getQuestionnaires() throws SQLException {
		connection = null;
		statement = null;
		result = null;
		ArrayList<String> info = new ArrayList<String>();

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:src\\doctorDB.db");
			statement = connection.createStatement();

			queryForSQL = "SELECT Name " +
					" FROM questionnaire";

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

	@Override
	public ArrayList<String> getPatients(int qID) throws SQLException {
		
		connection = null;
		statement = null;
		result = null;
		ArrayList<String> info = new ArrayList<String>();

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:src\\doctorDB.db");
			statement = connection.createStatement();
			
			System.out.println(qID);
			queryForSQL = "SELECT PatientID "
					+ "FROM  feedback "
					+ "WHERE QuestionnaireID =" + qID + " GROUP BY PatientID";				

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
		System.out.println(info);
		return info;
	}
	@Override
	public void addfeedback() throws SQLException {	
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;

		
	}
	@Override
	public void addpatients() throws SQLException {
		
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;

		try {
			ArrayList<String> info = new ArrayList<String>();
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:src\\doctorDB.db");
			statement = connection.createStatement();

			String queryForSQL = "SELECT * FROM patients " +
					"WHERE PatientID = '"+ patientID + "' ;";
			result = statement.executeQuery(queryForSQL);

			int columns = result.getMetaData().getColumnCount();

			while (result.next()) {
				for (int i = 1; i <= columns; i++) {
					info.add(result.getString(i));
				}
			}
			if (info.isEmpty()){

			queryForSQL = "INSERT INTO patients VALUES " +
					"('" + patientID +"' , '" + firstName + "', '"+ lastName +"', '" + dateOfBirth + "')";
			statement.execute(queryForSQL);
			}

		} catch (ClassNotFoundException e) {
			System.err.print("CLASS NOT FOUND");
		}
		
		
	}
	@Override
	public void deletePatientAnswer(String pid, int qid) throws SQLException {
		
		
		connection = null;
		statement = null;
		@SuppressWarnings("unused")
		int x;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:src\\doctorDB.db");
			statement = connection.createStatement();

			queryForSQL = "DELETE FROM feedback "
					+ "WHERE feedback.PatientID = '" + pid + "' AND feedback.QuestionID / 100 = " + qid;
		

			x = statement.executeUpdate(queryForSQL);

		} catch (ClassNotFoundException e) {
			System.err.print("CLASS NOT FOUND");
		}
	}

	
	@Override
	public ArrayList<String> getQuestionnaireInfoForFinalData() throws SQLException {
		connection = null;
		statement = null;
		result = null;
		ArrayList<String> info = new ArrayList<String>();

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:src\\doctorDB.db");
			statement = connection.createStatement();

			queryForSQL = "SELECT Name , questionnaireID " +
					" FROM questionnaire";

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
	@Override
	public ArrayList<String> getPatientsInfoForFinalData(int qID)throws SQLException {
		connection = null;
		statement = null;
		result = null;
		ArrayList<String> info = new ArrayList<String>();
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:src\\modifiedAnswers.db");
			statement = connection.createStatement();
			
			
			queryForSQL = "SELECT PatientID "
					+ "FROM  finalData "
					+ "WHERE QuestionnaireID =" + qID + " GROUP BY PatientID";				

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