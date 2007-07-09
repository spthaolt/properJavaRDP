/* RdpMenu.java
 * Component: ProperJavaRDP
 * 
 * Revision: $Revision$
 * Author: $Author$
 * Date: $Date$
 *
 * Copyright (c) 2005 Propero Limited
 *
 * Purpose: Menu bar for main frame
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
package net.propero.rdp.menu;

import java.awt.Event;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;

import net.propero.rdp.Common;
import net.propero.rdp.Options;
import net.propero.rdp.RdesktopFrame;

public class RdpMenu extends MenuBar {

	private static final long serialVersionUID = 4400808258398800489L;

	RdesktopFrame parent;

	/**
	 * Initialise the properJavaRDP menu bar and attach to an RdesktopFrame
	 * 
	 * @param parent
	 *            Menu is attached to this frame
	 */
	public RdpMenu(RdesktopFrame parent) {
		this.parent = parent;

		Menu m = new Menu("File");
		m.add(new MenuItem("Exit"));
		this.add(m);

		m = new Menu("Input");
		m.add(new MenuItem("Insert Symbol"));
		m.addSeparator();
		m.add(new MenuItem("Turn Caps-Lock On"));
		m.add(new MenuItem("Turn Num-Lock On"));
		m.add(new MenuItem("Turn Scroll-Lock On"));
		this.add(m);

		m = new Menu("Display");
		MenuItem mi = null;

		if (!parent.common.options.fullscreen) {
			mi = new MenuItem("Fullscreen Mode");
			mi.disable();
		} else
			mi = new MenuItem("Windowed Mode");

		m.add(mi);
		this.add(m);
	}

	public boolean action(Event event, Object arg) {
		if (arg == "Turn Caps-Lock On")
			((MenuItem) event.target).setLabel("Turn Caps-Lock Off");
		if (arg == "Turn Caps-Lock Off")
			((MenuItem) event.target).setLabel("Turn Caps-Lock On");

		if (arg == "Turn Num-Lock On")
			((MenuItem) event.target).setLabel("Turn Num-Lock Off");
		if (arg == "Turn Num-Lock Off")
			((MenuItem) event.target).setLabel("Turn Num-Lock On");

		if (arg == "Turn Scroll-Lock On")
			((MenuItem) event.target).setLabel("Turn Scroll-Lock Off");
		if (arg == "Turn Scroll-Lock Off")
			((MenuItem) event.target).setLabel("Turn Scroll-Lock On");

		if (arg == "Exit")
			parent.common.exit();

		if (arg == "Fullscreen Mode") {
			parent.goFullScreen();
			((MenuItem) event.target).setLabel("Windowed Mode");
		}

		if (arg == "Windowed Mode") {
			parent.leaveFullScreen();
			((MenuItem) event.target).setLabel("Fullscreen Mode");
		}
		return false;
	}

}
