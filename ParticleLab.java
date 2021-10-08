/**
 * @author Hannah
 * Underwater Particle Lab
 *
 * Choice Particle Descriptions:
 *
 * "Above Ocean" Mode: behaves in a similar fashion to 'normal' sand lab.
 *
 * Soap: combines with oil and dissolves into soap bubbles which fade into water.
 *
 * Kelp flakes: slowly grow over time when exposed to water.
 *
 * Shrimp: swim in water and eat kelp flakes, small bubbles are created when they eat.
 *
 * Invert: rgb values of all colors on the grid are inverted (-255)
 *
 * RESET: Resets display to original loaded file, also resets any alternative modes back to normal and underwater.
 *
 * End Of Evangelion: inspired by the Neon Genesis Evangelion series, resets to underwater mode and changes color scheme.
 */


import java.awt.*;

public class ParticleLab{
    static final int NBR_ROWS  = 160;  //180
    static final int NBR_COLS  = 180;  //180
    static final int CELL_SIZE = 800;  //800

    static final String FILE_NAME     = "ParticleLabFileTesting.txt";         //This is the name of the input file.
    static final String NEW_FILE_NAME = "ParticleLabFileTesting.txt";  //This is the name of the file you are saving.

    //add constants for particle types here
    public static final int EMPTY     = 0;
    public static final int METAL     = 1;
    public static final int SAND      = 2;
    public static final int AIR       = 3;
    public static final int VAPOR      = 4;
    public static final int OIL        = 5;
    public static final int SOAP       = 6;
    public static final int ALGAE      = 7;
    public static final int FISH       = 8;
    public static final int DESTRUCTOR = 9;
    public static final int GENERATOR  = 10;
    public static final int TOGGLE_GENERATOR = 11;
    public static final int GRAVITY    = 12;
    public static final int INVERT_COLORS = 13;
    public static final int SAVEFILE   = 14;
    public static final int EOE    = 15;
    public static final int SPACE = 16;     //above ocean mode
    public static final int RESET = 17;

    //non button items
    public static final int BUBBLES    = 18;
    public static final int SOAP_BUBBLES    = 19;

    //densities
    public static final int IMMOVABLE = 7;
    public static final int AIR_DENSITY = 0;
    public static final int VAPOR_DENSITY = 0;
    public static final int SPACE_DENSITY = 1;
    public static final int OIL_DENSITY = 2;
    public static final int WATER_DENSITY = 3; //EMPTY particle
    public static final int FISH_DENSITY = 3;
    public static final int SOAP_DENSITY = 4;
    public static final int ALGAE_DENSITY = 5;
    public static final int SAND_DENSITY = 6;
    public static final int DESTRUCTOR_DENSITY = IMMOVABLE;
    public static final int GENERATOR_DENSITY  = IMMOVABLE;
    public static final int METAL_DENSITY = IMMOVABLE; //solid, immovable particle
    public static final int BUBBLE_DENSITY = 0;

    public static final int LARGE_SPREAD = 3;
    public static final int MEDIUM_SPREAD = 2;
    public static final int SMALL_SPREAD = 1;

    public static final int WATER_SPREAD = SMALL_SPREAD;
    public static final int FISH_SPREAD = LARGE_SPREAD;
    public static final int METAL_SPREAD = 0;
    public static final int GEN_SPREAD = 0;
    public static final int DEST_SPREAD = 0;
    public static final int SAND_SPREAD = LARGE_SPREAD;
    public static final int SOAP_SPREAD = LARGE_SPREAD;
    public static final int AIR_SPREAD = SMALL_SPREAD;
    public static final int VAPOR_SPREAD = SMALL_SPREAD;
    public static final int OIL_SPREAD = SMALL_SPREAD; //POSSIBLY CHANGE TO MEDIUM
    public static final int ALGAE_SPREAD = SMALL_SPREAD;
    public static final int BUBBLE_SPREAD = SMALL_SPREAD;
    public static final int SPACE_SPREAD = SMALL_SPREAD;

