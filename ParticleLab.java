
import java.awt.*;
import java.util.*;

public class ParticleLab{
  static final int NBR_ROWS  = 160;  //180
  static final int NBR_COLS  = 180;  //180
  static final int CELL_SIZE = 800;  //800
  
  static final String FILE_NAME     = "ParticleLabFileTesting.txt";         //This is the name of the input file.
  static final String NEW_FILE_NAME = "ParticleLabFileTesting.txt";  //This is the name of the file you are saving.
  
  //add constants for particle types here
  public static final int EMPTY     = 0;
  public static final int METAL     = 1;
  public static final int SAVEFILE  = 2;
  public static final int SAND      = 3;
  public static final int AIR       = 4;
  public static final int GRAVITY   = 5;
  public static final int OIL       = 6;
  public static final int GENERATOR = 7;
  public static final int TOGGLE_GENERATOR = 8;
  public static final int DESTRUCTOR = 9;
  public static final int VAPOR = 10;
  
  public static boolean REVERSE_GRAVITY = false;
  public static boolean GENERATOR_ON = true;
  
  //do not add any more global fields
  private int row = 0;
  private int col = 0;
  
  private int[][] particleGrid;
  private LabDisplay display;
  
  //---------------------------------------------------------------------------------------------------------
  
  public static void main(String[] args){
    System.out.println("================= Starting Program =================");
    System.out.println("ROWS: " + NBR_ROWS + "\nCOLS: " + NBR_COLS + "\nCELL_SIZE: " + CELL_SIZE + "\n");
    
    //testing array with buffer zone
    ParticleLab lab = new ParticleLab(NBR_ROWS, NBR_COLS);  //creates the object
    lab.run();
  }
  
  //SandLab constructor - ran when the above lab object is created
  public ParticleLab(int numRows, int numCols){
    String[] names = new String[10];
    
    names[EMPTY]    = "Empty";
    names[METAL]    = "Metal";
    names[SAVEFILE] = "SaveFile";
    names[SAND]     = "Sand";
    names[AIR] = "Air Bubbles";
    names[GRAVITY] = "Reverse Gravity";
    names[OIL] = "Oil";
    names[GENERATOR] = "Generator";
    names[TOGGLE_GENERATOR] = "Toggle Generator";
    names[DESTRUCTOR] = "Destructor";
    
    display      = new LabDisplay("SandLab", numRows, numCols, names);  //uses the LabDisplay.class file
    particleGrid = new int[numRows][numCols];
    
    
    if (FILE_NAME != "") {
      System.out.println("Attempting to load: " + FILE_NAME);
      particleGrid = ParticleLabFiles.readFile(FILE_NAME);
    }
  }
  
  //called when the user clicks on a location using the given tool
  private void locationClicked(int row, int col, int tool){
    
    int particle = particleGrid[row][col];
    
    //the particles will not over ride each other, unless the empty button is used to delete them
    if (particleGrid[row][col] == EMPTY || tool == EMPTY){
      particleGrid[row][col] = tool; 
    }
    
    if (tool == SAVEFILE) {
       //Without these two lines, save file action kills the space it touches.
       tool = EMPTY;
       particleGrid[row][col] = particle; //should not need to update array here BUT original did not have this line 
                                          //and somehow was updating the array anyway...
      ParticleLabFiles.writeFile(particleGrid, NEW_FILE_NAME);
    }
    
    if (tool == GRAVITY) {
      
      particleGrid[row][col] =  particle;
      REVERSE_GRAVITY = !REVERSE_GRAVITY;
    }
    if(tool == TOGGLE_GENERATOR){
       particleGrid[row][col] = particle;
       GENERATOR_ON = !GENERATOR_ON;
    }
    
    
  }
  
