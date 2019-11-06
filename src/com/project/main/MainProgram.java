package com.project.main;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class MainProgram {

	public static void main(String[] args) {
		String inputFilePath = "";
		String replacePath = "";
		String outputPath = "";
		
		if(args.length != 3) {
			System.err.println("Arguments should be exactly three");
			System.exit(1);
		}
		
		inputFilePath = args[0].trim();
		replacePath = args[1].trim();
		outputPath = args[2].trim();

		validateFilePaths(inputFilePath, replacePath, outputPath);
		
		FindAndReplacer findAndReplacer = new FindAndReplacer(inputFilePath, replacePath, outputPath);
		
		findAndReplacer.process();
		
	}
	
	public static boolean isBlankOrEmpty(String str) {
		return str.isBlank() || str.isEmpty();
	}
	
	public static void validateFilePaths(String... filePaths) {
		int ctr = 1;
		for(String filePath : filePaths) {
			if(isBlankOrEmpty(filePath.trim())) { 
				System.err.println("File Path " + ctr + " is blank or empty.");
				System.exit(1);
			}
			ctr++;
		}
	}

}