    public static final int [] DENSITY = {WATER_DENSITY, METAL_DENSITY, SAND_DENSITY, AIR_DENSITY, VAPOR_DENSITY, OIL_DENSITY, SOAP_DENSITY, ALGAE_DENSITY, FISH_DENSITY,DESTRUCTOR_DENSITY, GENERATOR_DENSITY, 100,100,100,100,100, SPACE_DENSITY,100,BUBBLE_DENSITY, BUBBLE_DENSITY};
    public int [] SPREAD = {WATER_SPREAD, METAL_SPREAD, SAND_SPREAD, AIR_SPREAD, VAPOR_SPREAD, OIL_SPREAD, SOAP_SPREAD, ALGAE_SPREAD, FISH_SPREAD,DEST_SPREAD,GEN_SPREAD, 0,0,0,0,0, SPACE_SPREAD,100,BUBBLE_SPREAD, BUBBLE_SPREAD};
    public int [] SPEED = {10,0,0,30,40,15,0,0,0,0,0,0,0,0,0,0, 50,0,30, 3};


    public static boolean REVERSE_GRAVITY = false;
    public static boolean GENERATOR_ON = true;
    public static boolean END_OF_EVANGELION = false;
    public static boolean INVERT = false;
    public static boolean SPACE_MODE = false;

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    public static final int RED = 0;
    public static final int GREEN = 1;
    public static final int BLUE = 2;

    //do not add any more global fields
    private int row = 0;
    private int col = 0;

    private int[][] particleGrid;
    private LabDisplay display;

    //---------------------------------------------------------------------------------------------------------

    public static void main(String[] args){
        System.out.println("================= Starting Program =================");
        System.out.println("ROWS: " + NBR_ROWS + "\nCOLS: " + NBR_COLS + "\nCELL_SIZE: " + CELL_SIZE + "\n");

        ParticleLab lab = new ParticleLab(NBR_ROWS, NBR_COLS);  //creates the object
        lab.run();
    }

