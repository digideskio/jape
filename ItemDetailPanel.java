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

public class ItemDetailPanel extends InsetPanel implements DataChangeListener
{
    // Gui Elements
    private Frame parent;
    private GridBagLayout layout = new GridBagLayout();
    private GridBagConstraints constraint = new GridBagConstraints();

    // State machine
    private boolean modified = false;

    // Data sources
    private Item item;

    // Data views
    private Vector views = new Vector();
    private Vector ammoComponents = new Vector();
    private Vector attachmentComponents = new Vector();
    private Vector moneyComponents = new Vector();

    private ChoiceView idView;
    private NumberView quantityView;
    private Label itemPctLabel;
    private NumberView[] itemPctViews = new NumberView[6];
    private Label ammoIdLabel;
    private ChoiceView ammoIdView;
    private Label ammoQuantityLabel;
    private NumberView ammoQuantityView;
    private Label ammoVarietyLabel;
    private NumberView ammoVarietyView;
    private Label ammoPctLabel;
    private NumberView ammoPctView;
    private ChoiceView[] attachmentIdViews = new ChoiceView[4];
    private NumberView[] attachmentPctViews = new NumberView[4];
    private NumberView weightView;
    private Label moneyValueLabel;
    private NumberView moneyValueView;

    // Instance methods
    public ItemDetailPanel(Frame parent)
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

	// Field views
	this.newRow();
	this.addText(1, "Item");
	this.idView = addItemView(6, "Item ID", ItemExemplar.nameList);

	this.newRow();
	this.addText(1, "Number of Items");
	this.quantityView = addByteView(1, "Quantity");

	this.newRow();
	this.itemPctLabel = this.addText(1, "Item %");
	for( int idx = 0; idx < 6; ++idx ) {
	    this.itemPctViews[idx] = addByteView(1, "Item " + (idx+1) + " %");
	}

	this.newRow();
	this.ammoIdLabel = this.addText(1, "Ammo");
	this.ammoComponents.addElement(this.ammoIdLabel);
	this.ammoIdView = addItemView(6, "Ammo ID", ItemExemplar.ammoNameList);
	this.ammoComponents.addElement(this.ammoIdView);

	this.newRow();
	this.ammoQuantityLabel = this.addText(1, "Number of Rounds");
	this.ammoComponents.addElement(this.ammoQuantityLabel);
	this.ammoQuantityView = addByteView(1, "Ammo Quantity");
	this.ammoComponents.addElement(this.ammoQuantityView);

	this.newRow();
	this.ammoVarietyLabel = this.addText(1, "Ammo Variety");
	this.ammoComponents.addElement(this.ammoVarietyLabel);
	this.ammoVarietyView = addByteView(1, "Ammo Variety");
	this.ammoComponents.addElement(this.ammoVarietyView);

	this.newRow();
	this.ammoPctLabel = this.addText(1, "Ammo %");
	this.ammoComponents.addElement(this.ammoPctLabel);
	this.ammoPctView = addByteView(1, "Ammo %");
	this.ammoComponents.addElement(this.ammoPctView);

	for( int idx = 0; idx < 4; ++idx ) {
	    String str = "Attachment " + (idx+1);

	    this.newRow();
	    Label label = this.addText(1, str);
	    this.attachmentComponents.addElement(label);

	    ChoiceView view = addItemView(6, str + " ID", ItemExemplar.attachmentNameList);
	    this.attachmentIdViews[idx] = view;
	    this.attachmentComponents.addElement(view);

	    NumberView pctView = this.addByteView(1, str + " %");
	    this.attachmentPctViews[idx] = pctView;
	    this.attachmentComponents.addElement(pctView);

	    Label pctLabel = this.addText(1, "%");
	    this.attachmentComponents.addElement(pctLabel);
	}

	this.newRow();
	this.addText(1, "Weight (x 0.1 kg)");
	this.weightView = this.addByteView(1, "Weight");

	this.newRow();
	this.moneyValueLabel = this.addText(1, "Money Value");
	this.moneyComponents.addElement(this.moneyValueLabel);
	this.moneyValueView = this.addShortView(2, "Money Value");
	this.moneyComponents.addElement(this.moneyValueView);

