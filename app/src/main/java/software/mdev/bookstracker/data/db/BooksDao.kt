package software.mdev.bookstracker.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import software.mdev.bookstracker.data.db.entities.Book

@Dao
interface BooksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: Book)

    @Delete
    suspend fun delete(item: Book)

    @Query("SELECT * FROM Book WHERE item_bookStatus LIKE 'read'")
    fun getReadBooks(): LiveData<List<Book>>

    @Query("SELECT * FROM Book WHERE item_bookStatus LIKE 'in_progress'")
    fun getInProgressBooks(): LiveData<List<Book>>

    @Query("SELECT * FROM Book WHERE item_bookStatus LIKE 'to_read'")
    fun getToReadBooks(): LiveData<List<Book>>

    @Query("UPDATE Book SET item_bookTitle =:bookTitle ,item_bookAuthor=:bookAuthor ,item_bookRating=:bookRating, item_bookStatus=:bookStatus, item_bookFinishDate=:bookFinishDateMs, item_bookNumberOfPages=:bookNumberOfPagesInt, item_bookTitle_ASCII=:bookTitle_ASCII, item_bookAuthor_ASCII=:bookAuthor_ASCII WHERE id=:id")
    suspend fun updateBook(id: Int?, bookTitle: String, bookAuthor: String, bookRating: Float, bookStatus: String, bookFinishDateMs: String, bookNumberOfPagesInt: Int, bookTitle_ASCII: String, bookAuthor_ASCII: String)

    @Query("SELECT * FROM Book WHERE (item_bookTitle_ASCII LIKE '%' || :searchQuery || '%' OR item_bookAuthor_ASCII LIKE '%' || :searchQuery || '%')")
    fun searchBooks(searchQuery: String): LiveData<List<Book>>

    @Query("SELECT * FROM Book WHERE item_bookStatus LIKE :bookStatus ORDER BY item_bookTitle DESC")
    fun getSortedBooksByTitleDesc(bookStatus: String): LiveData<List<Book>>
    @Query("SELECT * FROM Book WHERE item_bookStatus LIKE :bookStatus ORDER BY item_bookTitle ASC")
    fun getSortedBooksByTitleAsc(bookStatus: String): LiveData<List<Book>>
    @Query("SELECT * FROM Book WHERE item_bookStatus LIKE :bookStatus ORDER BY item_bookAuthor DESC")
    fun getSortedBooksByAuthorDesc(bookStatus: String): LiveData<List<Book>>
    @Query("SELECT * FROM Book WHERE item_bookStatus LIKE :bookStatus ORDER BY item_bookAuthor ASC")
    fun getSortedBooksByAuthorAsc(bookStatus: String): LiveData<List<Book>>
    @Query("SELECT * FROM Book WHERE item_bookStatus LIKE :bookStatus ORDER BY item_bookRating DESC")
    fun getSortedBooksByRatingDesc(bookStatus: String): LiveData<List<Book>>
    @Query("SELECT * FROM Book WHERE item_bookStatus LIKE :bookStatus ORDER BY item_bookRating ASC")
    fun getSortedBooksByRatingAsc(bookStatus: String): LiveData<List<Book>>
    @Query("SELECT * FROM Book WHERE item_bookStatus LIKE :bookStatus ORDER BY item_bookNumberOfPages DESC")
    fun getSortedBooksByPagesDesc(bookStatus: String): LiveData<List<Book>>
    @Query("SELECT * FROM Book WHERE item_bookStatus LIKE :bookStatus ORDER BY item_bookNumberOfPages ASC")
    fun getSortedBooksByPagesAsc(bookStatus: String): LiveData<List<Book>>

    @Query("SELECT COUNT(id) FROM Book WHERE item_bookStatus LIKE :bookStatus")
    fun getBookCount(bookStatus: String): LiveData<Integer>
}