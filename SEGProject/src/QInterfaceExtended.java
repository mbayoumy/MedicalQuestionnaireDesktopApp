import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;




@SuppressWarnings("serial")
public class QInterfaceExtended extends JFrame{
	
	
	
	private JPanel mainPanel, ROW_one , ROW_two, ROW_three, ROW_four, ROW_five;
	private JLabel questionLabel, typeLabel, nOfChoices;
	private JTextArea questionTextArea;
	private JScrollPane jsp;
	private JTextField choice_one, choice_two, choice_three, choice_four,choice_five;
	private JComboBox<String> typeOfQuestion,comboBoxChoices;
	private JButton addTheQuestion;
	private static LinkedList<Question> listOfQuestionsForQ = new LinkedList<Question>();
	private boolean editQuestionClicked;
	private String editQuestionString;
	public int i = 0;

	
	
	public QInterfaceExtended(){
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(500, 500);
		setLocationRelativeTo(null);
		editQuestionClicked = false;
		initialiseGUI();
		pack();
		setResizable(false);
		setVisible(true);
		
	}
	public QInterfaceExtended(String editQuestionString ){
	
		this.editQuestionString = editQuestionString;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(500, 500);
		setLocationRelativeTo(null);
		editQuestionClicked = true;
		initialiseGUI();
		pack();
		setVisible(true);
		
	}
	
	
	public void initialiseGUI(){
		
		BufferedImage ic = null;
		try {
			ic = ImageIO.read(this.getClass().getResource("res/nhs-logo-image-1-296169897.jpg"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setIconImage(ic);
		
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent e) {

				if (editQuestionClicked == true){
					for(Question q: listOfQuestionsForQ){
						
						String justQuestionText = editQuestionString.substring(3,
								editQuestionString.indexOf(':'));
						
						if(q.getQText().equals(justQuestionText)){
							fillWidgets(q);
						}
					}				
				}			
			}

		});
		
		mainPanel = new JPanel();
		ROW_one = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ROW_two = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ROW_three = new JPanel(new FlowLayout());
		ROW_four = new JPanel(new FlowLayout(FlowLayout.CENTER));
		ROW_five = new JPanel(new FlowLayout());
	
		add(mainPanel);
		mainPanel.add(ROW_one);
		mainPanel.add(ROW_two);
		mainPanel.add(ROW_three);
		mainPanel.add(ROW_four);
		mainPanel.add(ROW_five);
		
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		
		
		questionLabel = new JLabel("Question:");
		typeLabel = new JLabel("Type:");
		questionTextArea = new JTextArea();
		jsp = new JScrollPane();
		//jsp.setViewportView(questionTextArea);
		typeOfQuestion = new JComboBox<String>();
		
		nOfChoices = new JLabel("Number of choices: ");
		comboBoxChoices = new JComboBox<String>();
		
		choice_one = new JTextField();
		choice_two = new JTextField();
		choice_three = new JTextField();
		choice_four = new JTextField();
		choice_five = new JTextField();
		
		addTheQuestion = new JButton("Add Question");
		
		
		
								
		questionLabel.setPreferredSize(new Dimension(300,10));
		
		ROW_one.add(questionLabel);
		ROW_one.add(typeLabel);
		
			
		jsp.setPreferredSize(new Dimension(250,50));
		questionTextArea.setWrapStyleWord(true);
		questionTextArea.setLineWrap(true);
			jsp.setViewportView(questionTextArea);	
		typeOfQuestion.setPreferredSize(new Dimension(150,20));
		typeOfQuestion.addItem("Multiple Choice");
		typeOfQuestion.addItem("Single Choice");
		typeOfQuestion.addItem("Text");
		typeOfQuestion.addItem("Date");
		typeOfQuestion.addItem("Numerical");
		typeOfQuestion.addItem("Yes/No");
		
		
		
		typeOfQuestion.addItemListener(new ItemListener() {
		
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getItem() == "Multiple Choice" || e.getItem() == "Single Choice"){
					
					ROW_three.add(nOfChoices);
					ROW_three.add(comboBoxChoices);
					ROW_four.add(choice_one);
					ROW_four.add(choice_two);
					ROW_four.add(choice_three);
					ROW_four.add(choice_four);
					ROW_four.add(choice_five);
					QInterfaceExtended.this.pack();
					choice_one.setText("");
					choice_two.setText("");
					choice_three.setText("");
					choice_four.setText("");
					choice_five.setText("");
					
					
					
					
				}
				else if (e.getItem() == "Numerical"){
					
					ROW_three.remove(nOfChoices);
					ROW_three.remove(comboBoxChoices);
					ROW_four.add(choice_one);
					ROW_four.add(choice_two);
					ROW_four.remove(choice_three);
					ROW_four.remove(choice_four);
					ROW_four.remove(choice_five);;				
					QInterfaceExtended.this.pack();
					
					choice_one.setText("Min");
					choice_two.setText("Max");
				}
				else{
					ROW_three.remove(nOfChoices);
					ROW_three.remove(comboBoxChoices);
					ROW_four.remove(choice_one);
					ROW_four.remove(choice_two);
					ROW_four.remove(choice_three);
					ROW_four.remove(choice_four);
					ROW_four.remove(choice_five);
					QInterfaceExtended.this.pack();
					
					choice_one.setText("");
					choice_two.setText("");
				}
		
			}
		
		});
		
				
		ROW_two.add(jsp);
		ROW_two.add(Box.createRigidArea(new Dimension(50,0)));
		ROW_two.add(typeOfQuestion);
		
	
	
		
		comboBoxChoices.setPreferredSize(new Dimension(150,20));
		comboBoxChoices.addItem("5");
		comboBoxChoices.addItem("4");
		comboBoxChoices.addItem("3");
		comboBoxChoices.addItem("2");
		
		
		
		comboBoxChoices.addItemListener(new ItemListener() {
		
			
			public void itemStateChanged(ItemEvent e) {
				
				if(e.getItem() == "2"){
					choice_one.setVisible(true);
					choice_two.setVisible(true);
					choice_three.setVisible(false);
					choice_four.setVisible(false);
					choice_five.setVisible(false);
					QInterfaceExtended.this.pack();
					
					
				}
				else if(e.getItem() == "3"){
					choice_one.setVisible(true);
					choice_two.setVisible(true);
					choice_three.setVisible(true);
					choice_four.setVisible(false);
					choice_five.setVisible(false);					
					QInterfaceExtended.this.pack();
				}
				else if(e.getItem() == "4"){
					choice_one.setVisible(true);
					choice_two.setVisible(true);
					choice_three.setVisible(true);
					choice_four.setVisible(true);
					choice_five.setVisible(false);	
					QInterfaceExtended.this.pack();
				}
				else{
					choice_one.setVisible(true);
					choice_two.setVisible(true);
					choice_three.setVisible(true);
					choice_four.setVisible(true);
					choice_five.setVisible(true);
					QInterfaceExtended.this.pack();
					
				}		
			}		
		});
		
		
		ROW_three.add(nOfChoices);
		ROW_three.add(Box.createRigidArea(new Dimension(10,0)));
		ROW_three.add(comboBoxChoices);
		
		
		
		choice_one.setPreferredSize(new Dimension(100,25));
		choice_two.setPreferredSize(new Dimension(100,25));
		choice_three.setPreferredSize(new Dimension(100,25));
		choice_four.setPreferredSize(new Dimension(100,25));
		choice_five.setPreferredSize(new Dimension(100,25));
		
		choice_one.setAlignmentX(CENTER_ALIGNMENT);
		choice_two.setAlignmentX(CENTER_ALIGNMENT);
		choice_three.setAlignmentX(CENTER_ALIGNMENT);
		choice_four.setAlignmentX(CENTER_ALIGNMENT);
		choice_five.setAlignmentX(CENTER_ALIGNMENT);
		
		ROW_four.add(choice_one);
		ROW_four.add(choice_two);
		ROW_four.add(choice_three);
		ROW_four.add(choice_four);
		ROW_four.add(choice_five);
		ROW_four.setAlignmentX(CENTER_ALIGNMENT);
		ROW_four.setAlignmentY(CENTER_ALIGNMENT);
		
		addTheQuestion.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				String type = typeOfQuestion.getSelectedItem().toString();
				String question = questionTextArea.getText();
				
				if(type.equals("Multiple Choice") || type.equals("Single Choice")||type.equals("Numerical")){
					
					
					
					ArrayList<String> choices = new ArrayList<String>();				
					JTextField [] choicesObjects = {choice_one,choice_two,choice_three,choice_four,
							choice_five};
					
					for(JTextField o : choicesObjects){
						if(o.isVisible() && !o.getText().equals("")){
							String singleChoice = o.getText();
							choices.add(singleChoice);
						}					
					}
					Question tempQ = new Question(question,choices,type);
					
					//if edit button is clicked it replaces the element, otherwise adds new element
					if(editQuestionClicked){
						
						int index = Integer.parseInt(editQuestionString.substring(0,1))-1;
						listOfQuestionsForQ.set(index, tempQ);
					}
					else{
						listOfQuestionsForQ.add(tempQ);	
					}
					
					i++;
					
					
					
					
				}
				else{
					Question tempQ = new Question(question,typeOfQuestion.getSelectedItem().toString());					

					if(editQuestionClicked){
						
						int index = Integer.parseInt(editQuestionString.substring(0,1))-1;
						listOfQuestionsForQ.set(index, tempQ);
					}
					else{
						listOfQuestionsForQ.add(tempQ);	
					}
									
				}
				
				QuestionnaireGUI.setList(listOfQuestionsForQ);
				dispose();
				
			}
		
		});
			
		
		
		
		
		ROW_five.add(addTheQuestion);
		
		
			
	}
	
	public void fillWidgets(Question q){
		
		
		
		if(q.getType().equals("Text")||q.getType().equals("Date")||q.getType().equals("Yes/No")){
			
			ROW_three.remove(nOfChoices);
			ROW_three.remove(comboBoxChoices);
			ROW_four.remove(choice_one);
			ROW_four.remove(choice_two);
			ROW_four.remove(choice_three);
			ROW_four.remove(choice_four);
			ROW_four.remove(choice_five);
			typeOfQuestion.setSelectedItem(q.getType());
			
			questionTextArea.setText(q.getQText());
				
		}
		else{
			
			questionTextArea.setText(q.getQText());
			typeOfQuestion.setSelectedItem(q.getType());
			
			ArrayList<String> choices = q.getQChoices();
			JTextField [] choicesObjects = {choice_one,choice_two,choice_three,choice_four,
					choice_five};
			
			//fills the textFields based on how many elements are in choices
			int index = 0;
			for(String s: choices){
				choicesObjects[index].setText(s);
				index++;
				
			}
			
			if(q.getType().equals("Multiple Choice") ||q.getType().equals("Single Choice")){				
				comboBoxChoices.setSelectedItem(Integer.toString(choices.size()));
				
			}
			else{
				ROW_three.remove(nOfChoices);
				ROW_three.remove(comboBoxChoices);
			}
			for(JTextField t: choicesObjects){
				if(t.getText().equals("")){
					ROW_four.remove(t);
				}
			}
		
			
			
			
			
		}
		
		
		
	}

}
