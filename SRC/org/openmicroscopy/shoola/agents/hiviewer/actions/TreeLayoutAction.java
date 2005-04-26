/*
 * org.openmicroscopy.shoola.agents.hiviewer.actions.TreeLayoutAction
 *
 *------------------------------------------------------------------------------
 *
 *  Copyright (C) 2004 Open Microscopy Environment
 *      Massachusetts Institute of Technology,
 *      National Institutes of Health,
 *      University of Dundee
 *
 *
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *------------------------------------------------------------------------------
 */

package org.openmicroscopy.shoola.agents.hiviewer.actions;


//Java imports
import java.awt.event.ActionEvent;
import javax.swing.Action;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.hiviewer.IconManager;
import org.openmicroscopy.shoola.agents.hiviewer.browser.ImageDisplay;
import org.openmicroscopy.shoola.agents.hiviewer.cmd.LayoutCmd;
import org.openmicroscopy.shoola.agents.hiviewer.layout.Layout;
import org.openmicroscopy.shoola.agents.hiviewer.layout.LayoutFactory;
import org.openmicroscopy.shoola.agents.hiviewer.view.HiViewer;
import org.openmicroscopy.shoola.util.ui.UIUtilities;

/** 
 * 
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author  <br>Andrea Falconi &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:a.falconi@dundee.ac.uk">
 * 					a.falconi@dundee.ac.uk</a>
 * @version 2.2
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */
public class TreeLayoutAction
    extends HiViewerAction
{

    private static final String NAME = "Tree";

    
    public TreeLayoutAction(HiViewer model)
    {
        super(model);
        putValue(Action.NAME, NAME);
        Layout layout = LayoutFactory.createLayout(LayoutFactory.TREE_LAYOUT);
        String description = layout.getDescription();
        putValue(Action.SHORT_DESCRIPTION, 
                UIUtilities.makeParagraph(null, description,
                        UIUtilities.TABLE_WIDTH));
        IconManager im = IconManager.getInstance();
        putValue(Action.SMALL_ICON, im.getIcon(IconManager.TREE_LAYOUT));
    }

    /** Handle the action. */
    public void actionPerformed(ActionEvent e)
    {
        LayoutCmd cmd = new LayoutCmd(model, LayoutFactory.TREE_LAYOUT);
        cmd.execute();
    }

    protected void onDisplayChange(ImageDisplay selectedDisplay)
    {
        if (selectedDisplay != null) setEnabled(true);
    }

}
