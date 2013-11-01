package com.gamfig.monitorabrasil;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.gamfig.monitorabrasil.DAO.UserDAO;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;

public class SignInActivity extends Activity implements OnClickListener, PlusClient.ConnectionCallbacks,
		PlusClient.OnConnectionFailedListener, PlusClient.OnAccessRevokedListener {

	private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;

	private static final int REQUEST_CODE_SIGN_IN = 1;
	private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 2;

	private TextView mSignInStatus;
	private PlusClient mPlusClient;
	private SignInButton mSignInButton;
	private View mSignOutButton;
	private View mRevokeAccessButton;
	private ConnectionResult mConnectionResult;
	private Session currentSession;
	// facebook
	ProfilePictureView profilePicFacebook;
	// google
	ImageView profilePicGoogle;
	// layouts
	private RelativeLayout rlGoogle;
	private RelativeLayout rlFacebook;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);

		mPlusClient = new PlusClient.Builder(this, this, this).setScopes(Scopes.PLUS_LOGIN)
				.setVisibleActivities("http://schemas.google.com/AddActivity").build();

		mSignInStatus = (TextView) findViewById(R.id.sign_in_status);
		mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
		mSignInButton.setOnClickListener(this);
		mSignOutButton = findViewById(R.id.sign_out_button);
		mSignOutButton.setOnClickListener(this);
		mRevokeAccessButton = findViewById(R.id.revoke_access_button);
		mRevokeAccessButton.setOnClickListener(this);

		rlGoogle = (RelativeLayout) findViewById(R.id.rlGoogle);
		rlFacebook = (RelativeLayout) findViewById(R.id.rlFacebook);

		profilePicFacebook = (ProfilePictureView) findViewById(R.id.profilepic2);
		profilePicGoogle = (ImageView) findViewById(R.id.profilePicGoogle);

		LoginButton btnFace = (LoginButton) findViewById(R.id.loginFacebook);
		btnFace.setReadPermissions(Arrays.asList("user_location", "user_hometown", "user_education_history", "email",
				"user_about_me", "user_birthday"));
		if (savedInstanceState == null) {
			btnFace.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
				@Override
				public void onUserInfoFetched(GraphUser user) {
					fetchUserInfo();
				}
			});
		}
	}
	private boolean jaCadastrou() {
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.id_key_preferencias),
				MODE_PRIVATE);
		String id = sharedPref.getString(getString(R.string.id_key_idcadastro), null);
		if (id == null) {
			return false;
		} else {
			if (id.equals("null"))
				return false;
			Integer idCadastro = Integer.valueOf(id);
			if (idCadastro > -1)
				return true;
		}

		return false;
	}
	private void fetchUserInfo() {
		currentSession = Session.getActiveSession();
		if (currentSession != null && currentSession.isOpened()) {
			Request request = Request.newMeRequest(currentSession, new Request.GraphUserCallback() {
				@Override
				public void onCompleted(GraphUser me, Response response) {
					if (response.getRequest().getSession() == currentSession) {
						rlGoogle.setVisibility(View.GONE);
						mSignInStatus.setText("Olá, " + me.getName());
						String idFacebook = me.getId();

						profilePicFacebook.setProfileId(idFacebook);
						profilePicFacebook.setVisibility(View.VISIBLE);
						if (!jaCadastrou()) {
							new UserDAO(me,null,0).execute(getApplicationContext());
						}
					}
				}

				
			});
			request.executeAsync();
		} else {
			profilePicFacebook.setVisibility(View.INVISIBLE);
			rlGoogle.setVisibility(View.VISIBLE);
			mSignInStatus.setText(R.string.default_status);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		mPlusClient.connect();
	}

	@Override
	public void onStop() {
		mPlusClient.disconnect();
		super.onStop();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.sign_in_button:
			int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
			if (available != ConnectionResult.SUCCESS) {
				showDialog(DIALOG_GET_GOOGLE_PLAY_SERVICES);
				return;
			}

			try {
				mSignInStatus.setText(getString(R.string.signing_in_status));
				mConnectionResult.startResolutionForResult(this, REQUEST_CODE_SIGN_IN);
			} catch (IntentSender.SendIntentException e) {
				// Fetch a new result to start.
				mPlusClient.connect();
			}
			break;
		case R.id.sign_out_button:
			if (mPlusClient.isConnected()) {
				mPlusClient.clearDefaultAccount();
				mPlusClient.disconnect();
				mPlusClient.connect();
				rlFacebook.setVisibility(View.VISIBLE);
				profilePicGoogle.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.revoke_access_button:
			if (mPlusClient.isConnected()) {
				mPlusClient.revokeAccessAndDisconnect(this);
				updateButtons(false /* isSignedIn */);
			}
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id != DIALOG_GET_GOOGLE_PLAY_SERVICES) {
			return super.onCreateDialog(id);
		}

		int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (available == ConnectionResult.SUCCESS) {
			return null;
		}
		if (GooglePlayServicesUtil.isUserRecoverableError(available)) {
			return GooglePlayServicesUtil.getErrorDialog(available, this, REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES);
		}
		return new AlertDialog.Builder(this).setMessage(R.string.plus_generic_error).setCancelable(true).create();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// facebook
		if (data != null) {
			super.onActivityResult(requestCode, resultCode, data);
			Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		}
		// google
		if (requestCode == REQUEST_CODE_SIGN_IN || requestCode == REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES) {
			if (resultCode == RESULT_OK && !mPlusClient.isConnected() && !mPlusClient.isConnecting()) {
				// This time, connect should succeed.
				mPlusClient.connect();
			}
		}
	}

	@Override
	public void onAccessRevoked(ConnectionResult status) {
		if (status.isSuccess()) {
			mSignInStatus.setText(R.string.revoke_access_status);
		} else {
			mSignInStatus.setText(R.string.revoke_access_error_status);
			mPlusClient.disconnect();
		}
		mPlusClient.connect();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		rlFacebook.setVisibility(View.GONE);
		profilePicFacebook.setVisibility(View.INVISIBLE);
		Person user = mPlusClient.getCurrentPerson();
		String currentPersonName = user != null ? user.getDisplayName() : getString(R.string.unknown_person);
		mSignInStatus.setText(getString(R.string.signed_in_status, currentPersonName));
		// carregar foto
		if (user != null) {
			if (!jaCadastrou()) {
				new UserDAO(user,null,0).execute(getApplicationContext());
				// new enviaUser(me, registrationId, 0).execute();
			}
			String[] urlFoto = new String[] { "https://plus.google.com/s2/photos/profile/" + user.getId() + "?sz=60" };
			new buscaFoto().execute(urlFoto);
		}
		
		updateButtons(true /* isSignedIn */);
	}

	@Override
	public void onDisconnected() {
		mSignInStatus.setText(R.string.loading_status);
		mPlusClient.connect();
		rlFacebook.setVisibility(View.VISIBLE);
		updateButtons(false /* isSignedIn */);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		mConnectionResult = result;
		updateButtons(false /* isSignedIn */);
	}

	private void updateButtons(boolean isSignedIn) {
		if (isSignedIn) {
			mSignInButton.setVisibility(View.INVISIBLE);
			mSignOutButton.setEnabled(true);
			mRevokeAccessButton.setEnabled(true);
		} else {
			if (mConnectionResult == null) {
				// Disable the sign-in button until onConnectionFailed is called
				// with result.
				mSignInButton.setVisibility(View.INVISIBLE);
				mSignInStatus.setText(getString(R.string.loading_status));
			} else {
				// Enable the sign-in button since a connection result is
				// available.
				mSignInButton.setVisibility(View.VISIBLE);
				mSignInStatus.setText(getString(R.string.signed_out_status));
			}

			mSignOutButton.setEnabled(false);
			mRevokeAccessButton.setEnabled(false);
		}
	}

	class buscaFoto extends AsyncTask<String, Void, Drawable> {

		protected Drawable doInBackground(String... urls) {
			Drawable thumb_d = null;
			try {
				URL thumb_u = new URL(urls[0]);

				try {
					thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception e) {

				return null;
			}
			return thumb_d;
		}

		protected void onPostExecute(Drawable thumb_d) {
			profilePicGoogle.setImageDrawable(thumb_d);
			profilePicGoogle.setVisibility(View.VISIBLE);
			profilePicFacebook.setVisibility(View.INVISIBLE);
		}
	}
}
