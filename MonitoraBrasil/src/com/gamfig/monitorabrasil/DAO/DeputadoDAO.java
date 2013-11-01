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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;

import com.gamfig.monitorabrasil.classes.Comentario;
import com.gamfig.monitorabrasil.classes.Doacao;
import com.gamfig.monitorabrasil.classes.Doador;
import com.gamfig.monitorabrasil.classes.Politico;
import com.gamfig.monitorabrasil.classes.Projeto;
import com.gamfig.monitorabrasil.classes.User;
import com.gamfig.monitorabrasil.classes.Votacao;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DeputadoDAO {

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

	// busca a listagem dos deputados
	public static String connect(String partido, String uf, String i, String query, String idUser) {
		HttpClient httpclient = new DefaultHttpClient();
		String param = "tipo=" + i;
		if (i.equals("2")) {
			param += "&iduser=" + idUser;
		}
		if (!partido.equals("Todos os Partidos")) {
			param += "&partido=" + partido;
		}
		if (!uf.equals("Brasil")) {
			param += "&uf=" + uf;
		}
		if (!query.equals("")) {
			param += "&query=" + query;
		}
		HttpGet httpget = new HttpGet("http://www.gamfig.com/mbrasilwsdl/buscadeputados.php?" + param);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			// Log.i(TAG,response.getStatusLine().toString());
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();
				JSONObject json = new JSONObject(result);
				JSONArray nameArray = json.names();
				JSONArray valArray = json.toJSONArray(nameArray);

				return valArray.getString(0);
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return null;
	}

	public Drawable getFoto(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;

	}

	public static Politico buscaPolitico(int idCadastro) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://www.gamfig.com/mbrasilwsdl/buscapolitico.php?idcadastro="
				+ String.valueOf(idCadastro));
		HttpResponse response;
		Politico politico;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();
				Gson gson = new Gson();
				politico = gson.fromJson(result, Politico.class);

				return politico;
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		return null;

	}

	public Projeto buscaProjeto(String idProjeto) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://www.gamfig.com/mbrasilwsdl/buscaproposicao.php?idProp=" + idProjeto);
		HttpResponse response;
		Projeto projeto;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();
				// JSONObject json = new JSONObject(result);
				// JSONArray nameArray=json.names();
				// JSONArray valArray=json.toJSONArray(nameArray);
				Gson gson = new Gson();
				projeto = gson.fromJson(result, Projeto.class);

				return projeto;
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
			// } catch (JSONException e) {
			//
			// e.printStackTrace();
		}
		return null;
	}

	public static String buscaRanking(String partido, String uf, String tpRank, String ano) {
		HttpClient httpclient = new DefaultHttpClient();
		String param = "&ano=" + ano;
		if (!partido.equals("Todos os Partidos")) {
			param += "&partido=" + partido;
		}
		if (!uf.equals("Brasil")) {
			param += "&uf=" + uf;
		}

		HttpGet httpget = new HttpGet("http://www.gamfig.com/mbrasilwsdl/buscarank.php?tprank=" + tpRank + param);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			// Log.i(TAG,response.getStatusLine().toString());
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();
				JSONObject json = new JSONObject(result);
				JSONArray nameArray = json.names();
				JSONArray valArray = json.toJSONArray(nameArray);

				return valArray.getString(0);
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return null;
	}

	public static User enviaUser(JSONObject jsonObject, String registrationId) {
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost("http://www.gamfig.com/mbrasilwsdl/insereuser.php");

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(1);
		params.add(new BasicNameValuePair("json", jsonObject.toString()));
		params.add(new BasicNameValuePair("gcmId", registrationId));
		HttpResponse response;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			response = httpclient.execute(httppost);
			// Log.i(TAG,response.getStatusLine().toString());
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();
				Gson gson = new Gson();
				User user = gson.fromJson(result, User.class);
				return user;
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {

			e.printStackTrace();
			e.printStackTrace();
		}
		return null;
	}

	public void marcaFavorito(String id, int idCadastro, String acao) {
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost("http://www.gamfig.com/mbrasilwsdl/admfavorito.php");

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(3);
		params.add(new BasicNameValuePair("idfacebook", id));
		params.add(new BasicNameValuePair("idpolitico", String.valueOf(idCadastro)));
		params.add(new BasicNameValuePair("acao", acao));
		HttpResponse response;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				instream.close();

			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void marcaProjetoFavorito(String id, String idProjeto, String acao) {
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost("http://www.gamfig.com/mbrasilwsdl/admfavoritoprojeto.php");

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(3);
		params.add(new BasicNameValuePair("idfacebook", id));
		params.add(new BasicNameValuePair("idprojeto", idProjeto));
		params.add(new BasicNameValuePair("acao", acao));
		HttpResponse response;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				instream.close();
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void salvaAvaliacaoPolitico(String id, String idProjeto, String acao) {
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost("http://www.gamfig.com/mbrasilwsdl/admfavoritoprojeto.php");

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(3);
		params.add(new BasicNameValuePair("idfacebook", id));
		params.add(new BasicNameValuePair("idprojeto", idProjeto));
		params.add(new BasicNameValuePair("acao", acao));
		HttpResponse response;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				instream.close();
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public ArrayList<Projeto> buscaProjetos(String idUser) {

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://www.gamfig.com/mbrasilwsdl/buscaprojetosfavoritos.php?idcadastro="
				+ idUser);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();
				Gson gson = new Gson();
				ArrayList<Projeto> projetos = gson.fromJson(result, new TypeToken<ArrayList<Projeto>>() {
				}.getType());

				return projetos;
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		return null;

	}

	public void avaliaPolitico(String id, int idCadastro, float notaPolitico) {
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost("http://www.gamfig.com/mbrasilwsdl/avaliapolitico.php");

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(3);
		params.add(new BasicNameValuePair("iduser", id));
		params.add(new BasicNameValuePair("idpolitico", String.valueOf(idCadastro)));
		params.add(new BasicNameValuePair("nota", String.valueOf(notaPolitico)));
		HttpResponse response;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				instream.close();
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void avaliaProjeto(String idUser, String idProjeto, float notaProjeto) {
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost("http://www.gamfig.com/mbrasilwsdl/avaliaprojeto.php");

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(3);
		params.add(new BasicNameValuePair("iduser", idUser));
		params.add(new BasicNameValuePair("idprojeto", String.valueOf(idProjeto)));
		params.add(new BasicNameValuePair("nota", String.valueOf(notaProjeto)));
		HttpResponse response;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				instream.close();
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public static boolean temAtualizacao(String dtAtualizacao) {
		if (dtAtualizacao == null)
			return true;
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://www.gamfig.com/mbrasilwsdl/getDataAtualizacao.php?idcadastro="
				+ dtAtualizacao);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();

				return Boolean.valueOf(result);
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		return false;
	}

	public void insereVoto(String mVoto, String mIdProposta, String mIdUser) {
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost("http://www.gamfig.com/mbrasilwsdl/votaproposta.php");

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(3);
		params.add(new BasicNameValuePair("voto", mVoto));
		params.add(new BasicNameValuePair("idProposta", mIdProposta));
		params.add(new BasicNameValuePair("idUser", mIdUser));
		HttpResponse response;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public ArrayList<Projeto> buscaProjetosVoto(int page, String filtro, String uf, String idUser, int idAutor) {
		if (idUser == null)
			idUser = "0";
		if (filtro.equals("Selecione um filtro")) {
			filtro = "0";
		} else if (filtro.equals("Projetos que votei")) {
			filtro = "1";
		} else if (filtro.equals("Projetos que não votei")) {
			filtro = "2";
		} else if (filtro.equals("Projetos Monitorados")) {
			filtro = "3";
		} else if (filtro.equals("Projetos Votados na Câmara")) {
			filtro = "4";
		}else if (filtro.equals("Projetos mais Comentados")) {
			filtro = "5";
		}else if (filtro.equals("Projetos mais Recentes")) {
			filtro = "6";
		}else if (filtro.equals("Projetos mais Votados")) {
			filtro = "7";
		}else if(filtro.equals("8")) {
			filtro = "8&idAutor="+idAutor;
			uf="Brasil";
		}
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://www.gamfig.com/mbrasilwsdl/buscaprojetosvoto.php?page=" + page
				+ "&filtro=" + filtro + "&uf=" + uf + "&iduser=" + idUser);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();
				Gson gson = new Gson();
				ArrayList<Projeto> projetos = gson.fromJson(result, new TypeToken<ArrayList<Projeto>>() {
				}.getType());

				return projetos;
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		return null;
	}

	public void insereComentario(String idUser, String idProposta, String comentario) {
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost("http://www.gamfig.com/mbrasilwsdl/inserecomentario.php");

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(3);
		params.add(new BasicNameValuePair("comentario", comentario));
		params.add(new BasicNameValuePair("idProposta", idProposta));
		params.add(new BasicNameValuePair("idUser", idUser));
		HttpResponse response;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public ArrayList<Comentario> buscaComentarios(String idProposta) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://www.gamfig.com/mbrasilwsdl/buscacomentarios.php?id=" + idProposta);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();
				Gson gson = new Gson();
				ArrayList<Comentario> projetos = gson.fromJson(result, new TypeToken<ArrayList<Comentario>>() {
				}.getType());

				return projetos;
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		return null;
	}

	public Votacao buscaVotacao(String idProposta, String partido, String uf, String voto) {
		
		if(partido.equals("Todos os Partidos")){
			partido = "0";
		}
		
		HttpClient httpclient = new DefaultHttpClient();
		String link ="http://www.gamfig.com/mbrasilwsdl/buscavotacao.php?id=" + idProposta+"&partido="+partido+"&uf="+uf+"&voto="+voto;
		HttpGet httpget = new HttpGet(link);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();
				Gson gson = new Gson();
				Votacao votacao = gson.fromJson(result, new TypeToken<Votacao>() {
				}.getType());

				return votacao;
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		return null;
	}

	public User atualizaUser(int idUser, String registrationId) {
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost("http://www.gamfig.com/mbrasilwsdl/atualizauser.php");

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(1);
		params.add(new BasicNameValuePair("idUser", String.valueOf(idUser)));
		params.add(new BasicNameValuePair("gcmId", registrationId));
		HttpResponse response;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			response = httpclient.execute(httppost);
			// Log.i(TAG,response.getStatusLine().toString());
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();
				Gson gson = new Gson();
				User user = gson.fromJson(result, User.class);
				return user;
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {

			e.printStackTrace();
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<Doacao> buscaDoacoesAgrupadas(int idCadastro) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://www.gamfig.com/mbrasilwsdl/getdoacaoagrupada.php?id=" + idCadastro);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();
				Gson gson = new Gson();
				ArrayList<Doacao> doacoes = gson.fromJson(result, new TypeToken<ArrayList<Doacao>>() {
				}.getType());

				return doacoes;
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		return null;
	}

	public ArrayList<Doador> buscaDoacoesEspecifica(int idCadastro, int idTipo) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://www.gamfig.com/mbrasilwsdl/getdoacaodetalhe.php?id=" + idCadastro+"&tipo="+idTipo);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();
				Gson gson = new Gson();
				ArrayList<Doador> doadores = gson.fromJson(result, new TypeToken<ArrayList<Doador>>() {
				}.getType());

				return doadores;
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		return null;
	}

	public static String buscaCotas(String ano, String mes, String idSubcota) {
		HttpClient httpclient = new DefaultHttpClient();
		String param="";
		if(idSubcota != null)
			param = "?idsubcota="+idSubcota;
		if(!mes.equals("Mês")){
			if(param.length()>0)
				param=param+"&mes="+mes;
			else
				param="?mes="+mes;
		}
		HttpGet httpget = new HttpGet("http://www.gamfig.com/mbrasilwsdl/buscacota.php"+param);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			// Log.i(TAG,response.getStatusLine().toString());
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();
				JSONObject json = new JSONObject(result);
				JSONArray nameArray = json.names();
				JSONArray valArray = json.toJSONArray(nameArray);

				return valArray.getString(0);
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return null;
	}

}
