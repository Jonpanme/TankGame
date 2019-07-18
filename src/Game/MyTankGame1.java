/*
 * by：梅鹏
  * 实现坦克大战
  * 1： 画出坦克
  * 2：我的坦克上下左右移动
  * 3：坦克可以发射子弹，可以连发
  * 4：当击中敌人坦克，敌人坦克爆炸消失
  * 5：我被击中后，爆炸消失
  * 
 * */
package Game;
import javax.swing.*;

import Game.Bomb;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

public class MyTankGame1 extends JFrame{
	MyPanel mp=null;
	public MyTankGame1()
	{
		mp=new MyPanel();
		Thread t1=new Thread(mp);
		t1.start();
		this.add(mp);
		this.addKeyListener(mp);
		this.addMouseListener(mp);
		this.setSize(800,800);
		this.setVisible(true);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyTankGame1 mtg=new MyTankGame1();
	}

} 
class MyPanel extends JPanel implements KeyListener,Runnable,MouseListener
{
	int m=600;//初始横坐标
	int n=600;//初始纵坐标
	//定义自己的坦克
	Hero hero=null;
	//定义敌人坦克
	Vector<Enemy> ets=new Vector<Enemy>();
	//敌人坦克数量
	int enSize=3;
	//定义炸弹集合
	Vector<Bomb> bombs=new Vector<Bomb>();
	
	//定义三张图片
		Image image1=null;
		Image image2=null;
		Image image3=null;
			
	public MyPanel()
	{
		hero=new Hero(m,n);
		//初始化敌人坦克
		for(int i=0;i<enSize;i++)
		{
			//创建敌人坦克
			Enemy et=new Enemy((i+1)*50,0);
			et.setColor(0);
			//加入
			ets.add(et);
			//启动敌人坦克
			Thread t=new Thread(et);
			t.start();
			Shot s=new Shot(et.x+10,et.y+30,2);
			et.ss.add(s);
			//启动线程
			Thread t2=new Thread(s);
			t2.start();
			
		}
		image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/boom1.png"));
		image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/boom2.png"));
		image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/boom3.png"));
		
	}
	public void paint(Graphics g)
	{
		super.paint(g);
		g.setColor(Color.black);
		//清除当前屏幕上的所有东西
		g.fillRect(0, 0, 800, 800);
		//画出自己的坦克
		if(hero.isLive)
		{
		this.drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 0);
		}
		
		//从ss中取出每一颗子弹并且画出
		for(int i=0;i<hero.ss.size();i++)
		 {	
			Shot myshot=hero.ss.get(i);//得到子弹集合中的每一个子弹
		 
		//画出一颗子弹
		if(myshot!=null&&myshot.isLive==true)
		{
			g.fill3DRect(myshot.x, myshot.y, 2, 2, false);
		}    
		if(myshot.isLive==false)
		{
			hero.ss.remove(myshot);
		}
		}
		
		//画出炸弹效果
		//第一次击中没有爆炸效果？
		if(bombs.size()>0)
		{
		System.out.println(bombs.size());
		for(int i=0;i<bombs.size();i++)
		{
			Bomb b=bombs.get(i);
			if(b.life>6)
			{
				g.drawImage(image1, b.x, b.y, 30, 30,this);
			}
			else if(b.life>4)
			{
				g.drawImage(image2, b.x, b.y, 30, 30,this);
			}
			else
			{
				g.drawImage(image3, b.x, b.y, 30, 30,this);
			}
			if(b.life>0)
			{
			//减少生命值
			b.lifeDown();
			}
			//炸弹生命值为0，死亡
			if(b.life==0)
			{
				bombs.remove(b);
			}
		}
		}
		
