package com.sevya.onemoney.utility;

public class Utility {
	
	private Utility() {}
	
	public static Purpose getPurposeEnum(String purpose){
		switch(purpose){
			case OneMoneyConstants.BUSINESS :
				return Purpose.B;
			case OneMoneyConstants.PERSONAL :
				return Purpose.P;
			default : return null;
		}
	}
	
	public static String getPurposeString(Purpose purpose){
		switch(purpose.toString()){
			case OneMoneyConstants.BUSINESS :
				return OneMoneyConstants.BUSINESS;
			case OneMoneyConstants.PERSONAL :
				return OneMoneyConstants.PERSONAL;
			default : return null;
		}
	}

}
