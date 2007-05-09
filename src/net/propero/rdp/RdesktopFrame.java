/* RdesktopFrame.java
 * Component: ProperJavaRDP
 * 
 * Revision: $Revision$
 * Author: $Author$
 * Date: $Date$
 *
 * Copyright (c) 2005 Propero Limited
 *
 * Purpose: Window for RDP session
 */
package net.propero.rdp;

import java.awt.*;
import java.awt.event.*;

import org.apache.log4j.Logger;

import net.propero.rdp.Rdp;
import net.propero.rdp.keymapping.KeyCode;
import net.propero.rdp.keymapping.KeyCode_FileBased;
import net.propero.rdp.menu.RdpMenu;
import net.propero.rdp.rdp5.cliprdr.ClipChannel;

//import javax.swing.Box;

public abstract class RdesktopFrame extends Frame {  
    
	static Logger logger = Logger.getLogger(RdesktopFrame.class);

	public RdesktopCanvas canvas = null;

	public Rdp rdp = null;

	public RdpMenu menu = null;

    /**
     * Register the clipboard channel
     * @param c ClipChannel object for controlling clipboard mapping
     */
	public void setClip(ClipChannel c) {
		canvas.addFocusListener(c);
	}

    public boolean action(Event event, Object arg) {
		if (menu != null)
			return menu.action(event, arg);
		return false;
	}

	protected boolean inFullscreen = false;

    /**
     * Switch to fullscreen mode
     */
	public void goFullScreen() {
		inFullscreen = true;
	}

    /**
     * Exit fullscreen mode
     */
	public void leaveFullScreen() {
		inFullscreen = false;
	}

    /**
     * Switch in/out of fullscreen mode
     */
	public void toggleFullScreen() {
		if (inFullscreen)
			leaveFullScreen();
		else
			goFullScreen();
	}

	private boolean menuVisible = false;

    /**
     * Display the menu bar
     */
	public void showMenu(){
		if (menu == null)
			menu = new RdpMenu(this);

		if (!menuVisible && Options.enable_menu)
			this.setMenuBar(menu);
		canvas.repaint();
		menuVisible = true;
	}
	
    /**
     * Hide the menu bar
     */
	public void hideMenu(){
		if(menuVisible && Options.enable_menu) this.setMenuBar(null);
		//canvas.setSize(this.WIDTH, this.HEIGHT);
		canvas.repaint();
		menuVisible = false;
	}
	
	/**
     * Toggle the menu on/off (show if hidden, hide if visible)
     *
	 */
	public void toggleMenu() {
		if(!menuVisible) showMenu();
		else hideMenu();
	}

    /**
     * Create a new RdesktopFrame.
     * Size defined by Options.width and Options.height
     * Creates RdesktopCanvas occupying entire frame
     */
	public RdesktopFrame() {
		super();
		Common.frame = this;
		this.canvas = new RdesktopCanvas_Localised(Options.width, Options.height);
		add(this.canvas);
		setTitle(Options.windowTitle);

		if (Constants.OS == Constants.WINDOWS)
			setResizable(false);
		// Windows has to setResizable(false) before pack,
		// else draws on the frame

		if (Options.fullscreen) {
			goFullScreen();
			pack();
			setLocation(0, 0);
		} else {// centre
			pack();
			centreWindow();
		}

		if (Constants.OS != Constants.WINDOWS)
			setResizable(false);
		// Linux Java 1.3 needs pack() before setResizeable

		addWindowListener(new RdesktopWindowAdapter());
        canvas.addFocusListener(new RdesktopFocusListener());
        if (Constants.OS == Constants.WINDOWS) {
			// redraws screen on window move
			addComponentListener(new RdesktopComponentAdapter());
		}

		canvas.requestFocus();
	}


    /**
     * Retrieve the canvas contained within this frame
     * @return RdesktopCanvas object associated with this frame
     */
	public RdesktopCanvas getCanvas() {
		return this.canvas;
	}

    /**
     * Register the RDP communications layer with this frame
     * @param rdp Rdp object encapsulating the RDP comms layer
     */
	public void registerCommLayer(Rdp rdp) {
		this.rdp = rdp;
		canvas.registerCommLayer(rdp);
	}

    /**
     * Register keymap
     * @param keys Keymapping object for use in handling keyboard events
     */
	public void registerKeyboard(KeyCode_FileBased keys) {
		canvas.registerKeyboard(keys);
	}

    class RdesktopFocusListener implements FocusListener {

        public void focusGained(FocusEvent arg0) {
            if (Constants.OS == Constants.WINDOWS) {
                // canvas.repaint();
                canvas.repaint(0, 0, Options.width, Options.height);
            }
            // gained focus..need to check state of locking keys
            canvas.gainedFocus();
        }

        public void focusLost(FocusEvent arg0) {
            //  lost focus - need clear keys that are down
            canvas.lostFocus();            
        }
    }
    
