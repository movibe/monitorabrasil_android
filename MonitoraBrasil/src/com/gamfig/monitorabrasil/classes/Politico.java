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

public class Politico {
	
	private int idCadastro;
	private int idMatricula;
	private int idParlamentar;
	private String nome;
	private String nomeParlamentar;
	private String tipoParlamentar;
	private Partido partido;
	private String email;
	private String uf;
	private int gabinete;
	private String anexo;
	private String telefone;
	private String twitter;
	private String facebook;
	private ArrayList<Presenca> presenca;
	private ArrayList<Projeto> projetos;
	private int nrProjetos;
	private Float notaAvaliacao;
	private String lider;
	private String voto;
	private double valor;//total doacao recebida
	private Cota cotas;
	
	
	
	
	public ArrayList<Projeto> getProjetos() {
		return projetos;
	}
	public void setProjetos(ArrayList<Projeto> projetos) {
		this.projetos = projetos;
	}
	public int getIdCadastro() {
		return idCadastro;
	}
	public void setIdCadastro(int idCadastro) {
		this.idCadastro = idCadastro;
	}
	public int getIdMatricula() {
		return idMatricula;
	}
	public void setIdMatricula(int idMatricula) {
		this.idMatricula = idMatricula;
	}
	public int getIdParlamentar() {
		return idParlamentar;
	}
	public void setIdParlamentar(int idParlamentar) {
		this.idParlamentar = idParlamentar;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNomeParlamentar() {
		return nomeParlamentar;
	}
	public void setNomeParlamentar(String nomeParlamentar) {
		this.nomeParlamentar = nomeParlamentar;
	}
	public String getTipoParlamentar() {
		return tipoParlamentar;
	}
	public void setTipoParlamentar(String tipoParlamentar) {
		this.tipoParlamentar = tipoParlamentar;
	}
	public Partido getPartido() {
		return partido;
	}
	public void setPartido(Partido partido) {
		this.partido = partido;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public int getGabinete() {
		return gabinete;
	}
	public void setGabinete(int gabinete) {
		this.gabinete = gabinete;
	}
	public String getAnexo() {
		return anexo;
	}
	public void setAnexo(String anexo) {
		this.anexo = anexo;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getTwitter() {
		return twitter;
	}
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}
	public String getFacebook() {
		return facebook;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}
	
	
	public ArrayList<Presenca> getPresenca() {
		return presenca;
	}
	public void setPresenca(ArrayList<Presenca> presenca) {
		this.presenca = presenca;
	}
	public int getNrProjetos() {
		return nrProjetos;
	}
	public void setNrProjetos(int nrProjetos) {
		this.nrProjetos = nrProjetos;
	}
	public Float getNotaAvaliacao() {
		return notaAvaliacao;
	}
	public void setNotaAvaliacao(Float notaAvaliacao) {
		this.notaAvaliacao = notaAvaliacao;
	}
	public String getLider() {
		return lider;
	}
	public void setLider(String lider) {
		this.lider = lider;
	}
	public String getVoto() {
		return voto;
	}
	public void setVoto(String voto) {
		this.voto = voto;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public Cota getCotas() {
		return cotas;
	}
	public void setCotas(Cota cotas) {
		this.cotas = cotas;
	}
	
	

}
