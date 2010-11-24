package de.fu_berlin.inf.dpp.ui.widgetGallery.suits.basic;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.fu_berlin.inf.dpp.ui.widgetGallery.demos.Demo;
import de.fu_berlin.inf.dpp.ui.widgetGallery.demos.DemoContainer;
import de.fu_berlin.inf.dpp.ui.widgets.RoundedComposite;

public class RoundedCompositeDemo extends Demo {
	public class RoundedCompositeContent extends Composite {
		public RoundedCompositeContent(Composite parent) {
			super(parent, SWT.NONE);
			this.setLayout(new GridLayout(2, false));
			
			Label label = new Label(this, SWT.NONE);
			label.setText("I'm a label.");
			
			Button button = new Button(this, SWT.PUSH);
			button.setText("I'm a push button");
		}
	}
	
	public RoundedCompositeDemo(DemoContainer demoContainer, String title) {
		super(demoContainer, title);
	}
	
	@Override
	public void createPartControls(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		
		/* row 1 */
		Label l1 = new Label(parent, SWT.NONE);
		l1.setText("SWT.NONE");
		l1.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		
		final Color cl1 = new Color(parent.getDisplay(), 240, 233, 255);
		RoundedComposite c1 = new RoundedComposite(parent, SWT.NONE);
		c1.setBackground(cl1);
		new RoundedCompositeContent(c1);
		c1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		c1.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				cl1.dispose();
			}
		});
		
		/* row 2 */
		Label l2 = new Label(parent, SWT.NONE);
		l2.setText("SWT.NONE");
		l2.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		
		final Color cl2 = new Color(parent.getDisplay(), 233, 240, 255);
		RoundedComposite c2 = new RoundedComposite(parent, SWT.NONE);
		c2.setBackground(cl2);
		new RoundedCompositeContent(c2);
		c2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		c2.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				cl2.dispose();
			}
		});
		
		/* row 3 */
		Label l3 = new Label(parent, SWT.NONE);
		l3.setText("SWT.SEPARATOR");
		l3.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		
		final Color cl3 = new Color(parent.getDisplay(), 233, 255, 241);
		RoundedComposite c3 = new RoundedComposite(parent, SWT.SEPARATOR);
		c3.setBackground(cl3);
		new RoundedCompositeContent(c3);
		c3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		c3.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				cl3.dispose();
			}
		});
		
		/* row 4 */
		Label l4 = new Label(parent, SWT.NONE);
		l4.setText("SWT.SEPARATOR");
		l4.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		
		final Color cl4 = new Color(parent.getDisplay(), 255, 255, 233);
		RoundedComposite c4 = new RoundedComposite(parent, SWT.SEPARATOR);
		c4.setBackground(cl4);
		new RoundedCompositeContent(c4);
		c4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		c4.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				cl4.dispose();
			}
		});
		
		/* row 5 */
		Label l5 = new Label(parent, SWT.NONE);
		l5.setText("SWT.NONE\nGrid Fill");
		l5.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		
		final Color cl5 = new Color(parent.getDisplay(), 233, 245, 255);
		RoundedComposite c5 = new RoundedComposite(parent, SWT.NONE);
		c5.setBackground(cl5);
		new RoundedCompositeContent(c5);
		c5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		c5.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				cl5.dispose();
			}
		});
		
		/* row 6 */
		Label l6 = new Label(parent, SWT.NONE);
		l6.setText("SWT.SEPARATOR\nGrid Fill");
		l6.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		
		final Color cl6 = new Color(parent.getDisplay(), 255, 230, 230);
		RoundedComposite c6 = new RoundedComposite(parent, SWT.SEPARATOR);
		c6.setBackground(cl6);
		new RoundedCompositeContent(c6);
		c6.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		c6.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				cl5.dispose();
			}
		});
	}
}