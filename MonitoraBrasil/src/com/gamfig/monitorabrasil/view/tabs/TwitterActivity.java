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

import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.adapter.TwitterAdapter;
import com.gamfig.monitorabrasil.classes.Pojo;
import com.gamfig.monitorabrasil.classes.StatusTwitter;
import com.gamfig.monitorabrasil.view.FichaDeputado;

public class TwitterActivity extends FragmentActivity {

	static String TWITTER_CONSUMER_KEY = ""; // coloque o seu
	static String TWITTER_CONSUMER_SECRET = ""; // coloque o seu
																							

	static String TWITTER_ACCESS_TOKEN = "";// coloque o seu
	static String TWITTER_ACCESS_TOKEN_SECRET = "";// coloque o seu;

	// Preference Constants
	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

	static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

	// Twitter oauth urls
	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

	private Activity minhaActivity;
	private View mListTwitter;
	private View mFormulario;
	private String email;
	public String userPolitico;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter);
		minhaActivity = this;
		mListTwitter = findViewById(R.id.layListaTwitter);
		mFormulario = findViewById(R.id.layFormularioTwitter);
		email = FichaDeputado.politico.getEmail();
		new carregaTwitter(this, FichaDeputado.politico.getTwitter()).execute();
		Button tweet = (Button) findViewById(R.id.btnTwitterT);
		tweet.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendTwitter();
				// new
				// MyDialogFragment(TwitterActivity.this).show(getSupportFragmentManager(),
				// "MyDialog");
			}
		});

	}

	public Intent findTwitterClient() {
		final String[] twitterApps = {
				// package // name - nb installs (thousands)
				"com.twitter.android", // official - 10 000
				"com.twidroid", // twidroyd - 5 000
				"com.handmark.tweetcaster", // Tweecaster - 5 000
				"com.thedeck.android" // TweetDeck - 5 000
		};
		Intent tweetIntent = new Intent();
		tweetIntent.setType("text/plain");
		final PackageManager packageManager = getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

		for (int i = 0; i < twitterApps.length; i++) {
			for (ResolveInfo resolveInfo : list) {
				String p = resolveInfo.activityInfo.packageName;
				if (p != null && p.startsWith(twitterApps[i])) {
					tweetIntent.setPackage(p);
					return tweetIntent;
				}
			}
		}
		return null;
	}

	public void sendTwitter() {

		Intent i = findTwitterClient();
		i.putExtra(Intent.EXTRA_TEXT, "@" + userPolitico + " #MonitoraBrasil ");
		try {
			startActivity(Intent.createChooser(i, "Enviar Twitter..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(TwitterActivity.this, "Não há twitter configurado no dipositivo.", Toast.LENGTH_SHORT)
					.show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.twitter, menu);
		return true;
	}

	public class carregaTwitter extends AsyncTask<Void, Void, String> {
		private Activity mActivity;
		private String twitterId;// id do politico
		ProgressBar pbar;
		private List<StatusTwitter> statusTwitter;

		public carregaTwitter(Activity activity, String twitterId) {
			mActivity = activity;
			this.twitterId = twitterId;
			pbar = (ProgressBar) findViewById(R.id.progressBar1);
		}

		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(Void... params) {

			// verifica se tem twitter
			if (twitterId.length() > 0) {
				Twitter twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
				AccessToken accessToken = new AccessToken(TWITTER_ACCESS_TOKEN, TWITTER_ACCESS_TOKEN_SECRET);

				twitter.setOAuthAccessToken(accessToken);
				try {

					Paging paging = new Paging(1, 20);
					List<twitter4j.Status> statuses = twitter.getUserTimeline(twitterId, paging);
					statusTwitter = new ArrayList<StatusTwitter>();
					// pega a imagem
					Bitmap imagemUser = Pojo.getImageBitmap(statuses.get(0).getUser().getProfileImageURL());
					Bitmap imagem = imagemUser;
					userPolitico = statuses.get(0).getUser().getScreenName();
					for (twitter4j.Status status : statuses) {

						// pegar a foto de quem twittou se for retwitt
						if (status.isRetweet()) {
							status = status.getRetweetedStatus();
							imagem = Pojo.getImageBitmap(status.getUser().getProfileImageURL());
						} else {
							imagem = imagemUser;
						}
						statusTwitter.add(new StatusTwitter(status.getUser(), status.getCreatedAt(), status.getText(),
								imagem));
					}

				} catch (TwitterException e) {

					e.printStackTrace();
				}
			} else {
				mFormulario.setVisibility(View.VISIBLE);
				Button btnEnviarEmail = (Button) findViewById(R.id.btnDeputados);
				btnEnviarEmail.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						sendEmail();
					}
				});

				Button btnEnviarTwitter = (Button) findViewById(R.id.btnRanking);
				btnEnviarTwitter.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						sendEmailTwitter();
					}

				});
			}

			return "ok";
		}

		public void sendEmailTwitter() {
			EditText txt = (EditText) findViewById(R.id.txtComentario);
			if (txt.getText().length() > 0) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				i.putExtra(Intent.EXTRA_EMAIL, new String[] { "fale@monitorabrasil.com" });
				i.putExtra(Intent.EXTRA_SUBJECT, "Conta no Twitter - indicação");
				i.putExtra(Intent.EXTRA_TEXT,
						"Twitter: " + txt.getText() + "\nDeputado: " + FichaDeputado.politico.getIdCadastro() + " "
								+ FichaDeputado.politico.getNome() + "\n\n");
				try {
					startActivity(Intent.createChooser(i, "Enviar email..."));
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(TwitterActivity.this, "Não há email configurado no dispositivo.", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(TwitterActivity.this, "Informe o twitter", Toast.LENGTH_SHORT).show();
			}
		}

		public void sendEmail() {
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
			i.putExtra(Intent.EXTRA_SUBJECT, "Conta no Twitter - #MonitoraBrasil");
			i.putExtra(
					Intent.EXTRA_TEXT,
					Html.fromHtml(new StringBuilder()
							.append("<p>Excelentíssimo Sr. Dep. "
									+ FichaDeputado.politico.getNome()
									+ "</p>"
									+ "<p>Vossa Excelência possui conta no Twitter?</p>"
									+ "<p>Se não possui, poderia criá-la para que possa seguí-lo.</p>"
									+ "<p>Obrigado</p><br>"
									+ "<small><p>Email enviado pelo aplicativo Monitora, Brasil!"
									+ "<br>Android - <a href='https://play.google.com/store/apps/details?id=com.gamfig.monitorabrasil'>https://play.google.com/store/apps/details?id=com.gamfig.monitorabrasil</a>"
									+ "<br>Breve para Iphone e Ipad</p></small>").toString()));
			try {
				startActivity(Intent.createChooser(i, "Enviar email..."));
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(TwitterActivity.this, "Não há email configurado no dispositivo.", Toast.LENGTH_SHORT)
						.show();
			}
		}

		protected void onPostExecute(String results) {
			if (twitterId.length() > 0) {
				// verifica se a lista de twiiter eh valida
				if (statusTwitter != null) {
					// pega a listiview
					ListView lv = (ListView) findViewById(R.id.listView1);
					// monta a listview
					TwitterAdapter adapter = new TwitterAdapter(minhaActivity, R.layout.listview_item_twitter,
							statusTwitter);
					lv.setAdapter(adapter);
				}else{
					Toast.makeText(TwitterActivity.this, "Nenhum twitter recente! Conta sem atividade", Toast.LENGTH_SHORT).show();
				}
			}
			// esconder o progressbar
			pbar.setVisibility(View.GONE);
		}
	}

}
