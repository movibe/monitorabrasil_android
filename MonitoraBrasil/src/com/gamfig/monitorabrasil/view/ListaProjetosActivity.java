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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.DAO.DeputadoDAO;
import com.gamfig.monitorabrasil.adapter.ProjetoVotoAdapter;
import com.gamfig.monitorabrasil.classes.Projeto;

public class ListaProjetosActivity extends Activity implements OnScrollListener {
	String idUser;
	private Activity minhaActivity;
	ProjetoVotoAdapter adapter;
	ListView lv;
	private boolean isDefaultSelectionPartido;
	private boolean isDefaultSelectionUf;
	int currentPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_projetos);
		// Show the Up button in the action bar.
		minhaActivity = this;
		setupActionBar();
		

		isDefaultSelectionPartido = true;
		isDefaultSelectionUf = true;

		montaListas();

		// buscar projetos na lista de favoritos
		idUser = jaCadastrou();
		// if (idUser == null) {
		//
		// } else {
		new buscaProjetos(minhaActivity).execute();
		// }
		lv = (ListView) findViewById(R.id.listView1);
		lv.setOnScrollListener(this);

	}

	private void montaListas() {
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
					new buscaProjetos(minhaActivity).execute();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// spinner de filtro projetos
		String[] listProjetos = getResources().getStringArray(R.array.opcaofiltroprojetos);
		Spinner spinnerProjetos = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> adapterProjetos = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				listProjetos);
		// Specify the layout to use when the list of choices appears
		adapterProjetos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinnerProjetos.setAdapter(adapterProjetos);

		spinnerProjetos.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

				if (isDefaultSelectionPartido) { // If spinner initializes
					isDefaultSelectionPartido = false;
				} else {
					new buscaProjetos(minhaActivity).execute();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}

	@Override
	public void onScrollStateChanged(AbsListView v, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			if (lv.getLastVisiblePosition() >= lv.getCount() - 3) {
				currentPage++;
				// load more list items:
				loadElements(currentPage);
			}
		}

	}

	private void loadElements(int currentPage2) {

		new buscaMaisProjetos(minhaActivity, currentPage2).execute();

	}

	public class buscaMaisProjetos extends AsyncTask<Void, Void, String> {
		Activity listaProjetosActivit;
		private ProgressDialog mDialog;
		ArrayList<Projeto> projetos;
		int page;

		public buscaMaisProjetos(Activity listaProjetosActivity, int currentPage2) {
			this.listaProjetosActivit = listaProjetosActivity;
			page = currentPage2;
		}

		@Override
		protected String doInBackground(Void... params) {

			// busca o filtro selecionado
			String filtro;
			Spinner filtroSpinner = (Spinner) findViewById(R.id.spinner1);
			filtro = (String) filtroSpinner.getItemAtPosition(filtroSpinner.getSelectedItemPosition());

			// busca a uf selecionada
			String uf;
			Spinner ufSpinner = (Spinner) findViewById(R.id.spinner2);
			uf = (String) ufSpinner.getItemAtPosition(ufSpinner.getSelectedItemPosition());

			// buscar os projetos da lista do user
			projetos = new DeputadoDAO().buscaProjetosVoto(page * 10, filtro, uf, idUser,0);
			return "ok";
		}

		protected void onPostExecute(String results) {

			// popula listivew com os projetos de autoria do deputado
			if (projetos != null) {
				if (!projetos.get(0).getNome().equals("Nenhum projeto")) {
					for (Projeto projeto : projetos) {
						adapter.add(projeto);
					}
					adapter.notifyDataSetChanged();
				} else {
					Toast.makeText(ListaProjetosActivity.this, "Não há mais projetos!", Toast.LENGTH_LONG).show();
				}
			}

		}
	}

	// buscar id do usuário
	private String jaCadastrou() {
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.id_key_preferencias), MODE_PRIVATE);
		String id = sharedPref.getString(getString(R.string.id_key_idcadastro), null);
		if (id == null) {
			return null;
		} else {
			if (id.equals("null"))
				return null;
			return id;
		}

	}

	public class buscaProjetos extends AsyncTask<Void, Void, String> {
		Activity listaProjetosActivit;
		ArrayList<Projeto> projetos;
		ProgressBar pbar;
		ListView lv;

		public buscaProjetos(Activity listaProjetosActivity) {
			this.listaProjetosActivit = listaProjetosActivity;
			pbar = (ProgressBar) findViewById(R.id.progressBar1);
			lv = (ListView) findViewById(R.id.listView1);
		}

		protected void onPreExecute() {
			pbar.setVisibility(View.VISIBLE);
			lv.setVisibility(View.INVISIBLE);
		}

		@Override
		protected String doInBackground(Void... params) {

			// busca o filtro selecionado
			String filtro;
			Spinner filtroSpinner = (Spinner) findViewById(R.id.spinner1);
			filtro = (String) filtroSpinner.getItemAtPosition(filtroSpinner.getSelectedItemPosition());

			// busca a uf selecionada
			String uf;
			Spinner ufSpinner = (Spinner) findViewById(R.id.spinner2);
			uf = (String) ufSpinner.getItemAtPosition(ufSpinner.getSelectedItemPosition());

			// buscar os projetos da lista do user
			projetos = new DeputadoDAO().buscaProjetosVoto(0, filtro, uf, idUser,0);

			return "ok";
		}

		protected void onPostExecute(String results) {

			// popula listivew com os projetos de autoria do deputado

			lv = (ListView) findViewById(R.id.listView1);
			if (projetos != null) {
				if (!projetos.get(0).getNome().equals("Nenhum projeto")) {
					// add the footer before adding the adapter, else the footer
					// will not load!
					if (lv.getFooterViewsCount() == 0) {
						View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
								R.layout.listfooter, null, false);
						lv.addFooterView(footerView);
					}

					adapter = new ProjetoVotoAdapter(listaProjetosActivit, R.layout.listview_item_projetos, projetos);
					lv.setAdapter(adapter);
					lv.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							Projeto projeto = (Projeto) parent.getItemAtPosition(position);
							if (projeto != null) {

								// buscar detalhes do projeto clicado
								Bundle sendBundle = new Bundle();
								sendBundle.putInt("idProjeto", projeto.getId());
								Intent i = new Intent(ListaProjetosActivity.this, ProjetoDetalheActivity.class);
								i.putExtras(sendBundle);
								startActivity(i);
							}
						}
					});
				}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista_projetos, menu);
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
