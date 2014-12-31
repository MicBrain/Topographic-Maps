package trip;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;

import java.util.Scanner;
import java.util.regex.Pattern;

import ucb.util.CommandArgs;

/** Initial class for the 'trip' program.
 *  @author P. N. Hilfinger
 */
public final class Main {

    /** Describes separator in a comma-separated list. */
    static final Pattern LISTSEP = Pattern.compile("\\s*,\\s*");

    /** Entry point for the CS61B trip program.  RAWARGS may contain options
     *  and targets:
     *      [ -m MAP ] [ -o OUT ] [ REQUEST ]
     *  where MAP (default Map) contains the map data, OUT (default standard
     *  output) takes the result, and REQUEST (default standard input) contains
     *  the locations along the requested trip.
     */
    public static void main(String... rawArgs) {
        String mapFileName;
        String outFileName;
        String requestFileName;
        CommandArgs args =
            new CommandArgs("-m={0,1} -o={0,1} --={0,1}", rawArgs);

        if (!args.ok()) {
            usage();
        }

        mapFileName = args.getFirst("-m", "Map");
        outFileName = args.getFirst("-o");
        requestFileName = args.getFirst("--");

        setFiles(requestFileName, outFileName);

        Trip trip = new Trip();

        try {
            trip.readMap(mapFileName);

            Scanner inp = new Scanner(System.in);
            while (inp.hasNextLine()) {
                String line = inp.nextLine().trim();
                if (!line.isEmpty()) {
                    trip.makeTrip(LISTSEP.split(line));
                }
            }
        } catch (IllegalArgumentException excp) {
            System.err.printf("trip: %s%n", excp.getMessage());
            System.exit(1);
        }
    }


    /** Set System.in to read from INFILE, if non-null; otherwise, leaves
     *  System.in unchanged.  Set System.out to go to OUTFILE, if non-null;
     *  otherwise leaves System.out unchanged. */
    private static void setFiles(String inFile, String outFile) {
        if (inFile != null) {
            try {
                System.setIn(new FileInputStream(inFile));
            } catch  (FileNotFoundException e) {
                System.err.printf("Could not open %s.%n", inFile);
                System.exit(1);
            }
        }

        if (outFile != null) {
            try {
                System.setOut(new PrintStream(new FileOutputStream(outFile),
                                              true));
            } catch  (FileNotFoundException e) {
                System.err.printf("Could not open %s for writing.%n",
                                  outFile);
                System.exit(1);
            }
        }
    }


    /** Throw an exception containing a message constructed from FORMAT
     *  and ARGS, as for String.format. */
    static void error(String format, Object... args) {
        throw new IllegalArgumentException(String.format(format, args));
    }

    /** Print a brief usage message and exit program abnormally. */
    private static void usage() {
        System.err.printf("Usage: java trip.Main [ -m MAPFILE ] [ -o OUTFILE ]"
                          + " [ REQUESTFILE ]%n");
        System.exit(1);
    }

}
