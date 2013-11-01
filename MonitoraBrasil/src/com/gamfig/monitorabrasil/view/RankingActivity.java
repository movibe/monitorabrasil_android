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
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.view.tabs.RankingFactoryActivity;

public class RankingActivity extends TabActivity {

	private TabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking);
		// Show the Up button in the action bar.
		setupActionBar();		
		mTabHost = getTabHost();
		montarTabs();
	}

	private void montarTabs() {
		// montar tabs
		
		mTabHost.setup();
		setupTab(new TextView(this), "Faltosos", new Intent(this, RankingFactoryActivity.class), "falta");
		setupTab(new TextView(this), "Assiduidade", new Intent(this, RankingFactoryActivity.class), "assid");
		setupTab(new TextView(this), "Projetos", new Intent(this, RankingFactoryActivity.class), "projetos");
		setupTab(new TextView(this), "Doa��es Recebidas", new Intent(this, RankingFactoryActivity.class), "doacao");
		
		
		
	}
	
	private void setupTab(final View view, final String tag, Intent intent, String tpRank) {
		View tabview = createTabView(mTabHost.getContext(), tag);

		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(new TabContentFactory() {
			public View createTabContent(String tag) {
				return view;
			}
		});
		Bundle b = new Bundle();	
		b.putString("tpRank", tpRank);
		intent.putExtras(b);
		setContent.setContent(intent);
		mTabHost.addTab(setContent);

	}

	private View createTabView(Context context, String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ranking, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
