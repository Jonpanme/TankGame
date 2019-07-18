package Game;

import java.util.Vector;

class Tank
{
	boolean isLive=true;
	int x=10;//坦克横坐标
	int y=10;//坦克纵坐标
	int direct=0;//表示坦克方向，0向上，1向右，2向下，3向左
	int spead=2;//坦克速
	int color;//坦克颜色
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getSpead() {
		return spead;
	}
	public void setSpead(int spead) {
		this.spead = spead;
	}
	public int getDirect() {
		return direct;
	}
	public void setDirect(int direct) {
		this.direct = direct;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public Tank(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
}
//我方坦克
class Hero extends Tank
{
	int spead=5;
	//自己坦克子弹类
	Vector<Shot> ss=new Vector<Shot>();
	Shot s=null;
	public Hero(int x,int y)
	{
		super(x,y);
	}
	//上移
	public void moveUp()
	{
		if(this.y>0)
		y-=spead;
	}
	//右移
	public void moveRight()
	{
		if(this.x<750)
		x+=spead;
	}
	//下移
	public void moveDown()
	{
		if(this.y<710)
		y+=spead;
	}
	//左移
	public void moveLeft()
	{
		if(x>0)
		x-=spead;
	}
	public void shouEnemy()//开火
	{
		switch(this.direct)
		{
		//向上
		case 0:
		    s=new Shot(x+10, y,0);
			ss.add(s);
			break;
		case 1:
			s=new Shot(x+27, y+9,1);
			ss.add(s);
			break;
		case 2:
			s=new Shot(x+10, y+27,2);
			ss.add(s);
			break;
		case 3:
			s=new Shot(x+1, y+9,3);
			ss.add(s);
			break;
		}
		Thread t=new Thread(s);
		t.start();
		
	}
}
//敌方坦克
class Enemy extends Tank implements Runnable
{
	//敌人的子弹
	Vector<Shot> ss=new Vector<Shot>();
	//不再需要，因为在hittank函数中，改变的是Tank的isLive，
	//这里会局部变量覆盖全局变量导致敌人不会死亡
	//boolean isLive=true;
	public Enemy (int x,int y)
	{
		super(x,y);
	}
	@Override
	public void run() {
			while(true)
			{
			switch(this.direct)
			{//坦克向上走
			case 0:
				for(int i=0;i<30;i++)
				{
					if(y>0)
					y-=spead;
					try {
						Thread.sleep(50);
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
				break;
			case 1:
				for(int i=0;i<30;i++)
				{
					if(x<750)
					x+=spead;
					try {
						Thread.sleep(50);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				break;
			case 2:
				for(int i=0;i<30;i++)
				{
					if(y<710)
					y+=spead;
					try {
						Thread.sleep(50);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				break;
			case 3:
				for(int i=0;i<30;i++)
				{
					if(x>0)
					x-=spead;
					try {
						Thread.sleep(50);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				break;
			}
			//随机产生方向
		this.direct=(int)(Math.random()*4);
		
		//判断是不是活着
		if(this.isLive==false)
		{
			//让坦克死亡，退出线程
			break;
		}
			
		
			//判断敌人是否需要加入子弹
			 if(isLive)
			 {
				 if(ss.size()<5)
				 {
					 Shot s=null;
					 switch(direct)
					 {
				 case 0:
					    s=new Shot(x+10, y,0);
						ss.add(s);
						break;
					case 1:
						s=new Shot(x+27,y+9,1);
						ss.add(s);
						break;
					case 2:
						s=new Shot(x+10,y+27,2);
						ss.add(s);
						break;
					case 3:
						s=new Shot(x+1, y+9,3);
						ss.add(s);
						break;
					 }
					 Thread t3=new Thread(s);
					 t3.start();
				 }
			 }
			}	 

		
	}
}

//子弹类
class Shot implements Runnable
{
	//子弹横坐标
	int x;
	//子弹纵坐标
	int y;
	//子弹方向
	int direct;
	//子弹速度
	 int spead=5;
	//判断子弹是不是存在
	 boolean isLive=true;
	 
	public Shot(int x,int y,int direct)
	{
	  this.x=x;
	  this.y=y;
	  this.direct=direct;
	}
	@Override
	public void run() {
		while(true)
		{
			try{
				Thread.sleep(50);
			}
			catch(Exception e)
			{
				
			}
		   switch(direct)
		   {
		   case 0:
			   y-=spead;
			   break;
		   case 1:
			   x+=spead;
			   break;
		   case 2:
			   y+=spead;
			   break;
		   case 3:
			   x-=spead;
			   break;		      
		   }
		 //  System.out.println("x="+x+"y="+y);
		   if(x<0||x>800||y<0||y>800||this.isLive==false)
		   {
			   isLive=false;
			   break;
		   }
		}

		
	}
}
//定义一个炸弹类，用来制作爆炸效果
class Bomb
{
	//炸弹坐标
	int x,y;
	//炸弹过程生命
	int life=9;
	boolean isLive=true;
	public Bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	public void lifeDown()
	{
		if(life>0)
		{
			life--;
		}
		else
			this.isLive=false;
	}

	
}
