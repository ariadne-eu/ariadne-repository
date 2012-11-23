package net.sourceforge.minor.lucene.core.utils;

import java.io.Serializable;
import java.util.Random;

public class RandomStringGenerator implements Serializable{

	private static Random rn = new Random();
 		
	private static int rand(int lo, int hi)
    {
            int n = hi - lo + 1;
            int i = rn.nextInt() % n;
            if (i < 0)
                    i = -i;
            return lo + i;
    }

    public static String randomString(int size)
    {          
    		if (size < 0){
    			return "";
    		}
    	
            if( size < 8 ){
            	return randomSmallString(size);
            }else if (size < 20){
            	return randomMediumString(size);
            }else{
            	return randomLargeString(size);
            }
    }
		
    
    private static String randomSmallString(int size)
    {          
            byte b[] = new byte[size];
            
            for (int i = 0; i < (size/2); i++)
                b[i] = (byte)rand('a', 'z');
            
            for (int i = (size/2); i < size; i++)
                b[i] = (byte)rand('0', '9');
                    
            return new String(b);
    }
    
    private static String randomMediumString(int size)
    {          
            byte b[] = new byte[size];
                       
            for (int i = 0; i < (size/4); i++)
                b[i] = (byte)rand('a', 'z');
            
            for (int i = (size/4); i < (size/2); i++)
                b[i] = (byte)rand('0', '9');
            
            for (int i = (size/2); i < (3*size/4); i++)
                b[i] = (byte)rand('a', 'z');
            
            for (int i = (3*size/4); i < size; i++)
                b[i] = (byte)rand('0', '9');
            
            return new String(b);
    }
    
    private static String randomLargeString(int size)
    {          
            byte b[] = new byte[size];
                        
            for (int i = 0; i < b.length; i++) {
				int j = rn.nextInt();
                
				if (j % 2 == 0){
					b[i] = (byte)rand('a', 'z');
				}else{
					//b[i] = (byte)rand('0', '9');
					b[i] = (byte)rand('a', 'z');
				}            	
			}
             
            return new String(b);
    }
	
	
}
