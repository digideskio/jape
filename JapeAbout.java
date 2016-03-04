/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

import java.awt.*;
import java.awt.event.*;
//import java.io.File;
//import java.io.IOException;

public class JapeAbout extends Dialog {
    public JapeAbout(Frame parent)
    {
	super(parent, "About JAPE", true);

	// Create body panel
	Panel body = new Panel();
	body.setLayout(new BorderLayout());
	this.add(body);

	// Add icon

	// Text Panel
	Panel textPanel = new InsetPanel(5, 5, 5, 5);
	textPanel.setLayout(new GridLayout(4,1));
	textPanel.setBackground(SystemColor.text);

	Label line1 = new Label("JAPE: A Jagged Alliance 2 Save Game Editor", Label.CENTER);
	textPanel.add(line1);
	Label line2 = new Label("Version 0.41", Label.CENTER);
	textPanel.add(line2);
	Label line3 = new Label("Copyright (c) 2008  Douglas Greiman", Label.CENTER);
	textPanel.add(line3);
	Label line4 = new Label("http://www.duggelz.org/", Label.CENTER);
	textPanel.add(line4);

	// Button bar
	Panel buttonPanel = new InsetPanel(0, 5, 0, 5);
	buttonPanel.setLayout(new FlowLayout());
	Button okButton = new Button("OK");
	okButton.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    dispose();
		}});
	buttonPanel.add(okButton);

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
}
