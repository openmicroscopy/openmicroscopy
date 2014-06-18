/*
 * org.openmicroscopy.shoola.util.ui.search.SearchPanel 
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2014 University of Dundee. All rights reserved.
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
package org.openmicroscopy.shoola.util.ui.search;


//Java imports
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//Third-party libraries
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTaskPane;

//Application-internal dependencies
import org.openmicroscopy.shoola.util.ui.IconManager;
import org.openmicroscopy.shoola.util.ui.SeparatorPane;
import org.openmicroscopy.shoola.util.ui.UIUtilities;

/** 
 * The Component hosting the various fields used to collect the 
 * context of the search.
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
public class SearchPanel
	extends JPanel
	implements ActionListener
{

	/** The title of the Advanced search UI component. */
	private static final String		ADVANCED_SEARCH_TITLE = 
		"Advanced Search For Images";
	
	/** The title of the search UI component. */
	private static final String		SEARCH_TITLE = "Search For Images";
	
	/** The title of the type UI component. */
	private static final String		TYPE_TITLE = "Type";
	
	/** Search Tip. */
	private static final String		SEARCH_TIP = "<html><i>Tip: " +
			"Use these options to look for an <br> exact phrase or to exclude" +
			" certain words.</i></html>";
	
	/** Users Tip. */
	//private static final String		USERS_TIP = "Tip: Add a minus in front " +
	//		"of a name to exclude the user.";
	
	/** The type of font used for the tips. */
	private static final int		TIP_FONT_TYPE = Font.ITALIC;
	
	/** The size of font used for the tips. */
	private static final int		TIP_FONT_SIZE = 10;
	
	/** The number of columns of the search areas. */
	private static final int		AREA_COLUMNS = 12;
	
	/** Possible time options. */
	private static String[]		dateOptions;
	
	/** The maximum number of results returned. */
	private static String[]		numberOfResults;
	
	/** The maximum number of results returned. */
	private static String[]		fileFormats;
	
	static {
		dateOptions = new String[SearchContext.MAX+1];
		dateOptions[SearchContext.ANY_DATE] = "Any date";
		dateOptions[SearchContext.LAST_TWO_WEEKS] = "Last two weeks";
		dateOptions[SearchContext.LAST_MONTH] = "Last 30 days";
		dateOptions[SearchContext.LAST_TWO_MONTHS] = "Last 60 days";
		dateOptions[SearchContext.ONE_YEAR] = "1 year";
		dateOptions[SearchContext.RANGE] = "Specify date range " +
								"("+UIUtilities.DATE_FORMAT.toUpperCase()+")";
		numberOfResults = new String[SearchContext.MAX_RESULTS+1];
		numberOfResults[SearchContext.LEVEL_ONE] = 
									SearchContext.LEVEL_ONE_VALUE+" results";
		numberOfResults[SearchContext.LEVEL_TWO] = 
			SearchContext.LEVEL_TWO_VALUE+" results";
		numberOfResults[SearchContext.LEVEL_THREE] = 
			SearchContext.LEVEL_THREE_VALUE+" results";
		numberOfResults[SearchContext.LEVEL_FOUR] = 
			SearchContext.LEVEL_FOUR_VALUE+" results";
		
		fileFormats = new String[SearchContext.MAX_FORMAT+1];
		fileFormats[SearchContext.ALL_FORMATS] = "All formats";
		fileFormats[SearchContext.HTML] = "HTML (.htm, .html)";
		fileFormats[SearchContext.PDF] = "Adobe PDF (.pdf)";
		fileFormats[SearchContext.EXCEL] = "Microsoft Excel (.xls)";
		fileFormats[SearchContext.POWER_POINT] = "Microsoft PowerPoint (.ppt)";
		fileFormats[SearchContext.WORD] = "Microsoft Word (.doc)";
		fileFormats[SearchContext.XML] = "RSS/XML (.xml)";
		fileFormats[SearchContext.TEXT] = "Text Format (.txt)";
	}

	/** If checked the case is taken into account. */ 
	private JCheckBox				caseSensitive;
	
	/** The button used to display the available owners. */
	private JButton					ownerButton;
	
	/** The button used to display the available annotators. */
	private JButton					annotatorButton;
	
	/** Fields with the possible users. */
	private JTextField 				usersAsOwner;
	
	/** Fields with the possible users. */
	private JTextField 				usersAsAnnotator;
	
	/** Possible dates. */
	private JComboBox				dates;
	
	/** The terms to search for. */
	private JTextField				fullTextArea;
	
	/** Date used to specify the beginning of the time interval. */
	private JXDatePicker			fromDate;
	
	/** Date used to specify the ending of the time interval. */
	private JXDatePicker			toDate;
	
	private JButton clearDate;
	
	/** Reference to the model .*/
	private SearchComponent 		model;
	
	/** Items used to defined the scope of the search. */
	private Map<Integer, JCheckBox>	scopes;
	
	/** Items used to defined the scope of the search. */
	private Map<Integer, JCheckBox>	types;
	
	/** Button to only retrieve the current user's data. */
	private JCheckBox				currentUserAsOwner;
	
	/** Button to only retrieve the current user and selected users' data. */
	private JCheckBox				othersAsOwner;

	/** Button to only retrieve the current user's data. */
	private JCheckBox				currentUserAsAnnotator;
	
	/** Button to only retrieve the current user and selected users' data. */
	private JCheckBox				othersAsAnnotator;
	
	/** The possible textual areas. */
	private Map<JTextField, JLabel>	areas;
	
	/** Button to bring up the tooltips for help. */
	private JButton					helpBasicButton;
	
