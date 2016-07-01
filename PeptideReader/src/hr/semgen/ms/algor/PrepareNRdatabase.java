package hr.semgen.ms.algor;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Mjenja sve L u I u NR bazi. Mice header i postvlja SEQ ID, da bude brze za
 * rapsearch
 * 
 * @author tag
 * 
 */
public class PrepareNRdatabase {
	static final Charset ASCII = Charset.forName("ASCII");

	public static void main(String[] args) throws IOException {
		String nrPath = "C:\\Rad\\nr\\nr";

		System.out.println("Start");

		BufferedReader reader = new BufferedReader(new FileReader(nrPath));

		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(nrPath + "_new"));
		String line = reader.readLine();
		int seqID = 1;
		while (line != null) {
			if (line.startsWith(">")) {
				out.write((">" + seqID + "\n").getBytes(ASCII));
				seqID++;

				if (seqID > 100) {
					break;
				}
				line = reader.readLine();
				continue;
			}

			line = line.replace('L', 'I');
			line = line.replace('K', 'Q');
			out.write((line + "\n").getBytes(ASCII));
			line = reader.readLine();
		}
		out.close();
		reader.close();
		System.out.println("finish");

	}
}
