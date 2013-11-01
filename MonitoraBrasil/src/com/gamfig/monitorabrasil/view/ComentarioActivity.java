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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.DAO.DeputadoDAO;
import com.gamfig.monitorabrasil.adapter.ComentarioAdapter;
import com.gamfig.monitorabrasil.classes.Comentario;
import com.gamfig.monitorabrasil.classes.User;

public class ComentarioActivity extends Activity {

	private String idUser;
	private String idProposta;
	private String titulo;
	private Activity minhaActivity;
	ComentarioAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comentario);
		// Show the Up button in the action bar.
		setupActionBar();

		// pegar parametros
		Bundle receiveBundle = this.getIntent().getExtras();
		idUser = receiveBundle.getString("idUser");
		idProposta = receiveBundle.getString("idProposta");
		titulo = receiveBundle.getString("titulo");

		TextView txtTitulo = (TextView) findViewById(R.id.txtPropostaTitulo);
		txtTitulo.setText(titulo);
		ImageView imgComentar = (ImageView) findViewById(R.id.imgComentar);

		OnClickListener onclicklistener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// salvar comentario
				EditText txtComentario = (EditText) findViewById(R.id.txtComentario);
				String comentario = txtComentario.getText().toString();
				if (comentario.length() == 0) {
					Toast.makeText(getBaseContext(), "Informe o comentário!", Toast.LENGTH_SHORT).show();

					return;
				}
				// inserir
				new insereComentario(comentario, idProposta, idUser).execute();
				txtComentario.setText("");
				// atulizar o listview
				Comentario coment = new Comentario();
				coment.setComentario(comentario);
				User user = new User();
				// buscar nome
				user.setNome(MainActivity.nomeFace);
				// buscar idFacebook
				user.setIdFacebook(MainActivity.idFacebook);
				long now = System.currentTimeMillis();
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(now);
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
				coment.setData(formatter.format(calendar.getTime()));
				coment.setUser(user);
				if (adapter == null) {
					ArrayList<Comentario> comentarios = new ArrayList<Comentario>();
					comentarios.add(coment);
					adapter = new ComentarioAdapter(minhaActivity, R.layout.listview_item_comentario, comentarios);
					ListView lv = (ListView) findViewById(R.id.listView1);
					lv.setAdapter(adapter);
					lv.setVisibility(View.VISIBLE);
				} else {
					adapter.add(coment);

					adapter.notifyDataSetChanged();
				}

			}
		};

		if (idUser.equals("0")) {
			EditText txtComentario = (EditText) findViewById(R.id.txtComentario);
			txtComentario.setHint("Para comentar é necessário logar");
			txtComentario.setEnabled(false);
			// esconder imgComentar

			imgComentar.setVisibility(View.INVISIBLE);
		} else {
			imgComentar.setOnClickListener(onclicklistener);
		}

		// buscar comentarios
		minhaActivity = this;
		new buscaComentarios(minhaActivity).execute();
	}

	public class buscaComentarios extends AsyncTask<Void, Void, String> {
		Activity comentarioActivit;
		ArrayList<Comentario> comentarios;
		ProgressBar pbar;
		ListView lv;

		public buscaComentarios(Activity comentarioActivit) {
			this.comentarioActivit = comentarioActivit;
			pbar = (ProgressBar) findViewById(R.id.progressBar1);
			lv = (ListView) findViewById(R.id.listView1);
		}

		protected void onPreExecute() {
			pbar.setVisibility(View.VISIBLE);
			lv.setVisibility(View.INVISIBLE);
		}

		@Override
		protected String doInBackground(Void... params) {

			// buscar os projetos da lista do user
			comentarios = new DeputadoDAO().buscaComentarios(idProposta);

			return "ok";
		}

		protected void onPostExecute(String results) {

			// popula listivew com os projetos de autoria do deputado

			lv = (ListView) findViewById(R.id.listView1);
			if (comentarios != null) {

				adapter = new ComentarioAdapter(comentarioActivit, R.layout.listview_item_comentario, comentarios);
				lv.setAdapter(adapter);
				lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						
					}
				});
				lv.setVisibility(View.VISIBLE);
			}
			// esconder o progressbar
			pbar.setVisibility(View.GONE);
		}
	}

	public class insereComentario extends AsyncTask<Void, Void, String> {
		String mMensagem;
		String mIdProposta;
		String mIdUser;

		public insereComentario(String mensagem, String idProposta, String idUser) {
			mMensagem = mensagem;
			mIdProposta = idProposta;
			mIdUser = idUser;
		}

		@Override
		protected String doInBackground(Void... params) {

			new DeputadoDAO().insereComentario(idUser, idProposta, mMensagem);

			return null;
		}

		protected void onPostExecute(String results) {

			Toast.makeText(getBaseContext(), "Comentário inserido!", Toast.LENGTH_SHORT).show();
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
		getMenuInflater().inflate(R.menu.comentario, menu);
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