	//addByteStat("Unknown 1", "Unknown 1");
	//addByteStat("Unknown 4", "Unknown 4");

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

    private NumberView addNumberView(int colwidth, String statField, int charWidth)
    {
	NumberView view = new NumberView(statField, charWidth);
	this.constraint.fill = this.constraint.NONE;
	this.addComponent(colwidth, view);
	this.views.addElement(view);
	view.addDataChangeListener(this);
	return view;
    }

    private Label addText(int colwidth, String text) 
    {
	return this.addText(colwidth, text, Label.LEFT);
    }

    private Label addText(int colwidth, String text, int align) 
    {
	// Create new label
	Label label = new Label(text, align);
	this.constraint.fill = this.constraint.HORIZONTAL;
	this.addComponent(colwidth, label);
	return label;
    }

    public NumberView addByteView(int colwidth, String statField)
    {
	return this.addNumberView(1, statField, 3);
    }

    public NumberView addShortView(int colwidth, String statField)
    {
	return this.addNumberView(1, statField, 3);
    }

    public ChoiceView addItemView(int colwidth, String statField, Vector statChoices)
    {
	// Create new view
	ChoiceView view = new ChoiceView(statField, statChoices);
	this.constraint.fill = this.constraint.HORIZONTAL;
	this.addComponent(colwidth, view);
	this.views.addElement(view);
	view.addDataChangeListener(this);
	return view;
    }

    public void setItem(Item item) 
    {
	// Update fields
	this.item = item;
//  	if( this.item != null ) {
//  	    for( int foo = 0; foo < this.item.data.length; ++foo ) {
//  		System.err.println("" + foo + ":\t" + this.item.data[foo]);
//  	    }
//  	}

	// Update views from data sources
	for( Enumeration e = this.views.elements(); e.hasMoreElements(); ) {
	    FieldView view = (FieldView) e.nextElement();
	    view.setStruct(item);
	    view.refresh();
	}

	// Decide which views to show
	this.setEnabledAll();

	// Actor now clean
	this.modified = false;
    }

    public void setEnabled(Vector views, boolean enabled)
    {
	for( Enumeration e = views.elements(); e.hasMoreElements(); ) {
	    Component view = (Component) e.nextElement();
	    view.setEnabled(enabled);
	}
    }

    public void setEnabledAll() 
    {
	// Noop if not showing any item
	if( this.item == null ) {
	    return;
	}

	// Decide which fields to show based on item type
	ItemExemplar exemplar = this.item.getExemplar();
	switch ( exemplar.category ) {
	case ItemExemplar.WEAPON_CATEGORY:
	    this.setEnabled(this.ammoComponents, true); 
	    this.setEnabled(this.attachmentComponents, true);
	    this.setEnabled(this.moneyComponents, false);
	    break;
	    
	case ItemExemplar.BODY_ARMOR_CATEGORY:
	    this.setEnabled(this.ammoComponents, false); 
	    this.setEnabled(this.attachmentComponents, true);
	    this.setEnabled(this.moneyComponents, false);
	    break;

	case ItemExemplar.MONEY_CATEGORY:
	    this.setEnabled(this.ammoComponents, false); 
	    this.setEnabled(this.attachmentComponents, false);
	    this.setEnabled(this.moneyComponents, true);
	    break;
	    
	default:
	    this.setEnabled(this.ammoComponents, false); 
	    this.setEnabled(this.attachmentComponents, false);
	    this.setEnabled(this.moneyComponents, false);
	    break;
	}

	// Show the appropriate number of %'s
	int quantity = this.item.getInt("Quantity");
	for( int idx = 0; idx < 6; ++idx ) {
	    this.itemPctViews[idx].setEnabled( (idx < quantity) );
	}
	if( quantity == 0 ) {
	    this.itemPctLabel.setEnabled(false);
	} else {
	    this.itemPctLabel.setEnabled(true);
	}

	// Set label appropriately
	if( exemplar.category == ItemExemplar.AMMO_CATEGORY ) {
	    this.itemPctLabel.setText("Number of Rounds");
	} else {
	    this.itemPctLabel.setText("Item %");
	}
    }

