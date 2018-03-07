package com.matculer.tool;
import com.uxyq7e.test.*;
import android.content.*;
import android.util.*;
import android.graphics.*;
import android.view.*;

public class MatrixEditor extends Screen7e
{
	TableView matrix=new TableView();
	
	int index_x=-1,index_y=-1;
	int w,h;
	public MatrixEditor(Context c){
		super(c);
		init();
	}
	public MatrixEditor(Context c,AttributeSet attr){
		super(c,attr);
		init();
	}
	public void init(){
		matrix.setitem_wh(150,150);
		matrix.setheader_h(0);
		matrix.setTableClickListener(new TableView.TableClickListener(){

				@Override
				public void headerclick(int position)
				{
					// TODO: Implement this method
				}

				@Override
				public void itemclick(int row, int column)
				{
					if(index_x>=0&&index_y>=0)matrix.items.get(index_y).get(index_x).setbgcolor(0);
					index_x=column;index_y=row;
					
					Lable la=matrix.items.get(index_y).get(index_x);
					la.setbgcolor(0xff66ccff);
					try{
						if(Double.parseDouble(la.text.toString())==0)la.settext("");
					}catch(Exception e){}
				}

				@Override
				public void headerlongclick(int position)
				{
					// TODO: Implement this method
				}

				@Override
				public void itemlongclick(int row, int column)
				{
					// TODO: Implement this method
				}

				@Override
				public void noclick()
				{
					
				}
			});
	}

	@Override
	public void Draw(Canvas canvas)
	{
		matrix.Draw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		matrix.touch(event);
		return true;
	}

	@Override
	public void poi()
	{
		matrix.poi();
		super.poi();
	}
	
	
	
	public void setwh(int w,int h){
		this.w=w;this.h=h;
		matrix.addcolumns(new String[w],0);
		for(int i=0;i<h;i++)matrix.addline(new String[w],0);
	}
	public void setMatrix(Matrix7e mat){
		w=mat.getWdith();
		h=mat.getHeight();
		setwh(w,h);
		for(int i=0;i<w;i++){
			//matrix.header.get(i).settext("fuck");
			for(int u=0;u<h;u++){
				matrix.items.get(u).get(i).settext(mat.getValue(i,u)+"");
			}
		}
	}
	public void append(String str){
		if(index_x>=0&&index_y>=0)matrix.items.get(index_y).get(index_x).append(str);
	}
	public void delete(){
		if(index_x>=0&&index_y>=0)matrix.items.get(index_y).get(index_x).deleteText(1);
	}
	public Matrix7e label2mat(){
		Matrix7e mat=new Matrix7e(w,h);
		for(int i=0;i<w;i++)
			for(int u=0;u<h;u++)
				mat.setValue(Double.parseDouble(matrix.items.get(u).get(i).text.toString()),i,u);
		return mat;
	}
}
