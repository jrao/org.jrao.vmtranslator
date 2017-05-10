package org.jrao.vmtranslator;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jrao.vmtranslator.Parser.COMMAND_TYPE;

public class VMTranslator {

	public static void main(String[] args) {
        System.out.println("Welcome to org.jrao.vmtranslator!\n");
        
        if (args.length < 1) {
        	System.err.println("Usage: java -jar org.jrao.vmtranslator.jar [path-to-vm-file-or-containing-folder]");
        	return;
        }
        
        List<File> vmFiles = new ArrayList<File>();
		String currentPathString = Paths.get("").toAbsolutePath().toString();
		String inputPathString = currentPathString + "/" + args[0];
        File input = new File(inputPathString);

        if (input.isFile()) {
        	vmFiles.add(input);
        }
        else if (input.isDirectory()) {
        	File[] files = input.listFiles(new FilenameFilter() {
        		@Override
        		public boolean accept(File dir, String name) {
        			return name.toLowerCase().endsWith(".vm");
        		}
        	});
        	
        	for (File file : files) {
        		vmFiles.add(file);
        	}
        }
        else {
			System.err.println(
					"Error: You must supply either a .vm file or a directory" +
					" containing .vm files as an argument.");
			return;
        }
        
        String outputFilePath = "";
        CodeWriter writer = null;
        // If there's only on input .vm file, base the output .asm file's name on the input file's name
        if (vmFiles.size() == 1) {
        	File vmFile = vmFiles.get(0);
			String vmFileName = vmFile.getName();
			String asmFileName = vmFileName.substring(0, vmFileName.lastIndexOf(".vm")) + ".asm";
			outputFilePath = vmFile.getParent() + "/" + asmFileName;
			writer = new CodeWriter(new File(outputFilePath));
			writer.setFileName(vmFileName);
        }
        // If there are multiple input .vm files, base the output .asm file's name on the input directory's name
        else if (vmFiles.size() > 1) {
        	String inputPath = input.getAbsolutePath();
			outputFilePath = inputPath + "/" + input.getName() + ".asm";
			writer = new CodeWriter(new File(outputFilePath));
        }

        /*
         * Bootstrap assembly code must come  first (before any of the other files are translated)
         */
        writer.writeBootstrap();

        for (File vmFile : vmFiles) {
        	writer.setFileName(vmFile.getName());
			Parser parser = new Parser(vmFile);

			while (parser.hasMoreCommands()) {
				parser.advance();
				COMMAND_TYPE commandType = parser.commandType();
				
				if (commandType == COMMAND_TYPE.C_ARITHMETIC) {
					writer.writeArithmetic(parser.arg0());
				}
				else if (commandType == COMMAND_TYPE.C_PUSH || commandType == COMMAND_TYPE.C_POP) {
					writer.writePushPop(commandType, parser.arg1(), Integer.parseInt(parser.arg2()));
				}
				else if (commandType == COMMAND_TYPE.C_LABEL) {
					writer.writeLabel(parser.arg1());
				}
				else if (commandType == COMMAND_TYPE.C_GOTO) {
					writer.writeGoto(parser.arg1());
				}
				else if (commandType == COMMAND_TYPE.C_IF) {
					writer.writeIf(parser.arg1());
				}
				else if (commandType == COMMAND_TYPE.C_FUNCTION) {
					writer.writeFunction(parser.arg1(), Integer.parseInt(parser.arg2()));
				}
				else if (commandType == COMMAND_TYPE.C_RETURN) {
					writer.writeReturn();
				}
				else if (commandType == COMMAND_TYPE.C_CALL) {
					writer.writeCall(parser.arg1(), Integer.parseInt(parser.arg2()));
				}
			}
        }
        
        System.out.println("Created output file: " + outputFilePath);

        if (writer != null) {
        	writer.close();
        }
	}

}
