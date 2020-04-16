package com.belatrix.enums;

/**

*
* @author <a href="ing_stiven@hotmail.com">Stiven Jose Mosquera Puello</a>

*
*/
public enum Environment {

	QA("https://www.ebay.com/"),
			
	CUSTOM(null);//

	private String ebayUrl;
	

	private Environment(String ebayUrl) {//
		this.ebayUrl = ebayUrl;
	}

	public static Environment createEnvironment(String ebayUrl) {//

		Environment environment = Environment.CUSTOM;
		environment.ebayUrl = ebayUrl;
		
        return environment;
	}

	/**
	 * @return the cartPostUrl
	 */
	public String getEbayUrl() {
		return ebayUrl;
	}

	/**
	 * @param url
	 * @return
	 */
	public static Environment fromString(String url) {
		if (url != null) {
			for (Environment enviroment : Environment.values()) {
				if (enviroment.getEbayUrl().contains(url))
				return enviroment;
			}
		}
		return null;
	}

}
