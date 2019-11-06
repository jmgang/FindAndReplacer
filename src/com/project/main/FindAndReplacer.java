package com.project.main;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class FindAndReplacer {
	
	private String inputFilePath;
	private String replacePath;
	private File inputFile;
	private File replaceFile;
	private File outputFile;	
	
	private static final String DELIMITER = "=";
	
	public FindAndReplacer(String inputFilePath, String replacePath, String outputFilePath) {
		inputFile = new File(inputFilePath);
		replaceFile = new File(replacePath);
		outputFile = new File(outputFilePath);
	}
	
	public void process() {

		Map<String, String> replaceContents = getReplaceContents();
		List<String> readContents = readAndRetrieveFileContents().parallelStream().distinct().collect(Collectors.toList());
		System.out.println("Total Lines in Memory: " + readContents.size());
		System.out.println("Running. Please wait...");
		AtomicInteger replaces = new AtomicInteger(0);
		
		try ( BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile)) ) {
			replaceContents.entrySet().forEach(entry -> {
				readContents.parallelStream().forEach(line -> {
					if(line.contains(entry.getKey())) {
						replaces.set(replaces.incrementAndGet());
						int index = readContents.indexOf(line);
						readContents.set( index , line.replaceAll( entry.getKey(), entry.getValue()) );
					}
				});
			});
			
			System.out.println("Total Replacements: " + replaces.intValue());
			System.out.println("Total Lines After Replacement: " + readContents.size());
			
			AtomicInteger ctr = new AtomicInteger(0);
			readContents.stream().forEach(line -> {
				try {
					bufferedWriter.write(line);
					bufferedWriter.newLine();
					ctr.set(ctr.incrementAndGet());
				} catch (IOException e) {
					System.err.println("Error " + e.getMessage().concat("\nThe System will now exit..."));
					System.exit(1);
				}
			});
			
			System.out.println("Total Lines written to file: " + ctr.intValue());
			
			System.out.println("Find and Replace operation completed successfully. \nPlease check the output file at "
					+ outputFile.getAbsolutePath());
			Desktop.getDesktop().open(outputFile);
		} catch (FileNotFoundException fnfe) {
			System.err.println("Error " + fnfe.getMessage().concat("\nThe System will now exit..."));
		} catch (IOException e) {
			System.err.println("Error " + e.getMessage().concat("\nThe System will now exit..."));
		}
	}
	
	public File getInputFile() {
		return inputFile;
	}


	public void setInputFile(File inputFile) {
		this.inputFile = inputFile;
	}


	public File getReplaceFile() {
		return replaceFile;
	}


	public void setReplaceFile(File replaceFile) {
		this.replaceFile = replaceFile;
	}


	public File getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	public String getInputFilePath() {
		return inputFilePath;
	}
	
	public void setInputFilePath(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}
	
	public String getReplacePath() {
		return replacePath;
	}
	
	public void setReplacePath(String replacePath) {
		this.replacePath = replacePath;
	}
	
	protected Map<String, String> getReplaceContents() {
		String inputLine;
		Map<String, String> replaceContents = new HashMap<>();
		
		try ( BufferedReader bufferedReader = new BufferedReader(new FileReader(replaceFile)) ) {
			while ((inputLine = bufferedReader.readLine()) != null) {
				String tempStringArray[] = inputLine.trim().split(DELIMITER);
				replaceContents.put(tempStringArray[0].trim(), tempStringArray[1].trim());
			}
		} catch (FileNotFoundException fnfe) {
			System.err.println("Error " + fnfe.getMessage().concat("\nThe System will now exit..."));
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Error " + e.getMessage().concat("\nThe System will now exit..."));
			System.exit(1);
		}
		
		return replaceContents;
	}
	
	protected List<String> readAndRetrieveFileContents() {
		String inputLine;
		List<String> fileContents = new ArrayList<>();
		
		try ( BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile)) ) {
			while ((inputLine = bufferedReader.readLine()) != null) {
				fileContents.add(inputLine.trim());
			}
		} catch (FileNotFoundException fnfe) {
			System.err.println("Error " + fnfe.getMessage().concat("\nThe System will now exit..."));
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Error " + e.getMessage().concat("\nThe System will now exit..."));
			System.exit(1);
		}
		
		return fileContents;
		
	}
	
	
}
