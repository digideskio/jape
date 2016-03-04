/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

import java.awt.*;

/** A panel with specified insets on all four sides. */
public class InsetPanel extends Panel
{
  private Insets _insets;

  public InsetPanel( Insets insets ) {
    super();
    this._insets = insets;
    //setLayout(new BorderLayout());
  }

  public InsetPanel( int top, int left, int bottom, int right ) {
    super();
    this._insets = new Insets(top, left, bottom, right);
    //setLayout(new BorderLayout());
  }

  public Insets getInsets() {
    return this._insets;
  }
}
