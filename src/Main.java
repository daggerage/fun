import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner s=new Scanner(System.in);
        while (true){
            String input=s.nextLine();
            if(!input.equals("quit")) {
                if(input.length()==6){
                    long startTime=System.currentTimeMillis();

                    WordPuzzleSix wps = new WordPuzzleSix(input);
                    wps.findAllWords();

                    long endTime=System.currentTimeMillis();
                    long timeCost=endTime-startTime;
                    System.out.println("time cost : "+timeCost+" ms");
                }
                else{
                    System.out.println("number of characters less than 3!");
                }
            }
            else{
                break;
            }
        }

        s.close();

    }
}

