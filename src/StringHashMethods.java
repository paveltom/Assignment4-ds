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
        generatedTree.insert(11);
        generatedTree.insert(24);
        generatedTree.insert(34);
        generatedTree.insert(40);
        generatedTree.insert(10);
        generatedTree.insert(9);
        generatedTree.insert(41);
        generatedTree.insert(8);
        generatedTree.insert(42);
        generatedTree.insert(64);
        generatedTree.insert(70);
        generatedTree.insert(80);
        generatedTree.insert(90);
        generatedTree.insert(35);

        System.out.println(generatedTree.toString());

        generatedTree.delete(40);
        generatedTree.delete(42);
//        generatedTree.delete(40);
//        generatedTree.delete(64);
//        generatedTree.delete(34);

        System.out.println(generatedTree.toString());

        generatedTree.insert(12);
        generatedTree.delete(24);
        generatedTree.delete(12);
        generatedTree.delete(11);



        System.out.println(generatedTree.toString());









        /*
        1. leaf:
           a. minimal leaf
             * both bros minimal - VVVVVVVV
             * only one bro minimal - VVVVVVV
             * both bros ok - VVVVVVVVV
             * bros and parent minimal - VVVVVVV

           b. inner node
             * has pred: pred is minimal / non-minimal - VVVVVV
             * doesn't have pred: succ is minimal / non-minimal - VVVVVVVVVV
             * both pred and succ are minimal - VVVVVVVVVVVV
             * both pred and succ are minimal and both childs are minimal - VVVVVVVVVVVVVV
         */
    }
}