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
    public int hits;
    public int searches; 
    public float hitRate;

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
        hits = 0;
        searches = 0;
        hitRate = 0;

    }

    public cache(String name, int totalSize, int Associativity, int blockSize)
    {
        //initialize members
        this.name = name;
        sizeTotal = totalSize;
        ways = Associativity;
        sizeBlock = blockSize;
        sizeIndex = totalSize/(ways*blockSize);
        hits = 0;
        searches = 0;
        hitRate = 0;

    }

    // search method
    public void search(int memAddress)
    {
        int byteOffset = memAddress % 4;
        int blockOffset = (memAddress/4) % sizeBlock;
        int index = ((memAddress/4)/sizeBlock) % (sizeIndex);


        searches++; 
    }

    // update method

    // print method
    public void showSummary()
    {
        //Cache #1
        //Cache size: 2048B	Associativity: 1	Block size: 1
        //Hits: 4028929	Hit Rate: 80.58%

        System.out.printf("%s\nCache size: %dB Associativity: %d Block size: %d\n"+
                          "Hits: %d Hit Rate: %.2f%%\n", name, sizeTotal, ways, sizeBlock, hits, hitRate);
    }
}
