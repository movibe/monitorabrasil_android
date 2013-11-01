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
package com.gamfig.monitorabrasil.view.tabs;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.classes.Presenca;
import com.gamfig.monitorabrasil.view.FichaDeputado;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class PresencaActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_presenca);
		TextView txt = (TextView) findViewById(R.id.txtAno1);
		
		int tamanhoArray = FichaDeputado.politico.getPresenca().size();
		String[] anoArray = new String[tamanhoArray];
		GraphViewData[] valoresPresencaArray = new GraphViewData[tamanhoArray];
		int i = 0;
		for (Presenca presenca : FichaDeputado.politico.getPresenca()) {
			if (i == 0) {
				txt.setText(String.valueOf(presenca.getAno()));
				txt = (TextView) findViewById(R.id.txtPresenca1);
				if (Float.parseFloat(presenca.getPercentualPresenca().replace(",", ".")) < 75)
					txt.setTextColor(getResources().getColor(R.color.dark_red));
				txt.setText(String.valueOf(presenca.getNrPresenca()) + " (" + presenca.getPercentualPresenca() + "%)");

				txt = (TextView) findViewById(R.id.txtAusJustif1);
				txt.setText(String.valueOf(presenca.getNrAusenciaJustificada()) + " ("
						+ presenca.getPercentualAusenciaJustificada() + "%)");
				txt = (TextView) findViewById(R.id.txtAusNaoJustif1);
				txt.setText(String.valueOf(presenca.getNrAusenciaNaoJustificada()) + " ("
						+ presenca.getPercentualAusenciaNaoJustificada() + "%)");
				txt = (TextView) findViewById(R.id.total1);
				txt.setText(String.valueOf(presenca.getNrAusenciaNaoJustificada() + presenca.getNrAusenciaJustificada()
						+ presenca.getNrPresenca())
						+ " (" + "100%)");

			}
			if (i == 1) {
				txt = (TextView) findViewById(R.id.txtAno2);
				txt.setText(String.valueOf(presenca.getAno()));
				txt = (TextView) findViewById(R.id.txtPresenca2);
				if (Float.parseFloat(presenca.getPercentualPresenca().replace(",", ".")) < 75)
					txt.setTextColor(getResources().getColor(R.color.dark_red));
				txt.setText(String.valueOf(presenca.getNrPresenca()) + " (" + presenca.getPercentualPresenca() + "%)");
				txt = (TextView) findViewById(R.id.txtAusJustif2);
				txt.setText(String.valueOf(presenca.getNrAusenciaJustificada()) + " ("
						+ presenca.getPercentualAusenciaJustificada() + "%)");
				txt = (TextView) findViewById(R.id.txtAusNaoJustif2);
				txt.setText(String.valueOf(presenca.getNrAusenciaNaoJustificada()) + " ("
						+ presenca.getPercentualAusenciaNaoJustificada() + "%)");
				;
				txt = (TextView) findViewById(R.id.total2);
				txt.setText(String.valueOf(presenca.getNrAusenciaNaoJustificada() + presenca.getNrAusenciaJustificada()
						+ presenca.getNrPresenca())
						+ " (" + "100%)");
			}
			if (i == 2) {
				txt = (TextView) findViewById(R.id.txtAno3);
				txt.setText(String.valueOf(presenca.getAno()));
				txt = (TextView) findViewById(R.id.txtPresenca3);
				if (Float.parseFloat(presenca.getPercentualPresenca().replace(",", ".")) < 75)
					txt.setTextColor(getResources().getColor(R.color.dark_red));
				txt.setText(String.valueOf(presenca.getNrPresenca()) + " (" + presenca.getPercentualPresenca() + "%)");
				txt = (TextView) findViewById(R.id.txtAusJustif3);
				txt.setText(String.valueOf(presenca.getNrAusenciaJustificada()) + " ("
						+ presenca.getPercentualAusenciaJustificada() + "%)");
				txt = (TextView) findViewById(R.id.txtAusNaoJustif3);
				txt.setText(String.valueOf(presenca.getNrAusenciaNaoJustificada()) + " ("
						+ presenca.getPercentualAusenciaNaoJustificada() + "%)");
				;
				txt = (TextView) findViewById(R.id.total3);
				txt.setText(String.valueOf(presenca.getNrAusenciaNaoJustificada() + presenca.getNrAusenciaJustificada()
						+ presenca.getNrPresenca())
						+ " (" + "100%)");
			}

			valoresPresencaArray[i] = new GraphViewData(i + 1, Double.valueOf(presenca.getPercentualPresenca().replace(
					",", ".")));
			i++;

		}

		// valores do grafico
		GraphViewSeries exampleSeries = new GraphViewSeries(valoresPresencaArray);

		GraphView graphView = new LineGraphView(this, "Gr�fico");
		graphView.addSeries(exampleSeries); // data

		graphView.setHorizontalLabels(anoArray);

		LinearLayout layout = (LinearLayout) findViewById(R.id.grafico);
		layout.addView(graphView);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.presenca, menu);

		return true;
	}

}