    public void dataChanged(DataChangeEvent event) 
    {
	// Extract the control was changed
	FieldView view = (FieldView) event.getSource();
	String oldValue = event.getOldValue();
	String newValue = event.getNewValue();
	
	//System.err.println("doValueChanged(" + view + ",'" + oldValue + "','" + newValue + "')");

	// Do nothing if no item
	if( this.item == null ) {
	    return;
	}

	// Do control-specific stuff
	if( view == this.idView ) 
	{
	    //	    System.err.println(" idview");

	    // Make the input more palatable
	    if( (oldValue == null) || (oldValue == "") ) {
		oldValue = "None";
	    }
	    if( (newValue == null) || (newValue == "") ) {
		newValue = "None";
	    }

	    // If switching from none to something, change % to 100 and count to 1
	    if( oldValue.equals("None") && (! newValue.equals("None")) )
	    {
		this.quantityView.setText("1");
		this.itemPctViews[0].setEnabled(true);
		this.itemPctViews[0].setText("100");
	    }
	    // If switching from something to none, change % to 0 and count to 0
	    else if ( newValue.equals("None") )
	    {
		this.quantityView.setText("0");
		this.itemPctViews[0].setEnabled(true);
		this.itemPctViews[0].setText("0");
	    }

	    // If switching to weapon, change count to 1
	    ItemExemplar exemplar = this.item.getExemplar();
	    if( exemplar.category == ItemExemplar.WEAPON_CATEGORY ) {
		this.quantityView.setText("1");
	    }
	    // If switching to new ammo, set variety
  	    else if ( exemplar.category == ItemExemplar.AMMO_CATEGORY ) 
  	    {
  		int quantity = this.item.getInt("Quantity");
  		for( int idx = 0; idx < quantity; ++idx ) {
  		    this.itemPctViews[idx].setEnabled(true);
  		    this.itemPctViews[idx].setText("" + exemplar.ammoCapacity);
  		}
  	    }

	    // Decide which fields to show
	    this.setEnabledAll();
	} 
	else if ( view == this.ammoIdView ) 
	{
	    //	    System.err.println(" ammoIdView");

	    // Make the input more palatable
	    if( (oldValue == null) || (oldValue == "") ) {
		oldValue = "None";
	    }
	    if( (newValue == null) || (newValue == "") ) {
		newValue = "None";
	    }

	    // If switching to new ammo, set variety
	    if( (! newValue.equals("None")) && (! newValue.equals(oldValue)) )
	    {
		int ammoId = this.item.getInt("Ammo ID");
		ItemExemplar exemplar = (ItemExemplar) ItemExemplar.exemplarTable.get(new Integer(ammoId));
		System.err.println("" + exemplar.ammoVariety);
		this.ammoVarietyView.setText("" + exemplar.ammoVariety);
		this.ammoQuantityView.setText("" + exemplar.ammoCapacity);
		this.ammoPctView.setText("100");
	    }
	} 
	else if (view == this.quantityView ) 
	{
	    // Decide which fields to show
	    this.setEnabledAll();
	}
	else 
	{
	    for( int idx = 0; idx < this.attachmentIdViews.length; ++idx ) 
	    {
		if( view == this.attachmentIdViews[idx] ) 
		{
		    // Make the input more palatable
		    if( (oldValue == null) || (oldValue == "") ) {
			oldValue = "None";
		    }
		    if( (newValue == null) || (newValue == "") ) {
			newValue = "None";
		    }
		    
		    // If switching attachment from none to something
		    if( oldValue.equals("None") && (! newValue.equals("None")) )
		    {
			this.attachmentPctViews[idx].setText("100");
		    }

		    break;
		}
	    }
	}

	// Actor now dirty
	//	System.err.println("ItemDetailPanel.doValueChanged(" + view + ",'" + oldValue + "','" + newValue + "')");
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

