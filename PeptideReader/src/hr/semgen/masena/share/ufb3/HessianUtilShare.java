package hr.semgen.masena.share.ufb3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.hessian.io.Deflation;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

public class HessianUtilShare {
	private static final Logger log = LoggerFactory
			.getLogger(HessianUtilShare.class);

	public static Object deserialize(String dataPath) throws IOException {
		FileInputStream in = new FileInputStream(dataPath);
		try {
			return deserialize(in);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	public static Object deserialize(InputStream stream) throws IOException {
		Hessian2Input in = new Hessian2Input(stream);
		in.getSerializerFactory().setAllowNonSerializable(true);
		in.startMessage();
		in.setCloseStreamOnClose(true);

		final Object obj = in.readObject();

		in.completeMessage();

		in.close();

		return obj;
	}

	public static Object deserialize(String path, boolean useCompress)
			throws IOException {
		final FileInputStream input = new FileInputStream(path);

		ByteArrayInputStream bin = new ByteArrayInputStream(
				IOUtils.toByteArray(input));
		input.close();
		Hessian2Input in = new Hessian2Input(bin);
		in.getSerializerFactory().setAllowNonSerializable(true);

		if (useCompress) {
			Deflation envelope = new Deflation();
			in = envelope.unwrap(in);
		}

		in.startMessage();

		final Object obj = in.readObject();

		in.completeMessage();

		in.close();
		bin.close();

		return obj;
	}

	public static void serialize(Object obj, OutputStream stream,
			boolean useCompress) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		Hessian2Output out = new Hessian2Output(bos);
		out.getSerializerFactory().setAllowNonSerializable(true);
		out.setCloseStreamOnClose(true);
		if (useCompress) {
			Deflation envelope = new Deflation();
			out = envelope.wrap(out);
		}

		out.startMessage();

		out.writeObject(obj);
		out.completeMessage();
		out.close();

		stream.write(bos.toByteArray());

	}

	public static void serialize(Object o, String path) throws IOException {
		serialize(path, o, false);
	}

	public static void serialize(String path, Object obj, boolean useCompress)
			throws IOException {
		// ByteArrayOutputStream bos = new ByteArrayOutputStream();
		FileOutputStream outf = new FileOutputStream(path);
		Hessian2Output out = new Hessian2Output(outf);
		out.getSerializerFactory().setAllowNonSerializable(true);
		out.setCloseStreamOnClose(true);
		if (useCompress) {
			Deflation envelope = new Deflation();
			out = envelope.wrap(out);
		}

		out.startMessage();

		out.writeObject(obj);
		out.completeMessage();
		out.flush();
		out.close();
		outf.close();

	}

	public static Object fromText(String stringObj) throws IOException {
		final byte[] b = DatatypeConverter.parseBase64Binary(stringObj);
		ByteArrayInputStream bin = new ByteArrayInputStream(b);
		Hessian2Input in = new Hessian2Input(bin);
		in.getSerializerFactory().setAllowNonSerializable(true);

		in.startMessage();

		final Object obj = in.readObject();

		in.completeMessage();

		in.close();
		bin.close();

		return obj;
	}

	public static String toText(Object o) throws IOException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		Hessian2Output out = new Hessian2Output(bos);
		out.getSerializerFactory().setAllowNonSerializable(true);

		out.startMessage();

		out.writeObject(o);
		out.completeMessage();
		out.close();
		return DatatypeConverter.printBase64Binary(bos.toByteArray());
		// return new String(bos.toByteArray(), "UTF-8");

	}

}
