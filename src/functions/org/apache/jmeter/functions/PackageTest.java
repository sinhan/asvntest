// $Header$
/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

/*
 * Package to test functions
 * 
 * Functions are created and parameters set up in one thread.
 * 
 * They are then tested in another thread, or two threads running in parallel
 * 
 */
package org.apache.jmeter.functions;

import java.util.Collection;
import java.util.LinkedList;

import junit.extensions.ActiveTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.junit.JMeterTestCase;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JMeterStopThreadException;
import org.apache.log.Logger;

/**
 * @version $Revision: 325609 $ $Date: 2005-08-10 07:28:31 -0700 (Wed, 10 Aug 2005) $
 */
public class PackageTest extends JMeterTestCase {

	transient private static final Logger log = LoggingManager.getLoggerForClass();

	static {
		// LoggingManager.setPriority("DEBUG","jmeter");
		// LoggingManager.setTarget(new java.io.PrintWriter(System.out));
	}

	public PackageTest(String arg0) {
		super(arg0);
	}

	// Create the CSVRead function and set its parameters.
	private static CSVRead setParams(String p1, String p2) throws Exception {
		CSVRead cr = new CSVRead();
		Collection parms = new LinkedList();
		if (p1 != null)
			parms.add(new CompoundVariable(p1));
		if (p2 != null)
			parms.add(new CompoundVariable(p2));
		cr.setParameters(parms);
		return cr;
	}

	// Create the CSVRead function and set its parameters.
	private static CSVRead setParams(String p1, String p2, String p3, String p4) throws Exception {
		CSVRead cr = new CSVRead();
		Collection parms = new LinkedList();
		parms.add(new CompoundVariable(p1));
		parms.add(new CompoundVariable(p2));
		parms.add(new CompoundVariable(p3));
		if (p4 != null)
			parms.add(new CompoundVariable(p4));
		cr.setParameters(parms);
		return cr;
	}

	// Create the StringFromFile function and set its parameters.
	private static StringFromFile SFFParams(String p1, String p2, String p3, String p4) throws Exception {
		StringFromFile sff = new StringFromFile();
		Collection parms = new LinkedList();
		if (p1 != null)
			parms.add(new CompoundVariable(p1));
		if (p2 != null)
			parms.add(new CompoundVariable(p2));
		if (p3 != null)
			parms.add(new CompoundVariable(p3));
		if (p4 != null)
			parms.add(new CompoundVariable(p4));
		sff.setParameters(parms);
		return sff;
	}

	// Create the SplitFile function and set its parameters.
	private static SplitFunction SplitParams(String p1, String p2, String p3) throws Exception {
		SplitFunction split = new SplitFunction();
		Collection parms = new LinkedList();
		parms.add(new CompoundVariable(p1));
		if (p2 != null)
			parms.add(new CompoundVariable(p2));
		if (p3 != null)
			parms.add(new CompoundVariable(p3));
		split.setParameters(parms);
		return split;
	}

	// Create the BeanShell function and set its parameters.
	private static BeanShell BSHFParams(String p1, String p2, String p3) throws Exception {
		BeanShell bsh = new BeanShell();
		bsh.setParameters(MakeParams(p1, p2, p3));
		return bsh;
	}

	private static Collection MakeParams(String p1, String p2, String p3) {
		Collection parms = new LinkedList();
		if (p1 != null)
			parms.add(new CompoundVariable(p1));
		if (p2 != null)
			parms.add(new CompoundVariable(p2));
		if (p3 != null)
			parms.add(new CompoundVariable(p3));
		return parms;
	}