	class RdesktopWindowAdapter extends WindowAdapter {

		public void windowClosing(WindowEvent e) {
			hide();
			Rdesktop.exit(0, rdp, (RdesktopFrame) e.getWindow(), true);
		}

		public void windowLostFocus(WindowEvent e) {
            logger.info("windowLostFocus");
			// lost focus - need clear keys that are down
			canvas.lostFocus();
		}

		public void windowDeiconified(WindowEvent e) {
			if (Constants.OS == Constants.WINDOWS) {
				// canvas.repaint();
				canvas.repaint(0, 0, Options.width, Options.height);
			}
			canvas.gainedFocus();
		}

		public void windowActivated(WindowEvent e) {
			if (Constants.OS == Constants.WINDOWS) {
				// canvas.repaint();
				canvas.repaint(0, 0, Options.width, Options.height);
			}
			// gained focus..need to check state of locking keys
			canvas.gainedFocus();
		}

		public void windowGainedFocus(WindowEvent e) {
			if (Constants.OS == Constants.WINDOWS) {
				// canvas.repaint();
				canvas.repaint(0, 0, Options.width, Options.height);
			}
			// gained focus..need to check state of locking keys
			canvas.gainedFocus();
		}
	}

	class RdesktopComponentAdapter extends ComponentAdapter {
		public void componentMoved(ComponentEvent e) {
			canvas.repaint(0, 0, Options.width, Options.height);
		}
	}

	class YesNoDialog extends Dialog implements ActionListener {

		Button yes, no;

		boolean retry = false;

		public YesNoDialog(Frame parent, String title, String[] message) {
			super(parent, title, true);
			// Box msg = Box.createVerticalBox();
			// for(int i=0; i<message.length; i++) msg.add(new
			// Label(message[i],Label.CENTER));
			// this.add("Center",msg);
			Panel msg = new Panel();
			msg.setLayout(new GridLayout(message.length, 1));
			for (int i = 0; i < message.length; i++)
				msg.add(new Label(message[i], Label.CENTER));
			this.add("Center", msg);

			Panel p = new Panel();
			p.setLayout(new FlowLayout());
			yes = new Button("Yes");
			yes.addActionListener(this);
			p.add(yes);
			no = new Button("No");
			no.addActionListener(this);
			p.add(no);
			this.add("South", p);
			this.pack();
			if (getSize().width < 240)
				setSize(new Dimension(240, getSize().height));

			centreWindow(this);
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == yes)
				retry = true;
			else
				retry = false;
			this.hide();
			this.dispose();
		}
	}

	class OKDialog extends Dialog implements ActionListener {
		public OKDialog(Frame parent, String title, String[] message) {

			super(parent, title, true);
			// Box msg = Box.createVerticalBox();
			// for(int i=0; i<message.length; i++) msg.add(new
			// Label(message[i],Label.CENTER));
			// this.add("Center",msg);

			Panel msg = new Panel();
			msg.setLayout(new GridLayout(message.length, 1));
			for (int i = 0; i < message.length; i++)
				msg.add(new Label(message[i], Label.CENTER));
			this.add("Center", msg);

			Panel p = new Panel();
			p.setLayout(new FlowLayout());
			Button ok = new Button("OK");
			ok.addActionListener(this);
			p.add(ok);
			this.add("South", p);
			this.pack();

			if (getSize().width < 240)
				setSize(new Dimension(240, getSize().height));

			centreWindow(this);
		}

		public void actionPerformed(ActionEvent e) {
			this.hide();
			this.dispose();
		}
	}

    /**
     * Display an error dialog with "Yes" and "No" buttons and the title "properJavaRDP error"
     * @param msg Array of message lines to display in dialog box
     * @return True if "Yes" was clicked to dismiss box
     */
	public boolean showYesNoErrorDialog(String[] msg) {

		YesNoDialog d = new YesNoDialog(this, "properJavaRDP error", msg);
		d.show();
		return d.retry;
	}

    /**
     * Display an error dialog with the title "properJavaRDP error"
     * @param msg Array of message lines to display in dialog box
     */
	public void showErrorDialog(String[] msg) {
		Dialog d = new OKDialog(this, "properJavaRDP error", msg);
		d.show();
	}

    /**
     * Notify the canvas that the connection is ready for sending messages
     */
	public void triggerReadyToSend() {
		this.show();
		canvas.triggerReadyToSend();
	}

    /**
     * Centre a window to the screen
     * @param f Window to be centred
     */
	public void centreWindow(Window f) {
		Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension window_size = f.getSize();
		int x = (screen_size.width - window_size.width) / 2;
		if (x < 0)
			x = 0; // window can be bigger than screen
		int y = (screen_size.height - window_size.height) / 2;
		if (y < 0)
			y = 0; // window can be bigger than screen
		f.setLocation(x, y);
	}

    /**
     * Centre this window
     */
	public void centreWindow() {
		centreWindow(this);
	}

}
