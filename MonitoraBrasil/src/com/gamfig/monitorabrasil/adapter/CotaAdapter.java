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

import java.text.NumberFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.classes.Politico;

public class CotaAdapter extends ArrayAdapter<Politico> {
	Context context;
	int layoutResourceId;
	List<Politico> data = null;

	public CotaAdapter(Context context, int layoutResourceId, List<Politico> politicos) {
		super(context, layoutResourceId, politicos);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = politicos;
	}

	public void sendTwitter(String texto) {

		Intent i = findTwitterClient();
		i.putExtra(Intent.EXTRA_TEXT,  texto + " #MonitoraBrasil ");
		try {
			getContext().startActivity(Intent.createChooser(i, "Enviar Twitter..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getContext(), "Não há twitter configurado no dipositivo.", Toast.LENGTH_SHORT).show();
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
		CotaHolder holder = null;

		if (row == null) {
			LayoutInflater infater = ((Activity) context).getLayoutInflater();
			row = infater.inflate(layoutResourceId, parent, false);
			holder = new CotaHolder();
			holder.nome = (TextView) row.findViewById(R.id.nome);
			holder.partido = (TextView) row.findViewById(R.id.partido);
			holder.valor = (TextView) row.findViewById(R.id.valor);
			holder.percentual = (TextView) row.findViewById(R.id.percentual);

//			holder.btnFace = (ImageButton) row.findViewById(R.id.btnFace);
			holder.btnTwitter = (ImageButton) row.findViewById(R.id.btnTwitter);
			//holder.btnFace.setTag(holder);
			holder.btnTwitter.setTag(holder);

			holder.btnTwitter.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					CotaHolder ch = (CotaHolder) v.getTag();
					String texto = null;
					String tipo="";
					if(ch.tipo.length()>0){
						tipo = "em "+ch.tipo+" ";
					}
					String mes="";
					if(ch.mes > 0){
						mes=String.valueOf(ch.mes)+"/";
					}
					if (ch.twitter.length() > 0)
						texto = ch.twitter + " gastou "+tipo + ch.valor.getText() + " ("
								+ ch.percentual.getText() + ") em "+mes+"2013";
					else
						texto = "O " + ch.nome.getText() + " gastou "+tipo + ch.valor.getText() + " ("
								+ ch.percentual.getText() + ") em "+mes+"2013";
					sendTwitter(texto);
				}
			});

//			holder.btnFace.setOnClickListener(new View.OnClickListener() {
//
//				public void onClick(View v) {
//					CotaHolder ph = (CotaHolder) v.getTag();
//					// sendTwitter("Parabéns por votar "+ph.txtVoto.getText().toString()+" na #"+ph.nomeProjeto,
//					// ph.twitter);
//				}
//			});

			row.setTag(holder);
		} else {
			holder = (CotaHolder) row.getTag();
		}

		Politico politico = getData().get(position);

		holder.nome.setText("Dep. "+politico.getNome());
		holder.partido.setText(politico.getPartido().getSigla());
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		holder.valor.setText( nf.format(politico.getCotas().getValor()));
		int variacaoPer=politico.getCotas().getVariacaoPercentual();
		if (variacaoPer > 0){
			holder.percentual.setText(String.valueOf(variacaoPer)
					+ "% a mais que a média.");
		}else{
			if(variacaoPer == 0){
				holder.percentual.setText("Está na média");
			}else{
				holder.percentual.setText(String.valueOf(variacaoPer) + "% que a média.");
			}
		}
		//alterar cores
		row.setBackgroundColor(0xFF191919);
		if(variacaoPer > 25){
			//cor vermelhar
			row.setBackgroundColor(context.getResources().getColor(R.color.vermelho));
		}
		if(variacaoPer < -25){
			row.setBackgroundColor(0xFF3366CC);
		}
			
		holder.twitter = politico.getTwitter();
		holder.tipo = politico.getCotas().getTipo();
		holder.mes = politico.getCotas().getMes();
		return row;
	}

	public List<Politico> getData() {
		return data;
	}

	public void setData(List<Politico> data) {
		this.data = data;
	}

	static class CotaHolder {

		TextView nome;
		TextView partido;
		TextView valor;
		TextView percentual;
		ImageButton btnFace;
		ImageButton btnTwitter;
		String twitter;
		String tipo;
		int mes;
	}

}
