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

public class Votacao {
	private int id;
	private String resumo;
	private String data;
	private String objetivo;
	private Projeto projeto;
	private ArrayList<Politico> politicos;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getResumo() {
		return resumo;
	}
	public void setResumo(String resumo) {
		this.resumo = resumo;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getObjetivo() {
		return objetivo;
	}
	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}
	public Projeto getProjeto() {
		return projeto;
	}
	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}
	public ArrayList<Politico> getPoliticos() {
		return politicos;
	}
	public void setPoliticos(ArrayList<Politico> politicos) {
		this.politicos = politicos;
	}
}
