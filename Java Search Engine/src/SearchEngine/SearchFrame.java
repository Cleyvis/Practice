package SearchEngine;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;



public class SearchFrame
{
	// Enumerations
	enum SearchBy
	{
		AND,
		OR,
		PHRASE;
	}
	
	
	
	// Global Variables
	JFrame searchFrame;
	JTextField searchTermsJTextField;
	JTextPane resultsJTextPane;
	
	JRadioButton rdbtnAnd;
	JRadioButton rdbtnOr;
	JRadioButton rdbtnPhrase;
	
	SearchClass searchClass;
	FileFrame fileFrame;
	
	
	
	// Constructor
	public SearchFrame()
	{
		// Frame
		searchFrame = new JFrame();
		searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		searchFrame.setSize(750, 400);
		searchFrame.setLocationRelativeTo(null);
		searchFrame.getContentPane().setLayout(null);
		searchFrame.setVisible(false);
		
		
		
		// Label
		JLabel lblJavaSearchEngine = new JLabel("Java Search Engine");
		lblJavaSearchEngine.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblJavaSearchEngine.setBounds(268, 11, 197, 25);
		searchFrame.getContentPane().add(lblJavaSearchEngine);
		
		
		
		// Search TextField
		searchTermsJTextField = new JTextField();
		searchTermsJTextField.setBounds(135, 47, 464, 20);
		searchFrame.getContentPane().add(searchTermsJTextField);
		searchTermsJTextField.setColumns(10);
		
		
		
		// Radio Button AND
		rdbtnAnd = new JRadioButton("AND");
		rdbtnAnd.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				rdbtnAnd.setSelected(true);
				rdbtnOr.setSelected(false);
				rdbtnPhrase.setSelected(false);
			}
		});
		rdbtnAnd.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnAnd.setBounds(116, 74, 89, 23);
		searchFrame.getContentPane().add(rdbtnAnd);
		
		
		
		// Radio Button OR
		rdbtnOr = new JRadioButton("OR");
		rdbtnOr.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				rdbtnAnd.setSelected(false);
				rdbtnOr.setSelected(true);
				rdbtnPhrase.setSelected(false);
			}
		});
		rdbtnOr.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnOr.setBounds(321, 74, 89, 23);
		searchFrame.getContentPane().add(rdbtnOr);
		
		
		
		// Radio Button PHRASE
		rdbtnPhrase = new JRadioButton("PHRASE");
		rdbtnPhrase.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				rdbtnAnd.setSelected(false);
				rdbtnOr.setSelected(false);
				rdbtnPhrase.setSelected(true);
			}
		});
		rdbtnPhrase.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnPhrase.setBounds(526, 74, 89, 23);
		searchFrame.getContentPane().add(rdbtnPhrase);
		
		
		
		// Button Search
		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(116, 104, 89, 23);
		btnSearch.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if (rdbtnAnd.isSelected())
				{
					Search(SearchBy.AND);
				}
				
				if (rdbtnOr.isSelected())
				{
					Search(SearchBy.OR);
				}
				
				if (rdbtnPhrase.isSelected())
				{
					Search(SearchBy.PHRASE);
				}
			}
		});
		searchFrame.getContentPane().add(btnSearch);
		
		
		
		// Button Files
		JButton btnFiles = new JButton("Files");
		btnFiles.setBounds(321, 104, 89, 23);
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
		btnQuit.setBounds(526, 104, 89, 23);
		btnQuit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				searchClass.ExitProgram();
			}
		});
		searchFrame.getContentPane().add(btnQuit);
		
		
		
		// Text Field Results
		resultsJTextPane = new JTextPane();
		resultsJTextPane.setFont(new Font("Tahoma", Font.BOLD, 11));
		resultsJTextPane.setBounds(10, 138, 714, 212);
		searchFrame.getContentPane().add(resultsJTextPane);
		
		searchFrame.setVisible(false);
	}
	
	
	
	// Search Subs
	private void Search(SearchBy searchMethod)
	{
		ArrayList<String> files = new ArrayList<String>();
		String results;
		
		if (searchMethod == SearchBy.AND)
		{
			files = searchClass.SearchByAND(searchTermsJTextField.getText().toLowerCase().replaceAll("[!?,.]", "").split("\\s+"));
		}
		
		if (searchMethod == SearchBy.OR)
		{
			files = searchClass.SearchByOR(searchTermsJTextField.getText().toLowerCase().replaceAll("[!?,.]", "").split("\\s+"));
		}
		
		if (searchMethod == SearchBy.PHRASE)
		{
			files = searchClass.SearchByPHRASE(searchTermsJTextField.getText().toLowerCase().replaceAll("[!?,.]", "").split("\\s+"));
		}
		
		
		
		if (files != null && files.size() != 0)
		{
			results = "";
			for (String file:  files)
			{
				results += file + "\n";
			}
			resultsJTextPane.setText(results);
		}
		else
		{
			resultsJTextPane.setText("No matches found");
		}
	}
	
	
	
	// Helper Subs
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