  //Examines each element of the 2D particleGrid and paints a color onto the display
  public void updateDisplay(){
    
    //Color purple = new Color (192, 0, 255);
    
    //Color seaBlue = new Color (51, 153, 255);
    Color seaBlue = new Color(0, 155, 219);
    Color metal = new Color (76, 76, 76);
    Color sand = new Color (223, 196, 123);
    Color oil = new Color(255,204, 0,180);
    Color vapor = new Color(51,204,255,110);
    Color air = new Color(205,232,255,150);
     
    for (int r = 0; r < particleGrid.length; r++){
      for (int c = 0; c < particleGrid[0].length; c++){
        
        if (particleGrid[r][c] == EMPTY){
          display.setColor(r,c, seaBlue);
        }
        else if (particleGrid[r][c] == METAL){
          display.setColor(r,c,metal);
        }
        else if (particleGrid[r][c] == SAND){
          display.setColor(r,c,sand);
        }
        else if (particleGrid[r][c] == AIR){
          display.setColor(r,c,air);
        }
        else if(particleGrid[r][c] == OIL){
           display.setColor(r,c,Color.black);
        }
        else if(particleGrid[r][c] == GENERATOR){
           display.setColor(r,c,Color.pink);
        }
        else if(particleGrid[r][c] == DESTRUCTOR){
           display.setColor(r,c,Color.cyan);
        }
        else if(particleGrid[r][c] == VAPOR){
           display.setColor(r,c,vapor);
        }
      }
    }
    
    
    
  }
  
  //called repeatedly.
  //causes one random particle to maybe do something.
  public void step(){

    //gets a random particle location in the grid
    int r = getRandomNumber(0, NBR_ROWS);
    int c = getRandomNumber(0, NBR_COLS);
    
    //find sand particles in the grid
    if(particleGrid[r][c] == SAND){
      moveSandParticles(r, c);
    }
    
    //find air particles
    if(particleGrid[r][c] == AIR){
      moveAirParticles(r, c);
    }
    
    //find oil particles
    if(particleGrid[r][c] == OIL){
      moveOilParticles(r, c);
    }
    
    if(particleGrid[r][c] == GENERATOR){
     generator(r, c); 
    }
    
    if(particleGrid[r][c] == DESTRUCTOR){
     destructor(r, c); 
    }
    
    if(particleGrid[r][c] == VAPOR){
     moveVapor(r, c); 
    }
    
  }
  
  
  //TODO: bug occurs where small bits of the destructor sometimes disappear
  //unsure if it is destructor code or vapor code causing this
  //update I think it is the vapor code
  public void destructor(int r, int c){
    int above = r - 2;
    int below = r +2;
    int up = r-1;
    int down = r+1;
    int left = c-1;
    int right = c+1;
    
    if(down > NBR_ROWS){down = 0; }
    if(up < 0){ up = NBR_ROWS-1;}
    if(above < 0){ above = NBR_ROWS-1;}
    if(below > NBR_ROWS){below = 0; }
        //enables wrapping horizontally 
    if(left < 0){left = NBR_COLS-1;}
    if(right >= NBR_COLS){right = 0;}
    
    //if reverse gravity button was clicked swap up and down
    if (REVERSE_GRAVITY){
      above = r+2;
      below = r - 3;
      up = r+1;
      down = r-1;
      if(up > NBR_ROWS-1){up = 0; }
      if(above > NBR_ROWS-1){above = 0; }
      if(down < 0){ down = NBR_ROWS-1;}
      if(below < 0){ below = NBR_ROWS-1;}
    }
    
    //destroy particles above 
    if(particleGrid[up][c] != EMPTY && particleGrid[up][c] != DESTRUCTOR){
      if(particleGrid[up][c] != METAL){
      particleGrid[up][c] = EMPTY;
      if( particleGrid[above][c] !=METAL){
        particleGrid[above][c] = VAPOR;
      }
      }
    }
    
    //destroy particles below  
    //crashed here during high activity involving gravity
    if(particleGrid[down][c] != EMPTY && particleGrid[down][c] != DESTRUCTOR){
      if(particleGrid[down][c] != METAL){
      particleGrid[down][c] = EMPTY;
      if(particleGrid[below][c] !=METAL){
        particleGrid[below][c] = VAPOR;
      }
      }
    }
    
    if(particleGrid[r][left] != EMPTY && particleGrid[r][left] != DESTRUCTOR){
      if(particleGrid[r][left] != METAL){
        particleGrid[r][left] = EMPTY;
      }
    }
    
    if(particleGrid[r][right] != EMPTY&& particleGrid[r][right] != DESTRUCTOR){
      if(particleGrid[r][right] != METAL){
        particleGrid[r][right] = EMPTY;
      }
    }
    
    particleGrid[r][c] = DESTRUCTOR;
    
  }
  
  
  public void generator(int r, int c){
    int pushUp = r-2;
    int pushDown = r+2;
   
    int up = r-1;
    int down = r+1;
    int left = c-1;
    int right = c+1;
    
    if(down > NBR_ROWS){down = 0; }
    if(up < 0){ up = NBR_ROWS-1;}
    if(pushUp <0){pushUp = NBR_ROWS -1;}
    if(pushDown > NBR_ROWS){pushDown = 0; }
        //enables wrapping horizontally 
    if(left < 0){left = NBR_COLS-1;}
    if(right >= NBR_COLS){right = 0;}
    
    //if reverse gravity button was clicked swap up and down
    if (REVERSE_GRAVITY){
      pushUp = r+2;
      pushDown = r-2;
      
      up = r+1;
      down = r-1;

      if(up > NBR_ROWS-1){up = 0; }
      if(down < 0){ down = NBR_ROWS-1;}
      if(pushUp > NBR_ROWS-1){pushUp = 0;}
      if(pushDown < 0){ pushDown = NBR_ROWS-1;}
    }
    
    //if the generator is turned off, this code will not execute
    if(GENERATOR_ON){
      
    if(particleGrid[up][c] != EMPTY){
      if(particleGrid[up][c] != METAL && particleGrid[up][c] != GENERATOR){
      
      int particle = particleGrid[up][c];
      
      particleGrid[pushUp][c] = particle;
      particleGrid[up][c] = particle;
      
      }
    }
    
    if(particleGrid[down][c] != EMPTY){
      if(particleGrid[up][c] != METAL && particleGrid[down][c] != GENERATOR){
        
      int particle = particleGrid[down][c];
      
      particleGrid[pushDown][c] = particle;
      particleGrid[down][c] = particle;
     
      }
    }
    
    }//GENERATOR ON/OFF Check
    
    particleGrid[r][c] = GENERATOR;
    
  }
  
