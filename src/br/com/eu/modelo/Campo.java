package br.com.eu.modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {

	// Posição fixa do campo no tabuleiro
	private final int linha;
	private final int coluna;

	// Estados do campo
	private boolean minado = false;
	private boolean aberto = false;
	private boolean marcado = false;

	// Lista de campos vizinhos
	private List<Campo> vizinhos = new ArrayList<>();
	
	// Lista de observadores 
	private List<CampoObservador> observadores = new ArrayList<>();

	// Construtor recebe a posição do campo
	Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}
	
	// Registra um observador para ser notificado quando algo acontecer
	public void registrarObservador(CampoObservador observador) {
		observadores.add(observador);
	}
	
	// Notifica todos os observadores sobre um evento ocorrido neste campo
	private void notificarObservadores(CampoEvento evento) {
		observadores.stream()
			.forEach(o -> o.ocorreuEvento(this, evento));
	}

	// Tenta adicionar um campo como vizinho
	boolean adicionarVizinho(Campo possivelVizinho) {

		// Verifica se linha e coluna são diferentes
		boolean linhaDiferente = linha != possivelVizinho.linha;
		boolean colunaDiferente = coluna != possivelVizinho.coluna;

		// Se ambos forem diferentes, é diagonal
		boolean diagonal = linhaDiferente && colunaDiferente;

		// Calcula a distância entre os campos
		int deltaLinha = Math.abs(linha - possivelVizinho.linha);
		int deltaColuna = Math.abs(coluna - possivelVizinho.coluna);
		int deltaGeral = deltaColuna + deltaLinha;

		// Vizinho horizontal ou vertical
		if (deltaGeral == 1 && !diagonal) {
			vizinhos.add(possivelVizinho);
			return true;

		// Vizinho diagonal
		} else if (deltaGeral == 2 && diagonal) {
			vizinhos.add(possivelVizinho);
			return true;

		} else {
			return false;
		}
	}

	// Alterna a marcação do campo (bandeira)
	public void alternarMarcacao() {
		if (!aberto) {
			marcado = !marcado;
			
			// Notifica o evento correto
			if (marcado) {
				notificarObservadores(CampoEvento.MARCAR);
			} else {
				notificarObservadores(CampoEvento.DESMARCAR);
			}
		}
	}

	// Abre o campo
	public boolean abrir() {

		// Só abre se não estiver aberto nem marcado
		if (!aberto && !marcado) {
			aberto = true;

			// Se for minado, explode
			if (minado) {
				notificarObservadores(CampoEvento.EXPLODIR);
				return true;
			}

			// Marca como aberto e notifica
			setAberto(true);
			
			// Se não houver minas ao redor, abre os vizinhos
			if (vizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrir());
			}

			return true;
		} else {
			return false;
		}
	}

	// Verifica se nenhum vizinho está minado
	public boolean vizinhancaSegura() {
		return vizinhos.stream().noneMatch(v -> v.minado);
	}
	
	// Coloca uma mina neste campo
	void minar() {
		minado = true;
	}
	
	public boolean isMinado() {
		return minado;
	}
	
	public boolean isMarcado() {
		return marcado;
	}
	
	// Define o campo como aberto e notifica
	void setAberto(boolean aberto) {
		this.aberto = aberto;
		
		if (aberto) {
			notificarObservadores(CampoEvento.ABRIR);
		}
	}

	public boolean isAberto() {
		return aberto;
	}
	
	public boolean isFechado() {
		return !isAberto();
	}

	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}
	
	// Verifica se o objetivo deste campo foi alcançado
	// Ou ele foi aberto sem mina
	// Ou foi corretamente marcado com mina
	boolean objetivoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		return desvendado || protegido;
	}
	
	// Conta quantas minas existem na vizinhança
	public int minasNaVizinhanca() {
		return (int) vizinhos.stream()
				.filter(v -> v.minado)
				.count();
	}
	
	// Reinicia o campo para um novo jogo
	void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
		notificarObservadores(CampoEvento.REINICIAR);
	}
}
