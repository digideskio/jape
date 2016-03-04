/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

public class StatPanel extends InsetPanel implements DataChangeListener
{
    // Gui Elements
    private Frame parent;
    private GridBagLayout layout = new GridBagLayout();
    private GridBagConstraints constraint = new GridBagConstraints();

    // State machine
    private boolean modified = false;

    // Data sources
    private Actor actor;
    private Mercenary merc;

    // Data Views
    private Vector views = new Vector();

    // Instance methods
    public StatPanel(Frame parent)
    {
	super(new Insets(10,10,10,10));
	this.parent = parent;
	this.setLayout(this.layout);

	// Constraints
	this.constraint.anchor = GridBagConstraints.SOUTHWEST;
	this.constraint.fill = GridBagConstraints.NONE;
	this.constraint.gridx = 0;
	this.constraint.gridheight = 1;
	this.constraint.gridwidth = 1;
	this.constraint.insets = new Insets(0,0,0,0);

	// Name and nickname
	this.addStringStat("Name", "Name", 30);
	this.addStringStat("Nickname", "Nickname", 30);

	// Health, current max and inc
	this.newRow();
	this.addText(1, "Health", Label.LEFT);
	this.addNumberView(1, "Health", 3);
	this.addText(1, "/", Label.CENTER);
	this.addNumberView(1, "Max Health", 3);
	this.addText(1, "(+", Label.RIGHT);
	this.addNumberView(1, "Health Inc", 3);
	this.addText(1, ")", Label.LEFT);

	// Ability scores
	this.addByteStat("Energy", "Energy");
	this.addByteStat("Morale", "Morale");
	this.addIncStat("Agility", "Agility", "Agility Inc");
	this.addIncStat("Dexterity", "Dexterity", "Dexterity Inc");
	this.addIncStat("Strength", "Strength", "Strength Inc");
	this.addIncStat("Leadership", "Leadership", "Leadership Inc");
	this.addIncStat("Wisdom", "Wisdom", "Wisdom Inc");
	this.addIncStat("Marksmanship", "Marksmanship", "Marksmanship Inc");
	this.addIncStat("Explosives", "Explosives", "Explosives Inc");
	this.addIncStat("Mechanical", "Mechanical", "Mechanical Inc");
	this.addIncStat("Medical", "Medical", "Medical Inc");
	this.addIncStat("Exp Lvl", "Level", "Level Inc");

	// Skills
	this.addChoiceStat("Skill1", "Skill1", Skill.list);
	this.addChoiceStat("Skill2", "Skill2", Skill.list);

	// Statistics
	this.addShortStat("Kills", "Kills");
	this.addShortStat("Assists", "Assists");
	this.addShortStat("Shots Fired", "Shots Fired");
	this.addShortStat("Shots Hit", "Shots Hit");
	this.addShortStat("Battles", "Battles");
	this.addShortStat("Times Wounded", "Wounds");

	// Alignment hack
	// Throw in extra label to make it look right
	this.newRow();
	this.constraint.fill = this.constraint.BOTH;
	this.constraint.weightx = 1;
	this.constraint.weighty = 1;
	this.addComponent(this.constraint.REMAINDER, new Label());
    }

    private void newRow() 
    {
	// Start a new row
	this.constraint.gridy++;
	this.constraint.gridx = 0;
    }

    private void addComponent(int colwidth, Component component)
    {
	// Add component to panel
	this.constraint.gridwidth = colwidth;
	this.add(component, this.constraint);
	this.constraint.gridx += colwidth;
    }

    private void addTextView(int colwidth, String statField, int charWidth)
    {
	TextView view = new TextView(statField, charWidth);
	this.addComponent(colwidth, view);
	this.views.addElement(view);
	view.addDataChangeListener(this);
    }

    private void addNumberView(int colwidth, String statField, int charWidth)
    {
	NumberView view = new NumberView(statField, charWidth);
	this.addComponent(colwidth, view);
	this.views.addElement(view);
	view.addDataChangeListener(this);
    }

    private void addText(int colwidth, String text, int align) 
    {
	// Create new label
	Label label = new Label(text, align);
	this.addComponent(colwidth, label);
    }

    public void addByteStat(String statTitle, String statField)
    {
	this.newRow();
	this.addText(1, statTitle, Label.LEFT);
	this.addNumberView(1, statField, 3);
    }

    public void addIncStat(String statTitle, String statField, String incStatField)
    {
	this.newRow();
	this.addText(1, statTitle, Label.LEFT);
	this.addNumberView(1, statField, 3);
	this.addText(1, "(+", Label.RIGHT);
	this.addNumberView(1, incStatField, 3);
	this.addText(1, ")", Label.LEFT);
    }

    public void addShortStat(String statTitle, String statField)
    {
	this.addByteStat(statTitle, statField);
    }

    public void addStringStat(String statTitle, String statField, int charWidth)
    {
	this.newRow();
	this.addText(1, statTitle, Label.LEFT);
	this.addNumberView(this.constraint.REMAINDER, statField, charWidth);
    }	

    public void addChoiceStat(String statTitle, String statField, Vector statChoices)
    {
	this.newRow();
	this.addText(1, statTitle, Label.LEFT);
	ChoiceView view = new ChoiceView(statField, statChoices);
	this.addComponent(this.constraint.REMAINDER, view);
	this.views.addElement(view);
	view.addDataChangeListener(this);
    }

    public void setActor(Actor actor, Mercenary merc) 
    {
	// Update fields
	this.actor = actor;
	this.merc = merc;

	Structure struct;
	if( this.merc != null ) {
	    struct = this.merc;
	    struct.chain(this.actor);
	} else {
	    struct = this.actor;
	}

	// Update views from data sources
	for( Enumeration e = this.views.elements(); e.hasMoreElements(); ) {
	    FieldView view = (FieldView) e.nextElement();
	    view.setStruct(struct);
	    view.refresh();
	}

	// Actor now clean
	this.modified = false;
    }

    public void dataChanged(DataChangeEvent event) {
	// Extract the control was changed
	FieldView view = (FieldView) event.getSource();
	String oldValue = event.getOldValue();
	String newValue = event.getNewValue();
	
	//System.err.println("doValueChanged(" + view + ",'" + oldValue + "','" + newValue + "')");

	// Do nothing if no item
	if( this.actor == null ) {
	    return;
	}

	// Actor now dirty
	//	System.err.println("StatPanel.doValueChanged(" + view + ",'" + oldValue + "','" + newValue + "')");
	this.modified = true;
	this.fireDataChangeEvent(event);
    }

    public boolean isModified() { return this.modified; }
    public void setModified(boolean modified) { this.modified = modified; }

    // Data Change event processing
    private DataChangeMixin mixin = new DataChangeMixin();

    public void addDataChangeListener(DataChangeListener l) {
	this.mixin.addDataChangeListener(l);
    }

    public void removeDataChangeListener(DataChangeListener l) {
	this.mixin.removeDataChangeListener(l);
    }

    public void fireDataChangeEvent(DataChangeEvent e) {
	this.mixin.fireDataChangeEvent(e);
    }
}