	public static Test suite() throws Exception {
		TestSuite allsuites = new TestSuite();

		TestSuite bsh = new TestSuite("BeanShell");
		bsh.addTest(new PackageTest("BSH1"));
		allsuites.addTest(bsh);

		TestSuite suite = new TestSuite("SingleThreaded");
		suite.addTest(new PackageTest("CSVParams"));
		suite.addTest(new PackageTest("CSVNoFile"));
		suite.addTest(new PackageTest("CSVSetup"));
		suite.addTest(new PackageTest("CSVRun"));

		suite.addTest(new PackageTest("CSValias"));
		suite.addTest(new PackageTest("CSVBlankLine"));
		allsuites.addTest(suite);

		// Reset files
		suite.addTest(new PackageTest("CSVSetup"));
		TestSuite par = new ActiveTestSuite("Parallel");
		par.addTest(new PackageTest("CSVThread1"));
		par.addTest(new PackageTest("CSVThread2"));
		allsuites.addTest(par);

		TestSuite sff = new TestSuite("StringFromFile");
		sff.addTest(new PackageTest("SFFTest1"));
		sff.addTest(new PackageTest("SFFTest2"));
		sff.addTest(new PackageTest("SFFTest3"));
		sff.addTest(new PackageTest("SFFTest4"));
		sff.addTest(new PackageTest("SFFTest5"));
		allsuites.addTest(sff);

		TestSuite split = new TestSuite("SplitFunction");
		split.addTest(new PackageTest("SplitTest1"));
		allsuites.addTest(split);

		return allsuites;
	}

	private JMeterContext jmctx = null;

	private JMeterVariables vars = null;

	public void setUp() {
		jmctx = JMeterContextService.getContext();
		jmctx.setVariables(new JMeterVariables());
		vars = jmctx.getVariables();
	}

	public void BSH1() throws Exception {
		String fn = "testfiles/BeanShellTest.bsh";
		BeanShell bsh;
		try {
			bsh = BSHFParams(null, null, null);
			fail("Expected InvalidVariableException");
		} catch (InvalidVariableException e) {
		}

		try {
			bsh = BSHFParams("", "", "");
			fail("Expected InvalidVariableException");
		} catch (InvalidVariableException e) {
		}

		try {
			bsh = BSHFParams("", "", null);
			assertEquals("", bsh.execute());
		} catch (InvalidVariableException e) {
			fail("BeanShell not present");
		}

		bsh = BSHFParams("1", null, null);
		assertEquals("1", bsh.execute());

		bsh = BSHFParams("1+1", "VAR", null);
		assertEquals("2", bsh.execute());
		assertEquals("2", vars.get("VAR"));

		// Check some initial variables
		bsh = BSHFParams("return threadName", null, null);
		assertEquals(Thread.currentThread().getName(), bsh.execute());
		bsh = BSHFParams("return log.getClass().getName()", null, null);
		assertEquals(log.getClass().getName(), bsh.execute());

		// Check source works
		bsh = BSHFParams("source (\"testfiles/BeanShellTest.bsh\")", null, null);
		assertEquals("9876", bsh.execute());

		// Check persistence
		bsh = BSHFParams("${SCR1}", null, null);

		vars.put("SCR1", "var1=11");
		assertEquals("11", bsh.execute());

		vars.put("SCR1", "var2=22");
		assertEquals("22", bsh.execute());

		vars.put("SCR1", "x=var1");
		assertEquals("11", bsh.execute());

		vars.put("SCR1", "++x");
		assertEquals("12", bsh.execute());

		vars.put("VAR1", "test");
		vars.put("SCR1", "vars.get(\"VAR1\")");
		assertEquals("test", bsh.execute());

		// Check init file functioning
		JMeterUtils.getJMeterProperties().setProperty(BeanShell.INIT_FILE, fn);
		bsh = BSHFParams("${SCR2}", null, null);
		vars.put("SCR2", "getprop(\"" + BeanShell.INIT_FILE + "\")");
		assertEquals(fn, bsh.execute());// Check that bsh has read the file
		vars.put("SCR2", "getprop(\"avavaav\",\"default\")");
		assertEquals("default", bsh.execute());
		vars.put("SCR2", "++i");
		assertEquals("1", bsh.execute());
		vars.put("SCR2", "++i");
		assertEquals("2", bsh.execute());

	}

