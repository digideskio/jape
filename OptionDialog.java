/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

import java.awt.*;
import java.awt.event.*;
import java.util.StringTokenizer;

public class OptionDialog extends Dialog 
{
    // Option types
    public static final int         DEFAULT_OPTION = -1;
    public static final int         OK_ONLY_OPTION = -1;
    public static final int         YES_NO_OPTION = 0;
    public static final int         YES_NO_CANCEL_OPTION = 1;
    public static final int         OK_CANCEL_OPTION = 2;

    /** Return value from class method if YES is chosen. */
    public static final int         YES_OPTION = 0;
    /** Return value from class method if NO is chosen. */
    public static final int         NO_OPTION = 1;
    /** Return value from class method if CANCEL is chosen. */
    public static final int         CANCEL_OPTION = 2;
    /** Return value form class method if OK is chosen. */
    public static final int         OK_OPTION = 0;
    /** Return value from class method if user closes window without selecting
     * anything, more than likely this should be treated as either a
     * CANCEL_OPTION or NO_OPTION. */
    public static final int         CLOSED_OPTION = -1;

    // Message types. Used by the UI to determine what icon to display,
    // and possibly what behavior to give based on the type.
    /** Used for error messages. */
    public static final int  ERROR_MESSAGE = 0;
    /** Used for information messages. */
    public static final int  INFORMATION_MESSAGE = 1;
    /** Used for warning messages. */
    public static final int  WARNING_MESSAGE = 2;
    /** Used for questions. */
    public static final int  QUESTION_MESSAGE = 3;
    /** No icon is used. */
    public static final int   PLAIN_MESSAGE = -1;

    /** Member variables **/
    private int optionType;
    private int messageType;
    private int value = CLOSED_OPTION;

    public OptionDialog(
	Frame parent,
	Object message,
	String title,
	int optionType,
	int messageType)
    {
	super(parent, title, true);

	// Set fields
	this.optionType = optionType;
	this.messageType = messageType;
	this.value = CLOSED_OPTION;

	// Create body panel
	Panel body = new Panel();
	body.setLayout(new BorderLayout());
	this.add(body);

	// Add icon

	// Text Panel
	Panel textPanel = new InsetPanel(5, 5, 5, 5);
	textPanel.setLayout(new GridLayout(0,1));
	textPanel.setBackground(SystemColor.text);

	// Create various lines of text
	String messageStr = message.toString();
	// It would be more intelligent to parse into words using java.text.BreakIterator
	StringTokenizer st = new StringTokenizer(messageStr, "\n");
	while( st.hasMoreTokens() )
	{
	    String line = st.nextToken();
	    Label label = new Label(line, Label.LEFT);
	    textPanel.add(label);
	}

	// Button bar
	Panel buttonPanel = new InsetPanel(0, 5, 0, 5);
	buttonPanel.setLayout(new FlowLayout());

	// Various buttons
	if( this.optionType == OK_ONLY_OPTION || 
	    this.optionType == OK_CANCEL_OPTION )
	{
	    Button button = new Button("OK");
	    button.addActionListener( new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			setValue(OK_OPTION);
		    }});
	    buttonPanel.add(button);
	}
	if( this.optionType == YES_NO_OPTION || 
	    this.optionType == YES_NO_CANCEL_OPTION )
	{
	    Button button = new Button("Yes");
	    button.addActionListener( new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			setValue(YES_OPTION);
		    }});
	    buttonPanel.add(button);
	}
	if( this.optionType == YES_NO_OPTION || 
	    this.optionType == YES_NO_CANCEL_OPTION )
	{
	    Button button = new Button("No");
	    button.addActionListener( new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			setValue(NO_OPTION);
		    }});
	    buttonPanel.add(button);
	}
	if( this.optionType == OK_CANCEL_OPTION || 
	    this.optionType == YES_NO_CANCEL_OPTION )
	{
	    Button button = new Button("Cancel");
	    button.addActionListener( new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			setValue(CANCEL_OPTION);
		    }});
	    buttonPanel.add(button);
	}

	// Add elements to dialog
	body.add(buttonPanel, "South");
	body.add(textPanel, "Center");

	// Handle window events
	this.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    dispose();
		}});

	// Instantiate peers for the size calculation below
	this.addNotify();
	this.pack();

	// Put this dialog in the center of the parent frame
	if( parent != null )
	{
	    Point parentLoc = parent.getLocation();
	    Dimension parentSize = parent.getSize();
	    Dimension size = this.getSize();
	    Point loc = new Point(
		parentLoc.x + (parentSize.width - size.width)/2,
		parentLoc.y + (parentSize.height - size.height)/2
		);
	    this.setLocation(loc);
	}
    }

    public int getValue()
    {
	return this.value;
    }

    public void setValue(int value)
    {
	this.value = value;
	dispose();
    }

    public static void showMessageDialog(
	Frame parent,
	Object message,
	String title,
	int messageType)
    {
	OptionDialog dialog = new OptionDialog(
	    parent, 
	    message, 
	    title, 
	    DEFAULT_OPTION, 
	    messageType);
	dialog.show();
    }

    public static int showConfirmDialog(
	Frame parent, 
	Object message,
	String title, 
	int optionType,
	int messageType) 
    {
	OptionDialog dialog = new OptionDialog(
	    parent, 
	    message, 
	    title, 
	    optionType, 
	    messageType);
	dialog.show();
	return dialog.getValue();
    }
}
