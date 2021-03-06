package com.gimangi.singleline_note.data.database.dao

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.room.*
import com.gimangi.singleline_note.data.database.dto.MemoItemEntity
import com.gimangi.singleline_note.data.database.dto.MemoTableEntity
import java.util.*

@Dao
abstract class MemoDao {

    @Query("SELECT * FROM memo")
    abstract suspend fun getMemoAll(): List<MemoTableEntity>?

    @Query("SELECT * FROM memo WHERE memo_id = :memoId")
    abstract suspend fun getMemoById(memoId: Int): MemoTableEntity?

    @Query("DELETE FROM memo WHERE memo_id IN (:list)")
    abstract suspend fun deleteMemoTables(list: List<Int>): Int

    @Insert
    abstract suspend fun insertMemoTable(memoTableEntity: MemoTableEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun updateMemoTable(memoTableEntity: MemoTableEntity): Int

    @Transaction
    open suspend fun addMemoItem(index: Int?, memoTableEntity: MemoTableEntity, memoItemEntity: MemoItemEntity): MemoTableEntity {
        updateMemoTable(
            memoTableEntity.apply {
                updatedAt = Date()

                if (index == null)
                    rowList.add(memoItemEntity)
                else
                    rowList.add(index, memoItemEntity)
            }
        )
        return memoTableEntity
    }

    @Transaction
    open suspend fun updateMemoTableDefine(memoId: Int, memoName: String, suffix: String) {
        val memoTable = getMemoById(memoId)

        if (memoTable != null) {
            updateMemoTable(memoTable.apply {
                this.memoName = memoName
                this.suffix = suffix
            })
        }
    }

    @Insert
    abstract suspend fun insertMemoItem(memoItemEntity: MemoItemEntity)

    @Update
    abstract suspend fun updateMemoItem(memoItemEntity: MemoItemEntity)


}
