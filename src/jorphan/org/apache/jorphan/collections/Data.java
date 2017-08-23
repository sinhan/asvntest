/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
 

package org.apache.jorphan.collections;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/****************************************************************
 Use this class to store database-like data.  This class uses rows
 and columns to organize its data.  It has some convenience methods
 that allow fast loading and retrieval of the data into and out of
 string arrays.  It is also handy for reading CSV files.
@author Michael Stover (mstover1 at apache.org)
 ******************************************************************/
public class Data implements Serializable
{
  Map data;
  Map iterators = new HashMap();
 // Hashtable dataLine;
  ArrayList header;
  // saves current position in data Vector
  int currentPos,size;

/*******************************************************************
 Constructor - takes no arguments.
*****************************************************************/
  public Data()
  {
  	header = new ArrayList();
	 data=new HashMap();
	 currentPos=-1;
	 size = currentPos+1;
  }

/***************************************************************
  Replaces the given header name with a new header name.
@param oldHeader Old header name.
@param newHeader New header name.
**************************************************************/
  public void replaceHeader(String oldHeader,String newHeader)
  {
	 List tempList;
	 int index=header.indexOf(oldHeader);
	 header.set(index,newHeader);
	 tempList = (List)data.remove(oldHeader);
	 data.put(newHeader,tempList);
  }
/*************************************************************
Adds the rows of the given Data object to this Data object.
@param d data object to be appended to this one.
**************************************************************/
  public void append(Data d)
  {
	 boolean valid=true;
	 String[] headers=getHeaders();
	 String[] dHeaders=d.getHeaders();
	 if(headers.length!=dHeaders.length)
		valid=false;
	 else
		for(int count = 0;count<dHeaders.length;count++)
		  if(!header.contains(dHeaders[count]))
			 valid=false;
	 if(valid)
	 {
		currentPos=size;
		d.reset();
		while(d.next())
		{
		  for(int count = 0;count<headers.length;count++)
			 addColumnValue(headers[count],d.getColumnValue(headers[count]));
		}
	 }
  }

  /***********************************
	Get the number of the current row.
@return  Integer representing the current row.
  *************************************/
  public int getCurrentPos()
  {
	 return currentPos;
  }// end method

  /******************************************************
  Removes the current row.
  ******************************************************/
  public void removeRow()
  {
	 List tempList;
	 Iterator it = data.keySet().iterator();
	 if(currentPos>-1 && currentPos<size)
	 {
		while(it.hasNext())
		{
		  tempList = (List)data.get(it.next());
		  tempList.remove(currentPos);
		}
		if(currentPos > 0)
		{
			currentPos--;
		}
		size--;
	 }
  } // End Method
  
  public void removeRow(int index)
  {
  	if(index < size)
  	{
  		setCurrentPos(index);
  		removeRow();
  	}
  }
  
  public void addRow()
  {
  	String[] headers = getHeaders();
  	List tempList = new ArrayList();
  	for(int i = 0;i < headers.length;i++)
  	{
  		if((tempList = (ArrayList)data.get(header.get(i))) == null)
	 	{
			tempList = new ArrayList();
			data.put(headers[i],tempList);
	 	}
	 	tempList.add("");
  	}
  	size = tempList.size();
  	setCurrentPos(size - 1);
  }

  /******************************************************
  Sets the current pos. If value sent to method is not a valid number,
  the current position is set to one higher than the maximum.
@param  r position to set to.
  ******************************************************/
  public void setCurrentPos(int r)
  {
	 currentPos=r;
  } // End Method

  /******************************************************
  Sorts the data using a given row as the sorting criteria.  A boolean
	value indicates whether to sort ascending or descending.
@param column Name of column to use as sorting criteria.
@param asc boolean value indicating whether to sort ascending or descending.  True for
	asc, false for desc.  Currently this feature is not enabled and all sorts are asc.
  ******************************************************/
  public void sort(String column,boolean asc)
  {
	 sortData(column,0,size);
  } // End Method

  private void swapRows(int row1,int row2)
  {
	 List temp;
	 Object o;
	 Iterator it = data.keySet().iterator();
	 while(it.hasNext())
	 {
		temp = (List)data.get(it.next());
		o = temp.get(row1);
		temp.set(row1,temp.get(row2));
		temp.set(row2,o);
	 }
  }

	 /************************************************************
	Private method that implements the quicksort algorithm to sort the rows of the Data object.
@param column Name of column to use as sorting criteria.
@param starting index (for quicksort algorithm).
@param ending index (for quicksort algorithm).
	***********************************************************/
  private void sortData(String column,int start,int end)
  {
	 int x=start,y=end-1;
	 String basis=((List)data.get(column)).get((int)((x+y)/2)).toString();
	 Object temp;
	 if(x==y)
		return;
	 while(x<=y)
	 {
		while(x<end && ((List)data.get(column)).get(x).toString().compareTo(basis)<0)
		  x++;
		while(y>=(start-1) && ((List)data.get(column)).get(y).toString().compareTo(basis)>0)
		  y--;
		if(x<=y)
		{
		  swapRows(x,y);
		  x++;
		  y--;
		}
	 }
	 if(x==y)
		x++;
	 y=end-x;
	 if(x>0)
		sortData(column,start,x);
	 if(y>0)
		sortData(column,x,end);
  }

