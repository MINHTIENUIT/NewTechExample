package com.example.firebaseauthenticationtwitter.Model;

import com.github.mikephil.charting.data.RadarEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
	private List<DataItem> dataItems;
	private TwitterUser twitter;

	public Data(){
		dataItems = new ArrayList<>();
		twitter = TwitterUser.NONE_USER;
	}

	public Data(TwitterUser twitter, List<DataItem> dataItems) {
		this.twitter = twitter;
		this.dataItems = dataItems;
	}

	public void setDataItems(List<DataItem> dataItems){
		this.dataItems = dataItems;
	}

	public List<DataItem> getDataItems(){
		return dataItems;
	}

	public TwitterUser getTwitter() {
		return twitter;
	}

	public void setTwitter(TwitterUser twitter) {
		this.twitter = twitter;
	}

	public Map<String,RadarEntry> getDataNeedChart(){
		Map<String,RadarEntry> dataChart = new HashMap<>();
		for (DataItem data : dataItems){
			dataChart.put(data.getName(),new RadarEntry(Math.round((data.getPercentile()*100))));
		}
		return dataChart;
	}

	@Override
 	public String toString(){
		return 
			"Data{" +
			"dataItems = '" + dataItems + '\'' +
			",twitterID = '" + twitter + '\'' +
			"}";
		}
}