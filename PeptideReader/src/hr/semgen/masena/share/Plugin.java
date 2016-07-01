package hr.semgen.masena.share;

import java.io.Serializable;

public class Plugin implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private PluginVersion version;

	public Plugin() {

	}

	public Plugin(PluginVersion pluginVersion, String name) {
		version = pluginVersion;
		this.name = name;
	}

	@Override
	public String toString() {
		return name + "_" + version;
	}

	public void setVersion(PluginVersion version) {
		this.version = version;
	}

	public PluginVersion getVersion() {
		return version;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Plugin other = (Plugin) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	/**
	 * Ima plugina na disku!
	 * 
	 * @return
	 */
	public String createPluginFileName() {
		return getName() + "_" + getVersion() + ".jar";
	}

}
