package com.example.firebaseauthenticationtwitter.Model;

public class DataItem{
	private float percentile;
	private String name;
	private Object rawScore;
	private String traitId;
	private boolean significant;
	private String category;

	public void setPercentile(float percentile){
		this.percentile = percentile;
	}

	public float getPercentile(){
		return percentile;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setRawScore(Object rawScore){
		this.rawScore = rawScore;
	}

	public Object getRawScore(){
		return rawScore;
	}

	public void setTraitId(String traitId){
		this.traitId = traitId;
	}

	public String getTraitId(){
		return traitId;
	}

	public void setSignificant(boolean significant){
		this.significant = significant;
	}

	public boolean isSignificant(){
		return significant;
	}

	public void setCategory(String category){
		this.category = category;
	}

	public String getCategory(){
		return category;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"percentile = '" + percentile + '\'' + 
			",name = '" + name + '\'' + 
			",rawScore = '" + rawScore + '\'' + 
			",traitId = '" + traitId + '\'' + 
			",significant = '" + significant + '\'' + 
			",category = '" + category + '\'' + 
			"}";
		}
}
