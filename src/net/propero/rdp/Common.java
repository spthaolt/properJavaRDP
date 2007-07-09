/* Common.java
 * Component: ProperJavaRDP
 * 
 * Revision: $Revision$
 * Author: $Author$
 * Date: $Date$
 *
 * Copyright (c) 2005 Propero Limited
 *
 * Purpose: Provide a static interface to individual network layers
 *          and UI components
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 * 
 * (See gpl.txt for details of the GNU General Public License.)
 *          
 */
package net.propero.rdp;

import net.propero.rdp.rdp5.Rdp5;

public class Common {

	public boolean underApplet = false;
	public Rdp5 rdp;
	public Secure secure;
	public MCS mcs;
	public RdesktopFrame frame;
	public Rdesktop rdesktop;

	public Options options;
	public CommunicationMonitor cMonitor;

	public Common() {
		this.options = new Options();
		this.cMonitor = new CommunicationMonitor();
	}

	/**
	 * Quit the application
	 */
	public void exit() {
		rdesktop.exit(0, rdp, frame, true);
	}
	
	public void exit( int n, Rdp rdp, RdesktopFrame window, boolean sysexit ) {
		rdesktop.exit( n, rdp, window, sysexit );
	}

}
