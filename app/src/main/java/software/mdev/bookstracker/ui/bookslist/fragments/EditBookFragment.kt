package software.mdev.bookstracker.ui.bookslist.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import software.mdev.bookstracker.R
import software.mdev.bookstracker.data.db.BooksDatabase
import software.mdev.bookstracker.data.db.entities.Book
import software.mdev.bookstracker.data.repositories.BooksRepository
import software.mdev.bookstracker.ui.bookslist.viewmodel.BooksViewModel
import software.mdev.bookstracker.ui.bookslist.viewmodel.BooksViewModelProviderFactory
import software.mdev.bookstracker.ui.bookslist.ListActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_edit_book.*
import software.mdev.bookstracker.data.db.YearDatabase
import software.mdev.bookstracker.data.repositories.YearRepository
import software.mdev.bookstracker.other.Constants
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import software.mdev.bookstracker.data.db.LanguageDatabase
import software.mdev.bookstracker.data.repositories.LanguageRepository
import software.mdev.bookstracker.data.repositories.OpenLibraryRepository


class EditBookFragment : Fragment(R.layout.fragment_edit_book) {

    lateinit var viewModel: BooksViewModel
    private val args: EditBookFragmentArgs by navArgs()
    lateinit var book: Book
    lateinit var listActivity: ListActivity
    private var bookFinishDateMs: Long? = null
    private var bookStartDateMs: Long? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ListActivity).booksViewModel
        listActivity = activity as ListActivity

        var whatIsClicked = Constants.BOOK_STATUS_NOTHING

        val database = BooksDatabase(view.context)
        val yearDatabase = YearDatabase(view.context)
        val languageDatabase = LanguageDatabase(view.context)

        val repository = BooksRepository(database)
        val yearRepository = YearRepository(yearDatabase)
        val openLibraryRepository = OpenLibraryRepository()
        val languageRepository = LanguageRepository(languageDatabase)

        val booksViewModelProviderFactory = BooksViewModelProviderFactory(
            repository,
            yearRepository,
            openLibraryRepository,
            languageRepository
        )

        val book = args.book
        var accentColor = getAccentColor(view.context)

        val viewModel = ViewModelProviders.of(this, booksViewModelProviderFactory).get(BooksViewModel::class.java)

        etEditedBookTitle.setText(book.bookTitle)
        etEditedBookAuthor.setText(book.bookAuthor)
        rbEditedRating.rating = book.bookRating
        etEditedPagesNumber.setText(book.bookNumberOfPages.toString())

        dpEditBookFinishDate.visibility = View.GONE
        dpEditBookStartDate.visibility = View.GONE

        btnEditorSaveFinishDate.visibility = View.GONE
        btnEditorCancelFinishDate.visibility = View.GONE
        btnEditorSaveStartDate.visibility = View.GONE
        btnEditorCancelStartDate.visibility = View.GONE

        btnEditFinishDate.visibility = View.GONE
        btnEditStartDate.visibility = View.GONE

        tvDateStartedTitle.visibility = View.GONE
        tvDateFinishedTitle.visibility = View.GONE

        dpEditBookFinishDate.maxDate = System.currentTimeMillis()
        dpEditBookStartDate.maxDate = System.currentTimeMillis()

        if(book.bookFinishDate == "none" || book.bookFinishDate == "null") {
            btnEditFinishDate.text = getString(R.string.set)
            ivClearFinishDate.visibility = View.GONE
        } else {
            var bookFinishTimeStampLong = book.bookFinishDate.toLong()
            btnEditFinishDate.text = convertLongToTime(bookFinishTimeStampLong)
            ivClearFinishDate.visibility = View.VISIBLE
        }

        if(book.bookStartDate == "none" || book.bookStartDate == "null") {
            btnEditStartDate.text = getString(R.string.set)
            ivClearStartDate.visibility = View.GONE
        } else {
            var bookStartTimeStampLong = book.bookStartDate.toLong()
            btnEditStartDate.text = convertLongToTime(bookStartTimeStampLong)
            ivClearStartDate.visibility = View.VISIBLE
        }

        if(book.bookOLID == "none" || book.bookOLID == "null") {
            etEditedBookOLID.setText(Constants.EMPTY_STRING)
        } else {
            etEditedBookOLID.setText(book.bookOLID)
        }

        if(book.bookISBN10 == "none" || book.bookISBN10 == "null") {
            etEditedBookISBN10.setText(Constants.EMPTY_STRING)
        } else {
            etEditedBookISBN10.setText(book.bookISBN10)
        }

        if(book.bookISBN13 == "none" || book.bookISBN13 == "null") {
            etEditedBookISBN13.setText(Constants.EMPTY_STRING)
        } else {
            etEditedBookISBN13.setText(book.bookISBN13)
        }

        etEditedBookTitle.requestFocus()
        showKeyboard(etEditedBookTitle,350)

        when (book.bookStatus) {
            Constants.BOOK_STATUS_READ -> {
                ivEditorBookStatusRead.setColorFilter(accentColor, android.graphics.PorterDuff.Mode.SRC_IN)
                ivEditorBookStatusInProgress.setColorFilter(ContextCompat.getColor(view.context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
                ivEditorBookStatusToRead.setColorFilter(ContextCompat.getColor(view.context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
                whatIsClicked = Constants.BOOK_STATUS_READ
                rbEditedRating.visibility = View.VISIBLE
                etEditedPagesNumber.visibility = View.VISIBLE
                btnEditStartDate.visibility = View.VISIBLE
                btnEditFinishDate.visibility = View.VISIBLE

                tvDateStartedTitle.visibility = View.VISIBLE
                tvDateFinishedTitle.visibility = View.VISIBLE

                if(book.bookFinishDate != "none" && book.bookFinishDate != "null") {
                    bookFinishDateMs = book.bookFinishDate.toLong()
                }

                if(book.bookStartDate != "none" && book.bookStartDate != "null") {
                    bookStartDateMs = book.bookStartDate.toLong()
                }
            }
            Constants.BOOK_STATUS_IN_PROGRESS -> {
                ivEditorBookStatusRead.setColorFilter(ContextCompat.getColor(view.context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
                ivEditorBookStatusInProgress.setColorFilter(accentColor, android.graphics.PorterDuff.Mode.SRC_IN)
                ivEditorBookStatusToRead.setColorFilter(ContextCompat.getColor(view.context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
                whatIsClicked = Constants.BOOK_STATUS_IN_PROGRESS
                rbEditedRating.visibility = View.GONE
                etEditedPagesNumber.visibility = View.GONE
            }
            Constants.BOOK_STATUS_TO_READ -> {
                ivEditorBookStatusRead.setColorFilter(ContextCompat.getColor(view.context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
                ivEditorBookStatusInProgress.setColorFilter(ContextCompat.getColor(view.context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
                ivEditorBookStatusToRead.setColorFilter(accentColor, android.graphics.PorterDuff.Mode.SRC_IN)
                whatIsClicked = Constants.BOOK_STATUS_TO_READ
                rbEditedRating.visibility = View.GONE
                etEditedPagesNumber.visibility = View.GONE
            }
        }

        ivEditorBookStatusRead.setOnClickListener {
            it.hideKeyboard()

            ivEditorBookStatusRead.setColorFilter(accentColor, android.graphics.PorterDuff.Mode.SRC_IN)
            ivEditorBookStatusInProgress.setColorFilter(ContextCompat.getColor(view.context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivEditorBookStatusToRead.setColorFilter(ContextCompat.getColor(view.context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            whatIsClicked = Constants.BOOK_STATUS_READ
            rbEditedRating.visibility = View.VISIBLE
            etEditedPagesNumber.visibility = View.VISIBLE
            btnEditStartDate.visibility = View.VISIBLE
            btnEditFinishDate.visibility = View.VISIBLE

            tvDateStartedTitle.visibility = View.VISIBLE
            tvDateFinishedTitle.visibility = View.VISIBLE

            if (bookStartDateMs != null)
                ivClearStartDate.visibility = View.VISIBLE

            if (bookFinishDateMs != null)
                ivClearFinishDate.visibility = View.VISIBLE
        }

        ivEditorBookStatusInProgress.setOnClickListener {
            it.hideKeyboard()

            ivEditorBookStatusRead.setColorFilter(ContextCompat.getColor(view.context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivEditorBookStatusInProgress.setColorFilter(accentColor, android.graphics.PorterDuff.Mode.SRC_IN)
            ivEditorBookStatusToRead.setColorFilter(ContextCompat.getColor(view.context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            whatIsClicked = Constants.BOOK_STATUS_IN_PROGRESS
            rbEditedRating.visibility = View.GONE
            etEditedPagesNumber.visibility = View.GONE
            btnEditStartDate.visibility = View.GONE
            btnEditFinishDate.visibility = View.GONE

            tvDateStartedTitle.visibility = View.GONE
            tvDateFinishedTitle.visibility = View.GONE

            tvDateStartedTitle.visibility = View.VISIBLE
            btnEditStartDate.visibility = View.VISIBLE

            ivClearFinishDate.visibility = View.GONE

            if (bookStartDateMs != null)
                ivClearStartDate.visibility = View.VISIBLE
        }

        ivEditorBookStatusToRead.setOnClickListener {
            it.hideKeyboard()

            ivEditorBookStatusRead.setColorFilter(ContextCompat.getColor(view.context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivEditorBookStatusInProgress.setColorFilter(ContextCompat.getColor(view.context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivEditorBookStatusToRead.setColorFilter(accentColor, android.graphics.PorterDuff.Mode.SRC_IN)
            whatIsClicked = Constants.BOOK_STATUS_TO_READ
            rbEditedRating.visibility = View.GONE
            etEditedPagesNumber.visibility = View.GONE
            btnEditStartDate.visibility = View.GONE
            btnEditFinishDate.visibility = View.GONE

            tvDateStartedTitle.visibility = View.GONE
            tvDateFinishedTitle.visibility = View.GONE

            ivClearStartDate.visibility = View.GONE
            ivClearFinishDate.visibility = View.GONE
        }

        btnEditFinishDate.setOnClickListener {
            it.hideKeyboard()

            dpEditBookFinishDate.visibility = View.VISIBLE
            btnEditorSaveFinishDate.visibility = View.VISIBLE
            btnEditorCancelFinishDate.visibility = View.VISIBLE
            btnEditorSaveFinishDate.isClickable = true
            btnEditorCancelFinishDate.isClickable = true

            etEditedBookTitle.visibility = View.GONE
            etEditedBookAuthor.visibility = View.GONE

            ivEditorBookStatusRead.visibility = View.GONE
            ivEditorBookStatusInProgress.visibility = View.GONE
            ivEditorBookStatusToRead.visibility = View.GONE
            tvFinished.visibility = View.GONE
            tvInProgress.visibility = View.GONE
            tvToRead.visibility = View.GONE

            etEditedPagesNumber.visibility = View.GONE
            rbEditedRating.visibility = View.GONE
            btnEditFinishDate.visibility = View.GONE
            btnEditStartDate.visibility = View.GONE

            tvDateStartedTitle.visibility = View.GONE
            tvDateFinishedTitle.visibility = View.GONE

            fabSaveEditedBook.visibility = View.GONE
            fabDeleteBook.visibility = View.GONE

            etEditedBookOLID.visibility = View.GONE
            etEditedBookISBN10.visibility = View.GONE
            etEditedBookISBN13.visibility = View.GONE

            tvEditedOLID.visibility = View.GONE
            tvEditedISBN10.visibility = View.GONE
            tvEditedISBN13.visibility = View.GONE

            ivClearStartDate.visibility = View.GONE
            ivClearFinishDate.visibility = View.GONE
        }

        btnEditorSaveFinishDate.setOnClickListener {
            bookFinishDateMs = getDateFromDatePickerInMillis(dpEditBookFinishDate)

            dpEditBookFinishDate.visibility = View.GONE
            btnEditorSaveFinishDate.visibility = View.GONE
            btnEditorCancelFinishDate.visibility = View.GONE
            btnEditorSaveFinishDate.isClickable = false
            btnEditorCancelFinishDate.isClickable = false

            etEditedBookTitle.visibility = View.VISIBLE
            etEditedBookAuthor.visibility = View.VISIBLE

            ivEditorBookStatusRead.visibility = View.VISIBLE
            ivEditorBookStatusInProgress.visibility = View.VISIBLE
            ivEditorBookStatusToRead.visibility = View.VISIBLE
            tvFinished.visibility = View.VISIBLE
            tvInProgress.visibility = View.VISIBLE
            tvToRead.visibility = View.VISIBLE

            etEditedPagesNumber.visibility = View.VISIBLE
            rbEditedRating.visibility = View.VISIBLE
            btnEditFinishDate.visibility = View.VISIBLE
            btnEditStartDate.visibility = View.VISIBLE

            tvDateStartedTitle.visibility = View.VISIBLE
            tvDateFinishedTitle.visibility = View.VISIBLE

            fabSaveEditedBook.visibility = View.VISIBLE
            fabDeleteBook.visibility = View.VISIBLE

            etEditedBookOLID.visibility = View.VISIBLE
            etEditedBookISBN10.visibility = View.VISIBLE
            etEditedBookISBN13.visibility = View.VISIBLE

            tvEditedOLID.visibility = View.VISIBLE
            tvEditedISBN10.visibility = View.VISIBLE
            tvEditedISBN13.visibility = View.VISIBLE

            btnEditFinishDate.text = bookFinishDateMs?.let { it1 -> convertLongToTime(it1) }

            if (bookStartDateMs != null)
                ivClearStartDate.visibility = View.VISIBLE
            if (bookFinishDateMs != null)
                ivClearFinishDate.visibility = View.VISIBLE
        }

        btnEditorCancelFinishDate.setOnClickListener {
            dpEditBookFinishDate.visibility = View.GONE
            btnEditorSaveFinishDate.visibility = View.GONE
            btnEditorCancelFinishDate.visibility = View.GONE
            btnEditorSaveFinishDate.isClickable = false
            btnEditorCancelFinishDate.isClickable = false

            etEditedBookTitle.visibility = View.VISIBLE
            etEditedBookAuthor.visibility = View.VISIBLE

            ivEditorBookStatusRead.visibility = View.VISIBLE
            ivEditorBookStatusInProgress.visibility = View.VISIBLE
            ivEditorBookStatusToRead.visibility = View.VISIBLE
            tvFinished.visibility = View.VISIBLE
            tvInProgress.visibility = View.VISIBLE
            tvToRead.visibility = View.VISIBLE

            etEditedPagesNumber.visibility = View.VISIBLE
            rbEditedRating.visibility = View.VISIBLE
            btnEditFinishDate.visibility = View.VISIBLE
            btnEditStartDate.visibility = View.VISIBLE

            tvDateStartedTitle.visibility = View.VISIBLE
            tvDateFinishedTitle.visibility = View.VISIBLE

            fabSaveEditedBook.visibility = View.VISIBLE
            fabDeleteBook.visibility = View.VISIBLE

            etEditedBookOLID.visibility = View.VISIBLE
            etEditedBookISBN10.visibility = View.VISIBLE
            etEditedBookISBN13.visibility = View.VISIBLE

            tvEditedOLID.visibility = View.VISIBLE
            tvEditedISBN10.visibility = View.VISIBLE
            tvEditedISBN13.visibility = View.VISIBLE

            if (bookStartDateMs != null)
                ivClearStartDate.visibility = View.VISIBLE
            if (bookFinishDateMs != null)
                ivClearFinishDate.visibility = View.VISIBLE
        }

        btnEditStartDate.setOnClickListener {
            it.hideKeyboard()

            dpEditBookStartDate.visibility = View.VISIBLE
            btnEditorSaveStartDate.visibility = View.VISIBLE
            btnEditorCancelStartDate.visibility = View.VISIBLE
            btnEditorSaveStartDate.isClickable = true
            btnEditorCancelStartDate.isClickable = true

            etEditedBookTitle.visibility = View.GONE
            etEditedBookAuthor.visibility = View.GONE

            ivEditorBookStatusRead.visibility = View.GONE
            ivEditorBookStatusInProgress.visibility = View.GONE
            ivEditorBookStatusToRead.visibility = View.GONE
            tvFinished.visibility = View.GONE
            tvInProgress.visibility = View.GONE
            tvToRead.visibility = View.GONE

            etEditedPagesNumber.visibility = View.GONE
            rbEditedRating.visibility = View.GONE
            btnEditFinishDate.visibility = View.GONE
            btnEditStartDate.visibility = View.GONE

            tvDateStartedTitle.visibility = View.GONE
            tvDateFinishedTitle.visibility = View.GONE

            fabSaveEditedBook.visibility = View.GONE
            fabDeleteBook.visibility = View.GONE

            etEditedBookOLID.visibility = View.GONE
            etEditedBookISBN10.visibility = View.GONE
            etEditedBookISBN13.visibility = View.GONE

            tvEditedOLID.visibility = View.GONE
            tvEditedISBN10.visibility = View.GONE
            tvEditedISBN13.visibility = View.GONE

            ivClearStartDate.visibility = View.GONE
            ivClearFinishDate.visibility = View.GONE
        }

        btnEditorSaveStartDate.setOnClickListener {
            bookStartDateMs = getDateFromDatePickerInMillis(dpEditBookStartDate)

            dpEditBookStartDate.visibility = View.GONE
            btnEditorSaveStartDate.visibility = View.GONE
            btnEditorCancelStartDate.visibility = View.GONE
            btnEditorSaveStartDate.isClickable = false
            btnEditorCancelStartDate.isClickable = false

            etEditedBookTitle.visibility = View.VISIBLE
            etEditedBookAuthor.visibility = View.VISIBLE

            ivEditorBookStatusRead.visibility = View.VISIBLE
            ivEditorBookStatusInProgress.visibility = View.VISIBLE
            ivEditorBookStatusToRead.visibility = View.VISIBLE
            tvFinished.visibility = View.VISIBLE
            tvInProgress.visibility = View.VISIBLE
            tvToRead.visibility = View.VISIBLE

            etEditedPagesNumber.visibility = View.VISIBLE
            rbEditedRating.visibility = View.VISIBLE
            btnEditStartDate.visibility = View.VISIBLE

            tvDateStartedTitle.visibility = View.VISIBLE
            if (whatIsClicked == Constants.BOOK_STATUS_READ) {
                tvDateFinishedTitle.visibility = View.VISIBLE
                btnEditFinishDate.visibility = View.VISIBLE

                if (bookFinishDateMs != null)
                    ivClearFinishDate.visibility = View.VISIBLE
            }

            fabSaveEditedBook.visibility = View.VISIBLE
            fabDeleteBook.visibility = View.VISIBLE

            etEditedBookOLID.visibility = View.VISIBLE
            etEditedBookISBN10.visibility = View.VISIBLE
            etEditedBookISBN13.visibility = View.VISIBLE

            tvEditedOLID.visibility = View.VISIBLE
            tvEditedISBN10.visibility = View.VISIBLE
            tvEditedISBN13.visibility = View.VISIBLE

            btnEditStartDate.text = bookStartDateMs?.let { it1 -> convertLongToTime(it1) }

            if (bookStartDateMs != null)
                ivClearStartDate.visibility = View.VISIBLE
        }

        btnEditorCancelStartDate.setOnClickListener {
            dpEditBookStartDate.visibility = View.GONE
            btnEditorSaveStartDate.visibility = View.GONE
            btnEditorCancelStartDate.visibility = View.GONE
            btnEditorSaveStartDate.isClickable = false
            btnEditorCancelStartDate.isClickable = false

            etEditedBookTitle.visibility = View.VISIBLE
            etEditedBookAuthor.visibility = View.VISIBLE

            ivEditorBookStatusRead.visibility = View.VISIBLE
            ivEditorBookStatusInProgress.visibility = View.VISIBLE
            ivEditorBookStatusToRead.visibility = View.VISIBLE
            tvFinished.visibility = View.VISIBLE
            tvInProgress.visibility = View.VISIBLE
            tvToRead.visibility = View.VISIBLE

            etEditedPagesNumber.visibility = View.VISIBLE
            rbEditedRating.visibility = View.VISIBLE
            btnEditStartDate.visibility = View.VISIBLE

            tvDateStartedTitle.visibility = View.VISIBLE

            if (whatIsClicked == Constants.BOOK_STATUS_READ) {
                tvDateFinishedTitle.visibility = View.VISIBLE
                btnEditFinishDate.visibility = View.VISIBLE

                if (bookFinishDateMs != null)
                    ivClearFinishDate.visibility = View.VISIBLE
            }

            fabSaveEditedBook.visibility = View.VISIBLE
            fabDeleteBook.visibility = View.VISIBLE

            etEditedBookOLID.visibility = View.VISIBLE
            etEditedBookISBN10.visibility = View.VISIBLE
            etEditedBookISBN13.visibility = View.VISIBLE

            tvEditedOLID.visibility = View.VISIBLE
            tvEditedISBN10.visibility = View.VISIBLE
            tvEditedISBN13.visibility = View.VISIBLE

            if (bookStartDateMs != null)
                ivClearStartDate.visibility = View.VISIBLE
        }

        ivClearStartDate.setOnClickListener {
            bookStartDateMs = null
            ivClearStartDate.visibility = View.GONE
            btnEditStartDate.text = getString(R.string.set)
        }

        ivClearFinishDate.setOnClickListener {
            bookFinishDateMs = null
            ivClearFinishDate.visibility = View.GONE
            btnEditFinishDate.text = getString(R.string.set)
        }

        fabSaveEditedBook.setOnClickListener {
            val bookTitle = etEditedBookTitle.text.toString()
            val bookAuthor = etEditedBookAuthor.text.toString()
            var bookRating = 0.0F
            val bookNumberOfPagesIntOrNull = etEditedPagesNumber.text.toString().toIntOrNull()
            var bookNumberOfPagesInt: Int

            var bookOLID = etEditedBookOLID.text.toString()
            if (bookOLID.isEmpty()) {
                bookOLID = Constants.DATABASE_EMPTY_VALUE
            }

            var bookISBN10 = etEditedBookISBN10.text.toString()
            if (bookISBN10.isEmpty()) {
                bookISBN10 = Constants.DATABASE_EMPTY_VALUE
            }

            var bookISBN13 = etEditedBookISBN13.text.toString()
            if (bookISBN13.isEmpty()) {
                bookISBN13 = Constants.DATABASE_EMPTY_VALUE
            }

            if (bookTitle.isNotEmpty()) {
                if (bookAuthor.isNotEmpty()) {
                    if (whatIsClicked != Constants.BOOK_STATUS_NOTHING) {
                            bookNumberOfPagesInt = when (bookNumberOfPagesIntOrNull) {
                                null -> 0
                                else -> bookNumberOfPagesIntOrNull
                            }

                                    if ((bookFinishDateMs != null && bookStartDateMs != null && bookStartDateMs!! < bookFinishDateMs!!)
                                        || whatIsClicked == Constants.BOOK_STATUS_IN_PROGRESS
                                        || whatIsClicked == Constants.BOOK_STATUS_TO_READ
                                        || bookFinishDateMs == null
                                        || bookStartDateMs == null ) {

                                        when (whatIsClicked) {
                                            Constants.BOOK_STATUS_READ -> {
                                                bookRating = rbEditedRating.rating
                                            }
                                            Constants.BOOK_STATUS_IN_PROGRESS -> {
                                                bookRating = 0.0F
                                                bookFinishDateMs = null
                                            }
                                            Constants.BOOK_STATUS_TO_READ -> {
                                                bookRating = 0.0F
                                                bookNumberOfPagesInt = 0
                                                bookStartDateMs = null
                                                bookFinishDateMs = null
                                            }
                                        }

                                            val REGEX_UNACCENT =
                                                "\\p{InCombiningDiacriticalMarks}+".toRegex()

                                            fun CharSequence.unaccent(): String {
                                                val temp =
                                                    Normalizer.normalize(this, Normalizer.Form.NFD)
                                                return REGEX_UNACCENT.replace(temp, "")
                                            }

                                            val bookStatus = whatIsClicked

                                            var newStartDate = bookStartDateMs.toString()
                                            var newFinishDate = bookFinishDateMs.toString()

                                            if (whatIsClicked == Constants.BOOK_STATUS_IN_PROGRESS || whatIsClicked == Constants.BOOK_STATUS_TO_READ) {
                                                newStartDate = Constants.DATABASE_EMPTY_VALUE
                                                newFinishDate = Constants.DATABASE_EMPTY_VALUE
                                            }

                                            viewModel.updateBook(
                                                book.id,
                                                bookTitle,
                                                bookAuthor,
                                                bookRating,
                                                bookStatus,
                                                book.bookPriority,
                                                newStartDate,
                                                newFinishDate,
                                                bookNumberOfPagesInt,
                                                bookTitle_ASCII = bookTitle.unaccent()
                                                    .replace("ł", "l", false),
                                                bookAuthor_ASCII = bookAuthor.unaccent()
                                                    .replace("ł", "l", false),
                                                false,
                                                book.bookCoverUrl,
                                                bookOLID,
                                                bookISBN10,
                                                bookISBN13
                                            )

                                            recalculateChallenges()
                                        } else {
                                            Snackbar.make(it, R.string.sbWarningStartDateMustBeBeforeFinishDate, Snackbar.LENGTH_SHORT).show()
                                        }
                    } else {
                        Snackbar.make(it, getString(R.string.sbWarningState), Snackbar.LENGTH_SHORT).show()
                    }
                } else {
                    Snackbar.make(it, getString(R.string.sbWarningAuthor), Snackbar.LENGTH_SHORT).show()
                }
            } else {
                Snackbar.make(it, getString(R.string.sbWarningTitle), Snackbar.LENGTH_SHORT).show()
            }
        }

        class UndoBookDeletion : View.OnClickListener {
            override fun onClick(view: View) {
                viewModel.updateBook(
                    book.id,
                    book.bookTitle,
                    book.bookAuthor,
                    book.bookRating,
                    book.bookStatus,
                    book.bookPriority,
                    book.bookStartDate,
                    book.bookFinishDate,
                    book.bookNumberOfPages,
                    book.bookTitle_ASCII,
                    book.bookAuthor_ASCII,
                    false,
                    book.bookCoverUrl,
                    book.bookOLID,
                    book.bookISBN10,
                    book.bookISBN13
                )
            }
        }

        fabDeleteBook.setOnClickListener{
            viewModel.updateBook(
                book.id,
                book.bookTitle,
                book.bookAuthor,
                book.bookRating,
                book.bookStatus,
                book.bookPriority,
                book.bookStartDate,
                book.bookFinishDate,
                book.bookNumberOfPages,
                book.bookTitle_ASCII,
                book.bookAuthor_ASCII,
                true,
                book.bookCoverUrl,
                book.bookOLID,
                book.bookISBN10,
                book.bookISBN13
            )
            recalculateChallenges()

            Snackbar.make(it, getString(R.string.bookDeleted), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.undo), UndoBookDeletion())
                .show()
        }
    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    fun View.showKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.toggleSoftInputFromWindow(windowToken, 0, 0)
    }

    fun showKeyboard(et: EditText, delay: Long) {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                val inputManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(et, 0)
            }
        }, delay)
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd MMM yyyy")
        return format.format(date)
    }

    fun getDateFromDatePickerInMillis(datePicker: DatePicker): Long? {
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year
        val calendar = Calendar.getInstance()
        calendar[year, month] = day
        return calendar.timeInMillis
    }

    fun getAccentColor(context: Context): Int {
        var accentColor = ContextCompat.getColor(context, R.color.green_500)

        val sharedPref = (activity as ListActivity).getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        var accent = sharedPref.getString(Constants.SHARED_PREFERENCES_KEY_ACCENT, Constants.THEME_ACCENT_DEFAULT).toString()

        when(accent){
            Constants.THEME_ACCENT_LIGHT_GREEN -> accentColor = ContextCompat.getColor(context, R.color.light_green)
            Constants.THEME_ACCENT_ORANGE_500 -> accentColor = ContextCompat.getColor(context, R.color.orange_500)
            Constants.THEME_ACCENT_CYAN_500 -> accentColor = ContextCompat.getColor(context, R.color.cyan_500)
            Constants.THEME_ACCENT_GREEN_500 -> accentColor = ContextCompat.getColor(context, R.color.green_500)
            Constants.THEME_ACCENT_BROWN_400 -> accentColor = ContextCompat.getColor(context, R.color.brown_400)
            Constants.THEME_ACCENT_LIME_500 -> accentColor = ContextCompat.getColor(context, R.color.lime_500)
            Constants.THEME_ACCENT_PINK_300 -> accentColor = ContextCompat.getColor(context, R.color.pink_300)
            Constants.THEME_ACCENT_PURPLE_500 -> accentColor = ContextCompat.getColor(context, R.color.purple_500)
            Constants.THEME_ACCENT_TEAL_500 -> accentColor = ContextCompat.getColor(context, R.color.teal_500)
            Constants.THEME_ACCENT_YELLOW_500 -> accentColor = ContextCompat.getColor(context, R.color.yellow_500)
        }
        return accentColor
    }

    fun convertLongToYear(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy")
        return format.format(date)
    }

    private fun recalculateChallenges() {
        viewModel.getSortedBooksByFinishDateDesc(Constants.BOOK_STATUS_READ)
            .observe(viewLifecycleOwner, Observer { books ->
                var year: Int
                var years = listOf<Int>()

                for (item in books) {
                    if (item.bookFinishDate != "null" && item.bookFinishDate != "none") {
                        year = convertLongToYear(item.bookFinishDate.toLong()).toInt()
                        if (year !in years) {
                            years = years + year
                        }
                    }
                }

                for (item_year in years) {
                    var booksInYear = 0

                    for (item_book in books) {
                        if (item_book.bookFinishDate != "none" && item_book.bookFinishDate != "null") {
                            year = convertLongToYear(item_book.bookFinishDate.toLong()).toInt()
                            if (year == item_year) {
                                booksInYear++
                            }
                        }
                    }
                    viewModel.updateYearsNumberOfBooks(item_year.toString(), booksInYear)
                }
            }
            )
        lifecycleScope.launch {
            delay(500L)
            view?.hideKeyboard()
            findNavController().popBackStack()
            findNavController().popBackStack()
        }
    }
}