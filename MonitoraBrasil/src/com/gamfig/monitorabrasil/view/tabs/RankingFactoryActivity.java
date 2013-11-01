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
package com.gamfig.monitorabrasil.view.tabs;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.DAO.DeputadoDAO;
import com.gamfig.monitorabrasil.adapter.RankingAdapter;
import com.gamfig.monitorabrasil.classes.Politico;
import com.gamfig.monitorabrasil.view.FichaDeputado;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RankingFactoryActivity extends Activity {


	
	String jsonPoliticos;
	String tpRank;
	Context context = null;
	Activity minhaActivity;
	// Checks report spinner selection is default or user selected item
	private boolean isDefaultSelectionPartido;
	private boolean isDefaultSelectionUf;
	private boolean isDefaultSelectionAno;
	ProgressBar pbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking_factory);
		Bundle receiveBundle = this.getIntent().getExtras();
		tpRank = receiveBundle.getString("tpRank");
		// Set true at onCreate
		isDefaultSelectionPartido = true;
		isDefaultSelectionAno = true;
		isDefaultSelectionUf = true;
		minhaActivity=this;
		pbar = (ProgressBar) findViewById(R.id.progressBar1);
		carregarSpinners();
	}

	private void carregarSpinners() {
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
				}else{
					new LoadRank(minhaActivity).execute(new String[] { tpRank });
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
				}else{
					new LoadRank(minhaActivity).execute(new String[] { tpRank });
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				

			}

		});

		// spinner de ano
		String[] listAno = getResources().getStringArray(R.array.anos);
		Spinner spinnerAno = (Spinner) findViewById(R.id.spinnerAno);
		ArrayAdapter<String> adapterAno = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listAno);
		// Specify the layout to use when the list of choices appears
		adapterAno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinnerAno.setAdapter(adapterAno);

		spinnerAno.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

				if (isDefaultSelectionAno) { // If spinner initializes					
					isDefaultSelectionAno = false;
				}else{
					new LoadRank(minhaActivity).execute(new String[] { tpRank });
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				

			}

		});

		context = this;
		 new LoadRank(this).execute(new String[] { tpRank });
	}

	// <Params, naoSei, Retorno>
	public class LoadRank extends AsyncTask<String, Void, String> {

		private Activity mActivity;
	    ProgressBar pbar;
	    ListView lv;
		
		
	    public LoadRank(Activity activity) {
	        mActivity = activity;
	        pbar = (ProgressBar) findViewById(R.id.progressBar1);
	        lv = (ListView) findViewById(R.id.listView1);
	    }
	    
	    protected void onPreExecute() {
	      
	        lv.setVisibility(View.INVISIBLE);
	        pbar.setVisibility(View.VISIBLE);
	        //mDialog.show();
	    }
		@Override
		protected String doInBackground(String... params) {

			// busca o partido selecionado
			String partido;
			Spinner partidoSpinner = (Spinner) findViewById(R.id.spinner1);
			partido = (String) partidoSpinner.getItemAtPosition(partidoSpinner.getSelectedItemPosition());

			// busca a uf selecionada
			String uf;
			Spinner ufSpinner = (Spinner) findViewById(R.id.spinner2);
			uf = (String) ufSpinner.getItemAtPosition(ufSpinner.getSelectedItemPosition());

			// busca o ano
			String ano;
			Spinner anoSpinner = (Spinner) findViewById(R.id.spinnerAno);
			ano = (String) anoSpinner.getItemAtPosition(anoSpinner.getSelectedItemPosition());

			new DeputadoDAO();
			jsonPoliticos = DeputadoDAO.buscaRanking(partido, uf, params[0], ano);
			return jsonPoliticos;
		}

		protected void onPostExecute(String results) {

			if (results != null) {
				

				Gson gson = new Gson();
				List<Politico> politicos = gson.fromJson(jsonPoliticos, new TypeToken<ArrayList<Politico>>() {
				}.getType());
				RankingAdapter adapter = new RankingAdapter(context, R.layout.listview_item_ranking, politicos);
				lv.setAdapter(adapter);
				lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Politico politico = (Politico) parent.getItemAtPosition(position);
						if (politico != null) {
							Bundle sendBundle = new Bundle();
							sendBundle.putInt("idCadastro", politico.getIdCadastro());
							Intent i = new Intent(RankingFactoryActivity.this, FichaDeputado.class);
							i.putExtras(sendBundle);
							startActivity(i);
						}
					}
				});
				lv.setVisibility(View.VISIBLE);
			}
			pbar.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ranking_factory, menu);
		return true;
	}

}
