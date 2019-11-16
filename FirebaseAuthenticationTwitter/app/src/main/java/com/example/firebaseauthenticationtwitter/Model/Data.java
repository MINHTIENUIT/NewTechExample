package com.example.firebaseauthenticationtwitter.Model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.data.RadarEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
	private List<DataItem> dataItems = new ArrayList<>();
	private String twitterID = "";


	public void setDataItems(List<DataItem> dataItems){
		this.dataItems = dataItems;
	}

	public List<DataItem> getDataItems(){
		return dataItems;
	}

	public void setTwitterID(String twitterID){
		this.twitterID = twitterID;
	}

	public String getTwitterID(){
		return twitterID;
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
			",twitterID = '" + twitterID + '\'' + 
			"}";
		}
}