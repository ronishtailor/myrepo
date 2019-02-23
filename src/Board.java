import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Board extends JFrame {
	JPanel panMiddle;
	int boardWidth=600,boardHeight=700,speed=3,coinWidth=50,coinHeight=50,strikerWidth=50,strikerHeight=50;
	int strikerX=(boardWidth-strikerWidth)/2,strikerY=boardHeight-strikerHeight;
	int scorePlayer=0,scoreComputer=0,strikerXComputer=(boardWidth-strikerWidth)/2,strikerYComputer=0;
	int predictedX=0,timer=300;
	ImageIcon iiCoin,iiStriker,iiStrikerComputer;
	JButton btnCoin,btnStriker,btnStrikerComputer;
	JLabel lblPlayer,lblComputer,lblTimer;
	Thread t1;
	Board(){		
		lblPlayer=new JLabel("Player Score  : 0");
		lblPlayer.setBounds(650,50,150,50);
		lblComputer=new JLabel("Computer Score: 0");
		lblComputer.setBounds(650,150,150,50);
		panMiddle=new MyJPanel();
		panMiddle.setLayout(null);
		panMiddle.add(lblPlayer);
		panMiddle.add(lblComputer);
		lblTimer=new JLabel("300");
		lblTimer.setBounds(650,0,150,50);
		panMiddle.add(lblTimer);
		new Thread() {
			public void run() {
				while(timer>0) {
					try {
						Thread.sleep(1000);
					}
					catch(InterruptedException e) {
						e.printStackTrace();
					}	
					timer--;
					lblTimer.setText(timer+"");
					if(timer==0) {
						t1.stop();
						if(scoreComputer>scorePlayer)
							JOptionPane.showMessageDialog(Board.this, "You loose the game");
						else if(scoreComputer<scorePlayer)
							JOptionPane.showMessageDialog(Board.this, "You win the game");
						else
							JOptionPane.showMessageDialog(Board.this, "Game Tie");
					}
					int ans=JOptionPane.showOptionDialog(Board.this,"","",0,JOptionPane.QUESTION_MESSAGE,null,new String[] {"Restart","Quit"},"Restart");
					if(ans==0) {
						scoreComputer=scorePlayer=0;
						lblPlayer=new JLabel("Player Score  : 0");
						lblComputer=new JLabel("Computer Score: 0");
						timer=300;
						lblTimer.setText("300");
						t1.start();
					}
					else {
						dispose();
					}
				}
			}
		}.start();

		iiCoin=new ImageIcon("images/coin.png");
		iiCoin=new ImageIcon(iiCoin.getImage().getScaledInstance(coinWidth, coinHeight,Image.SCALE_FAST ));
		btnCoin=new JButton(iiCoin);
		btnCoin.setContentAreaFilled(false);
		btnCoin.setBorderPainted(false);
		btnCoin.setFocusable(false);
		btnCoin.setFocusPainted(false);
		btnCoin.setBounds(0, 0, coinWidth, coinHeight);
		panMiddle.add(btnCoin);
		iiStriker=new ImageIcon("images/striker.png");
		iiStriker=new ImageIcon(iiStriker.getImage().getScaledInstance(strikerWidth, strikerHeight,Image.SCALE_FAST ));
		btnStriker=new JButton(iiStriker);
		btnStriker.setContentAreaFilled(false);
		btnStriker.setBorderPainted(false);
		btnStriker.setFocusable(false);
		btnStriker.setFocusPainted(false);
		btnStriker.setBounds(strikerX,strikerY,strikerWidth,strikerHeight);
		panMiddle.add(btnStriker);

		iiStrikerComputer=new ImageIcon("images/striker.png");
		iiStrikerComputer=new ImageIcon(iiStrikerComputer.getImage().getScaledInstance(strikerWidth, strikerHeight,Image.SCALE_FAST ));
		btnStrikerComputer=new JButton(iiStrikerComputer);
		btnStrikerComputer.setContentAreaFilled(false);
		btnStrikerComputer.setBorderPainted(false);
		btnStrikerComputer.setFocusable(false);
		btnStrikerComputer.setFocusPainted(false);
		btnStrikerComputer.setBounds(strikerXComputer,strikerYComputer,strikerWidth,strikerHeight);
		panMiddle.add(btnStrikerComputer);

		add(panMiddle);
		
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				if(ke.getKeyCode()==KeyEvent.VK_ESCAPE) {
					t1.stop();
					dispose();
				}
				else if(ke.getKeyCode()==KeyEvent.VK_RIGHT) {
					new Thread() {
						public void run() {
							if(strikerX>boardWidth-strikerWidth)
								return;
							for(int i=1;i<=50;i++) {
								strikerX++;
								if(strikerX>=boardWidth-strikerWidth)
									return;
								
								btnStriker.setLocation(strikerX, strikerY);
								revalidate();
							}							
						}
					}.start();
				}
				else if(ke.getKeyCode()==KeyEvent.VK_LEFT) {
					new Thread() {
						public void run() {
							if(strikerX<=0)
								return;
							for(int i=1;i<=50;i++) {
								strikerX--;
								if(strikerX<=0)
									return;
								btnStriker.setLocation(strikerX, strikerY);
								revalidate();
							}
						}
					}.start();
				}
				else if(ke.getKeyCode()==KeyEvent.VK_UP) {
					t1.resume();
				}
			}
		});
		getContentPane().setBackground(Color.DARK_GRAY);
		setUndecorated(true);
		setFocusable(true);
		setSize(boardWidth+200,boardHeight);
		setLocationRelativeTo(null);
		setVisible(true);
		move();
	}
	public static void main(String[] args) {
		new Board();
	}
	void move() {
		t1=new Thread(new Runnable() {
			public void run() {
				int coinX=0,coinY=0,signX=1,signY=1;
				while(true) {	
					btnCoin.setLocation(coinX, coinY);
					coinX+=signX;
					coinY+=signY;
					if(coinX<=0 || coinX>=boardWidth-coinWidth)
						signX=signX*-1;
					int x1,x2,y1,y2,x3,y3;
					x1=coinX+coinWidth/2;
					y1=coinY+coinHeight/2;
					x2=strikerX+strikerWidth/2;
					y2=strikerY+strikerHeight/2;
					x3=strikerXComputer+strikerWidth/2;
					y3=strikerYComputer+strikerHeight/2;
					if(signY==-1 && coinY==boardHeight/4) {
						prediction(coinX,coinY,signX);
						moveStrikerComputer();
					}
					if(signY==1 && Math.abs(x1-x2)<=coinWidth-10 && Math.abs(y1-y2)<=coinHeight-10) {//if Player 1 hits the coin
						int p1=x1-x2;
						if(Math.abs(p1)<=10) {
							signX=0;
							signY=-1;
						}
						else if(p1>10 && p1 <=25) {
							signX=1;
							signY=-1;	
						}
						else if(p1>25 && p1 <=40) {
							signX=2;
							signY=-1;
						}
						else if(p1>=-25 && p1 <-10) {
							signX=-1;
							signY=-1;	
						}
						else if(p1>=-40 && p1 <-25) {
							signX=-2;
							signY=-1;
						}						
					}		
					if(signY==-1 && Math.abs(x1-x3)<=coinWidth-10 && Math.abs(y1-y3)<=coinHeight-10) {//if computer hits the coin
						int p1=x1-x3;
						if(Math.abs(p1)<=10) {
							signX=0;
							signY=1;
						}
						else if(p1>10 && p1 <=25) {
							signX=1;
							signY=1;	
						}
						else if(p1>25 && p1 <=40) {
							signX=2;
							signY=1;
						}
						else if(p1>=-25 && p1 <-10) {
							signX=-1;
							signY=1;	
						}
						else if(p1>=-40 && p1 <-25) {
							signX=-2;
							signY=1;
						}
					}
					else if(coinY<=0 || coinY>=boardHeight-coinHeight) {//Goal
						boolean flag=false;
						if(coinX+coinWidth/2<100 || coinX+coinWidth/2 >boardWidth-100)
							signY=signY*-1;
						else {
							if(coinY==0) {//Goal on Computer
								scorePlayer++;
								coinX=strikerXComputer;
								coinY=strikerYComputer+coinHeight;
							}
							if(coinY==boardHeight-coinHeight) {//Goal on user
								flag=true;
								scoreComputer++;
								coinX=strikerX;
								coinY=strikerY-coinHeight;
							}
							lblPlayer.setText("Player Score  : "+scorePlayer);
							lblComputer.setText("Computer Score  : "+scoreComputer);
							try {
								Thread.sleep(3000);
							}
							catch(InterruptedException e) {
								e.printStackTrace();
							}
							if(flag==true) {//goal on User
								btnCoin.setLocation(coinX, coinY);
								signX=0;
								t1.suspend();
							}
							signY=signY*-1;
						}
					}
					try {
						Thread.sleep(speed);
					}
					catch(InterruptedException e) {
						e.printStackTrace();
					}
				}	
			}
		});
		t1.start();
	}
	void prediction(int coinX,int coinY,int signX) {
		predictedX=0;
		predictedX=coinX+boardHeight/4*signX;
		if(predictedX<0)
			predictedX*=-1;
		else if(predictedX>boardWidth-coinWidth) {
			predictedX-=(boardWidth-coinWidth);
		}
	}
	void moveStrikerComputer() {
		new Thread() {
			public void run() {
				if(Math.abs(strikerXComputer-predictedX)>=300)
					return;
				if(strikerXComputer>predictedX) {
					while(strikerXComputer>predictedX) {
						strikerXComputer--;
						btnStrikerComputer.setLocation(strikerXComputer,strikerYComputer);
						try {
							Thread.sleep(1);
						}
						catch(InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				else if(strikerXComputer<predictedX){
					while(strikerXComputer<predictedX) {
						strikerXComputer++;
						btnStrikerComputer.setLocation(strikerXComputer,strikerYComputer);
						try {
							Thread.sleep(1);
						}
						catch(InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}.start();
	}
}
class MyJPanel extends JPanel{
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		ImageIcon ii=new ImageIcon("images/board.png");
		g.drawImage(ii.getImage(),0,0,600,700,null);
	}
}
