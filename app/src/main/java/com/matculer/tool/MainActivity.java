package com.matculer.tool;

import android.app.*;
import android.os.*;
import android.widget.*;
import java.util.*;
import android.view.*;
import android.content.*;
import android.widget.AdapterView.*;

public class MainActivity extends Activity 
{
	ImageView im_rect,im_run;
	EditText et;
	TextView tx;
	public static HashMap<String,Matrix7e> matrixs=new HashMap<String,Matrix7e>();
    public static MainActivity ma;
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		ma=this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        setContentView(R.layout.main);
		
		et=(EditText)findViewById(R.id.mainEditText1);
		tx=(TextView)findViewById(R.id.mainTextView1);
		im_rect=(ImageView)findViewById(R.id.titleImageView_rect);
		im_run=(ImageView)findViewById(R.id.titleImageView_run);
		im_rect.setOnClickListener(new ImageView.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					ListView lv=new ListView(MainActivity.this);
					lv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
					ArrayList<HashMap<String, Object>> listItem=new ArrayList<HashMap<String, Object>>();
					final String[] keys=matrixs.keySet().toArray(new String[1]);// 得到全部的key
					for(int i=0;i<keys.length;i++) {
						if(keys[i]==null)continue;
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("name",keys[i]+" "+matrixs.get(keys[i]).getHeight()+"×"+matrixs.get(keys[i]).getWdith());
						map.put("icon",R.drawable.rect);
						listItem.add(map);
					}
					
					SimpleAdapter mSA=new SimpleAdapter(MainActivity.this,listItem,R.layout.matitem,
														new String[] {"name","icon"},new int[]{R.id.matitemTextView1,R.id.matitemImageView1});
					
					lv.setAdapter(mSA);
					lv.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int index,long arg3) {
								Intent intent=new Intent(MainActivity.this,MatrixEditActivity.class);
								intent.putExtra("matrix",keys[index]);
								startActivity(intent);
							}
						});
					
					new AlertDialog.Builder(MainActivity.this).setTitle("矩阵列表").
						setIcon(R.drawable.ic_launcher).setView(lv).
						setPositiveButton("添加", new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								showadddia();
							}
						})
						.setNegativeButton("取消", null).show();
				}
			});
		im_run.setOnClickListener(new ImageView.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					String[] cmds=et.getText().toString().split("\n");
					
					for (String i:cmds){
						try{
							tx.append(execute_cmd(i));
						}catch (Exception e)
						{tx.append(e+"\n");}
					}
					
				}
			});
    }
	public String execute_cmd(String cmd) throws Exception{
		Expression exp=new Expression();
		exp.doTrans(cmd);
		Matrix7e mat=exp.calculate_check(exp.display());
		return mat.toRectString()+"\n";
	}
	public void showadddia(){
		final View vi=getLayoutInflater().inflate(R.layout.matdia,null);
		new AlertDialog.Builder(MainActivity.this).setTitle("添加矩阵").
			setIcon(R.drawable.ic_launcher).setView(vi).
			setPositiveButton("确定", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					String name=((EditText)vi.findViewById(R.id.matdiaEditText_name)).getText().toString();
					matrixs.put(name,new Matrix7e(
									Integer.parseInt(((EditText)vi.findViewById(R.id.matdiaEditText_w)).getText().toString())
									,Integer.parseInt(((EditText)vi.findViewById(R.id.matdiaEditText_h)).getText().toString())));
					Intent intent=new Intent(MainActivity.this,MatrixEditActivity.class);
					intent.putExtra("matrix",name);
					startActivity(intent);
				}
			})
			.setNegativeButton("取消", null).show();
		
	}
}