//	/** Button indicating that the time entered is the creation time. */
//	private JRadioButton			creationTime;
	
//	/** Button indicating that the time entered is the time of update. */
//	private JRadioButton			updatedTime;
	
	/** The possible file formats. */
	private JComboBox				formats;
	
	/** Keep tracks of the users selected. */
	private Map<Long, String>		otherOwners;
			
	/** The panel hosting the selected users. */
	private JPanel					otherOwnersPanel;
	
	/** The component used to perform a basic search. */
	private JPanel					basicSearchComp;
	
	/** The component used to perform an advanced search. */
	private JPanel					advancedSearchComp;
	
	/** The component hosting either the advanced or basic search component. */
	private JPanel 					searchFor;
	
	/** The collection of buttons to add. */
	private List<JButton>			controls;
	
	/** The box displaying the groups.*/
	private JComboBox groupsBox;
	
	/** The box displaying the groups.*/
	private List<JComboBox> groupsBoxes;
	
	private JPanel groupRow;
	
	private JXTaskPane datePane;
	
	/**
	 * Returns the selected groups.
	 * 
	 * @return See above.
	 */
	private Collection<GroupContext> getGroups() 
	{
		if (groupsBox == null) return model.getGroups();
		GroupContext ctx = (GroupContext) groupsBox.getSelectedItem();
		if (ctx.getId() < 0 || ctx.getId() == GroupContext.ALL_GROUPS_ID) return model.getGroups();
		List<GroupContext> groups = new ArrayList<GroupContext>();
		groups.add(ctx);
		Iterator<JComboBox> i = groupsBoxes.iterator();
		JComboBox box;
		while (i.hasNext()) {
			box = i.next();
			ctx = (GroupContext) box.getSelectedItem();
			if (ctx.getId() < 0) return model.getGroups();
			if (!groups.contains(ctx)) groups.add(ctx);
		}
		return groups;
	}
	
	/**
	 * Creates a <code>JComboBox</code> with the available groups.
	 * 
	 * @return See above.
	 */
	private JComboBox createBox()
	{
		List<GroupContext> groups = model.getGroups();
		Object[] values = new Object[groups.size()+1];
		values[0] = new GroupContext("All groups", GroupContext.ALL_GROUPS_ID);
		int j = 1;
		Iterator<GroupContext> i = groups.iterator();
		while (i.hasNext()) {
			values[j] = i.next();
			j++;
		}
		return new JComboBox(values);
	}
	
	/** Initializes the components composing the display.  */
	private void initComponents()
	{
		groupsBoxes = new ArrayList<JComboBox>();
		groupsBox = createBox();
		otherOwners = new LinkedHashMap<Long, String>();
		otherOwnersPanel = new JPanel();
		otherOwnersPanel.setBackground(UIUtilities.BACKGROUND_COLOR);
		scopes = new HashMap<Integer, JCheckBox>(model.getNodes().size());
		types = new HashMap<Integer, JCheckBox>(model.getTypes().size());
		IconManager icons = IconManager.getInstance();
 		fromDate = UIUtilities.createDatePicker(false);
 		fromDate.setBackground(UIUtilities.BACKGROUND_COLOR);
		toDate = UIUtilities.createDatePicker(false);
		toDate.setBackground(UIUtilities.BACKGROUND_COLOR);
		
		clearDate = new JButton(icons.getIcon(IconManager.CANCEL_22));
		clearDate.setToolTipText("Reset the dates");
		UIUtilities.unifiedButtonLookAndFeel(clearDate);
		clearDate.setBackground(UIUtilities.BACKGROUND_COLOR);
		clearDate.setActionCommand(""+SearchComponent.RESET_DATE);
		clearDate.addActionListener(model);
		
		fullTextArea = new JTextField(AREA_COLUMNS);
		fullTextArea.addKeyListener(new KeyAdapter() {

            /** Finds the phrase. */
            public void keyPressed(KeyEvent e)
            {
            	Object source = e.getSource();
            	if (source != fullTextArea) return;
            	switch (e.getKeyCode()) {
					case KeyEvent.VK_ENTER:
						model.search();
				}
            }
        });
		
	
		usersAsOwner = new JTextField(AREA_COLUMNS);
		usersAsAnnotator = new JTextField(AREA_COLUMNS);
		usersAsAnnotator.setEditable(false);
		usersAsOwner.setEditable(false);
		
		dates = new JComboBox(dateOptions);
		dates.setBackground(UIUtilities.BACKGROUND_COLOR);
		dates.addActionListener(model);
		dates.setActionCommand(""+SearchComponent.DATE);
		ownerButton = new JButton(icons.getIcon(IconManager.OWNER));
		ownerButton.setToolTipText("Select the owner of the objects.");
		ownerButton.setBackground(UIUtilities.BACKGROUND_COLOR);
		//UIUtilities.unifiedButtonLookAndFeel(ownerButton);
		ownerButton.addActionListener(model);
		ownerButton.setActionCommand(""+SearchComponent.OWNER);
		ownerButton.setEnabled(false);
		usersAsOwner.setEnabled(false);
		annotatorButton = new JButton(icons.getIcon(IconManager.OWNER));
		annotatorButton.setToolTipText("Select the users who annotated the " +
										"objects.");
		annotatorButton.setBackground(UIUtilities.BACKGROUND_COLOR);
		annotatorButton.setEnabled(false);
		usersAsAnnotator.setEnabled(false);
		UIUtilities.unifiedButtonLookAndFeel(annotatorButton);
		annotatorButton.addActionListener(model);
		annotatorButton.setActionCommand(""+SearchComponent.ANNOTATOR);
		
		currentUserAsOwner = new JCheckBox("Me");
		currentUserAsOwner.setSelected(true);
		currentUserAsOwner.setBackground(UIUtilities.BACKGROUND_COLOR);
		currentUserAsAnnotator = new JCheckBox("Me");
		currentUserAsAnnotator.setSelected(true);
		currentUserAsAnnotator.setBackground(UIUtilities.BACKGROUND_COLOR);
		othersAsOwner = new JCheckBox("Others");
		othersAsOwner.setBackground(UIUtilities.BACKGROUND_COLOR);
		othersAsAnnotator = new JCheckBox("Others");
		othersAsAnnotator.setBackground(UIUtilities.BACKGROUND_COLOR);
		othersAsOwner.addChangeListener(new ChangeListener() {
		
			public void stateChanged(ChangeEvent e) {
				ownerButton.setEnabled(othersAsOwner.isSelected());
				usersAsOwner.setEnabled(othersAsOwner.isSelected());
			}
		});
		
		othersAsAnnotator.addChangeListener(new ChangeListener() {
			
			public void stateChanged(ChangeEvent e) {
				annotatorButton.setEnabled(othersAsAnnotator.isSelected());
				usersAsAnnotator.setEnabled(othersAsAnnotator.isSelected());
			}
		});
		
		caseSensitive = new JCheckBox("Case sensitive");
		areas = new LinkedHashMap<JTextField, JLabel>();
		helpBasicButton = new JButton(icons.getIcon(IconManager.HELP));
		helpBasicButton.setToolTipText("Search Tips.");
		helpBasicButton.setBackground(UIUtilities.BACKGROUND_COLOR);
		UIUtilities.unifiedButtonLookAndFeel(helpBasicButton);
		helpBasicButton.addActionListener(model);
		helpBasicButton.setActionCommand(""+SearchComponent.HELP);
		

		formats = new JComboBox(fileFormats);
		formats.setEnabled(false);
//		ButtonGroup group = new ButtonGroup();
//		creationTime = new JRadioButton("Created");
//		creationTime.setSelected(true);
//		creationTime.setBackground(UIUtilities.BACKGROUND_COLOR);
//		updatedTime = new JRadioButton("Updated");
//		updatedTime.setBackground(UIUtilities.BACKGROUND_COLOR);
//		group.add(creationTime);
//		group.add(updatedTime);
		
		SearchContext ctx = model.getSearchContext();
		if (ctx == null) return;
//		if (ctx.getTimeType() == SearchContext.UPDATED_TIME)
//			updatedTime.setSelected(true);
		List<Integer> l = ctx.getOwnerSearchContext();
		if (l != null) {
			othersAsOwner.setSelected(l.contains(SearchContext.OTHERS));
			currentUserAsOwner.setSelected(
					l.contains(SearchContext.CURRENT_USER));
		}
		
		l = ctx.getAnnotatorSearchContext();
		if (l != null) {
			othersAsAnnotator.setSelected(l.contains(SearchContext.OTHERS));
			currentUserAsAnnotator.setSelected(
					l.contains(SearchContext.CURRENT_USER));
		}
		int dateIndex = ctx.getDateIndex();
		if (dateIndex != -1) dates.setSelectedIndex(dateIndex);
		
		//initialize
//		setDateIndex();
	}
	
	/**
	 * Resets the date fields
	 */ 
	public void resetDate() {
	    toDate.setDate(null);
	    fromDate.setDate(null);
	}
	
	/** Lays out the selected users. */
	private void layoutOtherOwners()
	{
		otherOwnersPanel.removeAll();
		Iterator<Long> i = otherOwners.keySet().iterator();
		long id;
		otherOwnersPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;
		JButton button;
		JToolBar bar;
		JLabel label;
		IconManager icons = IconManager.getInstance();
		Icon icon = icons.getIcon(IconManager.CLOSE);
		while (i.hasNext()) {
			c.gridx = 0;
			c.weightx = 0;
			id = i.next();
			label = new JLabel(otherOwners.get(id));
			label.setBackground(UIUtilities.BACKGROUND_COLOR);
			otherOwnersPanel.add(label, c);
			c.gridx = 1;
			button = new JButton(icon);
			button.setBackground(UIUtilities.BACKGROUND_COLOR);
			UIUtilities.unifiedButtonLookAndFeel(button);
			//button.setBorder(null);
			button.setToolTipText("Remove the user.");
			button.setActionCommand(""+id);
			button.addActionListener(this);
			bar = new JToolBar();
			bar.setBackground(UIUtilities.BACKGROUND_COLOR);
			bar.setFloatable(false);
			bar.setRollover(true);
			bar.setBorder(null);
			bar.add(button);
			otherOwnersPanel.add(bar, c);
			++c.gridy;
		}
		otherOwnersPanel.validate();
		otherOwnersPanel.repaint();
	}
	
	/**
	 * Sets the name of the passed user in the specified <code>TextField</code>.
	 * 
	 * @param name	The value to set.
	 * @param field	The UI component to handle.
	 */
	private void setUserString(String name, JTextField field)
	{
		List<String> values = SearchUtil.splitTerms(field.getText(), 
											SearchUtil.QUOTE_SEPARATOR);
		String v = "";
		int n = values.size();
		if (n == 0)
			v = SearchUtil.QUOTE_SEPARATOR+name+SearchUtil.QUOTE_SEPARATOR;
		else {
			Iterator i = values.iterator();
			String value;
			boolean exist = false;
			int index = 0;
			StringBuffer buffer = new StringBuffer();
			while (i.hasNext()) {
				value = (String) i.next();
				if (!value.equals(name)) {
					buffer.append(SearchUtil.QUOTE_SEPARATOR);
					buffer.append(value);
					buffer.append(SearchUtil.QUOTE_SEPARATOR);
					if (index < n)
						buffer.append(SearchUtil.SPACE_SEPARATOR);
				}
				index++;
			}
			v = buffer.toString();
			if (!exist) 
				v += SearchUtil.QUOTE_SEPARATOR+name+SearchUtil.QUOTE_SEPARATOR;
		}
		field.setText(v);
	}
	
	/**
	 * Retrieves the users to exclude.
	 * 
	 * @param field The text field to handle.
	 * @return See above.
	 */
	private List<String> getExcludedUsers(JTextField field)
	{
		String text = field.getText();
		if (text == null || text.length() == 0) return null;
		if (!text.contains(SearchUtil.MINUS_SEPARATOR)) return null;
		List<String> l = SearchUtil.splitTerms(text, 
											SearchUtil.QUOTE_SEPARATOR);
		int index = 0;
		Iterator i;
		List<String> excluded = new ArrayList<String>();
		if (l != null) {
			i = l.iterator();
			String value;
			String nextItem;
			while (i.hasNext()) {
				value = (String) i.next();
				if (SearchUtil.MINUS_SEPARATOR.equals(value)) {
					nextItem = l.get(index+1);
					if (nextItem != null)
						excluded.add(nextItem);
				}
				index++;
			}
		}
		return excluded;
	}
	
	/**
	 * Retrieves the users to exclude.
	 * 
	 * @param field The text field to handle.
	 * @return See above.
	 */
	private List<String> getUsers(JTextField field)
	{
		String text = field.getText();
		if (text == null || text.length() == 0) return null;
		List<String> l = SearchUtil.splitTerms(text, 
										SearchUtil.QUOTE_SEPARATOR);
		Iterator i;
		List<String> terms = new ArrayList<String>();
		if (l != null) {
			i = l.iterator();
			String value;
			while (i.hasNext()) {
				value = (String) i.next();
				if (!SearchUtil.MINUS_SEPARATOR.equals(value))
					terms.add(value);
			}
		}
		return terms;
	}
	
	/** 
	 * Builds the panel hosting the time fields.
	 * 
	 * @return See above;
	 */
	private JPanel buildTimeRange()
	{
		JPanel p = new JPanel();
		p.setBackground(UIUtilities.BACKGROUND_COLOR);
		p.add(UIUtilities.setTextFont("From: "));
		p.add(fromDate);
		p.add(UIUtilities.setTextFont("To: "));
		p.add(toDate);
		p.add(clearDate);
		return p;
	}
	
	private void layoutGroup()
	{
		if (groupRow == null) {
			groupRow = new JPanel();
			groupRow.setLayout(new FlowLayout(FlowLayout.LEFT));
			groupRow.setBackground(UIUtilities.BACKGROUND_COLOR);
		}
		groupRow.removeAll();
		if (groupsBox != null) {
			groupRow.add(new JLabel("Search in Group:"));
			groupRow.add(groupsBox);
		}
	}
	
	/** 
	 * Builds and lays out the component displaying the various options.
	 * 
	 * @return See above.
	 */
	private JPanel buildFields()
	{
		
        List<SearchObject> nodes = model.getNodes();
		SearchObject n;
		int m = nodes.size();
		JCheckBox box;
		JPanel p = new JPanel();
		p.setBackground(UIUtilities.BACKGROUND_COLOR);
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1.0;
		c.gridy = 1;
		List<Integer> ctxNodes = null;
		SearchContext ctx = model.getSearchContext();
		if (ctx != null) ctxNodes = ctx.getContext();
		if (ctxNodes == null) {
			for (int i = 0; i < m; i++) {
				n = nodes.get(i);
				box = new JCheckBox(n.getDescription());
				box.setBackground(UIUtilities.BACKGROUND_COLOR);
				
				if (i%2 == 0) {
				    c.gridy++;
				}
				
				p.add(box, c);
				scopes.put(n.getIndex(), box);
			}
		} else {
			for (int i = 0; i < m; i++) {
				n = nodes.get(i);
				box = new JCheckBox(n.getDescription());
				box.setBackground(UIUtilities.BACKGROUND_COLOR);
				box.setSelected(ctxNodes.contains(n.getIndex()));
				if (i%2 == 0) c.gridy++;
				
				p.add(box, c);
				scopes.put(n.getIndex(), box);
			}
		}
		c.gridy++;
		UIUtilities.setBoldTitledBorder("Fields", p);
		return p;
	}

	/** 
         * Builds and lays out the component displaying the various options.
         * 
         * @return See above.
         */
        private JPanel buildScope()
        {
            JPanel p = new JPanel();
            p.setBackground(UIUtilities.BACKGROUND_COLOR);
            p.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridy = 0;
            layoutGroup();
            p.add(groupRow, c);
            return p;
        }
        
	/** 
	 * Builds and lays out the component displaying the various types.
	 * 
	 * @return See above.
	 */
	private JPanel buildType()
	{
		JPanel p = new JPanel();
		p.setBackground(UIUtilities.BACKGROUND_COLOR);
		p.setLayout(new GridBagLayout());
        //p.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(3, 3, 3, 3);
        List<SearchObject> nodes = model.getTypes();
        List<Integer> ctxNodes = null;
		SearchContext ctx = model.getSearchContext();
		if (ctx != null) ctxNodes = ctx.getType();
        
		SearchObject n;
		int m = nodes.size();
		JCheckBox box;
		c.weightx = 1.0;
		
		
		if (ctxNodes == null) {
			for (int i = 0; i < m; i++) {
				n = nodes.get(i);
				box = new JCheckBox(n.getDescription());
				box.setBackground(UIUtilities.BACKGROUND_COLOR);
				box.setSelected(true);
				p.add(box, c);
				types.put(n.getIndex(), box);
			}
		} else {
			for (int i = 0; i < m; i++) {
				n = nodes.get(i);
				box = new JCheckBox(n.getDescription());
				box.setBackground(UIUtilities.BACKGROUND_COLOR);
				box.setSelected(ctxNodes.contains(n.getIndex()));
				p.add(box, c);
				types.put(n.getIndex(), box);
			}
		}
		
		UIUtilities.setBoldTitledBorder(TYPE_TITLE, p);
		return p;
	}

	/**
	 * Builds and lays out the Basic search component.
	 * 
	 * @return See above.
	 */
	private JPanel buildBasicSearchComp()
	{
		basicSearchComp = new JPanel();
		basicSearchComp.setBackground(UIUtilities.BACKGROUND_COLOR);
		UIUtilities.setBoldTitledBorder(SEARCH_TITLE, basicSearchComp);
		basicSearchComp.setLayout(new BoxLayout(basicSearchComp, 
				BoxLayout.Y_AXIS));
		basicSearchComp.add(fullTextArea);
		JToolBar bar = new JToolBar();
		bar.setBackground(UIUtilities.BACKGROUND_COLOR);
		bar.setFloatable(false);
		bar.setRollover(true);
		bar.setBorder(null);
		bar.add(helpBasicButton);
		if (controls != null && controls.size() > 0) {
			Iterator<JButton> i = controls.iterator();
			while (i.hasNext()) {
				bar.add(i.next());
			}
		}
		JPanel p = UIUtilities.buildComponentPanel(bar);
		p.setBackground(UIUtilities.BACKGROUND_COLOR);

		basicSearchComp.add(p);
		return basicSearchComp;
	}
	
	/**
	 * Builds and lays out the Advanced search component.
	 * 
	 * @return See above.
	 */
	private JPanel buildAdvancedSearchComp()
	{
		advancedSearchComp = new JPanel();
		advancedSearchComp.setBackground(UIUtilities.BACKGROUND_COLOR);
		JPanel p = new JPanel();
		p.setBackground(UIUtilities.BACKGROUND_COLOR);
		UIUtilities.setBoldTitledBorder(ADVANCED_SEARCH_TITLE, 
				advancedSearchComp);
		p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(3, 3, 3, 3);
        Iterator i = areas.keySet().iterator();
        JLabel label;
        JTextField area;
        while (i.hasNext()) {
            ++c.gridy;
            c.gridx = 0;
            area = (JTextField) i.next();
            area.setBackground(UIUtilities.BACKGROUND_COLOR);
            label = areas.get(area);
            c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
            c.weightx = 0.0;  
            p.add(label, c);
            label.setLabelFor(area);
            ++c.gridy;
            c.gridwidth = GridBagConstraints.REMAINDER;     //end row
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            p.add(area, c);  
        }
        ++c.gridy;
        c.gridx = 0;
        c.weightx = 0.0; 
        p.add(UIUtilities.setTextFont(SEARCH_TIP, TIP_FONT_TYPE, 
        							TIP_FONT_SIZE), c); 
        advancedSearchComp.setLayout(new BoxLayout(advancedSearchComp, 
				BoxLayout.Y_AXIS));
        advancedSearchComp.add(p);
        
		//Tool Bar
        JToolBar bar = new JToolBar();
        bar.setBackground(UIUtilities.BACKGROUND_COLOR);
		bar.setFloatable(false);
		bar.setRollover(true);
		bar.setBorder(null);
		if (controls != null && controls.size() > 0) {
			Iterator<JButton> j = controls.iterator();
			while (j.hasNext()) {
				bar.add(j.next());
			}
		}
		JPanel comp = UIUtilities.buildComponentPanel(bar);
	    comp.setBackground(UIUtilities.BACKGROUND_COLOR);
		advancedSearchComp.add(comp);
		return advancedSearchComp;
	}
	/**
	 * Builds the UI component hosting the terms to search for.
	 * 
	 * @return See above.
	 */
	private JPanel buildSearchFor()
	{
		if (searchFor == null) {
			searchFor = new JPanel();
		    searchFor.setBackground(UIUtilities.BACKGROUND_COLOR);
			searchFor.setLayout(new BoxLayout(searchFor, BoxLayout.Y_AXIS));
		}
		searchFor.removeAll();
		searchFor.add(buildBasicSearchComp());
		return searchFor;
	}
	
	/**
	 * Lays out the specified text field and button.
	 * 
	 * @param box		The box to lay out.
	 * @param field		The field to lay out.
	 * @param button	The button to lay out.
	 * @return See above.
	 */
	private JPanel buildOtherUserSelection(JCheckBox box, JComponent field, 
										JButton button)
	{
		JPanel p = new JPanel();
	    p.setBackground(UIUtilities.BACKGROUND_COLOR);
		p.setLayout(new GridBagLayout());
		//p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        p.add(box, c);
        c.gridx++;
		p.add(Box.createHorizontalStrut(5), c);
        JToolBar bar = new JToolBar();
	    bar.setBackground(UIUtilities.BACKGROUND_COLOR);
        bar.setFloatable(false);
        bar.setFloatable(false);
        bar.setRollover(true);
        bar.setBorder(null);
        bar.add(button);
        c.gridx++;
		p.add(bar, c);
		c.gridx++;
		p.add(Box.createHorizontalStrut(5), c);
		c.gridx++;
		c.gridheight = 2;
		p.add(field, c);
		return p;
	}

	/**
	 * Builds the UI component hosting the users to search for.
	 * 
	 * @return See above.
	 */
	private JPanel buildUsers()
	{	
		JPanel usersPanel = new JPanel();
	    usersPanel.setBackground(UIUtilities.BACKGROUND_COLOR);
		//UIUtilities.setBoldTitledBorder(USER_TITLE, usersPanel);
		usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));
		JPanel content = new JPanel();
	    content.setBackground(UIUtilities.BACKGROUND_COLOR);
		content.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.gridy = 0;
        c.gridwidth = 2;
        content.add(UIUtilities.setTextFont("Owned by "), c);
        c.gridy++;
        c.gridwidth = 1;
        c.gridx = 0;
        content.add(currentUserAsOwner, c);
		c.gridy++;
		c.ipady = 10;
		content.add(buildOtherUserSelection(othersAsOwner, otherOwnersPanel, 
				ownerButton), c);
		JPanel p = UIUtilities.buildComponentPanel(content);
		p.setBackground(UIUtilities.BACKGROUND_COLOR);
		usersPanel.add(p);
		//usersPanel.add(new JSeparator());
		content = new JPanel();
	    content.setBackground(UIUtilities.BACKGROUND_COLOR);
		content.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
		c.gridx = 0;
		/* commented out 02/06
		c.gridwidth = 2;
		c.gridy++;
		content.add(UIUtilities.setTextFont("Annotated by "), c);
		c.gridy++;
	    c.gridwidth = 1;
        c.gridx = 0;
        content.add(currentUserAsAnnotator, c);
		c.gridy++;
		content.add(buildUserSelection(othersAsAnnotator, usersAsAnnotator, 
				annotatorButton), c);
         */
		p = UIUtilities.buildComponentPanel(content);
		p.setBackground(UIUtilities.BACKGROUND_COLOR);
		usersPanel.add(p);
		return usersPanel;
	}
	
	/**
	 * Builds the UI component hosting the time interval.
	 * 
	 * @return See above.
	 */
	private JPanel buildDate()
	{
		JPanel content = new JPanel();
		content.setBackground(UIUtilities.BACKGROUND_COLOR);
		content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
//		content.add(creationTime);
//		content.add(updatedTime);
		
		JPanel p = new JPanel();
		p.setBackground(UIUtilities.BACKGROUND_COLOR);
		p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.ipady = 10;
        p.add(content, c);
        c.ipady = 0;
        c.gridy++;
        //p.add(dates, c);
       // c.gridy++;
		p.add(buildTimeRange(), c);
		
		JPanel panel = UIUtilities.buildComponentPanel(p);
		panel.setBackground(UIUtilities.BACKGROUND_COLOR);
		//UIUtilities.setBoldTitledBorder(DATE_TITLE, panel);
		return panel;
	}

	
	/** Builds and lays out the UI. */
	private void buildGUI()
	{
		setBorder(null);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(0, 2, 2, 0);
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		setBackground(UIUtilities.BACKGROUND_COLOR);
		
		add(buildSearchFor(), c);//, "0, 0");
		
		SeparatorPane sep = new SeparatorPane();
		sep.setBackground(UIUtilities.BACKGROUND_COLOR);
		c.gridy++;
		add(sep, c);//, "0, 1");
		
		JXTaskPane pane = UIUtilities.createTaskPane("Search for", null); 
                pane.setCollapsed(false);
                pane.add(buildType());
                c.gridy++;
                add(pane, c);//, "0, 2")
                
		pane = UIUtilities.createTaskPane("Restrict to" , null); 
		pane.setCollapsed(false);
		pane.add(buildFields());
		c.gridy++;
		add(pane, c);//, "0, 2");
		
		pane = UIUtilities.createTaskPane("Scope" , null); 
                pane.setCollapsed(false);
                pane.add(buildScope());
                c.gridy++;
                add(pane, c);//, "0, 2");
		
		datePane = UIUtilities.createTaskPane("Date", null); 
		datePane.add(buildDate());
		c.gridy++;
		add(datePane, c);//, "0, 4");

//		setDateIndex();
	}

	/** 
	 * Creates a new instance.
	 * 
	 * @param model Reference to the model. Mustn't be <code>null</code>.
	 * @param controls The collection of buttons to add next to the help etc.
	 */
	SearchPanel(SearchComponent model, List<JButton> controls)
	{
		if (model == null)
			throw new IllegalArgumentException("No model.");
		this.model = model;
		this.controls = controls;
		initComponents();
		buildGUI();
	}

	/** Resets.*/
	public void reset()
	{
		groupsBoxes.clear();
		int n = model.getGroups().size();
		if (n == 1) groupsBox = null;
		else groupsBox = createBox();
		layoutGroup();
		groupRow.setVisible(n > 1);
		validate();
		repaint();
	}
	
