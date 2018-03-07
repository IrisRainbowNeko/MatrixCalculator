package com.matculer.tool;

import java.io.*;
import java.util.*;

public class Expression
 {
	private Stack<String> stack;
	//输入的表达式
	private String input;
	//输出道arraylist
	private ArrayList<String> arrayList;
	//记录数字的数组
	private String num;

	public Expression(){
		stack=new Stack<String>();
		arrayList=new ArrayList<String>();
		num="";input="";
	}

	public void doTrans(String input)throws Exception{
		if(!isEmpty(input))
		{
			throw new Exception("");
		}
		this.input=input;
		for(int i=0;i<input.length();i++)
		{ 
			char s=input.charAt(i);
			if(s=='+'||s=='-')
			{
				if(!num.equals("")){
					arrayList.add(num);
					num="";
				}
				if(i==0)
					arrayList.add("0");
				else if(input.charAt(i-1)=='('||input.charAt(i-1)==',')
					arrayList.add("0");
				getOperator(s+"",1);
			}
			else if((s=='*')||(s=='/'))
			{
				if(!num.equals(""))
				{
					arrayList.add(num);
					num="";
				}
				getOperator(s+"",2);
			}
			else if((s=='^')||s=='√')
			{
				if(!num.equals(""))
				{
					arrayList.add(num);
					num="";
				}
				if(s=='√')
				{
					if(i==0)
						arrayList.add("2");
					else if(!((input.charAt(i-1)<='9'&&input.charAt(i-1)>='0')||input.charAt(i-1)==')'||input.charAt(i-1)=='x'))
						arrayList.add("2");
				}
				getOperator(s+"",3);
			}
			else if((s==','))
			{
				if(!num.equals(""))
				{
					arrayList.add(num);
					num="";
				}
				getOperator(s+"",0);
			}
			else if(s=='(')
			{
				if(!num.equals(""))
				{
					arrayList.add(num);
					num="";
				}
				stack.push(s+"");
			}
			else if(s==')')
			{
				if(!num.equals(""))
				{
					arrayList.add(num);
					num="";
				}
				getOperator(s+"");
			}
			else if(s=='[')
			{
				if(!num.equals(""))
				{
					arrayList.add(num);
					num="";
				}
				int p=input.indexOf("]",i);
				arrayList.add(input.substring(i,p+1));
				i=p;
			}
			else
			{
				if(!(s>='0'&&s<='9'||s=='.')){
					int c=0;
					if (!num.equals("")) {
						arrayList.add(num);
						num = "";
						getOperator("*", 2);
						c=1;
					}
					int index=subfs(input, "+-*/^√,()", i);
					num=input.substring(i,index);
					i+=(index-i-1);
					if ("x,t,π,e".indexOf(num)!=-1||MainActivity.matrixs.containsKey(num)) {
						arrayList.add(num);
						num="";
					}
				} else {
					num+=s;
				}
				ishs(Constant.FUNCTION_NAME);
			}
			if(i==input.length()-1&&!num.equals(""))
			{
				arrayList.add(num);
			}
		}
		while(!stack.empty())
		{
			arrayList.add(""+stack.pop());
		}
	}
	public void ishs(String[] ba)
	{
		for(int i=0;i<ba.length;i++)
		if(num.equals(ba[i]))
		{
			getOperator(num,4);
			num="";
			break;
		}
	}
	public int subfs(String a, String b, int i) {
        for (int i2=i;i2<a.length();i2++) {
            char c=a.charAt(i2);
            for (int i3=0;i3<b.length();i3++) {
                if (c==b.charAt(i3)) {
                    return i2;
                }
            }
        }
        return a.length();
    }
	
	public void getOperator(String opthis,int prec)
	{
		while(!stack.empty())
		{
			String top=stack.pop();
			if(top.equals("("))
			{
				stack.push(top);
				break;
			}
			else
			{
				int precX;
				if(top.equals(","))
				{
					precX=0;
				}
				else if(top.equals("+")||top.equals("-"))
				{
					precX=1;
				}
				else if(top.equals("*")||top.equals("/"))
				{
					precX=2;
				}
				else if(top.equals("^")||top.equals("√"))
					precX=3;
				else precX=4;
				if(precX<prec)
				{
					stack.push(top);
					break;
				}
				else
				{
					arrayList.add(top);
				}
			}
		}
		stack.push(opthis);
	}
	public void getOperator(String opthis)
	{
		while(!stack.empty())
		{
			String top=stack.pop();
			if(top.equals("("))
			{
				break;
			}
			else
			{
				arrayList.add(top);
			}
		}
	}
	
	private boolean isEmpty(String input)
	{
		boolean b=true;
		if(input==null||input.length()==0)
		{
			b=false;
		}
		return b;
	}
	public String display(){
		String displayStr="";
		for(int i=0;arrayList!=null&&i<arrayList.size();i++)
		{
			displayStr+=(arrayList.get(i)+"·");
		}
		return displayStr.substring(0,displayStr.length()-1);
	}
	public Matrix7e calculate_check(String ii)throws Exception{
		String[] in=ii.split("·");
		Stack<Matrix7e> st=new Stack<Matrix7e>();
		uu:for(int i=0;i<in.length;i++)
		{
				if(MainActivity.matrixs.containsKey(in[i])){
					st.push(new Matrix7e(MainActivity.matrixs.get(in[i])));
				}
				else if(in[i].length()==1)
				{
				if(in[i].equals(","))
					st.push(st.pop());
				else if(in[i].equals("+"))
					st.push(Matrix7e.add(st.pop(),st.pop()));
				else if(in[i].equals("-"))
				{Matrix7e x=st.pop(),y=st.pop();
					st.push(Matrix7e.minus(y,x));}
				else if(in[i].equals("*")){
					Matrix7e x=st.pop(),y=st.pop();
					st.push(Matrix7e.multip(y,x));
				}else if(in[i].equals("/"))
				{Matrix7e x=st.pop(),y=st.pop();
					st.push(Matrix7e.div(y,x));}
				else if(in[i].equals("^"))
				{Matrix7e x=st.pop(),y=st.pop();
					st.push(Matrix7e.pow(y,x));}
				else if(in[i].equals("π"))
					st.push(new Matrix7e(Math.PI));
				else if(in[i].equals("e"))
					st.push(new Matrix7e(Math.E));
				else if(in[i].equals("√")){
					st.push(new Matrix7e(Math.pow(st.pop().getnumber(),1/st.pop().getnumber())));
				}else
					st.push(toMatrix(in[i]));
				}
				else if(in[i].length()==2)
				{
					if(in[i].equals("lg"))
						st.push(new Matrix7e(Math.log10(st.pop().getnumber())));
					else if(in[i].equals("ln"))
						st.push(new Matrix7e(Math.log(st.pop().getnumber())));
					else
						st.push(toMatrix(in[i]));
				}
				else if(in[i].length()==3)
				{if(in[i].equals("sin"))
					st.push(new Matrix7e(Math.sin(st.pop().getnumber())));
				else if(in[i].equals("cos"))
					st.push(new Matrix7e(Math.cos(st.pop().getnumber())));
				else if(in[i].equals("tan"))
					st.push(new Matrix7e(Math.tan(st.pop().getnumber())));
				else if(in[i].equals("log"))
					{double x=st.pop().getnumber(),y=st.pop().getnumber();
					st.push(new Matrix7e(Math.log10(x)/Math.log10(y)));}
				else if(in[i].equals("abs"))
					st.push(new Matrix7e(Math.abs(st.pop().getnumber())));
				else if(in[i].equals("max"))
					st.push(new Matrix7e(Math.max(st.pop().getnumber(),st.pop().getnumber())));
				else if(in[i].equals("min"))
					st.push(new Matrix7e(Math.min(st.pop().getnumber(),st.pop().getnumber())));
				else if(in[i].equals("det"))
					st.push(Matrix7e.det(st.pop()));
				else if(in[i].equals("sub")){
					int y=(int)st.pop().getnumber(),x=(int)st.pop().getnumber();
					Matrix7e mat=st.pop();
					st.push(Matrix7e.subtype(mat,x,y));
				}else
					st.push(toMatrix(in[i]));}
				else if(in[i].length()==4)
				{if(in[i].equals("sinh"))
					st.push(new Matrix7e(Math.sinh(st.pop().getnumber())));
				else if(in[i].equals("cosh"))
					st.push(new Matrix7e(Math.cosh(st.pop().getnumber())));
				else if(in[i].equals("tanh"))
					st.push(new Matrix7e(Math.tanh(st.pop().getnumber())));
				else if(in[i].equals("comp"))
					st.push(Matrix7e.companion(st.pop()));
				else
					st.push(toMatrix(in[i]));
				}else if(in[i].length()==5){
					if(in[i].equals("trans"))
						st.push(Matrix7e.Transpose(st.pop()));
					else
						st.push(toMatrix(in[i]));
				}else if(in[i].length()==6)
				{if(in[i].equals("arcsin"))
					st.push(new Matrix7e(Math.asin(st.pop().getnumber())));
				else if(in[i].equals("arccos"))
					st.push(new Matrix7e(Math.acos(st.pop().getnumber())));
				else if(in[i].equals("arctan"))
					st.push(new Matrix7e(Math.atan(st.pop().getnumber())));
				else
					st.push(toMatrix(in[i]));}
				else
					st.push(toMatrix(in[i]));}
		if(st.size()!=1){throw new Exception();}
		return st.pop();
	}
	public Matrix7e toMatrix(String str) throws Exception{
		if(str.startsWith("[")){
			return new Matrix7e(str);
		}else return new Matrix7e(Double.parseDouble(str));
	}
}
