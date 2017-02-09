package Model;

import android.content.pm.ApplicationInfo;

public class AppInfo {
	
	private ApplicationInfo appInfo;
	private String pkgName;
	private int resultIcon;

	public AppInfo() {
	}

	public ApplicationInfo getAppInfo() {
		return appInfo;
	}

	public void setAppInfo(ApplicationInfo appInfo) {
		this.appInfo = appInfo;
	}

	public String getPkgName() {
		return pkgName;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}

	public int getResultIcon() {
		return resultIcon;
	}

	public void setResultIcon(int resultIcon) {
		this.resultIcon = resultIcon;
	}




}
