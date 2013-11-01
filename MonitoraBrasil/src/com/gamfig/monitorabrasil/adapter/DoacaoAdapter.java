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

import com.gamfig.monitorabrasil.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gamfig.monitorabrasil.classes.Doacao;

public class DoacaoAdapter extends ArrayAdapter<Doacao> {
	Context context;
	int layoutResourceId;
	List<Doacao> data = null;

	public DoacaoAdapter(Context context, int layoutResourceId,
			 List<Doacao> doacoes) {
		super(context, layoutResourceId, doacoes);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = doacoes;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		DoacaoHolder holder = null;
		
		if(row==null){
			LayoutInflater infater = ((Activity)context).getLayoutInflater();
			row = infater.inflate(layoutResourceId, parent,false);
			holder = new DoacaoHolder(); 
			holder.txtTipoDoacao = (TextView)row.findViewById(R.id.txtTipoDoacao);	
			holder.txtId = (TextView)row.findViewById(R.id.txtId);
			holder.txtTotal = (TextView)row.findViewById(R.id.txtTotal);
			
			
			row.setTag(holder);
		}else{
			holder = (DoacaoHolder)row.getTag();
		}
		Doacao doacao = data.get(position);	
			
		holder.txtId.setText(String.valueOf(doacao.getIdTipo()));
		holder.txtTipoDoacao.setText(doacao.getTipo());		
		//formatar valor
		NumberFormat nf = NumberFormat.getInstance();
		holder.txtTotal.setText("R$ "+nf.format(Double.parseDouble(String.valueOf(doacao.getTotal())))+",00");	
		
		return row;
	}
	static class DoacaoHolder{
		
	
		TextView txtTipoDoacao;
		TextView txtTotal;
		TextView txtId;
	}
}
