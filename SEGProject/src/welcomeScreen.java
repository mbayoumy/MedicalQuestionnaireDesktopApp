import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class welcomeScreen extends JFrame {

	private String name;
	private JLabel title;
	private JButton create, check, logOff, openConnection, displayResu;
	private int GMCNumeber;
	private java.sql.Date SQLDate;
	

	public welcomeScreen(int GMCNumber, String fName, String lName) {
		
		this.GMCNumeber = GMCNumber;
		if(GMCNumber ==0){
			name = "Admin";		}
		else{

		name = "Dr " + fName + " " + lName;
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		setLocationRelativeTo(null);
		initialiseGUI();
		pack();
		setResizable(false);
		setTitle("Main Menu");
		setVisible(true);

	}
	public welcomeScreen() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		setLocationRelativeTo(null);
		initialiseGUI();
		pack();
		setVisible(true);

	}

	private void initialiseGUI() {
		BufferedImage ic = null;
		try {
			ic = ImageIO.read(this.getClass().getResource("res/nhs-logo-image-1-296169897.jpg"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setIconImage(ic);

		JPanel back = new JPanel();
		back.setLayout(new BorderLayout());

		title = new JLabel("Welcome " + name, SwingConstants.CENTER);
		title.setFont(new Font("Lucida Bright", Font.LAYOUT_LEFT_TO_RIGHT, 30));

		JPanel cen = new JPanel();
		cen.setLayout(new BoxLayout(cen, BoxLayout.PAGE_AXIS));

		create = new JButton("Create New Questionaire");
		create.setAlignmentX(Component.CENTER_ALIGNMENT);
		check = new JButton("Check  Questionaire Results");
		check.setAlignmentX(Component.CENTER_ALIGNMENT);
		logOff = new JButton("Log Off");
		logOff.setAlignmentX(Component.CENTER_ALIGNMENT);
		openConnection = new JButton("Connect To Android Device");
		openConnection.setAlignmentX(Component.CENTER_ALIGNMENT);
		displayResu = new JButton("View Stored Patient Feedback");
		displayResu.setAlignmentX(Component.CENTER_ALIGNMENT);

		cen.add(Box.createRigidArea(new Dimension(300, 40)));
		cen.add(create);
		cen.add(Box.createRigidArea(new Dimension(20, 20)));
		cen.add(check);
		cen.add(Box.createRigidArea(new Dimension(20, 20)));
		cen.add(displayResu);
		cen.add(Box.createRigidArea(new Dimension(20, 20)));
		cen.add(openConnection);
		cen.add(Box.createRigidArea(new Dimension(20, 20)));
		cen.add(logOff);
		cen.add(Box.createRigidArea(new Dimension(20, 20)));
		

		back.add(title, BorderLayout.NORTH);
		//back.add(create, BorderLayout.CENTER);
		//back.add(check, BorderLayout.SOUTH);
		back.add(cen, BorderLayout.CENTER);

		add(back);

		create.addActionListener(new ActionListener() {

			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent e) {

				//http://www.mkyong.com/java/java-how-to-get-current-date-time-date-and-calender/
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				java.util.Date date = new Date();
				String currentDate = dateFormat.format(date);
				
				
				try{
				SQLDate= new java.sql.Date(dateFormat.parse(currentDate).getTime());
				}
				catch (Exception e1) {
					
				}
			
				//http://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html#input	
				JOptionPane pane = new JOptionPane();
				String questionnaireName = pane.showInputDialog(null,
						"Questionnaire Name:", "Add new questionnaire",
						JOptionPane.QUESTION_MESSAGE);
				

				if(questionnaireName != null){
					if(questionnaireName.equals("")){
						 pane.showMessageDialog(null, "please enter questionnaire name to proceed");	
						
					}
					else{
					QuestionaireFunctionality addNewQuestionnaire = new QuestionaireFunctionality(
							 questionnaireName, SQLDate, GMCNumeber);
					
					try {
						addNewQuestionnaire.addQuestionaireInfo();

					} catch (SQLException e1) {					
						e1.printStackTrace();
					}
					
					
					QuestionaireFunctionality findingLastEntryIndex = new QuestionaireFunctionality();

					ArrayList<String> lastIndex = null;
					try {

						lastIndex = findingLastEntryIndex.getQuestionaireInfo();

					} catch (SQLException e1) {

						e1.printStackTrace();
					}

					int questionnaireID = lastIndex.size();
					
					new QuestionnaireGUI(questionnaireID,questionnaireName);
				}
				}
				
				
			}

		});
		
		check.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				new QuestionnaireChecker();
				
				
			}
		} );

		logOff.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//clear user information.
				LoginExtended.clearDoctorInfo();
				new LoginExtended();
				dispose();
			}
		
		});
		
		openConnection.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {			
				new Server();
			}
		
		});
		
		displayResu.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {

					new QuestionnaireChecker2();

			}
		
		});
	}

}
