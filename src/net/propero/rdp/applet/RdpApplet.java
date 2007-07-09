/* RdpApplet.java
 * Component: ProperJavaRDP
 * 
 * Revision: $Revision$
 * Author: $Author$
 * Date: $Date$
 *
 * Copyright (c) 2005 Propero Limited
 *
 * Purpose: Provide an applet interface to ProperJavaRDP
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

package net.propero.rdp.applet;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.TextArea;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import net.propero.rdp.Common;
import net.propero.rdp.Rdesktop;
import net.propero.rdp.RdesktopException;

public class RdpApplet extends Applet {

	private static final long serialVersionUID = 583386592743649642L;

	TextArea aTextArea = null;

	PrintStream aPrintStream = null;

	public void paint(Graphics g) {
		g.setColor(new Color(0xFFFFFF));
		g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
		g.setColor(new Color(0x000000));
		int width = g.getFontMetrics().stringWidth(
				"Launching properJavaRDP session...");
		int x = (int) (g.getClipBounds().getWidth() / 2) - (width / 2);
		int y = (int) (g.getClipBounds().getHeight() / 2);
		if (!redirectOutput)
			g.drawString("Launching properJavaRDP session...", x, y);
		width = g.getFontMetrics().stringWidth(
				"Connect to:" + getParameter("server"));
		x = (int) (g.getClipBounds().getWidth() / 2) - (width / 2);
		y = (int) (g.getClipBounds().getHeight() / 2) + 20;
		if (!redirectOutput)
			g.drawString("Connecting to:" + getParameter("server"), x, y);
	}

	boolean redirectOutput = false;

	public void init() {
		redirectOutput = isSet("redirectOutput");
		if (redirectOutput) {
			aPrintStream = new PrintStream(new FilteredStream(
					new ByteArrayOutputStream()));
			System.setOut(aPrintStream);
			System.setErr(aPrintStream);
			aTextArea = new TextArea();
			setLayout(new BorderLayout());
			add("Center", aTextArea);
		}
	}

	RdpThread rThread = null;

	public void start() {

		String argLine = "";
		argLine += genParam("-m", "keymap");
		argLine += " " + genParam("-u", "username");
		argLine += " " + genParam("-p", "password");
		argLine += " " + genParam("-n", "hostname");
		argLine += " " + genParam("-t", "port");
		argLine += " " + genParam("-l", "debug_level");
		argLine += " " + genParam("-s", "shell");
		argLine += " " + genParam("-t", "title");
		argLine += " " + genParam("-c", "command");
		argLine += " " + genParam("-d", "domain");
		argLine += " " + genParam("-o", "bpp");
		argLine += " " + genParam("-g", "geometry");
		argLine += " " + genParam("-s", "shell");
		argLine += " " + genFlag("--console", "console");
		argLine += " " + genFlag("--use_rdp4", "rdp4");
		argLine += " " + genFlag("--debug_key", "debug_key");
		argLine += " " + genFlag("--debug_hex", "debug_hex");
		argLine += " " + genFlag("--no_remap_hash", "no_remap_hash");

		argLine += " " + genParam("", "server");

		String[] args;
		StringTokenizer tok = new StringTokenizer(argLine, " ");
		for (Object[] obj = { tok, args = new String[tok.countTokens()],
				new int[] { 0 } }; ((StringTokenizer) obj[0]).hasMoreTokens(); ((String[]) obj[1])[((int[]) obj[2])[0]++] = ((StringTokenizer) obj[0])
				.nextToken()) {
		}
		// String[] args = argLine.split(" ");

		rThread = new RdpThread(args, this.getParameter("redirect_on_exit"),
				this);
		rThread.start();
	}

	public void stop() {
		rThread = null;
		notify();
	}

	private boolean isSet(String parameter) {
		String s = this.getParameter(parameter);
		if (s != null) {
			if (s.equalsIgnoreCase("yes"))
				return true;
		}
		return false;
	}

	private String genFlag(String flag, String parameter) {
		String s = this.getParameter(parameter);
		if (s != null) {
			if (s.equalsIgnoreCase("yes"))
				return flag;
		}
		return "";
	}

	private String genParam(String name, String parameter) {
		String s = this.getParameter(parameter);
		if (s != null) {
			if (name != "")
				return name + " " + s;
			else
				return s;
		} else
			return "";
	}

	class FilteredStream extends FilterOutputStream {
		public FilteredStream(OutputStream aStream) {
			super(aStream);
		}

		public void write(byte b[]) throws IOException {
			String aString = new String(b);
			aTextArea.append(aString);
		}

		public void write(byte b[], int off, int len) throws IOException {
			String aString = new String(b, off, len);
			aTextArea.append(aString);
		}
	}

}

class RdpThread extends Thread {

	private String[] args;
	private String redirect = null;
	private Applet parentApplet = null;
	private Rdesktop rdesktop;

	public RdpThread(String[] args, String redirect, Applet a) {
		parentApplet = a;
		this.args = args;
		this.redirect = redirect;
		this.rdesktop = new Rdesktop(true);
	}

	public void run() {
		this.setPriority(Thread.MAX_PRIORITY);

		try {
			rdesktop.main_nonstatic(args);
			if (redirect != null) {
				URL u = new URL(redirect);
				parentApplet.getAppletContext().showDocument(u);
			}
		} catch (RdesktopException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		}
	}
}
