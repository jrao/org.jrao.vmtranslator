package org.jrao.vmtranslator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import org.jrao.vmtranslator.Parser.COMMAND_TYPE;

public class CodeWriter {
	
	public CodeWriter(File outputFile) {
		_labelIndex = 0;
		
		try {
			_fw = new FileWriter(outputFile);
			_bw = new BufferedWriter(_fw);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void setFileName(String fileName) {
		if (fileName.contains(".")) {
			_fileName = fileName.substring(0, fileName.lastIndexOf("."));
		}

		try {
			String currentPathString = Paths.get("").toAbsolutePath().toString();
			String vmFilePathString = currentPathString + "/" + fileName;
			File vmFile = new File(vmFilePathString);
			_fw = new FileWriter(vmFile);
			_bw = new BufferedWriter(_fw);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public String getFileName() {
		return _fileName;
	}
	
	public void writeArithmetic(String command) {
		try {
			switch (command) {
			case "add":
				_bw.write("// add\n");
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=M\n");
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=M+D\n");
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n");
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				break;
			case "sub":
				_bw.write("// sub\n");
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=M\n");
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=M-D\n");
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n");
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				break;
			case "neg":
				_bw.write("// neg\n");
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=-M\n");
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n");
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				break;
			case "eq":
				_bw.write("// eq\n");
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=M\n"); // first popped value
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=M-D\n"); // second popped value minus first popped value
				_bw.write(getLabelAInstruction(getCurrentLabelIndex()) + "\n"); // @TRUE
				_bw.write("D;JEQ\n");
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=0\n");
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				_bw.write(getLabelAInstruction(getCurrentLabelIndex() + 1) + "\n"); // @END
				_bw.write("0;JMP\n");
				_bw.write(getLabelDeclaration(getCurrentLabelIndex()) + "\n"); // (TRUE)
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=-1\n");
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				_bw.write(getLabelDeclaration(getCurrentLabelIndex() + 1) + "\n"); // (END)
				incrementLabel();
				incrementLabel();
				break;
			case "gt":
				_bw.write("// gt\n");
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=M\n"); // first popped value
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=M-D\n"); // second popped value minus first popped value
				_bw.write(getLabelAInstruction(getCurrentLabelIndex()) + "\n"); // @TRUE
				_bw.write("D;JGT\n");
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=0\n");
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				_bw.write(getLabelAInstruction(getCurrentLabelIndex() + 1) + "\n"); // @END
				_bw.write("0;JMP\n");
				_bw.write(getLabelDeclaration(getCurrentLabelIndex()) + "\n"); // (TRUE)
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=-1\n");
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				_bw.write(getLabelDeclaration(getCurrentLabelIndex() + 1) + "\n"); // (END)
				incrementLabel();
				incrementLabel();
				break;
			case "lt":
				_bw.write("// lt\n");
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=M\n"); // first popped value
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=M-D\n"); // second popped value minus first popped value
				_bw.write(getLabelAInstruction(getCurrentLabelIndex()) + "\n"); // @TRUE
				_bw.write("D;JLT\n");
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=0\n");
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				_bw.write(getLabelAInstruction(getCurrentLabelIndex() + 1) + "\n"); // @END
				_bw.write("0;JMP\n");
				_bw.write(getLabelDeclaration(getCurrentLabelIndex()) + "\n"); // (TRUE)
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=-1\n");
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				_bw.write(getLabelDeclaration(getCurrentLabelIndex() + 1) + "\n"); // (END)
				incrementLabel();
				incrementLabel();
				break;
			case "and":
				_bw.write("// and\n");
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=M\n");
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=D&M\n");
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n");
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				break;
			case "or":
				_bw.write("// or\n");
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=M\n");
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=D|M\n");
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n");
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				break;
			case "not":
				_bw.write("// not\n");
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=!M\n");
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n");
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				break;
			default:
				break;
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void writePushPop(COMMAND_TYPE command, String segment, int index) {
		if (command == COMMAND_TYPE.C_PUSH) {
			writePush(segment, index);
		}
		else if (command == COMMAND_TYPE.C_POP) {
			writePop(segment, index);
		}
	}
	
	public void writeLabel(String label) {
		try {
			_bw.write("// label " + label + "\n");
			_bw.write("(" + label + ")\n");
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void writeGoto(String label) {
		try {
			_bw.write("// goto " + label + "\n");
			_bw.write("@" + label + "\n");
			_bw.write("0;JMP\n");
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void writeIf(String label) {
		try {
			_bw.write("// if " + label + "\n");
			_bw.write("@SP\n");
			_bw.write("M=M-1\n");
			_bw.write("A=M\n");
			_bw.write("D=M\n"); // D = value popped from stack to use for condition check
			_bw.write("@" + label + "\n");
			_bw.write("D;JNE\n");
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void writeFunction(String functionName, int numLocals) {
		try {
			_bw.write("// function " + functionName + " " + numLocals + "\n");
			_bw.write("(" + functionName + ")\n");
			for (int i = 0; i < numLocals; i++) {
				_bw.write("@0\n");
				_bw.write("D=A\n");
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n");
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void writeReturn() {
		try {
			_bw.write("// return\n");
			_bw.write("// FRAME = LCL\n");
			_bw.write("@LCL\n");
			_bw.write("D=M\n");
			_bw.write("@R13\n"); // Use R13 to represent FRAME
			_bw.write("M=D\n");

			_bw.write("// RET = *(FRAME-5)\n");
			_bw.write("@R13\n");
			_bw.write("D=M\n");
			_bw.write("@5\n");
			_bw.write("A=D-A\n");
			_bw.write("D=M\n");
			_bw.write("@R14\n"); // Use R14 to represent RET
			_bw.write("M=D\n");

			_bw.write("// *ARG = pop()\n");
			_bw.write("@SP\n");
			_bw.write("M=M-1\n");
			_bw.write("A=M\n");
			_bw.write("D=M\n");
			_bw.write("@ARG\n");
			_bw.write("A=M\n");
			_bw.write("M=D\n");
			
			_bw.write("// SP = ARG + 1\n");
			_bw.write("@ARG\n");
			_bw.write("D=M\n");
			_bw.write("@1\n");
			_bw.write("D=D+A\n");
			_bw.write("@SP\n");
			_bw.write("M=D\n");

			_bw.write("// THAT = *(FRAME-1)\n");
			_bw.write("@R13\n");
			_bw.write("D=M\n");
			_bw.write("@1\n");
			_bw.write("A=D-A\n");
			_bw.write("D=M\n");
			_bw.write("@THAT\n");
			_bw.write("M=D\n");

			_bw.write("// THIS = *(FRAME-2)\n");
			_bw.write("@R13\n");
			_bw.write("D=M\n");
			_bw.write("@2\n");
			_bw.write("A=D-A\n");
			_bw.write("D=M\n");
			_bw.write("@THIS\n");
			_bw.write("M=D\n");

			_bw.write("// ARG = *(FRAME-3)\n");
			_bw.write("@R13\n");
			_bw.write("D=M\n");
			_bw.write("@3\n");
			_bw.write("A=D-A\n");
			_bw.write("D=M\n");
			_bw.write("@ARG\n");
			_bw.write("M=D\n");

			_bw.write("// LCL = *(FRAME-4)\n");
			_bw.write("@R13\n");
			_bw.write("D=M\n");
			_bw.write("@4\n");
			_bw.write("A=D-A\n");
			_bw.write("D=M\n");
			_bw.write("@LCL\n");
			_bw.write("M=D\n");

			_bw.write("// goto RET\n");
			_bw.write("@R14\n");
			_bw.write("A=M\n");
			_bw.write("0;JMP\n");
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void writeCall(String functionName, int numArgs) {
		// TODO: implement this method
		try {
			_bw.write("// call " + functionName + " " + numArgs + "\n");
			_bw.write("// push return-address\n");
			int retAddrLabelIndex = getCurrentRetAddrLabelIndex();
			_bw.write(getRetAddrLabelAInstruction(retAddrLabelIndex) + "\n");
			_bw.write("D=A\n");
			_bw.write("@SP\n");
			_bw.write("A=M\n");
			_bw.write("M=D\n");
			_bw.write("@SP\n");
			_bw.write("M=M+1\n");
			
			_bw.write("// push LCL\n");
			_bw.write("@LCL\n");
			_bw.write("D=M\n");
			_bw.write("@SP\n");
			_bw.write("A=M\n");
			_bw.write("M=D\n");
			_bw.write("@SP\n");
			_bw.write("M=M+1\n");

			_bw.write("// push ARG\n");
			_bw.write("@ARG\n");
			_bw.write("D=M\n");
			_bw.write("@SP\n");
			_bw.write("A=M\n");
			_bw.write("M=D\n");
			_bw.write("@SP\n");
			_bw.write("M=M+1\n");

			_bw.write("// push THIS\n");
			_bw.write("@THIS\n");
			_bw.write("D=M\n");
			_bw.write("@SP\n");
			_bw.write("A=M\n");
			_bw.write("M=D\n");
			_bw.write("@SP\n");
			_bw.write("M=M+1\n");

			_bw.write("// push THAT\n");
			_bw.write("@THAT\n");
			_bw.write("D=M\n");
			_bw.write("@SP\n");
			_bw.write("A=M\n");
			_bw.write("M=D\n");
			_bw.write("@SP\n");
			_bw.write("M=M+1\n");

			_bw.write("// ARG = SP-n-5\n");
			_bw.write("@SP\n");
			_bw.write("D=M\n");
			_bw.write("@" + numArgs + "\n");
			_bw.write("D=D-M\n");
			_bw.write("@5\n");
			_bw.write("D=D-M\n");
			_bw.write("@ARG\n");
			_bw.write("M=D\n");

			_bw.write("// LCL = SP\n");
			_bw.write("@SP\n");
			_bw.write("D=M\n");
			_bw.write("@LCL\n");
			_bw.write("M=D\n");

			_bw.write("// goto " + functionName + "\n");
			_bw.write("@" + functionName + "\n");
			_bw.write("0;JMP\n");

			_bw.write("// (return-address)\n");
			_bw.write(getRetAddrLabelDeclaration(retAddrLabelIndex) + "\n");

			incrementRetAddrLabel();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void writeBootstrap() {
		try {
			_bw.write("@256\n");
			_bw.write("D=A\n");
			_bw.write("@SP\n");
			_bw.write("M=D\n");
			writeCall("Sys.init", 0);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void close() {
		try {
			if (_bw != null) {
				_bw.flush();
				_bw.close();
			}

			if (_fw != null) {
				_fw.close();
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private void writePush(String segment, int index) {
		try {
			switch (segment) {
			case "local":
				_bw.write("// push local " + index + "\n");
				_bw.write("@LCL\n");
				_bw.write("D=M\n");
				_bw.write("@" + index + "\n");
				_bw.write("A=D+A\n");
				_bw.write("D=M\n");
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n");
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				break;
			case "argument":
				_bw.write("// push argument " + index + "\n");
				_bw.write("@ARG\n");
				_bw.write("D=M\n");
				_bw.write("@" + index + "\n");
				_bw.write("A=D+A\n");
				_bw.write("D=M\n");
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n");
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				break;
			case "this":
				_bw.write("// push this " + index + "\n");
				_bw.write("@THIS\n");
				_bw.write("D=M\n");
				_bw.write("@" + index + "\n");
				_bw.write("A=D+A\n");
				_bw.write("D=M\n");
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n");
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				break;
			case "that":
				_bw.write("// push that " + index + "\n");
				_bw.write("@THAT\n");
				_bw.write("D=M\n");
				_bw.write("@" + index + "\n");
				_bw.write("A=D+A\n");
				_bw.write("D=M\n");
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n");
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				break;
			case "constant":
				_bw.write("// push constant " + index + "\n");
				_bw.write("@" + index + "\n");
				_bw.write("D=A\n");
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n");
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				break;
			case "static":
				_bw.write("// push static " + index + "\n");
				_bw.write("@" + getFileName() + "." + index + "\n");
				_bw.write("D=M\n"); // store value of static variable in D
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n"); // add value of static variable to the stack
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				break;
			case "temp":
				_bw.write("// push temp " + index + "\n");
				_bw.write("@5\n");
				_bw.write("D=M\n");
				_bw.write("@" + index + "\n");
				_bw.write("A=D+A\n");
				_bw.write("D=M\n");
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n");
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				break;
			case "pointer":
				String thisOrThat = null;
				if (index == 0) {
					thisOrThat = "THIS";
				}
				else if (index == 1) {
					thisOrThat = "THAT";
				}
				else {
					System.err.println("Illegal VM command: 'push pointer x' is only valid for x values of 0 and 1");
					return;
				}
				_bw.write("// push pointer " + index + "\n");
				_bw.write("@" + thisOrThat + "\n");
				_bw.write("D=M\n"); // store thisOrThat in D
				_bw.write("@SP\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n"); // add thisOrThat to the stack
				_bw.write("@SP\n");
				_bw.write("M=M+1\n");
				break;
			default:
				break;
			}
		}
		catch (IOException ioe) {
			
		}
	}
	
	private void writePop(String segment, int index) {
		try {
			switch (segment) {
			case "local":
				_bw.write("// pop local " + index + "\n");
				_bw.write("@LCL\n");
				_bw.write("D=M\n");
				_bw.write("@" + index + "\n");
				_bw.write("A=D+A\n");
				_bw.write("D=A\n");
				_bw.write("@R13\n");
				_bw.write("M=D\n"); // make R13 store address
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=M\n"); // make D store popped value
				_bw.write("@R13\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n"); // store popped value at R13's saved address
				break;
			case "argument":
				_bw.write("// pop argument " + index + "\n");
				_bw.write("@ARG\n");
				_bw.write("D=M\n");
				_bw.write("@" + index + "\n");
				_bw.write("A=D+A\n");
				_bw.write("D=A\n");
				_bw.write("@R13\n");
				_bw.write("M=D\n"); // make R13 store address
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=M\n"); // make D store popped value
				_bw.write("@R13\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n"); // store popped value at R13's saved address
				break;
			case "this":
				_bw.write("// pop this " + index + "\n");
				_bw.write("@THIS\n");
				_bw.write("D=M\n");
				_bw.write("@" + index + "\n");
				_bw.write("A=D+A\n");
				_bw.write("D=A\n");
				_bw.write("@R13\n");
				_bw.write("M=D\n"); // make R13 store address
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=M\n"); // make D store popped value
				_bw.write("@R13\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n"); // store popped value at R13's saved address
				break;
			case "that":
				_bw.write("// pop that " + index + "\n");
				_bw.write("@THAT\n");
				_bw.write("D=M\n");
				_bw.write("@" + index + "\n");
				_bw.write("A=D+A\n");
				_bw.write("D=A\n");
				_bw.write("@R13\n");
				_bw.write("M=D\n"); // make R13 store address
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=M\n"); // make D store popped value
				_bw.write("@R13\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n"); // store popped value at R13's saved address
				break;
			case "static":
				_bw.write("// pop static " + index + "\n");
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=M\n"); // make D store popped value
				_bw.write("@" + getFileName() + "." + index + "\n");
				_bw.write("M=D\n"); // store popped value in variable
				break;
			case "temp":
				_bw.write("// pop temp " + index + "\n");
				_bw.write("@5\n");
				_bw.write("D=A\n");
				_bw.write("@" + index + "\n");
				_bw.write("A=D+A\n");
				_bw.write("D=A\n");
				_bw.write("@R13\n");
				_bw.write("M=D\n"); // make R13 store address
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=M\n"); // make D store popped value
				_bw.write("@R13\n");
				_bw.write("A=M\n");
				_bw.write("M=D\n"); // store popped value at R13's saved address
				break;
			case "pointer":
				String thisOrThat = null;
				if (index == 0) {
					thisOrThat = "THIS";
				}
				else if (index == 1) {
					thisOrThat = "THAT";
				}
				else {
					System.err.println("Illegal VM command: 'pop pointer x' is only valid for x values of 0 and 1");
					return;
				}
				_bw.write("// pop pointer " + index + "\n");
				_bw.write("@SP\n");
				_bw.write("M=M-1\n");
				_bw.write("A=M\n");
				_bw.write("D=M\n");
				_bw.write("@" + thisOrThat + "\n");
				_bw.write("M=D\n");
				break;
			default:
				break;
			}
		}
		catch (IOException ioe) {
			
		}
	}
	
	private int getCurrentLabelIndex() {
		return _labelIndex;
	}
	
	private String getLabelDeclaration(int labelIndex) {
		return "(LABEL" + Integer.toString(labelIndex) + ")";
	}
	
	private String getLabelAInstruction(int labelIndex) {
		return "@LABEL" + Integer.toString(labelIndex);
	}
	
	private void incrementLabel() {
		_labelIndex++;
	}
	
	private int getCurrentRetAddrLabelIndex() {
		return _labelIndex;
	}
	
	private String getRetAddrLabelDeclaration(int labelIndex) {
		return "(RETADDRLABEL" + Integer.toString(labelIndex) + ")";
	}
	
	private String getRetAddrLabelAInstruction(int labelIndex) {
		return "@RETADDRLABEL" + Integer.toString(labelIndex);
	}
	
	private void incrementRetAddrLabel() {
		_labelIndex++;
	}
	
	private String _fileName;
	private int _labelIndex;
	private BufferedWriter _bw;
	private FileWriter _fw;

}
