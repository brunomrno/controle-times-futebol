package br.com.codenation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import br.com.codenation.desafio.annotation.Desafio;
import br.com.codenation.desafio.app.MeuTimeInterface;
import br.com.codenation.desafio.exceptions.CapitaoNaoInformadoException;
import br.com.codenation.desafio.exceptions.IdentificadorUtilizadoException;
import br.com.codenation.desafio.exceptions.JogadorNaoEncontradoException;
import br.com.codenation.desafio.exceptions.TimeNaoEncontradoException;

public class DesafioMeuTimeApplication implements MeuTimeInterface {

	// Map das equipes e dos jogadores
	private Map<Long, Equipe> mapDasEquipes = new HashMap<>();
	private Map<Long, Jogador> mapDosJogadores = new HashMap<>();

	@Desafio("incluirTime")
	public void incluirTime(Long id, String nome, LocalDate dataCriacao, String corUniformePrincipal, String corUniformeSecundario) {

		//Verificando se a equipe ja está cadastrada
		verificaPrimeiraInsercao(mapDasEquipes, id);

		Equipe equipe = new Equipe(id, nome, dataCriacao, corUniformePrincipal, corUniformeSecundario);
		mapDasEquipes.put(id, equipe);
	}

	@Desafio("incluirJogador")
	public void incluirJogador(Long id, Long idTime, String nome, LocalDate dataNascimento, Integer nivelHabilidade, BigDecimal salario) {

		//Verificando se o jogador já está cadastrado e se existe equipe
		verificaPrimeiraInsercao(mapDosJogadores, id);
		verificaExistenciaEquipe(idTime);

		Jogador jogador = new Jogador(id, idTime, nome, dataNascimento, nivelHabilidade, salario);
		mapDosJogadores.put(id, jogador);
	}

	@Desafio("definirCapitao")
	public void definirCapitao(Long idJogador) {
		verificaExistenciaJogador(idJogador);
		mapDasEquipes.get(mapDosJogadores.get(idJogador).getIdTime()).setCapitao(idJogador);
	}

	@Desafio("buscarCapitaoDoTime")
	public Long buscarCapitaoDoTime(Long idTime) {

		verificaExistenciaEquipe(idTime);

		if (mapDasEquipes.get(idTime).getCapitao() == null) {
			throw new CapitaoNaoInformadoException();
		}

		return mapDasEquipes.get(idTime).getCapitao();
	}

	@Desafio("buscarNomeJogador")
	public String buscarNomeJogador(Long idJogador) {
		verificaExistenciaJogador(idJogador);
		return mapDosJogadores.get(idJogador).getNome();
	}

	@Desafio("buscarNomeTime")
	public String buscarNomeTime(Long idTime) {
		verificaExistenciaEquipe(idTime);
		return mapDasEquipes.get(idTime).getNome();
	}

	@Desafio("buscarJogadoresDoTime")
	public List<Long> buscarJogadoresDoTime(Long idTime) {

		verificaExistenciaEquipe(idTime);
		//Retornando os keys(ids) dos jogadores usando stream, convertendo map para list
		return mapDosJogadores.values().stream()
				.filter(jogadores -> jogadores.getIdTime().equals(idTime))
				.map(jogadores -> jogadores.getId()).collect(Collectors.toList());
	}

	@Desafio("buscarMelhorJogadorDoTime")
	public Long buscarMelhorJogadorDoTime(Long idTime) {
		verificaExistenciaEquipe(idTime);
		return mapDosJogadores.values().stream()
				.filter(melhorJogador -> melhorJogador.getIdTime().equals(idTime))
				.max(Comparator.comparing(Jogador::getNivelHabilidade)).get().getId();
	}

	@Desafio("buscarJogadorMaisVelho")
	public Long buscarJogadorMaisVelho(Long idTime) {

		verificaExistenciaEquipe(idTime);
		return mapDosJogadores.values().stream()
				.filter(jogadorMaisVelho -> jogadorMaisVelho.getIdTime().equals(idTime))
				.min(Comparator.comparing(Jogador::getDataNascimento)).get().getId();
	}

	@Desafio("buscarTimes")
	public List<Long> buscarTimes() {
		//Retornando os keys(ids) das equipes usando stream, convertendo map para list
		return this.mapDasEquipes.keySet().stream().collect(Collectors.toList());
	}

	@Desafio("buscarJogadorMaiorSalario")
	public Long buscarJogadorMaiorSalario(Long idTime) {

		verificaExistenciaEquipe(idTime);
		return mapDosJogadores.values().stream()
				.filter(maiorSalario -> maiorSalario.getIdTime().equals(idTime))
				.max(Comparator.comparing(Jogador::getSalario)).get().getId();
	}

	@Desafio("buscarSalarioDoJogador")
	public BigDecimal buscarSalarioDoJogador(Long idJogador) {
		verificaExistenciaJogador(idJogador);
		return mapDosJogadores.get(idJogador).getSalario();
	}

	@Desafio("buscarTopJogadores")
	public List<Long> buscarTopJogadores(Integer top) {

		return mapDosJogadores.values().stream()
				.sorted(Comparator.comparingInt(Jogador::getNivelHabilidade).reversed())
				.map(melhorJogador -> melhorJogador.getId())
				.limit(top).collect(Collectors.toList());
	}

	@Desafio("buscarCorCamisaTimeDeFora")
	public String buscarCorCamisaTimeDeFora(Long timeDaCasa, Long timeDeFora) {

		verificaExistenciaEquipe(timeDaCasa);
		verificaExistenciaEquipe(timeDeFora);

		Equipe equipeCasa = mapDasEquipes.get(timeDaCasa);
		Equipe equipeFora = mapDasEquipes.get(timeDeFora);

		if (equipeCasa.getCorUniformePrincipal() == equipeFora.getCorUniformePrincipal()) {
			return equipeFora.getCorUniformeSecundario();
		} else {
			return equipeFora.getCorUniformePrincipal();
		}
	}

	public void verificaPrimeiraInsercao(Map map, Long id) {
		if (map.containsKey(id)) {
			throw new IdentificadorUtilizadoException();
		}
	}

	public void verificaExistenciaEquipe(Long idTime) {
		if (!(mapDasEquipes.containsKey(idTime))) {
			throw new TimeNaoEncontradoException();
		}
	}

	public void verificaExistenciaJogador(Long idJogador) {
		if (!(mapDosJogadores.containsKey(idJogador))) {
			throw new JogadorNaoEncontradoException();
		}
	}
}
