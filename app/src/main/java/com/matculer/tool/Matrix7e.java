package com.matculer.tool;
import java.text.*;

public class Matrix7e
{
	double[][] nums;
	DecimalFormat df=new DecimalFormat("#.####");
	public Matrix7e(String str) throws Exception{
		int start=str.indexOf("[")+1,end=str.indexOf("]",start);
		String[] temp=str.substring(start,end).split(";");
		int len=temp[0].split(",").length;
		nums=new double[temp.length][];
		for(int i=0;i<temp.length;i++){
			String[] str_temp=temp[i].split(",");
			if(str_temp.length!=len)throw new Exception("这不是矩阵");
			nums[i]=str2double_arr(str_temp);
		}
	}
	public Matrix7e(int w,int h){
		nums=new double[h][w];
	}
	public Matrix7e(double num){
		nums=new double[1][1];
		nums[0][0]=num;
	}
	public Matrix7e(Matrix7e mat){
		this(mat.getWdith(),mat.getHeight());
		for(int i=0;i<nums.length;i++)
			for(int u=0;u<nums[i].length;u++)
				setValue(mat.getValue(u,i),u,i);
	}

	@Override
	public String toString()
	{
		String str="";
		for(double[] i:nums){
			for(double u:i)str+=(u+",");
			str=cutlast(str);
			str+=";";
		}
		return cutlast(str);
	}
	public String toRectString(){
		String str="";
		for(double[] i:nums){
			for(double u:i)str+=(df.format(u)+"    \t");
			str=cutlast(str);
			str+="\n";
		}
		return str;
	}
	
	public static Matrix7e add(Matrix7e a,Matrix7e b) throws Exception{
		if(a.getWdith()!=b.getWdith()||a.getHeight()!=b.getHeight())throw new Exception("不满足相加条件");
		Matrix7e result=new Matrix7e(a.getWdith(),a.getHeight());
		for(int i=0;i<result.getHeight();i++)
			for(int u=0;u<result.getWdith();u++)
				result.setValue(a.getValue(u,i)+b.getValue(u,i),u,i);
		return result;
	}
	public static Matrix7e minus(Matrix7e a,Matrix7e b) throws Exception{
		if(a.getWdith()!=b.getWdith()||a.getHeight()!=b.getHeight())throw new Exception("不满足相减条件");
		Matrix7e result=new Matrix7e(a.getWdith(),a.getHeight());
		for(int i=0;i<result.getHeight();i++)
			for(int u=0;u<result.getWdith();u++)
				result.setValue(a.getValue(u,i)-b.getValue(u,i),u,i);
		return result;
	}
	public static Matrix7e multip(Matrix7e a,Matrix7e b) throws Exception{
		Matrix7e result=null;
		if(a.getHeight()!=b.getWdith()){
			if(b.isnumber()){
				result=new Matrix7e(a.getWdith(),a.getHeight());
				for(int i=0;i<result.getHeight();i++)
					for(int u=0;u<result.getWdith();u++){
						result.setValue(a.getValue(u,i)*b.getnumber(),u,i);
					}
			}else if(a.isnumber()){
				result=new Matrix7e(b.getWdith(),b.getHeight());
				for(int i=0;i<result.getHeight();i++)
					for(int u=0;u<result.getWdith();u++){
						result.setValue(b.getValue(u,i)*a.getnumber(),u,i);
					}
			}else throw new Exception("不满足相乘条件");
			return result;
		}
		
		result=new Matrix7e(b.getWdith(),a.getHeight());
		for(int i=0;i<result.getHeight();i++)
			for(int u=0;u<result.getWdith();u++){
				double value=0;
				for(int o=0;o<a.getWdith();o++)value+=(a.getValue(o,i)*b.getValue(u,o));
				result.setValue(value,u,i);
			}
		return result;
	}
	public static Matrix7e div(Matrix7e a,Matrix7e b) throws Exception{
		if(!b.isnumber())throw new Exception("不满足相除条件");
		Matrix7e result=new Matrix7e(a.getWdith(),a.getHeight());
		for(int i=0;i<result.getHeight();i++)
			for(int u=0;u<result.getWdith();u++)
				result.setValue(a.getValue(u,i)/b.getnumber(),u,i);
		return result;
	}
	public static Matrix7e pow(Matrix7e a,Matrix7e n) throws Exception{
		if(a.isnumber()&&n.isnumber())return new Matrix7e(Math.pow(a.getnumber(),n.getnumber()));
		
		if(!n.isnumber()||a.getWdith()!=a.getHeight())throw new Exception("不满足乘方条件");
		if(n.getnumber()==0)return creatItemMat(a.getWdith());
		if(n.getnumber()==-1)return div(companion(a),det(a));
		
		Matrix7e result=new Matrix7e(a);
		for(int i=2;i<=n.getnumber();i++){
			result=Matrix7e.multip(result,a);
		}
		return result;
	}
	public static Matrix7e det(Matrix7e mat) throws Exception{
		return new Matrix7e(determinant(mat.getValues()));
	}
	public static Matrix7e Transpose(Matrix7e mat){
		Matrix7e mm=new Matrix7e(mat.getHeight(),mat.getWdith());
		for(int i=0;i<mat.getHeight();i++)
			for(int u=0;u<mat.getWdith();u++)
				mm.setValue(mat.getValue(u,i),i,u);
		return mm;
	}
	public static Matrix7e companion(Matrix7e mat) throws Exception{
		if(!mat.isrect()||(mat.getWdith()<2||mat.getHeight()<2))throw new Exception("无伴随矩阵");
		Matrix7e result=new Matrix7e(mat.getWdith(),mat.getHeight());
		for(int i=0;i<mat.getHeight();i++)
			for(int u=0;u<mat.getWdith();u++)
				result.setValue(det(subtype(mat,u,i)).getnumber(),u,i);
		return Transpose(result);
	}
	public static Matrix7e subtype(Matrix7e mat,int x,int y) throws Exception{
		if(mat.getWdith()<2||mat.getHeight()<2)throw new Exception("无余子式");
		Matrix7e result=new Matrix7e(mat.getWdith()-1,mat.getHeight()-1);
		for(int i=0,ir=0;i<mat.getHeight();i++){
			if(i==y)continue;
			for(int u=0,ur=0;u<mat.getWdith();u++){
				if(u==x)continue;
				result.setValue(mat.getValue(u,i),ur,ir);
				ur++;
			}
			ir++;
		}
		return result;
	}
	
