/*******************************************************************************
 * Copyright  2013 de Geraldo Augusto de Morais Figueiredo
 * Este arquivo � parte do programa Monitora, Brasil!. O Monitora, Brasil! � um software livre.
 * Voc� pode redistribu�-lo e/ou modific�-lo dentro dos termos da GNU Affero General Public License 
 * como publicada pela Funda��o do Software Livre (FSF); na vers�o 3 da Licen�a. 
 * Este programa � distribu�do na esperan�a que possa ser �til, mas SEM NENHUMA GARANTIA,
 * sem uma garantia impl�cita de ADEQUA��O a qualquer MERCADO ou APLICA��O EM PARTICULAR. 
 * Veja a licen�a para maiores detalhes. 
 * Voc� deve ter recebido uma c�pia da GNU Affero General Public License, sob o t�tulo "LICENSE.txt", 
 * junto com este programa, se n�o, acesse http://www.gnu.org/licenses/
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
import com.gamfig.monitorabrasil.classes.Politico;

public class PoliticoAdapter extends ArrayAdapter<Politico> {
	Context context;
	int layoutResourceId;
	private List<Politico> data = null;
	List<Politico> dataOriginal = null;

	List<Politico> subItems;

	public PoliticoAdapter(Context context, int layoutResourceId, List<Politico> politicos) {
		super(context, layoutResourceId, politicos);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.setData(politicos);
		this.dataOriginal = politicos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		PoliticoHolder holder = null;

		if (row == null) {
			LayoutInflater infater = ((Activity) context).getLayoutInflater();
			row = infater.inflate(layoutResourceId, parent, false);
			holder = new PoliticoHolder();
			holder.txtNomeRow = (TextView) row.findViewById(R.id.txtNomeRow);
			holder.txtId = (TextView) row.findViewById(R.id.txtId);
			holder.txtPartido = (TextView) row.findViewById(R.id.txtPartido);

			row.setTag(holder);
		} else {
			holder = (PoliticoHolder) row.getTag();
		}

		Politico politico = getData().get(position);

		holder.txtId.setText(String.valueOf(politico.getIdCadastro()));
		holder.txtNomeRow.setText(politico.getNome());
		holder.txtPartido.setText(politico.getPartido().getSigla());
		holder.txtPartido.setText(politico.getPartido().getSigla());
		if (politico.getLider() != null) {
			if (politico.getLider().length() > 0)
				holder.txtPartido.setText(politico.getPartido().getSigla() + " (" + politico.getLider() + ")");
		}

		return row;
	}
	

	public List<Politico> getData() {
		return data;
	}

	public void setData(List<Politico> data) {
		this.data = data;
	}

	static class PoliticoHolder {

		TextView txtNomeRow;
		TextView txtPartido;
		TextView txtId;
	}
}
