package hr.semgen.masena.ufb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pomocna klasa za debugiranje na serveru.
 * 
 * @author tag
 * 
 */
public class ServerDubugUtil {

	private static final Logger log = LoggerFactory.getLogger(ServerDubugUtil.class);
	private static BufferedWriter b;
	private static FileWriter w;

	public static void write(UFB_ProteinWrapper noviProt, UFB_ProteinWrapper stariProt) {
		init();
		
		printProt(noviProt);
		
	}

	private static void printProt(UFB_ProteinWrapper noviProt) {
		
	}

	private static void init() {
		if(b != null) {
			return;
		}
		try {
			w = new FileWriter(new File("/tmp/prot_debug.txt"));
			b = new BufferedWriter(w, 1024 * 22);
			
			
			
		} catch (IOException e) {
			log.error("", e);
		}
	}

	
	@Override
	protected void finalize() throws Throwable {
		IOUtils.closeQuietly(b);
		IOUtils.closeQuietly(w);
	}
}
