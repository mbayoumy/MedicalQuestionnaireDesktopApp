import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class displayFinalInfo extends JFrame implements Printable {

	private int qID;
	private ArrayList<String> infoList;
	private String qText, c1, c2, c3, c4, c5, ans, pID;
	private JPanel backGround, middle, bottom,temp;
	private JButton save;
	private JScrollPane jsp;
	
	private TreeMap<String	, String> modifiedAnswers;


	public displayFinalInfo(int qID, String pID) {
		this.qID = qID;
		this.pID = pID;
		pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(1000, 450);
		setLocationRelativeTo(null);
		initialiseGUI();
		setResizable(false);
		setTitle("Display Results");
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
		
		AddQuestion QwithAnswers = new AddQuestion(qID, pID);
		
		try{
			infoList = QwithAnswers.getFinalData();
		}catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		jsp = new JScrollPane();
		

		backGround = new JPanel();
		backGround.setLayout(new BorderLayout());
		jsp.setViewportView(backGround);
		
		middle = new JPanel();
		bottom = new JPanel();
		save = new JButton ("Print");
		bottom.add(save);
		
		middle.setLayout(new GridLayout((infoList.size()/7 )+1, 2));
		middle.add(new JLabel("<html><center>Question & Possible Solutions</center></html>"));
		middle.add(new JLabel("<html><center>Patient's Solution</center></html>"));
		
		
		modifiedAnswers = new TreeMap<String, String>();
		
		for(int i =0; i<infoList.size(); i++){
			final JLabel qLabel;
			final JTextField ansLabel;
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
			 
			
			qLabel.setText("<html>" + "Dr. " + qText + " " + c1 + ", GMC: " + c2 + "<br><br>" + "   " + "   "  + c5   + "</i><br></html>" );
			
			jspQ.setViewportView(qLabel);
			ansLabel = new JTextField(ans);
			ansLabel.setEditable(false);
			jspA.setViewportView(ansLabel);
			middle.add(jspQ);
			middle.add(jspA);
			
			final String question =  qText+ ":(" + c1 + " ,  " + c2 + " ,  "+ c3 + " ,  " + c4 + " ,  "  + c5 +")";
			modifiedAnswers.put(question, ansLabel.getText());

				
		}

		save.addActionListener(new ActionListener() {
			//http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/2d/printing/examples/PrintUIWindow.java
			//http://stackoverflow.com/questions/12764634/printing-a-jframe-and-its-components
			//printing source code used from stack overflow to print all contents of a jframe
			//based on the tutorial provided by oracle
			@Override
			public void actionPerformed(ActionEvent arg0) {
				PrinterJob pj = PrinterJob.getPrinterJob();
				PageFormat preformat = pj.defaultPage();
				preformat.setOrientation(PageFormat.LANDSCAPE);
				PageFormat postformat = pj.pageDialog(preformat);
				if (preformat != postformat) {
				    pj.setPrintable(displayFinalInfo.this, postformat);
				    if (pj.printDialog()) {
				        try {
							pj.print();
						} catch (PrinterException e) {
							e.printStackTrace();
						}
				    }
				}
			    
			}
		});
		
		backGround.add(middle, BorderLayout.CENTER);
		backGround.add(bottom, BorderLayout.SOUTH);
		add(jsp);
		
		
		
	}
	//http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/2d/printing/examples/PrintUIWindow.java
	//http://stackoverflow.com/questions/12764634/printing-a-jframe-and-its-components
	//print meathod paints the screen for the printer, sets background white
	//ensures all fits on the page and returns a printable to the action performed meathod
	public int print(Graphics g, PageFormat format, int page_index) 
            throws PrinterException {
        if (page_index > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        Dimension dim = this.getSize();
        double cHeight = dim.getHeight();
        double cWidth = dim.getWidth();

        double pHeight = format.getImageableHeight();
        double pWidth = format.getImageableWidth();

        double pXStart = format.getImageableX();
        double pYStart = format.getImageableY();

        double xRatio = pWidth / cWidth;
        double yRatio = pHeight / cHeight;


        Graphics2D g2 = (Graphics2D) g;
        g2.setBackground(Color.WHITE);
        g2.translate(pXStart, pYStart);
        g2.scale(xRatio, yRatio);
        this.paint(g2);

        return Printable.PAGE_EXISTS;
    }
	
}
