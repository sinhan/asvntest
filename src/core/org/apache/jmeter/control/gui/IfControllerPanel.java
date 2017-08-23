package org.apache.jmeter.control.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.control.IfController;
import org.apache.jmeter.gui.util.FocusRequester;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;

/**
 * The user interface for a controller which specifies that its subcomponents
 * should be executed while a condition holds.  This component can be
 * used standalone or embedded into some other component.
 *
 * @author    Cyrus Montakab
 * @version   $Revision: 323445 $ $Date: 2003-09-05 16:18:02 -0700 (Fri, 05 Sep 2003) $
 */

public class IfControllerPanel extends AbstractControllerGui
	implements ActionListener
{

	  private static final String CONDITION_TITLE = "if_controller_title";
	  private static final String CONDITION_LABEL = "if_controller_label";

	/**
	 * A field allowing the user to specify the number of times the controller
	 * should loop.
	 */
	private JTextField theCondition;

	/**
	 * Boolean indicating whether or not this component should display its
	 * name. If true, this is a standalone component. If false, this component
	 * is intended to be used as a subpanel for another component.
	 */
	private boolean displayName = true;


	/** The name of the loops field component. */
	private static final String CONDITION = "JS_Condition";

	/**
	 * Create a new LoopControlPanel as a standalone component.
	 */
	public IfControllerPanel()   {
		this(true);
	}

	/**
	 * Create a new IfControllerPanel as either a standalone or an embedded
	 * component.
	 *
	 * @param displayName  indicates whether or not this component should
	 *                     display its name.  If true, this is a standalone
	 *                     component.  If false, this component is intended
	 *                     to be used as a subpanel for another component.
	 */
	public IfControllerPanel (boolean displayName)
	{
		this.displayName = displayName;
		init();
	}

	/**
	 * A newly created component can be initialized with the contents of
	 * a Test Element object by calling this method.  The component is
	 * responsible for querying the Test Element object for the
	 * relevant information to display in its GUI.
	 *
	 * @param element the TestElement to configure
	 */
	public void configure(TestElement element) {
		super.configure(element);
		if (element instanceof IfController) {
			theCondition.setText(((IfController) element).getCondition());
		}

	}

	/**
	 *  Implements JMeterGUIComponent.createTestElement()
	 */
	public TestElement createTestElement() {
		IfController controller = new IfController();
		modifyTestElement(controller);
		return controller;
	}

	/**
	 * Implements JMeterGUIComponent.modifyTestElement(TestElement)
	 */
	public void modifyTestElement(TestElement controller) {
		configureTestElement(controller);
		if (controller instanceof IfController)
		{
			if (theCondition.getText().length() > 0) {
				((IfController) controller).setCondition(theCondition.getText());
			} else {
				((IfController) controller).setCondition("");
			}
		}
	}

	/**
	 * Invoked when an action occurs.  This implementation assumes that the
	 * target component is the infinite loops checkbox.
	 *
	 * @param event the event that has occurred
	 */
	public void actionPerformed(ActionEvent event) {
		new FocusRequester(theCondition);
	}

	/* Implements JMeterGUIComponent.getStaticLabel() */
	public String getStaticLabel() {
		return JMeterUtils.getResString(CONDITION_TITLE);
	}

	/**
	 * Initialize the GUI components and layout for this component.
	 */
	private void init()
	{
		// Standalone
		if (displayName) {
			setLayout(new BorderLayout(0, 5));
			setBorder(makeBorder());
			add(makeTitlePanel(), BorderLayout.NORTH);

			JPanel mainPanel = new JPanel(new BorderLayout());
			mainPanel.add(createConditionPanel(), BorderLayout.NORTH);
			add(mainPanel, BorderLayout.CENTER);

		} else {
			// Embedded
			setLayout(new BorderLayout());
			add(createConditionPanel(), BorderLayout.NORTH);
		}
	}


	/**
	 * Create a GUI panel containing the condition.
	 *
	 * @return a GUI panel containing the condition components
	 */
	private JPanel createConditionPanel()  {
		JPanel conditionPanel = new JPanel(new BorderLayout(5, 0));

		// Condition LABEL
		JLabel conditionLabel =
		new JLabel(JMeterUtils.getResString( CONDITION_LABEL ));
		conditionPanel.add(conditionLabel, BorderLayout.WEST);

		// TEXT FIELD
		theCondition = new JTextField("");
		theCondition.setName(CONDITION);
		conditionLabel.setLabelFor(theCondition);
		conditionPanel.add(theCondition, BorderLayout.CENTER);
		theCondition.addActionListener(this);

		conditionPanel.add(
			Box.createHorizontalStrut( conditionLabel.getPreferredSize().width
			+ theCondition.getPreferredSize().width)
			, BorderLayout.NORTH);

		return conditionPanel;
	}
}