	public void SplitTest1() throws Exception {
		SplitFunction split = null;
		String src = "";

		try {
			split = SplitParams("a,b,c", null, null);
			fail("Expected InvalidVariableException (wrong number of parameters)");
		} catch (InvalidVariableException e) {
			// OK
		}
		src = "a,b,c";
		split = SplitParams(src, "VAR1", null);
		assertEquals(src, split.execute());
		assertEquals(src, vars.get("VAR1"));
		assertEquals("3", vars.get("VAR1_n"));
		assertEquals("a", vars.get("VAR1_1"));
		assertEquals("b", vars.get("VAR1_2"));
		assertEquals("c", vars.get("VAR1_3"));
		assertNull(vars.get("VAR1_4"));

		split = SplitParams(src, "VAR2", ",");
		assertEquals(src, split.execute());
		assertEquals(src, vars.get("VAR2"));
		assertEquals("3", vars.get("VAR2_n"));
		assertEquals("a", vars.get("VAR2_1"));
		assertEquals("b", vars.get("VAR2_2"));
		assertEquals("c", vars.get("VAR2_3"));
		assertNull(vars.get("VAR2_4"));

		src = "a|b|c";
		split = SplitParams(src, "VAR3", "|");
		assertEquals(src, split.execute());
		assertEquals(src, vars.get("VAR3"));
		assertEquals("3", vars.get("VAR3_n"));
		assertEquals("a", vars.get("VAR3_1"));
		assertEquals("b", vars.get("VAR3_2"));
		assertEquals("c", vars.get("VAR3_3"));
		assertNull(vars.get("VAR3_4"));

		src = "a|b||";
		split = SplitParams(src, "VAR4", "|");
		assertEquals(src, split.execute());
		assertEquals(src, vars.get("VAR4"));
		assertEquals("3", vars.get("VAR4_n"));
		assertEquals("a", vars.get("VAR4_1"));
		assertEquals("b", vars.get("VAR4_2"));
		assertEquals("?", vars.get("VAR4_3"));
		assertNull(vars.get("VAR4_5"));

		src = "a,,c";
		vars.put("VAR", src);
		split = SplitParams("${VAR}", "VAR", null);
		assertEquals(src, split.execute());
		assertEquals("3", vars.get("VAR_n"));
		assertEquals("a", vars.get("VAR_1"));
		assertEquals("?", vars.get("VAR_2"));
		assertEquals("c", vars.get("VAR_3"));
	}

	public void SFFTest1() throws Exception {
		StringFromFile sff1 = SFFParams("testfiles/SFFTest#.txt", "", "1", "3");
		assertEquals("uno", sff1.execute());
		assertEquals("dos", sff1.execute());
		assertEquals("tres", sff1.execute());
		assertEquals("cuatro", sff1.execute());
		assertEquals("cinco", sff1.execute());
		assertEquals("one", sff1.execute());
		assertEquals("two", sff1.execute());
		sff1.execute();
		sff1.execute();
		assertEquals("five", sff1.execute());
		assertEquals("eins", sff1.execute());
		sff1.execute();
		sff1.execute();
		sff1.execute();
		assertEquals("fuenf", sff1.execute());
		try {
			sff1.execute();
			fail("Should have thrown JMeterStopThreadException");
		} catch (JMeterStopThreadException e) {
			// expected
		}
	}

	public void SFFTest2() throws Exception {
		StringFromFile sff = SFFParams("testfiles/SFFTest1.txt", "", null, null);
		assertEquals("uno", sff.execute());
		assertEquals("dos", sff.execute());
		assertEquals("tres", sff.execute());
		assertEquals("cuatro", sff.execute());
		assertEquals("cinco", sff.execute());
		assertEquals("uno", sff.execute()); // Restarts
		assertEquals("dos", sff.execute());
		assertEquals("tres", sff.execute());
		assertEquals("cuatro", sff.execute());
		assertEquals("cinco", sff.execute());
	}

