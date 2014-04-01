package SearchEngine;


import java.io.*;
import java.util.*;
import java.util.Map.Entry;



public class SearchClass 
{
	
	
	
	// Global Variables
	private long indexLastBuilt = 0;
	private boolean indexOutOfDate;
	private Map<Integer, String> files = new HashMap<Integer, String>();
	private Map<String, ArrayList<Long>> words = new HashMap<String, ArrayList<Long>>();
	private String currentDirectory = System.getProperty("user.dir");
	
	
	
	// Add and remove files
	public boolean AddFileToIndex(String filePath)
	{
		boolean added;
		
		
				
		// If the file has not already been added
		if (!files.containsValue(filePath))
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
		
		
		
		// If the file has already been added to the index
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
	
	
	
	// Rebuilding the index
	public void AskToReindexFiles()
	{
		if (indexOutOfDate)
		{
			ReindexFiles();
		}
	}
	private void ReindexFiles()
	{
		String[] wordsToIndex;
		String [] filepaths = GetFileNames();
		FileClass MyFileClass = new FileClass();
		int i, j;
		
		
		
		// House keeping
		files = new HashMap<Integer, String>();
		words = new HashMap<String, ArrayList<Long>>();
		
		
		
		// For each file
		for (i = 0; i < filepaths.length; i++)
		{
			// Read file into memory and split it into words
			wordsToIndex = MyFileClass.ImportTextFile(filepaths[i]).toLowerCase().replaceAll("[!?,.]", "").split("\\s+");
			
			
			
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
		
		
		
		// Change the the time the index was last built
		// Reset the index out of date flag
		indexLastBuilt = new Date().getTime();
		indexOutOfDate = false;
	}
	
	
	
	// Index Subs
	public void ImportIndex()
	{
		// Make the index directory
		// If the directory exists, this will do nothing
		new File(currentDirectory + "\\Inverted Index").mkdir();
		
		
		
		// Attempt to open the index
		File indexFile = new File(currentDirectory + "\\Inverted Index\\Index.txt");
		
		
		
		// Attempt to import the index
		if (indexFile.exists())
		{
			try
			{
				Scanner indexFileScanner = new Scanner(indexFile);
				
				
				
				// New Index File?
				String nextLine = indexFileScanner.nextLine();
				if (!nextLine.equals("0"))
				{
					String[] lineArguments;
					String word;
					int numberOfFiles, i;
					
					
					
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
						
						
						
						// Check if the file has been modified after the index was last built
						if (indexLastBuilt < (new File(lineArguments[1]).lastModified()))
						{
							indexOutOfDate = true;
						}
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
				
				
				
				// Close the File Scanner
				indexFileScanner.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			// Create a new index file
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
		
		
		
		// If the index is out of date, rebuild it
		AskToReindexFiles();
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
				
				indexFileWriter.newLine();	
				indexFileWriter.write(word);

				for (long fileAndPosition:  filesAndPositions)
				{
					indexFileWriter.write(" " + GetFile(fileAndPosition) + "," + GetPosition(fileAndPosition));
				}

			}
			
			
			
			// Close the file
			indexFileWriter.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	
	
	// Searching subs
	public ArrayList<String> SearchByOR(String[] searchTerms)
	{
		ArrayList<Integer> matchingFileNumbers = new ArrayList<Integer>();
		ArrayList<String> matchingFilePaths = new ArrayList<String>();
		
		
		
		for (String searchTerm:  searchTerms)
		{
			for (Map.Entry<String, ArrayList<Long>> word:  words.entrySet())
			{
				if (word.getKey().equals(searchTerm.toLowerCase()))
				{
					for (long file:  word.getValue())
					{
						if (!matchingFileNumbers.contains(GetFile(file)))
						{
							matchingFileNumbers.add(GetFile(file));
						}
					}
				}
			}
		}
		
		
		
		// Return the matches
		for (Integer fileNumber:  matchingFileNumbers)
		{
			matchingFilePaths.add(files.get(fileNumber));
		}
		return matchingFilePaths;
	}
	public ArrayList<String> SearchByAND(String[] searchTerms)
	{
		// Declarations
		ArrayList<Integer> matchingFileNumbers = new ArrayList<Integer>(GetFileNumbers());
		ArrayList<Integer> fileNumbersToRemove = new ArrayList<Integer>();
		ArrayList<String> matchingFilePaths = new ArrayList<String>();
		boolean contains;
		
		
		
		//
		for (String searchTerm:  searchTerms)
		{
			for (int fileNumber:  matchingFileNumbers)
			{
				contains = false;
				if (words.containsKey(searchTerm))
				{
					for (long file:  words.get(searchTerm))  
					{
						if (fileNumber == GetFile(file))
						{
							contains = true;
							break;
						}
					}
				}
				
				
				
				if (contains == false && matchingFileNumbers.contains(fileNumber))
				{
					if (!fileNumbersToRemove.contains(fileNumber))
					{
						fileNumbersToRemove.add(fileNumber);
					}
				}
			}
		}
		
		
		
		// Remove the files
		for (int fileNumber:  fileNumbersToRemove)
		{
			matchingFileNumbers.remove((Object) fileNumber);
		}
		
		
		
		// Return the matches
		for (Integer fileNumber:  matchingFileNumbers)
		{
			matchingFilePaths.add(files.get(fileNumber));
		}
		return matchingFilePaths;
	}
	public ArrayList<String> SearchByPHRASE(String[] searchTerms)
	{
		ArrayList<Long> matchingFileNumbers = new ArrayList<Long>(words.get(searchTerms[0]));
		ArrayList<Long> tempMatchingFileNumbers = new ArrayList<Long>(words.get(searchTerms[0]));
		ArrayList<String> matchingFilePaths = new ArrayList<String>();
		String searchTerm;
		int i;
		
		
		
		// The rest of the words
		// Check that the first word in the phrase is contained in any file
		// If so, loop for the rest of the phrase
		if (matchingFileNumbers.size() != 0)
		{
			for (i = 1; i < searchTerms.length; i ++)
			{
				searchTerm = searchTerms[i];
				for (Map.Entry<String, ArrayList<Long>> entry:  words.entrySet())
				{
					if (entry.getKey().equals(searchTerm))
					{
						tempMatchingFileNumbers = new ArrayList<Long>();
						for (Long fileAndPosition:  entry.getValue())
						{
							for (Long matchingFileNumber:  matchingFileNumbers)
							{
								if ((GetFile(matchingFileNumber) == GetFile(fileAndPosition) && (GetPosition(matchingFileNumber) + i == GetPosition(fileAndPosition))))
								{
									tempMatchingFileNumbers.add(fileAndPosition);
								}
							}
						}
					}
				}
			}
			
			
			
			// Advance to next word
			matchingFileNumbers = tempMatchingFileNumbers;
		}

		
		
		
		// Return the matches
		for (Long fileAndPosition:  matchingFileNumbers)
		{
			if (!matchingFilePaths.contains(files.get(GetFile(fileAndPosition))))
			{
				matchingFilePaths.add(files.get(GetFile(fileAndPosition)));
			}
		}
		return matchingFilePaths;
	}
	
	
	
	// Helper methods	
	public String[] GetFileNames()
	{
		String[] f = new String[files.size()];
		files.values().toArray(f);
		return f;
	}
	private ArrayList<Integer> GetFileNumbers()
	{
		ArrayList<Integer> fileNumbers = new ArrayList<Integer>();
		fileNumbers.addAll(files.keySet());
		return fileNumbers;
	}
 	private int GetNextAvailableIndex()
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
	public void ExitProgram()
	{
		AskToReindexFiles();
		ExportIndex();
		SearchEngine.ExitProgram();
	}
	
	
	
	// File and Position Subs (Combining two int into a long and vice versa)
	private long CombineFileAndPosition(String fileAndPosition)
	{
		String[] arguments = fileAndPosition.split(",");
		int f = Integer.parseInt(arguments[0]);
		int p = Integer.parseInt(arguments[1]);
		
		return (((long) f) << 32) | (p & 0xffffffffL);
	}
	private int GetFile(long fileAndPosition)
	{
		return (int) (fileAndPosition >> 32);
	}
	private int GetPosition(long fileAndPosition)
	{
		return (int) fileAndPosition;
	}
	
}
