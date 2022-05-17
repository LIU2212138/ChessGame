package chess;

import java.awt.*;
import java.io.Serial;
import javax.swing.*;
import pieces.*;

/**
 * 实现对棋盘的样式控制
 */
public class Cell extends JPanel implements Cloneable{
	
	@Serial
	private static final long serialVersionUID = 1L;
	private boolean ispossibledestination;//可能的移动位置
	private JLabel content;
	private Piece piece;
	int x,y;                            
	private boolean isSelected=false;
	private boolean ischeck=false;
	
	
	public Cell(int x,int y,Piece p)
	{		
		this.x=x;
		this.y=y;
		
		setLayout(new BorderLayout());
	
	 if((x+y)%2==0)
	  setBackground(new Color(211,211,0));
	
	 else
	  setBackground(Color.white);
	 
	 if(p!=null)
		 setPiece(p);//设置图片样式
	}
	
	//这是对单个棋子的操作
	public Cell(Cell cell) throws CloneNotSupportedException
	{
		this.x=cell.x;
		this.y=cell.y;
		setLayout(new BorderLayout());
		//设置格子的颜色
		if((x+y)%2==0)
			setBackground(new Color(211,211,0));
		else
			setBackground(Color.white);
		//往格子里面塞棋子
		if(cell.getpiece()!=null)
		{
			setPiece(cell.getpiece().getcopy());
		}
		else
			piece=null;
	}
	
	//设置棋子
	public void setPiece(Piece p)    
	{
		piece=p;
		//getPath 是自己写的一个方法，用于获得棋子所对应图片的路径，而在设置构造器的时候有setPath方法
		ImageIcon img=new javax.swing.ImageIcon(this.getClass().getResource(p.getPath()));
		content=new JLabel(img);
		//添加棋子图片
		this.add(content);
	}
	
	
	/*public void removePiece()      
	{
		if (piece instanceof King)
		{
			piece=null;
			this.remove(content);
		}
		else
		{
			piece=null;
			this.remove(content);
		}
	}*/
	
	//移除图片
	public void removePiece()      
	{
		if (piece != null){
			if (piece instanceof King) {
				piece=null;
				this.remove(content);
			} else {
				piece=null;
				this.remove(content);
			}
		} else
			piece=null;
	}
	
	//得到图片
	public Piece getpiece()    
	{
		return this.piece;
	}
	
	//选择cell
	public void select()       
	{
		this.setBorder(BorderFactory.createLineBorder(Color.red,6));
		this.isSelected=true;
	}
	
	//判断是否选择cell
	public boolean isSelected()
	{
		return this.isSelected;
	}
	
	//清除选择cell
	public void deselect()      
	{
		this.setBorder(null);
		this.isSelected=false;
	}
	
	//orange强调下一步合法落子点
	public void setpossibledestination()     
	{
		this.setBorder(BorderFactory.createLineBorder(Color.ORANGE,4));
		this.ispossibledestination=true;
	}
	
	//清除合法落子点列表
	public void removepossibledestination()    
	{
		this.setBorder(null);
		this.ispossibledestination=false;
	}
	
	//判断cell是否是合法落子点
	public boolean ispossibledestination()  
	{
		return this.ispossibledestination;
	}
	
	//设置king将要checkmate
	public void setcheck()     //Function to highlight the current cell as checked (For King)
	{
		this.setBackground(Color.RED);
		this.ischeck=true;
	}
	
	//清除king的checkmate
	public void removecheck()   
	{
		this.setBorder(null);
		if((x+y)%2==0)
			setBackground(new Color(211,211,0));
		else
			setBackground(Color.white);
		this.ischeck=false;
	}
	
	//判断king是否checkmate
	public boolean ischeck()    
	{
		return ischeck;
	}

	public void setEnterBackground(){
		setBackground(new Color(211,90,80));
	}
	public void removeEnterBackground(){
		if (this.getpiece() instanceof King&&ischeck){
			setcheck();
		}else {
			if((x+y)%2==0)
				setBackground(new Color(211,211,0));
			else
				setBackground(Color.white);
		}
	}
}