  /***********************************
	Gets the number of rows in the Data object.
@return   Returns number of rows in Data object.
  *************************************/
  public int size()
  {
	 return size;
  }// end method

  /*******************************************************
	Adds a value into the Data set at the current row, using a column name
	to find the column in which to insert the new value.
@param column The name of the column to set.
@param value Value to set into column.
  **********************************************************/
  public void addColumnValue(String column,Object value)
  {
	 ArrayList tempList;
	 if((tempList = (ArrayList)data.get(column)) == null)
	 {
		tempList = new ArrayList();
		data.put(column,tempList);
	 }
	 int s = tempList.size();
	 if(currentPos==-1)
		currentPos=size;
	 if(currentPos>=size)
		  size = currentPos+1;
	 while(currentPos>s)
	 {
		s++;
		tempList.add(null);
	 }
	 if(currentPos == s)
		tempList.add(value);
	 else
		tempList.set(currentPos,value);
  }// end method

/**************************************************************
Returns the row number where a certain value is.
@param column Column to be searched for value.
@param value object in Search of.
@return row # where value exists.
**************************************************************/
  public int findValue(String column,Object value)
  {
	 List list = (List)data.get(column);
	 int ret=-1;
	 ret = list.indexOf(value);
	 return ret;
  }

  /*******************************************************
	Sets the value in the Data set at the current row, using a column name
	to find the column in which to insert the new value.
@param column The name of the column to set.
@param value Value to set into column.
  **********************************************************/
  public void setColumnValue(String column,Object value)
  {
	 List tempList;
	 if((tempList = (List)data.get(column)) == null)
	 {
		tempList = new ArrayList();
		data.put(column,tempList);
	 }
	 if(currentPos==-1)
		currentPos=0;
	 if(currentPos>=size)
	 {
		  size++;
		  tempList.add(value);
	 }
	 else if(currentPos >= tempList.size())
	 {
	 	tempList.add(value);
	 }
	 else
		tempList.set(currentPos,value);
  }// end method

  /******************************************************
  Checks to see if a column exists in the Data object.
@param  column Name of column header to check for.
@return True or False depending on whether the column exists.
  ******************************************************/
  public boolean hasHeader(String column)
  {
	 return data.containsKey(column);
  } // End Method

  /***********************************
	Sets the current position of the Data set to the next row.
@return  True if there is another row.  False if there are no more rows.
  *************************************/
  public boolean next()
  {
	 if(++currentPos>=size)
		return false;
	 else return true;
  }// end method

  /***********************************
	Sets the current position of the Data set to the previous row.
@return  True if there is another row.  False if there are no more rows.
  *************************************/
  public boolean previous()
  {
	 if(--currentPos<0)
		return false;
	 else return true;
  }// end method

  /***********************************
  Resets the current position of the data set to just before the first element.
  *************************************/
  public void reset()
  {
	 currentPos=-1;
  }// end method

  /***********************************
  Gets the value in the current row of the given column.
@param column Name of the column.
@return Returns an Object which holds the value of the column.
  *************************************/
  public Object getColumnValue(String column)
  {

	 try{
		if(currentPos<size)
		  return ((List)data.get(column)).get(currentPos);
		else
		  return null;
	 }catch(Exception e){return null;}
  }// end method

		  /***********************************
  Gets the value in the current row of the given column.
@param column index of the column (starts at 0).
@return Returns an Object which holds the value of the column.
  *************************************/
  public Object getColumnValue(int column)
  {
	 String columnName = (String)header.get(column);
	 try{
		if(currentPos<size)
		  return ((List)data.get(columnName)).get(currentPos);
		else
		  return null;
	 }catch(Exception e){return null;}
  }// end method
  
  public Object getColumnValue(int column,int row)
  {
  	setCurrentPos(row);
  	return getColumnValue(column);
  }
  
  public void removeColumn(int col)
  {
  	String columnName = (String)header.get(col);
  	data.remove(columnName);
  	header.remove(columnName);
  }


  /*****************************************************************
	Sets the headers for the data set.  Each header represents a column
	of data.  Each row's data can be gotten with the column header name,
	which will always be a string.
@param h Array of strings representing the column headers.
	**************************************************************/
  public void setHeaders(String[] h)
  {
	 int x=0;
	 header=new ArrayList(h.length);
	 for(x = 0;x<h.length;x++)
	 {
		header.add(h[x]);
		data.put(h[x],new ArrayList());
	 }
  }


  /*****************************************************************
	 Returns a String array of the column headers.
@return Array of strings of the column headers.
  *****************************************************************/
  public String[] getHeaders()
  {
	 String[] r=new String[header.size()];
	 if ( r.length>0 )
	 {
		r=(String[])header.toArray(r);
	 }
	 return r;
  } //end of Method

