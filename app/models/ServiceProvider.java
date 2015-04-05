package models;

public enum ServiceProvider {
	ZAIBALO, GOOGLE_PLUS, AVATARS_IO, INSTAGRAM, FACEBOOK;
	
	static public boolean contains(String aName) {
		ServiceProvider[] providers = ServiceProvider.values();
		for (ServiceProvider provider : providers)
			if (provider.toString().equals(aName))
				return true;
		return false;
	}
}
