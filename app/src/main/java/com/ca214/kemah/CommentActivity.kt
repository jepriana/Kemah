package com.ca214.kemah

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ca214.kemah.adapters.CampgroundListAdapter
import com.ca214.kemah.adapters.CommentListAdapter
import com.ca214.kemah.data.models.Campground
import com.ca214.kemah.data.models.Comment
import com.ca214.kemah.data.repositories.CommentRepository
import com.ca214.kemah.utils.Constants
import com.ca214.kemah.utils.TokenManager
import java.util.UUID

class CommentActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var commentRepository: CommentRepository
    var listComments = ArrayList<Comment>()
    private var progressDialog: ProgressDialog? = null
    private lateinit var campgroundId: UUID
    private lateinit var userId: UUID
    private lateinit var rvComments : RecyclerView
    lateinit var tokenManager: TokenManager
    lateinit var btnSend: Button
    lateinit var editReview: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        btnSend = findViewById(R.id.btn_send_review)
        editReview = findViewById(R.id.text_enter_review)
        rvComments = findViewById(R.id.rv_comments)
        commentRepository = CommentRepository()
        tokenManager = TokenManager(this@CommentActivity.applicationContext)

        val id = intent.getStringExtra(Constants.EXTRA_CAMPGROUND_ID)
        if (id != null) {
            campgroundId = UUID.fromString(id)
            Log.d("Campground ID", campgroundId.toString())
            showCommentList()
        }

        val actionBar = supportActionBar
        actionBar?.title = "Campground Review"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnSend.setOnClickListener(this)
    }
    private fun showLoading(msg: String) {
        progressDialog = ProgressDialog.show(this, "Loading Data", msg)
    }

    private fun showCommentList() {
        val commentsLiveData = commentRepository.getComments(campgroundId)
        val userIdString = tokenManager.getUserId()
        showLoading("Loading data user comments")
        commentsLiveData.observe(this, Observer { comments ->
            listComments.clear()
            listComments.addAll(
                comments
            )
            progressDialog?.dismiss()

            rvComments.layoutManager = LinearLayoutManager(this)
            val commentListAdapter = CommentListAdapter(listComments, campgroundId, UUID.fromString(userIdString))
            rvComments.adapter = commentListAdapter
        })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_send_review -> {
                // Get data from edit text
                val inputComment = editReview.text.toString().trim()
                var invalidEntries = false

                // Validate data
                if (inputComment.isEmpty()) {
                    invalidEntries = true
                    inputComment.setError("Review content is required")
                }

                // Penyimpanan data campground
                if (!invalidEntries) {
                    // Membuat object campground baru
                    var newComment = Comment(
                        id = null,
                        creatorUsername = null,
                        creatorId = null,
                        content = inputComment
                    )
                    showLoading("Saving comment")
                    commentRepository.addComment(campgroundId, newComment).observe(this, Observer {
                                createdData -> if (createdData != null) {
                            progressDialog?.dismiss()
                            editReview.text.clear()
                            showCommentList()
                        }
                        else {
                            progressDialog?.dismiss()
                            Toast.makeText(
                                this,
                                "Failed to submit review",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        })
                }
            }
        }
    }
}

private fun String.setError(s: String) {

}