  /*****************************************************************
	 This method will retrieve every entry in a certain column.  It returns an array of Objects
	from the column.
@param columnName String value, name of the column.
@return Array of Objects representing the data.
  *****************************************************************/
  public List getColumnAsObjectArray(String columnName)
  {
	 return (List)data.get(columnName);
  }// end of Method



  /*****************************************************************
	 This method will retrieve every entry in a certain column.  It returns an array of strings
	from the column. Even if the data are not strings, they will be returned as
	strings in this method.
@param columnName String value, name of the column.
@return Array of Strings representing the data.
  *****************************************************************/
  public String[] getColumn(String columnName)
  {
	 String[] returnValue;
	 Object o;
	 List temp=(List)data.get(columnName);
	 if(temp != null)
	 {
		returnValue = new String[temp.size()];
		Iterator it = temp.iterator();
		int index = 0;
		while(it.hasNext())
		{
		  o = it.next();
		  if(o != null)
		  {
			 if(o instanceof String)
				returnValue[index++] = (String)o;
			 else
				returnValue[index++] = o.toString();
		  }
		}
	 }
	 else
		returnValue = new String[0];
	 return returnValue;
  }// end of Method

  /*****************************************************************
	 Use this method to set the entire data set.  It takes an array of
	strings.  It uses the first row as the headers, and the next rows
	as the data elements.  Delimiter represents the delimiting character(s)
	that separate each item in a data row.
@param contents Array of strings, the first element is a list of the column
	headers, the next elements each represent a single row of data.
@param delimiter The delimiter character that separates columns within
	the string array.
	  *****************************************************************/
  public void setData(String[] contents, String delimiter)
  {
		setHeaders(contents[0].split(delimiter));
		int x=1;
		while(x<contents.length)
		{
			setLine(contents[x++].split(delimiter));
		}
	 } //end of Method

  /*
	Deletes a header from the Data object.
	Takes the column name as input.  It will delete the entire
	column.

  public void deleteHeader(String s)
  {

  }*/
  
  public void setColumnData(String colName,Object value)
  {
  	List list = this.getColumnAsObjectArray(colName);
  	for(int x = 0;x < list.size();x++)
  	{
  		list.set(x,value);
  	}
  }
  
  public void setColumnData(int col,List data)
  {
  	reset();
  	Iterator iter = data.iterator();
  	String columnName = (String)header.get(col);
  	while(iter.hasNext())
  	{
  		next();
  		setColumnValue(columnName,iter.next());
  	}
  }


  /***************************************************
	Adds a header name to the Data object.
@param s name of header.
	*************************************************/
  public void addHeader(String s)
  {
	header.add(s);
	data.put(s,new ArrayList(size()));
  }

  /************************************************************
	Sets a row of data using an array of strings as input.  Each
	value in the array represents a column's value in that row.
	Assumes the order will be the same order in which the headers
	were added to the data set.
@param line array of strings representing column values.
	*********************************************************/
  public void setLine(String[] line)
  {
	 List tempList;
	 String[] h = getHeaders();
	 for(int count = 0;count<h.length;count++)
	 {
		tempList = (List)data.get(h[count]);
		if(count<line.length && line[count].length()>0)
		  tempList.add(line[count]);
		else
		  tempList.add("N/A");
	 }
	 size++;
  }

  /************************************************************
	Sets a row of data using an array of strings as input.  Each
	value in the array represents a column's value in that row.
	Assumes the order will be the same order in which the headers
	were added to the data set.
@param line array of strings representing column values.
@param deflt Default value to be placed in data if line is not as long as headers.
	*********************************************************/
  public void setLine(String[] line,String deflt)
  {
	 List tempList;
	 String[] h = getHeaders();
	 for(int count = 0;count<h.length;count++)
	 {
		tempList = (List)data.get(h[count]);
		if(count<line.length && line[count].length()>0)
		  tempList.add(line[count]);
		else
		  tempList.add(deflt);
	 }
	 size++;
  }

  /******************************************************************
	Returns all the data in the Data set as an array of strings.  Each
	array gives a row of data, each column separated by tabs.
@return array of strings.

	*******************************************************************/
  public String[] getDataAsText()
  {
	 List tempList;
	 StringBuffer temp=new StringBuffer("");
	 String[] line=new String[size+1];
	 String[] elements = getHeaders();
	 for(int count = 0;count<elements.length;count++)
	 {
		temp.append(elements[count]);
		if(count+1<elements.length)
		  temp.append("\t");
	 }
	 line[0] = temp.toString();
	 reset();
	 int index = 1;
	 while(next())
	 {
		temp = new StringBuffer("");
		for(int count = 0;count<elements.length;count++)
		{
		  temp.append(getColumnValue(count));
		  if(count+1<elements.length)
			 temp.append("\t");
		}
		line[index++] = temp.toString();
	 }
	 return line;
  }

  public String toString()
  {
	 String[] contents = getDataAsText();
	 StringBuffer sb = new StringBuffer();
	 boolean first = true;
	 for(int x = 0;x < contents.length;x++)
	 {
		if(!first)
		  sb.append("\n");
		else first = false;
		sb.append(contents[x]);
	 }
	 return sb.toString();
  }
}
