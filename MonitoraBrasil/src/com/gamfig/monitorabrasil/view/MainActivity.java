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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.gamfig.monitorabrasil.ClientGCM;
import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.SignInActivity;
import com.gamfig.monitorabrasil.DAO.DeputadoDAO;
import com.gamfig.monitorabrasil.classes.AvaliacaoPolitico;
import com.gamfig.monitorabrasil.classes.AvaliacaoProjeto;
import com.gamfig.monitorabrasil.classes.ConnectionDetector;
import com.gamfig.monitorabrasil.classes.Politico;
import com.gamfig.monitorabrasil.classes.Projeto;
import com.gamfig.monitorabrasil.classes.User;
import com.gamfig.monitorabrasil.dialog.AlertDialogManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends Activity {

	private Session currentSession;
	// Internet Connection detector
	private ConnectionDetector cd;
	private String registrationId;
	private Boolean jaEnviouGCMID;
	private ShareActionProvider mShareActionProvider;

	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();
	public static String nomeFace = "";
	public static String idFacebook = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// nome da versao
		TextView lblVersionName = (TextView) findViewById(R.id.lblVersionName);
		try {
			lblVersionName.setText(this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(MainActivity.this, "Sem conexão de Internet",
					"Por favor conecte a uma rede com acesso a Internet", false);
			// stop executing code by return
			return;
		}

		// cadastrar o GCMID
		ClientGCM clientGCM = new ClientGCM(this);
		SharedPreferences prefs = clientGCM.getGCMPreferences();
		registrationId = prefs.getString("registration_id", "");
		jaEnviouGCMID = prefs.getBoolean("ja_enviou_gcmid", false);
		if (registrationId.isEmpty()) {
			// faz o primeiro registro
			clientGCM.registra();
		} else {
			if (!jaEnviouGCMID) {
				SharedPreferences sharedPref = getSharedPreferences(getString(R.string.id_key_preferencias),
						MODE_PRIVATE);
				String id = sharedPref.getString(getString(R.string.id_key_idcadastro), "");
				if (!id.isEmpty()) {

					new enviaUser(null, registrationId, Integer.valueOf(id)).execute();
					// atualizar o controle
					jaEnviouGCMID = true;
					SharedPreferences.Editor editor = prefs.edit();
					editor.putBoolean("ja_enviou_gcmid", jaEnviouGCMID);
					editor.commit();
				}
			}
		}

		LoginButton btnFace = (LoginButton) findViewById(R.id.loginFacebook);

		btnFace.setReadPermissions(Arrays.asList("user_location", "user_hometown", "user_education_history", "email",
				"user_about_me", "user_birthday"));
		if (savedInstanceState == null) {
			btnFace.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
				@Override
				public void onUserInfoFetched(GraphUser user) {
					fetchUserInfo();
				}
			});
		}

	}

	private void fetchUserInfo() {
		currentSession = Session.getActiveSession();
		if (currentSession != null && currentSession.isOpened()) {
			Request request = Request.newMeRequest(currentSession, new Request.GraphUserCallback() {
				@Override
				public void onCompleted(GraphUser me, Response response) {
					if (response.getRequest().getSession() == currentSession) {
						TextView txt = (TextView) findViewById(R.id.txtAutor2);
						nomeFace = me.getName();
						txt.setText("Olá, " + nomeFace);
						idFacebook = me.getId();
						ProfilePictureView profilePic = (ProfilePictureView) findViewById(R.id.profilepic2);
						profilePic.setProfileId(idFacebook);
						if (!jaCadastrou()) {

							new enviaUser(me, registrationId, 0).execute();
						}
					}
				}

				private boolean jaCadastrou() {
					SharedPreferences sharedPref = getSharedPreferences(getString(R.string.id_key_preferencias),
							MODE_PRIVATE);
					String id = sharedPref.getString(getString(R.string.id_key_idcadastro), null);
					if (id == null) {
						return false;
					} else {
						if (id.equals("null"))
							return false;
						Integer idCadastro = Integer.valueOf(id);
						if (idCadastro > -1)
							return true;
					}

					return false;
				}
			});
			request.executeAsync();
		} else {
			TextView txt = (TextView) findViewById(R.id.txtAutor2);
			txt.setText("Olá visitante");
		}
	}

	public class enviaUser extends AsyncTask<Void, Void, User> {
		private GraphUser me;
		private ProgressDialog mDialog;
		private String registrationId;
		private int idUser;

		public enviaUser(GraphUser me, String gcmid, int idUser) {
			this.me = me;
			this.registrationId = gcmid;
			this.idUser = idUser;
		}

		@Override
		protected User doInBackground(Void... params) {

			// envia informacao para o servidor do user
			// retorna o id do usuário
			// se tiver nao precisa mais
			if (idUser > 0) {
				return new DeputadoDAO().atualizaUser(idUser, this.registrationId);
			} else {

				return new DeputadoDAO().enviaUser(me.getInnerJSONObject(), this.registrationId);
			}

		}

		protected void onPostExecute(User user) {

			// inserir no SharedPreferences
			Gson gson = new Gson();

			SharedPreferences sharedPref = getSharedPreferences(getString(R.string.id_key_preferencias), MODE_PRIVATE);
			Integer idCadastro = gson.fromJson(sharedPref.getString(getString(R.string.id_key_idcadastro), null),
					new TypeToken<Integer>() {
					}.getType());
			if (idCadastro == null) {

				// salva o id
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(getString(R.string.id_key_idcadastro), String.valueOf(user.getId()));

				// salvar politicos favoritos
				List<String> politicosFav = new ArrayList<String>();
				if (user.getPoliticos() != null) {
					for (Politico politico : user.getPoliticos()) {
						politicosFav.add(String.valueOf(politico.getIdCadastro()));
					}
				}
				editor.putString(getString(R.string.id_key_favoritos), gson.toJson(politicosFav));

				// salvar projetos favoritos
				List<String> projetosFav = new ArrayList<String>();
				if (user.getProjetos() != null) {
					for (Projeto projeto : user.getProjetos()) {
						projetosFav.add(String.valueOf(projeto.getId()));
					}
				}
				editor.putString(getString(R.string.id_key_projetos_favoritos), gson.toJson(projetosFav));

				// salvar as avaliacoes de politicos
				HashMap<Integer, Float> avaliacaoPolitico = new HashMap<Integer, Float>();
				if (user.getAvaliacaoPolitico() != null) {
					for (AvaliacaoPolitico avalPolitico : user.getAvaliacaoPolitico()) {
						avaliacaoPolitico.put(avalPolitico.getIdPolitico(), avalPolitico.getNotaAvaliacao());

					}
				}
				editor.putString(getString(R.string.id_key_avaliacao_politicos), gson.toJson(avaliacaoPolitico));

				// salvar as avaliacoes de projetos
				HashMap<String, Float> avaliacaoProjeto = new HashMap<String, Float>();
				if (user.getAvaliacaoProjeto() != null) {
					for (AvaliacaoProjeto avalProjeto : user.getAvaliacaoProjeto()) {
						avaliacaoProjeto
								.put(String.valueOf(avalProjeto.getIdProjeto()), avalProjeto.getNotaAvaliacao());
					}
				}

				editor.putString(getString(R.string.id_key_avaliacao_projetos), gson.toJson(avaliacaoProjeto));

				editor.commit();

			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

	public void abrirDeputados(View v) {
		Bundle sendBundle = new Bundle();
		sendBundle.putString("tipoLista", "1");
		Intent intent = new Intent(this, ProcuraDeputado.class);
		intent.putExtras(sendBundle);
		startActivity(intent);
	}

	public void abrirDeputadosFavoritos(View v) {
		// Check if Internet present
		if (!cd.isConnectingToFacebook()) {
			// Internet Connection is not present
			alert.showAlertDialog(MainActivity.this, "Faça login no Facebook",
					"Para usar Deputados favoritos é necessário logar no Facebook.", false);
			// stop executing code by return
			return;
		}
		Bundle sendBundle = new Bundle();
		sendBundle.putString("tipoLista", "2");
		Intent intent = new Intent(this, ProcuraDeputado.class);
		intent.putExtras(sendBundle);
		startActivity(intent);
	}

	public void abreTwitterLista(View v) {
		Intent intent = new Intent(this, TwitterListaActivity.class);
		startActivity(intent);
	}
	
	public void abreCota(View v) {
		Intent intent = new Intent(this, CotasActivity.class);
		startActivity(intent);
	}

	public void abrirRanking(View v) {
		Intent intent = new Intent(this, RankingActivity.class);
		startActivity(intent);
	}

	public void compartilhar() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
	    shareIntent.setType("text/plain");
	    shareIntent.putExtra(Intent.EXTRA_TEXT, "App para monitorar os Deputados Federais  Android: https://play.google.com/store/apps/details?id=com.gamfig.monitorabrasil https://www.facebook.com/monitorabrasilapp #monitoraBrasil");
	    startActivity(Intent.createChooser(shareIntent, "Compartilhe!"));
	}

	public void enviarSugestao() {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_EMAIL, new String[] { "fale@monitorabrasil.com" });
		i.putExtra(Intent.EXTRA_SUBJECT, "Sugestão");
		i.putExtra(Intent.EXTRA_TEXT, "");
		try {
			startActivity(Intent.createChooser(i, "Enviar email..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(MainActivity.this, "Não há email configurado no dispositivo.", Toast.LENGTH_SHORT).show();
		}
	}

	public void abrirProjetosFavoritos(View v) {

		Intent intent = new Intent(this, ListaProjetosActivity.class);
		startActivity(intent);
	}

	public void loginGPlus(View v) {

		Intent intent = new Intent(this, SignInActivity.class);
		startActivity(intent);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// Call to update the share intent
	private void setShareIntent(Intent shareIntent) {
		if (mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(shareIntent);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent launchNewIntent = new Intent(MainActivity.this, SobreActivity.class);
			startActivityForResult(launchNewIntent, 0);
			return true;

		case R.id.action_fale:
			enviarSugestao();
			return true;
		case R.id.action_compartilhe:
			compartilhar();
			return true;	

		case R.id.action_enviar_email_grupo:
			Intent intent = new Intent(MainActivity.this, EmailActivity.class);
			startActivity(intent);
			return true;

		}
		return false;
	}

}
