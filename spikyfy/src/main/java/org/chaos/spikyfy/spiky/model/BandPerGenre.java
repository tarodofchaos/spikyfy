package org.chaos.spikyfy.spiky.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="spikyfy_band_per_genre")
public class BandPerGenre {

	private Integer id;
	private String bandCountry;
	private String bandGenre;
	private String bandName;
	
	@DynamoDBHashKey(attributeName="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@DynamoDBAttribute(attributeName="band_country")
	public String getBandCountry() {
		return bandCountry;
	}
	public void setBandCountry(String bandCountry) {
		this.bandCountry = bandCountry;
	}
	
	@DynamoDBAttribute(attributeName="band_genre")
	public String getBandGenre() {
		return bandGenre;
	}
	public void setBandGenre(String bandGenre) {
		this.bandGenre = bandGenre;
	}
	
	@DynamoDBAttribute(attributeName="band_name")
	public String getBandName() {
		return bandName;
	}
	public void setBandName(String bandName) {
		this.bandName = bandName;
	}
	
	
}
