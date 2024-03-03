import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class Ex_15_2101355196 extends JFrame {
    final int windowWidth = 800;
    final int windowHeight = 500;
	ScoreHistory scoreHistory;

    public static void main(String[] args){
        new Ex_15_2101355196();
    }

    public Ex_15_2101355196() {
        Dimension dimOfScreen =
               Toolkit.getDefaultToolkit().getScreenSize();

        setBounds(dimOfScreen.width/2 - windowWidth/2,
                  dimOfScreen.height/2 - windowHeight/2,
                  windowWidth, windowHeight);
        setResizable(false);
        setTitle("Software Development II");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

		scoreHistory = new ScoreHistory();		// score履歴用classの初期化
		scoreHistory.readFromFile();			// score履歴をファイルから読む
        MyStartJPanel panel= new MyStartJPanel();  // ゲームスタートパネルを表示する
		
		Container c = getContentPane();
        c.add(panel);
        setVisible(true);
    }
	
	// Panelを切り替える処理
	public void changePanel(JPanel panel) {
		// ContentPaneにはめ込まれたパネルを削除
		Container c = getContentPane();
		c.removeAll();
		c.add(panel);
		setVisible(true);
		panel.requestFocus();	// Add for KeyListener
	}

	//
	// ScoreとScore履歴を保存するClass
	//
	public class ScoreHistory {
		int currentScore;
		int savedScore[];
		int savedScoreNum = 3;
		String filename = "score_history.txt";
		
		public ScoreHistory() {
			currentScore = -1;
			savedScore = new int[savedScoreNum];
			for (int i = 0; i < savedScoreNum; i++)
				savedScore[i] = -1;
		}
		
		public int addToCurrentScore(int score) {
			currentScore += score;
			return (currentScore);
		}
		
		public int getCurrentScore() {
			return currentScore;
		}
		
		public void setCurrentScore(int score) {
			currentScore = score;
			//System.out.println("setCurrentScore: " + currentScore);			// for debugging
		}
		
		// scoreの履歴の配列を返却する。
		// 戻り値：配列の数
		public int getScoreHistory(int num, int[] score) {
			if (num > savedScoreNum)
				num = savedScoreNum;
			for (int i = 0; i < num; i++) {
				score[i] = savedScore[i];
			}
			return (num);
		}
		
		// scoreをscore履歴に保存する。履歴にはscoreが大きい順に保存される
		public void putCurrentScoreToHistory(int score) {
			int tempSavedScore;
			int prevTempSavedScore;
			
			for (int i = 0; i < savedScoreNum; i++) {
				if (savedScore[i] < 0) {
					savedScore[i] = score;
					break;
				} else if (savedScore[i] < score) {
					if (i >= (savedScoreNum - 1)) {
						savedScore[i] = score;
						break;
					} else {
						tempSavedScore = savedScore[i];
						savedScore[i] = score;
						for (int j = i+1; j < savedScoreNum; j++) {
							prevTempSavedScore = tempSavedScore;
							tempSavedScore = savedScore[j];
							savedScore[j] = prevTempSavedScore;
						} 
					}
					break;
				}	
			}
		}
		
		// score の履歴をファイルに書く
		public void writeToFile() {
			try {
				FileWriter fw = new FileWriter(filename);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter pw = new PrintWriter(bw);

				for (int i = 0; i < savedScoreNum; i++) {
					//System.out.println(savedScore[i]);		// for debugging
					pw.println(savedScore[i]);
				}
				pw.close();
			} catch (Exception e) {
				// do nothing for now
				System.out.println(e);
			}
		}
		
		// score の履歴をファイルから読む
		public void readFromFile() {
			int 	lineNum = 0;
			String 	line;
			
			try {
				FileReader fr = new FileReader(filename);
				BufferedReader br = new BufferedReader(fr);

				while ((line = br.readLine()) != null && (lineNum < savedScoreNum)) {
					savedScore[lineNum] = Integer.parseInt(line);
					//System.out.println(savedScore[lineNum]);	// for debugging
					lineNum ++;
				}
				br.close();
			} catch (Exception e) {
				// 履歴ファイルがないと java.io.FileNotFoundException が発生するが何もしない
				System.out.println(e);
			}
		}
	}

	//
	// スタートボタンと終了ボタンがあるPanelのJPanel
	//
	public class MyStartJPanel extends JPanel implements ActionListener, KeyListener {
		JLabel 	labelTitle;
		JButton	startButton;
		JButton	quitButton;
		JLabel	currentScoreLabel;
		JLabel	firstScoreLabel;
		JLabel	secondScoreLabel;
		JLabel 	thirdScoreLabel;
	
		public MyStartJPanel() {
			int currentScore;
			String currentScoreString;
			int[] score;
			String[] scoreString;
		
			setFocusable(true);			// Add for KeyListener
			addKeyListener(this);		// Add for KeyListener

			labelTitle = new JLabel("Shooting Game Project #15");
			startButton = new JButton("スタート");
			quitButton = new JButton("終了");
			
			startButton.addActionListener(this);
			quitButton.addActionListener(this);
			
			setLayout(null);
			labelTitle.setHorizontalAlignment(JLabel.CENTER);
			labelTitle.setBounds(300, 80, 200, 40);
			startButton.setBounds(340, 160, 120, 40);
			quitButton.setBounds(340, 210, 120, 40);
			
			add(labelTitle);
			add(startButton);
			add(quitButton);
			
			// score表示:最終スコア
			currentScore = scoreHistory.getCurrentScore();
			//System.out.println(currentScore);			// for debugging
			if (currentScore >= 0) {
				currentScoreString = String.format("スコア（最後のプレイ）: %d", currentScore);
				// System.out.println(currentScoreString);			// for debugging
				currentScoreLabel = new JLabel(currentScoreString);
				currentScoreLabel.setBounds(320, 270, 200, 40);
				add(currentScoreLabel);
			}
			
			// score表示：スコア履歴
			score = new int[3];
			scoreString = new String[3];
			scoreHistory.getScoreHistory(3, score);
			if (score[0] >= 0) {
				scoreString[0] = String.format("スコア（１位）: %d", score[0]);
				// System.out.println(scoreString[0]);			// for debugging
				firstScoreLabel = new JLabel(scoreString[0]);
				firstScoreLabel.setBounds(320, 310, 200, 40);
				add(firstScoreLabel);
				
				if (score[1] >= 0) {
					scoreString[1] = String.format("スコア（２位）: %d", score[1]);
					// System.out.println(scoreString[1]);			// for debugging
					secondScoreLabel = new JLabel(scoreString[1]);
					secondScoreLabel.setBounds(320, 340, 200, 40);
					add(secondScoreLabel);
					
					if (score[2] >= 0) {
						scoreString[2] = String.format("スコア（３位）: %d", score[2]);
						// System.out.println(scoreString[2]);			// for debugging
						thirdScoreLabel = new JLabel(scoreString[2]);
						thirdScoreLabel.setBounds(320, 370, 200, 40);
						add(thirdScoreLabel);
					}
				}
			}
		}
		
		public void paintComponent(Graphics g) {
			// System.out.println("MyStartJPanel paintComponent"); // for debugging
			super.paintComponent(g);
			g.setColor(Color.WHITE);
			g.fillRect(0,0,windowWidth,windowHeight);			
		}
		
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(startButton)) {
				// System.out.println("start button"); // for debugging
				MyJPanel panel= new MyJPanel();
				changePanel(panel);
				
			} else if (e.getSource().equals(quitButton)) {
				// System.out.println("quit button");	// for debugging
				scoreHistory.writeToFile();
				System.exit(0);
			}
		}
		
		// KeyListener
		public void keyTyped(KeyEvent e) {
		}
		// KeyListener
		public void keyPressed(KeyEvent e) {
			// System.out.println("keyPressed"); // for debugging
		}
		// KeyListener
		public void keyReleased(KeyEvent e) {
		}
	}

	//
	// ゲーム画面を表示するJPanel
	//
    public class MyJPanel extends JPanel implements
		ActionListener, MouseListener, MouseMotionListener, KeyListener {
        /* 全体の設定に関する変数 */
        Dimension dimOfPanel;
        Timer timer;
        ImageIcon iconMe, iconEnemy, iconHighScoreEnemy;
        Image imgMe, imgEnemy, imgHighScoreEnemy;

        /* 自機に関する変数 */
        int myHeight, myWidth;
        int myX, myY, tempMyX;
        int gap = 100;
		int numOfMyMissile = 3;		// 自機のミサイルの連続発射可能数
        int[] myMissileX = new int[numOfMyMissile];
		int[] myMissileY = new int[numOfMyMissile];
        boolean [] isMyMissileActive = new boolean[numOfMyMissile];
		int numOfMyAttacker = 3;	// 2023/01/22 自機の残り数

        /* 敵機に関する変数 */
        int numOfEnemy = 12;
        int numOfAlive = numOfEnemy;
        int enemyWidth, enemyHeight;
        int[] enemyX = new int[numOfEnemy];
        int[] enemyY = new int[numOfEnemy];
        int[] enemyMove = new int[numOfEnemy];
        int[] enemyMissileX = new int[numOfEnemy];
        int[] enemyMissileY = new int[numOfEnemy];
        int[] enemyMissileSpeed = new int[numOfEnemy];
        boolean[] isEnemyAlive = new boolean[numOfEnemy];
        boolean[] isEnemyMissileActive = new boolean[numOfEnemy];
		
		// 2023/01/28 高得点敵機 high score
		int numOfHighScoreEnemy = 0;
		int scoreOfHighScoreEnemy = 100;
		int highScoreEnemyWidth;
		int highScoreEnemyHeight;
		int highScoreEnemyX;
		int highScoreEnemyY;
		int highScoreEnemyMove;
		int highScoreEnemyMissileX;
		int highScoreEnemyMissileY;
		int highScoreEnemyMissileSpeed;
		boolean isHighScoreEnemyAlive;
		boolean isHighScoreEnemyMissileActive;
		int	highScoreEnemyFrequency = 100;  	// 50 msec x 100 = 5 sec
		int highScoreEnemyTimerCount = highScoreEnemyFrequency;
								
		boolean safeMode;
		boolean myMissileSizeBig;

        /* コンストラクタ（ゲーム開始時の初期化）*/
        public MyJPanel() {
			// System.out.println("MyPanel constructor"); // for debugging
            // 全体の設定
            setBackground(Color.black);
            addMouseListener(this);
            addMouseMotionListener(this);
			setFocusable(true);				// Add for KeyListener
			addKeyListener(this);			// Add for KeyListener
            timer = new Timer(50, this);
            timer.start();

            // 画像の取り込み
            imgMe = getImg("jiki.jpg");
            myWidth = imgMe.getWidth(this);
            myHeight = imgMe.getHeight(this);

            imgEnemy = getImg("teki.jpg");
            enemyWidth = imgEnemy.getWidth(this);
            enemyHeight = imgEnemy.getHeight(this);
			
            imgHighScoreEnemy = getImg("teki.jpg");
            highScoreEnemyWidth = imgHighScoreEnemy.getWidth(this);
            highScoreEnemyHeight = imgHighScoreEnemy.getHeight(this);

            // 自機と敵機の初期化
            initMyPlane();
            initEnemyPlane();
			initHighScoreEnemyPlane();	// 2023/01/28 高得点敵機の初期化
			
			safeMode = false;			// 安全モードの切り替え
			myMissileSizeBig = false; 	// 自機のミサイルサイズの切り替え
			
			scoreHistory.setCurrentScore(0);
        }

        /* パネル上の描画 */
        public void paintComponent(Graphics g) {
			int currentScore;
		
            dimOfPanel = getSize();
            super.paintComponent(g);

            // 各要素の描画
            drawMyPlane(g);       // 自機
            drawMyMissile(g);     // 自機のミサイル
            drawEnemyPlane(g);    // 敵機
            drawEnemyMissile(g);  // 敵機のミサイル
			drawHighScoreEnemyPlane(g); // 2023/01/28 高得点敵機
			drawHighScoreEnemyMissile(g); // 2023/01/28 高得点敵機のミサイル

            // 敵機を全機撃墜した時の終了処理
            if (numOfAlive == 0 && (isHighScoreEnemyAlive == false)) {
				
				//System.exit(0);
				currentScore = (numOfEnemy - numOfAlive) * 10 + numOfHighScoreEnemy*scoreOfHighScoreEnemy;
				scoreHistory.setCurrentScore(currentScore);
				scoreHistory.putCurrentScoreToHistory(currentScore);

				MyStartJPanel panel= new MyStartJPanel();
				changePanel(panel);		// panelをMyStartJPanelへ切り替える
            }
        }

        /* 一定時間ごとの処理（ActionListener に対する処理）*/
        public void actionPerformed(ActionEvent e) {
            repaint();
        }

        /* MouseListener に対する処理 */
        // マウスボタンをクリックする
        public void mouseClicked(MouseEvent e) {
        }

        // マウスボタンを押下する
        public void mousePressed(MouseEvent e) {
			// 発射可能な自機のミサイルがあれば発射する
			for (int i = 0; i < numOfMyMissile; i++) {
				if (!isMyMissileActive[i]) {
					myMissileX[i] = tempMyX + myWidth/2;
					myMissileY[i] = myY;
					isMyMissileActive[i] = true;
					break;
				}
            }
        }

        // マウスボタンを離す
        public void mouseReleased(MouseEvent e) {
        }

        // マウスが領域外へ出る
        public void mouseExited(MouseEvent e) {
        }

        // マウスが領域内に入る
        public void mouseEntered(MouseEvent e) {
        }

        /* MouseMotionListener に対する処理 */
        // マウスを動かす
        public void mouseMoved(MouseEvent e) {
            myX = e.getX();
        }

        // マウスをドラッグする
        public void mouseDragged(MouseEvent e) {
            myX = e.getX();
        }

        /* 画像ファイルから Image クラスへの変換 */
        public Image getImg(String filename) {
            ImageIcon icon = new ImageIcon(filename);
            Image img = icon.getImage();

            return img;
        }

        /* 自機の初期化 */
        public void initMyPlane() {
            myX = windowWidth / 2;
            myY = windowHeight - 100;
            tempMyX = windowWidth / 2;
			
			// 連続発射可能なミサイル数だけ初期化する
			for (int i = 0; i < numOfMyMissile; i++)
				isMyMissileActive[i] = false;
        }

        /* 敵機の初期化 */
        public void initEnemyPlane() {
            for (int i=0; i<7; i++) {
                enemyX[i] = 70*i;
                enemyY[i] = 50;
            }

            for (int i=7; i<numOfEnemy; i++) {
                enemyX[i] = 70*(i-6);
                enemyY[i] = 100;
            }

            for (int i=0; i<numOfEnemy; i++) {
                isEnemyAlive[i] = true;
                enemyMove[i] = 1;
            }

            for (int i=0; i<numOfEnemy; i++) {
                isEnemyMissileActive[i] = true;
                enemyMissileX[i] = enemyX[i] + enemyWidth/2;
                enemyMissileY[i] = enemyY[i];
                enemyMissileSpeed[i] = 10 + (i%6);
            }
        }

		public void initHighScoreEnemyPlane() {
			highScoreEnemyX = 0;
			highScoreEnemyY = 0;
			highScoreEnemyMove = 1;
			highScoreEnemyMissileX = highScoreEnemyX + highScoreEnemyWidth/2;
			highScoreEnemyMissileY = highScoreEnemyY;
			highScoreEnemyMissileSpeed = 16;
			isHighScoreEnemyAlive = false;
			isHighScoreEnemyMissileActive = false;
			highScoreEnemyTimerCount = highScoreEnemyFrequency;
		}

        /* 自機の描画 */
        public void drawMyPlane(Graphics g) {
            if (Math.abs(tempMyX - myX) < gap) {
                if (myX < 0) {
                    myX = 0;
                } else if (myX+myWidth > dimOfPanel.width) {
                    myX = dimOfPanel.width - myWidth;
                }
                tempMyX = myX;
                g.drawImage(imgMe, tempMyX, myY, this);
            } else {
                g.drawImage(imgMe, tempMyX, myY, this);
            }
			
			// 2023/01/22 自機の残り数をテキストで描画
			String numOfMyAttackerString = Integer.valueOf(numOfMyAttacker).toString();
			g.setColor(Color.WHITE);
			g.drawString(numOfMyAttackerString, tempMyX+myWidth/2-4, myY+40);
        }

        /* 自機のミサイルの描画 */
        public void drawMyMissile(Graphics g) {
			int extra_x;	// 当たり判定調整量x
			int	extra_y;	// 当たり判定調整量y
			
			// 自機のミサイルサイズによって当たり判定を調整する量を計算
			if (myMissileSizeBig == true) {
				extra_x = enemyWidth/4;
				extra_y = enemyHeight/4;
			} else {
				extra_x = 0;
				extra_y = 0;
			}
			
			for (int i = 0; i < numOfMyMissile; i++) {
				if (isMyMissileActive[i]) {
					// ミサイルの配置
					myMissileY[i] -= 15;
					g.setColor(Color.white);
				
					if (myMissileSizeBig == true)
						g.fillRect(myMissileX[i], myMissileY[i], 6, 15);
					else
						g.fillRect(myMissileX[i], myMissileY[i], 2, 5);

					// 自機のミサイルの敵機各機への当たり判定
					for (int j = 0; j < numOfEnemy; j++) {
						if (isEnemyAlive[j]) {	
							if ((myMissileX[i] >= enemyX[j] - extra_x) &&
								(myMissileX[i] <= enemyX[j] + enemyWidth + extra_x) &&
								(myMissileY[i] >= enemyY[j] - extra_y) &&
								(myMissileY[i] <= enemyY[j] + enemyHeight + extra_y)) {
								isEnemyAlive[j] = false;
								isMyMissileActive[i] = false;
								numOfAlive--;
							}
						}
					}

					// 自機のミサイルの高得点敵機への当たり判定
					if (isHighScoreEnemyAlive) {	
						if ((myMissileX[i] >= highScoreEnemyX - extra_x) &&
								(myMissileX[i] <= highScoreEnemyX + highScoreEnemyWidth + extra_x) &&
								(myMissileY[i] >= highScoreEnemyY - extra_y) &&
								(myMissileY[i] <= highScoreEnemyY + highScoreEnemyHeight + extra_y)) {
								
								isMyMissileActive[i] = false;
								numOfHighScoreEnemy++;
								initHighScoreEnemyPlane();
						}
					}
					
					// ミサイルがウィンドウ外に出たときのミサイルの再初期化
					if (myMissileY[i] < 0) isMyMissileActive[i] = false;
				}
			}
        }

        /* 敵機の描画 */
        public void drawEnemyPlane(Graphics g) {
            for (int i=0; i<numOfEnemy; i++) {
                if (isEnemyAlive[i]) {
                    if (enemyX[i] > dimOfPanel.width -
                                                   enemyWidth) {
                        enemyMove[i] = -1;
                    } else if (enemyX[i] < 0) {
                        enemyMove[i] = 1;
                    }
                    enemyX[i] += enemyMove[i]*10;
                    g.drawImage(imgEnemy, enemyX[i],
                                          enemyY[i], this);
                }
            }
        }

		// 高得点敵機の描画
		public void drawHighScoreEnemyPlane(Graphics g) {				
			if (isHighScoreEnemyAlive) {
				if (highScoreEnemyX > dimOfPanel.width - highScoreEnemyWidth) {
					highScoreEnemyMove = -1;
				} else if (highScoreEnemyX < 0) {
					highScoreEnemyMove = 1;
				}
				highScoreEnemyX += highScoreEnemyMove*14;
				g.drawImage(imgHighScoreEnemy, highScoreEnemyX,
                                          highScoreEnemyY, this);
			} else {
				highScoreEnemyTimerCount--;
				if (highScoreEnemyTimerCount <= 0) {
					isHighScoreEnemyAlive = true;
					isHighScoreEnemyMissileActive = true;
				}
			}
		}

        /* 敵機のミサイルの描画 */
        public void drawEnemyMissile(Graphics g) {
			int currentScore;
		
            for (int i=0; i<numOfEnemy; i++) {
                // ミサイルの配置
                if (isEnemyMissileActive[i]) {
                    enemyMissileY[i] += enemyMissileSpeed[i];
                    g.setColor(Color.red);
                    g.fillRect(enemyMissileX[i],
                               enemyMissileY[i], 2, 5);
                }

                // 敵機のミサイルの自機への当たり判定
				if (safeMode == false)
					if ((enemyMissileX[i] >= tempMyX) &&
						(enemyMissileX[i] <= tempMyX+myWidth) &&
						(enemyMissileY[i]+5 >= myY) &&
						(enemyMissileY[i]+5 <= myY+myHeight)) {
						
						//System.exit(0);
						numOfMyAttacker--;	// 2023/01/22 自機の残り数を減らす
						// System.out.println("Missile hit my attacker " + numOfMyAttacker); // for debugging
						// 2023/01/22 自機に敵ミサイルが当たったときの敵ミサイルの再初期化
						if (isEnemyAlive[i]) {
							enemyMissileX[i] = enemyX[i] + enemyWidth/2;
							enemyMissileY[i] = enemyY[i] + enemyHeight;
						} else {
							isEnemyMissileActive[i] = false;
						}
						
						// 自機が０機になったらゲームを終了する
						if (numOfMyAttacker <= 0) {						
							currentScore = (numOfEnemy - numOfAlive) * 10 + numOfHighScoreEnemy*scoreOfHighScoreEnemy;
							scoreHistory.setCurrentScore(currentScore);
							scoreHistory.putCurrentScoreToHistory(currentScore);
						
							MyStartJPanel panel= new MyStartJPanel();
							changePanel(panel);		// panelをMyStartJPanelへ切り替える
						}
					}

                // ミサイルがウィンドウ外に出たときのミサイルの再初期化
                if (enemyMissileY[i] > dimOfPanel.height) {
                    if (isEnemyAlive[i]) {
                        enemyMissileX[i] = enemyX[i] + enemyWidth/2;
                        enemyMissileY[i] = enemyY[i] + enemyHeight;
                    } else {
                        isEnemyMissileActive[i] = false;
                    }
                }
            }
        }
		
		// 高得点敵機のミサイルの描画
		public void drawHighScoreEnemyMissile(Graphics g) {
			int currentScore;
		
			// System.out.println("isHighScoreEnemyMissileActive = " + isHighScoreEnemyMissileActive + "," + highScoreEnemyMissileX + ","+highScoreEnemyMissileY);  // for debugging
			if (isHighScoreEnemyMissileActive) {
				highScoreEnemyMissileY += highScoreEnemyMissileSpeed;
				g.setColor(Color.red);
				g.fillRect(highScoreEnemyMissileX,
							highScoreEnemyMissileY, 2, 5);
			}

			// 高得点敵機のミサイルの自機への当たり判定
			if (safeMode == false) {
				if ((highScoreEnemyMissileX >= tempMyX) &&
					(highScoreEnemyMissileX <= tempMyX+myWidth) &&
					(highScoreEnemyMissileY+5 >= myY) &&
					(highScoreEnemyMissileY+5 <= myY+myHeight)) {
						
					//System.exit(0);
					numOfMyAttacker--;	// 2023/01/22 自機の残り数を減らす
					// System.out.println("Missile hit my attacker " + numOfMyAttacker); // for debugging
					// 2023/01/22 自機に敵ミサイルが当たったときの敵ミサイルの再初期化
					highScoreEnemyMissileX = highScoreEnemyX + highScoreEnemyWidth/2;
					highScoreEnemyMissileY = highScoreEnemyY + highScoreEnemyHeight;
					if (isHighScoreEnemyAlive == false) {
							isHighScoreEnemyMissileActive = false;
					}
						
					// 自機が０機になったらゲームを終了する
					if (numOfMyAttacker <= 0) {						
						currentScore = (numOfEnemy - numOfAlive) * 10 + numOfHighScoreEnemy*scoreOfHighScoreEnemy;
						scoreHistory.setCurrentScore(currentScore);
						scoreHistory.putCurrentScoreToHistory(currentScore);
						
						MyStartJPanel panel= new MyStartJPanel();
						changePanel(panel);		// panelをMyStartJPanelへ切り替える
					}
				}
			}
			// ミサイルがウィンドウ外に出たときのミサイルの再初期化				
			if (highScoreEnemyMissileY > dimOfPanel.height) {
				highScoreEnemyMissileX = highScoreEnemyX + highScoreEnemyWidth/2;
				highScoreEnemyMissileY = highScoreEnemyY + highScoreEnemyHeight;
				if (isHighScoreEnemyAlive == false) {
					isHighScoreEnemyMissileActive = false;
				}
			}
		}
		
		// KeyListener
		public void keyTyped(KeyEvent e) {
		}
		
		// KeyListener
		public void keyPressed(KeyEvent e) {
			//System.out.println("keyPressed"); // for debugging
			switch ( e.getKeyCode() ) {
				case KeyEvent.VK_SHIFT:   	// 安全モードとの切り替え
					if (safeMode == false)
						safeMode = true;
					else
						safeMode = false;
					break;
				case KeyEvent.VK_SPACE:  	// 自機のミサイルサイズの切り替え
					if (myMissileSizeBig == false)
						myMissileSizeBig = true;
					else
						myMissileSizeBig = false;
					break;
			}
		}
		
		// KeyListener
		public void keyReleased(KeyEvent e) {
		}
    }
}