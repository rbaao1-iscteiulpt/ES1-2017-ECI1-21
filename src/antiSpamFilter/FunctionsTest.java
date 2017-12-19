package antiSpamFilter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class FunctionsTest {

	String rules_path = "jUnitTests/TestFiles/rules.cf";
	String rules2_path = "jUnitTests/TestFiles/rules2.cf";
	String spam_path = "jUnitTests/TestFiles/spam.log";
	String NSGAII_1 = "jUnitTests/TestFiles/NSGAII.rf";
	String NSGAII_2 = "jUnitTests/TestFiles/NSGAII.rs";

	/**
	 *  Counts the number of lines in the file rules.cf defined by rules_path, which corresponds to the number of rules. 
	 *  To test this, it was created a test file with a number of rules and it was verified 
	 *  if the number of rules was the same as expected.
	 * @throws FileNotFoundException
	 */
	@Test
	public final void testNumber_of_rules() throws FileNotFoundException {
		Integer numberOfRules = Functions.number_of_rules(rules_path);
		assertSame("failure - should be same", 4, numberOfRules);
	}

	/**
	 *  Returns an array with the rules inside rules_path. 
	 *  To test this, it was created a test file with a number of rules and after it was verified 
	 *  if the ArrayList of rules read from the file equals the test ArrayList created.
	 * @throws FileNotFoundException
	 */
	@Test
	public final void testGet_rules() throws FileNotFoundException {
		ArrayList<String> expectedList = new ArrayList<String>();
		expectedList.add("A");
		expectedList.add("B");
		expectedList.add("C");
		expectedList.add("D");
		ArrayList<String> actualList = Functions.get_rules(rules_path);
		assertEquals("failure - lists are not equal", expectedList, actualList);
	}

	/**
	 * Returns an array with the weights inside rules2_path.
	 * To test this, it was created a test file with a number of weights and after it was verified 
	 * if the ArrayList of weights read from the file equals the test ArrayList created.
	 * @throws FileNotFoundException
	 */
	@Test
	public final void testGet_weights() throws FileNotFoundException {
		ArrayList<String> expectedList = new ArrayList<String>();
		expectedList.add("1.0");
		expectedList.add("0.0");
		expectedList.add("3.0");
		expectedList.add("4.0");
		ArrayList<String> actualList = Functions.get_weights(rules2_path);
		assertEquals("failure - lists are not equal", expectedList, actualList);
	}

	/**
	 * Writes in path (rules_write.cf, a test file created) the weights given on a arrayList. 
	 * To test this we created two ArrayLists with the same elements. One was given on the function write_Weights. 
	 * Then the function get_weights was called to read the weights of the file to a ArrayList and it was verified if the two
	 * ArrayLists were the same.
	 * @throws IOException
	 */
	@Test
	public final void testWrite_weights() throws IOException {
		ArrayList<String> expectedList = new ArrayList<String>();
		expectedList.add("5.0");
		expectedList.add("6.0");
		expectedList.add("7.0");
		expectedList.add("8.0");

		ArrayList<Double> solutionList = new ArrayList<Double>();
		solutionList.add(5.0);
		solutionList.add(6.0);
		solutionList.add(7.0);
		solutionList.add(8.0);

		Functions.write_weights("jUnitTests/TestFiles/rules_write.cf", solutionList);
		ArrayList<String> actualList = Functions.get_weights("jUnitTests/TestFiles/rules_write.cf");
		assertEquals("failure - lists are not equal", expectedList, actualList);	
	}

	/**
	 * It was created a test file with the structure of ham.log/spam.log. 
	 * Then it was created an ArrayList of ArrayLists to simulate that each position corresponded to an email and
	 * each position then has another array that contains the rules the email breaks.   
	 * Then it was verified if the ArrayList created equals the ArrayList read from the test file.
	 * @throws FileNotFoundException
	 */
	@Test
	public final void testFile_to_array() throws FileNotFoundException {
		ArrayList<ArrayList<String>> expectedList = new ArrayList<>();

		ArrayList<String> e1 = new ArrayList<>();
		ArrayList<String> e2 = new ArrayList<>();
		ArrayList<String> e3 = new ArrayList<>();
		ArrayList<String> e4 = new ArrayList<>();

		e1.add("A");
		e1.add("B");
		e1.add("C");

		e2.add("B");
		e2.add("D");

		e3.add("A");

		e4.add("D");

		expectedList.add(e1);
		expectedList.add(e2);
		expectedList.add(e3);
		expectedList.add(e4);

		ArrayList<ArrayList<String>> actualList = Functions.file_to_array("jUnitTests/TestFiles/ham.log");
		assertEquals("failure - lists are not equal", expectedList, actualList);
	}

	/**
	 * Evaluates the solution returning the total of FP (type 0).
	 * To test this we put the weights of the test file on an ArrayList and  then we evaluate the solution with the
	 * weights read and the given type. 
	 * @throws FileNotFoundException
	 */
	@Test
	public final void testEvaluate_solution() throws FileNotFoundException {
		ArrayList<Double> solution2 = new ArrayList<Double>();
		ArrayList<String> aux = Functions.get_weights(rules_path);
		for(String string: aux){
			solution2.add(Double.parseDouble(string));
		}
		double solution = Functions.evaluate_solution(0, Functions.get_rules(rules_path), solution2, Functions.file_to_array("jUnitTests/TestFiles/ham.log"));
		assertTrue(solution==2.0);
	}
	
	/**
	 * Evaluates the solution returning the total of FN (type 1).
	 * To test this we put the weights of the test file on an ArrayList and  then we evaluate the solution with the weights read and the given type. 
	 * @throws FileNotFoundException
	 */
	@Test
	public final void testEvaluate_solution2() throws FileNotFoundException {
		ArrayList<Double> solution2 = new ArrayList<Double>();
		ArrayList<String> aux = Functions.get_weights(rules_path);
		for(String string: aux){
			solution2.add(Double.parseDouble(string));
		}
		double solution = Functions.evaluate_solution(1, Functions.get_rules(rules_path), solution2, Functions.file_to_array(spam_path));
		assertTrue(solution==2.0);
	}

	/**
	 * Chooses the best solution to a Mixed (Professional and Leisure) Mailbox. To test this it was created a test file with different 
	 * solutions and then it was verified if the choosen solution was the best solution.
	 * @throws FileNotFoundException
	 */
	@Test
	public final void testChoose_solution() throws FileNotFoundException {
		int n_solution = Functions.choose_solution(NSGAII_1);
		
		assertSame("failure - should be same", 2, n_solution);
	}

	/**
	 * Returns an ArrayList with the solution in the line that we want.
	 * To test this we call the function get_solution giving the line of the expected solution (line starts at zero).
	 * And then we verify if the ArrayList returned is the same as test ArrayList created.
	 * @throws FileNotFoundException
	 */
	@Test
	public final void testGet_solution() throws FileNotFoundException {
		ArrayList<Double> expectedList = new ArrayList<Double>();
		ArrayList<Double> actualList = Functions.get_solution(2, NSGAII_2);
		
		expectedList.add(4.0);
		expectedList.add(2.0);
		expectedList.add(-1.0);
		expectedList.add(-4.0);
		assertEquals("failure - lists are  not equal", expectedList, actualList);
	}
	
	@Test
	public void testTestButton(){
		try {
			System.out.println("===TEST TEST BUTTON FROM MANUAL IMPLEMENTATION===");
			Interface testSubject = new Interface();

			testSubject.getTestButton().getActionListeners()[0].actionPerformed(null);
			
			Thread.sleep(1000);
			assertEquals(testSubject.getmFalseNegField().getText(), "0");
			assertEquals(testSubject.getmFalsePosField().getText(), "0");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testTestButton2(){
		try {
			System.out.println("===TEST 2 TEST BUTTON FROM MANUAL IMPLEMENTATION===");
			Interface testSubject = new Interface();
			System.out.println("chooose rules");
			testSubject.changeRules();
			System.out.println("chooose spam");
			testSubject.changeSpam();
			System.out.println("chooose ham");
			testSubject.changeHam();
			testSubject.getTestButton().getActionListeners()[0].actionPerformed(null);
			
			Thread.sleep(1000);
			assertNotEquals(testSubject.getmFalseNegField().getText(), "0");
			assertNotEquals(testSubject.getmFalsePosField().getText(), "0");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testManualSaveButton() {
		try {
			System.out.println("===TEST SAVE BUTTON FROM MANUAL IMPLEMENTATION===");
			Interface testSubject = new Interface();
			testSubject.getFrame().setVisible(true);
			System.out.println("chooose rules");
			testSubject.changeRules();
			
			Thread.sleep(5000);
			
			testSubject.getMSaveButton().getActionListeners()[0].actionPerformed(null);
			ArrayList<String> rules_file = Functions.get_rules(testSubject.getRulesPath().getText());
			ArrayList<String> weights_file = Functions.get_weights(testSubject.getRulesPath().getText());
			
			String temp1= "";
			String temp2= "";
			for(String s : rules_file) {
				temp1+=s + "\n";
			}
			for(String s : weights_file) {
				temp2+=s + "\n";
			}
			
			assertEquals(temp1, testSubject.getmRulesTextArea().getText());
			assertEquals(temp2, testSubject.getmWeightTextArea().getText());
			
		}	catch (InterruptedException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testChangeRules(){
		try {
			System.out.println("===TEST SUCCESSFUL RULES.CF IMPORT===");
			Interface testSubject = new Interface();
			System.out.println("chooose rules");
			testSubject.getRulesButton().getActionListeners()[0].actionPerformed(null);
			
			Thread.sleep(1000);
			
			assertThat(testSubject.getRulesPath().getText(), is(not("")));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testCancelChangeRules(){
		try {
			System.out.println("===TEST CANCEL RULES.CF IMPORT===");
			Interface testSubject = new Interface();
			System.out.println("chooose cancel");
			testSubject.changeRules();
			
			Thread.sleep(1000);
			
			assertThat(testSubject.getRulesPath().getText(), is(not("")));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testChangeRules_Failure(){
		try {
			System.out.println("===TEST UNSUCCESSFUL RULES.CF IMPORT===");
			Interface testSubject = new Interface();
			System.out.println("chooose any except rules");
			testSubject.changeRules();
			
			Thread.sleep(1000);
			
			assertThat(testSubject.getRulesPath().getText(), is(""));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testChangeHam(){
		try {
			System.out.println("===TEST SUCCESSFUL HAM.LOG IMPORT===");
			Interface testSubject = new Interface();
			System.out.println("chooose ham");
			testSubject.getHamButton().getActionListeners()[0].actionPerformed(null);
			
			Thread.sleep(1000);
			
			assertThat(testSubject.getHamPath().getText(), is(not("")));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testCancelChangeHam(){
		try {
			System.out.println("===TEST Cancel SUCCESSFUL HAM.LOG IMPORT===");
			Interface testSubject = new Interface();
			System.out.println("chooose cancel");
			testSubject.changeHam();
			
			Thread.sleep(1000);
			
			assertThat(testSubject.getHamPath().getText(), is(not("")));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testChangeSpam(){
		try {
			System.out.println("===TEST SUCCESSFUL SPAM.LOG IMPORT===");
			Interface testSubject = new Interface();
			System.out.println("chooose spam");
			testSubject.getSpamButton().getActionListeners()[0].actionPerformed(null);
			
			Thread.sleep(1000);
			
			assertThat(testSubject.getSpamPath().getText(), is(not("")));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testCancelChangeSpam(){
		try {
			System.out.println("===TEST Cancel SUCCESSFUL SPAM.LOG IMPORT===");
			Interface testSubject = new Interface();
			System.out.println("chooose cancel");
			testSubject.changeSpam();
			
			Thread.sleep(1000);
			
			assertThat(testSubject.getSpamPath().getText(), is(not("")));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void generateButton() {
		try {
			System.out.println("===TEST Generate BUTTON FROM AUTO IMPLEMENTATION===");
			Interface testSubject = new Interface();
			System.out.println("chooose rules");
			testSubject.changeRules();
			System.out.println("chooose spam");
			testSubject.changeSpam();
			System.out.println("chooose ham");
			testSubject.changeHam();
			testSubject.getGenerateButton().getActionListeners()[0].actionPerformed(null);
			
			Thread.sleep(1000);
			assertEquals(testSubject.getaFalseNegField().getText(), "0");
			assertEquals(testSubject.getaFalsePosField().getText(), "0");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public final void testAdditionCompare() {
		int [] a1 = {5, 7};
		int [] a2 = {3, 2};


		SolutionAdditionComparator sc = new SolutionAdditionComparator();

		int result = sc.compare(a1, a2);

		assertEquals("failure - should be equal", result, 7);

	}
	
	@Test
	public void testAutoSaveButton() {
		try {
			System.out.println("===TEST SAVE BUTTON FROM AUTO IMPLEMENTATION===");
			Interface testSubject = new Interface();
			testSubject.getFrame().setVisible(true);
			System.out.println("chooose rules");
			testSubject.changeRules();
			
			Thread.sleep(5000);
			
			testSubject.getASaveButton().getActionListeners()[0].actionPerformed(null);
			ArrayList<String> rules_file = Functions.get_rules(testSubject.getRulesPath().getText());
			ArrayList<String> weights_file = Functions.get_weights(testSubject.getRulesPath().getText());
			
			String temp1= "";
			String temp2= "";
			for(String s : rules_file) {
				temp1+=s + "\n";
			}
			for(String s : weights_file) {
				temp2+=s + "\n";
			}
			
			assertEquals(temp1, testSubject.getmRulesTextArea().getText());
			assertEquals(temp2, testSubject.getmWeightTextArea().getText());
			
		}	catch (InterruptedException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


}
