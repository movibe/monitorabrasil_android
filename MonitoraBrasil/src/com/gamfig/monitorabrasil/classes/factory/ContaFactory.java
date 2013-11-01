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
package com.gamfig.monitorabrasil.classes.factory;

import com.gamfig.monitorabrasil.classes.Conta;

public class ContaFactory {

	public ContaFactory() {

	}

	public Conta criaConta(String tipo) {
		Conta conta = null;
		if (tipo.equals("facebook"))
			conta = new Facebook();
		else if (tipo.equals("google"))
			conta = new Google();
		else if (tipo.equals("twitter"))
			conta = new Twitter();
		return conta;
	}
}
