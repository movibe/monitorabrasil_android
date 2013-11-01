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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.DAO.DeputadoDAO;
import com.gamfig.monitorabrasil.adapter.VotoAdapter;
import com.gamfig.monitorabrasil.classes.Votacao;

public class VotosActivity extends Activity {

	private String idProposta;
	public static String nomeProjeto;
	private Activity minhaActivity;
	VotoAdapter adapter;
	private boolean isDefaultSelectionPartido;
	private boolean isDefaultSelectionUf;
	private boolean isDefaultSelectionVoto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_votos);
		// Show the Up button in the action bar.
		setupActionBar();

		isDefaultSelectionPartido = true;
		isDefaultSelectionUf = true;
		isDefaultSelectionVoto = true;

		// pegar parametros
		Bundle receiveBundle = this.getIntent().getExtras();
		idProposta = String.valueOf(receiveBundle.getInt("idProjeto"));
		nomeProjeto = receiveBundle.getString("nomePec");
		
		montaListas();

		// buscar Votacao
		minhaActivity = this;

		new buscaVotacao(minhaActivity).execute();

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
					new buscaVotacao(minhaActivity).execute();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

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
					new buscaVotacao(minhaActivity).execute();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		// spinner de Voto
		String[] votos = getResources().getStringArray(R.array.votos);

		Spinner spinnerVoto = (Spinner) findViewById(R.id.spinner3);
		ArrayAdapter<String> adapterVoto = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, votos);
		// Specify the layout to use when the list of choices appears
		adapterVoto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinnerVoto.setAdapter(adapterVoto);

		spinnerVoto.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				if (isDefaultSelectionVoto) { // If spinner initializes
					isDefaultSelectionVoto = false;
				} else {
					new buscaVotacao(minhaActivity).execute();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

	}

	public class buscaVotacao extends AsyncTask<Void, Void, String> {
		Activity votacaoActivit;
		Votacao votacao;
		ProgressBar pbar;
		ListView lv;

		public buscaVotacao(Activity votacaoActivit) {
			this.votacaoActivit = votacaoActivit;
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
			
						String partido;
						Spinner filtroSpinner = (Spinner) findViewById(R.id.spinner1);
						partido = (String) filtroSpinner.getItemAtPosition(filtroSpinner.getSelectedItemPosition());

						// busca a uf selecionada
						String uf;
						Spinner ufSpinner = (Spinner) findViewById(R.id.spinner2);
						uf = (String) ufSpinner.getItemAtPosition(ufSpinner.getSelectedItemPosition());
						
						//filtro voto
						// busca a uf selecionada
						String voto;
						Spinner votoSpinner = (Spinner) findViewById(R.id.spinner3);
						voto = (String) votoSpinner.getItemAtPosition(votoSpinner.getSelectedItemPosition());

			// buscar os projetos da lista do user
			votacao = new DeputadoDAO().buscaVotacao(idProposta,partido,uf,voto);

			return "ok";
		}

		protected void onPostExecute(String results) {

			// popula listivew com os projetos de autoria do deputado

			lv = (ListView) findViewById(R.id.listView1);
			if (votacao.getPoliticos() != null) {

				// atualizar campos
				TextView txtVotosData = (TextView) findViewById(R.id.txtVotosData);
				TextView txtVotosObjetivo = (TextView) findViewById(R.id.txtVotosObjetivo);
				TextView txtVotosResumo = (TextView) findViewById(R.id.txtVotosResumo);
				TextView txtVotoNomeProjeto = (TextView) findViewById(R.id.txtVotoNomeProjeto);

				txtVotosObjetivo.setText(votacao.getObjetivo());
				txtVotosResumo.setText(votacao.getResumo());
				txtVotosData.setText(votacao.getData());
				txtVotoNomeProjeto.setText(nomeProjeto);

				adapter = new VotoAdapter(votacaoActivit, R.layout.listview_item_voto, votacao.getPoliticos());
				lv.setAdapter(adapter);
				lv.setVisibility(View.VISIBLE);
			}else{
				Toast.makeText(votacaoActivit, "Nenhum voto encontrado", Toast.LENGTH_SHORT).show();
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
		getMenuInflater().inflate(R.menu.votos, menu);
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
