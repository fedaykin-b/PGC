package com.brunoarruda.pgc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class TestUseCases {
	
	/**
	 * if you are opening stefano81-dcpabe as the root project folder on your IDE, 
	 * remove the part "sistemas\\stefano81-dcpabe\\" from TEST_PATH
	 * */
	final static String TEST_PATH = "sistemas\\stefano81-dcpabe\\test data";
	final static private PrintStream realSystemOut = System.out;
	final static private PrintStream realSystemErr = System.err;

	public static void main(String[] args) {
		System.out.println("Tests:\n");

		File root_test_path = new File(TEST_PATH);
		root_test_path.mkdir();

		runTestMethods();
	}
	private static class NullOutputStream extends OutputStream {
		@Override
		public void write(int b){
			return;
		}
		@Override
		public void write(byte[] b){
			return;
		}
		@Override
		public void write(byte[] b, int off, int len){
			return;
		}
		public NullOutputStream(){
		}
	}

	private static void runTestMethods() {
		Method[] methods = TestUseCases.class.getDeclaredMethods();
		for (Method m : methods) {			
			if (m.getName().startsWith("test")) {
				String testPath = TEST_PATH + File.separator + m.getName();
				try {
					System.out.print(String.format("running: %s ... ", m.getName()));

					// disables output streams from code to not mess the test logging
					System.setOut(new PrintStream(new NullOutputStream()));
					System.setErr(new PrintStream(new NullOutputStream()));
					
					// invoke the function which name starts with 'test'					
					m.invoke(null, testPath);

					// restores the output from System.out
					System.setOut(realSystemOut);
					System.setErr(realSystemErr);
					if (m.getName().contains("shouldFail")) {
						System.out.println("FAILED.");
					} else {
						System.out.println("OK.");
					}
				} catch (Exception e) {
					// restores the output from System.out
					System.setOut(realSystemOut);
					System.setErr(realSystemErr);					
					if(m.getName().contains("shouldFail")) {
						String fileDir = testPath + File.separator + "log_error.txt";
						try (PrintWriter pw = new PrintWriter(new FileWriter(fileDir, true))) {							
							e.printStackTrace(pw);
						} catch (Exception e1) {							
							e1.printStackTrace();
						}
						System.out.println("OK. (check the log on the test folder)");
					} else {
						System.out.println("FAILED.");
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void testDecrypt_WithOneAttribute(String testPath) {
		String[] names = { "Bob" };
		String[] attributes = { "paciente" };
		String authorityName = "Entidade Certificadora";

		UseCase test = new UseCase(authorityName, testPath, attributes);
		test.globalSetup();
		test.authoritySetup();
		test.keyGeneration(names, attributes);
		test.encrypt("", "paciente");
		String[] keyPath = test.searchKeys(names[0]);
		test.decrypt("", names[0], keyPath);
	}

	public static void testDecrypt_WithTwoAttributes(String testPath) {
		String[] names = { "Bob", "Bob" };
		String[] attributes = { "paciente", "dono-do-prontuário" };
		String authorityName = "Entidade Certificadora";

		UseCase test = new UseCase(authorityName, testPath, attributes);
		test.globalSetup();
		test.authoritySetup();
		test.keyGeneration(names, attributes);
		test.encrypt("", "dono-do-prontuário and paciente");
		test.decrypt("", names[0], test.searchKeys(names[0]));
	}

	public static void testPolicy_AndGateWithMissingAttribute_shouldFail(String testPath) {
		String[] names = { "Bob", "Bob", "Alice" };
		String[] attributes = { "paciente", "dono-do-prontuário", "usuário-credenciado" };
		String authorityName = "Entidade Certificadora";

		UseCase test = new UseCase(authorityName, testPath, attributes);
		test.globalSetup();
		test.authoritySetup();
		test.keyGeneration(names, attributes);
		test.encrypt("", "and usuário-credenciado and dono-do-prontuário paciente");
		test.decrypt("", names[0], test.searchKeys(names[0]));

	}

	public static void testPolicy_OrGateWithMissingAttribute(String testPath) {
		String[] names = { "Bob", "Alice" };
		String[] attributes = { "dono-do-prontuário", "médico" };
		String authorityName = "Entidade Certificadora";

		UseCase test = new UseCase(authorityName, testPath, attributes);
		test.globalSetup();
		test.authoritySetup();
		test.keyGeneration(names, attributes);
		test.encrypt("", "or dono-do-prontuário médico");
		test.decrypt("", names[0], test.searchKeys(names[0]));
	}

	public static void testDecrypt_WithKeyFromOtherUser_shouldFail(String testPath) {
		String[] names = { "Bob", "Mr. Robot" };
		String[] attributes = { "dono-do-prontuário", "hacker" };
		String authorityName = "Entidade Certificadora";

		UseCase test = new UseCase(authorityName, testPath, attributes);
		test.globalSetup();
		test.authoritySetup();
		test.keyGeneration(names, attributes);
		test.encrypt("", "dono-do-prontuário");
		test.decrypt("", names[1], test.searchKeys(names[0]));
	}

	public static void testEncrypt_WithLargeFile(String testPath) {
		String[] names = { "Bob" };
		String[] attributes = { "paciente" };
		String authorityName = "Entidade Certificadora";

		String fileDir = testPath + File.separator + "BigFile.txt";
		File filePath = new File(fileDir);
		try {
			OutputStreamWriter writer = new OutputStreamWriter(
				new FileOutputStream(filePath), StandardCharsets.UTF_8);
			for (int i = 0; i < 100000; i++) {
				writer.append("This is mock for a Big File sent to encryption with an ABE scheme.\n");
			}
			writer.close();
		} catch (Exception e) {		
			e.printStackTrace();
		}

		UseCase test = new UseCase(authorityName, testPath, attributes);
		test.globalSetup();
		test.authoritySetup();
		test.keyGeneration(names, attributes);
		
		test.encrypt(fileDir, "paciente");
		String[] keyPath = test.searchKeys(names[0]);
		test.decrypt(fileDir, names[0], keyPath);
	}
}
