import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AddQuestion extends MySQLInterface {

	private String questionText;
	private String ch1, ch2, ch3, ch4, ch5, questionType;
	private int questionnaireID, questionID;
	private String PID ;

	public AddQuestion(int qID, int questionnareID, String qText, String c1,
			String c2, String c3, String c4, String c5, String qType) {
		this.questionID = qID;
		this.questionnaireID = questionnareID;
		this.questionText = qText;
		this.ch1 = c1;
		this.ch2 = c2;
		this.ch3 = c3;
		this.ch4 = c4;
		this.ch5 = c5;

		this.questionType = qType;

	}
	
	public AddQuestion(int questionnaireID, String PID){
		this.questionnaireID = questionnaireID;
		this.PID = PID;
		
		
	}

	@Override
	public void addQuestionInfo() throws SQLException {
		Connection connection = null;
		Statement statement = null;

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:src\\doctorDB.db");
			statement = connection.createStatement();

			String queryForSQL = "INSERT INTO question VALUES(" + ""
					+ questionID + ", " + questionnaireID + ", '"
					+ questionText + "', '" + ch1 + "', '" + ch2 + "', '" + ch3
					+ "', '" + ch4 + "', '" + ch5 + "', '" + questionType + "'"
					+ ")";
			statement.execute(queryForSQL);

		} catch (ClassNotFoundException e) {
			System.err.print("CLASS NOT FOUND");
		}

	}
	
	@Override
	public ArrayList<String> getQuestionsWithAnswers() throws SQLException {
		
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		ArrayList<String> info = new ArrayList<String>();

		
		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:src\\doctorDB.db");
			statement = connection.createStatement();
			
			String queryForSQL = "SELECT QuestionText , CH1 , CH2 , CH3 , CH4 , CH5 , Answer " +
								 "FROM question NATURAL JOIN feedback " +
								 "WHERE PatientID = '" + PID +"' AND QuestionnaireID = " + questionnaireID;
			
					
			result = statement.executeQuery(queryForSQL);

			int columns = result.getMetaData().getColumnCount();

			while (result.next()) {
				for (int i = 1; i <= columns; i++) {
					info.add(result.getString(i));
				}
			}


			
		}
		catch(ClassNotFoundException e)
		{
			System.err.print("CLASS NOT FOUND");
		}
		
		return info;
	}
	@Override
	public ArrayList<String> getFinalData() throws SQLException {
		

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		ArrayList<String> info = new ArrayList<String>();

		
		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:src\\modifiedAnswers.db");
			statement = connection.createStatement();
			
			String queryForSQL = "SELECT * FROM finalData WHERE QuestionnaireID = "+ questionnaireID + "" +
					" AND PatientID = '" + PID +"' " ;
			
					
			result = statement.executeQuery(queryForSQL);

			int columns = result.getMetaData().getColumnCount();

			while (result.next()) {
				for (int i = 1; i <= columns; i++) {
					info.add(result.getString(i));
				}
			}

		}
		catch(ClassNotFoundException e)
		{
			System.err.print("CLASS NOT FOUND");
		}
		
		return info;
		
		
	}

}
