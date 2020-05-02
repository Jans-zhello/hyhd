package com.zzz.app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Hashtable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.client.R;
import com.zzz.server.MessageHandle;
import com.zzz.util.Tools;

public class RegisterActivity extends Activity {

	EditText uname = null;
	EditText password1 = null;
	EditText password2 = null;
	EditText address = null;
	EditText phone = null;
	RadioButton men = null;
	RadioButton wmen = null;
	Button submit = null;
	Button back = null;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.reg);
		uname = (EditText) findViewById(R.id.uname_editText);
		password1 = (EditText) findViewById(R.id.password1_editText);
		password2 = (EditText) findViewById(R.id.password2_editText);
		address = (EditText) findViewById(R.id.address_editText);
		phone = (EditText) findViewById(R.id.phone_editText);
		men = (RadioButton) findViewById(R.id.radio_men);
		wmen = (RadioButton) findViewById(R.id.radio_wmen);
		submit = (Button) findViewById(R.id.submit_button2);
		back = (Button) findViewById(R.id.back_button);
		// 如下两行代码解决socket连接不上的问题
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		submit.setOnClickListener(new SubmitButtonOnClickListener());
		back.setOnClickListener(new BackButtonOnClickListener());
	}

	class SubmitButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (!password1.getText().toString()
					.equals(password2.getText().toString())) {
				Toast.makeText(RegisterActivity.this, "密码不一致!",
						Toast.LENGTH_LONG).show();
				return;
			}
			Socket socket = null;
			try {
				socket = new Socket(Tools.IP, Tools.PORT_1);
				ObjectOutputStream oout = new ObjectOutputStream(
						socket.getOutputStream());
				ObjectInputStream oin = new ObjectInputStream(
						socket.getInputStream());

				MessageHandle m1 = new MessageHandle();
				Hashtable table = new Hashtable();
				table.put("uname", uname.getText().toString());

				if (men.isChecked()) {
					table.put("sex", "男");
				} else {

					table.put("sex", "女");
				}

				table.put("phone", phone.getText().toString());
				table.put("address", address.getText().toString());
				table.put("password", password1.getText().toString());
				m1.setValue(table);
				m1.setType(m1.REGISTER);

				oout.writeObject(m1);
				oout.flush();
				m1 = (MessageHandle) oin.readObject();
				if (m1.getReturnvalue().get("message").toString()
						.equalsIgnoreCase("register ok!")) {
					final String userid = m1.getReturnvalue().get("userid")
							.toString();
					AlertDialog.Builder bb = new AlertDialog.Builder(
							RegisterActivity.this);
					bb.setTitle("恭喜您，注册成功！");
					bb.setMessage("您的ID是：" + userid);
					bb.setNeutralButton("马上登录",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent in = new Intent();
									in.setClass(RegisterActivity.this,
											LoginActivity.class);
									in.putExtra("userid",userid);
									startActivity(in);
									RegisterActivity.this.finish();

								}
							});

					bb.setNegativeButton("退出",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									System.exit(0);

								}
							});
					bb.create().show();

				} else if (m1.getReturnvalue().get("message").toString()
						.equalsIgnoreCase("is registered")) {
					Toast.makeText(RegisterActivity.this, "用户名已存在!",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(RegisterActivity.this, "系统故障,注册失败",
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(RegisterActivity.this, "网络不通!",
						Toast.LENGTH_LONG).show();

			} finally {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}

	}

	class BackButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
			finish();
		}

	}
}
