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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.NavUtils;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.DAO.DeputadoDAO;
import com.gamfig.monitorabrasil.DAO.UserDAO;
import com.gamfig.monitorabrasil.classes.Politico;
import com.gamfig.monitorabrasil.dialog.AlertDialogManager;
import com.gamfig.monitorabrasil.view.tabs.DoacoesActivity;
import com.gamfig.monitorabrasil.view.tabs.PresencaActivity;
import com.gamfig.monitorabrasil.view.tabs.ProjetosActivity;
import com.gamfig.monitorabrasil.view.tabs.TwitterActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@SuppressLint("UseSparseArrays")
public class FichaDeputado extends TabActivity {
	private Session currentSession;
	String jsonPolitico;
	public static Politico politico;
	Context context = null;
	int idCadastro;
	ProgressDialog dialog;
	private TabHost mTabHost;
	private TextView txtRatingValue;
	Activity mActivity;
	boolean isChecked;
	private float notaPolitico;
	private RatingBar ratingBar;
	private String idUser;
	AlertDialogManager alert = new AlertDialogManager();
	ProgressBar pbar;

	// TODO arrumar icones para tabs

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ficha_deputado);
		// Show the Up button in the action bar.
		setupActionBar();
		//busca pbar
		pbar = (ProgressBar)findViewById(R.id.progressBar1);

		mTabHost = getTabHost();// (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setVisibility(View.INVISIBLE);
		context = this;
		Bundle receiveBundle = this.getIntent().getExtras();
		if (receiveBundle != null) {
			idCadastro = receiveBundle.getInt("idCadastro");
		}

		new carregaDeputado(this).execute();

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

	public void mudaTopo(View view) {

		RelativeLayout ll1 = (RelativeLayout) findViewById(R.id.layTopo1);
		RelativeLayout ll2 = (RelativeLayout) findViewById(R.id.layTopo2);
		Button btn = (Button)findViewById(R.id.btnDeputados);
		if (ll1.getVisibility() == View.GONE) {
			ll1.setVisibility(View.VISIBLE);
			ll2.setVisibility(View.VISIBLE);
			btn.setBackgroundResource(R.drawable.ic_action_collapse);
		} else {
			ll1.setVisibility(View.GONE);
			ll2.setVisibility(View.GONE);
			btn.setBackgroundResource(R.drawable.ic_action_expand);
		}
	}

	private float getAvaliacao() {

		HashMap<Integer, Float> avaliacaoPolitico = new HashMap<Integer, Float>();
		// inserir no sharedPref o array de favoritos o idCadastro
		// do politico
		Gson gson = new Gson();
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.id_key_preferencias),
				MODE_PRIVATE);
		avaliacaoPolitico = gson.fromJson(sharedPref.getString(getString(R.string.id_key_avaliacao_politicos), null),
				new TypeToken<HashMap<Integer, Float>>() {
				}.getType());
		if (avaliacaoPolitico == null) {
			return -1;
		}
		if (avaliacaoPolitico.get(idCadastro) != null)
			return avaliacaoPolitico.get(idCadastro);
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
					notaPolitico = rating;
					gravaAvaliacaoPolitico(rating);

				} else {
					alert.showAlertDialog(FichaDeputado.this, "Faça login no Facebook",
							"Para votar é necessário logar no Facebook.", false);
				}

			}

			private void gravaAvaliacaoPolitico(float rating) {

				Gson gson = new Gson();
				HashMap<Integer, Float> avaliacaoPolitico = new HashMap<Integer, Float>();
				// inserir no sharedPref o array de favoritos o idCadastro
				// do politico

				SharedPreferences sharedPref = getSharedPreferences(getString(R.string.id_key_preferencias),
						MODE_PRIVATE);
				avaliacaoPolitico = gson.fromJson(
						sharedPref.getString(getString(R.string.id_key_avaliacao_politicos), null),
						new TypeToken<HashMap<Integer, Float>>() {
						}.getType());
				if (avaliacaoPolitico == null) {
					avaliacaoPolitico = new HashMap<Integer, Float>();
				}
				avaliacaoPolitico.put(idCadastro, rating);

				// salva o conjunto
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(getString(R.string.id_key_avaliacao_politicos), gson.toJson(avaliacaoPolitico));
				editor.commit();

				new avaliaPolitico().execute();

			}
		});

	}

	// verifica se está nos favoritos
	private boolean isInFavoritos() {
		Gson gson = new Gson();
		List<String> politicosFav = new ArrayList<String>();

		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.id_key_preferencias), MODE_PRIVATE);
		politicosFav = gson.fromJson(sharedPref.getString(getString(R.string.id_key_favoritos), null),
				new TypeToken<ArrayList<String>>() {
				}.getType());
		if (politicosFav == null) {
			return false;
		} else {
			return politicosFav.contains(String.valueOf(idCadastro));
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

					SharedPreferences sharedPref = getSharedPreferences(getString(R.string.id_key_preferencias),
							MODE_PRIVATE);
					idUser = sharedPref.getString(getString(R.string.id_key_idcadastro), null);

					new marcaFavorito().execute();
					garavaSharedPreference();
					if (isChecked) {
						Toast.makeText(mActivity, "Monitorar deputado!", Toast.LENGTH_LONG).show();
					} else {

						Toast.makeText(mActivity, "Deputado excluído da lista!", Toast.LENGTH_LONG).show();
					}
				}

				private void garavaSharedPreference() {
					Gson gson = new Gson();
					List<String> politicosFav = new ArrayList<String>();
					// inserir no sharedPref o array de favoritos o idCadastro
					// do politico

					SharedPreferences sharedPref = getSharedPreferences(getString(R.string.id_key_preferencias),
							MODE_PRIVATE);
					politicosFav = gson.fromJson(sharedPref.getString(getString(R.string.id_key_favoritos), null),
							new TypeToken<ArrayList<String>>() {
							}.getType());
					if (politicosFav == null) {
						politicosFav = new ArrayList<String>();
					}
					if (isChecked)
						politicosFav.add(String.valueOf(idCadastro));
					else
						politicosFav.remove(politicosFav.indexOf(String.valueOf(idCadastro)));

					// salva o conjunto
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putString(getString(R.string.id_key_favoritos), gson.toJson(politicosFav));
					editor.commit();

				}
			});
			request.executeAsync();
		} else {
			// TODO fazer um dialog para informar isso aqui
			// custom dialog

			alert.showAlertDialog(FichaDeputado.this, "Faça login no Facebook",
					"Para salvar Parlamentar como favorito é necessário logar no Facebook.", false);
		}
	}

	public class avaliaPolitico extends AsyncTask<Void, Void, String> {

		public avaliaPolitico() {
		}

		@Override
		protected String doInBackground(Void... params) {

			new DeputadoDAO().avaliaPolitico(idUser, idCadastro, notaPolitico);

			return null;
		}

		protected void onPostExecute(String results) {

			;
		}
	}

	public class marcaFavorito extends AsyncTask<Void, Void, String> {

		public marcaFavorito() {

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
			new DeputadoDAO().marcaFavorito(idUser, idCadastro, acao);

			return null;
		}

		protected void onPostExecute(String results) {

			;
		}
	}

	public class carregaDeputado extends AsyncTask<Void, Void, String> {
		private Activity mActivity;
		

		public carregaDeputado(Activity activity) {
			mActivity = activity;
		}

		protected void onPreExecute() {
			
		}

		@Override
		protected String doInBackground(Void... params) {

			new DeputadoDAO();
			politico = DeputadoDAO.buscaPolitico(idCadastro);

			return politico.getNome();
		}

		protected void onPostExecute(String results) {

			if (politico != null) {
				// popula dados do deputado
				TextView txt = (TextView) findViewById(R.id.txtNomePerfil);
				txt.setText(politico.getNome());
				txt = (TextView) findViewById(R.id.txtEmailPerfil);
				txt.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
				txt.setText(politico.getEmail());
				txt = (TextView) findViewById(R.id.txtPartidoPerfil);
				txt.setText(politico.getPartido().getSigla() + "-" + politico.getUf());
				if(politico.getLider().length() > 0){
					txt.setText(txt.getText()+" ("+politico.getLider()+")");
				}
				txt = (TextView) findViewById(R.id.txtAnexoPerfil);
				txt.setText(" " + politico.getAnexo());
				txt = (TextView) findViewById(R.id.txtTelefonePerfil);
				txt.setText(" 61 "+politico.getTelefone() );
				Linkify.addLinks(txt  , Linkify.PHONE_NUMBERS);
				txt = (TextView) findViewById(R.id.txtGabinetePerfil);
				txt.setText(" " + String.valueOf(politico.getGabinete()));
				URL thumb_u;
				try {
					thumb_u = new URL("http://www.camara.gov.br/internet/deputado/bandep/" + idCadastro + ".jpg");
					Drawable thumb_d;
					thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
					ImageView foto = (ImageView) findViewById(R.id.imgTwitter);
					foto.setImageDrawable(thumb_d);
				} catch (MalformedURLException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}
				mTabHost.setup();
				// mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
				setupTab(new TextView(context), "Presença", new Intent(context, PresencaActivity.class));
				setupTab(new TextView(context), "Projetos", new Intent(context, ProjetosActivity.class));
				setupTab(new TextView(context), "Twitter", new Intent(context, TwitterActivity.class));
				setupTab(new TextView(context), "Doações Recebidas", new Intent(context, DoacoesActivity.class));

			}
			mTabHost.setVisibility(View.VISIBLE);
			pbar.setVisibility(View.INVISIBLE);
		}

		private void setupTab(final View view, final String tag, Intent intent) {
			View tabview = createTabView(mTabHost.getContext(), tag);

			TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(new TabContentFactory() {
				public View createTabContent(String tag) {
					return view;
				}
			});
			setContent.setContent(intent);
			mTabHost.addTab(setContent);

		}

		private View createTabView(Context context, String text) {
			View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
			TextView tv = (TextView) view.findViewById(R.id.tabsText);
			tv.setText(text);
			return view;
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ficha_deputado, menu);
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
