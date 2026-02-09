package br.com.eu.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador {

	// Dimensões do tabuleiro e quantidade total de minas
	private final int linhas;
	private final int colunas;
	private final int minas;

	// Todos os campos do tabuleiro
	private final List<Campo> campos = new ArrayList<Campo>();

	// Observadores do resultado do jogo (vitória ou derrota)
	private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();

	// Construtor: cria o tabuleiro completo
	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;

		// Etapas de inicialização do jogo
		gerarCampos();
		associarVizinhos();
		sortearMinas();
	}
	
	// Executa uma função para cada campo
	public void paraCadaCampo(Consumer<Campo> funcao) {
		campos.forEach(funcao);
	}

	// Registra um observador para receber o resultado do jogo
	public void registrarObservador(Consumer<ResultadoEvento> observador) {
		observadores.add(observador);
	}

	// Notifica todos os observadores com o resultado final
	private void notificarObservadores(boolean resultado) {
		observadores.stream()
			.forEach(o -> o.accept(new ResultadoEvento(resultado)));
	}

	// Abre o campo correspondente à posição informada
	public void abrir(int linha, int coluna) {
		campos.parallelStream()
			.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
			.findFirst()
			.ifPresent(c -> c.abrir());
	}

	// Alterna a marcação (bandeira) de um campo específico
	public void alternarMarcar(int linha, int coluna) {
		campos.parallelStream()
			.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
			.findFirst()
			.ifPresent(c -> c.alternarMarcacao());
	}

	// Cria todos os campos do tabuleiro
	private void gerarCampos() {
		for (int linha = 0; linha < linhas; linha++) {
			for (int coluna = 0; coluna < colunas; coluna++) {

				// Cria o campo com sua posição
				Campo campo = new Campo(linha, coluna);

				// Tabuleiro observa todos os campos
				campo.registrarObservador(this);

				campos.add(campo);
			}
		}
	}

	// Associa todos os campos aos seus vizinhos
	private void associarVizinhos() {
		for (Campo c1 : campos) {
			for (Campo c2 : campos) {
				c1.adicionarVizinho(c2);
			}
		}
	}

	// Sorteia aleatoriamente as minas no tabuleiro
	private void sortearMinas() {
		long minasArmadas = 0;

		// Predicado para verificar se um campo está minado
		Predicate<Campo> minado = c -> c.isMinado();

		// Continua sorteando até atingir a quantidade desejada
		do {
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minar();
			minasArmadas = campos.stream().filter(minado).count();
		} while (minasArmadas < minas);
	}

	// Verifica se todos os campos atingiram seu objetivo
	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}

	// Reinicia o tabuleiro para um novo jogo
	public void reiniciar() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
	}

	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}

	// Método do Observer: reage aos eventos dos campos
	@Override
	public void ocorreuEvento(Campo c, CampoEvento evento) {

		// Se um campo explodir, o jogo acaba
		if (evento == CampoEvento.EXPLODIR) {
			mostrarMinas();
			notificarObservadores(false);

		// Se todos os objetivos foram alcançados, o jogador vence
		} else if (objetivoAlcancado()) {
			notificarObservadores(true);
		}
	}

	// Abre todas as minas não marcadas (fim de jogo)
	public void mostrarMinas() {
		campos.stream()
			.filter(c -> c.isMinado())
			.filter(c -> !c.isMarcado())
			.forEach(c -> c.setAberto(true));
	}
}
