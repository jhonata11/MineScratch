package br.ufsc.ine.minetest.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.ufsc.ine.utils.Utils;

public class MinetestPacket {
	
	private List<byte[]> header;
	private List<byte[]> body;


	public MinetestPacket() {
		setHeader(new ArrayList<>());
		setBody(new ArrayList<>());
	}
	
	public byte[] converToMessage(){
		List<byte[]> toReturn = new ArrayList<>();
		toReturn.addAll(this.header);
		toReturn.addAll(this.body);
		
		return Utils.listToArray(toReturn);
	}

	public void addToHeader(byte[] byteToAdd) {
		this.header.add(byteToAdd);
	}

	public void appendFirst(byte[] byteToAdd) {
		List<byte[]> aux = new ArrayList<>();
		aux.addAll(Arrays.asList(byteToAdd));
		aux.addAll(this.body);
		this.body = aux;
	}
	
	public void appendLast(byte[] byteToAdd) {
		List<byte[]> aux = new ArrayList<>();
		aux.addAll(this.body);
		aux.addAll(Arrays.asList(byteToAdd));
		this.body = aux;
	}
	
	@Override
	public boolean equals(Object obj) {
	       if (!(obj instanceof MinetestPacket))
	            return false;
	        if (obj == this)
	            return true;

	        MinetestPacket rhs = (MinetestPacket) obj;
	        return this.toString().equals(rhs.toString());
	}
	
	@Override
	public String toString(){
		return Arrays.toString(this.converToMessage());
	}

	/**
	 * @return the header
	 */
	public List<byte[]> getHeader() {
		return header;
	}

	/**
	 * @param header the header to be set
	 */
	public void setHeader(List<byte[]> header) {
		this.header = header;
	}

	/**
	 * @return the body
	 */
	public List<byte[]> getBody() {
		return body;
	}

	/**
	 * @param body the body to be set
	 */
	public void setBody(List<byte[]> body) {
		this.body = body;
	}
}
