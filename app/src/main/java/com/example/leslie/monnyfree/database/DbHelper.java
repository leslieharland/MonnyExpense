package com.example.leslie.monnyfree.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.leslie.monnyfree.R;
import com.example.leslie.monnyfree.utils.DatabaseUtil;

/**
 * Created by Leslie on 3/18/2018.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final int databaseVersion = 1;
    private static final String CATEGORY_TABLE = "category";
    private static final String EXPENSE_TABLE = "expense";
    private static final String DATEGROUP_TABLE = "dateGroup";
    // Dynamic
    private static final String DATABASE_NAME = "expensesDb.db";

    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
        this.mContext = context;
    }
    /**
     * Constructor. This is where the database path gets set.
     * @param context Current context.
     */

    public DbHelper(Context context, String dbPath) {
        super(context, dbPath, null, databaseVersion);
        this.mContext = context;
    }

    private Context mContext;
    private String mPassword = "";

    public Context getContext() {
        return this.mContext;
    }

//    @Override
//    public void onConfigure(SQLiteDatabase db) {
//        super.onConfigure(db);
//        db.rawQuery("PRAGMA journal_mode=OFF", null).close();
//    }

    /**
     * Called when the database is being created.
     * @param db Database instance.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Dbhelper", "OpenHelper onCreate");

        try {
            executeRawSql(db, R.raw.tables);
            executeRawSql(db, R.raw.data);
            initDatabase(db);
        } catch (Exception e) {
            Log.e("Database Initializing", e.toString());
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

//        int version = db.getVersion();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("Upgrading from", oldVersion + "to" + newVersion);

        try {
            String currentDbFile = db.getPath();
            createDatabaseBackupOnUpgrade(currentDbFile, oldVersion);
        } catch (Exception ex) {
            Log.e(ex.toString(), "creating database backup, can't continue");

            // don't upgrade
            return;
        }

        // update databases
        updateDatabase(db, oldVersion, newVersion);

        // notify sync about the db update.
       // new SyncManager(getContext()).dataChanged();
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // nothing to do for now.
        Log.d("onDowngrade", "Downgrade attempt from" + oldVersion + " to" + newVersion);
    }

//    @Override
//    public synchronized void close() {
//        super.close();
//
//        mInstance = null;
//    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase db = null;
        try {
            db = super.getReadableDatabase();
        } catch (Exception ex) {
            Log.e(ex.toString(), "opening readable database");
        }
        return db;
    }

//    public SQLiteDatabase getReadableDatabase() {
//        return this.getReadableDatabase(this.mPassword);
//    }
//    @Override
//    public SQLiteDatabase getReadableDatabase(String password) {
//        SQLiteDatabase db = null;
//        try {
//            db = super.getReadableDatabase(password);
//        } catch (Exception ex) {
//            Timber.e(ex, "opening readable database");
//        }
//        return db;
//    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        try {
            //return getWritableDatabase_Internal();
            return super.getWritableDatabase();
        } catch (Exception ex) {
            Log.e(ex.toString(), "opening writable database");
        }
        return null;
    }

//    public SQLiteDatabase getWritableDatabase() {
//        return getWritableDatabase(this.mPassword);
//    }
//    @Override
//    public SQLiteDatabase getWritableDatabase(String password) {
//        try {
//            return getWritableDatabase_Internal(password);
//        } catch (Exception ex) {
//            Timber.e(ex, "opening writable database");
//        }
//        return null;
//    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

//    public boolean hasPassword() {
//        return !TextUtils.isEmpty(this.mPassword);
//    }

//    private SQLiteDatabase getWritableDatabase_Internal() {
//        // String password
////
////        SQLiteDatabase db = super.getWritableDatabase(password);
//        SQLiteDatabase db = super.getWritableDatabase();
//
//        if (db != null) {
//            db.rawQuery("PRAGMA journal_mode=OFF", null).close();
//        }
//
//        return db;
//    }

    /**
     * @param db    SQLite database to execute raw SQL
     * @param rawId id raw resource
     */
    private void executeRawSql(SQLiteDatabase db, int rawId) {
        String sqlRaw = DatabaseUtil.getRawAsString(getContext(), rawId);
        String sqlStatement[] = sqlRaw.split(";");

        // process all statements
        for (String aSqlStatment : sqlStatement) {
            Log.d("executeRawSql", aSqlStatment);

            try {
                db.execSQL(aSqlStatment);
            } catch (Exception e) {
                String errorMessage = e.getMessage();
                if (e instanceof SQLiteException && errorMessage != null && errorMessage.contains("not an error (code 0)")) {
                    //Timber.w(errorMessage);
                } else {
                    //Timber.e(e, "executing raw sql: %s", aSqlStatment);
                }
            }
        }
    }

    /**
     * Get SQLite Version installed
     * @return version of SQLite
     */
    public String getSQLiteVersion() {
        String sqliteVersion = null;
        Cursor cursor = null;
        try {
            if (getReadableDatabase() != null) {
                cursor = getReadableDatabase().rawQuery("select sqlite_version() AS sqlite_version", null);
                if (cursor != null && cursor.moveToFirst()) {
                    sqliteVersion = cursor.getString(0);
                }
            }
        } catch (Exception e) {
            Log.e(e.toString(), "getting sqlite version");
        } finally {
            if (cursor != null) cursor.close();
//            if (database != null) database.close();
        }
        return sqliteVersion;
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Execute every script between the old and the new version of the database schema.
        for (int i = oldVersion + 1; i <= newVersion; i++) {
            int resourceId = mContext.getResources()
                    .getIdentifier("database_version_" + Integer.toString(i),
                            "raw", mContext.getPackageName());
            if (resourceId > 0) {
                executeRawSql(db, resourceId);
            }
        }
    }

    private boolean initDatabase(SQLiteDatabase database) {
        try {
            //initBaseCurrency(database);
        } catch (Exception e) {
            Log.e(e.toString(), "init database, base currency");
        }

        //initDateFormat(database);


        return true;
    }


    /**
     * The creation of the record is done in tables_v1.sql initialization script.
     * Here we only update the record to the current system's date format.

     */
/*    private void initDateFormat(SQLiteDatabase database) {
        try {
            Core core = new Core(getContext());
            String pattern = core.getDefaultSystemDateFormat();
            if (pattern == null) return;

            InfoService infoService = new InfoService(getContext());
            infoService.updateRaw(database, InfoKeys.DATEFORMAT, pattern);
        } catch (Exception e) {
            Timber.e(e, "init database, date format");
        }
    }*/

    @Override
    public void finalize() throws Throwable {
        super.finalize();
    }

    public void createDatabaseBackupOnUpgrade(String currentDbFile, int oldVersion) {
     /*   File in = new File(currentDbFile);
        String backupFileNameWithExtension = in.getName();

        String backupName = FileUtil.getBaseName(backupFileNameWithExtension);
        String backupExtension = FileUtil.getExtension(backupFileNameWithExtension);

        // append last db version
        backupName += "_v" + Integer.toString(oldVersion);

        backupFileNameWithExtension = backupName + "." + backupExtension;

        String outPath = FileUtil.getFullPath(currentDbFile) + backupFileNameWithExtension;
        File out = new File(outPath);

        FileUtils.copyFile(in, out);*/
    }
}
