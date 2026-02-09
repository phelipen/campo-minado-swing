package br.com.eu.modelo;

@FunctionalInterface
public interface CampoObservador {
	
	public void ocorreuEvento(Campo c, CampoEvento evento);

}
