package com.property.dealwithme.enums;

public enum PropertyTypeEnum {

	SELL, RENTAL, HOUSE, APARTMENT, VILLA;

	public static PropertyTypeEnum formString(String type)
	{
		switch (type.toUpperCase()){
		case "HOUSE": {
			return HOUSE;
		}
		case "RENTAL":{
			return RENTAL;
		}
		case "APARTMENT":{
			return APARTMENT;
		}
		case "VILLA":{
			return VILLA;
		}
		
		default:
			throw new IllegalArgumentException("Unexpected value: " + type.toUpperCase());
		}
	}

}
