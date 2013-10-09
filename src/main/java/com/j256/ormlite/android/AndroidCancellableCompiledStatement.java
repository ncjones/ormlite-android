package com.j256.ormlite.android;

import java.sql.SQLException;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CancellationSignal;
import android.os.OperationCanceledException;

import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.stmt.StatementBuilder.StatementType;
import com.j256.ormlite.support.DatabaseResults;

/**
 * {@link AndroidCompiledStatement} which provides support for cancellable queries.
 */
public class AndroidCancellableCompiledStatement extends AndroidCompiledStatement {
	
	private final CancellationSignal cancellationSignal;

	public AndroidCancellableCompiledStatement(String sql, SQLiteDatabase db, StatementType type) {
		super(sql, db, type);
		cancellationSignal = new CancellationSignal();
	}

	@Override
	protected Cursor rawQuery(SQLiteDatabase db, String sql, String[] args) {
		return db.rawQuery(sql, args, cancellationSignal);
	}

	@Override
	public void cancelQuery() {
		cancellationSignal.cancel();
	}

	private static SQLException queryCancelledSqlException(OperationCanceledException e) {
		return SqlExceptionUtil.create("ORMLITE: query cancelled", e);
	}

	@Override
	public int getColumnCount() throws SQLException {
		try {
			return super.getColumnCount();
		} catch (OperationCanceledException e) {
			throw queryCancelledSqlException(e);
		}
	}

	@Override
	public String getColumnName(int column) throws SQLException {
		try {
			return super.getColumnName(column);
		} catch (OperationCanceledException e) {
			throw queryCancelledSqlException(e);
		}
	}

	@Override
	public DatabaseResults runQuery(ObjectCache objectCache) throws SQLException {
		try {
			return super.runQuery(objectCache);
		} catch (OperationCanceledException e) {
			throw queryCancelledSqlException(e);
		}
	}

	@Override
	public void close() throws SQLException {
		try {
			super.close();
		} catch (OperationCanceledException e) {
			throw queryCancelledSqlException(e);
		}
	}

	@Override
	public void closeQuietly() {
		try {
			super.closeQuietly();
		} catch(OperationCanceledException e) {
			// ignored
		}
	}

	@Override
	public Cursor getCursor() throws SQLException {
		try {
			return super.getCursor();
		} catch (OperationCanceledException e) {
			throw queryCancelledSqlException(e);
		}
	}

}
