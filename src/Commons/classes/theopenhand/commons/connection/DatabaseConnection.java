/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.commons.connection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 * <p>
 * This class allows to connect this client to the database,only if the DB is
 * reachable by an ip address or any other url which allows a direct connection
 * between <i>client - database</i>.</p>
 * <p>
 * Otherwise use the class NAME-OF-THE-CLASS-HERE that allows custom GET and
 * POST calls to a site and read its result as a json file input.</p>
 *
 * @author gabri
 */
public final class DatabaseConnection {

    Connection DBConn;

    /**
     *
     */
    public static String IP = "192.168.1.98:3306/ncdb?serverTimezone=UTC&useSSL=false";//"80.211.167.74:3306/ncdb";

    /**
     *
     */
    public static String USER = "superuser";

    /**
     *
     */
    public static String PASSWORD = "rootpass";

    private boolean AUTOCOMMIT = false;
    private boolean SAVEONEXIT = true;

    private boolean connected = false;

    /**
     * Create a new object that holds the connection properties and driver to
     * the database. The data needed to extabilish the connection is hold by the
     * DBProperties class:
     * <ul>
     * <li> Password and username used for the login @see DBConn(LoginData)</li>
     * <li> Driver needed for the connection</li>
     * <li> URL to the database</li>
     * <li> Control of the file's modifications during the programm run (will
     * cause the connection to restart)</li>
     * </ul>
     * <p>
     * This library works with the &lt&lt MLib DB properties &gt&gt module
     *
     */
    public DatabaseConnection() {
        hookShutdown();
        CONNECT();
    }

    /**
     *
     */
    public void hookShutdown() {
        Thread hook = new Thread() {
            @Override
            public void run() {
                try {
                    if (DBConn != null && DBConn.getMetaData() != null) {
                        DISCONNECT();
                    }
                } catch (SQLException ex) {
                    //ALLORA la connessione è già stata terminata
                }
            }
        };
        Runtime.getRuntime().addShutdownHook(hook);
    }

    /**
     *
     */
    public void CONNECT() {
        String def_url = "jdbc:mysql://" + IP;
        connect(def_url);
    }

    private void connect(String url) {
        try {
            DBConn = DriverManager.getConnection(url, USER, PASSWORD);
            DBConn.setAutoCommit(AUTOCOMMIT);
            DBConn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connected = true;
        } catch (SQLException ex) {
            //TODO : Aggiungi eccezione connessione
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Impossibile connettersi", "Non è stato possibile connettersi al database selezionato.\nRicontrollare di essere connessi alla rete o che il server sia acceso.", null).showAndWait();
        } catch (RuntimeException e) {

        }
    }

    /**
     *
     * @param autocommit
     */
    public void setAutoCommit(boolean autocommit) {
        try {
            this.AUTOCOMMIT = autocommit;
            DBConn.setAutoCommit(AUTOCOMMIT);
        } catch (SQLException ex) {
            //TODO : Aggiungi eccezione autocommit
        }
    }

    ArrayList<Savepoint> svs = new ArrayList<>();

    /**
     *
     * @return
     */
    public boolean createSavepoint() {
        if (!this.AUTOCOMMIT) {
            try {
                Savepoint s = DBConn.setSavepoint(DateFormat.getTimeInstance().format(new Date()));
                svs.add(s);
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    public List<Savepoint> getRollbackPoints() {
        return Collections.unmodifiableList(svs);
    }

    /**
     *
     * @param sp
     * @return
     */
    public boolean rollbackTo(Savepoint sp) {
        if (!this.AUTOCOMMIT) {
            try {
                DBConn.rollback(sp);
                boolean found = false;
                Iterator<Savepoint> iterator = svs.iterator();
                while (iterator.hasNext()) {
                    Savepoint s = iterator.next();
                    if (!found && s.getSavepointName().equals(sp.getSavepointName())) {
                        found = true;
                    }
                    if (found) {
                        iterator.remove();
                    }
                }
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    /**
     *
     * @param save
     */
    public void saveOnForcedExit(boolean save) {
        this.SAVEONEXIT = save;
    }

    private boolean check_allowance(int type, int concurrency) {
        try {
            DatabaseMetaData dbMetaData = DBConn.getMetaData();
            return dbMetaData.supportsResultSetConcurrency(type, concurrency);
        } catch (SQLException ex) {
            //TODO : Aggiungi eccezione connessione non supporta la concorrenza
            return false;
        }
    }

    /**
     *
     */
    public void DISCONNECT() {
        try {
            if (SAVEONEXIT && !AUTOCOMMIT) {
                DBConn.commit();
            }
            DBConn.close();
            connected = false;
            System.err.println("Disconnected");
            //System.exit(0);
            Platform.exit();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param update
     * @return
     */
    public int executeUpdate(String update) {
        try {
            Statement s = DBConn.createStatement();
            return s.executeUpdate(update);
        } catch (SQLException ex) {
            //TODO : Aggiungi eccezione update query
            return -1;
        } finally {
            //TODO : Aggiorna dati dopo update
        }
    }

    /**
     *
     * @param update
     * @return
     */
    public long executeLargeUpdate(String update) {
        try {
            Statement s = DBConn.createStatement();
            return s.executeLargeUpdate(update);
        } catch (SQLException ex) {
            //TODO : Aggiungi eccezione large update query
            return -1;
        } finally {
            //TODO : Aggiorna i dati dopo large update
        }
    }

    /**
     *
     * @param toquery
     * @return
     */
    public ResultSet executeQuery(String toquery) {
        try {
            Statement s = DBConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            return s.executeQuery(toquery);
        } catch (SQLException ex) {
            //TODO : Aggiungi eccezione query
            return null;
        } finally {
            //TODO : Aggiorna dati dopo query (Non necessario)
        }
    }

    /**
     *
     * @return
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     *
     * @return
     */
    public Connection getConn() {
        return DBConn;
    }

}
