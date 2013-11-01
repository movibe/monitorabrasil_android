/*******************************************************************************
 * Copyright  2013 de Geraldo Augusto de Morais Figueiredo
 * Este arquivo � parte do programa Monitora, Brasil!. O Monitora, Brasil! � um software livre.
 * Voc� pode redistribu�-lo e/ou modific�-lo dentro dos termos da GNU Affero General Public License 
 * como publicada pela Funda��o do Software Livre (FSF); na vers�o 3 da Licen�a. 
 * Este programa � distribu�do na esperan�a que possa ser �til, mas SEM NENHUMA GARANTIA,
 * sem uma garantia impl�cita de ADEQUA��O a qualquer MERCADO ou APLICA��O EM PARTICULAR. 
 * Veja a licen�a para maiores detalhes. 
 * Voc� deve ter recebido uma c�pia da GNU Affero General Public License, sob o t�tulo "LICENSE.txt", 
 * junto com este programa, se n�o, acesse http://www.gnu.org/licenses/
 ******************************************************************************/
package com.gamfig.monitorabrasil.classes;

import java.util.ArrayList;

public class Projeto {
	

	private int id;
	private String nome;
	private String ementa;
	private ArrayList<Politico> autores;
	private String orgaoEstado;
	private String orgaoNumerador;
	private String situacao;
	private String dtApresentacao;
	private String dtUltimoDespacho;
	private String link;
	private String ultimoDespacho;
	private String tipoProposicao;
	private String nomeAutor;
	private String email;
	private int s;//voto sim
	private int n;//voto nao
	private boolean votado;
	private ArrayList<Comentario> comentarios;
	private Comentario comentario;//para ultimo comentario
	public Comentario getComentario() {
		return comentario;
	}
	public void setComentario(Comentario comentario) {
		this.comentario = comentario;
	}
	private int qtdComentario;
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getUltimoDespacho() {
		return ultimoDespacho;
	}
	public void setUltimoDespacho(String ultimoDespacho) {
		this.ultimoDespacho = ultimoDespacho;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmenta() {
		return ementa;
	}
	public void setEmenta(String ementa) {
		this.ementa = ementa;
	}
	public ArrayList<Politico> getAutores() {
		return autores;
	}
	public void setAutores(ArrayList<Politico> autores) {
		this.autores = autores;
	}
	public String getOrgaoEstado() {
		return orgaoEstado;
	}
	public void setOrgaoEstado(String orgaoEstado) {
		this.orgaoEstado = orgaoEstado;
	}
	public String getOrgaoNumerador() {
		return orgaoNumerador;
	}
	public void setOrgaoNumerador(String orgaoNumerador) {
		this.orgaoNumerador = orgaoNumerador;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	public String getDtApresentacao() {
		return dtApresentacao;
	}
	public void setDtApresentacao(String dtApresentacao) {
		this.dtApresentacao = dtApresentacao;
	}
	public String getTipoProposicao() {
		return tipoProposicao;
	}
	public void setTipoProposicao(String tipoProposicao) {
		this.tipoProposicao = tipoProposicao;
	}
	public String getDtUltimoDespacho() {
		return dtUltimoDespacho;
	}
	public void setDtUltimoDespacho(String dtUltimoDespacho) {
		this.dtUltimoDespacho = dtUltimoDespacho;
	}
	public String getNomeAutor() {
		return nomeAutor;
	}
	public void setNomeAutor(String nomeAutor) {
		this.nomeAutor = nomeAutor;
	}
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	public int getS() {
		return s;
	}
	public void setS(int s) {
		this.s = s;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isVotado() {
		return votado;
	}
	public void setVotado(boolean votado) {
		this.votado = votado;
	}
	public ArrayList<Comentario> getComentarios() {
		return comentarios;
	}
	public void setComentarios(ArrayList<Comentario> comentarios) {
		this.comentarios = comentarios;
	}
	public int getQtdComentario() {
		return qtdComentario;
	}
	public void setQtdComentario(int qtdComentario) {
		this.qtdComentario = qtdComentario;
	}
	

}