    //SandLab constructor - ran when the above lab object is created
    public ParticleLab(int numRows, int numCols){
        String[] names = new String[18];

        names[EMPTY]      = "Water (Empty)";
        names[METAL]      = "Metal";
        names[SAND]       = "Sand";
        names[AIR]        = "Air Bubbles";
        names[VAPOR]      = "Vapor";
        names[OIL]        = "Oil";
        names[SOAP]       = "Soap";
        names[ALGAE]      = "Kelp Flakes";
        names[FISH]       = "Shrimp";
        names[DESTRUCTOR] = "Destructor";
        names[GENERATOR]  = "Generator";
        names[TOGGLE_GENERATOR] = "Toggle Generator OFF/ON";
        names[GRAVITY]    = "Reverse Gravity";
        names[INVERT_COLORS] = "Invert Colors";
        names[SAVEFILE]   = "SaveFile";
        names[EOE]        = "End of Evangelion (Red Sea)";
        names[SPACE]      = "Above Ocean Mode";
        names[RESET]      = "RESET";



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
        int density = DENSITY[particleGrid[row][col]];

        if(tool == SPACE){
            SPACE_MODE = !SPACE_MODE;
            //speeding up water particles in open air environment
            SPEED[0] =500;
            //narrowing spread of sand in open air environment
            //not sure if actually working
            SPREAD[3]=SMALL_SPREAD;

            //reset EOE mode to OFF when switching to SPACE_MODE and reset to original file
            if(END_OF_EVANGELION) {
                END_OF_EVANGELION = !END_OF_EVANGELION;
                particleGrid = ParticleLabFiles.readFile(FILE_NAME);
            }

            spaceMode();
        }

        //fix this statement, it might be setting non-visible items to the grid
        //can overwrite anything except immovable particles(metal, generators ,destructors) unless EMPTY tool is chosen
        if (particleGrid[row][col] >= EMPTY && density != IMMOVABLE || tool == EMPTY){
            particleGrid[row][col] = tool;
        }

        //when Reverse Gravity tool is chosen, global gravity variable is inverted.
        if (tool == GRAVITY) {
            particleGrid[row][col] =  particle;
            REVERSE_GRAVITY = !REVERSE_GRAVITY;
        }

        //when toggle generator is chosen, global generator variable is inverted.
        if(tool == TOGGLE_GENERATOR){
            particleGrid[row][col] = particle;
            GENERATOR_ON = !GENERATOR_ON;
        }

        if (tool == SAVEFILE) {
            //Without these two lines, save file action kills the space it touches.
            tool = EMPTY;
            particleGrid[row][col] = particle; //should not need to update array here BUT original did not have this line
            //and somehow was updating the array anyway...
            ParticleLabFiles.writeFile(particleGrid, NEW_FILE_NAME);
        }


        if(tool == EOE){
            particleGrid[row][col] =  particle;
            END_OF_EVANGELION = !END_OF_EVANGELION;

            //reset to underwater for EOE Mode if it was in space mode
            if(SPACE_MODE) {
                SPACE_MODE = !SPACE_MODE;
                spaceMode();
            }

            if(END_OF_EVANGELION){
                particleGrid = ParticleLabFiles.readFile("EVA.txt");
            }
            else{
                particleGrid = ParticleLabFiles.readFile(FILE_NAME);
            }
        }

        if(tool == INVERT_COLORS){
            particleGrid[row][col] =  particle;
            INVERT = !INVERT;
        }

        if(tool == RESET){
            //reset to underwater if it was in space mode
            if(SPACE_MODE) {
                SPACE_MODE = !SPACE_MODE;
                spaceMode();
            }
            //resets to normal mode if in EOE
            if(END_OF_EVANGELION) {
                END_OF_EVANGELION = !END_OF_EVANGELION;
            }
            //resets if colors are inverted
            if(INVERT){
                INVERT = !INVERT;
            }
            //resets to normal gravity
            if(REVERSE_GRAVITY){
                REVERSE_GRAVITY = !REVERSE_GRAVITY;
            }
            System.out.println("Attempting to load: " + FILE_NAME);
            particleGrid = ParticleLabFiles.readFile(FILE_NAME);
        }


    }

    public Color invertColors(Color color){

        int r =  255 - color.getRed();
        int g =  255 - color.getGreen();
        int b =  255 - color.getBlue();

        Color invertedColor = new Color(r,g,b);

        return invertedColor;
    }

    public Color [] getInvertedColors(Color [] colors){

        for(int i = 0; i <colors.length; i++){
            colors[i] = invertColors(colors[i]);
        }

        return colors;
    }

