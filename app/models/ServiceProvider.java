package models;

public enum ServiceProvider {
	GOOGLE_PLUS, AVATARS_IO;
	
	static public boolean contains(String aName) {
		ServiceProvider[] providers = ServiceProvider.values();
		for (ServiceProvider provider : providers)
			if (provider.toString().equals(aName))
				return true;
		return false;
	}
}
