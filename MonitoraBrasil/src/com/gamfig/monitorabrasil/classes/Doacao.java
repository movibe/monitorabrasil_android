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

public class Doacao {
private Politico politico;
private float total;
private String tipo;
private int idTipo;
private String nomeDoador;

public Politico getPolitico() {
	return politico;
}
public void setPolitico(Politico politico) {
	this.politico = politico;
}
public float getTotal() {
	return total;
}
public void setTotal(float valor) {
	this.total = valor;
}
public String getTipo() {
	return tipo;
}
public void setTipo(String tipo) {
	this.tipo = tipo;
}
public String getNomeDoador() {
	return nomeDoador;
}
public void setNomeDoador(String nomeDoador) {
	this.nomeDoador = nomeDoador;
}
public int getIdTipo() {
	return idTipo;
}
public void setIdTipo(int idTipo) {
	this.idTipo = idTipo;
}
}
