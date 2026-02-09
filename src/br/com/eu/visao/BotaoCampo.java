package br.com.eu.visao;

import java.awt.Color;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import br.com.eu.modelo.Campo;
import br.com.eu.modelo.CampoEvento;
import br.com.eu.modelo.CampoObservador;

@SuppressWarnings("serial")
public class BotaoCampo extends JButton implements CampoObservador, MouseListener {

    // Cores padrão
    private final Color BG_PADRAO = new Color(184, 184, 184);
    private final Color BG_MARCAR = new Color(8, 179, 247);
    private final Color TEXTO_VERDE = new Color(0, 100, 0);

    private static ImageIcon iconeBandeira;
    private static ImageIcon iconeBomba;

    private Campo campo;

    public BotaoCampo(Campo campo) {
        this.campo = campo;

        // Estilo inicial
        setBackground(BG_PADRAO);
        setOpaque(true);
        setBorder(BorderFactory.createBevelBorder(0));

        addMouseListener(this);
        campo.registrarObservador(this);

        if (iconeBandeira == null) {
        	iconeBandeira = carregarIcone("/imagens/bandeira.png");
        }
        
        if (iconeBomba == null) {
        	iconeBomba = carregarIcone("/imagens/bomba.png");
        }
    }

    private ImageIcon carregarIcone(String caminho) {
        try {
            var url = getClass().getResource(caminho);
            if (url != null) {
                return new ImageIcon(url);
            } else {
                System.err.println("Imagem não encontrada: " + caminho);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
	@Override
	public void ocorreuEvento(Campo c, CampoEvento evento) {
		switch (evento) {
		case ABRIR:
			aplicarEstiloAbrir();
			break;
		case MARCAR:
			aplicarEstiloMarcar();
			break;
		case EXPLODIR:
			aplicarEstiloExplodir();
			break;
		default:
			aplicarEstiloPadrao();
		}
		
		SwingUtilities.invokeLater(() -> {
			repaint();
			validate();
		});
	}

	private void aplicarEstiloPadrao() {
		setBorder(BorderFactory.createBevelBorder(0));
		setBackground(BG_PADRAO);
		setText("");
		setIcon(null);
	}

	private void aplicarEstiloExplodir() {
		setBackground(new Color(184, 184, 184));
		setForeground(Color.WHITE);
		setText("");
		setIcon(iconeBomba);
	}

	private void aplicarEstiloMarcar() {
		setBackground(BG_MARCAR);
		setForeground(Color.BLACK);
		setText("");
		setIcon(iconeBandeira);
	}

	private void aplicarEstiloAbrir() {
		setBorder(BorderFactory.createLineBorder(Color.GRAY));

		if (campo.isMinado()) {
			setBackground(new Color(184, 184, 184));
			setIcon(iconeBomba);
			return;
		}

		setBackground(BG_PADRAO);
		setIcon(null); 

		switch (campo.minasNaVizinhanca()) {
		case 1:
			setForeground(TEXTO_VERDE);
			break;
		case 2:
			setForeground(Color.BLUE);
			break;
		case 3:
			setForeground(Color.YELLOW);
			break;
		case 4:
		case 5:
		case 6:
			setForeground(Color.RED);
			break;
		default:
			setForeground(Color.PINK);
			break;
		}

		String valor = !campo.vizinhancaSegura()
				? campo.minasNaVizinhanca() + ""
				: "";
		setText(valor);
	}

	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {
		if (e.getButton() == 1) {
			campo.abrir();
		} else {
			campo.alternarMarcacao();
		}
	}
	
	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {}

	@Override
	public void mouseEntered(java.awt.event.MouseEvent e) {}

	@Override
	public void mouseExited(java.awt.event.MouseEvent e) {}
}