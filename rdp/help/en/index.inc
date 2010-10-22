<b>Terminal Services Client Help</b>
<p>The Microsoft Terminal Services Client provides any Sun Java 1.4+ equipped 
browser with access to corporate terminal servers.</p>

<a name="howtoconnect">
<b>How to Connect</b>
</a>
<p>To connect to Microsoft Terminal Services, do one of the following:
<ul>
<li>Click a link to the computer on this page to connect to it (assuming your system administrator added the link).
<li>Choose the rdp:// option next to the Address field, enter the 
name of the host into the Address text box along with any optional parameters you want, 
then click Browse. For example:
<blockquote><b>rdp://myterm</b></blockquote>
See <a href="#format">Completing 
the Address Field</a> for the parameter options.</li>
</ul>
<p>When you connect to a terminal server, a browser tab or window indicates the following:</li><br>
<BLOCKQUOTE><tt>Terminal Server connection will open in a popup window. Please don't close 
this page or go back to the portal page until you are finished with the session. 
Click here if you want to open another window with the portal page.</tt></BLOCKQUOTE>

<p><b><font color="#3333FF">Leave this window open until your work is finished; otherwise the popup window will close.</b></font></p>

<p>A popup window opens to provide access to the server.</p>

<p><a name="format">
<b>Completing the Address Field</b>
</a></p>
<p>The format of the text to enter into the address field to establish a session with a Microsoft Terminal Services is:
<p><kbd><em>server</em><b>:</b><em>port</em><b>/?</b><em>parameters</em></kbd></p>

<p><kbd><em>server</kbd></em> is the only required attribute. Enter the name of the computer hosting the Microsoft Terminal 
Services. You can ask someone with access to the computer to go to the Start > (Settings >)  
Control Panel > Performance and Maintenance > System > Computer Name tab and read the value of "Full computer name" to you. 
The name is case-insensitive.

<p><kbd><em>port</kbd></em> is the virtual address within the remote computer hosting the Microsoft Terminal 
Services. Entering the port number is optional. The default value, 3389, matches the well-known port number for Microsoft
Terminal Services.

<p><kbd><em>parameters</kbd></em> is an optional query string consisting of parameter-value pairs. 
Type a question mark before entering the first parameter value pair. Separate each 
parameter-value pair that follows with an ampersand. 
The following syntax shows the position of these characters:</p>

<p><kbd>server</em><b>:</b><em>port</em><em><b>/?</b><em>Parameter1</em><b>=</b><em>value</em><b>&</b><em>Parameter2</em><b>=</b><em>value</em><b>&</b><em>Parameter3</em><b>=</b>value</em></kbd></p>


You may enter the parameters in any order, however, 
do not enter all of them. We recommend entering the geometry parameter.</p>

<ul>
<li>
<b>geometry</b> - size of the client screen, WxH. To start the applet in FullScreen mode use this parameter depending on your screen size , for eg: 'geometry=1024x768'.
</li>
<li>
<b>bpp</b> - bits per pixel (color depth), 8|16|24|32. The default value is the lesser value of the following: 24 or the color depth of the OS when it starts.
</li>
<li>
<b>domain</b> - logon domain
</li>
<li>
<b>username</b> - username for logon
</li>
<li>
<b>password</b> - password for logon. Use with care - this value is used at the client side, and can be observed.
</li>
<li>
<b>console</b> - connect to console, yes|no
</li>
<li>
<b>ForceJava</b> - set to yes to use the Java client.  The default setting is no. By default, the plug-in attempts to use the Microsoft ActiveX client if it is available, or attempts to install ActiveX if it is unavailable and the browser is Internet Explorer. Otherwise, the plug-in uses the Java client.
</li>
<br>
</ul>

Parameters that only take effect for the ActiveX client are:

<ul>
<li>
<b>RedirectDrives</b> - set to true to map remote drives locally
</li>
<li>
<b>RedirectPrinters</b> - set to true to map remote printers locally
</li>
<li>
<b>FullScreen</b> - set to true to start in FullScreen mode
</li>

<br>
</ul>

<p>
<a name="aboutrdp">
<b>About the RDP Client</b>
</a></p>
<p>"properJavaRDP" is an open source Java RDP client for Windows Terminal Services and is 
redistributed without any changes, per the GNU General Public License. The origin of this client is <a href="http://properjavardp.sourceforge.net/">
http://properjavardp.sourceforge.net/</a>.</p>

