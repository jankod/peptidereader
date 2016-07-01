
package hr.semgen.pr.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import hr.semgen.pr.ui.ImportDialog;

public class ImportHandler {
	@Execute
	public void execute(Shell shell) {
		ImportDialog im = new ImportDialog(shell);
		im.open();
	}

}