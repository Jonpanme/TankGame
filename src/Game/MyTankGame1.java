/*
 * by��÷��
  * ʵ��̹�˴�ս
  * 1�� ����̹��
  * 2���ҵ�̹�����������ƶ�
  * 3��̹�˿��Է����ӵ�����������
  * 4�������е���̹�ˣ�����̹�˱�ը��ʧ
  * 5���ұ����к󣬱�ը��ʧ
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
	int m=600;//��ʼ������
	int n=600;//��ʼ������
	//�����Լ���̹��
	Hero hero=null;
	//�������̹��
	Vector<Enemy> ets=new Vector<Enemy>();
	//����̹������
	int enSize=3;
	//����ը������
	Vector<Bomb> bombs=new Vector<Bomb>();
	
	//��������ͼƬ
		Image image1=null;
		Image image2=null;
		Image image3=null;
			
	public MyPanel()
	{
		hero=new Hero(m,n);
		//��ʼ������̹��
		for(int i=0;i<enSize;i++)
		{
			//��������̹��
			Enemy et=new Enemy((i+1)*50,0);
			et.setColor(0);
			//����
			ets.add(et);
			//��������̹��
			Thread t=new Thread(et);
			t.start();
			Shot s=new Shot(et.x+10,et.y+30,2);
			et.ss.add(s);
			//�����߳�
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
		//�����ǰ��Ļ�ϵ����ж���
		g.fillRect(0, 0, 800, 800);
		//�����Լ���̹��
		if(hero.isLive)
		{
		this.drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 0);
		}
		
		//��ss��ȡ��ÿһ���ӵ����һ���
		for(int i=0;i<hero.ss.size();i++)
		 {	
			Shot myshot=hero.ss.get(i);//�õ��ӵ������е�ÿһ���ӵ�
		 
		//����һ���ӵ�
		if(myshot!=null&&myshot.isLive==true)
		{
			g.fill3DRect(myshot.x, myshot.y, 2, 2, false);
		}    
		if(myshot.isLive==false)
		{
			hero.ss.remove(myshot);
		}
		}
		
		//����ը��Ч��
		//��һ�λ���û�б�ըЧ����
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
			//��������ֵ
			b.lifeDown();
			}
			//ը������ֵΪ0������
			if(b.life==0)
			{
				bombs.remove(b);
			}
		}
		}
		
		//��������̹��
		for(int i=0;i<ets.size();i++)
		{
			Enemy et=ets.get(i); 
			if(et.isLive)
			{
	        this.drawTank(et.getX(), et.getY(), g, et.getDirect(),1);
	      //��������̹���ӵ�
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
	
	//�жϵ����ӵ��Ƿ������
	public void hitMe()
	{
		//ȡ��ÿһ������̹��
		for(int i=0;i<ets.size();i++)
		{
			Enemy et=ets.get(i);
			//ȡ��ÿһ���ӵ�
			if(hero.isLive==true)
			for(int j=0;j<et.ss.size();j++)
			{
				Shot enemyshot=et.ss.get(j);
				this.hitTank(enemyshot, hero);
			}
			
		}
	}
	
	
	//�ж��ҵ�̹���Ƿ���е���̹��,������ȡ��ÿһ��̹�����ҵ��ӵ������ж��ǲ��ǻ���
	public void hitEnemyTank()
	{
		 for(int i=0;i<hero.ss.size();i++)
		 {
			 //ȡ���ӵ�
			 Shot myshot=hero.ss.get(i);
			 //�ж��ӵ��Ƿ���Ч
			 if(myshot.isLive)
			 for(int j=0;j<ets.size();j++)
			 {
				 //ȡ��̹��
				 Enemy et =ets.get(j);
				 if(et.isLive)
				 {
					 this.hitTank(myshot, et);
				 }			 
			 }
		 }	
		
	}
	//����̹��
	public void drawTank(int x,int y,Graphics g,int direct,int type)
	{
		switch(type)//�ж�̹������ 
		{
		case 0://�ҷ�̹��
			g.setColor(Color.CYAN);
			break;
		case 1://�з�̹��
			g.setColor(Color.YELLOW);
			break;
		}
		switch(direct)
		{
		case 0://����
			//����̹��
			//1:������߾���
			g.fill3DRect(x, y, 5, 30, false);
			//2.�����м����
			g.fill3DRect(x+5, y+5, 10, 20, false);
			//3.������߾���
			g.fill3DRect(x+15, y, 5, 30, false);
			//4.�����м��԰
			g.fillOval(x+5, y+10, 8, 8);
			//5.����ֱ���ڸ�
			g.drawLine(x+10, y+7, x+10, y);
			break;
		case 1://����
			//����̹��
			//1:��������߾���
			g.fill3DRect(x, y, 30, 5, false);
			//2.�����м����
			g.fill3DRect(x+5, y+5, 20, 10, false);
			//3.�����������
			g.fill3DRect(x, y+15, 30, 5, false);
			//4.�����м��԰
			g.fillOval(x+9, y+5, 8, 8);
			//5.����ֱ���ڸ�
			g.drawLine(x+17, y+9, x+27, y+9);
			break;
			
		case 2://����
			//����̹��
			//1:������߾���
			g.fill3DRect(x, y, 5, 30, false);
			//2.�����м����
			g.fill3DRect(x+5, y+5, 10, 20, false);
			//3.������߾���
			g.fill3DRect(x+15, y, 5, 30, false);
			//4.�����м��԰
			g.fillOval(x+5, y+10, 8, 8);
			//5.����ֱ���ڸ�
			g.drawLine(x+10, y+12, x+10, y+27);
			break;
		case 3://����
			//����̹��
			//1:��������߾���
			g.fill3DRect(x, y, 30, 5, false);
			//2.�����м����
			g.fill3DRect(x+5, y+5, 20, 10, false);
			//3.�����������
			g.fill3DRect(x, y+15, 30, 5, false);
			//4.�����м��԰
			g.fillOval(x+9, y+5, 8, 8);
			//5.����ֱ���ڸ�
			g.drawLine(x+15, y+9, x+1, y+9);
			break;
			
		}
	}
	//�ж��Ƿ���е���̹��
	public void hitTank(Shot s,Tank ta)
	{
		//�ж�̹�˵ķ���
		switch(ta.direct)
		{//�����ǳ��ϻ�����
		case 0:
		case 2:
			if(s.x>ta.x&&s.x<ta.x+20&&s.y>ta.y&&s.y<ta.y+30)
			{
				//����
				//�ӵ�����
				s.isLive=false;
				//̹������
				ta.isLive=false;

				//����һ��ը��
				Bomb bm=new Bomb(ta.x,ta.y);
				
				//���뼯��
				this.bombs.add(bm);
				//System.out.println(ta.isLive);
				
			}
		case 1:
		case 3:
			if(s.x>ta.x&&s.x<ta.x+30&&s.y>ta.y&&s.y<ta.y+20)
			{
				//����
				//�ӵ�����
				s.isLive=false;
				//̹������
				ta.isLive=false;

				//����һ��ը��a
				Bomb bm=new Bomb(ta.x,ta.y);
				//���뼯��
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
	//����������
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_S)
		{
		  hero.moveDown();
		  //��������
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
		//�������������ػ���������ػ��Ͳ���Ҫ��
		//this.repaint();
		
		
	}
	
	public void keyReleased(KeyEvent e) {

		
	}
	//��굥����� 
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
		 //��run������һֱҪ�ж��Ƿ񱻻��е���̹��
		 this.hitEnemyTank();
         //�жϵ����Ƿ�����ҵ�̹��
		 this.hitMe();
		 //����Panle����һ���߳�ÿ��100��������ػ汣֤��̹�ˡ��ӵ���ը���Ķ�̬Ч��
		 this.repaint();
		}		
	}	
}
