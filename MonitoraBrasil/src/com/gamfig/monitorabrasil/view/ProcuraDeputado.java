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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.gamfig.monitorabrasil.adapter.PoliticoAdapter;
import com.gamfig.monitorabrasil.classes.Politico;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ProcuraDeputado extends Activity {
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
	PoliticoAdapter adapterPoliticos = null;

	// TODO procurar por nome em cima da lista j� populada

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_procura_deputado);
		// Show the Up button in the action bar.
		minhaActivity = this;
		setupActionBar();

		Bundle receiveBundle = this.getIntent().getExtras();
		idUser = "";
		if (receiveBundle != null) {
			tipoListagem = receiveBundle.getString("tipoLista");
			if (tipoListagem.equals("2")) {
				SharedPreferences sharedPref = getSharedPreferences(getString(R.string.id_key_preferencias),
						MODE_PRIVATE);
				idUser = sharedPref.getString(getString(R.string.id_key_idcadastro), null);
			}
		}

		isDefaultSelectionPartido = true;
		isDefaultSelectionUf = true;

		setupListas();

	}

	private void setupListas() {
		// spinner de partidos
		String[] partidos = getResources().getStringArray(R.array.partidos);

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
		String[] listUf = getResources().getStringArray(R.array.ufs);
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
			String partido;
			Spinner partidoSpinner = (Spinner) findViewById(R.id.spinner1);
			partido = (String) partidoSpinner.getItemAtPosition(partidoSpinner.getSelectedItemPosition());

			// busca a uf selecionada
			String uf;
			Spinner ufSpinner = (Spinner) findViewById(R.id.spinner2);
			uf = (String) ufSpinner.getItemAtPosition(ufSpinner.getSelectedItemPosition());

			// TODO salvar a pesquisa

			// SharedPreferences sharedPref =
			// getSharedPreferences(getString(R.string.id_key_preferencias),
			// MODE_PRIVATE);
			// String dtAtualizacao =
			// sharedPref.getString(getString(R.string.id_key_dtAtualizacao),
			// null);
			new DeputadoDAO();
			// verificar se tem atualizacao na lista de deputados
			// if (DeputadoDAO.temAtualizacao(dtAtualizacao)) {
			// parametro 1 diz que sao todos deputados
			jsonPoliticos = DeputadoDAO.connect(partido, uf, tipoListagem, query, idUser);
			// salva a data
			// final Calendar c = Calendar.getInstance();
			// String dt =
			// c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DAY_OF_MONTH);
			// SharedPreferences.Editor editor = sharedPref.edit();
			// editor.putString(getString(R.string.id_key_dtAtualizacao), dt);
			//
			// //salva o json
			// editor.putString(getString(R.string.id_key_dtAtualizacao),
			// jsonPoliticos);
			// } else {

			// }
			return jsonPoliticos;
		}

		protected void onPostExecute(String results) {

			if (results != null) {
				lv = (ListView) findViewById(R.id.listView1);

				Gson gson = new Gson();
				politicos = gson.fromJson(jsonPoliticos, new TypeToken<ArrayList<Politico>>() {
				}.getType());
				adapterPoliticos = new PoliticoAdapter(context, R.layout.listview_item_row, politicos);

				lv.setAdapter(adapterPoliticos);

				lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Politico politico = (Politico) parent.getItemAtPosition(position);
						if (politico != null) {
							Bundle sendBundle = new Bundle();
							if (politico.getIdCadastro() > 0) {
								sendBundle.putInt("idCadastro", politico.getIdCadastro());
								Intent i = new Intent(ProcuraDeputado.this, FichaDeputado.class);
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
		getMenuInflater().inflate(R.menu.procura_deputado, menu);
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
					// buscar na listagem j� carregada
					if (TextUtils.isEmpty(newText)) {
						adapterPoliticos = new PoliticoAdapter(context, R.layout.listview_item_row, politicos);
						lv.setAdapter(adapterPoliticos);
						// adapterPoliticos.getFilter().filter("");
						// lv.clearTextFilter();
					} else {
						// adapterPoliticos.getFilter().filter(newText.toString());
						newText = newText.toString().toLowerCase();
						List<Politico> founded = new ArrayList<Politico>();
						if (newText != null && newText.toString().length() > 0) {
							for (Politico item : adapterPoliticos.getData()) {
								if (item.getNome().toString().toLowerCase().contains(newText)) {
									founded.add(item);
								}
							}
						}
						adapterPoliticos = new PoliticoAdapter(context, R.layout.listview_item_row, founded);
						lv.setAdapter(adapterPoliticos);
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
