package br.com.eu.visao;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.eu.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel {

    public PainelTabuleiro(Tabuleiro tabuleiro, PainelTemporizador timer) {

        setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));

        timer.setQuandoTempoAcabar(() -> {
            JOptionPane.showMessageDialog(this, "O tempo acabou! Você perdeu.");
            tabuleiro.reiniciar();
            timer.reiniciar();
        });

        tabuleiro.paraCadaCampo(c -> {
            BotaoCampo botao = new BotaoCampo(c);
            
            botao.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    timer.iniciar(); 
                }
            });
            
            add(botao);
        });

        tabuleiro.registrarObservador(e -> {
            SwingUtilities.invokeLater(() -> {
                
                timer.parar();
                
                if(e.isGanhou()) {
                    JOptionPane.showMessageDialog(this, "Parabéns! Tempo restante: " + timer.getText());
                } else {
                    JOptionPane.showMessageDialog(this, "Explodiu! Tente novamente.");				
                }
                
                tabuleiro.reiniciar();
                timer.reiniciar();
            });
        });
    }
}