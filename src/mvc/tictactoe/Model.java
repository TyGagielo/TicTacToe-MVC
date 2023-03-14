package mvc.tictactoe;

import com.mrjaffesclass.apcs.messenger.*;

/**
 * The model represents the data that the app uses.
 * @author Roger Jaffe
 * @version 1.0
 */
public class Model implements MessageHandler {

  // Messaging system for the MVC
  private final Messenger mvcMessaging;

  // Model's data variables
  private boolean whoseMove;
  private boolean gameOver;
  private String[][] board;

  /**
   * Model constructor: Create the data representation of the program
   * @param messages Messaging class instantiated by the Controller for 
   *   local messages between Model, View, and controller
   */
  public Model(Messenger messages) {
    mvcMessaging = messages;
    this.board = new String[3][3];
  }
  
  /**
   * Initialize the model here and subscribe to any required messages
   */
  public void init() {
    mvcMessaging.subscribe("view:changeButton", this);
    this.newGame();
    this.mvcMessaging.subscribe("playerMove", this);
    this.mvcMessaging.subscribe("newGame", this);
  }
  
    /**
   * Reset the state for a new game
   */
  private void newGame() {
    for(int row=0; row<this.board.length; row++) {
      for (int col=0; col<this.board[0].length; col++) {
        this.board[row][col] = "";
      }
    }
    this.whoseMove = false;
    this.gameOver = false;
  }

  
  @Override
  public void messageHandler(String messageName, Object messagePayload) {
      // Display the message to the console for debugging
    if (messagePayload != null) {
      System.out.println("MSG: received by model: "+messageName+" | "+messagePayload.toString());
    } else {
      System.out.println("MSG: received by model: "+messageName+" | No data sent");
    }
    
    // playerMove message handler
    if (messageName.equals("playerMove")) {
      // Get the position string and convert to row and col
      String position = (String)messagePayload;
      Integer row = Integer.valueOf(position.substring(0,1));
      Integer col = Integer.valueOf(position.substring(1,2));
      // If square is blank...
      if (this.board[row][col].equals("")) {
        // ... then set X or O depending on whose move it is
        if (this.whoseMove) {
          this.board[row][col] = "X";
        } else {
          this.board[row][col] = "O";
        }
        whoseMove = !this.whoseMove;
        // Send the boardChange message along with the new board 
        this.mvcMessaging.notify("boardChange", this.board);
      }
      
    // newGame message handler
    } else if (messageName.equals("newGame")) {
      // Reset the app state
      this.newGame();
      // Send the boardChange message along with the new board 
      this.mvcMessaging.notify("boardChange", this.board);
    }
  }
  
  
  public void findWinner(){
      //initalize counting variables
      int co = 0;
      int ro = 0;
      int cx = 0;
      int rx = 0;
      
      //check columns
      for (int row = 0; row < this.board.length; row++){
          for (int col = 0; col < this.board[0].length; col++){
              if (this.board[row][col]!= null){
                if (this.board[row][col].equals("X")){
                    cx++;
                }
                if (this.board[row][col].equals("O")){
                    co++;
                }              
              }
          }
      }
      //check Rows
      for (int col = 0; col < this.board[0].length; col++){
          for (int row = 0; row < this.board.length; row++){
            if (this.board[row][col] != null){
              if (this.board[row][col].equals("X")){
                  rx++;
              }
              if (this.board[row][col].equals("O")){
                  ro++;
              }
            }
          }
      }
      
      
      //check for winner
      if (co == this.board.length || ro == this.board.length){
          System.out.println("O wins");
          this.mvcMessaging.notify("gameOver", this.board);
      }
      if (cx == this.board.length || rx == this.board.length){
          System.out.println("X wins");
          this.mvcMessaging.notify("gameOver", this.board);
      }
  }
      
 }
