package geekbrains.java_intro.homework_04;

//import java.util.Arrays;
import java.util.Scanner;

//1. Полностью разобраться с кодом, попробовать переписать с нуля, стараясь не подглядывать в методичку;
//2. Переделать проверку победы, чтобы она не была реализована просто набором условий, например, с использованием циклов.
//3. * Попробовать переписать логику проверки победы, чтобы она работала для поля 5х5 и количества фишек 4. Очень желательно не делать это просто набором условий для каждой из возможных ситуаций;
//4. *** Доработать искусственный интеллект, чтобы он мог блокировать ходы игрока.

// Подошел к вопросу серьезно и сделал программу с "умным компьютером" для н-мерного поля с настраиваемым размером выигрышной строки
// При классической игре 3х3 все партии сводятся к ничьей, при игре 5x5 и линией из 4х фишек обыграть тоже не выходит,
// Высшие размерности не тестировал особо, но в теории все должно работать)).

public class Main {

    public static final int BOARD_DIMENSION = 5; // <<-- Здесь можно увеличить размер поля, для дефолтных крестиков ноликов обе константы выставить на 3
    public static final int WINNING_LINE_SIZE = 4; // <<-- А здесь можно увеличить количество фишек (Очевидно, должен быть меньше размера поля)
    public static char[][] gameBoard = new char[BOARD_DIMENSION][BOARD_DIMENSION];
    public static final char CROSS = 'X';
    public static final char ZERO = 'O';
    public static final char EMPTY = '.';
    public static Scanner myScanner = new Scanner(System.in);

