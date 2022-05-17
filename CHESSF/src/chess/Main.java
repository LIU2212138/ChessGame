package chess;

import pieces.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;



public class Main extends JFrame implements MouseListener
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	//定义变量
	private JPanel board;
	private JPanel wdetails;
	private JPanel bdetails;
	private JPanel wcombopanel;
	private JPanel bcombopanel;
	private JPanel controlPanel,WhitePlayer,BlackPlayer,temp,displayTime,showPlayer,time;
	private JSplitPane split;
	private JLabel label,mov, beginname;
	private static JLabel CHNC;
	private static final int Height=800;
	private static final int Width=1100;
	private static Rook wr01,wr02,br01,br02;
	private static Knight wk01,wk02,bk01,bk02;
	private static Bishop wb01,wb02,bb01,bb02;
	private static Pawn wp[],bp[];
	private static Queen wq,bq;
	private static King wk,bk;
	private Cell c,previous;
	private int chance=0;
	private Cell boardState[][];
	private ArrayList<Cell> destinationlist = new ArrayList<Cell>();
	private Player White=null,Black=null;
	private Time timer;
	public static Main Mainboard;//主体框架
	private boolean selected=false,end=false;
	private Container content;
	private ArrayList<Player> wplayer,bplayer;
	private ArrayList<String> Wnames;
	private ArrayList<String> Bnames;
	private JComboBox<String> wcombo,bcombo;
	private String wname,bname,winner;
	static String move;
	private Player tempPlayer;
	private JScrollPane wscroll,bscroll;
	private String[] WNames={},BNames={};
	private JSlider timeSlider;
	private BufferedImage image;
	private Button start,wselect,bselect,WNewPlayer,BNewPlayer, restart,loadGame,saveGame,changeBackground;
	public static int timeRemaining=60;//每回合时间
	private static JLayeredPane jLayeredPane;


	public static void main(String[] args){

		//黑色方棋子布局
		br01=new Rook("R","rook-black.png",1);
		br02=new Rook("R","rook-black.png",1);
		bk01=new Knight("N","knight-black.png",1);
		bk02=new Knight("N","knight-black.png",1);
		bb01=new Bishop("B","bishop-black.png",1);
		bb02=new Bishop("B","bishop-black.png",1);
		bq=new Queen("Q","queen-black.png",1);
		bk=new King("K","king-black.png",1,0,3);
		bp=new Pawn[8];

		//白色方棋子布局
		wr01=new Rook("r","rook-white.png",0);
		wr02=new Rook("r","rook-white.png",0);
		wk01=new Knight("n","knight-white.png",0);
		wk02=new Knight("n","knight-white.png",0);
		wb01=new Bishop("b","bishop-white.png",0);
		wb02=new Bishop("b","bishop-white.png",0);
		wq=new Queen("q","queen-white.png",0);
		wk=new King("k","king-white.png",0,7,3);
		wp=new Pawn[8];

		//兵布局
		for(int i=0;i<8;i++)
		{
			wp[i]=new Pawn("p","pawn-white.png",0);
			bp[i]=new Pawn("P","pawn-black.png",1);
		}


		Mainboard = new Main();
		Mainboard.setVisible(true);//使界面可见
		Mainboard.setResizable(true);//使界面大小可以更改（可以全屏）
		new Thread(new clickMusic("C:\\Users\\86187\\Desktop\\CHESSF\\src\\chess\\backgroundMusic.wav")).start();
	}
	
	
	private  Main()//主界面
    {
		timeRemaining=60;
		timeSlider = new JSlider();
		move="White";
		//黑白方昵称
		wname=null;
		bname=null;

		winner=null;
		//棋盘
		board=new JPanel(new GridLayout(8,8));
		//黑白方信息
		wdetails=new JPanel(new GridLayout(3,3));
		bdetails=new JPanel(new GridLayout(3,3));

		bcombopanel=new JPanel();
		wcombopanel=new JPanel();
		//黑白方用户昵称组
		Wnames=new ArrayList<String>();
		Bnames=new ArrayList<String>();
		board.setMinimumSize(new Dimension(800,700));//设置棋盘的最小大小（注意不是窗口大小），棋盘大小可以增大，同时可以从窗口中间的那条线拖动
		
		timeSlider.setMinimum(1);
		timeSlider.setMaximum(15);
		timeSlider.setValue(1);
		timeSlider.setMajorTickSpacing(2);
		timeSlider.setPaintLabels(true);
		timeSlider.setPaintTicks(true);
		timeSlider.addChangeListener(new TimeChange());
		
		
		//获取用户信息
		wplayer= Player.fetch_players();
		//迭代器输出
		Iterator<Player> witr=wplayer.iterator();
		while(witr.hasNext())
			Wnames.add(witr.next().name());
				
		bplayer= Player.fetch_players();
		Iterator<Player> bitr=bplayer.iterator();
		while(bitr.hasNext())
			Bnames.add(bitr.next().name());
	    WNames=Wnames.toArray(WNames);	
		BNames=Bnames.toArray(BNames);
		
		//主体样式
		Cell cell;
		board.setBorder(BorderFactory.createLoweredBevelBorder());

		content=getContentPane();
		//Window里设置尺寸和标题
		setSize(Width,Height);
		setTitle("Chess");

		//设置框架的背景
		content.setBackground(Color.white);
		controlPanel=new JPanel();
		content.setLayout(new BorderLayout());
		controlPanel.setLayout(new GridLayout(3,3));
		
		//用户信息样式
		WhitePlayer=new JPanel();
		WhitePlayer.setBorder(BorderFactory.createTitledBorder(null, "White Player", TitledBorder.TOP,TitledBorder.CENTER, new Font("times new roman",Font.BOLD,18), Color.black));
		WhitePlayer.setLayout(new BorderLayout());
		
		BlackPlayer=new JPanel();
		BlackPlayer.setBorder(BorderFactory.createTitledBorder(null, "Black Player", TitledBorder.TOP,TitledBorder.CENTER, new Font("times new roman",Font.BOLD,18), Color.black));
	    BlackPlayer.setLayout(new BorderLayout());
		
	    JPanel whitestats=new JPanel(new GridLayout(3,3));
		JPanel blackstats=new JPanel(new GridLayout(3,3));

		//Java Swing JComboBox：下拉列表组件
		wcombo=new JComboBox<String>(WNames);
		bcombo=new JComboBox<String>(BNames);
		//滚轮
		wscroll=new JScrollPane(wcombo);
		bscroll=new JScrollPane(bcombo);

		wcombopanel.setLayout(new FlowLayout());
		bcombopanel.setLayout(new FlowLayout());

		wselect=new Button("Select");
		bselect=new Button("Select");
		wselect.addActionListener(new SelectHandler(0));
		bselect.addActionListener(new SelectHandler(1));
		WNewPlayer=new Button("New Player");
		BNewPlayer=new Button("New Player");
		WNewPlayer.addActionListener(new Handler(0));
		BNewPlayer.addActionListener(new Handler(1));
		wcombopanel.add(wscroll);
		wcombopanel.add(wselect);
		wcombopanel.add(WNewPlayer);
		bcombopanel.add(bscroll);
		bcombopanel.add(bselect);
		bcombopanel.add(BNewPlayer);
		WhitePlayer.add(wcombopanel,BorderLayout.NORTH);
		BlackPlayer.add(bcombopanel,BorderLayout.NORTH);
		whitestats.add(new JLabel("Name   :"));
		whitestats.add(new JLabel("Played :"));
		whitestats.add(new JLabel("Won    :"));
		blackstats.add(new JLabel("Name   :"));
		blackstats.add(new JLabel("Played :"));
		blackstats.add(new JLabel("Won    :"));
		WhitePlayer.add(whitestats,BorderLayout.WEST);
		BlackPlayer.add(blackstats,BorderLayout.WEST);
		controlPanel.add(WhitePlayer);
		controlPanel.add(BlackPlayer);


		//棋盘格样式
		Piece P;
		boardState=new Cell[8][8];
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
			{
				P=null;
				if(i==0&&j==0)
					P=br01;
				else if(i==0&&j==7)
					P=br02;
				else if(i==7&&j==0)
					P=wr01;
				else if(i==7&&j==7)
					P=wr02;
				else if(i==0&&j==1)
					P=bk01;
				else if (i==0&&j==6)
					P=bk02;
				else if(i==7&&j==1)
					P=wk01;
				else if (i==7&&j==6)
					P=wk02;
				else if(i==0&&j==2)
					P=bb01;
				else if (i==0&&j==5)
					P=bb02;
				else if(i==7&&j==2)
					P=wb01;
				else if(i==7&&j==5)
					P=wb02;
				else if(i==0&&j==3)
					P=bk;
				else if(i==0&&j==4)
					P=bq;
				else if(i==7&&j==3)
					P=wk;
				else if(i==7&&j==4)
					P=wq;
				else if(i==1)
					P=bp[j];
				else if(i==6)
					P=wp[j];
				cell=new Cell(i,j,P);
				cell.addMouseListener(this);
				board.add(cell);
				boardState[i][j]=cell;
			}

		showPlayer=new JPanel(new FlowLayout());

		displayTime=new JPanel(new FlowLayout());





		start=new Button("Start");

		start.setFont(new Font("SERIF", Font.BOLD, 50));
		start.setForeground(Color.black);
	    start.addActionListener(new START());
		start.setPreferredSize(new Dimension(240,80));
		label = new JLabel("Time Starts Now", JLabel.CENTER);
		label.setFont(new Font("SERIF", Font.BOLD, 30));

	    time=new JPanel(new GridLayout(3,3)); 
	    time.add(showPlayer);
	    displayTime.add(start);
	    time.add(displayTime);
	    controlPanel.add(time);
		board.setMinimumSize(new Dimension(800,700));



		//未启动时样式

		temp=new JPanel(){
			@Serial
			private static final long serialVersionUID = 1L;
			public void paintComponent(Graphics picture) {
				try {
					image = ImageIO.read(this.getClass().getResource("主界面背景 (2).jpg"));
				} catch (IOException ex) {
					System.out.println("not exists");
				}
				BufferedImage subImage;
				subImage=image.getSubimage(0, 0, temp.getWidth(), temp.getHeight());
				picture.drawImage(subImage, 0, 0, null);
			}
		};
		beginname = new JLabel("Chess Game", JLabel.CENTER);

		temp.add(beginname, BorderLayout.CENTER);
		beginname.setFont(new Font("SERIF", Font.BOLD, 100));
		temp.setMinimumSize(new Dimension(800,700));

		controlPanel.setMinimumSize(new Dimension(285,700));
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,temp, controlPanel);

	    content.add(split);
		setDefaultCloseOperation(EXIT_ON_CLOSE);



		
    }
	
	//更换行棋方
	public void changechance()
	{
		if (boardState[getKing(chance).getx()][getKing(chance).gety()].ischeck())
		{
			chance^=1;
			gameend();
		}
		if(destinationlist.isEmpty()==false)
			cleandestinations(destinationlist);
		if(previous!=null)
			previous.deselect();
		previous=null;
		chance^=1;
		if(!end && timer!=null)
		{
			timer.reset();
			timer.start();
			showPlayer.remove(CHNC);
			if(Main.move=="White")
				Main.move="Black";
			else
				Main.move="White";
			CHNC.setText(Main.move);
			showPlayer.add(CHNC);
		}
	}
	
	//得到black-king或者white-king
	private King getKing(int color)
	{
		if (color==0)
			return wk;
		else
			return bk;
	}
	
	//清除可能行棋列表
    private void cleandestinations(ArrayList<Cell> destlist)    
    {
    	ListIterator<Cell> it = destlist.listIterator();
    	while(it.hasNext())
    		it.next().removepossibledestination();
    }
    
    //建立可能行棋列表
    private void highlightdestinations(ArrayList<Cell> destlist)
    {
    	ListIterator<Cell> it = destlist.listIterator();
    	while(it.hasNext())
    		it.next().setpossibledestination();
    }
    
    
  //检测是否即将checkmate
    private boolean willkingbeindanger(Cell fromcell,Cell tocell)
    {
    	Cell newboardstate[][] = new Cell[8][8];
    	for(int i=0;i<8;i++)
    		for(int j=0;j<8;j++)
    		{	try { newboardstate[i][j] = new Cell(boardState[i][j]);} catch (CloneNotSupportedException e){e.printStackTrace(); System.out.println("There is a problem with cloning !!"); }}
    	
    	if(newboardstate[tocell.x][tocell.y].getpiece()!=null)
			newboardstate[tocell.x][tocell.y].removePiece();
    	
		newboardstate[tocell.x][tocell.y].setPiece(newboardstate[fromcell.x][fromcell.y].getpiece());
		if(newboardstate[tocell.x][tocell.y].getpiece() instanceof King)
		{
			((King)(newboardstate[tocell.x][tocell.y].getpiece())).setx(tocell.x);
			((King)(newboardstate[tocell.x][tocell.y].getpiece())).sety(tocell.y);
		}
		newboardstate[fromcell.x][fromcell.y].removePiece();
		if (((King)(newboardstate[getKing(chance).getx()][getKing(chance).gety()].getpiece())).isindanger(newboardstate)==true)
			return true;
		else
			return false;
    }
    
    //禁止导致checkmate的行棋
    private ArrayList<Cell> filterdestination (ArrayList<Cell> destlist, Cell fromcell)
    {
    	ArrayList<Cell> newlist = new ArrayList<Cell>();
    	Cell newboardstate[][] = new Cell[8][8];
    	ListIterator<Cell> it = destlist.listIterator();
    	int x,y;
    	while (it.hasNext())
    	{
    		for(int i=0;i<8;i++)
        		for(int j=0;j<8;j++)
        		{	try { newboardstate[i][j] = new Cell(boardState[i][j]);} catch (CloneNotSupportedException e){e.printStackTrace();}}
    		
    		Cell tempc = it.next();
    		if(newboardstate[tempc.x][tempc.y].getpiece()!=null)
    			newboardstate[tempc.x][tempc.y].removePiece();
    		newboardstate[tempc.x][tempc.y].setPiece(newboardstate[fromcell.x][fromcell.y].getpiece());
    		x=getKing(chance).getx();
    		y=getKing(chance).gety();
    		if(newboardstate[fromcell.x][fromcell.y].getpiece() instanceof King)
    		{
    			((King)(newboardstate[tempc.x][tempc.y].getpiece())).setx(tempc.x);
    			((King)(newboardstate[tempc.x][tempc.y].getpiece())).sety(tempc.y);
    			x=tempc.x;
    			y=tempc.y;
    		}
    		newboardstate[fromcell.x][fromcell.y].removePiece();
    		if ((((King)(newboardstate[x][y].getpiece())).isindanger(newboardstate)==false))
    			newlist.add(tempc);
    	}
    	return newlist;
    }
    
    
    private ArrayList<Cell> incheckfilter (ArrayList<Cell> destlist, Cell fromcell, int color)
    {
    	ArrayList<Cell> newlist = new ArrayList<Cell>();
    	Cell newboardstate[][] = new Cell[8][8];
    	ListIterator<Cell> it = destlist.listIterator();
    	int x,y;
    	while (it.hasNext())
    	{
    		for(int i=0;i<8;i++)
        		for(int j=0;j<8;j++)
        		{	try { newboardstate[i][j] = new Cell(boardState[i][j]);} catch (CloneNotSupportedException e){e.printStackTrace();}}
    		Cell tempc = it.next();
    		if(newboardstate[tempc.x][tempc.y].getpiece()!=null)
    			newboardstate[tempc.x][tempc.y].removePiece();
    		newboardstate[tempc.x][tempc.y].setPiece(newboardstate[fromcell.x][fromcell.y].getpiece());
    		x=getKing(color).getx();
    		y=getKing(color).gety();
    		if(newboardstate[tempc.x][tempc.y].getpiece() instanceof King)
    		{
    			((King)(newboardstate[tempc.x][tempc.y].getpiece())).setx(tempc.x);
    			((King)(newboardstate[tempc.x][tempc.y].getpiece())).sety(tempc.y);
    			x=tempc.x;
    			y=tempc.y;
    		}
    		newboardstate[fromcell.x][fromcell.y].removePiece();
    		if ((((King)(newboardstate[x][y].getpiece())).isindanger(newboardstate)==false))
    			newlist.add(tempc);
    	}
    	return newlist;
    }
    
    //检验是否checkmate
    public boolean checkmate(int color)
    {
    	ArrayList<Cell> dlist = new ArrayList<Cell>();
    	for(int i=0;i<8;i++) {
    		for(int j=0;j<8;j++) {
    			if (boardState[i][j].getpiece()!=null && boardState[i][j].getpiece().getcolor()==color) {
    				dlist.clear();
    				dlist=boardState[i][j].getpiece().move(boardState, i, j);
    				dlist=incheckfilter(dlist,boardState[i][j],color);
    				if(dlist.size()!=0)
    					return false;
    			}
    		}
    	}
    	return true;
    }
	
    
    @SuppressWarnings("deprecation")
	private void gameend() {
    	cleandestinations(destinationlist);
    	displayTime.disable();
    	timer.countdownTimer.stop();
    	if(previous!=null)
    		previous.removePiece();
    	if(chance==0)
		{	White.updateGamesWon();
			White.Update_Player();
			winner=White.name();
		}
		else
		{
			Black.updateGamesWon();
			Black.Update_Player();
			winner=Black.name();
		}
		JOptionPane.showMessageDialog(board,"Checkmate!!!\n"+winner+" Wins");
		WhitePlayer.remove(wdetails);
		BlackPlayer.remove(bdetails);
		displayTime.remove(label);
		
		displayTime.add(start);
		showPlayer.remove(mov);
		showPlayer.remove(CHNC);
		showPlayer.revalidate();
		showPlayer.add(timeSlider);
		
		split.remove(board);
		split.add(temp);
		WNewPlayer.enable();
		BNewPlayer.enable();
		wselect.enable();
		bselect.enable();
		end=true;
		Mainboard.disable();
		Mainboard.dispose();
		Mainboard = new Main();
		Mainboard.setVisible(true);
		Mainboard.setResizable(true);
    }
    
    //继承类，点击才会发生
	public void mouseClicked(MouseEvent arg0){
//		Boolean noWhereToGo=true;
//		for (int i=0;i<8;i++){
//			for (int j=0;j<8;j++){
//				if(boardState[i][j].getpiece()!=null){
//					if (boardState[i][j].getpiece().getcolor()!=chance){
//						destinationlist.clear();
//						destinationlist=filterdestination(destinationlist,boardState[i][j]);
//						if (destinationlist.size()!=0){
//							noWhereToGo=false;
//						}
//					}
//				}
//			}
//		}
		// TODO Auto-generated method stub
		c=(Cell)arg0.getSource();
		if (previous==null) {
			if(c.getpiece()!=null) {
				if(c.getpiece().getcolor()!=chance)
					return;
				c.select();
				previous=c;
				destinationlist.clear();
				destinationlist=c.getpiece().move(boardState, c.x, c.y);
				if(c.getpiece() instanceof King)
					destinationlist=filterdestination(destinationlist,c);
				else {
					if(boardState[getKing(chance).getx()][getKing(chance).gety()].ischeck())
						destinationlist = new ArrayList<Cell>(filterdestination(destinationlist,c));
					else if(destinationlist.isEmpty()==false && willkingbeindanger(c,destinationlist.get(0)))
						destinationlist.clear();
				}
				highlightdestinations(destinationlist);
			}
		} else {
			if(c.x==previous.x && c.y==previous.y) {
				c.deselect();
				cleandestinations(destinationlist);
				destinationlist.clear();
				previous=null;

			} else if(c.getpiece()==null||previous.getpiece().getcolor()!=c.getpiece().getcolor()) {
				if(c.ispossibledestination()) {
					if(c.getpiece()!=null)
						c.removePiece();
					c.setPiece(previous.getpiece());
					new Thread(new clickMusic("C:\\Users\\86187\\Desktop\\CHESSF\\src\\chess\\1秒落子音效.wav")).start();
					if (previous.ischeck())
						previous.removecheck();
					previous.removePiece();
					if(getKing(chance^1).isindanger(boardState)) {
						boardState[getKing(chance^1).getx()][getKing(chance^1).gety()].setcheck();
						if (checkmate(getKing(chance^1).getcolor())) {
							previous.deselect();
							if(previous.getpiece()!=null)
								previous.removePiece();
								gameend();
						}
					}
					if(getKing(chance).isindanger(boardState)==false)
						boardState[getKing(chance).getx()][getKing(chance).gety()].removecheck();
					if(c.getpiece() instanceof King) {
						((King)c.getpiece()).setx(c.x);
						((King)c.getpiece()).sety(c.y);
					}
					changechance();
					if(!end) {
						timer.reset();
						timer.start();
					}
				}if(previous!=null) {
					previous.deselect();
					previous=null;
				}
				cleandestinations(destinationlist);
				destinationlist.clear();
			} else if(previous.getpiece().getcolor()==c.getpiece().getcolor()) {
				previous.deselect();
				cleandestinations(destinationlist);
				destinationlist.clear();
				c.select();
				previous=c;
				destinationlist=c.getpiece().move(boardState, c.x, c.y);
				if(c.getpiece() instanceof King){
					destinationlist=filterdestination(destinationlist,c);
				} else {
					if(boardState[getKing(chance).getx()][getKing(chance).gety()].ischeck())
						destinationlist = new ArrayList<Cell>(filterdestination(destinationlist,c));
					else if(destinationlist.isEmpty()==false && willkingbeindanger(c,destinationlist.get(0)))
						destinationlist.clear();
				}
				highlightdestinations(destinationlist);
			}
		}
		if(c.getpiece()!=null && c.getpiece() instanceof King) {
			((King)c.getpiece()).setx(c.x);
			((King)c.getpiece()).sety(c.y);
		}
	}
    
    
	//抽象类，点击才会发生
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		c=(Cell)arg0.getSource();
		c.setEnterBackground();

	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		c=(Cell)arg0.getSource();
		c.removeEnterBackground();

	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub		
	}
	
	
	class START implements ActionListener {
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		if(White==null||Black==null) {
			JOptionPane.showMessageDialog(controlPanel, "Fill in the details");
			return;}
		White.updateGamesPlayed();
		White.Update_Player();
		Black.updateGamesPlayed();
		Black.Update_Player();
		//清除按钮
		WNewPlayer.disable();
		BNewPlayer.disable();
		wselect.disable();
		bselect.disable();
		displayTime.remove(start);
		displayTime.add(label);

		split.remove(temp);
		split.add(board);
		showPlayer.remove(timeSlider);
		mov=new JLabel("Move:");
		mov.setFont(new Font("times new roman",Font.PLAIN,20));
		mov.setForeground(Color.red);
		showPlayer.add(mov);
		//当前行棋方
		CHNC=new JLabel(move);
		CHNC.setFont(new Font("times new roman",Font.BOLD,20));
		CHNC.setForeground(Color.ORANGE);
		showPlayer.add(CHNC);

		timer=new Time(label);
		timer.start();
		
		
		restart=new Button("Restart");
		restart.setFont(new Font("SERIF", Font.BOLD, 15));
		//start.setBorder(BorderFactory.createRaisedBevelBorder());
		restart.setForeground(Color.black);
	    restart.addActionListener(new Restart());
	    restart.setPreferredSize(new Dimension(100,60));
	    displayTime.add(restart);

		loadGame=new Button("LoadGame");
		loadGame.setFont(new Font("SERIF", Font.BOLD, 15));
		loadGame.setForeground(Color.black);
		loadGame.setPreferredSize(new Dimension(100,60));
		loadGame.addActionListener(new LoadGame());
		displayTime.add(loadGame);

		saveGame=new Button("SaveGame");
		saveGame.setFont(new Font("SERIF", Font.BOLD, 15));
		saveGame.setForeground(Color.black);
		saveGame.setPreferredSize(new Dimension(100,60));
		saveGame.addActionListener(new SaveGame());
		displayTime.add(saveGame);


	}
	}

	class LoadGame implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser=new JFileChooser();
			//chooser.setFileSelectionMode(JFileChooser.);
			int result=chooser.showOpenDialog(null);
			File file=chooser.getSelectedFile();
			String filePath=file.getAbsolutePath();
			URLConnection connection = null;
			try {
				connection = file.toURL().openConnection();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			String type = connection.getContentType();

			FileInputStream fileInputStream=null;
			if (!type.equals("text/plain")){
				JOptionPane.showMessageDialog(controlPanel, "Invalid file type!");
			}

			try {
				fileInputStream=new FileInputStream(file);
				List<String> strings= Files.readAllLines(Paths.get(filePath));
				loadChessGame(strings);
			} catch (FileNotFoundException ex) {
					ex.printStackTrace();
				} catch (IOException ex) {
				ex.printStackTrace();
			} catch (IndexOutOfBoundsException ex){
				ex.printStackTrace();
			}finally {
				if (fileInputStream!=null){
					try {
						fileInputStream.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}

		}
		public void loadChessGame(List<String> chessboard){

			Boolean valid=true;
			here:
			for(int m=0;m<8;m++){
				char[] row=chessboard.get(m).toCharArray();
				for(int n=0;n<8;n++){
					char c=row[n];
					if (c!='_'&&c!='R'&&c!='B'&&c!='K'&&c!='Q'&&c!='N'&&c!='P'&&c!='r'&&c!='b'&&c!='k'&&c!='q'&&c!='n'&&c!='p') {
						JOptionPane.showMessageDialog(controlPanel, "错误棋子类型！");
						valid = false;
						break here;
					}
				}
			}
			if (valid==true){
				//board=new JPanel(new GridLayout(8,8));
				//board.setMinimumSize(new Dimension(800,700));
				//board.setBorder(BorderFactory.createLoweredBevelBorder());
				//pieces.Piece P;
				//Cell cell;
				if (chessboard.size()!=9){
					JOptionPane.showMessageDialog(controlPanel, "无效棋局！");
				}
				//boardState=new Cell[8][8];
				//for(int i=0;i<8;i++){
					//char[] row=chessboard.get(i).toCharArray();
					//for(int j=0;j<8;j++){
						//char c=row[j];
						//P=null;
						//if (c=='_'){
							//P=null;
						//}else if (c=='R'){
							//P=new Rook("R","rook-black.png",1);
						//}else if (c=='r') {
							//P = new Rook("r", "rook-white.png", 0);
						//}else if (c=='N'){
							//P=new Knight("K","knight-black.png",1);
						//}else if (c=='n'){
							//P=new Knight("k","knight-black.png",0);
						//}else if (c=='B'){
							//P=new Bishop("B","bishop-black.png",1);
						//}else if (c=='b'){
							//P=new Bishop("b","bishop-white.png",0);
						//}else if (c=='Q'){
							//P=new Queen("Q","queen-black.png",1);
						//}else if (c=='q'){
							//P=new Queen("q","queen-white.png",0);
						//}else if (c=='K'){
							///P=new King("K","king-black.png",1,i,j);
						//}else if (c=='k'){
							//P=new King("k","king-white.png",0,i,j);
						//}else if (c=='P'){
							//P=new Pawn("P","pawn-black.png",1);
						//}else if (c=='p'){
							//P=new Pawn("p","pawn-white.png",0);
						//}
						//cell=new Cell(i,j,P);


						//board.add(cell);
						//boardState[i][j]=cell;
					//}
				//}
				//if (chessboard.get(8).equals("w")){
					//chance=0;
				//}else {
					//chance=1;
				//}
			//}
			for(int i=0;i<8;i++){
				char[] row=chessboard.get(i).toCharArray();
				for(int j=0;j<8;j++)
				{
					//System.out.print(boardState[i][j].getpiece());
					boardState[i][j].removePiece();
					System.out.print(boardState[i][j].getpiece());
					char c=row[j];
					//p = null;
					//boardState[i][j].removePiece();
					if(c=='_'){
						boardState[i][j].setcheck();
						boardState[i][j].removePiece();
						boardState[i][j].removecheck();
					} else if(c=='R')
						//p = br01;
						boardState[i][j].setPiece(new Rook("R","rook-black.png",1));
					else if(c=='r')
						//p = br01;
						boardState[i][j].setPiece(new Rook("r","rook-white.png",0));
					else if(c=='N')
						//p = br01;
						boardState[i][j].setPiece(new Knight("N","knight-black.png",1));
					else if(c=='n')
						//p =bk01;
						boardState[i][j].setPiece(new Knight("n","knight-white.png",0));
					else if (c=='B')
						//p =bk02;
						boardState[i][j].setPiece(new Bishop("B","bishop-black.png",1));
					else if(c=='b')
						//p =wk01;
						boardState[i][j].setPiece(new Bishop("b","bishop-white.png",0));
					else if (c=='Q')
						//p =wk02;
						boardState[i][j].setPiece(new Queen("Q","queen-black.png",1));
					else if(c=='q')
						//p =bb01;
						boardState[i][j].setPiece(new Queen("q","queen-white.png",0));
					else if (c=='P')
						//p =bb02;
						boardState[i][j].setPiece(new Pawn("P","pawn-black.png",1));
					else if(c=='p')
						//p =wb01;
						boardState[i][j].setPiece(new Pawn("p","pawn-white.png",0));
					else if(c=='K')
						//p =wb01;
						boardState[i][j].setPiece(new King("K","king-black.png",1,i,j));
					else if(c=='k')
						//p =bk;
						boardState[i][j].setPiece(new King("k","king-white.png",0,i,j));
					}
			}
					//cellll=new Cell(i,j,p);
					//boardState[i][j]=cellll;
				}
			if (chessboard.get(8).equals("w")){
				chance=0;
			}else {
				chance=1;
			}

			timer.reset();
			timer.start();
			showPlayer.remove(CHNC);
			if (chance==0){
				Main.move="White";
			}else {
				Main.move="Black";
			}

			CHNC.setText(Main.move);
			showPlayer.add(CHNC);
		}
	}

	class SaveGame implements ActionListener, Serializable {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser=new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int result=chooser.showOpenDialog(null);
			File filePath=chooser.getSelectedFile();
			String path=filePath.getAbsolutePath()+File.separator+System.currentTimeMillis()+".txt";
			File file=new File(path);
			if (!file.exists()){
				try {
					file.createNewFile();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			FileOutputStream fos=null;
			OutputStreamWriter osw=null;
			try {
				//创建文件输出流对象
				fos=new FileOutputStream(file);
				osw=new OutputStreamWriter(fos);
				osw.write(getChessboardGraph());
				osw.close();
				//创建文件对象输出流对象
//				oos=new ObjectOutputStream(fos);
//				oos.writeObject(getChessboardGraph());
//				fos.wr
				//接下来处理文件不存在的异常
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (fos!=null){
					try {
						fos.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}


		}

		public String getChessboardGraph() {
			StringBuilder stringBuilder=new StringBuilder();
			for (int i=0;i<8;i++){
				for (int j=0;j<8;j++){
					if (boardState[i][j].getpiece()==null){
						stringBuilder.append('_');
						continue;
					}else {
						stringBuilder.append(boardState[i][j].getpiece().getId());
					}
				}
				stringBuilder.append("\n");
			}
			if (chance==0){
				stringBuilder.append("w");
			} else if (chance==1) {
				stringBuilder.append("b");
			}
			return String.valueOf(stringBuilder);
		}
	}
	class Restart implements ActionListener
	{

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		//boardState=new Cell[8][8];
		
				for(int i=0;i<8;i++)
					for(int j=0;j<8;j++)
					{	
						//System.out.print(boardState[i][j].getpiece());
						boardState[i][j].removePiece();
						System.out.print(boardState[i][j].getpiece());
						//p = null;
						//boardState[i][j].removePiece();
						if(i==0&&j==0)
							//p = br01;
							boardState[i][j].setPiece(br01);
						else if(i==0&&j==7)
							//p = br01;
							boardState[i][j].setPiece(br02);
						else if(i==7&&j==0)
							//p = br01;
							boardState[i][j].setPiece(wr01);
						else if(i==7&&j==7)
							//p = br01;
							boardState[i][j].setPiece(wr02);
						else if(i==0&&j==1)
							//p =bk01;
							boardState[i][j].setPiece(bk01);
						else if (i==0&&j==6)
							//p =bk02;
							boardState[i][j].setPiece(bk02);
						else if(i==7&&j==1)
							//p =wk01;
							boardState[i][j].setPiece(wk01);
						else if (i==7&&j==6)
							//p =wk02;
							boardState[i][j].setPiece(wk02);
						else if(i==0&&j==2)
							//p =bb01;
							boardState[i][j].setPiece(bb01);
						else if (i==0&&j==5)
							//p =bb02;
							boardState[i][j].setPiece(bb02);
						else if(i==7&&j==2)
							//p =wb01;
							boardState[i][j].setPiece(wb01);
						else if(i==7&&j==5)
							//p =wb01;
							boardState[i][j].setPiece(wb01);
						else if(i==0&&j==3)
							//p =bk;
							boardState[i][j].setPiece(bk);
						else if(i==0&&j==4)
							//p =bq;
							boardState[i][j].setPiece(bq);
						else if(i==7&&j==3)
							//p =wk;
							boardState[i][j].setPiece(wk);
						else if(i==7&&j==4)
							//p =wq;
							boardState[i][j].setPiece(wq);
						else if(i==1)
							//p =bp[j];
							boardState[i][j].setPiece(bp[j]);
						else if(i==6)
							//p =wp[j];
							boardState[i][j].setPiece(wp[j]);
						else {
							boardState[i][j].setcheck();
							boardState[i][j].removePiece();
							boardState[i][j].removecheck();
						}
						//cellll=new Cell(i,j,p);
						//boardState[i][j]=cellll;
					}	
				chance=0;
				timer.reset();
				timer.start();
				showPlayer.remove(CHNC);
				Main.move="White";
				CHNC.setText(Main.move);
				showPlayer.add(CHNC);
	}
	}
	class ChangeBackground implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser=new JFileChooser();
			//chooser.setFileSelectionMode(JFileChooser.);
			int result=chooser.showOpenDialog(null);
			File file=chooser.getSelectedFile();
			String filePath=file.getAbsolutePath();


		}
	}





	
	class TimeChange implements ChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent arg0)
		{
			timeRemaining=timeSlider.getValue()*60;
		}
	}
	
	class SelectHandler implements ActionListener {
		private int color;

		SelectHandler(int i) {
			color=i;
		}
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			tempPlayer=null;
			String n=(color==0)?wname:bname;
			JComboBox<String> jc=(color==0)?wcombo:bcombo;
			JComboBox<String> ojc=(color==0)?bcombo:wcombo;
			ArrayList<Player> pl=(color==0)?wplayer:bplayer;
			ArrayList<Player> opl=Player.fetch_players();
			if(opl.isEmpty()){
				return;}
			JPanel det=(color==0)?wdetails:bdetails;
			JPanel PL=(color==0)?WhitePlayer:BlackPlayer;
			if(selected==true){
				det.removeAll();}
			n=(String)jc.getSelectedItem();
			Iterator<Player> it=pl.iterator();
			Iterator<Player> oit=opl.iterator();
			while(it.hasNext()) {
				Player p=it.next();
				if(p.name().equals(n)) {
					tempPlayer=p;
					break;}
			}
			while(oit.hasNext()) {
				Player p=oit.next();
				if(p.name().equals(n)){
					opl.remove(p);
					break;}
			}

			if(tempPlayer==null)
				return;
			if(color==0)
				White=tempPlayer;
			else
				Black=tempPlayer;
			bplayer=opl;
			ojc.removeAllItems();
			for (Player s:opl)
				ojc.addItem(s.name());
			det.add(new JLabel(" "+tempPlayer.name()));
			det.add(new JLabel(" "+tempPlayer.gamesplayed()));
			det.add(new JLabel(" "+tempPlayer.gameswon()));

			PL.revalidate();
			PL.repaint();
			PL.add(det);
			selected=true;
		}

	}
		
	class Handler implements ActionListener{
		private int color;
		Handler(int i)
		{
			color=i;
		}
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String n=(color==0)?wname:bname;
			JPanel j=(color==0)?WhitePlayer:BlackPlayer;
			ArrayList<Player> N=Player.fetch_players();
			Iterator<Player> it=N.iterator();
			JPanel det=(color==0)?wdetails:bdetails;
			n=JOptionPane.showInputDialog(j,"Enter Your Name");
			if(n!=null)
			{

				while(it.hasNext())
				{
					if(it.next().name().equals(n))
					{JOptionPane.showMessageDialog(j,"Player Exists");
						return;}
				}

				if(n.length()!=0)
				{Player tem=new Player(n);
					tem.Update_Player();
					if(color==0)
						White=tem;
					else
						Black=tem;
				}
				else return;
			}
			else
				return;
			det.removeAll();
			det.add(new JLabel(" "+n));
			det.add(new JLabel(" 0"));
			det.add(new JLabel(" 0"));
			j.revalidate();
			j.repaint();
			j.add(det);
			selected=true;
		}
	}
	class musicStuff {
		void playMusic(String musicLocation){
			try {
				File musicPath = new File(musicLocation);
				if(musicPath.exists()){
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
				Clip clip = AudioSystem.getClip();
				clip.open(audioInput);
				clip.start();
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			}
		}
	}


