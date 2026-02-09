package br.com.eu.cm;

import br.com.eu.modelo.Tabuleiro;
import br.com.eu.visao.TabuleiroConsole;

public class Aplicacao {
	
	public static void main(String[] args) {
		
		Tabuleiro tabuleiro = new Tabuleiro(8, 8, 3);
		new TabuleiroConsole(tabuleiro);
	}

}
