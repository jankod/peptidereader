package hr.semgen.masena.share.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtils {

	private static final Logger log = LoggerFactory.getLogger(CommonUtils.class);

	public static boolean isSljevo(String text, String query) {
		if (text.startsWith(query)) {
			return true;
		}

		return false;
	}

	public static boolean isSdesna(String text, String query) {
		int lastIndexOf = text.lastIndexOf(StringUtils.reverse(query));
		// log.debug("index {}", lastIndexOf);
		if (lastIndexOf == -1) {
			return false;
		}
		if (lastIndexOf == (text.length() - query.length())) {
			return true;
		}
		return false;
	}

	public static double roundToDecimals(double num, int dec) {
		return Math.round(num * Math.pow(10, dec)) / Math.pow(10, dec);
	}

	public static float roundToDecimals(float num, int dec) {
		return (float) (Math.round(num * Math.pow(10, dec)) / Math.pow(10, dec));
	}

	public static void main(String[] args) {
		System.out.println(roundToDecimals(2000123.123456789d, 6));
	}

	public static void playSound(URL url) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(url);
			clip.open(ais);
			clip.loop(0);
		} catch (Throwable e) {
			log.error("Error song");
		}
	}

	public static byte[] compress(String xml) {
		byte[] input = xml.getBytes();
		return compress(input);
	}

	public static byte[] compress(byte[] bytes) {
		Deflater compressor = new Deflater(Deflater.BEST_COMPRESSION);
		compressor.setInput(bytes);
		compressor.finish();

		ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length * 2);

		byte[] buf = new byte[1024];
		while (!compressor.finished()) {
			int count = compressor.deflate(buf);
			bos.write(buf, 0, count);
		}
		try {
			bos.close();
		} catch (IOException e) {
			log.error("", e);
		}
		return bos.toByteArray();
	}

	public static String decompress(byte[] compressedData) {
		try {
			Inflater decompressor = new Inflater();
			decompressor.setInput(compressedData);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(compressedData.length);
			byte[] buf = new byte[1024];
			while (!decompressor.finished()) {
				int count;
				count = decompressor.inflate(buf);
				bos.write(buf, 0, count);

			}
			bos.close();
			byte[] decompressedData = bos.toByteArray();
			return new String(decompressedData);
		} catch (DataFormatException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		}
		return null;

	}

	public static void main2(String[] args) {
		String p = "perica perica perica perica";
		final byte[] c = compress(p);
		System.out.println(c.length);
		System.out.println("result: " + decompress(c));
	}

}
