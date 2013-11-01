/*******************************************************************************
 * Copyright  2013 de Geraldo Augusto de Morais Figueiredo
 * Este arquivo é parte do programa Monitora, Brasil!. O Monitora, Brasil! é um software livre.
 * Você pode redistribuí-lo e/ou modificá-lo dentro dos termos da GNU Affero General Public License 
 * como publicada pela Fundação do Software Livre (FSF); na versão 3 da Licença. 
 * Este programa é distribuído na esperança que possa ser útil, mas SEM NENHUMA GARANTIA,
 * sem uma garantia implícita de ADEQUAÇÃO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. 
 * Veja a licença para maiores detalhes. 
 * Você deve ter recebido uma cópia da GNU Affero General Public License, sob o título "LICENSE.txt", 
 * junto com este programa, se não, acesse http://www.gnu.org/licenses/
 ******************************************************************************/
package com.gamfig.monitorabrasil.classes;

import java.util.ArrayList;

public class User {
	private int id;
	private String email;
	private String cidade;
	private String uf;
	private String dtAniversario;
	private String cidadeNatal;
	private String sobreMim;
	private String historicoEscolar;
	private String nome;
	private ArrayList<Politico> politicos;
	private ArrayList<Projeto> projetos;
	private ArrayList<AvaliacaoPolitico> avaliacaoPolitico;
	private ArrayList<AvaliacaoProjeto> avaliacaoProjeto;
	private String idFacebook;
	private String tipoConta;
	private String idGoogle;
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getDtAniversario() {
		return dtAniversario;
	}

	public void setDtAniversario(String dtAniversario) {
		this.dtAniversario = dtAniversario;
	}

	public String getCidadeNatal() {
		return cidadeNatal;
	}

	public void setCidadeNatal(String cidadeNatal) {
		this.cidadeNatal = cidadeNatal;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String localizacao) {
		this.uf = localizacao;
	}

	public String getSobreMim() {
		return sobreMim;
	}

	public void setSobreMim(String sobreMim) {
		this.sobreMim = sobreMim;
	}

	public String getHistoricoEscolar() {
		return historicoEscolar;
	}

	public void setHistoricoEscolar(String historicoEscolar) {
		this.historicoEscolar = historicoEscolar;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ArrayList<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(ArrayList<Projeto> projetos) {
		this.projetos = projetos;
	}

	public ArrayList<Politico> getPoliticos() {
		return politicos;
	}

	public void setPoliticos(ArrayList<Politico> politicos) {
		this.politicos = politicos;
	}

	public ArrayList<AvaliacaoProjeto> getAvaliacaoProjeto() {
		return avaliacaoProjeto;
	}

	public void setAvaliacaoProjeto(ArrayList<AvaliacaoProjeto> avaliacaoProjeto) {
		this.avaliacaoProjeto = avaliacaoProjeto;
	}

	public ArrayList<AvaliacaoPolitico> getAvaliacaoPolitico() {
		return avaliacaoPolitico;
	}

	public void setAvaliacaoPolitico(ArrayList<AvaliacaoPolitico> avaliacaoPolitico) {
		this.avaliacaoPolitico = avaliacaoPolitico;
	}

	public String getIdFacebook() {
		return idFacebook;
	}

	public void setIdFacebook(String idFacebook) {
		this.idFacebook = idFacebook;
	}

	public String getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(String tipoConta) {
		this.tipoConta = tipoConta;
	}

	public String getIdGoogle() {
		return idGoogle;
	}

	public void setIdGoogle(String idGoogle) {
		this.idGoogle = idGoogle;
	}
	
}
