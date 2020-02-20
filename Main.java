import java.io.*;
import java.util.Scanner;
public class Main {
   public static void main(String[] args) throws IOException {
     //initializes arraySort();
     arraySort();
   }
   public static void arraySort() throws IOException {
     File file = new File("US.csv");
     Scanner sc = new Scanner(file);
     //skips the first 2 lines of the csv file bescase they are no important to the list
     sc.nextLine();
     sc.nextLine();
     //this figures out out the size of the list to create the appropriately sized array 
     int arraySize = 0;
     while (sc.hasNext()) {
       arraySize++;
       sc.nextLine();
     }
     //creates the array
     String[][] arr = new String[arraySize][2];
     sc = new Scanner(file);
     //skips the first 2 lines of the csv file bescase they are no important to the list
     sc.nextLine();
     sc.nextLine();
     //this works backwards with the csv file and begins with the URL first then goes 
     //on to the number of streams and then the artist's name
     for (int i=0; i<arraySize; i++) {
       arr[i][0] = sc.nextLine();
       //removes the URL from the list
       arr[i][0] = arr[i][0].substring(0, arr[i][0].lastIndexOf(",")-1);
       //finds the index of where the artists name ends
       int end = arr[i][0].lastIndexOf(",", arr[i][0].lastIndexOf(","));
       //finds the index of where the artists name begins
       int start = arr[i][0].lastIndexOf(",", end-1)+1;
       //Isolates the artists name in the array unsing the begining and end index integers
       arr[i][0] = arr[i][0].substring(start, end);
     }
     sc.close();
     //this is meant to removes duplictaes of an artist's name and counts how many times
     //they appear in the array
     for (int i=0; i<arr.length; i++) {
       //count is meant to keep track of how mant times the artist's name appears in the array
       //count begins with 1 since an artis's name appears in the array at least once
       int count = 1;      
       for (int j=i+1; j<arr.length; j++) {
         if (arr[i][0].equals(arr[j][0])) {
           count++;
           //once the for loop detects that there is a duplicate of the artist's name
           //it creates a new array and coppies all the index entries piror to the duplicate
           //and the coppies all the index enteries after the duplicate but not the duplicate itself
           String[][] temp = new String[arr.length-1][2];
           for (int k=0; k<j; k++) {
             temp[k][0] = arr[k][0];
             temp[k][1] = arr[k][1];
           }
           for (int k=j+1; k<arr.length; k++) {
             temp[k-1][0] = arr[k][0];
             temp[k-1][1] = arr[k][1];
           }
           //after the duplicate is removed the temp array becomes the current array
           arr = temp;
           j-=1;
         }
       }
       //turns count which is an int into a string and imports it into the second
       //coresponding dimention of the array 
       arr[i][1] = String.valueOf(count);
     }
     //this is mean to alphabetize the list
     //temp1 is created for the artists name while temp2 is created for the number of time their name
     //appears in the array. String[][] temp and temp3 are meant for comparing lowercase name to uppercase
     //without affecting the original array
     String temp1 = "";
     String temp2 = ""; 
     String temp3 = "";
     String[][] temp = arr;
     for (int i=0; i<arr.length; i++) {
       for (int j=0; j<arr.length; j++) {
         if (((temp[i][0].toLowerCase()).compareTo((temp[j][0].toLowerCase())))<0) {
           temp1 = arr[i][0];
           temp2 = arr[i][1];
           temp3 = temp[i][0];
           arr[i][0] = arr[j][0];
           arr[i][1] = arr[j][1];
           temp[i][0] = temp[j][0];
           arr[j][0] = temp1;
           arr[j][1] = temp2;
           temp[j][0] = temp3;
         }
       }
     }
     TopStreamingArtists artistNames = new TopStreamingArtists();
     //inserts the names of the artists and the number of times they apear on the list into the linked list
     for (int i=0; i<arr.length; i++)
       artistNames.insert(arr[i][0], arr[i][1]);
     //prints the Artists names in alphabetical order to an output file
     artistNames.displayList();
   }
}
//this set of classes creates the likned list, it's pretty much like the linked list we 
//created in class
class Artist {
  String artistName;
  String num;
  Artist next;
  Artist prev;

  Artist(String name, String numOfApperences) {
    artistName = name;
    num = numOfApperences;
  }
  public String displayArtist() {    
    return (artistName + "  " + num);
  }
}
class TopStreamingArtists {
  private Artist first;
  private Artist last;
  
  TopStreamingArtists(){
    first = null;
    last = null;
  }
  public boolean isEmpty(){
    return (first == null);
  }
  public void insert(String name, String num) throws IOException {
    Artist newNode = new Artist(name, num);
    if (isEmpty())
      first = newNode;
    else{
      last.next = newNode;
      newNode.prev = last;
    }
    last = newNode;
  }
  //uses a PrintWriter to write to an output text file
  public void displayList()throws IOException {
    PrintWriter output = new PrintWriter("US Sorted.txt");
    Artist current = first;
    while(current != null){
      output.println(current.displayArtist());
      current = current.next;
    }
    output.close();
  }
}
