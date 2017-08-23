/*
 * Created on May 16, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.apache.jmeter.protocol.jdbc.sampler;

import java.beans.PropertyDescriptor;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.TextAreaEditor;

/**
 * @author mstover
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class JDBCSamplerBeanInfo extends BeanInfoSupport
{
   
   /**
    * @param beanClass
    */
   public JDBCSamplerBeanInfo()
   {
      super(JDBCSampler.class);
      
      createPropertyGroup("varName", new String[]{"dataSource","queryOnly"});
      
      createPropertyGroup("sql", new String[]{"query"});
      
      PropertyDescriptor p = property("dataSource");
      p.setValue(NOT_UNDEFINED, Boolean.TRUE);
      p.setValue(DEFAULT, "");
      
      p = property("queryOnly");
      p.setValue(NOT_UNDEFINED,Boolean.TRUE);
      p.setValue(DEFAULT,new Boolean(true));
      
      p = property("query");
      p.setValue(NOT_UNDEFINED, Boolean.TRUE);
      p.setValue(DEFAULT, "");
      p.setPropertyEditorClass(TextAreaEditor.class);
      
   }
}
