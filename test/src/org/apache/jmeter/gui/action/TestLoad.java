/*
 * Created on Jun 19, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.apache.jmeter.gui.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

import junit.framework.TestCase;

import org.apache.jmeter.save.SaveService;
import org.apache.jorphan.collections.HashTree;


/**
  * 
  * @version    $Revision: 324711 $  Last updated: $Date: 2004-06-19 17:32:53 -0700 (Sat, 19 Jun 2004) $
  */
 public class TestLoad extends TestCase
 {
     File testFile1,
         testFile2,
         testFile3,
         testFile4,
         testFile5,
         testFile6,
         testFile7,
         testFile8,
         testFile9,
         testFile10,
         testFile11,
         testFile12,
         testFile13;
     static Load loader = new Load();

     public TestLoad(String name)
     {
         super(name);
     }

     public void setUp()
     {
         testFile1 = //TODO: not used - why?
             new File(
                 System.getProperty("user.dir") + "/testfiles",
                 "Test Plan.jmx");
         testFile2 = //TODO: not used - why?
             new File(
                 System.getProperty("user.dir") + "/testfiles",
                 "Modification Manager.jmx");
         testFile3 =
             new File(
                 System.getProperty("user.dir") + "/testfiles",
                 "proxy.jmx");
         testFile4 =
             new File(
                 System.getProperty("user.dir") + "/testfiles",
                 "AssertionTestPlan.jmx");
         testFile5 =
             new File(
                 System.getProperty("user.dir") + "/testfiles",
                 "AuthManagerTestPlan.jmx");
         testFile6 =
             new File(
                 System.getProperty("user.dir") + "/testfiles",
                 "HeaderManagerTestPlan.jmx");
         testFile7 =
             new File(
                 System.getProperty("user.dir") + "/testfiles",
                 "InterleaveTestPlan.jmx");
         testFile8 =
             new File(
                 System.getProperty("user.dir") + "/testfiles",
                 "InterleaveTestPlan2.jmx");
         testFile9 =
             new File(
                 System.getProperty("user.dir") + "/testfiles",
                 "LoopTestPlan.jmx");
         testFile10 =
             new File(
                 System.getProperty("user.dir") + "/testfiles",
                 "OnceOnlyTestPlan.jmx");
         testFile11 =
             new File(
                 System.getProperty("user.dir") + "/testfiles",
                 "ProxyServerTestPlan.jmx");
         testFile12 =
             new File(
                 System.getProperty("user.dir") + "/testfiles",
                 "SimpleTestPlan.jmx");
         testFile13 = //TODO: not used - why?
             new File(
                 System.getProperty("user.dir") + "/testfiles",
                 "URLRewritingExample.jmx");
     }

     public void testFile3() throws Exception
     {
         assertTree(getTree(testFile3));
     }

     private void assertTree(HashTree tree) throws Exception
     {
         assertTrue(
             tree.getArray()[0]
                 instanceof org.apache.jmeter.testelement.TestPlan);
     }

     public void testFile4() throws Exception
     {
         assertTree(getTree(testFile4));
     }

     public void testFile5() throws Exception
     {
         assertTree(getTree(testFile5));
     }

     public void testFile6() throws Exception
     {
         assertTree(getTree(testFile6));
     }

     public void testFile7() throws Exception
     {
         assertTree(getTree(testFile7));
     }

     public void testFile8() throws Exception
     {
         assertTree(getTree(testFile8));
     }

     public void testFile9() throws Exception
     {
         assertTree(getTree(testFile9));
     }

     public void testFile10() throws Exception
     {
         assertTree(getTree(testFile10));
     }

     public void testFile11() throws Exception
     {
         assertTree(getTree(testFile11));
     }

     public void testFile12() throws Exception
     {
         assertTree(getTree(testFile12));
     }

     private HashTree getTree(File f) throws Exception
     {
         HashTree tree = SaveService.loadTree(new FileInputStream(f));
         return tree;
     }
 }