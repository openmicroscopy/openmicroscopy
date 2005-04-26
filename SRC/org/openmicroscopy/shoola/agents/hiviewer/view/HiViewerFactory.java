/*
 * org.openmicroscopy.shoola.agents.hiviewer.view.HiViewerFactory
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

package org.openmicroscopy.shoola.agents.hiviewer.view;


//Java imports
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//Third-party libraries

//Application-internal dependencies

/** 
 * Factory to create {@link HiViewer} components.
 * This class keeps track of all {@link HiViewer} instances that have been
 * created and are not yet {@link HiViewer#DISCARDED discarded}.  A new
 * component is only created if none of the <i>tracked</i> ones is already
 * displaying the given hierarchy.  Otherwise, the existing component is
 * recycled.
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
public class HiViewerFactory
    implements ChangeListener
{

    /** The sole instance. */
    private static final HiViewerFactory  singleton = new HiViewerFactory();
    
    
    /**
     * Returns a viewer to display the Project/Dataset/Image hierarchy
     * rooted by the specified Project.
     * 
     * @param projectID The id of the Project root node.
     * @return A {@link HiViewer} component for the specified Project.
     */
    public static HiViewer getProjectViewer(int projectID)
    {
        HiViewerModel model = new ProjectModel(projectID);
        return singleton.getViewer(model);
    }
    
    /**
     * Returns a viewer to display the Dataset/Image hierarchy
     * rooted by the specified Dataset.
     * 
     * @param datasetID The id of the Dataset root node.
     * @return A {@link HiViewer} component for the specified Dataset.
     */
    public static HiViewer getDatasetViewer(int datasetID)
    {
        HiViewerModel model = new DatasetModel(datasetID);
        return singleton.getViewer(model);
    }
    
    /**
     * Returns a viewer to display the Category Group/Category/Image hierarchy
     * rooted by the specified Category Group.
     * 
     * @param cgID The id of the Category Group root node.
     * @return A {@link HiViewer} component for the specified Category Group.
     */
    public static HiViewer getCategoryGroupViewer(int cgID)
    {
        HiViewerModel model = new CategoryGroupModel(cgID);
        return singleton.getViewer(model);
    }
    
    /**
     * Returns a viewer to display the Category/Image hierarchy
     * rooted by the specified Category.
     * 
     * @param categoryID The id of the Category root node.
     * @return A {@link HiViewer} component for the specified Category.
     */
    public static HiViewer getCategoryViewer(int categoryID)
    {
        HiViewerModel model = new CategoryModel(categoryID);
        return singleton.getViewer(model);
    }
    
    /**
     * Returns a viewer to display data trees in the Project/Dataset/Image 
     * hierarchy that contain the specified images.
     * 
     * @param images The <code>ImageSummary</code> objects for the images that
     *               are at the bottom of the tree.
     * @return A {@link HiViewer} component for the specified images.
     */
    public static HiViewer getPDIViewer(Set images)
    {
        HiViewerModel model = new PDIModel(images);
        return singleton.getViewer(model);
    }
    
    /**
     * Returns a viewer to display data trees in the 
     * Category Group/Category/Image hierarchy that contain
     * the specified images.
     * 
     * @param images The <code>ImageSummary</code> objects for the images that
     *               are at the bottom of the tree.
     * @return A {@link HiViewer} component for the specified images.
     */
    public static HiViewer getCGCIViewer(Set images)
    {
        HiViewerModel model = new CGCIModel(images);
        return singleton.getViewer(model);
    }
    
    
    /** All the tracked components. */
    private Set     viewers;
    
    
    /**
     * Creates a new instance.
     */
    private HiViewerFactory() 
    {
        viewers = new HashSet();
    }
    
    /**
     * Creates or recycle a viewer component for the specified 
     * <code>model</code>.
     * 
     * @param model The component's Model.
     * @return A {@link HiViewer} for the specified <code>model</code>.  
     */
    private HiViewer getViewer(HiViewerModel model)
    {
        Iterator v = viewers.iterator();
        HiViewerComponent comp;
        while (v.hasNext()) {
            comp = (HiViewerComponent) v.next();
            if (model.isSameDisplay(comp.getModel())) return comp;
        }
        comp = new HiViewerComponent(model);
        comp.initialize();
        comp.addChangeListener(this);
        viewers.add(comp);
        return comp;
    }
    
    /**
     * Removes a viewer from the {@link #viewers} set when it is
     * {@link HiViewer#DISCARDED discarded}. 
     */ 
    public void stateChanged(ChangeEvent ce)
    {
        HiViewerComponent comp = (HiViewerComponent) ce.getSource(); 
        if (comp.getState() == HiViewer.DISCARDED) viewers.remove(comp);
    }

}
