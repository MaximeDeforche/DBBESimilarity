package be.ugent.telin.ddcm.util;

import org.neo4j.graphdb.*;
import org.neo4j.procedure.TerminationGuard;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class CypherUtils {

    public static boolean isQueryTerminated(TerminationGuard guard) {
        try {
            guard.check();
            return false;
        } catch (TransactionTerminatedException | NotInTransactionException e) {
            return true;
        }
    }

    public static void performTransactionally(GraphDatabaseService service, Consumer<Transaction> function) {
        try (Transaction tx = service.beginTx()) {
            function.accept(tx);
            tx.commit();
        }
    }

    public static <T> T getTransactionally(GraphDatabaseService service, Function<Transaction, T> function) {
        return getTransactionally(service, false, function);
    }

    public static <T> T getTransactionally(GraphDatabaseService service, boolean closeManually, Function<Transaction, T> function) {
        T res;

        try (Transaction tx = service.beginTx()) {
            res = function.apply(tx);
            if (!closeManually) tx.commit();
        }

        return res;
    }

    public static List<String> createPartialStatements(String statement, int size, long total) {
        return createPartialStatements(statement, "", size, total);
    }

    public static List<String> createPartialStatements(String statement, String statement2, int size, long total) {
        List<String> res = new ArrayList<>();
        statement = statement.trim();
        long counter = 0;
        long upper;

        while (counter < total) {
            upper = Math.min(counter + size, total);
            res.add(statement + " SKIP " + counter + " LIMIT " + size + " " + statement2);
            counter = upper;
        }

        return res;
    }
}