  public void moveOilParticles(int r, int c){
    
        
    int up = r-1;
    int down = r+1;
    int left = c-1;
    int right = c+1;
    
    if(down > NBR_ROWS){down = 0; }
    if(up < 0){ up = NBR_ROWS-1;}
    
    //if reverse gravity button was clicked swap up and down
    if (REVERSE_GRAVITY){
      up = r+1;
      down = r-1;
      
      if(up > NBR_ROWS-1){up = 0; }
      if(down < 0){ down = NBR_ROWS-1;}
    }
    
    //enables wrapping horizontally 
    if(left < 0){left = NBR_COLS-1;}
    if(right >= NBR_COLS){right = 0;}
    
        
    if(particleGrid[up][c] == EMPTY || particleGrid[up][left] == EMPTY ||particleGrid[up][right] == EMPTY){
      
      //the oil will rise about 55% slower than air bubbles
      int speed = getRandomNumber(1,100);
      if (speed <= 55){
      
      //empty the space below the oil as it rises
      particleGrid[r][c] = EMPTY;
      
      c = getAirDirection(r, c, up, right, left);
      
      //if there is nothing above the oil it rises
      particleGrid[up][c] = OIL;
      
    }//end speed moderation
      
    }//end checking if space is avaliable 
    
    
            
    else if(particleGrid[up][c] == OIL && (particleGrid[r][left] == EMPTY || particleGrid[r][right] == EMPTY)){
      
      //slow the oil down movement
      int speed = getRandomNumber(1,100);
      if (speed <= 20){
      
      //empty the space below the oil as it rises
      particleGrid[r][c] = EMPTY;
      
      c = poolOilBelow(r, c, up, right, left);
      
      //if there is nothing above the oil it rises
      particleGrid[r][c] = OIL;
      
      }
      
    }
    
    else if(particleGrid[up][c] == AIR && (particleGrid[r][left] == AIR || particleGrid[r][right] == AIR)){
      
      //slow the oil down movement
      int speed = getRandomNumber(1,100);
      if (speed <= 10){
      
      //make the oil pool above water also
      particleGrid[r][c] = AIR;
      c = poolOilAbove(r, c, up, right, left); 
      
      //if there is nothing above the oil it rises
      particleGrid[r][c] = OIL;
      
      }
      
    }    
    
    
  }//end moveOil
  
  
  public void moveAirParticles(int r, int c){
    
    
    int up = r-1;
    int down = r+1;
    int left = c-1;
    int right = c+1;
    
    if(down > NBR_ROWS){down = 0; }
    if(up < 0){ up = NBR_ROWS-1;}
    
    //if reverse gravity button was clicked swap up and down
    if (REVERSE_GRAVITY){
      up = r+1;
      down = r-1;
      
      if(up > NBR_ROWS-1){up = 0; }
      if(down < 0){ down = NBR_ROWS-1;}
    }
    
    //enables wrapping horizontally 
    if(left < 0){left = NBR_COLS-1;}
    if(right >= NBR_COLS){right = 0;}

    
    if(particleGrid[up][c] == EMPTY || particleGrid[up][left] == EMPTY ||particleGrid[up][right] == EMPTY){
      
      //empty the space below the air as it rises
      particleGrid[r][c] = EMPTY;
      
      c = getAirDirection(r, c, up, right, left);
      
      //if there is nothing above the air it rises
      particleGrid[up][c] = AIR;
      
    }//end checking if space is avaliable 
    
    
        
    else if(particleGrid[up][c] == AIR && (particleGrid[r][left] == EMPTY || particleGrid[r][right] == EMPTY)){
      
      //empty the space below the air as it rises
      particleGrid[r][c] = EMPTY;
      
      c = poolAir(r, c, up, right, left);
      
      //if there is nothing above the air it rises
      particleGrid[r][c] = AIR;
      
            
    }
    
        
    //checking if oil is above air
    if(particleGrid[up][c] == OIL ){
      
      particleGrid[r][c] = OIL;
      c = moveUpThroughParticles(r, c, up, right, left, OIL);
      
      particleGrid[up][c] = AIR;
      
    }//end oil check
    
 
  }
  
  
  //function dedicated to sand particle movement contains all the checks/controls that go along with sand movement
  public void moveSandParticles(int r, int c){
    
    
    int up = r-1;
    int down = r+1;
    int left = c-1;
    int right = c+1;
    
    
    //if reverse gravity button was clicked swap up and down
    if (REVERSE_GRAVITY){
      up = r+1;
      down = r-1;
      
      if(down < 0 ){down = NBR_ROWS-1; }
      if(up > NBR_ROWS-1){ up = 0;}
    }
    
    
    if(down > NBR_ROWS-1){down = 0; }
    if(up < 0){ up = NBR_ROWS-1;}
    
    //enables wrapping horizontally 
    if(left < 0){left = NBR_COLS-1;}
    if(right >= NBR_COLS){right = 0;}
    
    
    if(particleGrid[down][c] == EMPTY || particleGrid[down][left] == EMPTY ||particleGrid[down][right] == EMPTY){
      
        //empty the current pixel as the sand falls
        particleGrid[r][c] = EMPTY;
      
        //find the random direction for the sand to fall
         c = getSandDirection(r, c, down, right, left);
      
         particleGrid[down][c] = SAND;
      
    }//end space checks
    
    
    //checking if air bubbles are under the sand
    if(particleGrid[down][c] == AIR ){
      
      
      particleGrid[r][c] = AIR;
      c = moveDownThroughParticles(r, c, down, right, left,AIR);
      
      particleGrid[down][c] = SAND;
      
    }//end air bubble check
    
    //checking if oil is under the sand
    if(particleGrid[down][c] == OIL ){
      
      
      particleGrid[r][c] = OIL;
      c = moveDownThroughParticles(r, c, down, right, left, OIL);
      
      particleGrid[down][c] = SAND;
      
    }//end air bubble check
    
    
  }
  
  
  //if the sand can fall in the chosen direction, the direction to fall is returned to the moveSand function
  public int getSandDirection( int r, int c, int down, int right, int left){
    
    
    boolean sentDirection = false;
    
    while(!sentDirection){
      
      //0-33 of time it goes left, 33-66 it goes down, 66-100 it goes right.
      int directionPercentage = getRandomNumber(0, 100);
      
      //move left
      if(directionPercentage <= 33 && particleGrid[down][left] == EMPTY){
        sentDirection=true;
        return left;
      }
      
      //go straight down
      else if(directionPercentage <= 66 && particleGrid[down][c] == EMPTY){
        sentDirection=true;
        return c;
      }
      
      //go right
      else if(directionPercentage > 66 && particleGrid[down][right] == EMPTY){
        sentDirection=true;
        return right;
        
      }
      
    }//end while
    
    return c;
    
  }
  
  
  //used to move sand through other particles, may be implemented for other items
  public int moveDownThroughParticles(int r, int c, int down, int right, int left, int particle){
    

     boolean sentDirection = false;
    
    while(!sentDirection){
      
      //7% of time it goes left, 7% it goes right, 86% it goes straight up.
      int directionPercentage = getRandomNumber(0, 100);
      
      //move left
      if(directionPercentage <= 7 && particleGrid[down][left] == particle){
        sentDirection=true;
        return left;
      }
      
      //go right
      else if(directionPercentage <= 14 && directionPercentage >= 7 && particleGrid[down][right] == particle){
        sentDirection=true;
        return right;
        
      }
      
      //go straight down
      else if(directionPercentage > 14 && particleGrid[down][c] == particle){
        sentDirection=true;
        return c;
      }
      
      
    }//end while
    
    return c;
    
    
  }
  
  
  //moves air up through particles such as oil
    public int moveUpThroughParticles(int r, int c, int up, int right, int left, int particle){
    
    
    boolean sentDirection = false;
    
    while(!sentDirection){
      
      //7% of time it goes left, 7% it goes right, 86% it goes straight up.
      int directionPercentage = getRandomNumber(0, 100);
      
      //move left
      if(directionPercentage <= 7 && particleGrid[up][left] == particle){
        sentDirection=true;
        return left;
      }
      
      //go right
      else if(directionPercentage <= 14 && directionPercentage >= 7 && particleGrid[up][right] == particle){
        sentDirection=true;
        return right;
        
      }
      
      //go straight up
      else if(directionPercentage > 14 && particleGrid[up][c] == particle){
        sentDirection=true;
        return c;
      }
      
      
    }//end while
    
    return c;
    
    
  }
  
  
  //if the air can rise in the chosen direction, the direction to rise is returned to the function
  public int getAirDirection( int r, int c, int up, int right, int left){
    
    boolean sentDirection = false;
    
    while(!sentDirection){
      
      //7% of time it goes left, 7% it goes right, 86% it goes straight up.
      int directionPercentage = getRandomNumber(0, 100);
      
      //move left
      if(directionPercentage <= 7 && particleGrid[up][left] == EMPTY){
        sentDirection=true;
        return left;
      }
      
      //go right
      else if(directionPercentage <= 14 && directionPercentage >= 7 && particleGrid[up][right] == EMPTY){
        sentDirection=true;
        return right;
        
      }
      
      //go straight up
      else if(directionPercentage > 14 && particleGrid[up][c] == EMPTY){
        sentDirection=true;
        return c;
      }
      
      
    }//end while
    
    return c;
    
  }
  
  
  
