package br.ufsc.ine.minetest.commands;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.minetest.Command;
import br.ufsc.ine.minetest.Messages;
import br.ufsc.ine.minetest.Parameter;
import br.ufsc.ine.minetest.network.MinetestPacket;

public class SendChat extends Command{
	

	public SendChat(AbstractMinetest minetest) {
		super(minetest);
	}
	
	@Override
	public void execute(Parameter param) throws Exception {
		String  message = (String) param.getParameter();
		byte[] encodedMessage = Charset.forName("UTF-16BE").encode(message ).array();
		byte[] messageLength = ByteBuffer.allocate(2).putShort((short) message.getBytes().length).array();
		MinetestPacket packet = createPacket(Messages.TOSERVER_CHAT_MESSAGE);
		packet.appendLast(messageLength);
		packet.appendLast(encodedMessage);

		String str = new String(encodedMessage, StandardCharsets.UTF_16BE);
		System.err.println("mensagem enviada: " + str);

		minetest.getSender().sendCommand(packet);
	}

}
