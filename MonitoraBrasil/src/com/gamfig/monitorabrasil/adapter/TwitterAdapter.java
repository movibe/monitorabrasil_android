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

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.classes.StatusTwitter;

public class TwitterAdapter extends ArrayAdapter<StatusTwitter> {
	Context context;
	int layoutResourceId;
	List<StatusTwitter> data = null;

	public TwitterAdapter(Context context, int layoutResourceId, List<StatusTwitter> status) {
		super(context, layoutResourceId, status);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = status;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		StatusHolder holder = null;

		if (row == null) {
			LayoutInflater infater = ((Activity) context).getLayoutInflater();
			row = infater.inflate(layoutResourceId, parent, false);
			holder = new StatusHolder();
			holder.txtTwitterId = (TextView) row.findViewById(R.id.txtTwitterId);
			holder.txtTwitterNome = (TextView) row.findViewById(R.id.txtTwitterNome);
			holder.txtTwitterMsg = (TextView) row.findViewById(R.id.txtTwitterMsg);
			holder.txtTwitterTempo = (TextView) row.findViewById(R.id.txtTwitterTempo);
			holder.imgTwitter = (ImageView) row.findViewById(R.id.imgTwitter);

			row.setTag(holder);
		} else {
			holder = (StatusHolder) row.getTag();
		}
		StatusTwitter status = data.get(position);

		holder.txtTwitterId.setText(Html.fromHtml("<a href='http://twitter.com/#!/" + status.getId() + "'>@"
				+ status.getId() + "</a>"));
		holder.txtTwitterId.setMovementMethod(LinkMovementMethod.getInstance());

		holder.txtTwitterNome.setText(status.getNome());

		String mensagem = status.getMensagem();
		// verifica se tem a #MonitoraBrasil
		int indexSearch = -1;
		indexSearch = mensagem.indexOf("#MonitoraBrasil");
		LinearLayout ln = (LinearLayout) row.findViewById(R.id.lnLayoutTwett);
		if (indexSearch >= 0) {
			
			ln.setBackgroundResource(R.color.white);
			holder.txtTwitterMsg.setTextColor(Color.BLACK);
			holder.txtTwitterId.setTextColor(Color.BLACK);
			holder.txtTwitterNome.setTextColor(Color.BLACK);
			holder.txtTwitterTempo.setTextColor(Color.BLACK);
		}else{
			ln.setBackgroundResource(R.color.black);
			holder.txtTwitterMsg.setTextColor(Color.WHITE);
			holder.txtTwitterId.setTextColor(Color.WHITE);
			holder.txtTwitterNome.setTextColor(Color.WHITE);
			holder.txtTwitterTempo.setTextColor(Color.WHITE);
		}
		// busca link http
		int indexInicio = mensagem.indexOf("http://");
		int indexFim = mensagem.indexOf(" ", indexInicio);
		if (indexFim == -1)
			indexFim = mensagem.length();
		if (indexInicio > -1) {
			String link = mensagem.substring(indexInicio, indexFim);
			String linkNovo = "<a href='" + link + "'>" + link + "</a>";
			mensagem = mensagem.replaceAll(link, linkNovo);
		}

		// busca link http
		indexInicio = mensagem.indexOf("https://");
		indexFim = mensagem.indexOf(" ", indexInicio);
		if (indexFim == -1)
			indexFim = mensagem.length();
		if (indexInicio > -1) {
			String link = mensagem.substring(indexInicio, indexFim);
			String linkNovo = "<a href='" + link + "'>" + link + "</a>";
			mensagem = mensagem.replaceAll(link, linkNovo);
		}
		holder.txtTwitterMsg.setText(Html.fromHtml(mensagem));
		holder.txtTwitterMsg.setMovementMethod(LinkMovementMethod.getInstance());

		// calcula tempo
		long diferenca = System.currentTimeMillis() - status.getData().getTime();
		long diferencaMin = diferenca / (60 * 1000); // diferenca em minutos
		long diferencaHoras = diferenca / 3600000;
		if (diferencaMin < 60) {
			holder.txtTwitterTempo.setText(String.valueOf(diferencaMin) + "min");
		} else if (diferencaHoras < 24) {

			holder.txtTwitterTempo.setText(String.valueOf(diferencaHoras) + "h");
		} else {
			holder.txtTwitterTempo.setText(String.valueOf(diferencaHoras / 24) + "d");
		}
		holder.imgTwitter.setImageBitmap(status.getFoto());

		return row;
	}

	static class StatusHolder {

		TextView txtTwitterId;
		TextView txtTwitterNome;
		TextView txtTwitterMsg;
		TextView txtTwitterTempo;
		ImageView imgTwitter;
	}

}