    //Examines each element of the 2D particleGrid and paints a color onto the display
    public void updateDisplay(){

        Color metal = new Color (44, 49, 51);
        Color sand = new Color (223, 196, 123);
        Color air = new Color(205,232,255,150);
        Color vapor = new Color(160, 235, 228, 190);
        Color oil = new Color(0,0,0);
        Color generator = new Color(255, 94, 212);
        Color destructor = new Color(0, 255, 234);
        Color soap = new Color(156, 120, 255);
        
        //makes soap color a bit darker to be visible in above ocean mode
        if(SPACE_MODE){
            soap = new Color(139, 106, 230);
        }
        Color kelp = new Color(12, 199, 49);
        //Color kelp = new Color(73, 189, 55);
        //old fish color
        //Color fish = new Color(242, 86, 7);
        //new "shrimp" color
        Color shrimp = new Color(226, 59, 90);
        Color bubbles = new Color(0,170,242,185);
        Color soapBubbles = new Color(178, 150, 255);

        Color space = new Color(10, 0, 36);

        //metal acts as placeholder for non-displayed tools and bubbles for EMPTY which is not static
        Color [] colors = {bubbles, metal, sand, air, vapor, oil, soap, kelp, shrimp,destructor,generator,metal,metal,metal,metal,metal, space, metal, bubbles, soapBubbles};

        //rgb values for the underwater background
        int red = 0;
        int green = 170;
        int blue = 242;

        //rgb values for above ocean background
        int spaceR = 0;
        int spaceG = 156;
        int spaceB = 229;

        int [] aboveOceanGradient = {spaceR, spaceG, spaceB};

        //INVERT COLORS
        if(INVERT) {
            colors = getInvertedColors(colors);
            red = 255;
            green = (green - 255 ) *-1;
            blue = (blue - 255) * -1;

            //inverts background colors for above ocean mode
            if(SPACE_MODE){
                spaceR = 255;
                spaceG = (spaceG - 255) *-1;
                spaceB = (spaceB -255) * -1;
                aboveOceanGradient[RED] = spaceR;
                aboveOceanGradient[GREEN] = spaceG;
                aboveOceanGradient[BLUE] = spaceB;

            }
        }

        //get colors for evangelion color scheme
        if(END_OF_EVANGELION){
            colors = endOfEvangelionColorScheme(colors);
            red = 160;
            green = 11;
            blue = 11;

            //now you can invert the colors for EOE
            if(INVERT){
                colors = getInvertedColors(colors);
                red = 255-160;
                green = 255 - green;
                blue = 255 - blue;
            }
        }


        for (int r = 0; r < NBR_ROWS; r++) {
            for (int c = 0; c < NBR_COLS; c++) {

                int particle = particleGrid[r][c];

                if (particle == EMPTY) {
                    display.setColor(r, c, new Color(red, green, blue));
                }

                else if(SPACE_MODE && particle ==SPACE){
                    display.setColor(r, c, new Color(aboveOceanGradient[RED],aboveOceanGradient[GREEN],aboveOceanGradient[BLUE]));
                }

                else if (particle == GENERATOR) {
                    if (GENERATOR_ON) {
                        display.setColor(r, c, colors[GENERATOR]);
                    } else {
                        display.setColor(r, c, colors[METAL]);
                    }
                }
                else if (particle == DESTRUCTOR) {
                    display.setColor(r, c, colors[DESTRUCTOR]);
                }

                //updates display for all visible particle types
                else if (particle != TOGGLE_GENERATOR && particle != GRAVITY && particle != SAVEFILE && particle != EOE && particle != INVERT_COLORS && particle != SPACE) {
                    display.setColor(r, c, colors[particle]);
                }


            }

            //evangelion color scheme shifts red only
            if(END_OF_EVANGELION){
                red--;
                //changes the color shift to ++ for red if inverted
                if(INVERT){
                    red+=2;
                }
            }
            //inverted color shift
            else if(INVERT && !END_OF_EVANGELION){
                red--;
                blue ++;
                green ++;

                //gets the gradient values when the color is inverted for above ocean background
                aboveOceanGradient = getAboveOceanGradientValues(spaceR,spaceG,spaceB,r);
                spaceR = aboveOceanGradient[RED];
                spaceG = aboveOceanGradient[GREEN];
                spaceB = aboveOceanGradient[BLUE];

            }

            //normal color shift
            else{
                blue--;
                green--;
            }

            if(SPACE_MODE && !INVERT){
                //gets the new rgb values for the above ocean background to create a smooth gradient
                aboveOceanGradient = getAboveOceanGradientValues(spaceR,spaceG,spaceB,r);
                spaceR = aboveOceanGradient[RED];
                spaceG = aboveOceanGradient[GREEN];
                spaceB = aboveOceanGradient[BLUE];
            }
        }

    }


