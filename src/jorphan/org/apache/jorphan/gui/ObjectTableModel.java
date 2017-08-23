// $Header$
/*
 * Copyright 2003-2004 The Apache Software Foundation.
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

package org.apache.jorphan.gui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @version $Revision: 324262 $
 */
public class ObjectTableModel extends DefaultTableModel
{
    private static Logger log = LoggingManager.getLoggerForClass();
    private transient ArrayList objects = new ArrayList();
    private transient List headers = new ArrayList();
    private transient ArrayList classes = new ArrayList();
    private transient Class objectClass;

    private transient ArrayList setMethods = new ArrayList();
    private transient ArrayList getMethods = new ArrayList();

    public ObjectTableModel(String[] headers, String[] propertyNames, Class[] propertyClasses, Class[] renderClasses, Object sampleObject)
    {
        this.headers.addAll(Arrays.asList(headers));
        this.classes.addAll(Arrays.asList(renderClasses));
        objectClass = sampleObject.getClass();
        Class[] emptyClasses = new Class[0];
        for (int i = 0; i < propertyNames.length; i++)
        {
            propertyNames[i] =
                propertyNames[i].substring(0, 1).toUpperCase()
                    + propertyNames[i].substring(1);
            try
            {
                if (!propertyClasses[i].equals(Boolean.class)
                    && !propertyClasses[i].equals(boolean.class))
                {
                    getMethods.add(
                        objectClass.getMethod(
                            "get" + propertyNames[i],
                            emptyClasses));
                }
                else
                {
                    getMethods.add(
                        objectClass.getMethod(
                            "is" + propertyNames[i],
                            emptyClasses));
                }
                setMethods.add(objectClass.getMethod("set" + propertyNames[i], new Class[] { propertyClasses[i] }));
            }
            catch (NoSuchMethodException e)
            {
                log.error("Invalid Method name for class: " + objectClass, e);
            }
        }
    }

    public Iterator iterator()
    {
        return objects.iterator();
    }

    public void clearData()
    {
        int size = getRowCount();
        objects.clear();
        super.fireTableRowsDeleted(0, size);
    }

    public void addRow(Object value)
    {
        objects.add(value);
        super.fireTableRowsInserted(objects.size() - 1, objects.size());
    }

    /**
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount()
    {
        return headers.size();
    }

    /**
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int col)
    {
        return (String) headers.get(col);
    }

    /**
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount()
    {
        if (objects == null)
        {
            return 0;
        }
        return objects.size();
    }

    /**
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col)
    {
        Object value = objects.get(row);
        Method getMethod = (Method) getMethods.get(col);
        try
        {
            return getMethod.invoke(value, new Object[0]);
        }
        catch (IllegalAccessException e)
        {
            log.error("Illegal method access", e);
        }
        catch (InvocationTargetException e)
        {
            log.error("incorrect method access", e);
        }
        return null;
    }

    /**
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int arg0, int arg1)
    {
        return true;
    }

    /**
     * @see javax.swing.table.DefaultTableModel#moveRow(int, int, int)
     */
    public void moveRow(int start, int end, int to)
    {
        List subList = objects.subList(start, end);
        for (int x = end - 1; x >= start; x--)
        {
            objects.remove(x);
        }
        objects.addAll(to, subList);
        super.fireTableChanged(new TableModelEvent(this));
    }

    /**
     * @see javax.swing.table.DefaultTableModel#removeRow(int)
     */
    public void removeRow(int row)
    {
        objects.remove(row);
        super.fireTableRowsDeleted(row, row);
    }

    /**
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object cellValue, int row, int col)
    {
        if (row < objects.size())
        {
            Object value = objects.get(row);
            if (col < setMethods.size())
            {
                Method setMethod = (Method) setMethods.get(col);
                try
                {
                    setMethod.invoke(value, new Object[] { cellValue });
                }
                catch (IllegalAccessException e)
                {
                    log.error("Illegal method access", e);
                }
                catch (InvocationTargetException e)
                {
                    log.error("incorrect method access", e);
                }
                super.fireTableDataChanged();
            }
        }
    }

    /**
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    public Class getColumnClass(int arg0)
    {
        return (Class) classes.get(arg0);
    }

}
