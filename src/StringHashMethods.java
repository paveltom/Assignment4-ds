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
        BTree<Integer> btree = new BTree<>(3);
        btree.insert(10);
        btree.insert(25);
        btree.insert(40);
        btree.insert(60);
        btree.toString();


    }
}