    public int [] getAboveOceanGradientValues(int r, int g, int b, int row){

        //assign starting rgb values to the array
        int [] rgb = {r,g,b};

        //shifts from blue to pink
        if(row < NBR_ROWS/2){

            rgb[RED] += 2;
            if(row % 2 == 0){
                rgb[RED]++;
            }
            if(row%3==0){
                rgb[GREEN]--;
                rgb[BLUE]--;
            }

        }

        //shifts from pink to orange-ish
        else{
            if(row % 2 == 0){
                rgb[RED]++;
                rgb[GREEN]++;
                rgb[BLUE]-=2;
            }
        }



        //gradient for inverted color
        if(INVERT){
            rgb[RED] = r;
            rgb[GREEN] = g;
            rgb[BLUE] = b;

            rgb[RED]--;
            if(g < 255 && row % 5 ==0) {
                rgb[GREEN]++;
                rgb[BLUE]++;
            }
            if(row % 4 == 0){
                rgb[RED]--;
            }
        }

        return rgb;
    }


    public void spaceMode(){

        for (int r = 0; r < NBR_ROWS; r++) {
            for (int c = 0; c < NBR_COLS; c++) {

                if(SPACE_MODE) {
                    if (particleGrid[r][c] == EMPTY) {
                        particleGrid[r][c] = SPACE;
                    }

                    if (particleGrid[r][c] == AIR) {
                        particleGrid[r][c] = EMPTY;
                    }
                }
                else{

                    if (particleGrid[r][c] == EMPTY) {
                        particleGrid[r][c] = AIR;
                    }

                    if (particleGrid[r][c] == SPACE) {
                        particleGrid[r][c] = EMPTY;
                    }

                }
            }
        }
    }

    //called repeatedly.
    //causes one random particle to maybe do something.
    public void step(){

        int r = getRandomNumber(0,NBR_ROWS);
        int c = getRandomNumber(0,NBR_COLS);

        row = r;
        col = c;

        int currentParticle = particleGrid[r][c];

        //up,down,left,right
        int [] directions = {r-1, r+1, c-1, c+1};

        //gets valid directions for the particles to wrap without ever going out of bounds
        directions = wrapParticles(directions, r);

        int spread = SPREAD[currentParticle];

        int density = DENSITY[currentParticle];

        //The particle will move if it is NOT an immovable particle (metal, generator, destructor) AND its is not a fish
        if(density != IMMOVABLE /* && currentParticle!=FISH*/) {
            moveParticle(directions, r, c, spread);
            currentParticle = particleGrid[row][col];
            spread = SPREAD[currentParticle];
        }
        //if there is generator particles on grid and the generator is ON
        if(currentParticle == GENERATOR && GENERATOR_ON){
            generator(directions,row,col);
        }
        if(currentParticle == DESTRUCTOR){
            destructor(directions,row,col);
        }
        if(currentParticle == VAPOR){
            vapor(directions,row,col);
        }
        if(currentParticle == BUBBLES || currentParticle == SOAP_BUBBLES){
            bubbles(directions,row,col);
        }
        if(currentParticle == SOAP){
            soap(directions,row,col);
        }
        if(currentParticle ==ALGAE){
            growKelp(directions,row,col);
        }
        if(particleGrid[row][col] == FISH ){
            directions = wrapParticles(directions, row);
            fishBehavior(directions,row,col);
        }

    }