  public int poolAir( int r, int c, int up, int right, int left){
    
    boolean sentDirection = false;
    
    while(!sentDirection){  
    
      int directionPercentage = getRandomNumber(0, 100);
      
      if(particleGrid[up][c] == AIR){
        
        if(particleGrid[r][left] == EMPTY && directionPercentage <= 50){
          sentDirection = true;
          return left;
        }
        if(particleGrid[r][right] == EMPTY && directionPercentage > 50){
          sentDirection = true;
          return right;
        }
        else{
          sentDirection = true;
          return c;
        }
   
      }
      
    }
      
      return c;
      
  }
  
  
    public int poolOilBelow( int r, int c, int up, int right, int left){
    
    boolean sentDirection = false;
    
    while(!sentDirection){  
    
      int directionPercentage = getRandomNumber(0, 100);
      
      if(particleGrid[up][c] == OIL){
        
        if(particleGrid[r][left] == EMPTY && directionPercentage <= 50){
          sentDirection = true;
          return left;
        }
        if(particleGrid[r][right] == EMPTY && directionPercentage > 50){
          sentDirection = true;
          return right;
        }
        else{
          sentDirection = true;
          return c;
        }
   
      }
      
    }
      
      return c;
      
  }
  
  public int poolOilAbove( int r, int c, int up, int right, int left){
    
    boolean sentDirection = false;
    
    while(!sentDirection){  
    
      int directionPercentage = getRandomNumber(0, 100);
      
      if(particleGrid[up][c] == AIR){
        
        if(particleGrid[r][left] == AIR && directionPercentage <= 50){
          sentDirection = true;
          return left;
        }
        if(particleGrid[r][right] == AIR && directionPercentage > 50){
          sentDirection = true;
          return right;
        }
        else{
          sentDirection = true;
          return c;
        }
   
      }
      
    }
      
      return c;
      
  }
  
  
  
