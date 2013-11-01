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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.classes.Projeto;

public class ProjetoAdapter extends ArrayAdapter<Projeto> {
	Context context;
	int layoutResourceId;
	List<Projeto> data = null;

	public ProjetoAdapter(Context context, int layoutResourceId, List<Projeto> projetos) {
		super(context, layoutResourceId, projetos);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = projetos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ProjetoHolder holder = null;

		if (row == null) {
			LayoutInflater infater = ((Activity) context).getLayoutInflater();
			row = infater.inflate(layoutResourceId, parent, false);
			holder = new ProjetoHolder();
			holder.txtNomeRow = (TextView) row.findViewById(R.id.txtNomeRow);
			holder.txtId = (TextView) row.findViewById(R.id.txtId);
			holder.txtSituacao = (TextView) row.findViewById(R.id.txtPartido);
			holder.txtdtUltimoDespacho = (TextView) row.findViewById(R.id.txtdtUltimoDespacho);
			holder.txtAutor = (TextView) row.findViewById(R.id.txtAutor2);

			row.setTag(holder);
		} else {
			holder = (ProjetoHolder) row.getTag();
		}
		Projeto projeto = data.get(position);

		holder.txtId.setText(String.valueOf(projeto.getId()));
		holder.txtNomeRow.setText(projeto.getNome());
		if (projeto.getId() > -1) {
			holder.txtSituacao.setText(projeto.getSituacao());
			if (projeto.getDtUltimoDespacho() != null) {
				if (!projeto.getDtUltimoDespacho().equals("00/00/0000")
						|| !projeto.getDtUltimoDespacho().equals("0000-00-00")
						|| !projeto.getDtUltimoDespacho().isEmpty())
					holder.txtdtUltimoDespacho.setText("Dt. último despacho:" + projeto.getDtUltimoDespacho());
			}
			if (projeto.getNomeAutor() != null)
				holder.txtAutor.setText("Dep. " + projeto.getNomeAutor());
		}

		return row;
	}

	static class ProjetoHolder {

		TextView txtNomeRow;
		TextView txtSituacao;
		TextView txtId;
		TextView txtdtUltimoDespacho;
		TextView txtAutor;
	}
}
