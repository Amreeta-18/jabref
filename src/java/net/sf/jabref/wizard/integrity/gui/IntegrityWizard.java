/*
 Copyright (C) 2004 R. Nagel

 All programs in this directory and
 subdirectories are published under the GNU General Public License as
 described below.

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or (at
 your option) any later version.

 This program is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 USA

 Further information about the GNU GPL is available at:
 http://www.gnu.org/copyleft/gpl.ja.html

 */

// created by : r.nagel 14.09.2004
//
// function :
//
// todo:
//
// modified:
//

package net.sf.jabref.wizard.integrity.gui ;

import javax.swing.JDialog ;
import net.sf.jabref.BibtexEntry ;
import net.sf.jabref.JabRefFrame ;
import javax.swing.JPanel;
import java.awt.*;
import javax.swing.*;
import net.sf.jabref.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.border.EtchedBorder;
import java.io.IOException;

public class IntegrityWizard
    extends JDialog
    implements ActionListener
{

  private JabRefFrame _frame ;
  private BibtexDatabase dbase ;
  private JButton closeButton ;
  private JButton startButton ;
  private IntegrityMessagePanel warnPanel ;

  public IntegrityWizard( JabRefFrame frame, BibtexDatabase database )
  {
    super( frame, "dialog", true ) ;

    _frame = frame ;
    dbase = database ;

    try
    {
      jbInit() ;
      pack() ;
    }
    catch ( Exception ex )
    {
      ex.printStackTrace() ;
    }
  }

  private void jbInit() throws Exception
  {
//    this.setModal( true ) ;
    this.setResizable( false ) ;

    // messages
    this.setTitle( "Experimental feature - Integrity Check") ;//Globals.lang( "Plain_text_import" ) + " " + typeStr ) ;
    warnPanel = new IntegrityMessagePanel() ;

    // ButtonPanel
    JPanel buttonPanel = new JPanel() ;
    GridBagLayout gbl = new GridBagLayout() ;
    GridBagConstraints con = new GridBagConstraints() ;
    con.weightx = 0 ;
    con.insets = new Insets( 5, 10, 0, 10 ) ;
    con.fill = GridBagConstraints.HORIZONTAL ;

    // Buttons
    startButton = new JButton(Globals.lang("Scan")) ;
    startButton.addActionListener( this) ;
    closeButton = new JButton(Globals.lang("Close"));
    closeButton.addActionListener( this) ;

    // insert Buttons
    con.gridwidth = GridBagConstraints.REMAINDER ;
    gbl.setConstraints( startButton, con ) ;
    buttonPanel.add( startButton ) ;

    gbl.setConstraints( closeButton, con ) ;
    buttonPanel.add( closeButton ) ;

    // ----------------------------------------------------------------------
    // add a short info, if available

    JEditorPane infoText = null ;

    URL infoURL = JabRef.class.getResource(GUIGlobals.helpPre
                                           +GUIGlobals.shortIntegrityCheck);
    if (infoURL != null)
      try
      {
        infoText = new JEditorPane() ;
        infoText.setEditable(false);
        infoText.setPage(infoURL);
        infoText.setBackground(GUIGlobals.infoField);
        infoText.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        infoText.setPreferredSize( new Dimension(220, 50));
        infoText.setMinimumSize( new Dimension(180, 50));
//        bottomPanel.add( infoText, BorderLayout.CENTER ) ;
      }
      catch (IOException e)
      {
        infoText = null ;
      }

    // -----------------------------------------------------------------------

    // content
    Container content = this.getContentPane() ;
    content.setLayout( new BorderLayout());

    if (infoText != null) // only if some help available
    {
      content.add( infoText, BorderLayout.PAGE_START ) ;
    }
    content.add(warnPanel, BorderLayout.CENTER) ;
    content.add(buttonPanel, BorderLayout.PAGE_END) ;
  }


// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------

  public void actionPerformed( ActionEvent e )
  {
    Object sender = e.getSource() ;

    if (sender == closeButton)
    {
      dispose() ;
    }
    else if (sender == startButton)
    {
      startButton.setEnabled(false);
      Runnable scanWork = new Runnable()
      {
        public void run()
        {
          warnPanel.updateView(dbase);
        }
      } ;
      SwingUtilities.invokeLater(scanWork);
      startButton.setEnabled(true);
    }
  }



}