    public void moveVapor(int r, int c){
     
    int up = r-1;
    int down = r+1;
    int left = c-1;
    int right = c+1;
    
    if(down > NBR_ROWS){down = 0; }
    if(up < 0){ up = NBR_ROWS-1;}
    
    //if reverse gravity button was clicked swap up and down
    if (REVERSE_GRAVITY){
      up = r+1;
      down = r-1;
      
      if(up > NBR_ROWS-1){up = 0; }
      if(down < 0){ down = NBR_ROWS-1;}
    }
    
    //enables wrapping horizontally 
    if(left < 0){left = NBR_COLS-1;}
    if(right >= NBR_COLS){right = 0;}
    
    int particle = particleGrid[up][c];

    
    if(particleGrid[up][c] == EMPTY || particleGrid[up][left] == EMPTY ||particleGrid[up][right] == EMPTY){
      
      int fadeChance = getRandomNumber(0, 500);
      
      //empty the space below the air as it rises
      particleGrid[r][c] = EMPTY;
      
      c = getVaporDirection(r,c,up,right,left);
      
      if(fadeChance >= 15){
      particleGrid[up][c] = VAPOR;
      }
      else{
        //particleGrid[up][c] = particle;
      }
      
    }//end checking if space is avaliable 
     
    
    //vapor disappears when it touches anything
    if(particleGrid[up][c] != EMPTY){
      particleGrid[r][c] = EMPTY;
    }
    
  }
  
    
  public int getVaporDirection( int r, int c, int up, int right, int left){
    
    
    boolean sentDirection = false;
    
    
    while(!sentDirection){
      
      //7% of time it goes left, 7% it goes right, 86% it goes straight up.
      int directionPercentage = getRandomNumber(0, 100);
      
      //move left
      if(directionPercentage <= 7 && particleGrid[up][left] == EMPTY){
        sentDirection=true;
        return left;
      }
      
      //go right
      else if(directionPercentage <= 14 && directionPercentage >= 7 && particleGrid[up][right] == EMPTY){
        sentDirection=true;
        return right;
        
      }
      
      //go straight up
      else if(directionPercentage > 14 && particleGrid[up][c] == EMPTY){
        sentDirection=true;
        return c;
      }
      
      
    }//end while
    
    return c;
    
  }  
  
  
  ////////////////////////////////////////////////////
  //DO NOT modify anything below here!!! /////////////
  ////////////////////////////////////////////////////
  
  public void run(){
    while (true){
      for (int i = 0; i < display.getSpeed(); i++){
        step();
      }
      updateDisplay();
      display.repaint();
      display.pause(1);  //wait for redrawing and for mouse
      int[] mouseLoc = display.getMouseLocation();
      if (mouseLoc != null)  //test if mouse clicked
        locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
    }
  }
  
  public int getRandomNumber (int low, int high){
    return (int)(Math.random() * (high - low)) + low;
  }
  
  public static int getNbrRows() {return NBR_ROWS;}
  public static int getNbrCols() {return NBR_COLS;}
  public static int getCellSize(){return CELL_SIZE;}
}