    public static void boardReset(char[][] a){
        for (int i = 0; i < BOARD_DIMENSION; i++){
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                a[i][j] = EMPTY;
            }
        }
    }

    public static void boardPrint(char[][] a){
        System.out.println("Current state of the board:");
        for (int i = 0; i < BOARD_DIMENSION; i++){
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                System.out.print(a[i][j]);
            }
            System.out.println();
        }
    }

    public static void positionValuesPrint(int[][] a){

        for (int i = 0; i < BOARD_DIMENSION; i++){
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                System.out.print(a[i][j]);
            }
            System.out.println();
        }
    }

    public static void playerTurn(char[][] a){
        int i,j;
        System.out.println("Your turn!");
        System.out.println("Enter raw: ");
        i = myScanner.nextInt();
        System.out.println("Enter column: ");
        j = myScanner.nextInt();
        a[i-1][j-1] = CROSS;
    }

    public static int[][] positionValuesCalculator(char[][] a, char side){ // Returns array of numbers, each number shows maximum number of possible lines that we can create with this square
        int[][] positionValues = new int[BOARD_DIMENSION][BOARD_DIMENSION];
        int lineCounter = 0;
        int lineOffset = 0; //Difference between space potentially available for line and WINNING_LINE_SIZE
        int spaceToTheLeft, spaceToTheRight, spaceAbove, spaceBelow, spaceSlashAbove, spaceSlashBelow,spaceBackslashAbove, spaceBackslashBelow;

        for (int i=0;i<BOARD_DIMENSION;i++) {
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                positionValues[i][j] = 0;
                if (a[i][j]==EMPTY) {
                    spaceToTheLeft = j;
                    spaceToTheRight = BOARD_DIMENSION - 1 - j;
                    if (spaceToTheLeft > WINNING_LINE_SIZE - 1) {
                        spaceToTheLeft = WINNING_LINE_SIZE - 1;
                    }
                    if (spaceToTheRight > WINNING_LINE_SIZE - 1) {
                        spaceToTheRight = WINNING_LINE_SIZE - 1;
                    }
                    lineOffset = spaceToTheLeft + spaceToTheRight + 1 - WINNING_LINE_SIZE;
                    for (int x = j - spaceToTheLeft; x <= j - spaceToTheLeft + lineOffset; x++) { //We check for the winning line potential as many times as there are variations of winning lines passing through current point
                        lineCounter = 0;
                        for (int y = x; y < x + WINNING_LINE_SIZE; y++) {
                            if (a[i][y] == side || a[i][y] == EMPTY) {
                                lineCounter++;
                            }
                        }
                        if (lineCounter == WINNING_LINE_SIZE){
                            positionValues[i][j]++;
                            break;
                        }
                    }
                    spaceAbove = i;
                    spaceBelow = BOARD_DIMENSION - 1 - i;
                    if (spaceAbove > WINNING_LINE_SIZE - 1) {
                        spaceAbove = WINNING_LINE_SIZE - 1;
                    }
                    if (spaceBelow > WINNING_LINE_SIZE - 1) {
                        spaceBelow = WINNING_LINE_SIZE - 1;
                    }
                    lineOffset = spaceAbove + spaceBelow + 1 - WINNING_LINE_SIZE;
                    for (int x = i - spaceAbove; x <= i - spaceAbove + lineOffset; x++) { //We check for the winning line potential as many times as there are variations of winning lines passing through current point
                        lineCounter = 0;
                        for (int y = x; y < x + WINNING_LINE_SIZE; y++) {
                            if (a[y][j] == side || a[y][j] == EMPTY) {
                                lineCounter++;
                            }
                        }
                        if (lineCounter == WINNING_LINE_SIZE){
                            positionValues[i][j]++;
                            break;
                        }
                    }
                    spaceSlashAbove = java.lang.Math.min(spaceAbove,spaceToTheRight);
                    spaceSlashBelow = java.lang.Math.min(spaceBelow,spaceToTheLeft);
                    if (spaceSlashAbove > WINNING_LINE_SIZE - 1) {
                        spaceSlashAbove = WINNING_LINE_SIZE - 1;
                    }
                    if (spaceSlashBelow > WINNING_LINE_SIZE - 1) {
                        spaceSlashBelow = WINNING_LINE_SIZE - 1;
                    }
                    lineOffset = spaceSlashAbove + spaceSlashBelow + 1 - WINNING_LINE_SIZE;
                    if(lineOffset>=0){
                        for (int x = j - spaceSlashBelow; x <= j - spaceSlashBelow + lineOffset; x++) { //We check for the winning line potential as many times as there are variations of winning lines passing through current point
                            lineCounter = 0;
                            for (int y = x; y < x + WINNING_LINE_SIZE; y++) {
                                if (a[y][y] == side || a[y][y] == EMPTY) {
                                    lineCounter++;
                                }
                            }
                            if (lineCounter == WINNING_LINE_SIZE){
                                positionValues[i][j]++;
                                break;
                            }
                        }
                    }
                    spaceBackslashAbove = java.lang.Math.min(spaceAbove,spaceToTheLeft);
                    spaceBackslashBelow = java.lang.Math.min(spaceBelow,spaceToTheRight);
                    if (spaceBackslashAbove > WINNING_LINE_SIZE - 1) {
                        spaceBackslashAbove = WINNING_LINE_SIZE - 1;
                    }
                    if (spaceBackslashBelow > WINNING_LINE_SIZE - 1) {
                        spaceBackslashBelow = WINNING_LINE_SIZE - 1;
                    }
                    lineOffset = spaceBackslashAbove + spaceBackslashBelow + 1 - WINNING_LINE_SIZE;
                    if(lineOffset>=0){
                        for (int x = i - spaceBackslashAbove; x <= i - spaceBackslashAbove + lineOffset; x++) { //We check for the winning line potential as many times as there are variations of winning lines passing through current point
                            lineCounter = 0;
                            for (int y = x; y < x + WINNING_LINE_SIZE; y++) {
                                if (a[y][y] == side || a[y][y] == EMPTY) {
                                    lineCounter++;
                                }
                            }
                            if (lineCounter == WINNING_LINE_SIZE){
                                positionValues[i][j]++;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return positionValues;
    }

    public static int[] winningSquare(char[][] a, char side){ //Gets playing side as an argument and returns an array, where first element means: 1 there is winning square, 0 - there is none. 2nd and 3rd elements - coordinates of the first winning square if there is a winning square on the board for that side.
        int[] square = new int[]{0,0,0};
        char[][] currentWinningSquares = winningSquares(a,side);
        int currentWinningSquaresCounter = 0;
        for (int x = 0; x < BOARD_DIMENSION; x++){
            for (int y = 0; y < BOARD_DIMENSION; y++) {
                if(currentWinningSquares[x][y]==side){
                    currentWinningSquaresCounter++;
                    square[0]=currentWinningSquaresCounter;
                    square[1]=x;
                    square[2]=y;
                    return square;
                }
            }
        }
        return square;
//        int lineCounter = 0; //
//        int lineOffset = 0; //Difference between space potentially available for line and WINNING_LINE_SIZE
/*        int spaceToTheLeft, spaceToTheRight, spaceAbove, spaceBelow, spaceSlashAbove, spaceSlashBelow,spaceBackslashAbove, spaceBackslashBelow;

        for (int i=0;i<BOARD_DIMENSION;i++) {
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                //positionValues[i][j] = 0;
                if (a[i][j]==EMPTY) {
                    spaceToTheLeft = j;
                    spaceToTheRight = BOARD_DIMENSION - 1 - j;
                    if (spaceToTheLeft > WINNING_LINE_SIZE - 1) {
                        spaceToTheLeft = WINNING_LINE_SIZE - 1;
                    }
                    if (spaceToTheRight > WINNING_LINE_SIZE - 1) {
                        spaceToTheRight = WINNING_LINE_SIZE - 1;
                    }
                    lineOffset = spaceToTheLeft + spaceToTheRight + 1 - WINNING_LINE_SIZE;
                    for (int x = j - spaceToTheLeft; x <= j - spaceToTheLeft + lineOffset; x++) { //We check for the winning line potential as many times as there are variations of winning lines passing through current point
                        lineCounter = 0;
                        for (int y = x; y < x + WINNING_LINE_SIZE; y++) {
                            if (a[i][y] == side) {
                                lineCounter++;
                            }
                            if (a[i][y] != side && a[i][y] != EMPTY){ // Combobreaker!
                                break;
                            }

                        }
                        if (lineCounter == WINNING_LINE_SIZE-1){
                            square[0]=1;
                            square[1]=i;
                            square[2]=j;
                            return square;
                        }
                    }
                    spaceAbove = i;
                    spaceBelow = BOARD_DIMENSION - 1 - i;
                    if (spaceAbove > WINNING_LINE_SIZE - 1) {
                        spaceAbove = WINNING_LINE_SIZE - 1;
                    }
                    if (spaceBelow > WINNING_LINE_SIZE - 1) {
                        spaceBelow = WINNING_LINE_SIZE - 1;
                    }
                    lineOffset = spaceAbove + spaceBelow + 1 - WINNING_LINE_SIZE;
                    for (int x = i - spaceAbove; x <= i - spaceAbove + lineOffset; x++) { //We check for the winning line potential as many times as there are variations of winning lines passing through current point
                        lineCounter = 0;
                        for (int y = x; y < x + WINNING_LINE_SIZE; y++) {

                            if (a[y][j] == side) {
                                lineCounter++;
                            }
                            if (a[y][j] != side && a[y][j] != EMPTY){ // Combobreaker!
                                break;
                            }
                        }
                        if (lineCounter == WINNING_LINE_SIZE-1){
                            square[0]=1;
                            square[1]=i;
                            square[2]=j;
                            return square;
                        }
                    }
                    spaceSlashAbove = java.lang.Math.min(spaceAbove,spaceToTheRight);
                    spaceSlashBelow = java.lang.Math.min(spaceBelow,spaceToTheLeft);
                    if (spaceSlashAbove > WINNING_LINE_SIZE - 1) {
                        spaceSlashAbove = WINNING_LINE_SIZE - 1;
                    }
                    if (spaceSlashBelow > WINNING_LINE_SIZE - 1) {
                        spaceSlashBelow = WINNING_LINE_SIZE - 1;
                    }
                    lineOffset = spaceSlashAbove + spaceSlashBelow + 1 - WINNING_LINE_SIZE;
                    if(lineOffset>=0){
                        for (int x = j - spaceSlashBelow; x <= j - spaceSlashBelow + lineOffset; x++) { //We check for the winning line potential as many times as there are variations of winning lines passing through current point
                            lineCounter = 0;
                            for (int y = x; y < x + WINNING_LINE_SIZE; y++) {

                                if (a[y][y] == side) {
                                    lineCounter++;
                                }
                                if (a[y][y] != side && a[y][y] != EMPTY){ // Combobreaker!
                                    break;
                                }
                            }
                            if (lineCounter == WINNING_LINE_SIZE-1){
                                square[0]=1;
                                square[1]=i;
                                square[2]=j;
                                return square;
                            }
                        }
                    }
                    spaceBackslashAbove = java.lang.Math.min(spaceAbove,spaceToTheLeft);
                    spaceBackslashBelow = java.lang.Math.min(spaceBelow,spaceToTheRight);
                    if (spaceBackslashAbove > WINNING_LINE_SIZE - 1) {
                        spaceBackslashAbove = WINNING_LINE_SIZE - 1;
                    }
                    if (spaceBackslashBelow > WINNING_LINE_SIZE - 1) {
                        spaceBackslashBelow = WINNING_LINE_SIZE - 1;
                    }
                    lineOffset = spaceBackslashAbove + spaceBackslashBelow + 1 - WINNING_LINE_SIZE;
                    if(lineOffset>=0){
                        for (int x = i - spaceBackslashAbove; x <= i - spaceBackslashAbove + lineOffset; x++) { //We check for the winning line potential as many times as there are variations of winning lines passing through current point
                            lineCounter = 0;
                            for (int y = x; y < x + WINNING_LINE_SIZE; y++) {

                                if (a[y][y] == side) {
                                    lineCounter++;
                                }
                                if (a[y][y] != side && a[y][y] != EMPTY){ // Combobreaker!
                                    break;
                                }
                            }
                            if (lineCounter == WINNING_LINE_SIZE-1){
                                square[0]=1;
                                square[1]=i;
                                square[2]=j;
                                return square;
                            }
                        }
                    }
                }
            }
        }

        return square;*/
    }
    public static char[][] winningSquares(char[][] a, char side){ //Gets playing side as an argument and returns an array of all winning squares
        char[][] squares = new char[BOARD_DIMENSION][BOARD_DIMENSION];
        for (int i = 0; i < BOARD_DIMENSION; i++){
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                squares[i][j] = EMPTY;
            }
        }
        int lineCounter;// = 0; //
        int lineOffset;// = 0; //Difference between space potentially available for line and WINNING_LINE_SIZE
        int spaceToTheLeft, spaceToTheRight, spaceAbove, spaceBelow, spaceSlashAbove, spaceSlashBelow,spaceBackslashAbove, spaceBackslashBelow;

        for (int i=0;i<BOARD_DIMENSION;i++) {
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                //positionValues[i][j] = 0;
                if (a[i][j]==EMPTY) {
                    spaceToTheLeft = j;
                    spaceToTheRight = BOARD_DIMENSION - 1 - j;
                    if (spaceToTheLeft > WINNING_LINE_SIZE - 1) {
                        spaceToTheLeft = WINNING_LINE_SIZE - 1;
                    }
                    if (spaceToTheRight > WINNING_LINE_SIZE - 1) {
                        spaceToTheRight = WINNING_LINE_SIZE - 1;
                    }
                    lineOffset = spaceToTheLeft + spaceToTheRight + 1 - WINNING_LINE_SIZE;
                    for (int x = j - spaceToTheLeft; x <= j - spaceToTheLeft + lineOffset; x++) { //We check for the winning line potential as many times as there are variations of winning lines passing through current point
                        lineCounter = 0;
                        for (int y = x; y < x + WINNING_LINE_SIZE; y++) {
                            if (a[i][y] == side) {
                                lineCounter++;
                            }
                            if (a[i][y] != side && a[i][y] != EMPTY){ // Combobreaker!
                                break;
                            }

                        }
                        if (lineCounter == WINNING_LINE_SIZE-1){
                            squares[i][j]=side;

                        }
                    }
                    spaceAbove = i;
                    spaceBelow = BOARD_DIMENSION - 1 - i;
                    if (spaceAbove > WINNING_LINE_SIZE - 1) {
                        spaceAbove = WINNING_LINE_SIZE - 1;
                    }
                    if (spaceBelow > WINNING_LINE_SIZE - 1) {
                        spaceBelow = WINNING_LINE_SIZE - 1;
                    }
                    lineOffset = spaceAbove + spaceBelow + 1 - WINNING_LINE_SIZE;
                    for (int x = i - spaceAbove; x <= i - spaceAbove + lineOffset; x++) { //We check for the winning line potential as many times as there are variations of winning lines passing through current point
                        lineCounter = 0;
                        for (int y = x; y < x + WINNING_LINE_SIZE; y++) {

                            if (a[y][j] == side) {
                                lineCounter++;
                            }
                            if (a[y][j] != side && a[y][j] != EMPTY){ // Combobreaker!
                                break;
                            }
                        }
                        if (lineCounter == WINNING_LINE_SIZE-1){
                            squares[i][j]=side;
                        }
                    }
                    spaceSlashAbove = java.lang.Math.min(spaceAbove,spaceToTheRight);
                    spaceSlashBelow = java.lang.Math.min(spaceBelow,spaceToTheLeft);
                    if (spaceSlashAbove > WINNING_LINE_SIZE - 1) {
                        spaceSlashAbove = WINNING_LINE_SIZE - 1;
                    }
                    if (spaceSlashBelow > WINNING_LINE_SIZE - 1) {
                        spaceSlashBelow = WINNING_LINE_SIZE - 1;
                    }
                    lineOffset = spaceSlashAbove + spaceSlashBelow + 1 - WINNING_LINE_SIZE;
                    if(lineOffset>=0){
                        for (int x = j - spaceSlashBelow; x <= j - spaceSlashBelow + lineOffset; x++) { //We check for the winning line potential as many times as there are variations of winning lines passing through current point
                            lineCounter = 0;
                            for (int y = x; y < x + spaceSlashBelow; y++) {
//                                System.out.println("::"+i+"::");
//                                System.out.println("::"+j+"::");
//                                System.out.println("=="+(i+spaceSlashBelow-y)+"==");
//                                System.out.println("=="+(j+y-spaceSlashBelow)+"==");

                                if (a[i+spaceSlashBelow-y][j+y-spaceSlashBelow] == side) {
                                    lineCounter++;
                                }
                                if (a[i+spaceSlashBelow-y][j+y-spaceSlashBelow] != side && a[i+spaceSlashBelow-y][j+y-spaceSlashBelow] != EMPTY){ // Combobreaker!
                                    break;
                                }
                            }
                            if (lineCounter == WINNING_LINE_SIZE-1){
                                squares[i][j]=side;
                            }
                        }
                    }
                    spaceBackslashAbove = java.lang.Math.min(spaceAbove,spaceToTheLeft);
                    spaceBackslashBelow = java.lang.Math.min(spaceBelow,spaceToTheRight);
                    if (spaceBackslashAbove > WINNING_LINE_SIZE - 1) {
                        spaceBackslashAbove = WINNING_LINE_SIZE - 1;
                    }

                    if (spaceBackslashBelow > WINNING_LINE_SIZE - 1) {
                        spaceBackslashBelow = WINNING_LINE_SIZE - 1;
                    }
                    lineOffset = spaceBackslashAbove + spaceBackslashBelow + 1 - WINNING_LINE_SIZE;
                    if(lineOffset>=0){
                        for (int x = i - spaceBackslashAbove; x <= i - spaceBackslashAbove + lineOffset; x++) { //We check for the winning line potential as many times as there are variations of winning lines passing through current point
                            lineCounter = 0;
                            for (int y = x; y < x + spaceBackslashAbove; y++) {

                                if (a[y][y] == side) {
                                    lineCounter++;
                                }
                                if (a[y][y] != side && a[y][y] != EMPTY){ // Combobreaker!
                                    break;
                                }
                            }
                            if (lineCounter == WINNING_LINE_SIZE-1){
                                squares[i][j]=side;
                            }
                        }
                    }
                }
            }
        }
        return squares;
    }

    public static boolean isCorner(int i,int j){ // Determines offset based "logical" corner
        int globalOffset = BOARD_DIMENSION - WINNING_LINE_SIZE;
        if ((i==0+globalOffset && j==0+globalOffset)
                || (i==0+globalOffset && j==BOARD_DIMENSION-1-globalOffset)
                || (i==BOARD_DIMENSION-1-globalOffset && j==0+globalOffset)
                ||(i==BOARD_DIMENSION-1-globalOffset && j==BOARD_DIMENSION-1-globalOffset)){
            return true;
        }
        return false;
    }

    public static int[] bestPosition(char[][] a){
        int positionWithTotalMaxValue[] = new int[]{0,0,0}; //MaxValue, i(raw), j(column)

        if(BOARD_DIMENSION%2!=0 && a[(int)Math.floor(BOARD_DIMENSION/2)][(int)Math.floor(BOARD_DIMENSION/2)]==EMPTY){
            //Check if Center exists and is available
            positionWithTotalMaxValue[0]=99; // 99 is considered absolute maximum here
            positionWithTotalMaxValue[1]=(int)Math.floor(BOARD_DIMENSION/2);
            positionWithTotalMaxValue[2]=(int)Math.floor(BOARD_DIMENSION/2);
            return positionWithTotalMaxValue;
        }
        //First, check available logical corners!
        for (int i = 0; i < BOARD_DIMENSION; i++){
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                if (isCorner(i,j)&& a[i][j]==EMPTY){
                    positionWithTotalMaxValue[0]=99; // 99 is considered absolute maximum here
                    positionWithTotalMaxValue[1]=i;
                    positionWithTotalMaxValue[2]=j;
                    return positionWithTotalMaxValue;
                }
            }

        }
        //Second check best CROSS (player) values for maximum
        int[][] crossValues = positionValuesCalculator(a,CROSS);
        int[][] zeroValues = positionValuesCalculator(a,ZERO);
        int auxilaryMaximum = 0;
        for (int i = 0; i < BOARD_DIMENSION; i++){
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                if (crossValues[i][j] > positionWithTotalMaxValue[0]){
                    positionWithTotalMaxValue[0]=crossValues[i][j];

                }
            }
        }
        // from best crosses position select best zero maximum
        for (int i = 0; i < BOARD_DIMENSION; i++){
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                if (crossValues[i][j] == positionWithTotalMaxValue[0]){
                    if (zeroValues[i][j]>auxilaryMaximum){
                        auxilaryMaximum=zeroValues[i][j];
                    }

                }
            }
        }

        //Select best zero position from the best crosses positions
        for (int i = 0; i < BOARD_DIMENSION; i++){
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                if (crossValues[i][j] == positionWithTotalMaxValue[0]){
                    if (zeroValues[i][j]==auxilaryMaximum){
                        positionWithTotalMaxValue[1]=i;
                        positionWithTotalMaxValue[2]=j;
                        return positionWithTotalMaxValue;
                    }

                }
            }
        }

        //int[][] zeroValues = positionValuesCalculator(a,ZERO);

        //int[][] totalValues = new int[BOARD_DIMENSION][BOARD_DIMENSION];
