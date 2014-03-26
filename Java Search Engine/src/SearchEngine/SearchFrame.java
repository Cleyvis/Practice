package SearchEngine;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class SearchFrame
{
	// Global Variables
	JFrame searchFrame;
	
	SearchClass searchClass;
	FileFrame fileFrame;
	
	
	
	// Constructor
	public SearchFrame()
	{
		// Frame
		searchFrame = new JFrame();
		searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		searchFrame.setSize(500, 400);
		searchFrame.setLocationRelativeTo(null);
		searchFrame.getContentPane().setLayout(null);
		searchFrame.setVisible(false);
		
		
		
		// Label
		JLabel lblJavaSearchEngine = new JLabel("Java Search Engine");
		lblJavaSearchEngine.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblJavaSearchEngine.setBounds(143, 11, 197, 25);
		searchFrame.getContentPane().add(lblJavaSearchEngine);
		
		
		
		// Search TextField
		JTextField searchTerms = new JTextField();
		searchTerms.setBounds(10, 47, 464, 20);
		searchFrame.getContentPane().add(searchTerms);
		searchTerms.setColumns(10);
		
		
		
		// Radio Button AND
		JRadioButton rdbtnAnd = new JRadioButton("AND");
		rdbtnAnd.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnAnd.setBounds(54, 74, 89, 23);
		searchFrame.getContentPane().add(rdbtnAnd);
		
		
		
		// Radio Button OR
		JRadioButton rdbtnOr = new JRadioButton("OR");
		rdbtnOr.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnOr.setBounds(197, 74, 89, 23);
		searchFrame.getContentPane().add(rdbtnOr);
		
		
		
		// Radio Button PHRASE
		JRadioButton rdbtnPhrase = new JRadioButton("PHRASE");
		rdbtnPhrase.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnPhrase.setBounds(340, 74, 89, 23);
		searchFrame.getContentPane().add(rdbtnPhrase);
		
		
		
		// Button Search
		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(54, 104, 89, 23);
		btnSearch.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				
			}
		});
		searchFrame.getContentPane().add(btnSearch);
		
		
		
		// Button Files
		JButton btnFiles = new JButton("Files");
		btnFiles.setBounds(197, 104, 89, 23);
		btnFiles.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				fileFrame.setVisible(true);
			}
		});
		searchFrame.getContentPane().add(btnFiles);
		
		
		
		// Button Quit
		JButton btnQuit = new JButton("Quit");
		btnQuit.setBounds(340, 104, 89, 23);
		btnQuit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				searchClass.ExitProgram();
			}
		});
		searchFrame.getContentPane().add(btnQuit);
		
		
		
		// Text Field Results
		JTextPane results = new JTextPane();
		results.setBounds(10, 138, 464, 212);
		searchFrame.getContentPane().add(results);
		
		searchFrame.setVisible(false);
	}
	
	public void setVisible(boolean visible) 
	{
		if (visible)
		{
			searchFrame.setVisible(true);
		}
		else
		{
			searchFrame.setVisible(false);
		}
	}
	
	public void ImportRefrences(SearchClass searchClassInput, FileFrame fileFrameInput)
	{
		searchClass = searchClassInput;
		fileFrame = fileFrameInput;
	}
}
