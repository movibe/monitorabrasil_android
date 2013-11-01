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
package com.gamfig.monitorabrasil.DAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.gamfig.monitorabrasil.R;
import com.gamfig.monitorabrasil.classes.AvaliacaoPolitico;
import com.gamfig.monitorabrasil.classes.AvaliacaoProjeto;
import com.gamfig.monitorabrasil.classes.Politico;
import com.gamfig.monitorabrasil.classes.Projeto;
import com.gamfig.monitorabrasil.classes.User;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UserDAO extends AsyncTask<Context, Void, User> {
	private GraphUser me;
	private User meGoogle;
	private String registrationId;
	private int idUser;
	private Context context;

	public UserDAO(GraphUser me, String gcmid, int idUser) {
		this.me = me;
		this.registrationId = gcmid;
		this.idUser = idUser;
	}
	public UserDAO() {

	}
	
	public UserDAO(Person user, String gcmid, int idUser) {
		
		this.registrationId = gcmid;
		this.idUser = idUser;
		meGoogle = new User();
		meGoogle.setNome(user.getNickname());
		meGoogle.setDtAniversario(user.getBirthday());
		meGoogle.setEmail(user.getEmails().get(0).toString());
		meGoogle.setCidade(user.getCurrentLocation());
		meGoogle.setSobreMim(user.getAboutMe());
		meGoogle.setIdGoogle(user.getId());
		meGoogle.setTipoConta("google");
	}
	@Override
	protected User doInBackground(Context... params) {
		context = params[0];
		// envia informacao para o servidor do user
		// retorna o id do usuário
		// se tiver nao precisa mais
		if (idUser > 0) {
			return new DeputadoDAO().atualizaUser(idUser, this.registrationId);
		} else {
			
			return new DeputadoDAO().enviaUser(me.getInnerJSONObject(), this.registrationId);
		}

	}

	protected void onPostExecute(User user) {

		// inserir no SharedPreferences
		Gson gson = new Gson();

		SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.id_key_preferencias),Context.MODE_PRIVATE);
		Integer idCadastro = gson.fromJson(sharedPref.getString(context.getString(R.string.id_key_idcadastro), null),
				new TypeToken<Integer>() {
				}.getType());
		if (idCadastro == null) {

			// salva o id
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString(context.getString(R.string.id_key_idcadastro), String.valueOf(user.getId()));

			//salvar o id da conta utilizada
			editor.putString(context.getString(R.string.id_key_conta), String.valueOf(user.getIdFacebook()));
			//salvar nome
			editor.putString(context.getString(R.string.nome_conta), String.valueOf(user.getNome()));
			//salva o tipo da conta
			editor.putString(context.getString(R.string.tipo_conta), String.valueOf(user.getTipoConta()));
			
			// salvar politicos favoritos
			List<String> politicosFav = new ArrayList<String>();
			if (user.getPoliticos() != null) {
				for (Politico politico : user.getPoliticos()) {
					politicosFav.add(String.valueOf(politico.getIdCadastro()));
				}
			}
			editor.putString(context.getString(R.string.id_key_favoritos), gson.toJson(politicosFav));

			// salvar projetos favoritos
			List<String> projetosFav = new ArrayList<String>();
			if (user.getProjetos() != null) {
				for (Projeto projeto : user.getProjetos()) {
					projetosFav.add(String.valueOf(projeto.getId()));
				}
			}
			editor.putString(context.getString(R.string.id_key_projetos_favoritos), gson.toJson(projetosFav));

			// salvar as avaliacoes de politicos
			HashMap<Integer, Float> avaliacaoPolitico = new HashMap<Integer, Float>();
			if (user.getAvaliacaoPolitico() != null) {
				for (AvaliacaoPolitico avalPolitico : user.getAvaliacaoPolitico()) {
					avaliacaoPolitico.put(avalPolitico.getIdPolitico(), avalPolitico.getNotaAvaliacao());

				}
			}
			editor.putString(context.getString(R.string.id_key_avaliacao_politicos), gson.toJson(avaliacaoPolitico));

			// salvar as avaliacoes de projetos
			HashMap<String, Float> avaliacaoProjeto = new HashMap<String, Float>();
			if (user.getAvaliacaoProjeto() != null) {
				for (AvaliacaoProjeto avalProjeto : user.getAvaliacaoProjeto()) {
					avaliacaoProjeto
							.put(String.valueOf(avalProjeto.getIdProjeto()), avalProjeto.getNotaAvaliacao());
				}
			}

			editor.putString(context.getString(R.string.id_key_avaliacao_projetos), gson.toJson(avaliacaoProjeto));

			editor.commit();

		}
	}

	// verificar se está logado 
	public boolean isConnect() {
		//conectado no facebook
		Session currentSession = Session.getActiveSession();
		if (currentSession != null && currentSession.isOpened()) {
			return true;
		}		
		return false;
	}

	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public void enviaUser(JSONObject jsonObject) {
		// enviar o json para o servidor
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://www.gamfig.com/mbrasilwsdl/insereuser.php");

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(1);
		params.add(new BasicNameValuePair("json", jsonObject.toString()));
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			// Execute and get the response.
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				try {
					// do something useful
				} finally {
					instream.close();
				}
			}
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}
}
