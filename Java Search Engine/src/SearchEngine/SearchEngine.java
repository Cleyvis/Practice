package SearchEngine;


public class SearchEngine
{
	
	
	
	// Global Variables
	static SearchFrame searchFrame;
	static FileFrame fileFrame;
	static SearchClass searchClass;
	
	
	
	public static void main(String[] args)
	{
		// Initialize the classes
		searchFrame = new SearchFrame();
		fileFrame = new FileFrame();
		searchClass = new SearchClass();
		
		
		
		// Import references to the other classes
		searchFrame.ImportRefrences(searchClass,  fileFrame);
		fileFrame.ImportRefrences(searchClass);
		
		
		
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
