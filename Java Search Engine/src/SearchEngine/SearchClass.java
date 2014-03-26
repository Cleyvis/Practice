package SearchEngine;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.*;


public class SearchClass 
{
	public enum SearchBy
	{
		AND,
		OR,
		PHRASE
	}
	
	
	
	// Global Variables
	Long indexLastBuilt;
	boolean indexOutOfDate;
	SearchFrame searchFrame;
	FileFrame fileFrame;
	Map<Integer, String> files = new HashMap<Integer, String>();
	Map<String, ArrayList<Long>> words = new HashMap<String, ArrayList<Long>>();
	String currentDirectory = System.getProperty("user.dir");
	boolean newIndex;
	
	
	
	public boolean AddFileToIndex()
	{
		boolean added;
		String filePath;
		FileClass MyFileClass = new FileClass();
		
		
		
		// Call the Open File Dialog Box
		filePath = MyFileClass.CreateOpenFileDialogBox(currentDirectory + "\\Inverted Index");
		
		
				
		// If the file has not already been added
		if (filePath == null | !files.containsValue(filePath))
		{
			// Then add it to the hash map
			files.put(GetNextAvailableIndex(),  filePath);
			
			// The inverted index is now out of date
			// Throw a flag to rebuild it
			indexOutOfDate = true;
			
			// Set the return value
			added = true;
		}
		else
		{
			added = false;
		}
		
		return added;
	}
	
	public boolean RemoveFileFromIndex(String filePath)
	{
		boolean removed;
		int file = GetKeyFromValue(files, filePath);
		
		// If the file has already been added
		if (file != -1)
		{
			// Then remove it from the hash map
			files.remove(file);
			
			// The inverted index is now out of date
			// Throw a flag to rebuild it
			indexOutOfDate = true;
			
			// Set the return value
			removed = true;
		}
		else
		{
			removed = false;
		}
		
		return removed;
	}
	
	public void AskToReindexFiles()
	{
		if (indexOutOfDate)
		{
			ReindexFiles();
		}
	}
	public void ReindexFiles()
	{
		String[] wordsToIndex;
		String [] filepaths = GetFileNames();
		FileClass MyFileClass = new FileClass();
		int i, j;
		
		
		
		// House keeping
		files = new HashMap<Integer, String>();
		
		
		
		// For each file
		for (i = 0; i < filepaths.length; i++)
		{
			// Read file into memory and split it into words
			wordsToIndex = MyFileClass.ImportTextFile(filepaths[i]).replaceAll("[]!?,", "").split("\\s+");
			
			
			
			// Add file to the files HashMap
			files.put(i, filepaths[i]);
			
			
			
			// Add words and positions to the words map
			for (j = 0; j < wordsToIndex.length; j++)
			{
				
				
				
				// Add word to words if words does not already contain it
				if (!words.containsKey(wordsToIndex[j]))
				{
					words.put(wordsToIndex[j], new ArrayList<Long>());
				}
				
				
				
				// Add the file and position to words
				words.get(wordsToIndex[j]).add(CombineFileAndPosition(i + "," + j));
			}	
		}
	}
	
