package com.example.mybooks.ui.bookslist.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.example.mybooks.R
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.dialog_sort_books.*


class SortBooksDialog(context: Context, var sortBooksDialogListener: SortBooksDialogListener) : AppCompatDialog(context) {

    var whatIsClicked: String = "nothing"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_sort_books)

        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        ivSortTitleDesc.setOnClickListener {
            ivSortTitleDesc.setColorFilter(ContextCompat.getColor(context, R.color.orange_300), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortTitleAsc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortAuthorDesc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortAuthorAsc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortRatingDesc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortRatingAsc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            whatIsClicked = "ivSortTitleDesc"
        }

        ivSortTitleAsc.setOnClickListener {
            ivSortTitleDesc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortTitleAsc.setColorFilter(ContextCompat.getColor(context, R.color.orange_300), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortAuthorDesc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortAuthorAsc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortRatingDesc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortRatingAsc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            whatIsClicked = "ivSortTitleAsc"
        }

        ivSortAuthorDesc.setOnClickListener {
            ivSortTitleDesc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortTitleAsc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortAuthorDesc.setColorFilter(ContextCompat.getColor(context, R.color.orange_300), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortAuthorAsc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortRatingDesc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortRatingAsc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            whatIsClicked = "ivSortAuthorDesc"
        }

        ivSortAuthorAsc.setOnClickListener {
            ivSortTitleDesc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortTitleAsc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortAuthorDesc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortAuthorAsc.setColorFilter(ContextCompat.getColor(context, R.color.orange_300), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortRatingDesc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortRatingAsc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            whatIsClicked = "ivSortAuthorAsc"
        }

        ivSortRatingDesc.setOnClickListener {
            ivSortTitleDesc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortTitleAsc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortAuthorDesc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortAuthorAsc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortRatingDesc.setColorFilter(ContextCompat.getColor(context, R.color.orange_300), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortRatingAsc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            whatIsClicked = "ivSortRatingDesc"
        }

        ivSortRatingAsc.setOnClickListener {
            ivSortTitleDesc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortTitleAsc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortAuthorDesc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortAuthorAsc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortRatingDesc.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN)
            ivSortRatingAsc.setColorFilter(ContextCompat.getColor(context, R.color.orange_300), android.graphics.PorterDuff.Mode.SRC_IN)
            whatIsClicked = "ivSortRatingAsc"
        }

        btnSorterExecute.setOnClickListener {
            sortBooksDialogListener.onSaveButtonClicked(whatIsClicked)
            dismiss()
        }
    }
}
