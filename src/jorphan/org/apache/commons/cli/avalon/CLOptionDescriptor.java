/* 
 * Copyright 2002-2005 The Apache Software Foundation
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.cli.avalon;
//Renamed from org.apache.avalon.excalibur.cli

/**
 * Basic class describing an type of option.
 * Typically, one creates a static array of <code>CLOptionDescriptor</code>s,
 * and passes it to {@link CLArgsParser#CLArgsParser(String[], CLOptionDescriptor[])}.
 *
 * @version $Revision: 325222 $ $Date: 2005-03-18 07:27:20 -0800 (Fri, 18 Mar 2005) $
 * @see CLArgsParser
 * @see CLUtil
 */
public final class CLOptionDescriptor
{
    /** Flag to say that one argument is required */
    public static final int ARGUMENT_REQUIRED = 1 << 1;
    /** Flag to say that the argument is optional */
    public static final int ARGUMENT_OPTIONAL = 1 << 2;
    /** Flag to say this option does not take arguments */
    public static final int ARGUMENT_DISALLOWED = 1 << 3;
    /** Flag to say this option requires 2 arguments */
    public static final int ARGUMENTS_REQUIRED_2 = 1 << 4;
    /** Flag to say this option may be repeated on the command line */
    public static final int DUPLICATES_ALLOWED = 1 << 5;

    private final int m_id;
    private final int m_flags;
    private final String m_name;
    private final String m_description;
    private final int[] m_incompatible;

    /**
     * Constructor.
     *
     * @param name the name/long option
     * @param flags the flags
     * @param id the id/character option
     * @param description description of option usage
     */
    public CLOptionDescriptor( final String name,
            final int flags,
            final int id,
            final String description )
    {
        this( name, flags, id, description,
                ((flags & CLOptionDescriptor.DUPLICATES_ALLOWED) > 0)
                ? new int[0] : new int[]{id} );
    }

    /**
     * Constructor.
     *
     * @param name the name/long option
     * @param flags the flags
     * @param id the id/character option
     * @param description description of option usage
     * @param incompatible an array listing the ids of all incompatible options
     * @deprecated use the version with the array of CLOptionDescriptor's
     */
    public CLOptionDescriptor( final String name,
            final int flags,
            final int id,
            final String description,
            final int[] incompatible )
    {
        m_id = id;
        m_name = name;
        m_flags = flags;
        m_description = description;
        m_incompatible = incompatible;

        int modeCount = 0;
        if( (ARGUMENT_REQUIRED & flags) == ARGUMENT_REQUIRED )
        {
            modeCount++;
        }
        if( (ARGUMENT_OPTIONAL & flags) == ARGUMENT_OPTIONAL )
        {
            modeCount++;
        }
        if( (ARGUMENT_DISALLOWED & flags) == ARGUMENT_DISALLOWED )
        {
            modeCount++;
        }
        if( (ARGUMENTS_REQUIRED_2 & flags) == ARGUMENTS_REQUIRED_2 )
        {
            modeCount++;
        }

        if( 0 == modeCount )
        {
            final String message = "No mode specified for option " + this;
            throw new IllegalStateException( message );
        }
        else if( 1 != modeCount )
        {
            final String message = "Multiple modes specified for option " + this;
            throw new IllegalStateException( message );
        }
    }

    /**
     * Constructor.
     *
     * @param name the name/long option
     * @param flags the flags
     * @param id the id/character option
     * @param description description of option usage
     */
    public CLOptionDescriptor( final String name,
            final int flags,
            final int id,
            final String description,
            final CLOptionDescriptor[] incompatible )
    {
        m_id = id;
        m_name = name;
        m_flags = flags;
        m_description = description;

        m_incompatible = new int[incompatible.length];
        for( int i = 0; i < incompatible.length; i++ )
        {
            m_incompatible[i] = incompatible[i].getId();
        }
    }

    /**
     * @deprecated Use the correctly spelled {@link #getIncompatible} instead.
     * @return the array of incompatible option ids
     */
    protected final int[] getIncompatble()
    {
        return getIncompatible();
    }

    /**
     * Get the array of incompatible option ids.
     *
     * @return the array of incompatible option ids
     */
    protected final int[] getIncompatible()
    {
        return m_incompatible;
    }

    /**
     * Retrieve textual description.
     *
     * @return the description
     */
    public final String getDescription()
    {
        return m_description;
    }

    /**
     * Retrieve flags about option.
     * Flags include details such as whether it allows parameters etc.
     *
     * @return the flags
     */
    public final int getFlags()
    {
        return m_flags;
    }

    /**
     * Retrieve the id for option.
     * The id is also the character if using single character options.
     *
     * @return the id
     */
    public final int getId()
    {
        return m_id;
    }

    /**
     * Retrieve name of option which is also text for long option.
     *
     * @return name/long option
     */
    public final String getName()
    {
        return m_name;
    }

    /**
     * Convert to String.
     *
     * @return the converted value to string.
     */
    public final String toString()
    {
        final StringBuffer sb = new StringBuffer();
        sb.append( "[OptionDescriptor " );
        sb.append( m_name );
        sb.append( "[OptionDescriptor " );
        sb.append( m_name );
        sb.append( ", " );
        sb.append( m_id );
        sb.append( ", " );
        sb.append( m_flags );
        sb.append( ", " );
        sb.append( m_description );
        sb.append( " ]" );
        return sb.toString();
    }
}
