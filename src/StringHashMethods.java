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
        BTree<Integer> generatedTree = new BTree<Integer>(2);
        generatedTree.insert(97);
        generatedTree.insert(73);
        generatedTree.insert(75);
        generatedTree.insert(76);
        generatedTree.insert(56);
        generatedTree.insert(27);
        generatedTree.insert(21);
        generatedTree.insert(57);
        generatedTree.insert(20);
        generatedTree.insert(80);
        generatedTree.insert(50);
        generatedTree.insert(92);
        generatedTree.insert(36);
        generatedTree.insert(89);
        generatedTree.insert(68);
        generatedTree.insert(88);
        generatedTree.insert(41);
        generatedTree.insert(79);
        generatedTree.insert(60);
        generatedTree.insert(61);
        generatedTree.insert(55);
        System.out.println(generatedTree.toString());
        generatedTree.delete(56);
        System.out.println(generatedTree.toString());
    }
}