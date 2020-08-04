package com.tourguide.helper;

public class InternalTestHelper {

	private static int internalUserNumber = 1000;

	public static void setInternalUserNumber(int internalUserNumber) {
		InternalTestHelper.internalUserNumber = internalUserNumber;
	}

	public static int getInternalUserNumber() {
		return internalUserNumber;
	}
}