	public void ImportIndex()
	{
		File indexFile = new File(currentDirectory + "\\Inverted Index\\Index.txt");
		String nextLine;
		
		
		
		if (indexFile.exists())
		{
			try
			{
				Scanner indexFileScanner = new Scanner(indexFile);
				
				// New Index File?
				nextLine = indexFileScanner.nextLine();
				if (nextLine.startsWith("0"))
				{
					newIndex = true;
				}
				else
				{
					String[] lineArguments;
					String word;
					int numberOfFiles, i;
					
					newIndex = false;
					
					
					
					// Import the number of files and last modified
					lineArguments = nextLine.split(" ");
					numberOfFiles = Integer.parseInt(lineArguments[0]);
					indexLastBuilt = Long.parseLong(lineArguments[1]);
					
					
					
					// Import the files
					for (i = 0; i < numberOfFiles; i++)
					{
						nextLine = indexFileScanner.nextLine();
						lineArguments = nextLine.split(",");
						files.put(Integer.parseInt(lineArguments[0]), lineArguments[1]);
					}
					
					
					
					// Import the index
					while (indexFileScanner.hasNextLine())
					{
						nextLine = indexFileScanner.nextLine();
						lineArguments = nextLine.split(" ");
						word = lineArguments[0];
						
						ArrayList<Long> wordFilesAndPositions = new ArrayList<Long>();
						for(i = 1; i < lineArguments.length; i++)
						{
							wordFilesAndPositions.add(CombineFileAndPosition(lineArguments[i]));
						}
						words.put(word,  wordFilesAndPositions);
					}
				}
				
				indexFileScanner.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				indexFile.createNewFile();
				PrintWriter file = new PrintWriter(new OutputStreamWriter(new FileOutputStream(indexFile)));
				file.println("0 0");
				file.close();
				ImportIndex();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void ExportIndex()
	{
		// Open the file
		try
		{
			// Create the file writer
			BufferedWriter indexFileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(currentDirectory + "\\Inverted Index\\Index.txt"), "UTF-8"));
			
			
			
			// Write the number of files and the date it was last indexed
			indexFileWriter.write(files.size() + " " + indexLastBuilt);
			
			
			
			// Write the files and their indexes
			Integer fileIndex;
			String filePath;
			
			for (Map.Entry<Integer, String> fileEntry : files.entrySet())
			{
				fileIndex = fileEntry.getKey();
				filePath = fileEntry.getValue();
				
				indexFileWriter.newLine();
				indexFileWriter.write(fileIndex + "," + filePath);

			}
			
			
			
			// Write the words and their positions
			String word;
			ArrayList<Long> filesAndPositions;
			
			for (Map.Entry<String, ArrayList<Long>> wordEntry : words.entrySet())
			{
				word = wordEntry.getKey();
				filesAndPositions = wordEntry.getValue();
				indexFileWriter.write(word);
				
				indexFileWriter.newLine();	
				for (long fileAndPosition:  filesAndPositions)
				{
					indexFileWriter.write(" " + GetFile(fileAndPosition) + "," + GetPosition(fileAndPosition));
				}

			}
			
			
			
			// Close the file
			indexFileWriter.close();
			
			
			
			// Change the the time the index was last built
			// Reset the index out of date flag
			// indexLastBuilt = now;
			indexOutOfDate = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	public String[] GetFileNames()
	{
		String[] fileNames = new String[files.size()];
		int i = 0;
		
		for (String _fileName:  files.values())
		{
			fileNames[i] = _fileName;
			i++;
		}
		
		return fileNames;
	}
	
	
	
	
	public void ImportRefrences(SearchFrame searchFrameInput, FileFrame fileFrameInput)
	{
		searchFrame = searchFrameInput;
		fileFrame = fileFrameInput;
	}
	
	public void ExitProgram()
	{
		ExportIndex();
		SearchEngine.ExitProgram();
	}
	
	
	
	// File and Position Subs
	private long CombineFileAndPosition(String fileAndPosition)
	{
		String[] arguments = fileAndPosition.split(",");
		return (Integer.parseInt(arguments[0]) << 32 | Integer.parseInt(arguments[1]));
	}
	private int GetFile(long fileAndPosition)
	{
		return (int)fileAndPosition >> 32;
	}
	private int GetPosition(long fileAndPosition)
	{
		return (int)fileAndPosition;
	}
	
	public int GetNextAvailableIndex()
	{
		int nextIndex = 0;
		
		while (files.containsKey(nextIndex))
		{
			nextIndex++;
		}
		
		return nextIndex;
	}
	
	private int GetKeyFromValue(Map<Integer, String> inputMap, String value)
	{
		for (Entry <Integer, String> tempEntry:  inputMap.entrySet())
		{
			if (tempEntry.getValue() == value)
			{
				return tempEntry.getKey();
			}
		}
		
		return -1;
	}
	private void MsgBox(String prompt)
	{
		JOptionPane.showMessageDialog(null, prompt);
	}
	
}
