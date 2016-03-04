/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class JapeFrame extends Frame implements DataChangeListener
{
    private static final int DEFAULT_WIDTH = 450;
    private static final int DEFAULT_HEIGHT = 450;

    // Major GUI elements
    private GridBagLayout layout = new GridBagLayout();
    private List      actorList;
    private StatPanel statPanel;
    private ItemPanel itemPanel;

    // Menu
    private MenuBar   menuBar;
    private Menu      fileMenu;
    private MenuItem  openItem;
    private MenuItem  saveItem;
    private MenuItem  closeItem;
    private MenuItem  quitItem;
    private Menu      helpMenu;
    private MenuItem  aboutItem;

    // The save game engine
    private SaveGame saveGame;
    private boolean saveGameModified;
    private Actor currentActor;
    private Mercenary currentMerc;
    private String currentDir = System.getProperty("user.dir");

    public JapeFrame() 
    {
	super("JAPE");

	// Menu
	this.createMenuBar();

	// Create body panel
	Panel body = new Panel();
	body.setLayout(this.layout);
	this.add("Center", body);

	// Create actor list
	this.actorList = new List();
	this.actorList.addItemListener(new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
		    doSelectActor();
		}});
	GridBagConstraints c1 = new GridBagConstraints();
	c1.fill = c1.BOTH;
	c1.gridx = 0;
	c1.gridy = 0;
	c1.weighty = 1;
	c1.weightx = 0;
	body.add(this.actorList, c1);

	// Create stat panel
	this.statPanel = new StatPanel(this);
	this.statPanel.addDataChangeListener(this);
	GridBagConstraints c2 = new GridBagConstraints();
	c2.fill = c2.BOTH;
	c2.anchor = c2.NORTHWEST;
	c2.gridx = 1;
	c2.gridy = 0;
	c2.weighty = 1;
	c2.weightx = 1;
	body.add(this.statPanel, c2);

	// Create item panel
	this.itemPanel = new ItemPanel(this);
	this.itemPanel.addDataChangeListener(this);
	GridBagConstraints c3 = new GridBagConstraints();
	c3.fill = c3.BOTH;
	c3.anchor = c3.NORTHWEST;
	c3.gridx = 2;
	c3.gridy = 0;
	c3.weighty = 1;
	c3.weightx = 1;
	body.add(this.itemPanel, c3);

	// Handle window events
	this.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    doQuit();
		}});

	// Size the frame
	this.addNotify();
	//	Insets frameInsets = this.getInsets();
	//	int width = frameInsets.left + frameInsets.right + DEFAULT_WIDTH;
	//	int height = frameInsets.top + frameInsets.bottom + DEFAULT_HEIGHT;
	//	this.setSize(width, height);
	//	this.setLocation(50,50);

	// Init the panels
	this.populateActorList();

	// Show
	this.pack();
	this.repaint();
    }
    
    private void createMenuBar() {
	// Create menu bar
	this.menuBar = new MenuBar();

	// File menu
	this.fileMenu = new Menu("File");
	this.menuBar.add(this.fileMenu);

	this.openItem = new MenuItem("Open...");
	this.openItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    doOpen();
		}});
	this.fileMenu.add(this.openItem);

	this.saveItem = new MenuItem("Save");
	this.saveItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    doSave();
		}});
	this.fileMenu.add(this.saveItem);
	this.saveItem.setEnabled(false);

	this.closeItem = new MenuItem("Close");
	this.closeItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    doClose();
		}});
	this.fileMenu.add(this.closeItem);
	this.closeItem.setEnabled(false);

	this.fileMenu.addSeparator();

	this.quitItem = new MenuItem("Quit");
	this.quitItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    doQuit();
		}});
	this.fileMenu.add(this.quitItem);

	// About menu
	this.helpMenu = new Menu("Help");
	this.menuBar.add(this.helpMenu);
	this.menuBar.setHelpMenu(this.helpMenu);

	this.aboutItem = new MenuItem("About...");
	this.aboutItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    doAbout();
		}});
	this.helpMenu.add(this.aboutItem);
	
	// Attach menubar to frame
	this.setMenuBar(this.menuBar);
    }

    public boolean doAbout() {
	new JapeAbout(this).show();
	return true;
    }

    public boolean doClose() {
	// Do we currently have a save game open?
	if ( (this.saveGame != null) && (this.saveGameModified) )
	{
	    // Prompt user for save
	    String shortName = new File(this.saveGame.filename).getName();
	    int option = OptionDialog.showConfirmDialog(
		this, 
		"The file " + shortName + " has been modifed.\n" +
		"Do you want to save the changes?",
		this.getTitle(),
		OptionDialog.YES_NO_CANCEL_OPTION,
		OptionDialog.WARNING_MESSAGE);
	    if ( option == OptionDialog.CANCEL_OPTION || 
		 option == OptionDialog.CLOSED_OPTION )
	    {
		return false;
	    } else if ( option == OptionDialog.YES_OPTION )
	    {
		boolean success = doSave();
		if ( success == false ) {
		    return false;
		}
	    }
	}

	// Disable relevant menu items
	this.saveItem.setEnabled(false);
	this.closeItem.setEnabled(false);

	// Close current save
	this.saveGame = null;
	this.saveGameModified = false;
	this.populateActorList();
	return true;
    }

    public boolean doOpen() {
	// Close any existing save
	boolean success = this.doClose();
	if ( ! success ) {
	    return false;
	}

	// Create open file dialog
	FileDialog fileDialog = new FileDialog(this, "Open", FileDialog.LOAD);
	if( this.currentDir != null ) {
	    fileDialog.setDirectory(this.currentDir);
	}

	// Show dialog
	fileDialog.setFile("*.sav");
	fileDialog.show();

	// What did the user choose?
	String directory = fileDialog.getDirectory();
	String name = fileDialog.getFile();
	if( name == null ) {
	    return false;
	}
	this.currentDir = directory;
	String filename = new File(directory, name).toString();

	// Open the selected file
	SaveGame saveGame = new SaveGame();
	try {
	    saveGame.load(filename);
	} catch( EOFException e )
	{
	    // Display error message
	    OptionDialog.showMessageDialog(
		this, 
		"Unable to load file.\n" +
		"Unexpected end of file reached.",
		this.getTitle(),
		OptionDialog.ERROR_MESSAGE);
	    return false;
	} catch( FileNotFoundException e )
	{
	    // Display error message
	    OptionDialog.showMessageDialog(
		this, 
		"Unable to load file.\n" +
		"The system cannot find the file specified.",
		this.getTitle(),
		OptionDialog.ERROR_MESSAGE);
	    return false;
	} catch( IOException e )
	{
	    // Display error message
	    String errorMessage = e.getMessage();
	    if( e.getMessage() == null )
	    {
		errorMessage = "An unknown error occurred.";
	    }
	    OptionDialog.showMessageDialog(
		this, 
		"Unable to load file.\n" +
		errorMessage,
		this.getTitle(),
		OptionDialog.ERROR_MESSAGE);
	    return false;
	} 
	catch( SaveGame.FormatException e )
	{ 
	    // Display error message
	    //e.printStackTrace(System.err);
	    OptionDialog.showMessageDialog(
		this, 
		"Unable to load file.\n" +
		"The save game format was not recognized.",
		this.getTitle(),
		OptionDialog.ERROR_MESSAGE);
	    return false;
	}

	// Enable relevant menu items
	this.saveItem.setEnabled(true);
	this.closeItem.setEnabled(true);

	// Display the file
	this.saveGame = saveGame;

	// Select the first actor
	this.populateActorList();
	this.actorList.select(0);
	this.doSelectActor();
	this.saveGameModified = false;

	return true;
    }

    public boolean doSave() {
	// Do we have a save game open
	if ( this.saveGame == null )
	{
	    return true;
	}

	// Save the game
	try {
	    this.saveGame.save();
	} catch( IOException e )
	{
	    // Display error message
	    String errorMessage = e.getMessage();
	    if( e.getMessage() == null )
	    {
		errorMessage = "An unknown error occurred.";
	    }
	    OptionDialog.showMessageDialog(
		this, 
		"Unable to save file.\n" +
		errorMessage,
		this.getTitle(),
		OptionDialog.ERROR_MESSAGE);
	    return false;
	}

	// Reset modified flag
	this.saveGameModified = false;
	this.statPanel.setModified(false);
	this.itemPanel.setModified(false);

	return true;
    }

    public boolean doQuit() 
    {
	// Close any existing save
	boolean success = this.doClose();
	if ( ! success ) {
	    return false;
	}

	// Close window
	this.setVisible(false);
	this.dispose();
	System.exit(0);
	return true;
    }

    private boolean doSelectActor()
    {
	// Which name did the user select
	int idx = this.actorList.getSelectedIndex();

	// Lookup that actor in the save
	Actor actor = null;
	Mercenary merc = null;
	if ( idx != -1 )
	{
	    // Get actor and/or merc
	    actor = this.saveGame.getActor(idx);
	    String nickname = actor.get("Nickname");
	    merc = this.saveGame.getMercByNick(nickname);
	}	    

	// Do it
	this.doSetActor(actor, merc);
	return true;
    }

    private void doSetActor(Actor actor, Mercenary merc) 
    {
	this.currentActor = actor;
	this.currentMerc = merc;

	// Redisplay the gui
	this.statPanel.setActor(this.currentActor, this.currentMerc);
	this.itemPanel.setActor(this.currentActor, this.currentMerc);
    }

    private void populateActorList()
    {
	// Clear all entries
	this.actorList.removeAll();
	this.doSetActor(null, null);

	// Game loaded?
	if ( this.saveGame == null ) {
	    return;
	}

	// Iterator over actors in the save game
	for( int idx = 0; idx < this.saveGame.actorCount; ++idx )
	{
	    // Get actor
	    Actor actor = this.saveGame.getActor(idx);
	    String nickname = actor.get("Nickname");

	    // Is there an associated merc record?
	    Mercenary merc = this.saveGame.getMercByNick(nickname);
	    if ( merc != null )
	    {
		nickname = "*" + nickname;
	    }
	    
	    // Add actor nickname to list
	    this.actorList.add(nickname);
	}
    }

    public void dataChanged(DataChangeEvent event) {
	//	System.err.println("JapeFrame.dataChanged("+event+")");
	this.saveGameModified = true;
    }

    // Class methods
    public static void main(String[] args) 
    {
	// Create frame
	JapeFrame japeFrame = new JapeFrame();
	
	// Make frame visible
	japeFrame.show();

	// Open a save
	//japeFrame.doOpen();
    }
}