	public static Matrix7e creatItemMat(int size) throws Exception{
		if(size<=0)throw new Exception("行列不能小于1");
		Matrix7e mat=new Matrix7e(size,size);
		for(int i=0;i<size;i++)mat.setValue(1,i,i);
		return mat;
	}
	
	public int getWdith(){
		return nums[0].length;
	}
	public int getHeight(){
		return nums.length;
	}
	public void setValue(double value,int x,int y){
		nums[y][x]=value;
	}
	public double getValue(int x,int y){
		return nums[y][x];
	}
	public double[][] getValues(){
		return nums.clone();
	}
	public boolean isnumber(){
		return nums.length==1&&nums[0].length==1;
	}
	public boolean isrect(){
		return getWdith()==getHeight();
	}
	public double getnumber() throws Exception{
		if(!isnumber())throw new Exception("not a number");
		return nums[0][0];
	}
	
	public static String cutlast(String str){
		return str.substring(0,str.length()-1);
	}
	public static double[] str2double_arr(String[] arr){
		double[] temp=new double[arr.length];
		for(int i=0;i<arr.length;i++)temp[i]=Double.parseDouble(arr[i]);
		return temp;
	}
	
	
	
	/***
	 * 求行列式的算法
	 * @param value 需要算的行列式
	 * @return 计算的结果
	 */
	public static double determinant(double[][] value) throws Exception{
		if(value.length!=value[0].length)throw new Exception("非法行列式");
		if (value.length == 1) {
			//当行列式为1阶的时候就直接返回本身
			return value[0][0];
		}else if (value.length == 2) {
			//如果行列式为二阶的时候直接进行计算
			return value[0][0]*value[1][1]-value[0][1]*value[1][0];
		}
		//当行列式的阶数大于2时
		double result = 1;
		for (int i = 0; i < value.length; i++) {       
			//检查数组对角线位置的数值是否是0，如果是零则对该数组进行调换，查找到一行不为0的进行调换
			if (value[i][i] == 0) {
				value = changeDeterminantNoZero(value,i,i);
				result*=-1;
			}
			for (int j = 0; j <i; j++) {
				//让开始处理的行的首位为0处理为三角形式
				//如果要处理的列为0则和自己调换一下位置，这样就省去了计算
				if (value[i][j] == 0) {
					continue;
				}
				//如果要是要处理的行是0则和上面的一行进行调换
				if (value[j][j]==0) {
					double[] temp = value[i];
					value[i] = value[i-1];
					value[i-1] = temp;
					result*=-1;
					continue;
				}
				double  ratio = -(value[i][j]/value[j][j]);
				value[i] = addValue(value[i],value[j],ratio);
			}
		}
		return mathValue(value,result);
	}

	/**
	 * 计算行列式的结果
	 * @param value
	 * @return
	 */
	public static double mathValue(double[][] value,double result) throws Exception{
		for (int i = 0; i < value.length; i++) {
			//如果对角线上有一个值为0则全部为0，直接返回结果
			if (value[i][i]==0) {
				return 0;
			}
			result *= value[i][i];
		}
		return result;
	}

	/***
	 * 将i行之前的每一行乘以一个系数，使得从i行的第i列之前的数字置换为0
	 * @param currentRow 当前要处理的行
	 * @param frontRow i行之前的遍历的行
	 * @param ratio 要乘以的系数
	 * @return 将i行i列之前数字置换为0后的新的行
	 */
	public static double[] addValue(double[] currentRow,double[] frontRow, double ratio)throws Exception{
		for (int i = 0; i < currentRow.length; i++) {
			currentRow[i] += frontRow[i]*ratio;
		}
		return currentRow;
	}

	/**
	 * 指定列的位置是否为0，查找第一个不为0的位置的行进行位置调换，如果没有则返回原来的值
	 * @param determinant 需要处理的行列式
	 * @param line 要调换的行
	 * @param row 要判断的列
	 */
	public static double[][] changeDeterminantNoZero(double[][] determinant,int line,int row)throws Exception{
		for (int j = line; j < determinant.length; j++) {
			//进行行调换
			if (determinant[j][row] != 0) {
				double[] temp = determinant[line];
				determinant[line] = determinant[j];
				determinant[j] = temp;
				return determinant;
			}
		}
		return determinant;
	}
}
