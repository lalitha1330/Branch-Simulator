
package Branchism;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

public class BranchPredict {
//m is the value of m in (m,n)correlating predictor
static int m;

//n is the value of n in (m,n) correlating predictor
static int n;

//d is the number of bits of the PC to index into the Branch History Table
static int d;

static long x;
//s is the string of m bits which acts as Global branch history
static String s;
static int k=0;

//count is the variable to count the number of times the prediction is a miss
static int count=0;
static LinkedHashMap<String,Integer>[] hk;

@SuppressWarnings("unchecked")
public static void main(String[] args) throws IOException
{
	//Taking command line arguments
	String filename=args[0];
	m=Integer.parseInt(args[1]);
	n=Integer.parseInt(args[2]);
	d=Integer.parseInt(args[3]);
	x=(long) Math.pow(2,m);
	@SuppressWarnings("resource")
	BufferedReader br = new BufferedReader(new FileReader(filename));
	hk=new LinkedHashMap[(int) x];
	int a1;
	s="";
	for(a1=0;a1<m;a1++)
	{
		s=s+"0";
	}
	String line;
	
	//Reading the input file line by line and passing through the (m,n) predictor
	while ((line = br.readLine()) != null) 
	{
		 String[] tokens=line.split(" ");

		 String addr=tokens[0];
		 String actual=tokens[1];
		 int t = Integer.parseInt(addr, 16);
		 String longpc = Integer.toBinaryString(t);
		 int length=longpc.length()-d;
		 String pc=longpc.substring(length);
		 int n0=0;
		 if(n==1)
		 {
			 if(actual.equals("T"))
				 n0=1;
			 else 
				 n0=0;
		 }
		 else
		 {
			 if(actual.equals("T"))
				 n0=3;
			 else 
				 n0=0;
		 }
		 bitmnpredictor(pc,n0);
    }
//Printing the number of times the predictor predicts a miss: 
System.out.println(count);
System.out.println(k);
}

//Code for (m,n)correlating predictor:
public static void bitmnpredictor(String longpc2, int b) 
{

//For 1-bit predictor:
if(n==1)
{
	int i;
	try{
		i=Integer.parseInt(s,2);
	}
	catch(NumberFormatException e){
		i=0;
	}
	if(hk[i]==null)
		hk[i]=new LinkedHashMap<String,Integer>();
	if(!hk[i].containsKey(longpc2))
	{
		hk[i].put(longpc2, 0);
		k++;
	}
	if(b!=hk[i].get(longpc2))
    {
	count++;
	hk[i].put(longpc2,b);
    }
	if(m>0)
	s=s.substring(1)+b;
}

//For 2-bit predictor:
else
{
	int i;
	try{
		i=Integer.parseInt(s,2);
	}
	catch(NumberFormatException e){
		i=0;
	}
	if(hk[i]==null)
    hk[i]=new LinkedHashMap<String,Integer>();
	if(!hk[i].containsKey(longpc2))
	{
		hk[i].put(longpc2, 0);
		k++;
	}
	if(b!=hk[i].get(longpc2))
	{
		int z;
		z=hk[i].get(longpc2);
		switch (b){
		case 0:
			if(z==1)
			{z--;
			hk[i].put(longpc2, z);
			}
			else if(z==2)
			{count++;
			hk[i].put(longpc2, 0);
			}
			else
			{z--;
		    count++;
			hk[i].put(longpc2,z);
			}
			break;
		case 3:
			if(z==1)
			{count++;
			hk[i].put(longpc2, 3);
			}
			else if(z==2)
			{z++;
			hk[i].put(longpc2, z);
			}
			else
			{z++;
		    count++;
			hk[i].put(longpc2,z);
			}
			break;
		}
	}
	if(m>0)
	{
		if(b==3)
		s=s.substring(1)+"1";
		else
		s=s.substring(1)+"0";
	}
  }
}
}