	public void SFFTest3() throws Exception {
		StringFromFile sff = SFFParams("testfiles/SFFTest1.txt", "", "", "");
		assertEquals("uno", sff.execute());
		assertEquals("dos", sff.execute());
		assertEquals("tres", sff.execute());
		assertEquals("cuatro", sff.execute());
		assertEquals("cinco", sff.execute());
		assertEquals("uno", sff.execute()); // Restarts
		assertEquals("dos", sff.execute());
		assertEquals("tres", sff.execute());
		assertEquals("cuatro", sff.execute());
		assertEquals("cinco", sff.execute());
	}

	public void SFFTest4() throws Exception {
		StringFromFile sff = SFFParams("xxtestfiles/SFFTest1.txt", "", "", "");
		assertEquals(StringFromFile.ERR_IND, sff.execute());
		assertEquals(StringFromFile.ERR_IND, sff.execute());
	}

	// Test that only loops twice
	public void SFFTest5() throws Exception {
		StringFromFile sff = SFFParams("testfiles/SFFTest1.txt", "", "", "2");
		assertEquals("uno", sff.execute());
		assertEquals("dos", sff.execute());
		assertEquals("tres", sff.execute());
		assertEquals("cuatro", sff.execute());
		assertEquals("cinco", sff.execute());
		assertEquals("uno", sff.execute());
		assertEquals("dos", sff.execute());
		assertEquals("tres", sff.execute());
		assertEquals("cuatro", sff.execute());
		assertEquals("cinco", sff.execute());
		try {
			sff.execute();
			fail("Should have thrown JMeterStopThreadException");
		} catch (JMeterStopThreadException e) {
			// expected
		}
	}

	// Function objects to be tested
	private static CSVRead cr1, cr2, cr3, cr4, cr5, cr6, cr7;

	// Helper class used to implement co-routine between two threads
	private static class Baton {
		void pass() {
			done();
			try {
				// System.out.println(">wait:"+Thread.currentThread().getName());
				wait(1000);
			} catch (InterruptedException e) {
				System.out.println(e);
			}
			// System.out.println("<wait:"+Thread.currentThread().getName());

		}

		void done() {
			// System.out.println(">done:"+Thread.currentThread().getName());
			notifyAll();
		}

	}

	private static Baton baton = new Baton();

	public void CSVThread1() throws Exception {
		Thread.currentThread().setName("One");
		synchronized (baton) {

			assertEquals("b1", cr1.execute(null, null));

			assertEquals("", cr4.execute(null, null));

			assertEquals("b2", cr1.execute(null, null));

			baton.pass();

			assertEquals("", cr4.execute(null, null));

			assertEquals("b4", cr1.execute(null, null));

			assertEquals("", cr4.execute(null, null));

			baton.pass();

			assertEquals("b3", cr1.execute(null, null));

			assertEquals("", cr4.execute(null, null));

			baton.done();
		}
	}

	public void CSVThread2() throws Exception {
		Thread.currentThread().setName("Two");
		Thread.sleep(500);// Allow other thread to start
		synchronized (baton) {

			assertEquals("b3", cr1.execute(null, null));

			assertEquals("", cr4.execute(null, null));

			baton.pass();

			assertEquals("b1", cr1.execute(null, null));

			assertEquals("", cr4.execute(null, null));

			assertEquals("b2", cr1.execute(null, null));

			baton.pass();

			assertEquals("", cr4.execute(null, null));

			assertEquals("b4", cr1.execute(null, null));

			baton.done();
		}
	}

	public void CSVRun() throws Exception {
		assertEquals("b1", cr1.execute(null, null));
		assertEquals("c1", cr2.execute(null, null));
		assertEquals("d1", cr3.execute(null, null));

		assertEquals("", cr4.execute(null, null));
		assertEquals("b2", cr1.execute(null, null));
		assertEquals("c2", cr2.execute(null, null));
		assertEquals("d2", cr3.execute(null, null));

		assertEquals("", cr4.execute(null, null));
		assertEquals("b3", cr1.execute(null, null));
		assertEquals("c3", cr2.execute(null, null));
		assertEquals("d3", cr3.execute(null, null));

		assertEquals("", cr4.execute(null, null));
		assertEquals("b4", cr1.execute(null, null));
		assertEquals("c4", cr2.execute(null, null));
		assertEquals("d4", cr3.execute(null, null));

		assertEquals("", cr4.execute(null, null));
		assertEquals("b1", cr1.execute(null, null));
		assertEquals("c1", cr2.execute(null, null));
		assertEquals("d1", cr3.execute(null, null));

		assertEquals("a1", cr5.execute(null, null));
		assertEquals("", cr6.execute(null, null));
		assertEquals("a2", cr5.execute(null, null));

		assertEquals("b2", cr7.execute(null, null));
		assertEquals("b3", cr7.execute(null, null));
	}

