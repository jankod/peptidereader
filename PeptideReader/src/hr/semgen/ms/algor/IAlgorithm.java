package hr.semgen.ms.algor;

import hr.semgen.masena.share.model.Peak;
import hr.semgen.masena.share.model.Peptid;

import java.util.List;

public interface IAlgorithm  {

	
	
	/**
	 * 
	 * @param peaks
	 * @param massPrecursor
	 * @param devPlusMinus inace smo dobijali to sa Activator.PlusMinusR_or_K
	 * @return
	 */
	public List<Peptid> run(List<Peak> peaks, float massPrecursor, double devPlusMinus);
		
	
	
}
