/*
  Written 1999 by Douglas Greiman.
 
  This software may be used and distributed according to the terms
  of the GNU Public License, incorporated herein by reference.
*/

package duggelz.jape;

public interface FieldView
{
    public String getFieldName();
    public String getValue();
    public boolean isModified();
    public void setStruct(Structure struct);

    public void refresh();

    public void addDataChangeListener(DataChangeListener l);
    public void removeDataChangeListener(DataChangeListener l);
    public void fireDataChangeEvent(DataChangeEvent e);
}
