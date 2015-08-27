package br.ufsc.ine.utils;

import java.nio.ByteBuffer;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class Utils {

	public static int byteToInt(byte[] arr) {
		return ByteBuffer.wrap(arr, 0, 4).getInt();
	}

	public static short byteToShort(byte[] arr) {
		return ByteBuffer.wrap(arr, 0, 2).getShort();
	}

	public static byte[] concatenateBytes(byte[]... arrays) {
		byte[] toReturn = null;
		for (byte[] a : arrays) {
			toReturn = ArrayUtils.addAll(toReturn, a);
		}
		return toReturn;
	}

	public static byte[] listToArray(List<byte[]> arrayList) {
		byte[] toReturn = new byte[0];
		
		for (byte[] a: arrayList){
			toReturn = ArrayUtils.addAll(toReturn, a);
		}
		return toReturn;
	}
	
	public static int unsignedShortToInt(short shortValue){
		return shortValue >= 0 ? shortValue : 0x10000 + shortValue; 
	}

	public static String integerToBinary(int i) {
		return Integer.toBinaryString(i & 255 | 256).substring(1);
	}
}
