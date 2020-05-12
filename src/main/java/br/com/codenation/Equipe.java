package br.com.codenation;

import java.time.LocalDate;

public class Equipe {
    Long id;
    String nome;
    LocalDate dataCriacao;
    String corUniformePrincipal;
    String corUniformeSecundario;
    Long idCapitao;


    public Equipe(Long id, String nome, LocalDate dataCriacao, String corUniformePrincipal, String corUniformeSecundario) {
        this.id = id;
        this.nome = nome;
        this.dataCriacao = dataCriacao;
        this.corUniformePrincipal = corUniformePrincipal;
        this.corUniformeSecundario = corUniformeSecundario;
    }

//    public Long getId() {
//        return this.id;
//    }

    public String getNome() {
        return this.nome;
    }

    public String getCorUniformePrincipal() {
        return this.corUniformePrincipal;
    }

    public String getCorUniformeSecundario() {
        return this.corUniformeSecundario;
    }

    public Long getCapitao() {
        return this.idCapitao;
    }

    public void setCapitao(Long capitao) {
        this.idCapitao = capitao;
    }
}
