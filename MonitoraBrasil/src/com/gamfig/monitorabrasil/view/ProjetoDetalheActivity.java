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
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.DAO.DeputadoDAO;
import com.gamfig.monitorabrasil.DAO.UserDAO;
import com.gamfig.monitorabrasil.classes.Projeto;
import com.gamfig.monitorabrasil.dialog.AlertDialogManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ProjetoDetalheActivity extends Activity {
	String idProjeto;
	Projeto jsonProjeto;
	ProgressDialog dialog;
	boolean isChecked;
	private Session currentSession;
	Activity mActivity;
	private float notaProjeto;
	private RatingBar ratingBar;
	private TextView txtRatingValue;
	private String idUser;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_projeto_detalhe);

		Bundle receiveBundle = this.getIntent().getExtras();

		idProjeto = String.valueOf(receiveBundle.getInt("idProjeto"));
		new BuscaProjeto(this).execute();
		// verifica se está salvo nos favoritos
		if (isInFavoritos()) {
			CheckBox checkBox = (CheckBox) findViewById(R.id.star);
			checkBox.setChecked(true);
		}
		// verifica se avaliou
		float nota = getAvaliacao();
		if (nota >= 0) {
			ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
			ratingBar.setRating(nota);
			txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);
			txtRatingValue.setText(String.valueOf(nota));
		}
		addListenerOnRatingBar();
	}

	private float getAvaliacao() {

		HashMap<Integer, Float> avaliacaoProjeto = new HashMap<Integer, Float>();
		// inserir no sharedPref o array de favoritos o idCadastro
		// do politico
		Gson gson = new Gson();
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.id_key_preferencias), MODE_PRIVATE);
		avaliacaoProjeto = gson.fromJson(sharedPref.getString(getString(R.string.id_key_avaliacao_projetos), null),
				new TypeToken<HashMap<Integer, Float>>() {
				}.getType());
		if (avaliacaoProjeto == null) {
			return -1;
		}

		if (avaliacaoProjeto.get(Integer.valueOf(idProjeto)) != null)
			return avaliacaoProjeto.get(Integer.valueOf(idProjeto));
		else
			return -1;

	}

	private void addListenerOnRatingBar() {
		ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
		txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);

		// if rating value is changed,
		// display the current rating value in the result (textview)
		// automatically
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				// verifica se está logado no facebook

				if (new UserDAO().isConnect()) {
					txtRatingValue.setText(String.valueOf(rating));
					// salvar a avaliacao no sharedpreferences e no servidor
					SharedPreferences sharedPref = getSharedPreferences(getString(R.string.id_key_preferencias),
							MODE_PRIVATE);
					idUser = sharedPref.getString(getString(R.string.id_key_idcadastro), null);
					// grava no sharedPreferences
					notaProjeto = rating;
					gravaAvaliacaoProjeto(rating);

				} else {
					alert.showAlertDialog(ProjetoDetalheActivity.this, "Faça login no Facebook",
							"Para votar é necessário logar no Facebook.", false);
				}

			}

			private void gravaAvaliacaoProjeto(float rating) {

				Gson gson = new Gson();
				HashMap<String, Float> avaliacaoProjeto = new HashMap<String, Float>();
				// inserir no sharedPref o array de favoritos o idCadastro
				// do politico

				SharedPreferences sharedPref = getSharedPreferences(getString(R.string.id_key_preferencias),
						MODE_PRIVATE);
				avaliacaoProjeto = gson.fromJson(
						sharedPref.getString(getString(R.string.id_key_avaliacao_projetos), null),
						new TypeToken<HashMap<String, Float>>() {
						}.getType());
				if (avaliacaoProjeto == null) {
					avaliacaoProjeto = new HashMap<String, Float>();
				}
				avaliacaoProjeto.put(idProjeto, rating);

				// salva o conjunto
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(getString(R.string.id_key_avaliacao_projetos), gson.toJson(avaliacaoProjeto));
				editor.commit();

				new avaliaProjeto().execute();

			}
		});

	}

	public class avaliaProjeto extends AsyncTask<Void, Void, String> {

		public avaliaProjeto() {
		}

		@Override
		protected String doInBackground(Void... params) {

			new DeputadoDAO().avaliaProjeto(idUser, idProjeto, notaProjeto);

			return null;
		}

		protected void onPostExecute(String results) {

			;
		}
	}

	private boolean isInFavoritos() {
		Gson gson = new Gson();
		List<String> projetosFav = new ArrayList<String>();

		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.id_key_preferencias), MODE_PRIVATE);
		projetosFav = gson.fromJson(sharedPref.getString(getString(R.string.id_key_projetos_favoritos), null),
				new TypeToken<ArrayList<String>>() {
				}.getType());
		if (projetosFav == null) {
			return false;
		} else {
			return projetosFav.contains(String.valueOf(idProjeto));
		}
	}

	public void favIconClick(View view) {
		// verificar se está logado no Facebook ou Twitter
		currentSession = Session.getActiveSession();
		mActivity = this;
		// busca a estrela
		CheckBox checkBox = (CheckBox) findViewById(R.id.star);
		isChecked = checkBox.isChecked();
		if (currentSession != null && currentSession.isOpened()) {
			Request request = Request.newMeRequest(currentSession, new Request.GraphUserCallback() {
				@Override
				public void onCompleted(GraphUser me, Response response) {
					// inserir em favoritos

					new marcaFavorito(me).execute();
					garavaSharedPreference();
					if (isChecked) {
						Toast.makeText(mActivity, "Monitorar projeto!", Toast.LENGTH_LONG).show();
					} else {

						Toast.makeText(mActivity, "Projeto excluído da lista!", Toast.LENGTH_LONG).show();
					}
				}

				private void garavaSharedPreference() {
					Gson gson = new Gson();
					List<String> projetosFav = new ArrayList<String>();
					// inserir no sharedPref o array de favoritos o idCadastro
					// do politico

					SharedPreferences sharedPref = getSharedPreferences(getString(R.string.id_key_preferencias),
							MODE_PRIVATE);
					projetosFav = gson.fromJson(
							sharedPref.getString(getString(R.string.id_key_projetos_favoritos), null),
							new TypeToken<ArrayList<String>>() {
							}.getType());
					if (projetosFav == null) {
						projetosFav = new ArrayList<String>();
					}
					if (isChecked)
						projetosFav.add(idProjeto);
					else
						projetosFav.remove(projetosFav.indexOf(idProjeto));

					// salva o conjunto
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putString(getString(R.string.id_key_projetos_favoritos), gson.toJson(projetosFav));
					editor.commit();

				}
			});
			request.executeAsync();
		} else {
			alert.showAlertDialog(ProjetoDetalheActivity.this, "Faça login no Facebook",
					"Para adicionar projeto como favorito é necessário logar no Facebook.", false);
		}
	}

	public class marcaFavorito extends AsyncTask<Void, Void, String> {
		private GraphUser me;

		public marcaFavorito(GraphUser me) {
			this.me = me;
		}

		@Override
		protected String doInBackground(Void... params) {

			// envia informacao para o servidor do user
			String acao;
			if (isChecked) {
				acao = "add";
			} else {
				acao = "del";
			}
			new DeputadoDAO().marcaProjetoFavorito(me.getId(), idProjeto, acao);

			return me.getName();
		}

		protected void onPostExecute(String results) {

			;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.projeto_detalhe, menu);
		return true;
	}

	public class BuscaProjeto extends AsyncTask<Void, Void, String> {
		private Activity mActivity;

		public BuscaProjeto(Activity activity) {
			mActivity = activity;
		}

		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(Void... params) {

			jsonProjeto = new DeputadoDAO().buscaProjeto(idProjeto);

			return new Gson().toJson(jsonProjeto);
		}

		protected void onPostExecute(String results) {

			if (results != null) {

				// montar formulario com os dados recebidos

				TextView tx = (TextView) findViewById(R.id.txtNome);
				tx.setText(jsonProjeto.getNome());
				tx = (TextView) findViewById(R.id.txtEmenta);
				tx.setText(jsonProjeto.getEmenta());
				tx = (TextView) findViewById(R.id.txtLinkInteiroTeor);
				tx.setText(jsonProjeto.getLink());
				tx.setMovementMethod(LinkMovementMethod.getInstance());
				tx = (TextView) findViewById(R.id.txtSituacao);
				tx.setText(jsonProjeto.getSituacao());

				tx = (TextView) findViewById(R.id.txtDataUltDespacho);
				tx.setText(jsonProjeto.getDtUltimoDespacho());

				tx = (TextView) findViewById(R.id.txtUltDespacho);
				tx.setText(jsonProjeto.getUltimoDespacho());

				tx = (TextView) findViewById(R.id.txtTipoProp);
				tx.setText(jsonProjeto.getTipoProposicao());

			}
			ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar1);
			pbar.setVisibility(View.INVISIBLE);
		}
	}

}