    public void fishBehavior(int[] directions, int r, int c){

        int down = directions[DOWN];
        int up = directions[UP];
        int left = directions [LEFT];
        int right = directions [RIGHT];

        int fishParticle = particleGrid[r][c];

        int rightParticle = particleGrid[r][right];
        int leftParticle = particleGrid[r][left];
        int belowParticle = particleGrid[down][c];
        int aboveParticle = particleGrid[up][c];

        int speedPercentage = getRandomNumber(0,100);

        if(fishParticle == FISH  && speedPercentage > 30) {

            rightParticle = particleGrid[r][right];
            leftParticle = particleGrid[r][left];
            belowParticle = particleGrid[down][c];

            //moves particle 50% right, 50% left
            int directionPercentage = getRandomNumber(0, 100);

            if (directionPercentage <= 33 && (rightParticle == EMPTY)) {
                particleGrid[r][c] = particleGrid[r][right];
                particleGrid[r][right] = fishParticle;
                col = right;
            }
            else if ((directionPercentage <= 66 && directionPercentage >33) && (leftParticle == EMPTY)) {
                particleGrid[r][c] = particleGrid[r][left];
                particleGrid[r][left] = fishParticle;
                col = left;
            }
            else if (directionPercentage > 66 && (belowParticle == EMPTY)) {

                particleGrid[r][c] = particleGrid[down][c];
                particleGrid[down][c] = fishParticle;
                row = down;
            }
        }

        fishParticle = particleGrid[row][col];

        int bubbles = BUBBLES;
        if(SPACE_MODE){
            bubbles = SPACE;
        }

        if(fishParticle == FISH && speedPercentage  == 0) {
            //if there is a kelp on the fish's left or right or bottom, the fish moves toward it and eats it
            if (leftParticle == ALGAE) {
                particleGrid[r][c] = bubbles;
                particleGrid[r][left] = fishParticle;
            } else if (rightParticle == ALGAE) {
                particleGrid[r][c] = bubbles;
                particleGrid[r][right] = fishParticle;
            } else if (belowParticle == ALGAE /*&& speedPercentage < 10*/) {
                particleGrid[r][c] = bubbles;
                particleGrid[down][c] = fishParticle;
            }
        }

    }

    public void growKelp(int[] directions, int r, int c){

        int up = directions[UP];

        int aboveParticle = particleGrid[up][c];

        int growthRate = getRandomNumber(0, 1000);

        //1 in 1000 chance for the kelp to grow
        if(growthRate == 0) {

            //if the algae is exposed to water, it can grow
            if (aboveParticle == EMPTY) {

                particleGrid[up][c] = ALGAE;
            }
        }

    }

    public void soap(int[] directions, int r, int c){

        int currentParticle = particleGrid[r][c];

        int up = directions[UP];
        int down = directions[DOWN];
        int left = directions[LEFT];
        int right = directions[RIGHT];

        int belowParticle = particleGrid[down][c];
        int aboveParticle = particleGrid[up][c];
        int leftParticle = particleGrid[r][left];
        int rightParticle = particleGrid[r][right];

        int empty = EMPTY;

        if(SPACE_MODE){
            empty = SPACE;
        }

        if(belowParticle == OIL){
            particleGrid[r][c] = SOAP_BUBBLES;
            particleGrid[down][c] =empty;
            row = down;
        }
        else if(leftParticle == OIL && currentParticle == SOAP){
            particleGrid[r][c] = SOAP_BUBBLES;
            particleGrid[r][left] =empty;
            col = left;
        }
        else if(rightParticle == OIL && currentParticle ==SOAP){
            particleGrid[r][c] = SOAP_BUBBLES;
            particleGrid[r][right] =empty;
            col = right;
        }
        else if(aboveParticle == OIL){
            particleGrid[r][c] = SOAP_BUBBLES;
            particleGrid[up][c] =empty;
            row = up;
        }

    }

    public void vapor(int [] directions, int r, int c){

        int currentParticle = particleGrid[r][c];
        int aboveParticle = particleGrid[directions[UP]][c];

        int fadeChance = getRandomNumber(0,100);

        int empty = EMPTY;

        if(SPACE_MODE){
            empty = SPACE;
        }

        if(currentParticle == VAPOR && aboveParticle != empty){
            particleGrid[r][c] = empty;
        }
        else if(fadeChance < 4){
            particleGrid[r][c] = empty;
        }

    }

    public void bubbles(int [] directions, int r, int c){

        int currentParticle = particleGrid[r][c];
        int aboveParticle = particleGrid[directions[UP]][c];

        int fadeChance = getRandomNumber(0,100);

        int empty = EMPTY;

        if(SPACE_MODE){
            empty = SPACE;
        }

        if((currentParticle == BUBBLES || currentParticle == SOAP_BUBBLES) && aboveParticle != empty){
            particleGrid[r][c] = empty;
        }
        else if(fadeChance < 10){
            particleGrid[r][c] = empty;
        }

    }

