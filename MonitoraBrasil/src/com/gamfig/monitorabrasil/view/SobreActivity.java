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
package com.gamfig.monitorabrasil.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.gamfig.monitorabrasil.R;

public class SobreActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sobre);
		// Show the Up button in the action bar.
		setupActionBar();
		TextView txt = (TextView)findViewById(R.id.txtPropostaTitulo);
		txt.setText(Html.fromHtml("<p>O aplicativo Monitora Brasil utiliza os dados disponibilizados pelo site da Câmara dos Deputados. </p>"+
        "<p>Pode haver alguma diferença de informação, pois os dados são atualizados a cada dois dias. Caso encontre algum problema, favor entre em contato conosco.</p>"+
        "<p>Vamos juntos construir uma ferramenta que possibilite a nós, cidadãos, monitorar os parlamentares e suas ações!</p>" +
        "<p>Equipe Monitora Brasil <br><a href='https://twitter.com/monitorabrasil'>@MonitoraBrasil</a></p>"));
		txt.setMovementMethod(LinkMovementMethod.getInstance());
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	

}
