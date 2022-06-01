/**
 * Programmers: Nathan Jaggers, Storm Randolph
 * 
 * CPE 315 - Computer Architecture
 * Dr.Seng
 * Spring 2022
 * 
 */

package Lab6;

public class cache {

    /****************************************************
     * Class is to organize data and simplify process of
     * predicting branches as correlating branch predictor
     * for assembly programs.
     ****************************************************/

    // ---MEMBERS---
    public String name;
    public int sizeTotal;
    public int ways;
    public int sizeBlock;
    public int sizeIndex;
    public int searches; 
    public int hits;
    public float hitRate;
    public int[][] tagTable;
    public int[][][][] dataTable;

    // ---METHODS---
    // constructors
    public cache()
    {
        //initialize members
        name = "No Name Given";
        sizeTotal = 0;
        ways = 0;
        sizeBlock = 0;
        sizeIndex = 0;
        searches = 0;
        hits = 0;
        hitRate = 0;
        tagTable = null;
        dataTable = null;

    }

    public cache(String name, int totalSize, int Associativity, int blockSize)
    {
        //initialize members
        this.name = name;
        sizeTotal = totalSize;
        ways = Associativity;
        sizeBlock = blockSize;
        sizeIndex = totalSize/(ways*blockSize);
        searches = 0;
        hits = 0;
        hitRate = 0;
        tagTable = new int[ways][sizeIndex];
        dataTable = new int[ways][sizeIndex][sizeBlock][4];
    }

    // search method
    public void search(int memAddress)
    {
        //declare varibles
        boolean found, empty;

        //mask out index and offsets
        int byteOffset = memAddress % 4;
        int blockOffset = (memAddress/4) % sizeBlock;
        int index = ((memAddress/4)/sizeBlock) % (sizeIndex);
        int tag = ((memAddress/4)/sizeBlock)/(sizeIndex);

        //go through different ways and see if the address is 
        //present in cache at the index and block offset
        found = false;
        for(int way = 0; way < ways; way++)
        {
            if (tagTable[way][index] != 0) //pesudo valid bit
            {
                if ((tag == tagTable[way][index]) && (memAddress == dataTable[way][index][blockOffset][byteOffset]))
                {
                    //indicate that address was found
                    found = true;
                    
                    //increment hits
                    hits++;
    
                    //adjust LRU (Least Recently Used)
    
                    //leave
                    //break;
                }
            }

        }

        //if address wasn't found at either way, replace empty or LRU with new data
        if (!found)
        {
            empty = false;
            for(int way = 0; way < ways; way++)
            {
                //check if spot it empty
                if (dataTable[way][index][blockOffset][byteOffset] == 0)
                {
                    //indicate empty is true
                    empty = true;

                    //fill spot with address
                    dataTable[way][index][blockOffset][byteOffset] = memAddress;
                    tagTable[way][index] = tag;
                }
    
            }
            
            //if there are no empty spot available, replace LRU
            if (!empty)
            {
                for(int way = 0; way < ways; way++)
                {
                    //check if spot matches LRU
                    //adjust stuff here
                    //if (dataTable[way][index][blockOffset][byteOffset] == 0)
                    {
                        //fill spot with address
                        dataTable[way][index][blockOffset][byteOffset] = memAddress;
                        tagTable[way][index] = tag;
                    }
        
                }
            }
        }

        //increment amount of searches
        searches++; 
    }

    // update method

    // print method
    public void showSummary()
    {
        //Cache #1
        //Cache size: 2048B	Associativity: 1	Block size: 1
        //Hits: 4028929	Hit Rate: 80.58%
        hitRate = ((float)hits/searches) * 100;

        System.out.printf("%s\nCache size: %dB Associativity: %d Block size: %d\n"+
                          "Hits: %d Hit Rate: %.2f%%\n", name, sizeTotal, ways, sizeBlock, hits, hitRate);
    }
}
