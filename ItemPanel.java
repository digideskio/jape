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

public class ItemPanel extends InsetPanel implements DataChangeListener
{
    // Gui Elements
    private Frame parent;
    private GridBagLayout layout = new GridBagLayout();
    private GridBagConstraints constraint = new GridBagConstraints();
    private ItemDetailPanel itemDetailPanel;

    // State machine
    private boolean modified = false;

    // Data sources
    private Actor actor;
    private Mercenary merc;

    // Data Views
    private Vector views = new Vector();
    private ItemView currentView;

    // Instance methods
    public ItemPanel(Frame parent)
    {
	super(new Insets(10,10,10,10));
	this.parent = parent;
	this.setLayout(this.layout);

	// Constraints
	this.constraint.anchor = GridBagConstraints.SOUTHWEST;
	this.constraint.fill = GridBagConstraints.BOTH;
	this.constraint.gridx = 0;
	this.constraint.gridheight = 1;
	this.constraint.gridwidth = 1;
	this.constraint.weightx = 1;
	this.constraint.insets = new Insets(0,0,0,0);

	// Add buttons for item slots
	this.newRow();
	this.addItem(Mercenary.HEADGEAR_1_INDEX);
	this.addSpace();
	this.addItem(Mercenary.HELMET_INDEX);

	this.newRow();
	this.addItem(Mercenary.HEADGEAR_2_INDEX);
	this.addSpace();
	this.addItem(Mercenary.BODY_ARMOR_INDEX);
	
	this.newRow();
	this.addItem(Mercenary.RIGHT_HAND_INDEX);
	this.addSpace();
	this.addItem(Mercenary.LEG_ARMOR_INDEX);

	this.newRow();
	this.addItem(Mercenary.LEFT_HAND_INDEX);

	this.newRow();
	this.addItem(Mercenary.BACKPACK_1_1_INDEX);
	this.addItem(Mercenary.BACKPACK_1_2_INDEX);
	this.addItem(Mercenary.BACKPACK_1_3_INDEX);

	this.newRow();
	this.addItem(Mercenary.BACKPACK_2_1_INDEX);
	this.addItem(Mercenary.BACKPACK_2_2_INDEX);
	this.addItem(Mercenary.BACKPACK_2_3_INDEX);

	this.newRow();
	this.addItem(Mercenary.BACKPACK_3_1_INDEX);
	this.addItem(Mercenary.BACKPACK_3_2_INDEX);
	this.addItem(Mercenary.BACKPACK_3_3_INDEX);

	this.newRow();
	this.addItem(Mercenary.BACKPACK_4_1_INDEX);
	this.addItem(Mercenary.BACKPACK_4_2_INDEX);
	this.addItem(Mercenary.BACKPACK_4_3_INDEX);

	// Create item detail panel
	this.newRow();
	this.itemDetailPanel = new ItemDetailPanel(this.parent);
	this.constraint.gridwidth = 3;
	this.constraint.weightx = 1;
	this.constraint.weighty = 1;
	this.add(this.itemDetailPanel, this.constraint);
	this.itemDetailPanel.addDataChangeListener(this);
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

    private void addSpace() 
    {
	this.constraint.gridx++;
    }

    private void addItem(final int index) 
    {
	// Create new button
	final ItemView view = new ItemView(index);
	this.addComponent(1, view);
	this.views.addElement(view);
	view.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    doItemSelected(view);
		}});
    }

    public void setActor(Actor actor, Mercenary merc) 
    {
	// Set fields
	this.actor = actor;
	this.merc = merc;

	// Update views from data sources
	for( int idx = 0; idx < this.views.size(); ++idx ) {
	    ItemView view = (ItemView) this.views.elementAt(idx);
	    view.setActor(merc);
	}

	// Actor now clean
	this.modified = false;

	// Set detail fields
	this.doItemSelected((ItemView) this.views.elementAt(0));
    }

    public boolean isModified() { return this.itemDetailPanel.isModified(); }
    public void setModified(boolean modified) { 
	this.itemDetailPanel.setModified(modified);
	this.modified = modified; 
    }

    public void doItemSelected(ItemView view) 
    {
	// Get item index
	int index = view.getIndex();
	this.currentView = view;

	// Get new item
	Item item = null;
	if( merc != null ) {
	    item = this.merc.items[index];
	}

	// Change to new item
	this.itemDetailPanel.setItem(item);
    }

    public void dataChanged(DataChangeEvent event) 
    {
	// Extract the control was changed
	FieldView view = (FieldView) event.getSource();
	String oldValue = event.getOldValue();
	String newValue = event.getNewValue();

	// Update views from data sources
	this.currentView.refresh();

	// Actor now dirty
	//	System.err.println("ItemPanel.doValueChanged(" + view + ",'" + oldValue + "','" + newValue + "')");
	this.modified = true;
	this.fireDataChangeEvent(event);
    }

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