//
//        for (int i = 0; i < BOARD_DIMENSION; i++){
//            for (int j = 0; j < BOARD_DIMENSION; j++) {
//                //totalValues[i][j]=crossValues[i][j]+zeroValues[i][j];
//                if (crossValues[i][j]+zeroValues[i][j]>positionWithTotalMaxValue[0] || ((crossValues[i][j]+zeroValues[i][j]==positionWithTotalMaxValue[0]) && (isCorner(i,j)))){
//                    positionWithTotalMaxValue[0]=crossValues[i][j]+zeroValues[i][j];
//                    positionWithTotalMaxValue[1]=i;
//                    positionWithTotalMaxValue[2]=j;
//                }
//            }
//        }

//        for (int i = 0; i < BOARD_DIMENSION; i++){
//            for (int j = 0; j < BOARD_DIMENSION; j++) {
//                //totalValues[i][j]=crossValues[i][j]+zeroValues[i][j];
//                if (crossValues[i][j]>positionWithTotalMaxValue[0] || ((crossValues[i][j]==positionWithTotalMaxValue[0]) && (isCorner(i,j)))){
//                    positionWithTotalMaxValue[0]=crossValues[i][j];
//                    positionWithTotalMaxValue[1]=i;
//                    positionWithTotalMaxValue[2]=j;
//                }
//            }
//
//        }
//

        //positionValuesPrint(totalValues);
