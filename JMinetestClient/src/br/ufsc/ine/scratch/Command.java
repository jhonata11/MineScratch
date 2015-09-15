package br.ufsc.ine.scratch;

public class Command {
	public String nome;
	private String status;
	public String codigo;
	public String extra;

	public Command(String nome, String status, String codigo, String extra) {
		this.nome = nome;
		this.setStatus(status);
		this.codigo = codigo;
		this.extra = extra;
	}

	public Command() {
		nome = "AINDA NAO DEFINIDO";
	}

	public String toString() {
		return "Nome: " + nome + "; Status: " + getStatus() + "; Codigo: " + codigo + "; Extra: " + extra;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}
