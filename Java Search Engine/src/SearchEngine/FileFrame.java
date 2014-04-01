package SearchEngine;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class FileFrame 
{
	
	
	
	// Global Variables
	JFrame fileFrame;
	JList<String> fileList;
	DefaultListModel<String> fileListModel;
	
	SearchClass searchClass;
	
	
	
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
		
		
		
		// Add Button
		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(116, 47, 89, 23);
		btnAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				AddFileToIndex();
			}
		});
		fileFrame.getContentPane().add(btnAdd);
		
		
		
		// Remove Button
		JButton btnRemove = new JButton("Remove");
		btnRemove.setBounds(321, 47, 89, 23);
		btnRemove.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				RemoveFileFromIndex();
			}
		});
		fileFrame.getContentPane().add(btnRemove);
		
		
		
		// Hide Frame Button
		JButton btnHideFrame = new JButton("Done");
		btnHideFrame.setBounds(526, 47, 89, 23);
		btnHideFrame.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				searchClass.AskToReindexFiles();
				fileFrame.setVisible(false);
			}
		});
		fileFrame.getContentPane().add(btnHideFrame);
		
		
		
		// File list
		fileListModel = new DefaultListModel<String>();
		
		fileList = new JList<String>(fileListModel);
		fileList.setFont(new Font("Tahoma", Font.BOLD, 11));
		fileList.setBounds(25, 81, 699, 269);
		fileFrame.getContentPane().add(fileList);
		
		
		
		// Set visibility
		fileFrame.setVisible(false);
	}
	
	
	
	// Add and remove files
	public void AddFileToIndex()
	{
		// Declarations
		FileClass MyFileClass = new FileClass();
		
		
		
		// Call the Open File Dialog Box on the current directory
		String filePath = MyFileClass.CreateOpenFileDialogBox(System.getProperty("user.dir") + "\\Inverted Index");
		
		
		
		// If the method succeeded or failed
		if (filePath != null && searchClass.AddFileToIndex(filePath))
		{
			MsgBox("The file has been added successfully.");
		}
		else
		{
			MsgBox("The file was not added.");
		}
		
		
		
		// Refresh the frame
		RefreshFrame();
	}
	public void RemoveFileFromIndex()
	{
		// Declarations
		int file = fileList.getSelectedIndex();
		String filePath = (String) fileList.getSelectedValue();
		
		
		
		// If the file was removed
		if (searchClass.RemoveFileFromIndex(filePath))
		{
			fileListModel.remove(file);
			MsgBox("The file has been removed.");
		}
		else
		{
			MsgBox("The file was not removed.");
		}
		
		
		
		// Refresh the frame
		RefreshFrame();
	}
	
	
	
	// Helper methods
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
	public void RefreshFrame()
	{
		// Get the index file names
		String[] filepathArray = searchClass.GetFileNames();
		
		
		
		// Clear the list
		fileListModel.clear();
		
		
		
		// Add each index file name to the list
		for (String filePath:  filepathArray)
		{
			fileListModel.addElement(filePath);
		}
	}
	public void ImportRefrences(SearchClass searchClassInput)
	{
		searchClass = searchClassInput;
	}
	private void MsgBox(String prompt)
	{
		JOptionPane.showMessageDialog(null, prompt);
	}
	
}
