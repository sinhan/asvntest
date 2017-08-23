package org.apache.jmeter.junit;
import java.util.*;
import junit.framework.TestCase;
import org.apache.jmeter.gui.JMeterGUIComponent;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.ClassFinder;
import java.io.Serializable;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.save.SaveService;

/****************************************
 * Title: JMeter Description: Copyright: Copyright (c) 2000 Company: Apache
 *
 *@author    Michael Stover
 *@created   $Date: 2002-08-11 12:24:54 -0700 (Sun, 11 Aug 2002) $
 *@version   1.0
 ***************************************/

public class JMeterTest extends TestCase
{

	/****************************************
	 * !ToDo (Constructor description)
	 *
	 *@param name  !ToDo (Parameter description)
	 ***************************************/
	public JMeterTest(String name)
	{
		super(name);
	}

	/****************************************
	 * !ToDo
	 *
	 *@exception Exception  !ToDo (Exception description)
	 ***************************************/
	public void testGUIComponents() throws Exception
	{
		Iterator iter = getObjects(JMeterGUIComponent.class).iterator();
		while(iter.hasNext())
		{
			JMeterGUIComponent item = (JMeterGUIComponent)iter.next();
			if(item instanceof JMeterTreeNode)
			{
				continue;
			}
			this.assertEquals("Failed on " + item.getClass().getName(), 
					item.getStaticLabel(), item.getName());
			TestElement el = item.createTestElement();
			assertEquals("GUI-CLASS: Failed on " + item.getClass().getName(), item.getClass().getName(),
					el.getProperty(TestElement.GUI_CLASS));
			assertEquals("NAME: Failed on " + item.getClass().getName(), item.getName(),
					el.getProperty(TestElement.NAME));
			assertEquals("TEST-CLASS: Failed on " + item.getClass().getName(),
					el.getClass().getName(), el.getProperty(TestElement.TEST_CLASS));
			el.setProperty(TestElement.NAME, "hey, new name!:");
			el.setProperty("NOT","Shouldn't be here");
			TestElement el2 = item.createTestElement();
			assertNull("GUI-CLASS: Failed on " + item.getClass().getName(),
			el2.getProperty("NOT"));
			el = SaveService.createTestElement(SaveService.getConfigForTestElement(null,
					el));
			item.configure(el);
			assertEquals("CONFIGURE-TEST: Failed on " + item.getClass().getName(),
					el.getProperty(TestElement.NAME), item.getName());
		}
	}

	/****************************************
	 * !ToDo
	 *
	 *@exception Exception  !ToDo (Exception description)
	 ***************************************/
	public void testTestElements() throws Exception
	{
		Iterator iter = getObjects(TestElement.class).iterator();
		while(iter.hasNext())
		{
			TestElement item = (TestElement)iter.next();
			checkElementCloning(item);
			assertTrue(item.getClass().getName()+" must implement Serializable",
					item instanceof Serializable);
		}
	}


	/****************************************
	 * !ToDoo (Method description)
	 *
	 *@param extendsClass   !ToDo (Parameter description)
	 *@return               !ToDo (Return description)
	 *@exception Exception  !ToDo (Exception description)
	 ***************************************/
	protected Collection getObjects(Class extendsClass) throws Exception
	{
		Iterator classes = ClassFinder.findClassesThatExtend(new Class[]{extendsClass}).iterator();
		List objects = new LinkedList();
		while(classes.hasNext())
		{
			objects.add(Class.forName((String)classes.next()).newInstance());
		}
		return objects;
	}

	private void cloneTesting(TestElement item, TestElement clonedItem)
	{
		this.assertTrue(item != clonedItem);
		this.assertEquals("CLONE-SAME-CLASS: testing " + item.getClass().getName(),
				item.getClass().getName(), clonedItem.getClass().getName());
	}

	private void checkElementCloning(TestElement item)
	{
		TestElement clonedItem = (TestElement)item.clone();
		cloneTesting(item, clonedItem);
		Iterator iter2 = item.getPropertyNames().iterator();
		while(iter2.hasNext())
		{
			Object item2 = iter2.next();
			if(item2 instanceof TestElement)
			{
				checkElementCloning((TestElement)item2);
			}
		}
	}

}
