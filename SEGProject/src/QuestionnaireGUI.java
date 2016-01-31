import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class QuestionnaireGUI extends JFrame {

	private JPanel back, top, middle, bottom, bottomU, bottomD;
	private JList<String> QuestionList;
	private static DefaultListModel<String> modelList;
	private JButton add, edit, remove, submit;
	private JLabel questionnaireNameLabel;
	private JFrame questionFrame;
	private static LinkedList<Question> questionLinkedList;
	private int questionnaireID;
	private String questionnaireName;

	public QuestionnaireGUI(int questionnaireID, String questionnaireName) {

		this.questionnaireName = questionnaireName;
		this.questionnaireID = questionnaireID;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(500, 500);
		setLocationRelativeTo(null);
		initialiseGUI();
		setResizable(false);
		setTitle("New Questionnaire");
		setVisible(true);

	}

	public void initialiseGUI() {
		
		BufferedImage ic = null;
		try {
			ic = ImageIO.read(this.getClass().getResource("res/nhs-logo-image-1-296169897.jpg"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setIconImage(ic);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if (questionFrame != null) {
					questionFrame.dispose();
				}
			}
		});

		back = new JPanel();
		back.setBackground(new Color(250, 250, 250));
		top = new JPanel();
		top.setBackground(new Color(240, 240, 240));
		middle = new JPanel();
		bottom = new JPanel();
		bottom.setBackground(new Color(240, 240, 240));

		add(back);
		back.setLayout(new BorderLayout());
		back.add(top, BorderLayout.NORTH);
		back.add(middle, BorderLayout.CENTER);
		back.add(bottom, BorderLayout.SOUTH);

		top.setLayout(new FlowLayout());
		questionnaireNameLabel = new JLabel(questionnaireName);
		questionnaireNameLabel.setFont(questionnaireNameLabel.getFont().deriveFont(22.0f));
		top.add(questionnaireNameLabel);

		middle.setLayout(new BorderLayout());
		// String [] listData = {"1","2","3","4","5"};
		modelList = new DefaultListModel<String>();
		QuestionList = new JList<String>(modelList);
		QuestionList.clearSelection();
		QuestionList.setFixedCellHeight(30);

		middle.add(QuestionList);

		bottom.setLayout(new BoxLayout(bottom, BoxLayout.PAGE_AXIS));
		bottomU = new JPanel();
		bottomU.setLayout(new FlowLayout());

		bottomD = new JPanel();

		add = new JButton("New Question");
		edit = new JButton("Edit Question");
		remove = new JButton("Remove Question");
		submit = new JButton("Submit Questionnaire");

		bottomU.add(add);
		bottomU.add(edit);
		bottomU.add(remove);
		bottomD.add(submit);

		bottom.add(bottomU);
		bottom.add(bottomD);
		;

		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				questionFrame = new QInterfaceExtended();

			}
		});

		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!QuestionList.isSelectionEmpty()) {
					questionFrame = new QInterfaceExtended(QuestionList
							.getSelectedValue());
				} else {
					JOptionPane.showMessageDialog(null,
							"Select a Question first");
				}

			}
		});

		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (!QuestionList.isSelectionEmpty()) {
					int index = QuestionList.getSelectedIndex();
					questionLinkedList.remove(index);
					modelList.clear();
					setList(questionLinkedList);
				} else {
					JOptionPane.showMessageDialog(null,
							"Select a Question first");
				}

			}

		});

		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (!(questionLinkedList == null)) {
					if (!questionLinkedList.isEmpty()) {

						ArrayList<String> choices = new ArrayList<String>();
						choices.add("");
						choices.add("");
						choices.add("");
						choices.add("");
						choices.add("");

						for (Question q : questionLinkedList) {

							choices.set(0, "");
							choices.set(1, "");
							choices.set(2, "");
							choices.set(3, "");
							choices.set(4, "");

							int i = 0;
							ArrayList<String> t = q.getQChoices();
							for (String c : t) {

								choices.set(i, c);
								i++;
							}

							int questionID;
							if (questionLinkedList.indexOf(q) < 10) {

								questionID = Integer.parseInt(Integer
										.toString(questionnaireID)
										+ "0"
										+ Integer.toString(questionLinkedList
												.indexOf(q)));
							} else {
								questionID = Integer.parseInt(Integer
										.toString(questionnaireID)
										+ Integer.toString(questionLinkedList
												.indexOf(q)));

							}

							AddQuestion addQInfoObject = new AddQuestion(
									questionID, questionnaireID, q.getQText(),
									choices.get(0), choices.get(1), choices
											.get(2), choices.get(3), choices
											.get(4), q.getType());
							// The integers here need to be given unique IDs!
							try {

								addQInfoObject.addQuestionInfo();

							} catch (SQLException e1) {
								e1.printStackTrace();
							}
							dispose();

						}

					} else {
						JOptionPane
								.showMessageDialog(null,
										"Please add some questions to the questionnaire before submitting");
					}

				} else {
					JOptionPane
							.showMessageDialog(null,
									"Please add some questions to the questionnaire before submitting");
				}
			}

		});

	}

	public static void setList(LinkedList<Question> questionLinkedList) {

		modelList.clear();

		QuestionnaireGUI.questionLinkedList = questionLinkedList; // necessary
		// to be
		// able to
		// remove
		// questions

		for (Question q : questionLinkedList) {
			String singleQuestion = (questionLinkedList.indexOf(q) + 1) + "- "
					+ q.getQText() + ":			";
			if ((q.getType().equals("Multiple Choice")
					|| q.getType().equals("Single Choice") || q.getType()
					.equals("Numerical")) && !q.getQChoices().isEmpty()) {

				ArrayList<String> questionChoices = q.getQChoices();

				for (String m : questionChoices) {
					singleQuestion += "(	." + (questionChoices.indexOf(m) + 1)
							+ ". " + m + " )";
				}
			}
			modelList.addElement(singleQuestion.toString());
		}

	}

}
