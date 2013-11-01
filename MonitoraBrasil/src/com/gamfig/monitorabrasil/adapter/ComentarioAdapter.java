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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.classes.Comentario;

public class ComentarioAdapter extends ArrayAdapter<Comentario> {
	Context context;
	int layoutResourceId;
	List<Comentario> data = null;

	public ComentarioAdapter(Context context, int layoutResourceId, List<Comentario> comentarios) {
		super(context, layoutResourceId, comentarios);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = comentarios;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		StatusHolder holder = null;

		if (row == null) {
			LayoutInflater infater = ((Activity) context).getLayoutInflater();
			row = infater.inflate(layoutResourceId, parent, false);
			holder = new StatusHolder();


			holder.txtComentTexto = (TextView) row.findViewById(R.id.txtComentTexto);
			holder.txtComentTempo = (TextView) row.findViewById(R.id.txtComentTempo);
			holder.txtComentNome = (TextView) row.findViewById(R.id.txtComentNome);
			holder.fotoFace = (ProfilePictureView) row.findViewById(R.id.fotoFaceComentario);
			
			row.setTag(holder);
		} else {
			holder = (StatusHolder) row.getTag();
		}
		Comentario comentario = data.get(position);

		holder.txtComentNome.setText(comentario.getUser().getNome());
		holder.txtComentTexto.setText(unescape(comentario.getComentario()));
		
		

		// buscar a imagem
		holder.fotoFace.setPresetSize(ProfilePictureView.SMALL);
		holder.fotoFace.setProfileId(comentario.getUser().getIdFacebook());
		

		// calcula tempo
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
		Date date;
		try {
			date = (Date)formatter.parse(comentario.getData());
			long diferenca = System.currentTimeMillis() - date.getTime();
			long diferencaMin = diferenca / (60 * 1000); // diferenca em minutos
			long diferencaHoras = diferenca / 3600000;
			if (diferencaMin < 60) {
				holder.txtComentTempo.setText(String.valueOf(diferencaMin) + "min");
			} else if (diferencaHoras < 24) {

				holder.txtComentTempo.setText(String.valueOf(diferencaHoras) + "h");
			} else {
				holder.txtComentTempo.setText(String.valueOf(diferencaHoras / 24) + "d");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		

		return row;
	}
	
	 private String unescape(String description) {
		    return description.replaceAll("\\\\n", "\\\n");
		}

	static class StatusHolder {

		TextView txtComentTexto;
		TextView txtComentTempo;
		TextView txtComentNome;
		ProfilePictureView fotoFace;
	}

}
