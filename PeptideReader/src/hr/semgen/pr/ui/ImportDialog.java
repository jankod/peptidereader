package hr.semgen.pr.ui;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

public class ImportDialog extends TitleAreaDialog {
	private Text textMGFAbsciex;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ImportDialog(Shell parentShell) {
		super(parentShell);
		setHelpAvailable(false);
		setShellStyle(SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.TITLE);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Start import");
		setTitleImage(ResourceManager.getPluginImage("PeptideReader", "icons/import_wiz.png"));
		setTitle("Import MS and MS/MS spectra");
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));
		new Label(container, SWT.NONE);
		
		Label lblAbSciexImport = new Label(container, SWT.NONE);
		lblAbSciexImport.setText("AB sciex import");
		new Label(container, SWT.NONE);
		
		textMGFAbsciex = new Text(container, SWT.BORDER);
		textMGFAbsciex.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnBrowseABSciex = new Button(container, SWT.NONE);
		btnBrowseABSciex.setText("Browse...");
		getShell().setText("Import MS/MS data");
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(631, 500);
	}

}