    public void destructor(int [] directions, int r, int c){

        int down = directions[DOWN];
        int up = directions[UP];

        int aboveParticle = particleGrid[up][c];
        int aboveParticleDensity = DENSITY[aboveParticle];
        int belowParticle = particleGrid[down][c];
        int belowParticleDensity = DENSITY[belowParticle];

        int empty = EMPTY;

        if(SPACE_MODE){
            empty = SPACE;
        }

        if(aboveParticle != empty && aboveParticleDensity != IMMOVABLE){

            particleGrid[up][c] = VAPOR;
        }
        if(belowParticle!=empty && belowParticleDensity != IMMOVABLE){

            particleGrid[down][c] = VAPOR;
        }

    }

    //Generated particles overwrite the existing particle above/below generator
    public void generator(int[] directions, int r, int c){

        int empty = EMPTY;

        if(SPACE_MODE){
            empty = SPACE;
        }


        int down = directions[DOWN];
        int up = directions[UP];

        int aboveParticle = particleGrid[up][c];
        int aboveParticleDensity = DENSITY[aboveParticle];
        int belowParticle = particleGrid[down][c];
        int belowParticleDensity = DENSITY[belowParticle];

        if(aboveParticleDensity!= IMMOVABLE && aboveParticle != empty && belowParticleDensity != IMMOVABLE) {

            int num = getRandomNumber(0, 35);

            if (num == 0) {
                particleGrid[down][c] = aboveParticle;
            }
        }

        if(aboveParticleDensity!= IMMOVABLE && belowParticle != empty && belowParticleDensity != IMMOVABLE) {

            int num = getRandomNumber(0, 35);

            if (num == 0) {
                particleGrid[up][c] = belowParticle;
            }
        }

    }

    public int [] wrapParticles(int [] directions, int r){

        //if reverse gravity button was clicked swap up and down
        if (REVERSE_GRAVITY){
            directions[UP] = r+1;
            directions[DOWN] = r-1;

            if(directions[DOWN] < 0 ){directions[DOWN] = NBR_ROWS-1; }
            if(directions[UP] > NBR_ROWS-1){ directions[UP] = 0;}
        }

        if(directions[DOWN] > NBR_ROWS-1){directions[DOWN] = 0; }
        if(directions[UP] < 0){ directions[UP] = NBR_ROWS-1;}

        //enables wrapping horizontally
        if(directions[LEFT] < 0){directions[LEFT] = NBR_COLS-1;}
        if(directions[RIGHT] >= NBR_COLS){directions[RIGHT] = 0;}

        return directions;
    }

    public void moveParticle(int [] directions, int r, int c, int spread){

        int newLocation = c;
        int down = directions[DOWN];
        int up = directions[UP];

        int currentParticle = particleGrid[r][c];
        int belowParticle = particleGrid[directions[DOWN]][c];

        int particleDensity = DENSITY[currentParticle];
        int belowParticleDensity = DENSITY[belowParticle];

        int speed = SPEED[currentParticle];

        //particles heavier than those below it will fall, this includes water(EMPTY) particles
        if(particleDensity > belowParticleDensity){

            newLocation = getFallingParticleLocation(r,c,directions,spread);

            particleGrid[r][c] =  particleGrid[down][newLocation];
            particleGrid[down][newLocation] = currentParticle;
            row = down;
            col = newLocation;
        }

        //allows sand or other falling particles to pile naturally
        else if(particleDensity == belowParticleDensity){

            newLocation = getFallingParticleLocation(r,c,directions,spread);

            particleGrid[r][c] = particleGrid[down][newLocation];
            particleGrid[down][newLocation] = currentParticle;
            row = down;
            col = newLocation;
        }

        //If the particles are relatively light, they should level out into air/oil pockets
        else if(particleDensity == OIL_DENSITY || particleDensity == AIR_DENSITY || particleDensity == WATER_DENSITY || particleDensity==SPACE_DENSITY ){

            if(belowParticle != SAND) {
                newLocation = getPoolingDirection(r, c, directions, speed);

                particleGrid[r][c] = particleGrid[r][newLocation];
                particleGrid[r][newLocation] = currentParticle;
                row = r;
                col = newLocation;
            }
        }

        //when in open air environment, air bubbles behave more like vapor
        if(SPACE_MODE && particleGrid[row][col] == AIR){
            vapor(directions,row,col);
        }
    }

