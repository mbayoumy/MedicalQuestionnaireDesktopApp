import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONGenerator {
	

	private Connection connection;
	private Statement statement;
	private ResultSet result;
	private JSONArray main;
	
	
	
	@SuppressWarnings("unchecked")
	public String generateQuestions(String str){
		
		
		connection = null;
		statement = null;
		result = null;
		ArrayList<String> info = new ArrayList<String>();
		
		

		try {
			
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:src\\doctorDB.db");
			statement = connection.createStatement();
			String queryForSQL = "SELECT QuestionID, question.QuestionnaireID, Questiontext, CH1, CH2, CH3, CH4, CH5, QuestionType "
					+ "FROM question JOIN questionnaire ON question.QuestionnaireID = questionnaire.questionnaireID "
					+ "WHERE Name = '"+ str + "'" ;
			result = statement.executeQuery(queryForSQL);
			
			
			int columns = result.getMetaData().getColumnCount();

			while (result.next()) {
				for (int i = 1; i <= columns; i++) {
					info.add(result.getString(i));
				}
			}
			
			
			main = new JSONArray();
			
			for(int i =0; i<info.size(); i++){
				
				JSONObject question = new JSONObject();
				
				question.put("QuestionID",( info.get(i)));
				question.put("QuestionnaireID",(info.get(++i)));
				question.put("QuestionnaireText", info.get(++i));
				question.put("ch1", info.get(++i));
				question.put("ch2", info.get(++i));
				question.put("ch3", info.get(++i));
				question.put("ch4", info.get(++i));
				question.put("ch5", info.get(++i));
				question.put("QuestionType", info.get(++i));
				
				main.add(question);
						
			}
			System.out.println(main);
				
		}catch (Exception e) {
			// TODO: handle exception
		}
		return main.toString();
		
		
	}
	
	

	
	
}
	
	
	
	
	