//	/** 
//	 * Enables the date fields if the selected index is 
//	 * {@link SearchContext#RANGE}. 
//	 */
//	void setDateIndex()
//	{
//		int index = dates.getSelectedIndex();
//		fromDate.setEnabled(index == SearchContext.RANGE);
//		fromDate.getEditor().setEnabled(false);
//		toDate.setEnabled(index == SearchContext.RANGE);
//		toDate.getEditor().setEnabled(false);
//	}
	
//	/**
//	 * Returns the currently selected time index.
//	 * 
//	 * @return See above.
//	 */
//	int getSelectedDate() { return dates.getSelectedIndex(); }
	
	/**
	 * Returns <code>true</code> if the search is case sensitive,
	 * <code>false</code> otherwise.
	 * 
	 * @return See above.
	 */
	boolean isCaseSensitive() { return caseSensitive.isSelected(); }
	
	/**
	 * Returns the start time.
	 * 
	 * @return See above.
	 */
	Timestamp getFromDate()
	{
		Date d = fromDate.getDate();
		if (d == null) return null;
		return new Timestamp(d.getTime());
	}
	
	/**
	 * Returns the start time.
	 * 
	 * @return See above.
	 */
	Timestamp getToDate()
	{
		Date d = toDate.getDate();
		if (d == null) return null;
		return new Timestamp(d.getTime());
	}

	/** 
	 * Returns the scope of the search.
	 * 
	 * @return See above.
	 */
	List<Integer> getScope()
	{
		List<Integer> list = new ArrayList<Integer>();
		Iterator i = scopes.keySet().iterator();
		JCheckBox box;
		Integer key;
		while (i.hasNext()) {
			key = (Integer) i.next();
			box = scopes.get(key);
			if (box.isSelected())
				list.add(key);
		}
		return list;
	}
	
	/** 
	 * Returns the scope of the search.
	 * 
	 * @return See above.
	 */
	List<Integer> getType()
	{
		List<Integer> list = new ArrayList<Integer>();
		Iterator i = types.keySet().iterator();
		JCheckBox box;
		Integer key;
		while (i.hasNext()) {
			key = (Integer) i.next();
			box = types.get(key);
			if (box.isSelected())
				list.add(key);
		}
		return list;
	}
	
	/** 
	 * Sets the values to add. 
	 * 
	 * @param values The values to add.
	 */
	void setSomeValues(List<String> values)
	{
		if (values == null || values.size() == 0) return;
		StringBuffer text = new StringBuffer();
		Iterator<String> i = values.iterator();
		while (i.hasNext()) {
			text.append(i.next());
			text.append(SearchUtil.SPACE_SEPARATOR);
		}
			
		fullTextArea.setText(text.toString());
	}
	
	/**
	 * Returns the terms that may be in the document.
	 * 
	 * @return See above.
	 */
	String[] getQueryTerms()
	{
		String text;
			text = fullTextArea.getText();
			if (text != null && text.trim().length() > 0) {
				List<String> l = SearchUtil.splitTerms(text);
				if (l.size() > 0) 
					return l.toArray(new String[] {});
			}
		
		List<String> l = SearchUtil.splitTerms(text);

		if (l.size() > 0)
			return l.toArray(new String[] {});
		return null;
	}
	
	
	/**
	 * Returns the context of the search for users.
	 * 
	 * @return See above.
	 */
	List<Integer> getOwnerSearchContext()
	{
		List<Integer> context = new ArrayList<Integer>();
		if (currentUserAsOwner.isSelected())
			context.add(SearchContext.CURRENT_USER);
		if (othersAsOwner.isSelected())
			context.add(SearchContext.OTHERS);
		return context;
	}
	
	/**
	 * Returns the context of the search for users.
	 * 
	 * @return See above.
	 */
	List<Integer> getAnnotatorSearchContext()
	{
		List<Integer> context = new ArrayList<Integer>();
		if (currentUserAsAnnotator.isSelected())
			context.add(SearchContext.CURRENT_USER);
		if (othersAsAnnotator.isSelected())
			context.add(SearchContext.OTHERS);
		return context;
	}
	
	/**
	 * Returns the collection of the users who own the objects.
	 * 
	 * @return See above.
	 */
	List<Long> getOwners()
	{ 
		List<Long> users = new ArrayList<Long>();
		Iterator<Long> i = otherOwners.keySet().iterator();
		while (i.hasNext())
			users.add(i.next());
		return users; 
	}
	
	/**
	 * Returns the collection of the users who annotated the objects.
	 * 
	 * @return See above.
	 */
	List<String> getAnnotators() { return getUsers(usersAsAnnotator); }
	
	/** Indicates to set the focus on the search area. */
	void setFocusOnSearch()
	{
		if (areas != null) {
			Iterator i = areas.keySet().iterator();
			while (i.hasNext()) {
				((JTextField) i.next()).requestFocus();
				break;
			}
		}
	}
	
	/**
	 * Sets the name of the selected user.
	 * 
	 * @param userID The id of the selected user.
	 * @param name   The string to set.
	 */
	void setOwnerString(long userID, String name)
	{
		if (otherOwners.containsKey(userID)) return;
		otherOwners.put(userID, name);
		layoutOtherOwners();
		validate();
		repaint();
	}
	
	/**
	 * Sets the name of the selected user.
	 * 
	 * @param name The string to set.
	 */
	void setAnnotatorString(String name)
	{
		setUserString(name, usersAsAnnotator);
	}
	
	/**
	 * Returns the type of files to search for.
	 * 
	 * @return See above.
	 */
	int getAttachment() { return formats.getSelectedIndex(); }
	
