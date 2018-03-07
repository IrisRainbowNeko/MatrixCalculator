package com.matculer.tool;
import android.app.*;
import android.os.*;
import java.util.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.view.*;

public class MatrixEditActivity extends Activity
{
	MatrixEditor me;
	GridView gd;
	String matname;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.matrixedit);
		
		matname=getIntent().getStringExtra("matrix");
		Matrix7e matrix=MainActivity.matrixs.get(matname);
		me=(MatrixEditor)findViewById(R.id.matrixeditMatrixEditor1);
		me.setMatrix(matrix);
		//me.setVisibility(View.GONE);
		setTitle("编辑矩阵"+matname);
		
		ArrayList<HashMap<String, Object>> listItem=new ArrayList<HashMap<String, Object>>();
		final String str="123456789←0✔-.";
		for(int i=0;i<str.length();i++)
		{HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name",str.charAt(i)+"");
			listItem.add(map);}
		SimpleAdapter mSA=new SimpleAdapter(this,listItem,R.layout.numitem,
							   new String[] {"name"},new int[]{R.id.numitemTextView1});
		gd=(GridView)findViewById(R.id.matrixeditGridView_num);
		gd.setAdapter(mSA);
		gd.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int index,long arg3) {
					if(index<9)me.append(""+(index+1));
					else if(index==9)me.delete();
					else if(index==10)me.append("0");
					else if(index==11){
						MainActivity.matrixs.put(matname,me.label2mat());
						me.destoryScreen();
						finish();
					}else me.append(""+str.charAt(index));
				}
			});
	}
	
}
