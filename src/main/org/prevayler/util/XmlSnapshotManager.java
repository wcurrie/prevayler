package org.prevayler.util;

import java.io.*;

import javax.xml.transform.stream.*;

import org.prevayler.implementation.SnapshotManager;

import com.skaringa.javaxml.*;

/**
 * @author Alexandre
 *
 * Writes and reads snapshots to/from XML files.
 * @see org.prevayler.implementation.SnapshotManager
 */
public class XmlSnapshotManager extends SnapshotManager {

	private final ObjectTransformer trans;
	
	public XmlSnapshotManager() throws IOException {
		this("PrevalenceBase");
	}

	public XmlSnapshotManager(String snapshotDirectoryName) throws IOException {
		super(snapshotDirectoryName);
		try {
			this.trans = ObjectTransformerFactory.getInstance().getImplementation();
//			trans.setProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
//			trans.setProperty(javax.xml.transform.OutputKeys.ENCODING, "ISO-8859-1");
//			trans.setProperty(com.skaringa.javaxml.PropertyKeys.OMIT_XSI_TYPE, "true");
		}
		catch (NoImplementationException nie) {
			throw new IOException("Unable to start Skaringa: " + nie.getMessage());
		}
	}


	/**
	 * @see org.prevayler.implementation.SnapshotManager#readSnapshot(File)
	 */
	protected Object readSnapshot(InputStream in) throws IOException {
		Object system = null;
		StreamSource source = new StreamSource(in);
		try {
			system = this.trans.deserialize(source);
		}
		catch (DeserializerException se) {
			throw new IOException("Unable to deserialize with Skaringa: " + se.getMessage());
		}
		finally {
			source.getInputStream().close();
		}
		return system;
	}

	/**
	 * @see org.prevayler.implementation.SnapshotManager#suffix()
	 */
	protected String suffix() {
		return "xml";
	}

	/**
	 * @see org.prevayler.implementation.SnapshotManager#writeSnapshot(Object, File)
	 */
	protected void writeSnapshot(Object prevalentSystem, OutputStream out) throws IOException {
		StreamResult result = new StreamResult(out);
		try {
			this.trans.serialize(prevalentSystem, result);
		}
		catch (SerializerException se) {
			throw new IOException("Unable to serialize with Skaringa: " + se.getMessage());
		}
		finally {
			result.getOutputStream().close();
		}
	}
}
