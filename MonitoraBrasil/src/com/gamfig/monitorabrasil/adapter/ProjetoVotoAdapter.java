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

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;
import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.DAO.DeputadoDAO;
import com.gamfig.monitorabrasil.classes.Projeto;
import com.gamfig.monitorabrasil.dialog.AlertDialogManager;
import com.gamfig.monitorabrasil.view.ComentarioActivity;
import com.gamfig.monitorabrasil.view.ProjetoDetalheActivity;
import com.gamfig.monitorabrasil.view.VotosActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ProjetoVotoAdapter extends ArrayAdapter<Projeto> {
	Context context;
	int layoutResourceId;
	List<Projeto> data = null;
	AlertDialogManager alert = new AlertDialogManager();

	public ProjetoVotoAdapter(Context context, int layoutResourceId, List<Projeto> projetos) {
		super(context, layoutResourceId, projetos);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = projetos;

		// limpar votos
		// SharedPreferences sharedPref = getContext().getSharedPreferences(
		// getContext().getString(R.string.id_key_preferencias),
		// getContext().MODE_PRIVATE);
		// SharedPreferences.Editor editor = sharedPref.edit();
		// editor.putString(getContext().getString(R.string.id_key_votos),
		// null);
		// editor.commit();
	}

	private String buscaVoto(int id) {
		SharedPreferences sharedPref = getContext().getSharedPreferences(
				getContext().getString(R.string.id_key_preferencias), getContext().MODE_PRIVATE);
		String idUser = sharedPref.getString(getContext().getString(R.string.id_key_idcadastro), null);
		if (idUser != null) {
			Gson gson = new Gson();
			HashMap<String, String> votos = gson.fromJson(
					sharedPref.getString(getContext().getString(R.string.id_key_votos), null),
					new TypeToken<HashMap<String, String>>() {
					}.getType());
			if (votos == null)
				return null;
			if (votos.containsKey(String.valueOf(id))) {
				return votos.get(String.valueOf(id));
			}
		}
		return null;
	}

	public boolean isLogado() {
		SharedPreferences sharedPref = getContext().getSharedPreferences(
				getContext().getString(R.string.id_key_preferencias), getContext().MODE_PRIVATE);
		String idUser = sharedPref.getString(getContext().getString(R.string.id_key_idcadastro), null);
		if (idUser != null)
			return true;
		else {
			alert.showAlertDialog(getContext(), "Faça login ", "Para votar é necessário logar.", false);
			return false;

		}
	}

	public String getIdUser() {
		SharedPreferences sharedPref = getContext().getSharedPreferences(
				getContext().getString(R.string.id_key_preferencias), getContext().MODE_PRIVATE);
		String idUser = sharedPref.getString(getContext().getString(R.string.id_key_idcadastro), null);
		if (idUser != null)
			return idUser;
		else {
			return "0";

		}
	}

	public boolean votar(String voto, String idProposta) {
		Gson gson = new Gson();
		SharedPreferences sharedPref = getContext().getSharedPreferences(
				getContext().getString(R.string.id_key_preferencias), getContext().MODE_PRIVATE);
		String idUser = sharedPref.getString(getContext().getString(R.string.id_key_idcadastro), null);
		if (idUser != null) {
			// salvar no banco

			new insereVoto(voto, idProposta, idUser).execute();

			// salvar no SharedPreferences

			HashMap<String, String> votos = gson.fromJson(
					sharedPref.getString(getContext().getString(R.string.id_key_votos), null),
					new TypeToken<HashMap<String, String>>() {
					}.getType());
			if (votos == null)
				votos = new HashMap<String, String>();
			votos.put(idProposta, voto);

			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString(getContext().getString(R.string.id_key_votos), gson.toJson(votos));
			editor.commit();
			for (Projeto projeto : data) {
				if (projeto.getId() == Integer.parseInt(idProposta)) {
					if (voto.equals("s")) {
						projeto.setS(projeto.getS() + 1);
					} else {
						projeto.setN(projeto.getN() + 1);
					}
				}
			}
			return true;

		} else {
			alert.showAlertDialog(getContext(), "Faça login ", "Para votar é necessário logar.", false);
			return false;
		}

	}

	public class insereVoto extends AsyncTask<Void, Void, String> {
		String mVoto;
		String mIdProposta;
		String mIdUser;

		public insereVoto(String voto, String idProposta, String idUser) {
			mVoto = voto;
			mIdProposta = idProposta;
			mIdUser = idUser;
		}

		@Override
		protected String doInBackground(Void... params) {

			new DeputadoDAO().insereVoto(mVoto, mIdProposta, mIdUser);

			return null;
		}

		protected void onPostExecute(String results) {

			Toast.makeText(getContext(), "Voto registrado", Toast.LENGTH_SHORT).show();
		}
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ProjetoVotoHolder holder = null;

		if (row == null) {
			LayoutInflater infater = ((Activity) context).getLayoutInflater();
			row = infater.inflate(layoutResourceId, parent, false);
			holder = new ProjetoVotoHolder();
			holder.txtPropostaTitulo = (TextView) row.findViewById(R.id.txtPropostaTitulo);
			holder.txtId = (TextView) row.findViewById(R.id.txtId);
			holder.txtPropostaEmenta = (TextView) row.findViewById(R.id.txtPropostaEmenta);
			holder.txtPropostaAutor = (TextView) row.findViewById(R.id.txtPropostaAutor);
			holder.txtPropostaVotos = (TextView) row.findViewById(R.id.txtPropostaVotos);
			holder.txtPropostaSim = (TextView) row.findViewById(R.id.txtPropostaSim);
			holder.txtPropostaNao = (TextView) row.findViewById(R.id.txtPropostaNao);
			holder.dtProposicao = (TextView) row.findViewById(R.id.dtProposicao);
			holder.btnEmail = (ImageButton) row.findViewById(R.id.btnEmail);
			holder.btnEmail.setTag(holder);
			holder.btnSim = (Button) row.findViewById(R.id.btnDeputados);

			holder.btnSim.setTag(holder);
			holder.btnNao = (Button) row.findViewById(R.id.btnRanking);
			holder.btnNao.setTag(holder);

			holder.btnComentar = (Button) row.findViewById(R.id.btnComentar);
			holder.btnComentar.setTag(holder);

			holder.imgComentar = (ImageView) row.findViewById(R.id.imgComentar);
			holder.imgVotado = (ImageView) row.findViewById(R.id.imgVotado);

			holder.txtPropostaVotado = (TextView) row.findViewById(R.id.txtPropostaVotado);
			//parte do comentario se houver
			holder.fotoFaceComentario = (ProfilePictureView)row.findViewById(R.id.fotoFaceComentario);
			holder.txtNomeComentario = (TextView)row.findViewById(R.id.txtNomeComentario);
			holder.txtUserComentario = (TextView)row.findViewById(R.id.txtUserComentario);
			holder.lnComentario = (RelativeLayout)row.findViewById(R.id.rlComentario);

			row.setTag(holder);

			// click para inserir comentário
			holder.btnComentar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// passar idProposta,idUser,TituloProposta
					Button bt = (Button) v;
					ProjetoVotoHolder p = (ProjetoVotoHolder) bt.getTag();

					// chamar a atividade

					Bundle sendBundle = new Bundle();
					sendBundle.putString("idUser", getIdUser());
					sendBundle.putString("idProposta", String.valueOf(p.txtId.getText()));
					sendBundle.putString("titulo", String.valueOf(p.txtPropostaTitulo.getText()));
					Intent intent = new Intent(getContext(), ComentarioActivity.class);
					intent.putExtras(sendBundle);
					getContext().startActivity(intent);

				}

			});

			holder.btnSim.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (isLogado()) {
						Button bt = (Button) v;
						if (!bt.getText().equals("Votou Sim")) {
							bt.setText("Votou Sim");
							ProjetoVotoHolder p = (ProjetoVotoHolder) bt.getTag();

							int sim = Integer.valueOf(p.txtPropostaSim.getText().toString()) + 1;
							p.txtPropostaSim.setText(String.valueOf(sim));

							// verificar se já votou
							if (p.btnNao.getText().equals("Não")) {
								int total = Integer.valueOf(p.txtPropostaVotos.getText().toString()) + 1;
								p.txtPropostaVotos.setText(String.valueOf(total));
							} else {
								p.btnNao.setText("Não");
								// diminui do voto nao
								int nao = Integer.valueOf(p.txtPropostaNao.getText().toString()) - 1;
								p.txtPropostaNao.setText(String.valueOf(nao));
							}
							votar("s", String.valueOf(p.txtId.getText()));
						}

					}
				}

			});

			holder.btnNao.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					if (isLogado()) {
						Button bt = (Button) v;
						if (bt.getText().equals("Não")) {
							bt.setText("Votou Não");
							ProjetoVotoHolder p = (ProjetoVotoHolder) bt.getTag();
							int nao = Integer.valueOf(p.txtPropostaNao.getText().toString()) + 1;
							p.txtPropostaNao.setText(String.valueOf(nao));
							// verificar se já votou
							if (p.btnSim.getText().equals("Sim")) {
								int total = Integer.valueOf(p.txtPropostaVotos.getText().toString()) + 1;
								p.txtPropostaVotos.setText(String.valueOf(total));
							} else {
								p.btnSim.setText("Sim");
								// Diminui do voto sim
								int sim = Integer.valueOf(p.txtPropostaSim.getText().toString()) - 1;
								p.txtPropostaSim.setText(String.valueOf(sim));
							}
							votar("n", String.valueOf(p.txtId.getText()));
						}
					}
				}

			});

			holder.btnEmail.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					enviarEmail((ProjetoVotoHolder) v.getTag());
				}

				private void enviarEmail(ProjetoVotoHolder projeto) {

					Intent i = new Intent(Intent.ACTION_SEND);
					i.setType("text/plain");
					i.putExtra(Intent.EXTRA_EMAIL, new String[] { projeto.email + "@camara.leg.br" });
					i.putExtra(Intent.EXTRA_SUBJECT, projeto.txtPropostaTitulo.getText());
					i.putExtra(
							Intent.EXTRA_TEXT,
							Html.fromHtml(new StringBuilder()
									.append("<b>Proposta: </b>"
											+ projeto.txtPropostaTitulo.getText()
											+ "<br><br><b>Votos: </b>"
											+ projeto.txtPropostaVotos.getText()
											+ "<br><b>Sim: </b>"
											+ projeto.txtPropostaSim.getText()
											+ "<br><b>Não: </b>"
											+ projeto.txtPropostaNao.getText()
											+ "<br>"
											+ "<small><p>Email enviado pelo aplicativo Monitora, Brasil!"
											+ "<br>Android - <a href='https://play.google.com/store/apps/details?id=com.gamfig.monitorabrasil'>https://play.google.com/store/apps/details?id=com.gamfig.monitorabrasil</a>"
											+ "<br>Breve para Iphone e Ipad</p></small>").toString()));
					try {
						getContext().startActivity(Intent.createChooser(i, "Enviar email..."));
					} catch (android.content.ActivityNotFoundException ex) {
						Toast.makeText(getContext(), "Não há email configurado no dispositivo.", Toast.LENGTH_SHORT)
								.show();
					}

				}

			});
		} else {
			holder = (ProjetoVotoHolder) row.getTag();
		}
		Projeto projeto = data.get(position);
		if (projeto.getNome().equals("Nenhum Projeto"))
			return null;
		holder.txtId.setText(String.valueOf(projeto.getId()));
		holder.txtPropostaEmenta.setText(projeto.getEmenta());

		// Evento para abrir projeto
		OnClickListener onclicklistener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// buscar detalhes do projeto clicado
				Bundle sendBundle = new Bundle();
				int id = Integer.valueOf(v.getTag().toString());
				sendBundle.putInt("idProjeto", id);
				Intent i = new Intent(getContext(), ProjetoDetalheActivity.class);
				i.putExtras(sendBundle);
				getContext().startActivity(i);
			}
		};
		holder.txtPropostaEmenta.setOnClickListener(onclicklistener);
		holder.txtPropostaEmenta.setTag(projeto.getId());

		holder.txtPropostaTitulo.setText(projeto.getNome());
		holder.dtProposicao.setText("Dt. Apresentação: " + projeto.getDtApresentacao());
		holder.txtPropostaAutor.setText("Dep. " + projeto.getNomeAutor());
		holder.txtPropostaVotos.setText(String.valueOf(projeto.getS() + projeto.getN()));
		holder.txtPropostaSim.setText(String.valueOf(projeto.getS()));
		holder.txtPropostaNao.setText(String.valueOf(projeto.getN()));
		String voto = buscaVoto(projeto.getId());
		holder.btnNao.setText("Não");
		holder.btnSim.setText("Sim");
		holder.email = projeto.getEmail();
		if (voto != null) {
			if (voto.equals("s")) {
				holder.btnSim.setText("Votou Sim");
				holder.btnNao.setText("Não");
			}
			if (voto.equals("n")) {
				holder.btnNao.setText("Votou Não");
				holder.btnSim.setText("Sim");
			}
		}
		if(projeto.getComentario().getId()!=null){
			holder.txtUserComentario.setText(projeto.getComentario().getComent());
			holder.fotoFaceComentario.setPresetSize(ProfilePictureView.SMALL);
			holder.fotoFaceComentario.setProfileId(projeto.getComentario().getId());
			holder.txtNomeComentario.setText(projeto.getComentario().getNome());
			holder.lnComentario.setVisibility(View.VISIBLE);
		}else{
			holder.lnComentario.setVisibility(View.GONE);
		}

		// evento para abrir votos
		OnClickListener onclicklistenerVotos = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// buscar detalhes do projeto clicado
				Bundle sendBundle = new Bundle();
				Projeto projeto = (Projeto) v.getTag();
				int id = Integer.valueOf(projeto.getId());
				sendBundle.putInt("idProjeto", id);
				sendBundle.putString("nomePec", projeto.getNome());
				Intent i = new Intent(getContext(), VotosActivity.class);
				i.putExtras(sendBundle);
				getContext().startActivity(i);
			}
		};

		// verifica se já foi votado
		if (projeto.isVotado()) {
			holder.txtPropostaVotado.setVisibility(View.VISIBLE);
			holder.imgVotado.setVisibility(View.VISIBLE);
			holder.txtPropostaVotado.setOnClickListener(onclicklistenerVotos);
			holder.txtPropostaVotado.setTag(projeto);

			holder.imgVotado.setOnClickListener(onclicklistenerVotos);
			holder.imgVotado.setTag(projeto.getId());
		} else {
			holder.txtPropostaVotado.setVisibility(View.INVISIBLE);
			holder.imgVotado.setVisibility(View.INVISIBLE);
		}

		// verifica se tem comentários

		if (projeto.getQtdComentario() > 0) {
			holder.btnComentar.setText("   " + String.valueOf(projeto.getQtdComentario()));
			holder.btnComentar.setTextSize(15);
			holder.imgComentar.setVisibility(View.VISIBLE);
		} else {
			holder.btnComentar.setText("Comentar");
			holder.imgComentar.setVisibility(View.GONE);
		}

		return row;
	}

	static class ProjetoVotoHolder {

		String email;
		TextView txtPropostaTitulo;
		TextView txtPropostaEmenta;
		TextView txtId;
		TextView txtPropostaAutor;
		TextView txtPropostaVotos;
		TextView txtPropostaSim;
		TextView txtPropostaNao;
		Button btnSim;
		Button btnNao;
		ImageButton btnEmail;
		TextView txtPropostaVotado;
		Button btnComentar;
		ImageView imgComentar;
		TextView dtProposicao;
		ImageView imgVotado;
		ProfilePictureView fotoFaceComentario;
		TextView txtNomeComentario;
		TextView txtUserComentario;
		RelativeLayout lnComentario;
	}
}
