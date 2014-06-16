package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

import java.io.Serializable;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Conflict implements Parcelable{

	private List<House> houses;
	private List<Integer> diceValues;
	private Territory territory;

	public Conflict() {}
	
	public List<Integer> getDiceValues() {
		return diceValues;
	}

	public void setDiceValues(List<Integer> diceValues) {
		this.diceValues = diceValues;
	}

	public List<House> getHouses() {
		return houses;
	}

	public void setHouses(List<House> houses) {
		this.houses = houses;
	}

	public Territory getTerritory() {
		return territory;
	}

	public void setTerritory(Territory territory) {
		this.territory = territory;
	}
	
	// Don't even ask about this below... :)
	public Conflict(Parcel in) {  
	     readFromParcel(in);  
	} 

	@SuppressWarnings("unchecked")
	private void readFromParcel(Parcel in) {
		 houses = (List<House>) in.readSerializable();  
		 diceValues = (List<Integer>) in.readSerializable();  
		 territory = (Territory) in.readSerializable();  
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		
		out.writeSerializable((Serializable) houses);  
        out.writeSerializable((Serializable) diceValues);  
        out.writeSerializable(territory); 
	}
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
	    public Conflict createFromParcel(Parcel in) {
	        return new Conflict(in);
	    }

	    public Conflict[] newArray(int size) {
	        return new Conflict[size];
	    }
	};
}









