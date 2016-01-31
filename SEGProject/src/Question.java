import java.util.ArrayList;


public class Question {
	
	private String QText,QType;
	private ArrayList<String> QChoices;
	
	// if its a normal text question
	Question (String QText){
		this.QText = QText;
		QType = "Text";
	}
	Question (String QText, String QType){
		this.QText = QText;
		this.QType = QType;
	}
	
	// if the question type is multiple choice 
	Question(String QText,ArrayList<String> QChoices,String QType){
		this.QText = QText;	
		this.QChoices = QChoices;
		if(QChoices.isEmpty()){this.QType = "Text";}
		else{this.QType = QType;}
	}
	
	public String getQText(){
		return QText;
	}
	
	public String getType(){
		return QType;
	}
	
	public ArrayList<String> getQChoices(){
		if (QType.equals("Text")||QType.equals("Date")||QType.equals("Yes/No")){
			
			ArrayList<String> empty = new ArrayList<String>();
			empty.add("");
			empty.add("");
			empty.add("");
			empty.add("");
			empty.add("");
			return empty;
		}
		else{
			return QChoices;
		}
	}
	
	@Override
	public String toString() {
		if(QType.equals("Text")||QType.equals("Date")||QType.equals("Yes/No")){
			return "Type: " + QType +"\n" +"Question: "+ QText +": \n";
		}
		else{
		String result = "Type: " + QType +"\n" +"Question: "+ QText +" \n";
		
		for(String e : QChoices){
			result += "(" + e + " )";		
		}
		
		return result;
		}
	}

}
