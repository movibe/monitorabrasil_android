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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.adapter.TwitterAdapter;
import com.gamfig.monitorabrasil.classes.Pojo;
import com.gamfig.monitorabrasil.classes.StatusTwitter;

public class TwitterListaActivity extends Activity {

	static String TWITTER_CONSUMER_KEY = ""; // coloque o seu
	static String TWITTER_CONSUMER_SECRET = ""; // coloque o seu
																							

	static String TWITTER_ACCESS_TOKEN = "";// coloque o seu
	static String TWITTER_ACCESS_TOKEN_SECRET = "";// coloque o seu

	private Activity minhaActivity;
	List<StatusTwitter> statusTwitter;
	List<StatusTwitter> statusTwitterHT;
	private ProgressBar pgBar;
	private ProgressBar pgBar2;
	private ImageButton btnAtualizarLista;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter_lista);
		// Show the Up button in the action bar.
		setupActionBar();
		minhaActivity = this;
		new carregaTwitter(this, "inicio").execute();
		ImageButton tweet = (ImageButton) findViewById(R.id.btnEmail);
		tweet.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendTwitter("");
			}
		});

		btnAtualizarLista = (ImageButton) findViewById(R.id.btnReclameTwitter);
		btnAtualizarLista.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new carregaTwitter(minhaActivity, "atualiza").execute();
			}
		});
		pgBar = (ProgressBar) findViewById(R.id.progressBar1);
		pgBar2 = (ProgressBar) findViewById(R.id.progressBar2);
	}

	public void sendTwitter(String id) {

		Intent i = findTwitterClient();
		i.putExtra(Intent.EXTRA_TEXT, id+" #MonitoraBrasil ");
		try {
			startActivity(Intent.createChooser(i, "Enviar Twitter..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(TwitterListaActivity.this, "Não há twitter configurado no dipositivo.", Toast.LENGTH_SHORT)
					.show();
		}

	}

	public class carregaTwitter extends AsyncTask<Void, Void, String> {
		private Activity mActivity;
		private String acao;

		public carregaTwitter(Activity activity, String string) {
			mActivity = activity;
			acao = string;
		}

		protected void onPreExecute() {
			if (acao.equals("inicio")) {
				pgBar2 = (ProgressBar) findViewById(R.id.progressBar2);
				pgBar2.setVisibility(View.VISIBLE);
			} else {
				pgBar.setVisibility(View.VISIBLE);
				btnAtualizarLista.setVisibility(View.GONE);
			}
		}

		public class CustomComparator implements Comparator<StatusTwitter> {
			@SuppressLint("DefaultLocale")
			@Override
			public int compare(StatusTwitter e1, StatusTwitter e2) {
				return e2.getData().compareTo(e1.getData());
			}
		}

		@Override
		protected String doInBackground(Void... params) {

			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
			AccessToken accessToken = new AccessToken(TWITTER_ACCESS_TOKEN, TWITTER_ACCESS_TOKEN_SECRET);

			twitter.setOAuthAccessToken(accessToken);
			try {

				Paging paging = new Paging(1, 20);
				List<twitter4j.Status> statuses = twitter.getUserListStatuses("monitorabrasil", "deputados-federais",
						paging);
				if (acao.equals("inicio")) {
					statusTwitter = new ArrayList<StatusTwitter>();
					statusTwitterHT = new ArrayList<StatusTwitter>();
				}

				Bitmap imagem;
				boolean blControle = false;
				for (twitter4j.Status status : statuses) {
					if (acao.equals("inicio")) {
						imagem = Pojo.getImageBitmap(status.getUser().getProfileImageURL());
						statusTwitter.add(new StatusTwitter(status.getUser(), status.getCreatedAt(), status.getText(),
								imagem));
					} else {
						if (status.getCreatedAt().equals(statusTwitter.get(0).getData()) && !blControle) {
							// status = status.getRetweetedStatus();
							blControle = true;

						}
						if (!blControle) {
							imagem = Pojo.getImageBitmap(status.getUser().getProfileImageURL());
							statusTwitter.add(new StatusTwitter(status.getUser(), status.getCreatedAt(), status
									.getText(), imagem));
						}
					}

				}
				// buscar os status com #MonitoraBrasil
				Query query = new Query("#MonitoraBrasil");
				QueryResult result = twitter.search(query);
				statuses = result.getTweets();
				blControle = false;
				for (twitter4j.Status status : statuses) {
					if (acao.equals("inicio")) {
						imagem = Pojo.getImageBitmap(status.getUser().getProfileImageURL());
						StatusTwitter tsT = new StatusTwitter(status.getUser(), status.getCreatedAt(),
								status.getText(), imagem);
						statusTwitter.add(tsT);
						statusTwitterHT.add(tsT);
					} else {

					}
					if (status.getCreatedAt().equals(statusTwitterHT.get(0).getData()) && !blControle) {
						// status = status.getRetweetedStatus();
						blControle = true;

					}
					if (!blControle) {
						imagem = Pojo.getImageBitmap(status.getUser().getProfileImageURL());
						StatusTwitter tsT = new StatusTwitter(status.getUser(), status.getCreatedAt(),
								status.getText(), imagem);

						statusTwitter.add(tsT);
						statusTwitterHT.add(tsT);
					}
				}
				// ordenar
				Collections.sort(statusTwitter, new CustomComparator());
				// statusTwitterNovo.addAll(statusTwitter);
				// statusTwitter = statusTwitterNovo;

			} catch (TwitterException e) {

				e.printStackTrace();
			}

			return "ok";
		}

		protected void onPostExecute(String results) {

			// pega a listiview
			ListView lv = (ListView) findViewById(R.id.listView1);
			// monta a listview
			TwitterAdapter adapter = new TwitterAdapter(minhaActivity, R.layout.listview_item_twitter, statusTwitter);
			lv.setAdapter(adapter);
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					StatusTwitter st = (StatusTwitter) parent.getItemAtPosition(position);
					sendTwitter(st.getId());
				}
			});
			if (acao.equals("inicio")) {

				pgBar2.setVisibility(View.GONE);
			} else {
				pgBar.setVisibility(View.GONE);
				btnAtualizarLista.setVisibility(View.VISIBLE);
			}
		}
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
		// getMenuInflater().inflate(R.menu.twitter_lista, menu);
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