//	/**
//	 * Returns the index of the time.
//	 * 
//	 * @return See above.
//	 */
//	int getTimeIndex()
//	{
//		if (creationTime.isSelected()) return SearchContext.CREATION_TIME;
//		if (updatedTime.isSelected()) return SearchContext.UPDATED_TIME;
//		return -1;
//	}

	/**
	 * Returns the collection of excluded users.
	 * 
	 * @return See above.
	 */
	List<String> getExcludedAnnotators()
	{
		return getExcludedUsers(usersAsAnnotator);
	}
	
	/**
	 * Returns the collection of excluded users.
	 * 
	 * @return See above.
	 */
	List<String> getExcludedOwners()
	{
		return getExcludedUsers(usersAsOwner);
	}
	
	/**
	 * Returns the selected groups.
	 * 
	 * @return See above.
	 */
	List<Long> getSelectedGroups()
	{
		Collection<GroupContext> l = getGroups();
		Iterator<GroupContext> i = l.iterator();
		List<Long> ids = new ArrayList<Long>(l.size());
		while (i.hasNext()) {
			ids.add(i.next().getId());
		}
		return ids;
	}
	
	/**
	 * Removes the user from the display.
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		String s = e.getActionCommand();
		int index = Integer.parseInt(s);
		otherOwners.remove(Long.valueOf(index));
		layoutOtherOwners();
		validate();
		repaint();
	}
}
