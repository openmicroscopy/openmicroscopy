/*
 * org.openmicroscopy.shoola.agents.treemng.browser.Browser
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

package org.openmicroscopy.shoola.agents.treeviewer.browser;




//Java imports
import java.awt.Component;
import java.awt.Point;
import java.util.List;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JComponent;


//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.treeviewer.view.TreeViewer;
import org.openmicroscopy.shoola.util.ui.component.ObservableComponent;

import pojos.DataObject;

/** 
 * Defines the interface provided by the browser component.
 * The browser provides a <code>JComponent</code> to host and display one
 * visualization tree. That is, one {@link TreeImageDisplay} top node.
 * Use the {@link BrowserFactory} to create an object implementing this 
 * interface.
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @version 2.2
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */
public interface Browser
    extends ObservableComponent
{

    /** Flag to denote the <i>New</i> state. */
    public static final int     	NEW = 1;
    
    /** Flag to denote the <i>Loading Data</i> state. */
    public static final int     	LOADING_DATA = 2;
    
    /** Flag to denote the <i>Loading Data</i> state. */
    public static final int     	LOADING_LEAVES = 3;
    
    /** Flag to denote the <i>Counting items</i> state. */
    public static final int     	COUNTING_ITEMS = 4;
    
    /** Flag to denote the <i>Ready</i> state. */
    public static final int     	READY = 5;
    
    /** Flag to denote the <i>Discarded</i> state. */
    public static final int     	DISCARDED = 6;
   
    /** 
     * Indicates that the browser corresponds to an <code>Hierarchy</code>
     * explorer.
     */
    public static final int     	HIERARCHY_EXPLORER = 100;
    
    /** 
     * Indicates that the browser corresponds to an <code>Category</code>
     * explorer.
     */
    public static final int     	CATEGORY_EXPLORER = 101;
    
    /** 
     * Indicates that the browser corresponds to an <code>Images</code>
     * explorer.
     */
    public static final int     	IMAGES_EXPLORER = 102;
    

    /** Indicates that the container is of type <code>Dataset</code>.*/
    public static final int     	DATASET_CONTAINER = 200;
    
    /** Indicates that the container is of type <code>Category</code>.*/
    public static final int     	CATEGORY_CONTAINER = 201;
    
    /** 
     * Bound property name indicating a data retrieval cancellation occured. 
     */
    //public static final String  	CANCEL_PROPERTY = "cancel";
    
    /** 
     * Bound property name indicating the data retrieval is finished. 
     */
    public static final String  	ON_END_LOADING_PROPERTY = "onEndLoading";
    
    /** 
     * Bound property name indicating a new node is selected. 
     */
    public static final String  	SELECTED_DISPLAY_PROPERTY = 
        								"selectedDisplay";
    
    /** 
     * Bound property name indicating to remove the browser from the display. 
     */
    public static final String  	CLOSE_PROPERTY = "close";
    
    /** Bound property name indicating to bring up the popup menu.  */
    public static final String  	POPUP_MENU_PROPERTY = "popupMenu";
    
    /** Bound property name indicating to set the filters nodes.  */
    public static final String  	FILTER_NODES_PROPERTY = "filterNodes";
    
    /** Bound property indicating to change the root of the hierarchy. */
    public static final String		HIERARCHY_ROOT_PROPERTY = "hierarchyRoot";
  
    /** 
     * The browser's title corresponding to {@link #HIERARCHY_EXPLORER} type.
     */
    public static final String     HIERARCHY_TITLE = "Hierarchy Explorer";
    
    /** 
     * The browser's title corresponding to {@link #CATEGORY_EXPLORER} type.
     */
    public static final String     CATEGORY_TITLE = "Category Explorer";
    
    /** 
     * The browser's title corresponding to {@link #IMAGES_EXPLORER} type.
     */
    public static final String     IMAGES_TITLE = "Image Explorer";
    
    /**
     * Queries the current state.
     * 
     * @return One of the state flags defined by this interface.
     */
    public int getState();
    
    /**
     * Starts the data loading process when the current state is {@link #NEW} 
     * and puts the window on screen.
     * If the state is not {@link #NEW}, then this method simply moves the
     * window to front.
     * 
     * @throws IllegalStateException If the current state is {@link #DISCARDED}.  
     */
    public void activate();
    
    /**
     * Transitions the viewer to the {@link #DISCARDED} state.
     * Any ongoing data loading is cancelled.
     */
    public void discard();
    
    /** 
     * Returns the UI component. 
     * 
     * @return See above.
     * @throws IllegalStateException If the current state is
     *                               {@link #DISCARDED}.
     */
    public JComponent getUI();
    
    /**
     * Callback used by a data loader to set the nodes of the retrieved 
     * hierarchy.
     * 
     * @param nodes The root nodes.
     * @throws IllegalStateException If the current state is not 
     *                               {@link #LOADING_DATA}.
     */
    public void setNodes(Set nodes);
    
    /**
     * Callback used by a data loader to set the leaves contained in the 
     * currently selected node.
     * 
     * @param leaves The collection of leaves.
     * @throws IllegalStateException If the current state is not 
     *                               {@link #LOADING_LEAVES}.
     */
    public void setLeaves(Set leaves);
    
    /** 
     * Returns the type of this browser.
     * 
     * @return See above.
     */
    public int getBrowserType();
    
    /** Cancels any ongoing data loading. */
    public void cancel();   
    
    /**
     * Returns the location of the mouse click event occured.
     * 
     * @return See above.
     */
    public Point getClickPoint();
    
    /**
     * Returns the currently selected {@link TreeImageDisplay node}.
     * 
     * @return See above.
     */
    public TreeImageDisplay getSelectedDisplay();
    
    /** 
     * Collapses the specified node. 
     * 
     * @param node The node to collapse.
     */
    public void collapse(TreeImageDisplay node);
    
    /** Removes the {@link Browser} from the display. */
    public void close();
    
    /** 
     * Returns the title of this browser.
     * 
     * @return The browser's title.
     */
    public String getTitle();
    
    /** 
     * Returns the icon of this browser. 
     * 
     * @return The browser's icon.
     */
    public Icon getIcon();
    
    /**
     * Has the specified object visit all the visualization trees hosted by
     * the browser.
     * 
     * @param visitor The visitor.  Mustn't be <code>null</code>.
     * @see TreeImageDisplayVisitor
     */
    public void accept(TreeImageDisplayVisitor visitor);
    
    /**
     * Has the specified object visit all the visualization trees hosted by
     * the browser.
     * 
     * @param visitor   The visitor.  Mustn't be <code>null</code>.
     * @param algoType  The algorithm selected to visit the visualization trees.
     *                  One of the constants defined by
     *                  {@link TreeImageDisplayVisitor}.
     * @see TreeImageDisplayVisitor
     */
    public void accept(TreeImageDisplayVisitor visitor, int algoType);
    
    /**
     * Sets the nodes resulting of a sorting action.
     * 
     * @param nodes The collection of nodes to set.
     */
    public void setSortedNodes(List nodes);
    
    /**
     * Sets the selected {@link TreeImageDisplay node}.
     * 
     * @param display The selected node.
     */
    void setSelectedDisplay(TreeImageDisplay display);
    
    /**
     * Loads the hierachy e.g. <code>Project/Dataset</code>,
     * <code>CategoryGroup/Category</code>.
     */
    public void loadData();
    
    /**
     * Loads the children of the nodes specified by the collection 
     * of nodeIDs.
     * 
     * @param nodeIDs The collection of IDs.
     */
    public void loadData(Set nodeIDs);

    /**
     * Retrieves the images contained in a <code>Dataset</code> or 
     * <code>Category</code>.
     */
    public void loadLeaves();
    
    /** Brings up on screen the popup menu. */
    public void showPopupMenu();   
    
    /**
     * Sets the nodes retrieved using a filtering data loading.
     * 
     * @param nodes The collection of nodes to set.
     * @param type	The type of filter used.
     */
    public void setFilterNodes(Set nodes, int type);
    
    /**
     * Loads the data for the specified type of filter.
     * 
     * @param type 	The type of container the data is for. One of the following
     * 				constants: {@link #DATASET_CONTAINER} or 
     * 				{@link #CATEGORY_CONTAINER}
     */
    public void loadFilterData(int type);
    
    /** 
     * Brings up the <code>Filter menu</code> at the specified location 
     * and for the specified component.
     * 
     * @param c The invoking component. 
     * @param p The location.
     */
    public void showFilterMenu(Component c, Point p);
    
    /** 
     * Reloads the children of the currently selected node and rebuilds
     * the display.
     */
    public void refresh();
    
    /** 
     * Reloads children of the currently selected node and rebuilds
     * the display if the <code>Browser</code> is currently selected, 
     * if not, all the chidren are removed and there is no data loading.
     */
    public void refreshTree();
    
    /** 
     * Adds the specified nodes to the currently selected
     * {@link TreeImageDisplay}.
     * 
     * @param nodes Collection to set.
     * @param parent 	The parent hosting the nodes if <code>null</code> the 
     * 					nodes will be added to the root node.
     */
    public void setContainerNodes(Set nodes, TreeImageDisplay parent);
    
    /**
     * Sets the root of the retrieved hierarchies. 
     * The rootID is taken into account if and only if the passed 
     * <code>rootLevel</code> is {@link TreeViewer#GROUP_ROOT}.
     * 
     * @param rootLevel The level of the root. One of the following constants:
     * 					{@link TreeViewer#WORLD_ROOT}, 
     * 					{@link TreeViewer#GROUP_ROOT} and
     * 					{@link TreeViewer#USER_ROOT}.
     * @param rootID	The Id of the root.
     */
    public void setHierarchyRoot(int rootLevel, int rootID);
    
    /**
     * Returns the level of the root. One of the following constants:
     * {@link TreeViewer#WORLD_ROOT}, {@link TreeViewer#GROUP_ROOT} and
     * {@link TreeViewer#USER_ROOT}. 
     * 
     * @return See above.
     */
    public int getRootLevel();
    
    /**
     * The id of the root level.
     * 
     * @return See above.
     */
    public int getRootID();
    
    /**
     * Sets the number of items contained in the specified container.
     * 
     * @param containerID 	The id of the container.
     * @param value			The number of items contained in the container.
     */
    public void setContainerCountValue(int containerID, int value);
    
    /**
     * Returns a collection of containers which contain <code>Image</code>s
     * e.g. a <code>Dataset</code>.
     * 
     * @return See above.
     */
    public Set getContainersWithImagesNodes();
    
    /**
     * Returns a collection of containers which contain <code>Image</code>s
     * e.g. a <code>Dataset</code>.
     * 
     * @return See above.
     */
    public Set getContainersWithImages();
    
    /**
     * Sets the nodes found corresponding to a pattern.
     * 
     * @param nodes The collection of found nodes.
     */
    public void setFoundInBrowser(Set nodes);

    /** Finds the next occurence of the phrase. */
    public void findNext();
    
    /** Finds the previous occurence of the phrase. */
    public void findPrevious();
    
    /**
     * Sets the value to <code>true</code> if currently selected.
     * <code>false</code> otherwise.
     * 
     * @param b <code>true</code> if the browser is selected.
     * 			<code>false</code> otherwise.
     */
    public void setSelected(boolean b);
    
    /**
     * Refreshes the nodes hosting the specified <code>DataObject</code>.
     * If the <code>op</code> parameter is {@link TreeViewer#CREATE_OBJECT}
     * The currently selected node is the parent of the <code>DataObject</code>.
     * 
     * @param object    The <code>DataObject</code> to handle.
     * @param op        One of the following constants: 
     *                  {@link TreeViewer#CREATE_OBJECT}, 
     *                  {@link TreeViewer#REMOVE_OBJECT} or
     *                  {@link TreeViewer#UPDATE_OBJECT}.
     */
    public void refreshEdit(DataObject object, int op);
    
}
