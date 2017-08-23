package org.apache.jmeter.visualizers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.assertions.CompareAssertionResult;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;

public class ComparisonVisualizer extends AbstractVisualizer implements Clearable {
	JTree resultsTree;

	DefaultTreeModel treeModel;

	DefaultMutableTreeNode root;

	JTextPane base, secondary;

	public ComparisonVisualizer() {
		super();
		init();
	}

	public void add(SampleResult sample) {

		DefaultMutableTreeNode currNode = new DefaultMutableTreeNode(sample);
		treeModel.insertNodeInto(currNode, root, root.getChildCount());
		if (root.getChildCount() == 1) {
			resultsTree.expandPath(new TreePath(root));
		}
	}

	public String getLabelResource() {
		return "comparison_visualizer_title";
	}

	private void init() {
		setLayout(new BorderLayout());
		setBorder(makeBorder());
		add(makeTitlePanel(), BorderLayout.NORTH);
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.add(getTreePanel());
		split.add(getSideBySidePanel());
		add(split, BorderLayout.CENTER);
	}

	private JComponent getSideBySidePanel() {
		JPanel main = new JPanel(new GridLayout(1, 2));
		JScrollPane base = new JScrollPane(getBaseTextPane());
		base.setPreferredSize(base.getMinimumSize());
		JScrollPane secondary = new JScrollPane(getSecondaryTextPane());
		secondary.setPreferredSize(secondary.getMinimumSize());
		main.add(base);
		main.add(secondary);
		main.setPreferredSize(main.getMinimumSize());
		return main;
	}

	private JTextPane getBaseTextPane() {
		base = new JTextPane();
		return base;
	}

	private JTextPane getSecondaryTextPane() {
		secondary = new JTextPane();
		return secondary;
	}

	private JComponent getTreePanel() {
		root = new DefaultMutableTreeNode("Root");
		treeModel = new DefaultTreeModel(root);
		resultsTree = new JTree(treeModel);
		resultsTree.setCellRenderer(new TreeNodeRenderer());
		resultsTree.setCellRenderer(new TreeNodeRenderer());
		resultsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		resultsTree.addTreeSelectionListener(new Selector());
		resultsTree.setRootVisible(false);
		resultsTree.setShowsRootHandles(true);

		JScrollPane treePane = new JScrollPane(resultsTree);
		treePane.setPreferredSize(new Dimension(150, 50));
		JPanel panel = new JPanel(new GridLayout(1, 1));
		panel.add(treePane);
		return panel;
	}

	private class Selector implements TreeSelectionListener {
		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
		 */
		public void valueChanged(TreeSelectionEvent e) {
			try {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) resultsTree.getLastSelectedPathComponent();
				SampleResult sr = (SampleResult) node.getUserObject();
				AssertionResult[] results = sr.getAssertionResults();
				CompareAssertionResult result = null;
				for (AssertionResult r : results) {
					if (r instanceof CompareAssertionResult) {
						result = (CompareAssertionResult) r;
						break;
					}
				}
				if (result == null)
					result = new CompareAssertionResult();
				base.setText(result.getBaseResult());
				secondary.setText(result.getSecondaryResult());
			} catch (Exception err) {
				base.setText("Invalid Node " + err);
				secondary.setText("Invalid Node " + err);
			}
			base.setCaretPosition(0);
			secondary.setCaretPosition(0);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.gui.AbstractJMeterGuiComponent#clear()
	 */
	@Override
	public void clear() {
		super.clear();
		while (root.getChildCount() > 0) {
			// the child to be removed will always be 0 'cos as the nodes are
			// removed the nth node will become (n-1)th
			treeModel.removeNodeFromParent((DefaultMutableTreeNode) root.getChildAt(0));
			base.setText("");
			secondary.setText("");
		}
	}

}
