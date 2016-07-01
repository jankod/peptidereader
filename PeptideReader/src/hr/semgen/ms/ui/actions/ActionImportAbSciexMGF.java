package hr.semgen.ms.ui.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;

import hr.semgen.masena.share.model.Peak;
import hr.semgen.masena.share.model.PeakFile;
import hr.semgen.masena.share.util.ProteinConstants;
import hr.semgen.ms.algor.MGFparser;

public class ActionImportAbSciexMGF {

	private ActionEvent e;

	public ActionImportAbSciexMGF(ActionEvent e) {
		this.e = e;
	}

	public static File getDirectory() {
		Preferences root = Preferences.userNodeForPackage(ActionImportAbSciexMGF.class);
		String dirPath = root.get("import-dir", ".");
		
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File(dirPath));
		chooser.setDialogTitle("Choose MGF file AB Sciex");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			// System.out.println("getCurrentDirectory(): " +
			// chooser.getCurrentDirectory());
			// System.out.println("getSelectedFile() : " +
			// chooser.getSelectedFile());
			
			File dir = chooser.getSelectedFile();
			root.put("import-dir", dir.getAbsolutePath());
			return dir;
		}
		return null;
	}

	/**
	 * If return null, this is because user is cancel clicked.
	 * 
	 * @return
	 */
	public List<PeakFile> run(File dir) {

		ArrayList<PeakFile> msmsList = MGFparser.parseStevicaFiles(dir);
		for (PeakFile ms : msmsList) {

			Collection<PeakFile> msmsChilds = ms.getMsmsChilds();
			for (PeakFile msms : msmsChilds) {
				msms.setPositive(true);
			}
		}
		return msmsList;
	}

	protected void primjeniNewCaff(ArrayList<PeakFile> result, double delta) {
		// MS povecati za deltu.
		// MSMS neg povecati za deltu.
		// MSMS poz ostataje isteo osim mase precursora koje treba povecati.

		for (PeakFile ms : result) {
			List<Peak> allPeaks = ms.getAllPeaks();
			for (Peak peak : allPeaks) {
				peak.massPlusDelta(delta);
				// peak.plusMirjanaH1();
			}
			// ms.setName(Double.toString((Double.parseDouble(ms.getName()) +
			// ProteinConstants.MIRJANA_DELTA)));

			Collection<PeakFile> msmsChilds = ms.getMsmsChilds();
			for (PeakFile msms : msmsChilds) {
				// Float m = (float) (msms.getMassPrecursor() +
				// ProteinConstants.MIRJANA_DELTA);
				Float m = (float) (msms.getMassPrecursor() + delta);
				msms.setName(m.toString());

				if (msms.isNegative()) {
					List<Peak> msmsNegPeaks = msms.getAllPeaks();
					for (Peak peak : msmsNegPeaks) {
						// peak.plusMirjanaH1();
						peak.massPlusDelta(delta);
					}
				} else { // positive

					// pronaci dummy pik mass prekursor koji je isti kao i iz
					// negativa pa ga povecati za mirjanu

					List<Peak> msmsPosPeaks = msms.getAllPeaks();
					for (Peak p : msmsPosPeaks) {
						if (p.isDummy()) {

							if (p.getCentroidMass() == (msms.getMassPrecursor() - ProteinConstants.CAFF)) {
								// p.plusMirjanaH1();
								p.massPlusDelta(delta);
								// log.debug(msms + " " + p.getCentroidMass());
							}
						}
					}
				}
				double newMassPrecursor = msms.getMassPrecursor() + delta;
				msms.setMassPrecursor((float) newMassPrecursor);
			}
		}
	}

}
