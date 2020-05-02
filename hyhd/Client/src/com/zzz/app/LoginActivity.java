package com.zzz.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.client.R;
import com.zzz.net.LoginClient;
import com.zzz.util.Tools;

public class LoginActivity extends Activity {

	Button login = null;
	Button register = null;
	EditText username = null;
	EditText password = null;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		login = (Button) findViewById(R.id.submit_button);
		register = (Button) findViewById(R.id.reg_button);
		username = (EditText) findViewById(R.id.username_editText);
		password = (EditText) findViewById(R.id.password_editText);
		login.setOnClickListener(new LoginOnClickListener());
		register.setOnClickListener(new RegisterOnClickListener());
		// 如下两行代码解决socket连接不上的问题
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		String userid = this.getIntent().getStringExtra("userid");
		if (userid != null) {
			username.setText(userid);
		}

	}

	/**
	 * 菜单键的实现(右上角的三个点)
	 * 
	 * 设置服务器参数 简便ip地址一直更换
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("设置服务器信息").setOnMenuItemClickListener(
				new SettingOnMenuItemClickListener());
		return super.onCreateOptionsMenu(menu);
	}

	class SettingOnMenuItemClickListener implements OnMenuItemClickListener {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub

			AlertDialog.Builder buil = new AlertDialog.Builder(
					LoginActivity.this);
			buil.setTitle("设置服务器信息");
			LinearLayout line = (LinearLayout) LayoutInflater.from(
					LoginActivity.this).inflate(R.layout.set, null);

			final EditText ip = (EditText) line.findViewById(R.id.ip_editText);
			final EditText port1 = (EditText) line
					.findViewById(R.id.port1_editText);
			final EditText port2 = (EditText) line
					.findViewById(R.id.port2_editText);
			final EditText port3 = (EditText) line
					.findViewById(R.id.port3_editText);
			ip.setText(Tools.IP);
			port1.setText(Tools.PORT_1 + "");
			port2.setText(Tools.PORT_2 + "");
			port3.setText(Tools.PORT_3 + "");

			buil.setView(line);

			buil.setNeutralButton("设置", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					try {
						Tools.IP = ip.getText().toString();
						Tools.PORT_1 = Integer.parseInt(port1.getText()
								.toString());
						Tools.PORT_2 = Integer.parseInt(port2.getText()
								.toString());
						Tools.PORT_3 = Integer.parseInt(port3.getText()
								.toString());
						Toast.makeText(LoginActivity.this, "设置成功！",
								Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(LoginActivity.this, "设置失败！",
								Toast.LENGTH_LONG).show();
					}

				}
			});
			buil.setNegativeButton("退出", null);
			buil.create().show();

			return false;
		}

	}

	class LoginOnClickListener implements OnClickListener {
		public void onClick(View v) {
			try {
				String message = LoginClient.login(username.getText()
						.toString(), password.getText().toString());
				if (message.equalsIgnoreCase("ok")) {
					Toast.makeText(LoginActivity.this, "登录成功！",
							Toast.LENGTH_LONG).show();
					startActivity(new Intent(LoginActivity.this,
							ConsoleActivity.class));
					finish();
				} else if (message.equalsIgnoreCase("not activated")) {
					Toast.makeText(LoginActivity.this, "新用户，没有激活！",
							Toast.LENGTH_LONG).show();
				} else if (message.equalsIgnoreCase("password error")) {
					Toast.makeText(LoginActivity.this, "密码错误！",
							Toast.LENGTH_LONG).show();
					password.setText("");
					password.requestFocus();
				} else if (message.equalsIgnoreCase("notUser")) {
					Toast.makeText(LoginActivity.this, "没有此用户！",
							Toast.LENGTH_LONG).show();
					username.setText("");
					username.requestFocus();
					password.setText("");

				}
			} catch (Exception e) {
				Toast.makeText(LoginActivity.this, "服务连接失败！", Toast.LENGTH_LONG)
						.show();
			}

		}

	}

	class RegisterOnClickListener implements OnClickListener {
		public void onClick(View arg0) {
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			finish();
		}

	}
}
