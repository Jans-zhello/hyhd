package com.zzz.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client.R;
import com.zzz.db.HuiFuInfo;
import com.zzz.db.TitleInfo;
import com.zzz.net.InformClient;
import com.zzz.net.LoginClient;
import com.zzz.server.MessageHandle;
import com.zzz.util.Tools;

public class ConsoleActivity extends Activity {
	Button addButton = null;
	Button sendButton = null;// ���Ͱ�ť
	Button VoiceButton = null;// ������ť
	Button CameraButton = null;// �������ť
	EditText editText = null;// �༭������
	ScrollView s = null;// ���������
	LinearLayout list_linearLayout = null;
	RelativeLayout.LayoutParams lay = null;
	LinearLayout line = null;// ��ҳ���ܲ���
	View v = null;
	File extDir = Environment.getExternalStorageDirectory();
	File imagedir = null;
	File imageDir2 = null;
	String imageFilename = "";//ͼƬ�ļ�����
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.console);
		addButton = (Button) findViewById(R.id.jiahao_button);
		sendButton = (Button) findViewById(R.id.sendButton);
		editText = (EditText) findViewById(R.id.txt_editText);
		s = (ScrollView) findViewById(R.id.list_scrollView);
		line = (LinearLayout) findViewById(R.id.menu_linearLayout);
		list_linearLayout = (LinearLayout) findViewById(R.id.list_linearLayout);
		v = (View) LayoutInflater.from(ConsoleActivity.this).inflate(
				R.layout.menus, null);
		lay = (LayoutParams) s.getLayoutParams();
		VoiceButton = (Button) v.findViewById(R.id.amrbutton);
		CameraButton = (Button) v.findViewById(R.id.imagebutton);
		InformClient.action = this;
		// �������д�����socket���Ӳ��ϵ�����
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		addButton.setOnClickListener(new AddOnClickListener());
		sendButton.setOnClickListener(new SendOnClickListener());
		VoiceButton.setOnTouchListener(new VoiceButtonOnTouchListener());
		CameraButton.setOnClickListener(new CameraButtonOnClickListener());
	}

	/**
	 * ʵ��΢�Ź��ܵ��+�� ����������ͼƬ�Ի���
	 */
	class AddOnClickListener implements OnClickListener {
		boolean flag = true;

		public void onClick(View arg0) {
			if (flag) {
				line.addView(v);
				int m = lay.bottomMargin + 55;
				lay.bottomMargin = m;
				s.setLayoutParams(lay);
				addButton.setText("��");
				flag = false;
			} else {
				line.removeView(v);
				int m = lay.bottomMargin - 55;
				lay.bottomMargin = m;
				s.setLayoutParams(lay);
				addButton.setText("��");
				flag = true;
			}
		}
	}

	/**
	 * 
	 * �����ı����ݵ����ݿ�ͷ�����
	 */
	class SendOnClickListener implements OnClickListener {
		public void onClick(View arg0) {
			if (editText.getText().toString().trim().equals("")) {
				return;
			}
			
			MessageHandle msg = new MessageHandle();
			/***
			 * �ж�Ҫ�ظ���titleid�Ƿ�Ϊ0
			 * Ϊ0:���Ƿ�������
			 * ��Ϊ0:���ǻظ�����
			 */
			if (titleid == 0) {
				msg.setType(msg.SEND_TEXT);
				Hashtable t = new Hashtable();
				t.put("userid", Tools.userid);
				t.put("titletxt", editText.getText().toString());
				msg.setValue(t);
				LoginClient.send(msg);
				editText.setText("");
			}else {
				msg.setType(msg.HUIFU_TEXT);
				Hashtable t = new Hashtable();
				t.put("userid", Tools.userid);
				t.put("txt", editText.getText().toString());
				t.put("titleid", titleid);
				msg.setValue(t);
				LoginClient.send(msg);
				editText.setText("");
				titleid = 0;
				ConsoleActivity.this.setTitle(R.string.app_name);
			}
			
		}
	}

	/**
	 * ������Ƶ�ļ������ݿ�ͷ�����
	 * 
	 * @author Tiny
	 * 
	 */
	class VoiceButtonOnTouchListener implements OnTouchListener {

		MediaRecorder recorder = null;
		String filename = "";
		File dir = null;
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub

			if (arg1.getAction() == 0) {
				VoiceButton.setText(">>");
				Toast.makeText(ConsoleActivity.this, "����¼��!",
						Toast.LENGTH_SHORT).show();
				filename = new Date().getTime() + ".amr";
				dir = new File(extDir + "/amrs");
				if (!dir.exists()) {
					dir.mkdir();
				}
                System.out.println("�Ƿ����?"+dir.exists());
				try {
					recorder = new MediaRecorder();
					recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					recorder.setOutputFile(dir+File.separator+filename);
					recorder.prepare();
					recorder.start();
				} catch (IllegalStateException e) {
					System.out.println("������");
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("IO������");
				}
				
			} else if (arg1.getAction() == 1) {
				try {
					VoiceButton.setText("¼��");
					recorder.stop();
					recorder.reset();
					File f = new File(dir+File.separator+filename);
					Socket socket = new Socket(Tools.IP, Tools.PORT_3);
					InputStream in = socket.getInputStream();
					OutputStream out = socket.getOutputStream();
					// �ͻ���:���������͵��ļ�,��Ҫ�ϴ�
					out.write(("upload,amr," + f.length()).getBytes());
					out.flush();
					// �ͻ���:�յ��㷢��������
					byte[] b = new byte[1024 * 10];
					in.read(b);
					String serverInfo = new String(b).trim();
					// �ͻ���:��Ҫ����������Ƿ���
					FileInputStream fin = new FileInputStream(f);
					int len = 0;
					while ((len = fin.read(b)) != -1) {
						out.write(b, 0, len);
						out.flush();
					}
					fin.close();
					out.close();
					in.close();
					MessageHandle msg = new MessageHandle();
					if (titleid == 0) {
						msg.setType(msg.SEND_AMR);
						Hashtable table = new Hashtable();
						table.put("titletxt", serverInfo.split(",")[1]);
						table.put("userid", Tools.userid);
						msg.setValue(table);
					}else {
						msg.setType(msg.HUIFU_AMR);
						Hashtable table = new Hashtable();
						table.put("txt", serverInfo.split(",")[1]);
						table.put("userid", Tools.userid);
						table.put("titleid", titleid);
						msg.setValue(table);
                        titleid = 0;
                        ConsoleActivity.this.setTitle(R.string.app_name);
					}
					f.delete();
					LoginClient.send(msg);
					Toast.makeText(ConsoleActivity.this, "�ϴ��ɹ�",
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(ConsoleActivity.this, "�ϴ�ʧ��!",
							Toast.LENGTH_SHORT).show();
				}
			}
			return true;
		}

	}
	/**
	 * ����ͼƬ�ļ��������������ݿ�
	 */
	class CameraButtonOnClickListener implements OnClickListener{
		public void onClick(View arg0) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			imageFilename = new Date().getTime() + ".jpg";
			imagedir = new File(extDir + "/images");
			if (!imagedir.exists()) {
				imagedir.mkdir();
			}
			File ff = new File(imagedir, imageFilename);
			Uri u = Uri.fromFile(ff);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
			startActivityForResult(intent, 0);	
			
		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != 0 ) {//�ж��Ƿ�������Ƭ�����
		        return;	
 		}
	   new Thread(){
		   
		  public void run() {
				try {
					File f = new File(imagedir+File.separator+imageFilename);
/*					*//**
					 * ͼƬ����ͼ����
					 *//*
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;

					Bitmap bitmap = BitmapFactory.decodeFile(f.getPath(),
							options);
					options.inJustDecodeBounds = false;
					int be = (int) (options.outHeight / (float) 300);
					if (be <= 0)
						be = 1;
					options.inSampleSize = be;
					//������ϱ�������·��
					imageDir2 = new File(imagedir + "/edit");
					if (!imageDir2.exists()) {
						imageDir2.mkdir();
					}
					File file2 = new File(imageDir2+imageFilename);
					try {
						FileOutputStream out = new FileOutputStream(file2);
						if (bitmap.compress(Bitmap.CompressFormat.JPEG,
								100, out)) {
							out.flush();
							out.close();
						}
					} catch (Exception e) {

					}*/
					Socket socket = new Socket(Tools.IP, Tools.PORT_3);
					InputStream in = socket.getInputStream();
					OutputStream out = socket.getOutputStream();
					// �ͻ���:���������͵��ļ�,��Ҫ�ϴ�
					out.write(("upload,image," + f.length()).getBytes());
					out.flush();
					// �ͻ���:�յ��㷢��������
					byte[] b = new byte[1024 * 10];
					in.read(b);
					String serverInfo = new String(b).trim();
					// �ͻ���:��Ҫ����������Ƿ���
					FileInputStream fin = new FileInputStream(f);
					int len = 0;
					while ((len = fin.read(b)) != -1) {
						out.write(b, 0, len);
						out.flush();
					}
					fin.close();
					out.close();
					in.close();
					MessageHandle msg = new MessageHandle();
					if (titleid == 0) {
						msg.setType(msg.SEND_IMAGE);
						Hashtable table = new Hashtable();
						table.put("titletxt", serverInfo.split(",")[1]);
						table.put("userid", Tools.userid);
						msg.setValue(table);
					}else {
						msg.setType(msg.HUIFU_IMAGE);
						Hashtable table = new Hashtable();
						table.put("txt", serverInfo.split(",")[1]);
						table.put("userid", Tools.userid);
						table.put("titleid", titleid);
						msg.setValue(table);
                        titleid = 0;
                        ConsoleActivity.this.setTitle(R.string.app_name);
					}
					f.delete();
//					file2.delete();
					LoginClient.send(msg);
					Toast.makeText(ConsoleActivity.this, "�ϴ��ɹ�",
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(ConsoleActivity.this, "�ϴ�ʧ��!",
							Toast.LENGTH_SHORT).show();
				}
			  
		  }; 
		   
	   }.start();
		
	}
	/**
	 * ������Ϣ��������ʾ�ڿͻ��ˣ���֪ͨ�����ͻ���Ҳ��ʾ
	 */
	public Vector<TitleInfo> vv1 = null;
	public Handler hand = new Handler() {

		public void handleMessage(android.os.Message msg) {
			loadData(vv1);
		};
	};

	public int titleid = 0;//ȷ��Ҫ�ظ���titleid

	public void loadData(Vector<TitleInfo> v) {
		list_linearLayout.removeAllViews();
		System.gc();
		for (final TitleInfo titleInfo : v) {
		LinearLayout line1 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.title_item,null);
		LinearLayout infolLayout = (LinearLayout) line1.findViewById(R.id.info_linearLayout);
		TextView time = (TextView) line1.findViewById(R.id.time_textView);
		TextView uname = (TextView) line1.findViewById(R.id.uname_textView);
		uname.setText(titleInfo.getUname());
		uname.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ConsoleActivity.this.setTitle("��ѡ��["
						+ titleInfo.getUname() + "]�����Իظ���");
				titleid = titleInfo.getTitleid();

			}
		});
		if (titleInfo.getType().equalsIgnoreCase(MessageHandle.SEND_TEXT)) {
			TextView t = new TextView(ConsoleActivity.this);
			t.setText(titleInfo.getTitletxt());
			infolLayout.addView(t);
		}else if (titleInfo.getType().equalsIgnoreCase(MessageHandle.SEND_IMAGE)) {
			final MyImageButton t = new MyImageButton(ConsoleActivity.this);
			t.setImageResource(R.drawable.get);
			t.setFilename(titleInfo.getTitletxt());
			t.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						File f = new File(extDir+"/images", t.getFilename());
						if (!f.exists()) {
							Socket socket = new Socket(Tools.IP,
									Tools.PORT_3);
							InputStream in = socket.getInputStream();
							OutputStream out = socket.getOutputStream();
							out.write(("download,image," + t
									.getFilename()).getBytes());
							out.flush();
							byte[] b = new byte[1024];
							in.read(b);
							long size = Long.parseLong(new String(b)
									.trim());

							out.write("OK".getBytes());
							out.flush();

							FileOutputStream outf = new FileOutputStream(
									f);
							int len = 0;
							long length = 0;
							while ((len = in.read(b)) != -1) {
								length += len;
								outf.write(b, 0, len);
								if (length >= size) {
									break;
								}
							}
							outf.close();
							in.close();
							out.close();

						}

						final Dialog d = new Dialog(
								ConsoleActivity.this);
						ImageView image = new ImageView(
								ConsoleActivity.this);
						image.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								d.hide();
							}
						});
						image.setImageURI(Uri.fromFile(f));

						d.setContentView(image);
						d.show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			infolLayout.addView(t);	
		}else if (titleInfo.getType().equalsIgnoreCase(MessageHandle.SEND_AMR)) {
			final MyButton t = new MyButton(ConsoleActivity.this);
			t.setText("����¼��");
			t.setFilename(titleInfo.getTitletxt());
			/**
			 * �������¼��
			 */
			t.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						File f = new File(extDir+"/amrs", t.getFilename());
						if (!f.exists()) {
							Socket socket = new Socket(Tools.IP,
									Tools.PORT_3);
							InputStream in = socket.getInputStream();
							OutputStream out = socket.getOutputStream();
							out.write(("download,amr," + t
									.getFilename()).getBytes());
							out.flush();
							byte[] b = new byte[1024];
							in.read(b);
							long size = Long.parseLong(new String(b)
									.trim());

							out.write("OK".getBytes());
							out.flush();

							FileOutputStream outf = new FileOutputStream(
									f);
							int len = 0;
							long length = 0;
							while ((len = in.read(b)) != -1) {
								length += len;
								outf.write(b, 0, len);
								if (length >= size) {
									break;
								}
							}
							outf.close();
							in.close();
							out.close();
						}
						MediaPlayer mediaPlayer = new MediaPlayer();
						if (mediaPlayer.isPlaying()) {
							mediaPlayer.reset();// ����Ϊ��ʼ״̬
						}
						mediaPlayer.setDataSource(f.getPath());
						mediaPlayer.prepare();// ����
						mediaPlayer.start();// ��ʼ��ָ�����
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			infolLayout.addView(t);
		}
		time.setText("ʱ��:"+titleInfo.getSendcreate());
		/**
		 * ����ظ�������ʾ
		 */
		for (HuiFuInfo huifu : titleInfo.getHf()) {
			LinearLayout line2 = (LinearLayout) LayoutInflater.from(this)
					.inflate(R.layout.huifu_item, null);
			LinearLayout info_linearLayout = (LinearLayout) line2
					.findViewById(R.id.info_linearLayout);

			TextView time_textView = (TextView) line2
					.findViewById(R.id.time_textView);
			TextView uname_textView = (TextView) line2
					.findViewById(R.id.uname_textView);

			uname_textView.setText(huifu.getUname());

			if (huifu.getType().equalsIgnoreCase(MessageHandle.HUIFU_TEXT)) {
				TextView t = new TextView(ConsoleActivity.this);
				t.setText(huifu.getTxt());
				info_linearLayout.addView(t);
			} else if (huifu.getType().equalsIgnoreCase(MessageHandle.HUIFU_AMR)) {
				final MyButton t = new MyButton(ConsoleActivity.this);
				LinearLayout.LayoutParams lay = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				t.setLayoutParams(lay);
				t.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							File f = new File(extDir+"/amrs", t.getFilename());
							if (!f.exists()) {
								Socket socket = new Socket(Tools.IP,
										Tools.PORT_3);
								InputStream in = socket.getInputStream();
								OutputStream out = socket.getOutputStream();
								out.write(("download,amr," + t
										.getFilename()).getBytes());
								out.flush();
								byte[] b = new byte[1024];
								in.read(b);
								long size = Long.parseLong(new String(b)
										.trim());

								out.write("OK".getBytes());
								out.flush();

								FileOutputStream outf = new FileOutputStream(
										f);
								int len = 0;
								long length = 0;
								while ((len = in.read(b)) != -1) {
									length += len;
									outf.write(b, 0, len);
									if (length >= size) {
										break;
									}
								}
								outf.close();
								in.close();
								out.close();
							}
							MediaPlayer mediaPlayer = new MediaPlayer();
							if (mediaPlayer.isPlaying()) {
								mediaPlayer.reset();// ����Ϊ��ʼ״̬
							}
							mediaPlayer.setDataSource(f.getPath());
							mediaPlayer.prepare();// ����
							mediaPlayer.start();// ��ʼ��ָ�����
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
				t.setText("����¼��");
				t.setFilename(huifu.getTxt());
				info_linearLayout.addView(t);
			} else if (huifu.getType()
					.equalsIgnoreCase(MessageHandle.HUIFU_IMAGE)) {
				final MyButton t = new MyButton(ConsoleActivity.this);
				t.setText("�����ͼ");
				LinearLayout.LayoutParams lay = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				t.setLayoutParams(lay);
				t.setFilename(huifu.getTxt());

				t.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							File f = new File(extDir+"/images", t.getFilename());
							if (!f.exists()) {
								Socket socket = new Socket(Tools.IP,
										Tools.PORT_3);
								InputStream in = socket.getInputStream();
								OutputStream out = socket.getOutputStream();
								out.write(("download,image," + t
										.getFilename()).getBytes());
								out.flush();
								byte[] b = new byte[1024];
								in.read(b);
								long size = Long.parseLong(new String(b)
										.trim());

								out.write("OK".getBytes());
								out.flush();

								FileOutputStream outf = new FileOutputStream(
										f);
								int len = 0;
								long length = 0;
								while ((len = in.read(b)) != -1) {
									length += len;
									outf.write(b, 0, len);
									if (length >= size) {
										break;
									}
								}
								outf.close();
								in.close();
								out.close();

							}

							final Dialog d = new Dialog(
									ConsoleActivity.this);
							ImageView image = new ImageView(
									ConsoleActivity.this);
							image.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									d.hide();
								}
							});
							image.setImageURI(Uri.fromFile(f));

							d.setContentView(image);
							d.show();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

				info_linearLayout.addView(t);
			}
			time_textView.setText("ʱ��:" + huifu.getSendtime());
            line1.addView(line2);
		}
		list_linearLayout.addView(line1);
		}
	}
	
   /**
   * �˳��û���¼(�ٰ�һ�η��ص�����)
   */
	int count = 0;
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.KEYCODE_BACK == keyCode) {
			this.setTitle(R.string.app_name);//���ؼ���ԭ��������
			titleid = 0;
			if (count == 1) {
				try {
					InformClient.close();

				} catch (Exception e) {

				}
				try {
					LoginClient.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
				System.exit(0);
			} else {
				System.out.println("countֵ:_________"+count);
				count++;
			}
			return false;

		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * ������ҳ��ֱ�Ӽ���ȫ����Ϣ
	 */
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		loadData(LoginClient.getData());
	}

}
