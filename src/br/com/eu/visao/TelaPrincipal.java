package br.com.eu.visao;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import br.com.eu.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class TelaPrincipal extends JFrame {

	public TelaPrincipal() {
		setLayout(new BorderLayout());
		Tabuleiro tabuleiro = new Tabuleiro(16, 30, 4);
		
		PainelTemporizador temporizador = new PainelTemporizador(300);
		
		JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.CENTER));
		painelTopo.add(temporizador);
		add(painelTopo, BorderLayout.NORTH);
		
		PainelTabuleiro painelTabuleiro = new PainelTabuleiro(tabuleiro, temporizador);
		add(painelTabuleiro, BorderLayout.CENTER);
		
		setTitle("Campo Minado");
		setSize(690, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new TelaPrincipal();
	}
}