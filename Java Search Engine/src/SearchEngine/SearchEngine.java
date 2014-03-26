package SearchEngine;

public class SearchEngine
{
	// Global Variables
	static SearchFrame searchFrame;
	static FileFrame fileFrame;
	static SearchClass searchClass;
	
	
	
	// JOptionPane.showMessageDialog(null, currentDirectory);
	
	
	public static void main(String[] args)
	{
		// Initialize the classes
		searchFrame = new SearchFrame();
		fileFrame = new FileFrame();
		searchClass = new SearchClass();
		
		
		
		// Import References
		searchFrame.ImportRefrences(searchClass,  fileFrame);
		fileFrame.ImportRefrences(searchClass, searchFrame);
		searchClass.ImportRefrences(searchFrame, fileFrame);
		
		
		
		// Import Index
		searchClass.ImportIndex();
		
		
		// Show the Search Frame
		searchFrame.setVisible(true);
	}
	
	public static void ExitProgram()
	{
		searchFrame = null;
		fileFrame = null;
		searchClass = null;
		
		System.exit(0);
	}
	
}
