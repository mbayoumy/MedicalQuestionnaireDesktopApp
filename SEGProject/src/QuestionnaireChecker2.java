import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class QuestionnaireChecker2 extends JFrame {

	private JPanel backGround, north, mid, south;
	private JLabel info;
	private JList<String> questionnaireList, patientList;
	private JButton checkRes;
	private static DefaultListModel<String> qListModel, pListModel;
	private ArrayList<String>  pArray;
	private HashMap<String, Integer> qArray;
	private JScrollPane lspQ, lspP;
	private FeedbackFunctionality obj;

	public QuestionnaireChecker2() {
		setSize(600, 500);
		setLocationRelativeTo(null);
		initialiseGUI();
		setResizable(false);
		setTitle("Selection Screen");
		setVisible(true);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialiseGUI() {
		BufferedImage ic = null;
		try {
			ic = ImageIO.read(this.getClass().getResource("res/nhs-logo-image-1-296169897.jpg"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setIconImage(ic);

		obj = new FeedbackFunctionality();
		try {
			qArray = new HashMap<String, Integer>();
			ArrayList<String> listOfbothIDAndName = obj.getQuestionnaireInfoForFinalData();
			for(int i = 0; i< listOfbothIDAndName.size(); i++){
				qArray.put(listOfbothIDAndName.get(i), Integer.parseInt(listOfbothIDAndName.get(++i)));
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		backGround = new JPanel();
		backGround.setLayout(new BorderLayout());

		north = new JPanel();
		info = new JLabel();
		info.setText("Please choose a questionnaire: ");
		north.add(info);

		mid = new JPanel();
		mid.setBorder(new EmptyBorder(10, 10, 10, 10));
		mid.setLayout(new BorderLayout());
		qListModel = new DefaultListModel<String>();
		Set<String> onlyName = qArray.keySet();
		int i = 0;
		for(String name: onlyName){
			qListModel.add(i, name);
			i++;
		}
		
		lspQ = new JScrollPane();
		lspP = new JScrollPane();
		questionnaireList = new JList(qListModel);
		pListModel = new DefaultListModel<String>();
		patientList = new JList(pListModel);
		questionnaireList.setFixedCellHeight(30);
		questionnaireList.setFixedCellWidth(230);
		patientList.setFixedCellHeight(30);
		patientList.setFixedCellWidth(230);
		lspQ.setViewportView(questionnaireList);
		lspP.setViewportView(patientList);
		questionnaireList.setVisible(true);
		mid.add(lspQ, BorderLayout.WEST);
		mid.add(lspP, BorderLayout.EAST);

		south = new JPanel();
		south.setLayout(new FlowLayout());
		checkRes = new JButton("View Results");
		
		south.add(checkRes);

		backGround.add(north, BorderLayout.NORTH);
		backGround.add(mid, BorderLayout.CENTER);
		backGround.add(south, BorderLayout.SOUTH);
		add(backGround);

		checkRes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (questionnaireList.getSelectedIndex() > -1) {
					if (patientList.getSelectedIndex() > -1) {
						new displayFinalInfo(qArray.get(questionnaireList.getSelectedValue()), patientList.getSelectedValue());
						dispose();
					}
				} else {
					

				}
				

			}
		});

		questionnaireList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				pListModel.clear();
				if (!arg0.getValueIsAdjusting()) {

					try {
						pArray = obj.getPatientsInfoForFinalData(qArray.get(questionnaireList.getSelectedValue()));
					} catch (SQLException e) {
						e.printStackTrace();
					}

					for (int i = 0; i < pArray.size(); i++) {
						pListModel.add(i, pArray.get(i));

					}

				}
			}
		});

	}
}
