/**
 * Programmers: Nathan Jaggers, Storm Randolph
 * 
 * CPE 315 - Computer Architecture
 * Dr.Seng
 * Spring 2022
 * 
 */

package Lab6;

import java.util.HashMap;

public class cache {

    /****************************************************
     * Class is to organize data and simplify process of
     * simulating a cache in a processor.
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
    public boolean[][] validBits;

    public HashMap<Integer, Integer> lru; // = new HashMap<int,int>();
    // add valib bit array

    // ---METHODS---
    // constructors
    public cache() {
        // initialize members
        name = "No Name Given";
        sizeTotal = 0;
        ways = 0;
        sizeBlock = 0;
        sizeIndex = 0;
        searches = 0;
        hits = 0;
        hitRate = 0;
        tagTable = null;
        validBits = null;
        lru = new HashMap<Integer, Integer>();

    }

    public cache(String name, int totalSize, int Associativity, int blockSize) {
        // initialize members
        this.name = name;
        sizeTotal = totalSize;
        ways = Associativity;
        sizeBlock = blockSize;
        sizeIndex = totalSize / (ways * blockSize);
        searches = 0;
        hits = 0;
        hitRate = 0;
        tagTable = new int[ways][sizeIndex];
        validBits = new boolean[ways][sizeIndex];
        lru = new HashMap<>();
    }

    // search method
    public void search(int memAddress) {
        // declare varibles
        boolean found;

        // mask out index and offsets
        int byteOffset = memAddress % 4;
        int blockOffset = (memAddress / 4) % sizeBlock;
        int index = ((memAddress / 4) / sizeBlock) % (sizeIndex);
        int tag = ((memAddress / 4) / sizeBlock) / (sizeIndex);

        // go through different ways and see if the address is
        // present in cache at the index and block offset
        found = false;
        for (int way = 0; way < ways; way++) {
            if (validBits[way][index] == true) // check valid bit
            {
                // assuming valid bit is good
                //verify tag(?)
                if (tag == tagTable[way][index]) {
                    // indicate that address was found
                    found = true;

                    // increment hits
                    hits++;

                    // adjust LRU (Least Recently Used)
                    hash(tag, searches);
                    // leave?
                    // break;
                }
            }

        }

        // if address wasn't found at either way, replace empty or LRU with new data
        if (!found) {
            update(index, blockOffset, byteOffset, memAddress, tag);
        }

        // increment amount of searches
        searches++;
    }

    // update method
    public void update(int index, int blockOffset, int byteOffset, int memAddress, int tag) {
        boolean empty;

        empty = false;
        for (int way = 0; way < ways; way++) {
            // check if spot it empty
            if (tagTable[way][index] == 0) {
                // indicate empty is true
                empty = true;

                // fill spot with address
                // dataTable[way][index][blockOffset][byteOffset] = memAddress;
                tagTable[way][index] = tag;

                // flip valid bit to true
                validBits[way][index] = true;
            }

        }

        // if there are no empty spot available, replace LRU
        // least recently used
        if (!empty) {
            // getting from line numbe lru and comparing with currentLowest line number()
            // if(lru.get(tagTable[way][index])<currentLowest)
            // lowestTag = tagfrom Array
            // currentLowest = lru.get(tagTable[way][index])
            int currentLowest = 5000000;
            int lowestTag = 0;
            for (int way = 0; way < ways; way++) {
                if (lru.get(tagTable[way][index]) < currentLowest) {
                    lowestTag = tagTable[way][index];
                    currentLowest = lru.get(tagTable[way][index]);
                }

            }

            for (int way = 0; way < ways; way++) {
                // check if spot matches LRU
                // adjust stuff here
                if (tagTable[way][index] == lowestTag) {
                    // fill spot with address
                    tagTable[way][index] = tag;
                }

            }
        }

        // update hash?
        hash(tag, searches);
    }

    // least recently used hashmap method: Key: tag and value : line number
    // inputs: tag, line number
    // for given index, want to check two tags and whichever one is lesser, replace
    // that one
    public void hash(int tag, int linenum) {
        // have check for update or add
        if (lru.containsKey(tag)) {
            // update LRU value
            lru.replace(tag, linenum);

        } else {
            // add tag and line number to LRU
            lru.put(tag, linenum);
        }

    }

    // print method
    public void showSummary() {
        // Cache #1
        // Cache size: 2048B Associativity: 1 Block size: 1
        // Hits: 4028929 Hit Rate: 80.58%
        hitRate = ((float) hits / searches) * 100;

        System.out.printf("%s\nCache size: %dB Associativity: %d Block size: %d\n" +
                "Hits: %d Hit Rate: %.2f%%\n", name, sizeTotal, ways, sizeBlock, hits, hitRate);
    }
}
