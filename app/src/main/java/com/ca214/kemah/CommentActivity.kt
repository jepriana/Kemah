package com.ca214.kemah

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ca214.kemah.adapters.CommentListAdapter
import com.ca214.kemah.data.models.Comment
import com.ca214.kemah.data.repositories.CommentRepository
import com.ca214.kemah.utils.Constants.EXTRA_CAMPGROUND_ID
import com.ca214.kemah.utils.TokenManager
import okhttp3.internal.wait
import java.util.UUID

class CommentActivity : AppCompatActivity() {
    lateinit var  rvComments: RecyclerView
    lateinit var commentRepository: CommentRepository
    lateinit var tokenManager: TokenManager
    lateinit var campgroundId: UUID
    lateinit var userId: UUID
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        rvComments = findViewById(R.id.rv_comments)
        tokenManager = TokenManager(this)
        userId = UUID.fromString(tokenManager.getUserId())
        commentRepository = CommentRepository(tokenManager.getAccessToken().toString())

        val id = intent.getStringExtra(EXTRA_CAMPGROUND_ID)
        if (id != null) {
            campgroundId = UUID.fromString(id)
            showCommentList()
        }

        val actionBar = supportActionBar
        actionBar?.title = "Campground Review"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showLoading(msg: String) {
        progressDialog = ProgressDialog.show(this, "Loading Comment", msg)
    }

    private fun showCommentList() {
        val commentsLiveData = commentRepository.getComments(campgroundId)
        val userIdString = tokenManager.getUserId()
        showLoading("Loading campground comments")
        commentsLiveData.observe(this, Observer { comments ->
            progressDialog?.dismiss()
            if (!comments.isNullOrEmpty()) {
                val data = ArrayList<Comment>()
                data.addAll(comments)
                rvComments.layoutManager = LinearLayoutManager(this)
                val commentListAdapter = CommentListAdapter(data, campgroundId, UUID.fromString(userIdString))
                rvComments.adapter = commentListAdapter
            }
        }).runCatching {
            progressDialog?.dismiss()
        }
    }
}