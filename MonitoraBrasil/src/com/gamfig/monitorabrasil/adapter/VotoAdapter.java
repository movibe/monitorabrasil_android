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
package com.gamfig.monitorabrasil.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.classes.Politico;
import com.gamfig.monitorabrasil.view.VotosActivity;

public class VotoAdapter extends ArrayAdapter<Politico> {
	Context context;
	int layoutResourceId;
	List<Politico> data = null;

	public VotoAdapter(Context context, int layoutResourceId, ArrayList<Politico> politicos) {
		super(context, layoutResourceId, politicos);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = politicos;
	}
	
	public void sendTwitter(String texto,String twitter) {

		Intent i = findTwitterClient();
		i.putExtra(Intent.EXTRA_TEXT,  twitter +" "+ texto+" #MonitoraBrasil ");
		try {
			getContext().startActivity(Intent.createChooser(i, "Enviar Twitter..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getContext(), "Não há twitter configurado no dipositivo.", Toast.LENGTH_SHORT)
					.show();
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
		final PackageManager packageManager = getContext().getPackageManager();
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

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		PoliticoHolder holder = null;

		if (row == null) {
			LayoutInflater infater = ((Activity) context).getLayoutInflater();
			row = infater.inflate(layoutResourceId, parent, false);
			holder = new PoliticoHolder();
			holder.txtVotoNome = (TextView) row.findViewById(R.id.txtVotoNome);
			holder.txtVotoPartido = (TextView) row.findViewById(R.id.txtVotoPartido);
			holder.txtVoto = (TextView) row.findViewById(R.id.txtVoto);

			holder.btnElogieTwitter = (ImageButton) row.findViewById(R.id.btnElogieTwitter);
			holder.btnReclameTwitter = (ImageButton) row.findViewById(R.id.btnReclameTwitter);
			holder.btnEmailVoto = (ImageButton) row.findViewById(R.id.btnEmailVoto);
			holder.btnEmailVoto.setTag(holder);
			holder.btnElogieTwitter.setTag(holder);
			holder.btnReclameTwitter.setTag(holder);
			
			holder.btnReclameTwitter.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					PoliticoHolder ph=(PoliticoHolder) v.getTag();
					sendTwitter("Seu voto "+ph.txtVoto.getText().toString()+" na #"+ph.nomeProjeto+" não me representa!", ph.twitter);
				}
			});
			
			holder.btnElogieTwitter.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					PoliticoHolder ph=(PoliticoHolder) v.getTag();
					sendTwitter("Parabéns por votar "+ph.txtVoto.getText().toString()+" na #"+ph.nomeProjeto, ph.twitter);
				}
			});

			holder.btnEmailVoto.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					enviarEmail((PoliticoHolder) v.getTag());
				}

				private void enviarEmail(PoliticoHolder politicoHolder) {

					Intent i = new Intent(Intent.ACTION_SEND);
					i.setType("text/plain");
					i.putExtra(Intent.EXTRA_EMAIL, new String[] { politicoHolder.email + "@camara.leg.br" });
					i.putExtra(Intent.EXTRA_SUBJECT, "Voto "+politicoHolder.nomeProjeto);
					i.putExtra(
							Intent.EXTRA_TEXT,
							Html.fromHtml(new StringBuilder()
									.append("<br>"
											+ "<br>"
											+ "<small><p>Email enviado pelo aplicativo Monitora, Brasil!"
											+ "<br>Android - <a href='https://play.google.com/store/apps/details?id=com.gamfig.monitorabrasil'>https://play.google.com/store/apps/details?id=com.gamfig.monitorabrasil</a>"
											+ "<br>Breve para Iphone e Ipad</p></small>").toString()));
					try {
						getContext().startActivity(Intent.createChooser(i, "Enviar email..."));
					} catch (android.content.ActivityNotFoundException ex) {
						Toast.makeText(getContext(), "Não há email configurado no dispositivo.", Toast.LENGTH_SHORT).show();
					}

				}

			});

			row.setTag(holder);
		} else {
			holder = (PoliticoHolder) row.getTag();
		}

		Politico politico = getData().get(position);

		holder.txtVotoNome.setText(politico.getNomeParlamentar());
		holder.txtVotoPartido.setText(politico.getUf());
		if (politico.getVoto().equals("s")) {
			holder.txtVoto.setText("Sim");
			holder.txtVoto.setTextColor(getContext().getResources().getColor(R.color.bandeira_verde));
		} else {
			holder.txtVoto.setText("Não");
			holder.txtVoto.setTextColor(getContext().getResources().getColor(R.color.dark_red));
		}

		// colocar os onclick aqui
		if (politico.getTwitter().equals("")) {
			holder.btnElogieTwitter.setVisibility(View.INVISIBLE);
			holder.btnReclameTwitter.setVisibility(View.INVISIBLE);
		} else {
			holder.btnElogieTwitter.setVisibility(View.VISIBLE);
			holder.btnReclameTwitter.setVisibility(View.VISIBLE);
		}
		
		holder.twitter = politico.getTwitter();
		holder.email = politico.getEmail();
		holder.nomeProjeto = VotosActivity.nomeProjeto.replace(" ", "");
		holder.nomeProjeto = holder.nomeProjeto.substring(0, holder.nomeProjeto.indexOf("/"));
		

		return row;
	}

	public List<Politico> getData() {
		return data;
	}

	public void setData(List<Politico> data) {
		this.data = data;
	}

	static class PoliticoHolder {

		TextView txtVotoNome;
		TextView txtVotoPartido;
		TextView txtVoto;
		ImageButton btnElogieTwitter;
		ImageButton btnReclameTwitter;
		ImageButton btnEmailVoto;
		String email;
		String twitter;
		String nomeProjeto;
	}

}