    public int getFallingParticleLocation(int r, int c, int [] directions, int spread){

        int down = directions[DOWN];
        int left = directions [LEFT];
        int right = directions [RIGHT];

        //Densities of surrounding particles
        int currentParticle = DENSITY[particleGrid[r][c]];
        int downLeft = DENSITY[particleGrid[down][left]];
        int downRight = DENSITY[particleGrid[down][right]];
        int downStraight = DENSITY[particleGrid[down][c]];

        int [] spreadRange = getSpreadRange(spread);

        int spreadPercentage = getRandomNumber(0, 100);

        //move left if the current particle is more dense than the one to its down left
        if(spreadPercentage <= spreadRange[0] && currentParticle > downLeft){
            return left;
        }

        //go right
        else if(spreadPercentage <= spreadRange[1] && currentParticle > downRight){
            return right;
        }

        //go straight
        else if(spreadPercentage > spreadRange[1] && currentParticle > downStraight){
            return c;
        }

        return c;
    }


    public int getPoolingDirection(int r, int c, int [] directions, int speed){

        int up = directions[UP];
        int left = directions [LEFT];
        int right = directions [RIGHT];

        //Densities of surrounding particles
        int currentParticleD = DENSITY[particleGrid[r][c]];
        int upStraightD = DENSITY[particleGrid[up][c]];
        int rightD = DENSITY[particleGrid[r][right]];
        int leftD = DENSITY[particleGrid[r][left]];

        //speed determined by current particle
        int speedPercentage = getRandomNumber(0,100);

        //moves particle 50% right, 50% left
        int directionPercentage = getRandomNumber(0, 100);

        //Controls how fast particles pool, this includes water, which will mostly pool above soap
        if (speedPercentage > (100 - speed)) {

            //pooling only occurs when there is a pocket of the particle type
            if (currentParticleD == upStraightD) {

                if (directionPercentage <= 50 && currentParticleD < rightD && rightD != IMMOVABLE) {
                    return right;
                }
                if (directionPercentage > 50 && currentParticleD < leftD && leftD != IMMOVABLE) {
                    return left;
                }

            }
        }
        return c;
    }

    public int [] getSpreadRange(int spread){

        int range [] = new int[2];

        if(spread == LARGE_SPREAD){
            range[0] = 25;
            range[1]= 50;
        }
        if(spread == SMALL_SPREAD){
            range[0] = 15;
            range[1]= 25;
        }

        return range;
    }

    //Just playing around with colors in this function, not currently being used
    public Color [] endOfEvangelionColorScheme(Color [] colors){

        if(!SPACE_MODE) {
            colors[AIR] = new Color(191, 132, 132, 110);
            colors[VAPOR] = new Color(205, 232, 255, 50);
            colors[SOAP] = new Color(115, 119, 156, 110);
            colors[ALGAE] = new Color(82, 209, 145);
            colors[FISH] = new Color(145, 4, 36);
            colors[BUBBLES] = new Color(205, 232, 255, 110);
            colors[SAND] = new Color(199, 171, 165);
            colors[METAL] = new Color(36, 24, 21);
            colors[DESTRUCTOR] = new Color(230, 224, 223);
            colors[GENERATOR] = new Color(158, 16, 6);
            colors[SOAP_BUBBLES] = new Color(205, 232, 255, 110);

        }
        return colors;
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
