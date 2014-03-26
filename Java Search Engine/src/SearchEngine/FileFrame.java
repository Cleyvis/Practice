package SearchEngine;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;



public class FileFrame 
{
	// Global Variables
	JFrame fileFrame;
	JList fileList;
	DefaultListModel fileListModel;
	
	SearchClass searchClass;
	SearchFrame searchFrame;
	
	
	
	// Constructor
	public FileFrame()
	{
		// Frame
		fileFrame = new JFrame();
		fileFrame.setSize(750,400);
		fileFrame.setLocationRelativeTo(null);
		fileFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fileFrame.getContentPane().setLayout(null);
		fileFrame.setVisible(false);
		
		
		
		// Label
		JLabel lblFiles = new JLabel("Files");
		lblFiles.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblFiles.setBounds(344, 11, 46, 25);
		fileFrame.getContentPane().add(lblFiles);
		
		
		
		// Button Add
		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(25, 47, 89, 23);
		btnAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				AddFileToIndex();
			}
		});
		fileFrame.getContentPane().add(btnAdd);
		
		
		
		// Button Remove
		JButton btnRemove = new JButton("Remove");
		btnRemove.setBounds(139, 47, 89, 23);
		btnRemove.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				RemoveFileFromIndex();
			}
		});
		fileFrame.getContentPane().add(btnRemove);
		
		
		
		// Button Reindex
		JButton btnReindex = new JButton("Reindex");
		btnReindex.setBounds(253, 47, 89, 23);
		btnReindex.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				// searchClass.AskToReindexFiles();
			}
		});
		fileFrame.getContentPane().add(btnReindex);
		
		
		
		// Button Quit
		JButton btnQuit_1 = new JButton("Quit");
		btnQuit_1.setBounds(367, 47, 89, 23);
		btnQuit_1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				// searchClass.AskToReindexFiles();
				fileFrame.setVisible(false);
			}
		});
		fileFrame.getContentPane().add(btnQuit_1);
		
		
		
		// File list
		fileListModel = new DefaultListModel();
		
		fileList = new JList(fileListModel);
		fileList.setBounds(25, 81, 699, 269);
		fileFrame.getContentPane().add(fileList);
		
		
		
		// Set visibility
		fileFrame.setVisible(false);
	}
	
	public void setVisible(Boolean visible)
	{
		if (visible)
		{
			RefreshFrame();
			fileFrame.setVisible(true);
		}
		else
		{
			fileFrame.setVisible(false);
		}
	}
	
	public void AddFileToIndex()
	{
		searchClass.AddFileToIndex();
		RefreshFrame();
	}
	
	public void RemoveFileFromIndex()
	{
		int file = fileList.getSelectedIndex();
		String filePath = (String)fileList.getSelectedValue();
		
		if (searchClass.RemoveFileFromIndex(filePath))
		{
			fileListModel.remove(file);
			JOptionPane.showMessageDialog(null, "The file has been removed.");
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Error:  the file has not been removed.");
		}
	}
	
	public void RefreshFrame()
	{
		String[] filepathArray = searchClass.GetFileNames();
		fileListModel.clear();
		for (int i = 0; i < filepathArray.length; i++)
		{
			fileListModel.addElement(filepathArray[i]);
		}
	}
	
	public void ImportRefrences(SearchClass searchClassInput, SearchFrame searchFrameInput)
	{
		searchClass = searchClassInput;
		searchFrame = searchFrameInput;
	}
}
