/*
 * org.openmicroscopy.shoola.agents.rnd.QuantumMappingManager
 *
 *------------------------------------------------------------------------------
 *
 *  Copyright (C) 2004 Open Microscopy Environment
 *      Massachusetts Institute of Technology,
 *      National Institutes of Health,
 *      University of Dundee
 *
 *
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *------------------------------------------------------------------------------
 */

package org.openmicroscopy.shoola.agents.rnd.pane;

//Java imports

//Third-party libraries

//Application-internal dependencies

/** 
 * 
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author  <br>Andrea Falconi &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:a.falconi@dundee.ac.uk">
 * 					a.falconi@dundee.ac.uk</a>
 * @version 2.2 
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */
class QuantumMappingManager
{
	/** minimum value (real value) of the input window. */
	private int 				minimum;
	
	/** maximum value (real value) of the input window. */
	private int 				maximum;
	
	/** The current window input start value. */
	private int					curStart;
	
	/** The current window input end value. */
	private int					curEnd;
	
	/** The current window output start value. */
	private int					curOutputStart;
	
	/** The current window output end value. */
	private int					curOutputEnd;
	
	private QuantumMapping		view;
	
	QuantumMappingManager(QuantumMapping view)
	{
		this.view = view;
	}
	
	void setCurStart(int v)
	{
		curStart = v;
	}
	
	void setCurEnd(int v)
	{
		curEnd = v;
	}
	
	int getCurStart()
	{
		return curStart;
	}
	
	int getCurEnd()
	{
		return curEnd;
	}
	
	void setMinimum(int minimum)
	{
		this.minimum = minimum;
	}
	
	void setMaximum(int maximum)
	{
		this.maximum = maximum;
	}

	int getMaximum()
	{
		return maximum;
	}

	int getMinimum()
	{
		return minimum;
	}
	
	int getCurOutputEnd()
	{
		return curOutputEnd;
	}

	int getCurOutputStart()
	{
		return curOutputStart;
	}
	
	void setCurOutputEnd(int i)
	{
		curOutputEnd = i;
	}

	void setCurOutputStart(int i)
	{
		curOutputStart = i;
	}

	void setStrategy()
	{
		//TODO: update strategy
	}

	/**
	 * 
	 * @param index		index of the wavelength.
	 */
	void setWavelength(int index)
	{
	}
	
	/** 
	 * Resize the input window and forward event to the different views.
	 *
	 * @param value	real input value.
	 */
	void setInputWindowStart(int value)
	{
		//TODO: update window
		curStart = value;
		DomainPaneManager dpManager = view.getDomainPane().getManager();
		GraphicsRepresentationManager 
			grManager = view.getGRepresentation().getManager();
		dpManager.setInputWindowStart(value);
		grManager.setInputWindowStart(value);			
	}
	
	/** 
	 * Set the window input and synchronize the different view.
	 *
	 * @param value	real input value.
	 */
	void setInputWindowEnd(int value)
	{
		curEnd = value;
		//TODO: update window
		DomainPaneManager dpManager = view.getDomainPane().getManager();
  		GraphicsRepresentationManager 
	 	 	grManager = view.getGRepresentation().getManager();
  		dpManager.setInputWindowStart(value);
  		grManager.setInputWindowStart(value);	
	}
	
	
	
	
}
