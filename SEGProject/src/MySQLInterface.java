import java.sql.SQLException;
import java.util.ArrayList;


public abstract class MySQLInterface {

	//http://www.tutorialspoint.com/sqlite/sqlite_java.htm
	//similar style to adaptors.
	public ArrayList<String>  getQuestionnaires() throws SQLException{return null;}
	public void addQuestionInfo() throws SQLException{ String  dummy =null; }
	public void getQuestionInfo()throws SQLException{ String  dummy =null; }
	public ArrayList<String> getUserInfo()throws SQLException{return null;}
	public void addUserInfo()throws SQLException{ String  dummy =null; }
	public void addQuestionaireInfo() throws SQLException{ String  dummy =null; }
	public ArrayList<String> getQuestionaireInfo() throws SQLException{return null;}
	public ArrayList<String> getPatients(int x) throws SQLException{return null;}
	public ArrayList<String> getQuestionsWithAnswers() throws SQLException{return null;}
	public ArrayList<String> checkIfUserAlreadyExistByGMC() throws SQLException{return null;}
	public ArrayList<String> checkIfUserAlreadyExistByUsername() throws SQLException{return null;}
	public void addfeedback() throws SQLException{ String  dummy =null; }
	public void addpatients() throws SQLException{ String  dummy =null; }
	public void deletePatientAnswer(String pid, int qid) throws SQLException{ String  dummy =null; }
	public ArrayList<String> getQuestionnaireInfoForFinalData()throws SQLException{return null;}
	public ArrayList<String> getPatientsInfoForFinalData(int x) throws SQLException{return null;}

	public ArrayList<String> getFinalData() throws SQLException{return null;}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
