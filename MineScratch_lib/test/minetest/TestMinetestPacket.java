package minetest;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

import br.ufsc.ine.minetest.network.MinetestPacket;
import br.ufsc.ine.utils.Utils;

public class TestMinetestPacket {
	private MinetestPacket minetestPacket;

	@Before
	public void setUp(){
		this.minetestPacket = new MinetestPacket();
	}
	
	@Test
	public void addToHeader() throws Exception {
		byte[] test = ByteBuffer.allocate(4).putInt(0).array();
		this.minetestPacket.addToHeader(test);
		byte[] packet = this.minetestPacket.converToMessage();
		
		assertEquals(ArrayUtils.toString(test), ArrayUtils.toString(packet));
	}
	
	@Test
	public void addToBodyStart() throws Exception {
		byte[] header = ByteBuffer.allocate(4).putInt(0).array();
		byte[] body = ByteBuffer.allocate(4).putInt(1).array();
		minetestPacket.addToHeader(header);
		minetestPacket.appendFirst(body);
		byte[] packet = this.minetestPacket.converToMessage();
		
		assertEquals(Arrays.toString(Utils.concatenateBytes(header, body)), Arrays.toString(packet));
	}
	
	@Test
	public void addToBodyEnd() throws Exception {
		byte[] header = ByteBuffer.allocate(4).putInt(0).array();
		byte[] bodyBegin = ByteBuffer.allocate(4).putInt(1).array();
		byte[] bodyEnd = ByteBuffer.allocate(4).putInt(2).array();
		
		minetestPacket.addToHeader(header);
		minetestPacket.appendFirst(bodyBegin);
		minetestPacket.appendLast(bodyEnd);
		byte[] packet = this.minetestPacket.converToMessage();
		
		assertEquals(Arrays.toString(Utils.concatenateBytes(header, bodyBegin, bodyEnd)), Arrays.toString(packet));
	}
	
	@Test
	public void testEqualsRight() throws Exception {
		byte[] header = ByteBuffer.allocate(4).putInt(0).array();
		byte[] body = ByteBuffer.allocate(4).putInt(1).array();
		byte[] header2 = ArrayUtils.clone(header);
		byte[] body2 = ArrayUtils.clone(body);
		
		this.minetestPacket.addToHeader(header);
		this.minetestPacket.appendFirst(body);
		
		MinetestPacket testPacket = new MinetestPacket();
		testPacket.addToHeader(header2);
		testPacket.appendFirst(body2);
		
		assertEquals(testPacket, this.minetestPacket);
	}
	
	@Test
	public void testEqualsWrong() throws Exception {
		byte[] header = ByteBuffer.allocate(4).putInt(0).array();
		byte[] body = ByteBuffer.allocate(4).putInt(1).array();
		byte[] header2 = ByteBuffer.allocate(4).putInt(2).array();
		byte[] body2 = ByteBuffer.allocate(4).putInt(3).array();
		
		this.minetestPacket.addToHeader(header);
		this.minetestPacket.appendFirst(body);
		
		MinetestPacket testPacket = new MinetestPacket();
		testPacket.addToHeader(header2);
		testPacket.appendFirst(body2);
		
		assertNotEquals(testPacket, this.minetestPacket);
	}

}
