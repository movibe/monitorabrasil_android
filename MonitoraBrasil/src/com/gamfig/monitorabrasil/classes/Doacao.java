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
