package Game;

import java.util.Vector;

class Tank
{
	boolean isLive=true;
	int x=10;//̹�˺�����
	int y=10;//̹��������
	int direct=0;//��ʾ̹�˷���0���ϣ�1���ң�2���£�3����
	int spead=2;//̹����
	int color;//̹����ɫ
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
//�ҷ�̹��
class Hero extends Tank
{
	int spead=5;
	//�Լ�̹���ӵ���
	Vector<Shot> ss=new Vector<Shot>();
	Shot s=null;
	public Hero(int x,int y)
	{
		super(x,y);
	}
	//����
	public void moveUp()
	{
		if(this.y>0)
		y-=spead;
	}
	//����
	public void moveRight()
	{
		if(this.x<750)
		x+=spead;
	}
	//����
	public void moveDown()
	{
		if(this.y<710)
		y+=spead;
	}
	//����
	public void moveLeft()
	{
		if(x>0)
		x-=spead;
	}
	public void shouEnemy()//����
	{
		switch(this.direct)
		{
		//����
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
//�з�̹��
class Enemy extends Tank implements Runnable
{
	//���˵��ӵ�
	Vector<Shot> ss=new Vector<Shot>();
	//������Ҫ����Ϊ��hittank�����У��ı����Tank��isLive��
	//�����ֲ���������ȫ�ֱ������µ��˲�������
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
			{//̹��������
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
			//�����������
		this.direct=(int)(Math.random()*4);
		
		//�ж��ǲ��ǻ���
		if(this.isLive==false)
		{
			//��̹���������˳��߳�
			break;
		}
			
		
			//�жϵ����Ƿ���Ҫ�����ӵ�
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

//�ӵ���
class Shot implements Runnable
{
	//�ӵ�������
	int x;
	//�ӵ�������
	int y;
	//�ӵ�����
	int direct;
	//�ӵ��ٶ�
	 int spead=5;
	//�ж��ӵ��ǲ��Ǵ���
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
//����һ��ը���࣬����������ըЧ��
class Bomb
{
	//ը������
	int x,y;
	//ը����������
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
