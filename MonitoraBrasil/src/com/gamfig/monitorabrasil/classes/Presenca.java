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

import java.text.DecimalFormat;

public class Presenca {
	private int ano;
	private int nrPresenca;
	private int nrAusenciaJustificada;
	private int nrAusenciaNaoJustificada;
	private int total;
	
	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
	public int getNrPresenca() {
		return nrPresenca;
	}
	public void setNrPresenca(int nrPresenca) {
		this.nrPresenca = nrPresenca;
	}
	public int getNrAusenciaJustificada() {
		return nrAusenciaJustificada;
	}
	public void setNrAusenciaJustificada(int nrAusenciaJustificada) {
		this.nrAusenciaJustificada = nrAusenciaJustificada;
	}
	public int getNrAusenciaNaoJustificada() {
		return nrAusenciaNaoJustificada;
	}
	public void setNrAusenciaNaoJustificada(int nrAusenciaNaoJustificada) {
		this.nrAusenciaNaoJustificada = nrAusenciaNaoJustificada;
	}
	
	public String getPercentualPresenca(){
		DecimalFormat form = new DecimalFormat("0.00");
		return form.format(Float.valueOf(this.nrPresenca)/Float.valueOf((this.nrPresenca+this.nrAusenciaJustificada+this.nrAusenciaNaoJustificada))*100);
	}
	
	public String getPercentualAusenciaJustificada(){
		DecimalFormat form = new DecimalFormat("0.00");
		return form.format(Float.valueOf(this.nrAusenciaJustificada)/Float.valueOf((this.nrPresenca+this.nrAusenciaJustificada+this.nrAusenciaNaoJustificada))*100);
	}
	
	public String getPercentualAusenciaNaoJustificada(){
		DecimalFormat form = new DecimalFormat("0.00");
		return form.format(Float.valueOf(this.nrAusenciaNaoJustificada)/Float.valueOf((this.nrPresenca+this.nrAusenciaJustificada+this.nrAusenciaNaoJustificada))*100);
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
}
