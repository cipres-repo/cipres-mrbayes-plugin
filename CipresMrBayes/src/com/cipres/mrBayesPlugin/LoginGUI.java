package com.cipres.mrBayesPlugin;

import com.biomatters.geneious.publicapi.plugin.Options;

public class LoginGUI extends Options {
    private StringOption username;
    private StringOption password;
    private StringOption url;
	private StringOption appName;
    private StringOption appKey;
    

    public LoginGUI() {
        username = addStringOption("username","User Name","Please enter your username");
        password = addStringOption("password","Password","Please enter your password");
        url = addStringOption("url", "REST URL", "Please enter the REST URL");
        appName = addStringOption("appName", "App Name", "Please enter the App Name");
        appKey = addStringOption("appKey", "App Key", "Please eneter the App Key");
    }

    public String getUsername() {
        return username.getValue();
    }

    public String getPassword() {
    	return password.getValue();
    }
    
    public String getUrl() {
		return url.getValue();
	}

	public String getAppName() {
		return appName.getValue();
	}

	public String getAppKey() {
		return appKey.getValue();
	}
}