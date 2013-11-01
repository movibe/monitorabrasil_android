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
		txt.setText(Html.fromHtml("<p>O aplicativo Monitora Brasil utiliza os dados disponibilizados pelo site da C�mara dos Deputados. </p>"+
        "<p>Pode haver alguma diferen�a de informa��o, pois os dados s�o atualizados a cada dois dias. Caso encontre algum problema, favor entre em contato conosco.</p>"+
        "<p>Vamos juntos construir uma ferramenta que possibilite a n�s, cidad�os, monitorar os parlamentares e suas a��es!</p>" +
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
