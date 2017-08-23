package org.apache.jmeter.testelement;

import java.io.Serializable;

/**
 * @author    Michael Stover
 * Created   March 13, 2001
 * @version   $Revision: 323602 $ Last updated: $Date: 2003-10-19 15:30:05 -0700 (Sun, 19 Oct 2003) $
 */
public class WorkBench extends AbstractTestElement implements Serializable
{

    /**
     * Constructor for the WorkBench object.
     */
    public WorkBench(String name, boolean isRootNode)
    {
        setName(name);
    }

    public WorkBench()
    {
    }
}
