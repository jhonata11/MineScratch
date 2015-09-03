package br.ufsc.ine.scratch;

public class Command {
	public String nome;
	public String status;
	public String codigo;
	public String extra;

	public Command(String nome, String status, String codigo, String extra) {
		this.nome = nome;
		this.status = status;
		this.codigo = codigo;
		this.extra = extra;
	}

	public Command() {
		nome = "AINDA NAO DEFINIDO";
	}

	public String toString() {
		return "Nome: " + nome + "; Status: " + status + "; Codigo: " + codigo + "; Extra: " + extra;
	}
}
