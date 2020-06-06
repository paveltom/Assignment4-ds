import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

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

    public static void  print (BTree<Integer> generatedTree) throws IOException {
        System.out.println(generatedTree.toString());
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.readLine();
    }

    public static void main(String[] args) throws IOException {
        BTree<Integer> generatedTree = new BTree<Integer>(2);
        generatedTree.insert2pass(50);
        print(generatedTree);
        generatedTree.delete(50);
        print(generatedTree);

        generatedTree.insert2pass(51);
        print(generatedTree);

        generatedTree.insert2pass(49);
        print(generatedTree);

        generatedTree.insert2pass(52);
        print(generatedTree);

        generatedTree.insert2pass(48);
        print(generatedTree);

        generatedTree.insert2pass(53);
        print(generatedTree);

        generatedTree.insert2pass(47);
        print(generatedTree);

        generatedTree.insert2pass(54);
        print(generatedTree);

        generatedTree.insert2pass(46);
        print(generatedTree);

        generatedTree.insert2pass(55);
        print(generatedTree);

        generatedTree.insert2pass(45);
        print(generatedTree);

        generatedTree.insert2pass(56);
        print(generatedTree);

        generatedTree.insert2pass(44);
        print(generatedTree);

        generatedTree.insert2pass(57);
        print(generatedTree);

        generatedTree.insert2pass(43);
        print(generatedTree);

        generatedTree.insert2pass(58);
        print(generatedTree);

        generatedTree.insert2pass(42);
        print(generatedTree);

        generatedTree.insert2pass(59);
        print(generatedTree);

        generatedTree.insert2pass(41);
        print(generatedTree);

        generatedTree.insert2pass(33);
        print(generatedTree);

        generatedTree.insert2pass(30);
        print(generatedTree);

        generatedTree.insert2pass(60);
        print(generatedTree);

        generatedTree.insert2pass(39);
        print(generatedTree);

        generatedTree.insert2pass(38);
        print(generatedTree);

        generatedTree.insert2pass(62);
        print(generatedTree);

        generatedTree.insert2pass(61);
        print(generatedTree);

        generatedTree.insert2pass(35);
        print(generatedTree);


        generatedTree.insert2pass(34);
        print(generatedTree);

        generatedTree.insert2pass(66);
        print(generatedTree);

        generatedTree.insert2pass(65);
        print(generatedTree);

        generatedTree.insert2pass(12);
        print(generatedTree);






        generatedTree.delete(57);
        print(generatedTree);
        generatedTree.delete(58);
        print(generatedTree);
        generatedTree.delete(53);
        print(generatedTree);
        generatedTree.delete(51);
        print(generatedTree);
        generatedTree.delete(52);
        print(generatedTree);
        generatedTree.delete(44);
        print(generatedTree);
        System.out.println("coool");
        generatedTree.delete(54);
        print(generatedTree);




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