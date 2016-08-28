package docparse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/* docParse - Java Utility application to search through document(s) in order to find white-spaced information 
 * 			quickly and place the data into an array to be later used in an output document. The searches can be
 * 			detailed to find information followed by keywords and quoted information. 
 */

public class DocParse 
{
    static ArrayList<String> buffer = new ArrayList<String>();
    static ArrayList<String> array = new ArrayList<String>();
	
    public static void main(String[] args) 
    {
        init();
        parse();	
        System.out.println("Done");
    }
	
    public static void init()
    {
        /* Loading the input file into an arraylist buffer */
	try 
	{
            File file = new File("input.txt");
            Scanner scan = new Scanner(new FileInputStream(file));
            while(scan.hasNext())
            {
                buffer.add(scan.next());   
            }
            scan.close();
        }
	catch (FileNotFoundException e)
        {
            e.printStackTrace();
	}
    }
	
    public static void parse()
    {
        /* Initialized Variables */
	int i = 0;
        int start = 0;
        int end = 0;
        boolean quote = false;
        boolean keyword = false;
        boolean done = false;
        char c = '0';
        String str = "";
        
        /* Parsing the input file */
	while(i < buffer.size())
	{
            c = buffer.get(i).charAt(0);
            str = String.valueOf(c);
            if(str.equals("\"") && quote == false)
            {
                start = i;
            }
            c = buffer.get(i).charAt(buffer.get(i).length() - 1);
            str = String.valueOf(c);
            if(str.equals("\"") && quote == false)
            {
                end = i;
                quote = true;
                str = "";
                for(int j = start; j <= end; j++)
                {
                    str = str + buffer.get(j) + " ";
                }
                array.add(str);
                str = "";
            }
                        
            if(buffer.get(i).equalsIgnoreCase("doi:"))
            {
                array.add(buffer.get(i + 1));
            }
            
            if(buffer.get(i).equalsIgnoreCase("Keywords:") && keyword == false)
            {
                start = i + 1;
            }

            if(buffer.get(i).equalsIgnoreCase("URL:") && keyword == false)
            {
                end = i - 1;
                keyword = true;
                str = "";
                for(i = start; i <= end; i++)
                {
                    str = str + buffer.get(i) + " ";
                }
                array.add(str);
                str = "";
            }
           
            if(buffer.get(i).equalsIgnoreCase("URL:"))
            {
                array.add(buffer.get(i + 1));
                quote = false;
                keyword = false;
            }
            i++;
	}
        
        /* Creating and Writing to an output file */
        try 
        {
            File file = new File("output.txt");

            FileWriter out = new FileWriter(file);
            i = 0;
            while(i < array.size())
            {
                out.write("Title: " + array.get(i) + System.getProperty("line.separator"));
                if(i + 1 < array.size())
                {
                    out.write("DOI: " + array.get(i + 1) + System.getProperty("line.separator"));    
                }
                if(i + 2 < array.size())
                {
                    out.write("Keywords: " + array.get(i + 2) + System.getProperty("line.separator"));
                }
                if(i + 3 < array.size())
                {
                    out.write("URL: " + array.get(i + 3) + System.getProperty("line.separator"));
                }
                out.write(System.getProperty("line.separator"));
                i = i + 4;
            }
            out.close();

        }
        catch (IOException e) 
        {
	    e.printStackTrace();
        }


    } 
}