//        for (int i = 0; i < BOARD_DIMENSION; i++){
//            for (int j = 0; j < BOARD_DIMENSION; j++) {
//
//            }
//        }
        return positionWithTotalMaxValue;

    }

    public static void aiTurn (char[][] a){
//        do {
//            int i = (int)Math.floor(Math.random()*BOARD_DIMENSION);
//            int j = (int)Math.floor(Math.random()*BOARD_DIMENSION);
//            if (a[i][j]==EMPTY){
//                a[i][j]=ZERO;
//                break;
//            }
//        } while (true);
        int [] positionWithTotalMaxValue = bestPosition(a);
        int [] winningPositionPlayer = winningSquare(gameBoard,CROSS);
        int [] winningPositionAI = winningSquare(gameBoard,ZERO);

//        System.out.println("Position values for "+CROSS);
//        positionValuesPrint(positionValuesCalculator(gameBoard,CROSS));
//        System.out.println("Position values for "+ZERO);
//        positionValuesPrint(positionValuesCalculator(gameBoard,ZERO));
//        System.out.println(Arrays.toString(bestPosition(gameBoard)));
//        System.out.println(Arrays.toString(positionWithTotalMaxValue));
//        System.out.println(Arrays.toString(winningPositionAI));
//        System.out.println(Arrays.toString(winningPositionPlayer));

        if(winningPositionAI[0]==1){ //Check if AI can win
            a[winningPositionAI[1]][winningPositionAI[2]] = ZERO;
            return;
        }

        if(winningPositionPlayer[0]==1){ //Check if player can win
            a[winningPositionPlayer[1]][winningPositionPlayer[2]] = ZERO;
            return;
        }



        if (positionWithTotalMaxValue[0]>0){
            //Check if player can fork us on this position (i,j)
            char[][] shadowBoard = new char[BOARD_DIMENSION][BOARD_DIMENSION];
            for (int i = 0; i < BOARD_DIMENSION; i++){
                for (int j = 0; j < BOARD_DIMENSION; j++) {
                    shadowBoard[i][j]=a[i][j];
                }
            }
            shadowBoard[positionWithTotalMaxValue[1]][positionWithTotalMaxValue[2]]=ZERO;
            //boardPrint(shadowBoard);
            for (int i = 0; i < BOARD_DIMENSION; i++){
                for (int j = 0; j < BOARD_DIMENSION; j++) {
                    if(shadowBoard[i][j]==EMPTY){
                        shadowBoard[i][j]=CROSS;
                        char[][] shadowWinningSquares = winningSquares(shadowBoard,CROSS);
//                        System.out.println(">ShadowWinningSquares>");
//                        boardPrint(shadowWinningSquares);
//                        System.out.println(">ShadowBoard>");
//                        boardPrint(shadowBoard);
                        int shadowWinningSquaresCounter = 0;
                        for (int x = 0; x < BOARD_DIMENSION; x++){
                            for (int y = 0; y < BOARD_DIMENSION; y++) {
                                if(shadowWinningSquares[x][y]==CROSS){
                                    shadowWinningSquaresCounter++;
                                }
                            }
                        }
                        int[] testValue = winningSquare(shadowBoard,ZERO); //Check if we can win on the turn of potential fork
                        if (shadowWinningSquaresCounter>1 && testValue[0]==0){ // Yes! We can be forked on this position and we need to block it!
                            System.out.println("Yes! We can be forked on this position!");
                            a[i][j]=ZERO;
                            return;
                        }
                        shadowBoard[i][j]=EMPTY;
                    }
                }
            }
            a[positionWithTotalMaxValue[1]][positionWithTotalMaxValue[2]]=ZERO;
            return;
        } else { // If nothing else matters take first empty position
            for (int i = 0; i < BOARD_DIMENSION; i++){
                for (int j = 0; j < BOARD_DIMENSION; j++) {
                    if (a[i][j]==EMPTY) {
                        a[i][j] = ZERO;
                        return;
                    }
                }
            }
        }
    }

    public static void dumbPlayerTurn(char[][] a){
        do {
            int i = (int)Math.floor(Math.random()*BOARD_DIMENSION);
            int j = (int)Math.floor(Math.random()*BOARD_DIMENSION);
            if (a[i][j]==EMPTY){
                a[i][j]=CROSS;
                break;
            }
        } while (true);
    }

    public static int boardEmptySquares(char[][] a){
        int emptySquares = 0;
        for (int i = 0; i < BOARD_DIMENSION; i++){
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                if (a[i][j]==EMPTY){
                    emptySquares++;
                }
            }
        }
        return emptySquares;
    }
    public static char checkVictory(char[][] a){ // return EMPTY - no victory, CROSS - crosses win, ZERO - zeroes win
        int lineCounter=0;
        for (int i = 0; i < BOARD_DIMENSION; i++){
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                // Check that we are not out of range of the board to the right bottom and borh diagonals, than check those directions, else - return EMPTY
                if(i+WINNING_LINE_SIZE<=BOARD_DIMENSION) { //Check line to the bottom of position
                    if (a[i][j]==CROSS){
                        lineCounter++;
                        for(int x=1;x<WINNING_LINE_SIZE;x++){
                            if (a[i+x][j]==CROSS){
                            lineCounter++;
                            }
                        }
                        if(lineCounter>=WINNING_LINE_SIZE){
                            return CROSS;
                        }
                        lineCounter=0;
                    } else if (a[i][j]==ZERO){
                        lineCounter++;
                        for(int x=1;x<WINNING_LINE_SIZE;x++){
                            if (a[i+x][j]==ZERO){
                                lineCounter++;
                            }
                        }
                        if(lineCounter>=WINNING_LINE_SIZE){
                            return ZERO;
                        }
                        lineCounter=0;
                    }
                }
                if (j+WINNING_LINE_SIZE<=BOARD_DIMENSION){ //Check line to the right
                    if (a[i][j]==CROSS){
                        lineCounter++;
                        for(int x=1;x<WINNING_LINE_SIZE;x++){
                            if (a[i][j+x]==CROSS){
                                lineCounter++;
                            }
                        }
                        if(lineCounter>=WINNING_LINE_SIZE){
                            return CROSS;
                        }
                        lineCounter=0;
                    } else if (a[i][j]==ZERO){
                        lineCounter++;
                        for(int x=1;x<WINNING_LINE_SIZE;x++){
                            if (a[i][j+x]==ZERO){
                                lineCounter++;
                            }
                        }
                        if(lineCounter>=WINNING_LINE_SIZE){
                            return ZERO;
                        }
                        lineCounter=0;
                    }
                }
                if (i+WINNING_LINE_SIZE<=BOARD_DIMENSION && j+WINNING_LINE_SIZE<=BOARD_DIMENSION){ // Check right diagonal
                    if (a[i][j]==CROSS){
                        lineCounter++;
                        for(int x=1;x<WINNING_LINE_SIZE;x++){
                            if (a[i+x][j+x]==CROSS){
                                lineCounter++;
                            }
                        }
                        if(lineCounter>=WINNING_LINE_SIZE){
                            return CROSS;
                        }
                        lineCounter=0;
                    } else if (a[i][j]==ZERO){
                        lineCounter++;
                        for(int x=1;x<WINNING_LINE_SIZE;x++){
                            if (a[i+x][j+x]==ZERO){
                                lineCounter++;
                            }
                        }
                        if(lineCounter>=WINNING_LINE_SIZE){
                            return ZERO;
                        }
                        lineCounter=0;
                    }
                }
                if (i+WINNING_LINE_SIZE<=BOARD_DIMENSION && j-WINNING_LINE_SIZE+1>=0){ //Check left diagonal
                    if (a[i][j]==CROSS){
                        lineCounter++;
                        for(int x=1;x<WINNING_LINE_SIZE;x++){
                            if (a[i+x][j-x]==CROSS){
                                lineCounter++;
                            }
                        }
                        if(lineCounter>=WINNING_LINE_SIZE){
                            return CROSS;
                        }
                        lineCounter=0;
                    } else if (a[i][j]==ZERO){
                        lineCounter++;
                        for(int x=1;x<WINNING_LINE_SIZE;x++){
                            if (a[i+x][j-x]==ZERO){
                                lineCounter++;
                            }
                        }
                        if(lineCounter>=WINNING_LINE_SIZE){
                            return ZERO;
                        }
                        lineCounter=0;
                    }
                }
            }
        }
        return EMPTY;
    }

    public static void main(String[] args) {
        boardReset(gameBoard);
//        gameBoard[0][2] = ZERO;
//        gameBoard[2][2] = CROSS;
//        gameBoard[1][1] = CROSS;
//        boardPrint(gameBoard);
//        boardPrint(winningSquares(gameBoard,CROSS));

        //gameBoard[0][1] = ZERO;
//        boardPrint(gameBoard);
//        System.out.println("Position values for "+CROSS);
//        positionValuesPrint(positionValuesCalculator(gameBoard,CROSS));
//        System.out.println("Position values for "+ZERO);
//        positionValuesPrint(positionValuesCalculator(gameBoard,ZERO));
//        System.out.println(Arrays.toString(bestPosition(gameBoard)));

        do {
            if (boardEmptySquares(gameBoard)>0 && checkVictory(gameBoard)==EMPTY) { // Player turn
                boardPrint(gameBoard);
//                System.out.println("Position values for "+CROSS);
//                positionValuesPrint(positionValuesCalculator(gameBoard,CROSS));
//                System.out.println("Position values for "+ZERO);
//                positionValuesPrint(positionValuesCalculator(gameBoard,ZERO));
//                System.out.println(Arrays.toString(bestPosition(gameBoard)));
                playerTurn(gameBoard);
            } else {
                boardPrint(gameBoard);
//                System.out.println("Position values for "+CROSS);
//                positionValuesPrint(positionValuesCalculator(gameBoard,CROSS));
//                System.out.println("Position values for "+ZERO);
//                positionValuesPrint(positionValuesCalculator(gameBoard,ZERO));
                System.out.println("GAME OVER!!!");
                switch (checkVictory(gameBoard)){
                    case ZERO: System.out.println("AI wins!"); break;
                    case CROSS: System.out.println("Player wins!"); break;
                    case EMPTY: System.out.println("Draw!"); break;
                }
                break;
            }
            if (boardEmptySquares(gameBoard)>0 && checkVictory(gameBoard)==EMPTY) { // AI Turn
                aiTurn(gameBoard);
            } else {
                boardPrint(gameBoard);
//                System.out.println("Position values for "+CROSS);
//                positionValuesPrint(positionValuesCalculator(gameBoard,CROSS));
//                System.out.println("Position values for "+ZERO);
//                positionValuesPrint(positionValuesCalculator(gameBoard,ZERO));
                System.out.println("GAME OVER!!!");
                switch (checkVictory(gameBoard)){
                    case ZERO: System.out.println("AI wins!"); break;
                    case CROSS: System.out.println("Player wins!"); break;
                    case EMPTY: System.out.println("Draw!"); break;
                }
                break;
            }
        } while(true);

    }
}
