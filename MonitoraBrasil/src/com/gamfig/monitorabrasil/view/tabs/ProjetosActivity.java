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
package com.gamfig.monitorabrasil.view.tabs;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.DAO.DeputadoDAO;
import com.gamfig.monitorabrasil.adapter.ProjetoAdapter;
import com.gamfig.monitorabrasil.adapter.ProjetoVotoAdapter;
import com.gamfig.monitorabrasil.classes.Projeto;
import com.gamfig.monitorabrasil.view.FichaDeputado;
import com.gamfig.monitorabrasil.view.ListaProjetosActivity;
import com.gamfig.monitorabrasil.view.ProjetoDetalheActivity;
import com.gamfig.monitorabrasil.view.ListaProjetosActivity.buscaMaisProjetos;

public class ProjetosActivity extends Activity implements OnScrollListener{

	ProjetoVotoAdapter adapter;
	ListView lv;
	int currentPage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_projetos);

		// popula listivew com os projetos de autoria do deputado

		lv = (ListView) findViewById(R.id.listView1);
		// buscar os projetos da lista do user
		ArrayList<Projeto> projetosDeputado = new DeputadoDAO().buscaProjetosVoto(0, "8", null, null, FichaDeputado.politico.getIdCadastro());

		if (FichaDeputado.politico.getProjetos() != null) {
			adapter = new ProjetoVotoAdapter(this, R.layout.listview_item_projetos,
					projetosDeputado);
			lv.setAdapter(adapter);
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Projeto projeto = (Projeto) parent.getItemAtPosition(position);
					if (projeto != null) {

						// buscar detalhes do projeto clicado
						Bundle sendBundle = new Bundle();
						sendBundle.putInt("idProjeto", projeto.getId());
						Intent i = new Intent(ProjetosActivity.this, ProjetoDetalheActivity.class);
						i.putExtras(sendBundle);
						startActivity(i);
					}
				}
			});
		} else {
			ArrayList<Projeto> projetos = new ArrayList<Projeto>();
			Projeto projeto = new Projeto();
			projeto.setNome("Não há registros de projetos");
			projeto.setId(-1);
			projetos.add(projeto);
			ProjetoAdapter adapter = new ProjetoAdapter(this, R.layout.listview_item_row, projetos);
			lv.setAdapter(adapter);
		}
		lv = (ListView) findViewById(R.id.listView1);
		lv.setOnScrollListener(this);
	}
	
	public class buscaMaisProjetos extends AsyncTask<Void, Void, String> {
		Activity listaProjetosActivit;
		private ProgressDialog mDialog;
		ArrayList<Projeto> projetos;
		int page;

		public buscaMaisProjetos(Activity listaProjetosActivity, int currentPage2) {
			this.listaProjetosActivit = listaProjetosActivity;
			page = currentPage2;
		}

		@Override
		protected String doInBackground(Void... params) {

			

			// buscar os projetos da lista do user
			projetos = new DeputadoDAO().buscaProjetosVoto(page * 10, "8", null, null, FichaDeputado.politico.getIdCadastro());
			return "ok";
		}

		protected void onPostExecute(String results) {

			// popula listivew com os projetos de autoria do deputado
			if (projetos != null) {
				if (!projetos.get(0).getNome().equals("Nenhum projeto")) {
					for (Projeto projeto : projetos) {
						adapter.add(projeto);
					}
					adapter.notifyDataSetChanged();
				} else {
					Toast.makeText(ProjetosActivity.this, "Não há mais projetos!", Toast.LENGTH_LONG).show();
				}
			}

		}
	}
	private void loadElements(int currentPage2) {

		new buscaMaisProjetos(this, currentPage2).execute();

	}
	
	@Override
	public void onScrollStateChanged(AbsListView v, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			if (lv.getLastVisiblePosition() >= lv.getCount() - 3) {
				currentPage++;
				// load more list items:
				loadElements(currentPage);
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.projetos, menu);
		return true;
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

}
