package com.ncu.sdroidagent;

import java.util.List;

import Model.AppInfo;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * App list²M³æ
 * */
public class ApkAdapter extends BaseAdapter {

	List<AppInfo> packageList;
	Activity context;
	PackageManager packageManager;

	public ApkAdapter(Activity context, List<AppInfo> packageList,
			PackageManager packageManager) {
		super();
		this.context = context;
		this.packageList = packageList;
		this.packageManager = packageManager;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return packageList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return packageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = null;
		ViewHolder holder = null;
		LayoutInflater infater = context.getLayoutInflater();

		if (convertView == null || convertView.getTag() == null) {
			view = infater.inflate(R.layout.listview_layout, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) convertView.getTag();
		}
		AppInfo appInfo = (AppInfo) getItem(position);
		holder.appIcon.setImageDrawable(packageManager.getApplicationIcon(appInfo.getAppInfo()));
		holder.appLabel.setText(packageManager.getApplicationLabel(appInfo.getAppInfo()));
		holder.pkgName.setText(appInfo.getPkgName());
		holder.listResult.setImageResource(appInfo.getResultIcon());

		return view;
	}

	class ViewHolder {
		ImageView appIcon;
		TextView appLabel;
		TextView pkgName;
		ImageView listResult;

		public ViewHolder(View view) {
			this.appIcon = (ImageView) view.findViewById(R.id.listIcon);
			this.appLabel = (TextView) view.findViewById(R.id.listName);
			this.pkgName = (TextView) view.findViewById(R.id.listContent);
			this.listResult = (ImageView) view.findViewById(R.id.listResult);
		}
	}

}
