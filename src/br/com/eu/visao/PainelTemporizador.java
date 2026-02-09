package br.com.eu.visao;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class PainelTemporizador extends JLabel {

    private Timer timer;
    private int tempoInicial;
    private int tempoRestante;
    private Runnable quandoTempoAcabar;

    @SuppressWarnings("unused")
	public PainelTemporizador(int tempoEmSegundos) {
        this.tempoInicial = tempoEmSegundos;
        this.tempoRestante = tempoEmSegundos;
        
        setFont(new Font("Arial", Font.BOLD, 20));
        setBackground(Color.LIGHT_GRAY);
        setOpaque(true);
        setHorizontalAlignment(JLabel.CENTER);
        atualizarTexto();
        
        timer = new Timer(1000, e -> {
            tempoRestante--;
            atualizarTexto();
            
            if (tempoRestante <= 0) {
                timer.stop();
                if (quandoTempoAcabar != null) {
                    quandoTempoAcabar.run(); 
                }
            }
        });
    }

    public void setQuandoTempoAcabar(Runnable acao) {
        this.quandoTempoAcabar = acao;
    }

    public void iniciar() {
        if (!timer.isRunning() && tempoRestante > 0) {
            timer.start();
        }
    }

    public void parar() {
        timer.stop();
    }

    public void reiniciar() {
        timer.stop();
        tempoRestante = tempoInicial;
        atualizarTexto();
    }
    
    private void atualizarTexto() {
        long min = tempoRestante / 60;
        long seg = tempoRestante % 60;
        setText(String.format("%02d:%02d", min, seg));
        
        if (tempoRestante < 10) setForeground(Color.RED);
        else setForeground(Color.BLACK);
    }
}