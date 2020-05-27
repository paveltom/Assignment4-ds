public class StringHashMethods implements HashMethods
{
    private final long [ ] MULTIPLIERS;

    public StringHashMethods()
    {
        MULTIPLIERS = new long [ 2 ];
        generateNewFunctions( );
    }
    public int getNumberOfFunctions( )
    {
        return MULTIPLIERS.length;
    }

    public void generateNewFunctions( )
    {
    	MULTIPLIERS[0] = 2;
    	MULTIPLIERS[1] = 3;
    }

    public long hash( String input, int index )
    {
        final long multiplier = MULTIPLIERS[ index ];
        long hashVal = 0;

        for( int i = 0; i < input.length( ); i++ )
            hashVal = multiplier * hashVal + input.charAt( i );

        return hashVal;
    }

    public static void main(String[] args) {
        BTree<Integer> btree = new BTree<>(2);
        btree.insert(10);
        btree.insert(25);
        btree.insert(40);
        btree.insert(60);
        btree.insert(15);
        btree.insert(20);
        btree.insert(30);
        btree.insert(35);
        btree.delete(20);
        btree.delete(35);
        btree.delete(15);
        btree.delete(25);
        btree.insert(45);
        btree.insert(55);
        btree.insert(17);
        btree.insert(5);
        btree.insert(65);
        btree.insert(2);
        btree.delete(5);
        btree.delete(65);
        btree.delete(45);




//        btree.insert(35);
//        btree.insert(35);



        System.out.println(btree.toString());


    }
}