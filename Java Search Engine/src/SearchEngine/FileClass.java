package SearchEngine;


import java.io.File;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.UIManager;


public class FileClass 
{
	// Exit Status	Meaning
	//		0		Success
	//		1		File does not exist
	public int errorCode;
	
	public String ImportTextFile(String filepath)
	{
		File inputFile = new File(filepath);
		String fileText = "";
		
		if (inputFile.exists())
		{
			try
			{
				Scanner inputFileScanner = new Scanner(inputFile);
				
				while (inputFileScanner.hasNextLine())
				{
					fileText += inputFileScanner.nextLine() + "\n";
				}
				
				inputFileScanner.close();
				errorCode = 0;
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		else
		{
			errorCode = 1;
		}
		
		return fileText;
	}
	public void ExportToTextFile(String filepath, String filetext, boolean append)
	{
		
	}
	
	public String CreateOpenFileDialogBox(String defaultPath)
	{
		String filePath = null;
		
		
		
		// Set the look and feel
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		
		// Create the file chooser
		JFileChooser fileChooser = new JFileChooser(defaultPath);
		int fileChooserReturnValue = fileChooser.showOpenDialog(null);
		
		if (fileChooserReturnValue == JFileChooser.APPROVE_OPTION)
		{
			filePath = fileChooser.getSelectedFile().getAbsolutePath();
		}
		
		return filePath;
	}
}
