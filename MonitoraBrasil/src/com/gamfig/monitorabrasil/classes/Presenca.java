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
