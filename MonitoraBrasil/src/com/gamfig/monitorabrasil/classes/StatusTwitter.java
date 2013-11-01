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

import java.util.Date;

import twitter4j.User;
import android.graphics.Bitmap;

public class StatusTwitter {
	private String nome;
	private String id;
	private String mensagem;
	private Bitmap bmpImagem;
	private Date data;
	
	
	public StatusTwitter(User user, Date createdAt, String text, Bitmap imagem) {
		//pegar a url da imagem		
		this.bmpImagem = imagem ;
		this.mensagem = text;
		this.data = createdAt;
		this.nome = user.getName();
		this.id = user.getScreenName();
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public Bitmap getFoto() {
		return bmpImagem;
	}
	public void setFoto(Bitmap foto) {
		this.bmpImagem = foto;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	

}
