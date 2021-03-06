/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leftedge;
import java.awt.*;
import java.util.Enumeration;
import javax.swing.*;
/**
 *
 * @author yangy
 */
//add channel router
public class ChannelRouter extends JPanel{
  private NetList nl;
  //constructor
  public ChannelRouter(NetList n) { nl = n;}

  //try commit number
  private static final int BORDER_Y_OFFSET = 10;
  private static final int HEIGHT_ADJUST = 10;
  private static final int WIDTH_ADJUST = 1;
  

  private int colWidth;
  private int trackHeight;
  
  private void setScale() {
    Dimension d = size();
    colWidth = (d.width) / NetList.NUM_COLS;
    trackHeight = (d.height - HEIGHT_ADJUST - BORDER_Y_OFFSET) / (nl.getMaxTrack() + 1);
  }

  /** @return the Y coordinate for track number. */
  public int trackY(int track) {
    return BORDER_Y_OFFSET + trackHeight * track;
  }
  /** X coordinate of column */
  public int colX(int col) {
    return colWidth/2 + colWidth * col;
  }
  
  public int channelWidth() {
    return (colWidth * NetList.NUM_COLS - 1);
  }
  
  public int channelHeight() {
    return (trackY(nl.getMaxTrack() +1) - trackY(0) + 1);
  }
  
  private Font f;
  private FontMetrics fm;
  private boolean fontsSet = false;
  
  private void setFonts(Graphics g) {
    if (fontsSet) return;
    f = new Font("Helvetica", Font.PLAIN, 12);
    fm = g.getFontMetrics(f);
    g.setFont(f);
    fontsSet = true;
  }


   @Override
 public void paint(Graphics g) {
    super.paint(g);
    setFonts(g);
    setScale(); 
    if (nl.getCyclesPresent()) {
      g.drawString("This circuit contains unresolved vertical constraint cycles", 
        20, trackY(1) - 5);
    }
    for (Enumeration e = nl.getNets() ; e.hasMoreElements() ;) { 
      Net n = (Net)e.nextElement(); 
      if (n.getTrack() != Net.TRACK_UNASSIGNED) {
        g.setColor(Color.blue);
        g.fillRect(colX(n.getLeftEdge()), trackY(n.getTrack())-2,
          colX(n.getRightEdge()) - colX(n.getLeftEdge())+2, 5);
        // int xnoff = fm.stringWidth(n.getName()) / 2;
        for (Enumeration eterms = n.getTerminals(); eterms.hasMoreElements() ; ) {
          Terminal t = (Terminal)eterms.nextElement();
          g.setColor(Color.red);
          if (t.getTopOrBottom()) {
            g.fillRect(colX(t.getColumn())-2, trackY(0),
              5, trackY(n.getTrack()) - trackY(0) + 3);               
            //g.drawString(n.getName(), colX(t.getColumn()) - xnoff, trackY(0) - 2);
          } else {
            g.fillRect(colX(t.getColumn()) - 2, trackY(n.getTrack())-2,
              5, trackY(nl.getMaxTrack()+1) - trackY(n.getTrack())+4);
            //g.drawString(n.getName(), colX(t.getColumn()) - xnoff, trackY(nl.getMaxTrack()+1) + fm.getAscent());
          }
          g.setColor(Color.black);
          g.fillRect(colX(t.getColumn())-3, trackY(n.getTrack())-3, 7, 7);
        }
      }
    }
  } 
}
