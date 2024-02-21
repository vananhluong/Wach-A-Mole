package simplegame;

import java.awt.*;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;


public class WhacAMole {
  int boardWidth = 600;
  int boardHeight = 650;

  JFrame frame = new JFrame("Mario: WachAMole");
  JLabel textLabel = new JLabel();
  JPanel textPanel = new JPanel();
  JPanel boardPanel = new JPanel();
  JButton[] board = new JButton[9];
  ImageIcon moleIcon;
  ImageIcon plantIcon;

  JButton currentMoleTile;
  JButton currentPlantTile;
  JButton restartButton; // restart BUTTON

  Random random = new Random();
  Timer setMoleTimer;
  Timer setPlantTimer;
  int score;
  TreeMap<Integer, String> highScores = new TreeMap<>(Collections.reverseOrder());

  WhacAMole() {
    //frame.setVisible(true);
    frame.setSize(boardWidth, boardHeight);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());

    textLabel.setFont(new Font("Arial", Font.PLAIN, 50));
    textLabel.setHorizontalAlignment(JLabel.CENTER);
    textLabel.setText("Score: 0");
    textLabel.setOpaque(true);

    textPanel.setLayout(new BorderLayout());
    textPanel.add(textLabel);
    frame.add(textPanel, BorderLayout.NORTH);

    boardPanel.setLayout(new GridLayout(3, 3));
    //boardPanel.setBackground(Color.black);
    frame.add(boardPanel);


    Image plantImage = new ImageIcon(getClass().getResource("/piranha.png")).getImage();
    plantIcon = new ImageIcon(plantImage.getScaledInstance(110, 110, Image.SCALE_SMOOTH));

    Image moleImage = new ImageIcon(getClass().getResource("/monty.png")).getImage();
    moleIcon = new ImageIcon(moleImage.getScaledInstance(110, 110, Image.SCALE_SMOOTH));


    score = 0;

    for (int i = 0; i < 9; i++) {
      JButton tile = new JButton();
      board[i] = tile;
      boardPanel.add(tile);
      tile.setFocusable(false);
      //tile.setIcon(moleIcon);

      tile.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          JButton tile = (JButton) e.getSource();

          if (tile == currentMoleTile) {
            score += 10;
            textLabel.setText("Score: " + Integer.toString(score));
          } else if (tile == currentPlantTile) {
            textLabel.setText("GAME OVER!!! " + Integer.toString(score));
            setMoleTimer.stop();
            setPlantTimer.stop();
            for (int i = 0; i < 9; i++) {
              board[i].setEnabled(false);
            }
            restartButton.setVisible(true);// the button only is shown after game over
            displayHighScore();
          }
        }
      });
    }

    //when the game is over; you can restart
    restartButton = new JButton("Restart");
    restartButton.setVisible(false);
    restartButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        restartButton.setVisible(false); //after clicking game over, the button vanish
        score = 0;
        textLabel.setText("Score: " + Integer.toString(score));
        for (int i = 0; i < 9; i++) {
          board[i].setEnabled(true);
        }
        setMoleTimer.restart();
        setPlantTimer.restart();
      }
    });
    textPanel.add(restartButton, BorderLayout.SOUTH);


    //Set Timer for Plant and Mole
    setMoleTimer = new Timer(1000, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (currentMoleTile != null) {
          currentMoleTile.setIcon(null);
          currentMoleTile = null;
        }

        //randomly select another tile
        int num = random.nextInt(9);
        JButton tile = board[num];

        //if tile is occupied by plant skip tile for this turn
        if (currentPlantTile == tile) return;

        // set tile to mole
        currentMoleTile = tile;
        currentMoleTile.setIcon(moleIcon);
      }
    });

    setPlantTimer = new Timer(1200, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (currentPlantTile != null) {
          currentPlantTile.setIcon(null);
          currentPlantTile = null;
        }
        int num = random.nextInt(9);
        JButton tile = board[num];

        if (currentMoleTile == tile) return;

        currentPlantTile = tile;
        currentPlantTile.setIcon(plantIcon);
      }
    });

    setMoleTimer.start();
    setPlantTimer.start();
    frame.validate();
    frame.setVisible(true);


  }

  private void displayHighScore() {
    if (score > 0) {
      String playerName = JOptionPane.showInputDialog(frame, "Congratulations! Enter your name:");
      highScores.put(score, playerName != null && !playerName.trim().isEmpty() //if click "cancel" means NULL or "ok" without typing
                            ? playerName
                            : "Anonymous");
      while (highScores.size() > 5) {
        highScores.pollLastEntry();
      }
      showMe();

    } else {
      JOptionPane.showMessageDialog(frame, "GAME OVER! Your score: " + score);
    }
  }

  private void showMe(){
    StringBuilder sb = new StringBuilder("<html><body>");
    sb.append("<h1>Top 5 High Scores</h1>");
    sb.append("<ol>");
    int count = 0;
    for (Map.Entry<Integer, String> entry : highScores.entrySet()) {
      if (count >= 5) break;
      sb.append("<li>").append(entry.getValue()).append(": ").append(entry.getKey()).append("</li>");
      count++;
    }
    sb.append("</ol>");
    sb.append("<html><body>");
    JOptionPane.showMessageDialog(frame, sb.toString());

  }
}