	public void CSVParams() throws Exception {
		try {
			setParams(null, null);
			fail("Should have failed with 0 params");
		} catch (InvalidVariableException e) {
		}
		try {
			setParams("", null);
			fail("Should have failed with 1 param");
		} catch (InvalidVariableException e) {
		}
		try {
			setParams("", "","",null);
			fail("Should have failed with 3 params");
		} catch (InvalidVariableException e) {
		}
		try {
			setParams("", "","","");
			fail("Should have failed with 4 params");
		} catch (InvalidVariableException e) {
		}
	}

	public void CSVSetup() throws Exception {
		cr1 = setParams("testfiles/test.csv", "1");
		cr2 = setParams("testfiles/test.csv", "2");
		cr3 = setParams("testfiles/test.csv", "3");
		cr4 = setParams("testfiles/test.csv", "next");
		cr5 = setParams("", "0");
		cr6 = setParams("", "next");
		cr7 = setParams("testfiles/test.csv", "1next");
	}

	public void CSValias() throws Exception {
		cr1 = setParams("testfiles/test.csv", "*A");
		cr2 = setParams("*A", "1");
		cr3 = setParams("*A", "next");

		cr4 = setParams("testfiles/test.csv", "*B");
		cr5 = setParams("*B", "2");
		cr6 = setParams("*B", "next");

		String s;

		s = cr1.execute(null, null); // open as *A
		assertEquals("", s);
		s = cr2.execute(null, null); // col 1, line 1, *A
		assertEquals("b1", s);

		s = cr4.execute(null, null);// open as *B
		assertEquals("", s);
		s = cr5.execute(null, null);// col2 line 1
		assertEquals("c1", s);

		s = cr3.execute(null, null);// *A next
		assertEquals("", s);
		s = cr2.execute(null, null);// col 1, line 2, *A
		assertEquals("b2", s);

		s = cr5.execute(null, null);// col2, line 1, *B
		assertEquals("c1", s);

		s = cr6.execute(null, null);// *B next
		assertEquals("", s);

		s = cr5.execute(null, null);// col2, line 2, *B
		assertEquals("c2", s);

	}

	public void CSVNoFile() throws Exception {
		String s;

		cr1 = setParams("xtestfiles/test.csv", "1");
		log.info("Expecting file not found");
		s = cr1.execute(null, null);
		assertEquals("", s);

		cr2 = setParams("xtestfiles/test.csv", "next");
		log.info("Expecting no entry for file");
		s = cr2.execute(null, null);
		assertEquals("", s);

		cr3 = setParams("xtestfiles/test.csv", "*ABC");
		log.info("Expecting file not found");
		s = cr3.execute(null, null);
		assertEquals("", s);

		cr4 = setParams("*ABC", "1");
		log.info("Expecting cannot open file");
		s = cr4.execute(null, null);
		assertEquals("", s);
	}

	// Check blank lines are treated as EOF
	public void CSVBlankLine() throws Exception {
		CSVRead csv1 = setParams("testfiles/testblank.csv", "1");
		CSVRead csv2 = setParams("testfiles/testblank.csv", "next");

		String s;

		for (int i = 1; i <= 2; i++) {
			s = csv1.execute(null, null);
			assertEquals("b1", s);

			s = csv2.execute(null, null);
			assertEquals("", s);

			s = csv1.execute(null, null);
			assertEquals("b2", s);

			s = csv2.execute(null, null);
			assertEquals("", s);
		}

	}
}
