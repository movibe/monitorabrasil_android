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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.DAO.DeputadoDAO;
import com.gamfig.monitorabrasil.adapter.PoliticoAdapter;
import com.gamfig.monitorabrasil.classes.Politico;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class EmailActivity extends Activity {
	String jsonPoliticos;
	Context context = null;
	String partido;
	private boolean isDefaultSelectionPartido;
	private boolean isDefaultSelectionUf;
	private Activity minhaActivity;
	private String tipoListagem; // se traz todos ou so favoritos
	private String idUser;
	List<Politico> politicos = new ArrayList<Politico>();
	private ListView lv;
	PoliticoAdapter adapterPoliticos = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email);
		// Show the Up button in the action bar.
		minhaActivity = this;
		idUser = "";
		tipoListagem = "1";
		setupActionBar();
		isDefaultSelectionPartido = true;
		isDefaultSelectionUf = true;

		setupListas();
		ImageButton btn = (ImageButton) findViewById(R.id.btnEmail);
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				enviarEmail();
			}

			private void enviarEmail() {
				if(politicos.size()==0){
					Toast.makeText(minhaActivity, "Selecione algum filtro", Toast.LENGTH_SHORT)
					.show();
					return;
				}
				if(politicos.size()> 100){
					Toast.makeText(minhaActivity, "Selecione algum filtro", Toast.LENGTH_SHORT)
					.show();
					return;
				}
				String[] emails = new String[politicos.size()] ;
				int cont=0;
				for (Politico politico : politicos) {
					emails[cont]=politico.getEmail()+"@camara.leg.br";
					cont++;
				}				

				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				i.putExtra(Intent.EXTRA_EMAIL,  emails);
				i.putExtra(Intent.EXTRA_SUBJECT, "#MonitoraBrasil");
				i.putExtra(Intent.EXTRA_TEXT,
						Html.fromHtml(new StringBuilder()
					    .append("<small><p>Email enviado pelo aplicativo Monitora, Brasil!" +
					    		"<br>Android - <a href='https://play.google.com/store/apps/details?id=com.gamfig.monitorabrasil'>https://play.google.com/store/apps/details?id=com.gamfig.monitorabrasil</a>" +
					    		"<br>Breve para Iphone e Ipad</p></small>")
					    .toString()
					));
				try {
					startActivity(Intent.createChooser(i, "Enviar email..."));
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(minhaActivity, "Não há email configurado no dispositivo.", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

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
		context = this;
	}

	public class CargaDeputados extends AsyncTask<Void, Void, String> {

		private Activity mActivity;
		private ProgressDialog mDialog;
		private String query;

		public CargaDeputados(Activity activity, String q) {
			mActivity = activity;
			query = q;
		}

		protected void onPreExecute() {
			mDialog = new ProgressDialog(mActivity);
			mDialog.setMessage("Carregando...");
			mDialog.show();
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

			new DeputadoDAO();
			// parametro 1 diz que sao todos deputados
			jsonPoliticos = DeputadoDAO.connect(partido, uf, tipoListagem, query, idUser);
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

			}
			mDialog.dismiss();
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
		getMenuInflater().inflate(R.menu.email, menu);
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
