package chess;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;


/**
 *实现对玩家信息的更新存储
 */
public class Player implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;//玩家姓名
	private Integer gamesplayed;//玩过的回合数
	private Integer gameswon;//赢过的回合数


	public Player(String name)
	{
		this.name = name.trim();
		gamesplayed=0;
		gameswon=0;
	}

	public String name()
	{
		return name;
	}


	public Integer gamesplayed()
	{
		return gamesplayed;
	}

	public Integer gameswon()
	{
		return gameswon;
	}

	//更新回合数
	public void updateGamesPlayed()
	{
		gamesplayed++;
	}
	//更新赢棋数
	public void updateGamesWon()
	{
		gameswon++;
	}

	//获取玩家列表
	public static ArrayList<Player> fetch_players()
	{
		Player tempplayer;
		ObjectInputStream input = null;
		ArrayList<Player> players = new ArrayList<Player>();
		try
		{
			File infile = new File(System.getProperty("user.dir")+ File.separator + "chessgamedata.dat");
			input = new ObjectInputStream(new FileInputStream(infile));
			try
			{
				while(true)
				{
					tempplayer = (Player) input.readObject();
					players.add(tempplayer);
				}
			}
			catch(EOFException e)
			{
				input.close();
			}
		}
		catch (FileNotFoundException e)
		{
			players.clear();
			return players;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			try {input.close();} catch (IOException e1) {}
			JOptionPane.showMessageDialog(null, "Unable to Read the Required Game Files !!");
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Game Data File Corrupted !! Click Ok to Continue Builing New File");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return players;
	}

	//更新玩家信息
	public void Update_Player()
	{
		ObjectInputStream input = null;
		ObjectOutputStream output = null;
		Player temp_player;
		File inputfile=null;
		File outputfile=null;
		try
		{
			inputfile = new File(System.getProperty("user.dir")+ File.separator + "chessgamedata.dat");
			outputfile = new File(System.getProperty("user.dir")+ File.separator + "tempfile.dat");
		} catch (SecurityException e)
		{
			JOptionPane.showMessageDialog(null, "Read-Write Permission Denied !! Program Cannot Start");
			System.exit(0);
		}
		boolean playerdonotexist;
		try
		{
			if(outputfile.exists()==false)
				outputfile.createNewFile();
			if(inputfile.exists()==false)
			{
				output = new ObjectOutputStream(new java.io.FileOutputStream(outputfile,true));
				output.writeObject(this);
			}
			else
			{
				input = new ObjectInputStream(new FileInputStream(inputfile));
				output = new ObjectOutputStream(new FileOutputStream(outputfile));
				playerdonotexist=true;
				try
				{
					while(true)
					{
						temp_player = (Player)input.readObject();
						if (temp_player.name().equals(name()))
						{
							output.writeObject(this);
							playerdonotexist = false;
						}
						else
							output.writeObject(temp_player);
					}
				}
				catch(EOFException e){
					input.close();
				}
				if(playerdonotexist)
					output.writeObject(this);
			}
			inputfile.delete();
			output.close();
			File newf = new File(System.getProperty("user.dir")+ File.separator + "chessgamedata.dat");
			if(outputfile.renameTo(newf)==false)
				System.out.println("File Renameing Unsuccessful");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to Read/Write the Required Game Files . Click Ok to Continue");
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Game Data File Corrupted . Click Ok to Continue Builing New File");
		}
		catch (Exception e)
		{

		}
	}
}