		//画出敌人坦克
		for(int i=0;i<ets.size();i++)
		{
			Enemy et=ets.get(i); 
			if(et.isLive)
			{
	        this.drawTank(et.getX(), et.getY(), g, et.getDirect(),1);
	      //画出敌人坦克子弹
	        for(int j=0;j<et.ss.size();j++)
	        {
	        	Shot emshot=et.ss.get(j);
	        	if(emshot.isLive==true)
	        	{
	        		g.fill3DRect(emshot.x, emshot.y, 2, 2, false);
	        	}
	        	else
	        	{
	        		et.ss.remove(emshot);
	        	}
	        }
			}
		}
	}
	
	//判断敌人子弹是否击中我
	public void hitMe()
	{
		//取出每一个敌人坦克
		for(int i=0;i<ets.size();i++)
		{
			Enemy et=ets.get(i);
			//取出每一颗子弹
			if(hero.isLive==true)
			for(int j=0;j<et.ss.size();j++)
			{
				Shot enemyshot=et.ss.get(j);
				this.hitTank(enemyshot, hero);
			}
			
		}
	}
	
	
	//判断我的坦克是否击中敌人坦克,这里是取出每一辆坦克我我的子弹进行判断是不是击中
	public void hitEnemyTank()
	{
		 for(int i=0;i<hero.ss.size();i++)
		 {
			 //取出子弹
			 Shot myshot=hero.ss.get(i);
			 //判断子弹是否有效
			 if(myshot.isLive)
			 for(int j=0;j<ets.size();j++)
			 {
				 //取出坦克
				 Enemy et =ets.get(j);
				 if(et.isLive)
				 {
					 this.hitTank(myshot, et);
				 }			 
			 }
		 }	
		
	}
	//画出坦克
	public void drawTank(int x,int y,Graphics g,int direct,int type)
	{
		switch(type)//判断坦克类型 
		{
		case 0://我方坦克
			g.setColor(Color.CYAN);
			break;
		case 1://敌方坦克
			g.setColor(Color.YELLOW);
			break;
		}
		switch(direct)
		{
		case 0://向上
			//画出坦克
			//1:画出左边矩形
			g.fill3DRect(x, y, 5, 30, false);
			//2.画出中间矩形
			g.fill3DRect(x+5, y+5, 10, 20, false);
			//3.画出左边矩形
			g.fill3DRect(x+15, y, 5, 30, false);
			//4.画出中间的园
			g.fillOval(x+5, y+10, 8, 8);
			//5.画出直线炮杆
			g.drawLine(x+10, y+7, x+10, y);
			break;
		case 1://向右
			//画出坦克
			//1:画出上面边矩形
			g.fill3DRect(x, y, 30, 5, false);
			//2.画出中间矩形
			g.fill3DRect(x+5, y+5, 20, 10, false);
			//3.画出下面矩形
			g.fill3DRect(x, y+15, 30, 5, false);
			//4.画出中间的园
			g.fillOval(x+9, y+5, 8, 8);
			//5.画出直线炮杆
			g.drawLine(x+17, y+9, x+27, y+9);
			break;
			
		case 2://向下
			//画出坦克
			//1:画出左边矩形
			g.fill3DRect(x, y, 5, 30, false);
			//2.画出中间矩形
			g.fill3DRect(x+5, y+5, 10, 20, false);
			//3.画出左边矩形
			g.fill3DRect(x+15, y, 5, 30, false);
			//4.画出中间的园
			g.fillOval(x+5, y+10, 8, 8);
			//5.画出直线炮杆
			g.drawLine(x+10, y+12, x+10, y+27);
			break;
		case 3://向左
			//画出坦克
			//1:画出上面边矩形
			g.fill3DRect(x, y, 30, 5, false);
			//2.画出中间矩形
			g.fill3DRect(x+5, y+5, 20, 10, false);
			//3.画出下面矩形
			g.fill3DRect(x, y+15, 30, 5, false);
			//4.画出中间的园
			g.fillOval(x+9, y+5, 8, 8);
			//5.画出直线炮杆
			g.drawLine(x+15, y+9, x+1, y+9);
			break;
			
		}
	}
	//判断是否击中敌人坦克
	public void hitTank(Shot s,Tank ta)
	{
		//判断坦克的方向
		switch(ta.direct)
		{//方向是冲上或者下
		case 0:
		case 2:
			if(s.x>ta.x&&s.x<ta.x+20&&s.y>ta.y&&s.y<ta.y+30)
			{
				//击中
				//子弹死亡
				s.isLive=false;
				//坦克死亡
				ta.isLive=false;

				//创建一颗炸弹
				Bomb bm=new Bomb(ta.x,ta.y);
				
				//放入集合
				this.bombs.add(bm);
				//System.out.println(ta.isLive);
				
			}
		case 1:
		case 3:
			if(s.x>ta.x&&s.x<ta.x+30&&s.y>ta.y&&s.y<ta.y+20)
			{
				//击中
				//子弹死亡
				s.isLive=false;
				//坦克死亡
				ta.isLive=false;

				//创建一颗炸弹a
				Bomb bm=new Bomb(ta.x,ta.y);
				//放入集合
				this.bombs.add(bm);
				System.out.println(ta.isLive);
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	//按键被按下
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_S)
		{
		  hero.moveDown();
		  //方向向下
		  hero.direct=2;		
		}
		else if(e.getKeyCode()==KeyEvent.VK_W)
		{
			hero.moveUp();
			hero.direct=0;
		}
		else if(e.getKeyCode()==KeyEvent.VK_A)
		{
			hero.moveLeft();
			hero.direct=3;
		}
		else if(e.getKeyCode()==KeyEvent.VK_D)
		{
			hero.moveRight();
			hero.direct=1;
		}
		//整个面板进行了重画所以这个重画就不需要了
		//this.repaint();
		
		
	}
	
	public void keyReleased(KeyEvent e) {

		
	}
	//鼠标单击左键 
	public void mouseClicked(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON1)
		{
			if(hero.ss.size()<5&&hero.isLive==true)
			hero.shouEnemy();
			
		}
		//this.repaint();
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void run() 
	{
		while(true)
		{
		 try{
			 Thread.sleep(100);
		 }
		 catch(Exception e)
		 {
			 
		 }		 
		 //在run函数中一直要判断是否被击中敌人坦克
		 this.hitEnemyTank();
         //判断敌人是否击中我的坦克
		 this.hitMe();
		 //整个Panle做成一个线程每隔100毫秒进行重绘保证了坦克、子弹、炸弹的动态效果
		 this.repaint();
		}		
	}	
}
