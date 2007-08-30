/*
 * org.openmicroscopy.shoola.env.data.views.DataHandlerViewImpl 
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006 University of Dundee. All rights reserved.
 *
 *
 * 	This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */
package org.openmicroscopy.shoola.env.data.views;


//Java imports
import java.util.List;
import java.util.Map;
import java.util.Set;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.env.data.views.calls.AnnotationLoader;
import org.openmicroscopy.shoola.env.data.views.calls.AnnotationSaver;
import org.openmicroscopy.shoola.env.data.views.calls.ArchivedFilesLoader;
import org.openmicroscopy.shoola.env.data.views.calls.ClassificationLoader;
import org.openmicroscopy.shoola.env.data.views.calls.ClassificationSaver;
import org.openmicroscopy.shoola.env.event.AgentEventListener;
import pojos.AnnotationData;
import pojos.CategoryData;
import pojos.DataObject;

/** 
 * Implementation of the {@link DataHandlerView} implementation.
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since OME3.0
 */
public class DataHandlerViewImpl 
	implements DataHandlerView
{

	/**
     * Implemented as specified by the view interface.
     * @see DataHandlerView#createAnnotation(DataObject, AnnotationData,
     *                                      AgentEventListener)
     */
    public CallHandle createAnnotation(DataObject annotatedObject, 
                                       AnnotationData data,
                                        AgentEventListener observer)
    {
        BatchCallTree cmd = new AnnotationSaver(annotatedObject, data, 
                                    AnnotationSaver.CREATE);
        return cmd.exec(observer);
    }
    
    /**
     * Implemented as specified by the view interface.
     * @see DataHandlerView#updateAnnotation(DataObject, AnnotationData,
     *                                      AgentEventListener)
     */
    public CallHandle updateAnnotation(DataObject annotatedObject,
                                        AnnotationData data,
                                        AgentEventListener observer)
    {
        BatchCallTree cmd = new AnnotationSaver(annotatedObject, data, 
                                                AnnotationSaver.UPDATE);
        return cmd.exec(observer);
    }

    /**
     * Implemented as specified by the view interface.
     * @see DataHandlerView#deleteAnnotation(DataObject, AnnotationData,
     *                                       AgentEventListener)
     */
    public CallHandle deleteAnnotation(DataObject annotatedObject,
                                        AnnotationData data,
                                        AgentEventListener observer)
    {
        BatchCallTree cmd = new AnnotationSaver(annotatedObject, data, 
                                                AnnotationSaver.DELETE);
        return cmd.exec(observer);
    }
    
    /**
     * Implemented as specified by the view interface.
     * @see DataHandlerView#deleteAnnotation(DataObject, List,
     *                                       AgentEventListener)
     */
    public CallHandle deleteAnnotation(DataObject annotatedObject,
                                        List data,
                                        AgentEventListener observer)
    {
        BatchCallTree cmd = new AnnotationSaver(annotatedObject, data, 
                                                AnnotationSaver.DELETE);
        return cmd.exec(observer);
    }
    
    /**
     * Implemented as specified by the view interface.
     * @see DataHandlerView#loadAnnotations(Class, int, 
     *                                          AgentEventListener)
     */
    public CallHandle loadAnnotations(Class nodeType, long nodeID,
                                       AgentEventListener observer)
    {
        BatchCallTree cmd = new AnnotationLoader(nodeType, nodeID, false);
        return cmd.exec(observer);
    }
    
    /**
     * Implemented as specified by the view interface.
     * @see DataHandlerView#createAnnotation(Set, AnnotationData, 
     * 										AgentEventListener)
     */
	public CallHandle createAnnotation(Set annotatedObjects, 
							AnnotationData d, AgentEventListener observer) 
	{
		BatchCallTree cmd = new AnnotationSaver(annotatedObjects, d, false);
		return cmd.exec(observer);
	}

    /**
     * Implemented as specified by the view interface.
     * @see DataHandlerView#loadAnnotations(Class, Set, AgentEventListener)
     */
	public CallHandle loadAnnotations(Class nodeType, Set<Long> nodeIDs, 
							AgentEventListener observer) 
	{
		BatchCallTree cmd = new AnnotationLoader(nodeType, nodeIDs, true);
        return cmd.exec(observer);
	}

    /**
     * Implemented as specified by the view interface.
     * @see DataHandlerView#updateAndCreateAnnotation(Map, Set, 
     * 									AnnotationData, AgentEventListener)
     */
	public CallHandle updateAndCreateAnnotation(Map toUpdate, Set toCreate, 
					AnnotationData data, AgentEventListener observer) 
	{
		BatchCallTree cmd = new AnnotationSaver(toUpdate, toCreate, data);
		return cmd.exec(observer);
	}

    /**
     * Implemented as specified by the view interface.
     * @see DataHandlerView#updateAnnotation(Map, AgentEventListener)
     */
	public CallHandle updateAnnotation(Map annotatedObjects, 
										AgentEventListener observer)
	{
		BatchCallTree cmd = new AnnotationSaver(annotatedObjects);
		return cmd.exec(observer);
	}

    /**
     * Implemented as specified by the view interface.
     * @see DataHandlerView#loadClassificationPaths(Set, long, int,
     * 												AgentEventListener)
     */
	public CallHandle loadClassificationPaths(Set imageIDs, 
			long userID, int algorithm, AgentEventListener observer)
	{
		 BatchCallTree cmd = new ClassificationLoader(imageIDs, algorithm, 
				 								userID);
		 return cmd.exec(observer);
	}

    /**
     * Implemented as specified by the view interface.
     * @see DataHandlerView#classify(Set, Set, AgentEventListener)
     */
    public CallHandle classify(Set images, Set categories, 
                                AgentEventListener observer)
    {
        BatchCallTree cmd = new ClassificationSaver(images, categories, true);
        return cmd.exec(observer);
    }
    
    /**
     * Implemented as specified by the view interface.
     * @see DataHandlerView#declassify(Set, Set, AgentEventListener)
     */
    public CallHandle declassify(Set images, Set categories, 
                                AgentEventListener observer)
    {
        BatchCallTree cmd = new ClassificationSaver(images, categories, false);
        return cmd.exec(observer);
    }

    /**
     * Implemented as specified by the view interface.
     * @see DataHandlerView#loadArchivedFiles(String, long, AgentEventListener)
     */
	public CallHandle loadArchivedFiles(String location, long pixelsID, 
										AgentEventListener observer)
	{
		BatchCallTree cmd = new ArchivedFilesLoader(location, pixelsID);
        return cmd.exec(observer);
	}

    /**
     * Implemented as specified by the view interface.
     * @see DataHandlerView#classifyChildren(Set, Set, AgentEventListener)
     */
	public CallHandle classifyChildren(Set containers, Set categories, 
									AgentEventListener observer) 
	{
		BatchCallTree cmd = new ClassificationSaver(containers, categories);
        return cmd.exec(observer);
	}

    /**
     * Implemented as specified by the view interface.
     * @see DataHandlerView#annotateChildren(Set, AnnotationData, 
     * 										AgentEventListener)
     */
	public CallHandle annotateChildren(Set set, AnnotationData annotation, 
									AgentEventListener observer)
	{
		BatchCallTree cmd = new AnnotationSaver(set, annotation, true);
		return cmd.exec(observer);
	}

	/**
     * Implemented as specified by the view interface.
     * @see DataHandlerView#loadAllClassifications(long, long, 
     * 											AgentEventListener)
     */
	public CallHandle loadAllClassifications(long imageID, long userID, 
						AgentEventListener observer)
	{
		BatchCallTree cmd = new ClassificationLoader(imageID, userID);
		return cmd.exec(observer);
	}
    
	/**
     * Implemented as specified by the view interface.
     * @see DataHandlerView#createAndClassify(long, Set, 
     * 										AgentEventListener)
     */
	public CallHandle createAndClassify(long imageID, 
										Set<CategoryData> categories, 
										AgentEventListener observer)
	{
		BatchCallTree cmd = new ClassificationSaver(imageID, categories, null);
		return cmd.exec(observer);
	}

	/**
     * Implemented as specified by the view interface.
     * @see DataHandlerView#createAndClassify(long, Set, Set,
     * 										AgentEventListener)
     */
	public CallHandle createAndClassify(long imageID, 
			Set<CategoryData> categories, Set<CategoryData> categoriesToUpdate, 
			AgentEventListener observer)
	{
		BatchCallTree cmd = new ClassificationSaver(imageID, categories, 
												categoriesToUpdate);
		return cmd.exec(observer);
	}

}
