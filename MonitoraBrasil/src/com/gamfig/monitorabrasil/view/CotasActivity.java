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

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;

import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.DAO.DeputadoDAO;
import com.gamfig.monitorabrasil.adapter.CotaAdapter;
import com.gamfig.monitorabrasil.classes.Politico;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CotasActivity extends Activity {
	String jsonPoliticos;
	Context context = null;
	String partido;
	private Activity minhaActivity;
	private boolean isDefaultSelectionPartido;
	private boolean isDefaultSelectionUf;
	private String tipoListagem; // se traz todos ou so favoritos
	private String idUser;
	List<Politico> politicos = new ArrayList<Politico>();
	private ListView lv;
	CotaAdapter adapterCotas = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cotas);
		// Show the Up button in the action bar.
		setupActionBar();
		isDefaultSelectionPartido = true;
		isDefaultSelectionUf = true;

		setupListas();
	}

	private void setupListas() {
		// spinner de partidos
		String[] partidos = getResources().getStringArray(R.array.subCotas);

		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, partidos);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				if (isDefaultSelectionPartido) { // If spinner initializes
					isDefaultSelectionPartido = false;
				} else {
					new CargaDeputados(minhaActivity, "").execute();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		// spinner de uf
		String[] listUf = getResources().getStringArray(R.array.mes);
		Spinner spinnerUf = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<String> adapterUf = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listUf);
		// Specify the layout to use when the list of choices appears
		adapterUf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinnerUf.setAdapter(adapterUf);

		spinnerUf.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

				if (isDefaultSelectionUf) { // If spinner initializes
					isDefaultSelectionUf = false;
				} else {
					new CargaDeputados(minhaActivity, "").execute();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		ListView lv = (ListView) findViewById(R.id.listView1);
		if (lv.getAdapter() == null) {

			// guarda o context para usar na tarefa assync
			context = this;

			// BUSCA E MONTA O LISTVIEW COM OS DEPUTADOS
			new CargaDeputados(this, "").execute();
		}

	}

	public class CargaDeputados extends AsyncTask<Void, Void, String> {

		private Activity mActivity;
		private String query;
		ProgressBar pbar;

		public CargaDeputados(Activity activity, String q) {
			mActivity = activity;
			query = q;
			pbar = (ProgressBar) findViewById(R.id.progressBar1);
			lv = (ListView) findViewById(R.id.listView1);
		}

		protected void onPreExecute() {
			pbar.setVisibility(View.VISIBLE);
			lv.setVisibility(View.INVISIBLE);
		}

		@Override
		protected String doInBackground(Void... params) {

			// busca o partido selecionado
			String ano;
			Spinner partidoSpinner = (Spinner) findViewById(R.id.spinner1);
			ano = (String) partidoSpinner.getItemAtPosition(partidoSpinner.getSelectedItemPosition());

			// busca a uf selecionada
			String mes;
			Spinner ufSpinner = (Spinner) findViewById(R.id.spinner2);
			mes = (String) ufSpinner.getItemAtPosition(ufSpinner.getSelectedItemPosition());

			String idSubcota = null;
			// pegar idSubcota
			if (!ano.equals("Todas")) {
				String[] sucota = ano.split("-");
				idSubcota = sucota[1];
			}
			new DeputadoDAO();
			// parametro 1 diz que sao todos deputados
			jsonPoliticos = DeputadoDAO.buscaCotas(ano, mes, idSubcota);

			return jsonPoliticos;
		}

		protected void onPostExecute(String results) {

			if (results != null) {
				lv = (ListView) findViewById(R.id.listView1);

				Gson gson = new Gson();
				politicos = gson.fromJson(jsonPoliticos, new TypeToken<ArrayList<Politico>>() {
				}.getType());
				adapterCotas = new CotaAdapter(context, R.layout.listview_item_cota, politicos);

				lv.setAdapter(adapterCotas);

				lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Politico politico = (Politico) parent.getItemAtPosition(position);
						if (politico != null) {
							Bundle sendBundle = new Bundle();
							if (politico.getIdCadastro() > 0) {
								sendBundle.putInt("idCadastro", politico.getIdCadastro());
								Intent i = new Intent(CotasActivity.this, FichaDeputado.class);
								i.putExtras(sendBundle);
								startActivity(i);
							}
						}
					}
				});
				lv.setVisibility(View.VISIBLE);
			}
			// esconder o progressbar
			pbar.setVisibility(View.GONE);
		}
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

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cotas, menu);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
			SearchView searchView = (SearchView) menu.findItem(R.id.teste).getActionView();
			searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
			searchView.setIconifiedByDefault(false);
			searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

				@Override
				public boolean onQueryTextSubmit(String query) {

					return false;
				}

				@Override
				public boolean onQueryTextChange(String newText) {
					// PoliticoAdapter padpt = (PoliticoAdapter)
					// lv.getAdapter();
					// buscar na listagem já carregada
					if (TextUtils.isEmpty(newText)) {
						adapterCotas = new CotaAdapter(context, R.layout.listview_item_cota, politicos);
						lv.setAdapter(adapterCotas);
						// adapterPoliticos.getFilter().filter("");
						// lv.clearTextFilter();
					} else {
						// adapterPoliticos.getFilter().filter(newText.toString());
						newText = newText.toString().toLowerCase();
						List<Politico> founded = new ArrayList<Politico>();
						if (newText != null && newText.toString().length() > 0) {
							for (Politico item : adapterCotas.getData()) {
								if (item.getNome().toString().toLowerCase().contains(newText)) {
									founded.add(item);
								}
							}
						}
						adapterCotas = new CotaAdapter(context, R.layout.listview_item_cota, founded);
						lv.setAdapter(adapterCotas);
					}
					return false;
				}
			});
		}
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
		case R.id.teste:
			onSearchRequested();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
