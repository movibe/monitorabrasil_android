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

import java.text.NumberFormat;
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
import com.gamfig.monitorabrasil.classes.Presenca;

public class RankingAdapter extends ArrayAdapter<Politico> {
	Context context;
	int layoutResourceId;
	List<Politico> data = null;

	public RankingAdapter(Context context, int layoutResourceId, List<Politico> politicos) {
		super(context, layoutResourceId, politicos);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = politicos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		PoliticoHolder holder = null;

		if (row == null) {
			LayoutInflater infater = ((Activity) context).getLayoutInflater();
			row = infater.inflate(layoutResourceId, parent, false);
			holder = new PoliticoHolder();
			holder.txtRankingNome = (TextView) row.findViewById(R.id.txtRankingNome);
			holder.txtId = (TextView) row.findViewById(R.id.txtId);
			holder.txtRankingPartido = (TextView) row.findViewById(R.id.txtRankingPartido);
			holder.txtRankingPercentual = (TextView) row.findViewById(R.id.txtRankingPercentual);
			holder.txtRankingPresenca = (TextView) row.findViewById(R.id.txtRankingPresenca);
			holder.txtRankingPosicao = (TextView) row.findViewById(R.id.txtRankingPosicao);

			row.setTag(holder);
		} else {
			holder = (PoliticoHolder) row.getTag();
		}
		Politico politico = data.get(position);

		holder.txtId.setText(String.valueOf(politico.getIdCadastro()));
		holder.txtRankingNome.setText(politico.getNome());
		holder.txtRankingPartido.setText(politico.getPartido().getSigla());
		if (politico.getPresenca() != null) {
			Presenca presenca = politico.getPresenca().get(0);
			holder.txtRankingPresenca.setText("Presen�a: " + String.valueOf(presenca.getNrPresenca()) + " ("
					+ presenca.getPercentualPresenca() + "%)");
			holder.txtRankingPercentual.setText("N� sess�es: " + presenca.getTotal());

		} else {
			if (politico.getValor() > 0) {
				NumberFormat nf = NumberFormat.getInstance();
				holder.txtRankingPresenca.setText("R$ " + nf.format(politico.getValor())+",00");
			} else {
				holder.txtRankingPresenca.setText("N� de Projetos: " + String.valueOf(politico.getNrProjetos()));
			}
		}
		holder.txtRankingPosicao.setText(String.valueOf(position + 1) + "�");

		return row;
	}

	static class PoliticoHolder {

		TextView txtRankingPosicao;
		TextView txtRankingNome;
		TextView txtRankingPartido;
		TextView txtRankingPresenca;
		TextView txtRankingPercentual;
		TextView txtId;
	}
}
