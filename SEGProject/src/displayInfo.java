import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class displayInfo extends JFrame {

	private int qID;
	private ArrayList<String> infoList;
	private String qText, c1, c2, c3, c4, c5, ans, pID;
	private JPanel backGround, middle, bottom, temp;
	private JButton save;
	private JScrollPane jsp;
	private boolean isEditable = false;
	private TreeMap<String, String> modifiedAnswers;
	private JTextField[] jta;

	public displayInfo(int qID, String pID) {
		this.qID = qID;
		this.pID = pID;
		pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(1000, 450);
		setLocationRelativeTo(null);
		initialiseGUI();
		setResizable(false);
		setTitle("Correct Results");
		setVisible(true);
	}

	private void initialiseGUI() {
		BufferedImage ic = null;
		try {
			ic = ImageIO.read(this.getClass().getResource(
					"res/nhs-logo-image-1-296169897.jpg"));

		} catch (IOException e) {
			e.printStackTrace();
		}

		setIconImage(ic);

		AddQuestion QwithAnswers = new AddQuestion(qID, pID);

		try {
			infoList = QwithAnswers.getQuestionsWithAnswers();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		jsp = new JScrollPane();

		backGround = new JPanel();
		backGround.setLayout(new BorderLayout());
		jsp.setViewportView(backGround);

		middle = new JPanel();
		bottom = new JPanel();

		middle.setLayout(new GridLayout((infoList.size() / 7) + 1, 3));
		middle.add(new JLabel(
				"<html><center>Question & Possible Solutions</center></html>"));
		middle.add(new JLabel(
				"<html><center>Patient's Solution</center></html>"));
		middle.add(new JLabel("<html><center>Modify</center></html>"));
		jta = new JTextField[infoList.size() / 7];

		modifiedAnswers = new TreeMap<String, String>();
		int j = 0;
		for (int i = 0; i < infoList.size(); i++) {
			final JLabel qLabel;
			final JTextField ansLabel;
			JButton change;
			JScrollPane jspQ = new JScrollPane();
			JScrollPane jspA = new JScrollPane();
			temp = new JPanel();
			temp.setLayout(new BorderLayout());

			qText = infoList.get(i);
			c1 = infoList.get(++i);
			c2 = infoList.get(++i);
			c3 = infoList.get(++i);
			c4 = infoList.get(++i);
			c5 = infoList.get(++i);
			ans = infoList.get(++i);

			qLabel = new JLabel();

			qLabel.setText("<html>" + qText + "<br><br><i>" + c1 + "   " + c2
					+ "   " + c3 + "   " + c4 + "   " + c5 + "</i><br></html>");

			jspQ.setViewportView(qLabel);
			ansLabel = new JTextField(ans);
			jta[j] = ansLabel;
			j++;
			ansLabel.setEditable(false);
			jspA.setViewportView(ansLabel);
			change = new JButton("Modify Answer");
			temp.add(new JLabel("   "), BorderLayout.NORTH);
			temp.add(change, BorderLayout.CENTER);
			temp.add(new JLabel("   "), BorderLayout.SOUTH);
			middle.add(jspQ);
			middle.add(jspA);
			middle.add(temp);

			final String question = qText + ":(" + c1 + " ,  " + c2 + " ,  "
					+ c3 + " ,  " + c4 + " ,  " + c5 + ")";
			modifiedAnswers.put(question, ansLabel.getText());
			change.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (!ansLabel.isEditable()) {
						ansLabel.setEditable(true);
						ansLabel.setBackground(Color.YELLOW);

					} else {
						ansLabel.setEditable(false);
						modifiedAnswers.put(question, ansLabel.getText());
						ansLabel.setBackground(UIManager
								.getColor("Panel.background"));
					}

				}
			});

		}

		save = new JButton("Save");

		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				for (JTextField a : jta) {
					if (a.isEditable() == true) {
						isEditable = true;
						JOptionPane.showMessageDialog(null,
								"Please Finish Modifing Questions");
						return;
					} else {
						isEditable = false;
					}
				}
				if (isEditable == false) {
					int reply = JOptionPane.showConfirmDialog(null,
							"Are you sure? You can only modify answers once",
							"Are you sure?", JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION) {

						Connection connection;
						Statement statement;

						String doctorF = LoginExtended.doctorInfo().get(1);
						String doctorL = LoginExtended.doctorInfo().get(2);
						int GMC = Integer.parseInt(LoginExtended.doctorInfo()
								.get(0));

						try {
							Class.forName("org.sqlite.JDBC");
							connection = DriverManager
									.getConnection("jdbc:sqlite:src\\modifiedAnswers.db");
							statement = connection.createStatement();

							Set<String> iterator = modifiedAnswers.keySet();

							for (String sa : iterator) {

								String SQL = "INSERT INTO finalData (FirstName, LastName, GMC, PatientID,QuestionnaireID,Question,Answer) "
										+ " VALUES ";
								SQL += "( '" + doctorF + "' , '" + doctorL
										+ "' , " + GMC + ",'" + pID + "' ,"
										+ qID + ", '" + sa + "' , '"
										+ modifiedAnswers.get(sa) + "' )";

								statement.execute(SQL);

							}

						} catch (Exception e1) {
							System.err.print("CLASS NOT FOUND");
						}

						try {
							new FeedbackFunctionality().deletePatientAnswer(
									pID, qID);
						} catch (SQLException e1) {
						}

						dispose();
					}
					else{}
				}

			}

		});
		bottom.add(save);

		backGround.add(middle, BorderLayout.CENTER);
		backGround.add(bottom, BorderLayout.SOUTH);
		add(jsp);

	}
}
