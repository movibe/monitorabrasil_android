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

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.DAO.DeputadoDAO;
import com.gamfig.monitorabrasil.adapter.DoacaoAdapter;
import com.gamfig.monitorabrasil.adapter.DoadorAdapter;
import com.gamfig.monitorabrasil.classes.Doacao;
import com.gamfig.monitorabrasil.classes.Doador;
import com.gamfig.monitorabrasil.view.FichaDeputado;
import com.gamfig.monitorabrasil.view.ProcuraDeputado;
import com.gamfig.monitorabrasil.view.ProjetoDetalheActivity;

public class DoacoesActivity extends Activity {
	DoacaoAdapter adapter;
	ListView lv;
	Button btnVoltar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doacoes);
		btnVoltar = (Button) findViewById(R.id.btnVoltar);
		// popula listivew com as doacoes recebidas

		lv = (ListView) findViewById(R.id.listView1);
		// buscar os projetos da lista do user
		carregarDoacoes();
	}

	public void abrirDeputados(View v) {
		btnVoltar.setVisibility(View.GONE);
		carregarDoacoes();
	}

	private void carregarDoacoes() {
		ArrayList<Doacao> doacoes = new DeputadoDAO().buscaDoacoesAgrupadas(FichaDeputado.politico.getIdCadastro());

		if (doacoes != null) {
			adapter = new DoacaoAdapter(this, R.layout.listview_item_doacao, doacoes);
			lv.setAdapter(adapter);
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					try {
						Doacao doacao = (Doacao) parent.getItemAtPosition(position);
						if (doacao != null) {
							if (doacao.getIdTipo() > 0) {
								// buscar detalhe do tipo de doacao
								ArrayList<Doador> doacoes = new DeputadoDAO().buscaDoacoesEspecifica(
										FichaDeputado.politico.getIdCadastro(), doacao.getIdTipo());
								DoadorAdapter adapter = new DoadorAdapter(DoacoesActivity.this,
										R.layout.listview_item_doacao, doacoes);
								lv.setAdapter(adapter);
								btnVoltar.setVisibility(View.VISIBLE);
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.doacoes, menu);
		return true;
	}

}
