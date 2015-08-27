package utils;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import br.ufsc.ine.utils.Utils;

public class TestUtils {
	
	@Test
	public void testByteToInt() throws Exception {
		byte[] byteArray = ByteBuffer.allocate(4).putInt(Integer.MAX_VALUE).array();
		int convertedNumber = Utils.byteToInt(byteArray);
		
		assertEquals(Integer.MAX_VALUE, convertedNumber);
	}
	
	@Test
	public void testByteToShort() throws Exception {
		byte[] byteArray = ByteBuffer.allocate(2).putShort(Short.MAX_VALUE).array();
		short convertedNumber = Utils.byteToShort(byteArray);
		
		assertEquals(Short.MAX_VALUE, convertedNumber);
	}
	
	@Test
	public void testConcatenateBytes() throws Exception {
		byte[] arrayOne = "a".getBytes();
		byte[] arrayTwo = "b".getBytes();
		byte[] arrayThree = "c".getBytes();

		assertEquals("[97, 98, 99]", Arrays.toString(Utils.concatenateBytes(arrayOne, arrayTwo, arrayThree)));
	}
	
	@Test
	public void testConvertListToArray() throws Exception {
		byte[] arrayOne = "a".getBytes();
		byte[] arrayTwo = "b".getBytes();
		byte[] arrayThree = "c".getBytes();
		
		List<byte[]> arrayList = new ArrayList<>();
		arrayList.add(arrayOne);
		arrayList.add(arrayTwo);
		arrayList.add(arrayThree);
		
		byte[] received = Utils.listToArray(arrayList);
		assertEquals("[97, 98, 99]", Arrays.toString(received));
	}
	
	@Test
	public void testUnsignedShortToInt() throws Exception {
		int intValue = 65500;
		short shortValue=  (short) 65500;
		int converted = Utils.unsignedShortToInt(shortValue);
		
		assertNotEquals(intValue, shortValue);
		assertEquals(intValue, converted);
	}
	
	@Test
	public void testPrintBinarieInteger() throws Exception {
		String string1 = Utils.integerToBinary(1);
		String string2 = Utils.integerToBinary(2);
		String string3 = Utils.integerToBinary(3);
		
		assertEquals("00000001", string1);
		assertEquals("00000010", string2);
		assertEquals("00000011", string3);
	}
	
	@Test
	public void teste() throws Exception {
		System.out.println(Utils.integerToBinary(-1));
		
	}
}
