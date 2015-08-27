package minetest;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

import br.ufsc.ine.minetest.MinetestProtocol;
import br.ufsc.ine.minetest.Sender;
import br.ufsc.ine.utils.Utils;

public class TestMinetestProtocol {
	
	private MinetestProtocol minetestProtocol;

	@Before
	public void setUp() throws InterruptedException{
		minetestProtocol = new MinetestProtocol(new Sender("192.168.0.14", 3000));
	}
	
	@Test
	public void testCreateHeader() throws Exception {
		byte[] headerToSend = minetestProtocol.createHeader();
		int protocoID = Utils.byteToInt(ArrayUtils.subarray(headerToSend, 0, 4));
		short headerPeerID = Utils.byteToShort(ArrayUtils.subarray(headerToSend, 4, 6));
		byte headerChannelID = ArrayUtils.subarray(headerToSend, 6, 7)[0];

		
		System.out.println(headerToSend.length);
		
		assertEquals(0x4F457403, protocoID);
		assertEquals(0x00, headerPeerID);
		assertEquals(0x00, headerChannelID);
		assertEquals(7, headerToSend.length);
	}
	
	
	@Test
	public void testCreateHandshake() throws Exception {
		byte[] packet = minetestProtocol.createHandshakePacket("jhonata11", "senhaQualquer");
		short toServerInit = Utils.byteToShort(ArrayUtils.subarray(packet, 0, 2));
		byte serFmtVerHighestRead = ArrayUtils.subarray(packet, 2, 3)[0];
		String username = new String(ArrayUtils.subarray(packet, 3, 23), "UTF-8");
		String password = new String(ArrayUtils.subarray(packet, 23, 51), "UTF-8");
		short minSupportedProtocol = Utils.byteToShort(ArrayUtils.subarray(packet, 51, 53));
		short maxSupportedProtocol = Utils.byteToShort(ArrayUtils.subarray(packet, 53, 55));

		assertEquals(0x10, toServerInit);
		assertEquals(0x1A, serFmtVerHighestRead);
		assertEquals("jhonata11", username.trim());
		assertEquals("senhaQualquer", password.trim());
		assertEquals(0x0d, minSupportedProtocol);
		assertEquals(0x16, maxSupportedProtocol);
		assertEquals(0x16, maxSupportedProtocol);
		assertEquals(55, packet.length);
		
	}
	
	@Test
	public void testCreateCommandByte() throws Exception {
		byte[] packet = minetestProtocol.createCommandByte();
		assertEquals(0x01, packet[0]);
		assertEquals(1, packet.length);
	}
	
	@Test
	public void testCreateReliableBytes() throws Exception {
		int sequentialNumber = 65500;
		byte[] packet = minetestProtocol.createReliableBytes(sequentialNumber);
		byte reliable = ArrayUtils.subarray(packet, 0, 1)[0];
		short receivedSeqNumber = Utils.byteToShort(ArrayUtils.subarray(packet, 1, 3));

		assertEquals(reliable, 0x03);
		assertEquals(sequentialNumber, Utils.unsignedShortToInt(receivedSeqNumber));
		assertEquals(3, packet.length);
	}

}
